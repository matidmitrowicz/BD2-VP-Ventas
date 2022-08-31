package ar.unrn.tp.jpa.servicios;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import ar.unrn.tp.api.VentaService;
import ar.unrn.tp.modelo.CarritoCompra;
import ar.unrn.tp.modelo.Cliente;
import ar.unrn.tp.modelo.Producto;
import ar.unrn.tp.modelo.Promocion;
import ar.unrn.tp.modelo.RegistroVenta;
import ar.unrn.tp.modelo.TarjetaCredito;

public class VentaServiceJPA implements VentaService {

	@Override
	public void realizarVenta(Long idCliente, List<Long> productosID, Long idTarjeta) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-objectdb");
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
			em.persist(venta);

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
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-objectdb");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		double total = 0;

		try {
			tx.begin();
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
			TarjetaCredito tarjeta = em.find(TarjetaCredito.class, idTarjeta);
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
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-objectdb");
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

}
