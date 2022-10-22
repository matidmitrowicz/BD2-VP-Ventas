package ar.unrn.tp.cache;

import java.util.List;

import ar.unrn.tp.modelo.RegistroVenta;

public interface RedisCacheService {

	List<RegistroVenta> retrieveLastSells(String cliendId);

	void inserLastSells(String cliendId, String json);

	void flushLastSellsCache(String cliendId);

	void clearAll();

}
