package ar.unrn.tp.cache;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import ar.unrn.tp.modelo.RegistroVenta;
import redis.clients.jedis.Jedis;

public class RedisCacheServiceImpl implements RedisCacheService {

//	private String host = "127.0.0.1";
//	private int port = 6379;

	private final Gson gson = new Gson();

	private Jedis acquireJedisInstance() {

		return new Jedis("localhost", 6379);
	}

	private void releaseJedisInstance(Jedis jedis) {
		jedis.close();
	}

	@Override
	public List<RegistroVenta> retrieveLastSells(String cliendId) {
		Jedis jedis = null;

		try {

			jedis = acquireJedisInstance();

			String ultimasVentasJson = jedis.get(cliendId);

			if (ultimasVentasJson != null && !ultimasVentasJson.isEmpty()) {
				System.out.println("Recuperando ventas de caché");
				return gson.fromJson(ultimasVentasJson, new TypeToken<List<RegistroVenta>>() {
				}.getType());
			}

		} catch (Exception e) {
			releaseJedisInstance(jedis);
			throw new RuntimeException("Error al recuperar datos de caché", e);

		} finally {
			releaseJedisInstance(jedis);
		}

		return null;
	}

	@Override
	public void inserLastSells(String cliendId, String json) {
		Jedis jedis = null;
		try {
			jedis = acquireJedisInstance();
			jedis.set(cliendId, json);
		} catch (Exception e) {
			releaseJedisInstance(jedis);
			throw new RuntimeException("Error al recuperar datos de caché", e);
		} finally {
			releaseJedisInstance(jedis);
		}

	}

	@Override
	public void flushLastSellsCache(String cliendId) {
		Jedis jedis = null;
		try {

			jedis = acquireJedisInstance();
			jedis.del(cliendId);

		} catch (Exception e) {
			releaseJedisInstance(jedis);
			throw new RuntimeException(e);

		} finally {
			releaseJedisInstance(jedis);
		}

	}

	@Override
	public void clearAll() {
		Jedis jedis = null;
		try {

			jedis = acquireJedisInstance();
			jedis.flushAll();

		} catch (Exception e) {
			releaseJedisInstance(jedis);
			throw new RuntimeException(e);

		} finally {
			releaseJedisInstance(jedis);
		}

	}

}
