package ar.unrn.tp.jpa.servicios;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import ar.unrn.tp.api.VentaService;
import ar.unrn.tp.excepciones.DebitarCardException;
import ar.unrn.tp.modelo.CarritoCompra;
import ar.unrn.tp.modelo.Cliente;
import ar.unrn.tp.modelo.NumberYearVenta;
import ar.unrn.tp.modelo.Producto;
import ar.unrn.tp.modelo.Promocion;
import ar.unrn.tp.modelo.RegistroVenta;
import ar.unrn.tp.modelo.TarjetaCredito;
import redis.clients.jedis.Jedis;

public class VentaServiceJPA implements VentaService {

	private String persistence;
//	private RedisCacheService redisCacheService;

	public VentaServiceJPA(String persistence) {
		this.persistence = persistence;

	}

	@Override
	public void realizarVenta(Long idCliente, List<Long> productosID, Long idTarjeta) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistence);
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();

			// Validar cliente y tarjeta
			Cliente cliente = em.find(Cliente.class, idCliente);
			TarjetaCredito tarjeta = em.find(TarjetaCredito.class, idTarjeta);
			if (!cliente.tarjetaValida(tarjeta)) {
				throw new RuntimeException("La tarjeta no le pertenece al cliente");
			}
			// Lista de productos no vacia
			if (productosID == null || productosID.size() == 0) {
				throw new RuntimeException("No se agrego ningun producto.");
			}

			// Me cargo las promos activas
			List<Promocion> promosActivas = new ArrayList<Promocion>();
			TypedQuery<Promocion> promosAll = em.createQuery(
					"select p from Promocion p where :fecha between p.fechaInicio and p.fechaFin", Promocion.class);
			promosAll.setParameter("fecha", java.sql.Date.valueOf(LocalDate.now()));
			promosActivas = promosAll.getResultList();

			// Armo la lista de productos vendidos
			TypedQuery<Producto> productosFromList = em
					.createQuery("select prod from Producto prod where prod.idProducto in :prodIDList", Producto.class);
			productosFromList.setParameter("prodIDList", productosID);
			List<Producto> productosElegidos = productosFromList.getResultList();

			CarritoCompra carrito = new CarritoCompra(cliente, promosActivas);
			carrito.agregarListaProducto(productosElegidos);

			RegistroVenta venta = carrito.finalizarVenta(tarjeta);

			// Manejar la concurrencia con SELECT FOR UPDATE EN JPA --> PESSIMISTIC_WRITE

			LocalDateTime currentDate = LocalDateTime.now();
			int currentYear = currentDate.getYear();

			TypedQuery<NumberYearVenta> query = em.createQuery("from NumberYearVenta where year = :currentYear",
					NumberYearVenta.class);
			query.setParameter("currentYear", currentYear);
			query.setLockMode(LockModeType.PESSIMISTIC_WRITE);

			// Me devuelve algo -> uso el result, sino devuelvo nada -> nueva instacia

			NumberYearVenta naVenta = null;
			try {
				naVenta = query.getSingleResult();
				System.out.println(naVenta.toString());
			} catch (NoResultException e) {
				naVenta = new NumberYearVenta(); // Sin resultados --> nueva instancia
			}

			System.out.println("N-YYYY venta : " + naVenta.formatoNumYear());
			venta.setNumberYearFormat(naVenta.formatoNumYear()); // N-YYYY

			em.persist(venta);
			naVenta.nextValue();
			em.persist(naVenta);

			// Borrar caché del cliente, para generar una nueva
			Jedis jedis = new Jedis("localhost", 6379);
			jedis.del(String.valueOf(idCliente));
			jedis.close();

			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			throw new RuntimeException(e);
		} finally {
			if (em != null && em.isOpen())
				em.close();
			if (emf != null)
				emf.close();
		}

	}

	@Override
	public double calcularMonto(List<Long> productosID, Long idTarjeta) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistence);
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		double total = 0;

		try {
			tx.begin();
			TarjetaCredito tarjeta = em.find(TarjetaCredito.class, idTarjeta);
			if (tarjeta == null) {
				throw new DebitarCardException("Tarjeta no valida.");
			}
			// Armo la lista de productos comprados | query => menor conexiones a la bd
			TypedQuery<Producto> productosFromList = em
					.createQuery("select prod from Producto prod where prod.idProducto in :prodIDList", Producto.class);
			productosFromList.setParameter("prodIDList", productosID);
			List<Producto> productosElegidos = productosFromList.getResultList();

			// Me cargo las promos activas
			List<Promocion> promosActivas = new ArrayList<Promocion>();
			TypedQuery<Promocion> promosAll = em.createQuery(
					"select p from Promocion p where :fecha between p.fechaInicio and p.fechaFin", Promocion.class);
			promosAll.setParameter("fecha", java.sql.Date.valueOf(LocalDate.now()));
			promosActivas = promosAll.getResultList();

			// Armo el carrito con las promos+tarjeta+productos
			CarritoCompra carrito = new CarritoCompra(promosActivas);
			carrito.setMedioDePago(tarjeta.obtenerEntidadBancaria());
			carrito.agregarListaProducto(productosElegidos);

			total = carrito.montoTotal();

			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			throw new RuntimeException(e);
		} finally {
			if (em != null && em.isOpen())
				em.close();
			if (emf != null)
				emf.close();
		}
		return total;
	}

	@Override
	public List<RegistroVenta> ventas() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistence);
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		List<RegistroVenta> ventas = new ArrayList<RegistroVenta>();
		try {
			tx.begin();
			TypedQuery<RegistroVenta> query = em.createQuery("select rv from RegistroVenta rv", RegistroVenta.class);
			ventas = query.getResultList();

			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			throw new RuntimeException(e);
		} finally {
			if (em != null && em.isOpen())
				em.close();
			if (emf != null)
				emf.close();
		}
		return ventas;
	}

	@Override
	public List<RegistroVenta> ultimasVentas(Long idCliente) {

		Gson gson = new Gson();
		Jedis jedis;
		List<RegistroVenta> ultimasVentas = null;

		jedis = new Jedis("localhost", 6379);
		String ultimasVentasJson = jedis.get(String.valueOf(idCliente));
		jedis.close();

		// Si existe la caché para el idCliente, recupero las ventas
		if (ultimasVentasJson != null && !ultimasVentasJson.isEmpty()) {
			ultimasVentas = new ArrayList<RegistroVenta>();
			ultimasVentas = gson.fromJson(ultimasVentasJson, new TypeToken<List<RegistroVenta>>() {
			}.getType());
			System.out.println("Ventas recuperadas de caché");
		}

		// Si no está, las busco en la bd de mysql y las agrego a la caché
		if (ultimasVentas == null) {
			System.out.println("No hay caché, las recupero de mysql");
			EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistence);
			EntityManager em = emf.createEntityManager();
			EntityTransaction tx = em.getTransaction();
			ultimasVentas = new ArrayList<RegistroVenta>();
			try {
				System.out.println("Buscando ventas en MySQL..");
				tx.begin();
				TypedQuery<RegistroVenta> query = em.createQuery(
						"select rv from RegistroVenta rv WHERE rv.cliente.id = :client ORDER BY rv.numYearId DESC",
						RegistroVenta.class);
				query.setParameter("client", idCliente);
				query.setMaxResults(3); // Ultimas 3
				ultimasVentas = query.getResultList();
				tx.commit();
				System.out.println("Ventas retornadas..");

				// Luego de obtener las ventas, las agrego a la caché
				jedis = new Jedis("localhost", 6379);
				jedis.set(String.valueOf(idCliente), gson.toJson(ultimasVentas));
				jedis.close();

			} catch (Exception e) {
				tx.rollback();
				throw new RuntimeException("ultimas ventas caché");
			} finally {
				if (em != null && em.isOpen())
					em.close();
				if (emf != null)
					emf.close();
			}
		}

		return ultimasVentas;
	}

}
