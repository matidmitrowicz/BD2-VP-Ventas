package ar.unrn.tp.main;

import redis.clients.jedis.Jedis;

public class JedisTest {

	public static void main(String[] args) {
		Jedis jedis = new Jedis("localhost", 6379);
		String ultimasVentasJson = jedis.get("user2");
		System.out.println(ultimasVentasJson);
		jedis.close();
	}

}
