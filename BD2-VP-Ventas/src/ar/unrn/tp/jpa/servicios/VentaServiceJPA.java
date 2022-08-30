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
	public void realizarVenta(Long idCliente, List<Long> productos, Long idTarjeta) {
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
			if (productos == null || productos.size() == 0) {
				throw new RuntimeException("No se agrego ningun producto.");
			}
			// Genero la configuracion para la venta
			List<Promocion> promos = new ArrayList<Promocion>();
			TypedQuery<Promocion> promosBD = em.createQuery("select p from Promocion p", Promocion.class);
			promos = promosBD.getResultList();

			CarritoCompra carrito = new CarritoCompra(cliente, LocalDate.now());
			carrito.agregarPromo(promos);

			Producto prod = null;
			for (Long idProducto : productos) {
				prod = em.find(Producto.class, idProducto);
				carrito.addProduct(prod);
			}
			double totalVenta = calcularMonto(productos, idTarjeta);
			// El monto lo saco del carrito o de la funcion calcularMonto del servicio ?
			RegistroVenta venta = new RegistroVenta(LocalDate.now(), cliente, tarjeta.getEntidadBancaria(),
					carrito.getProductosSeleccionados(), totalVenta);
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
	public double calcularMonto(List<Long> productos, Long idTarjeta) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-objectdb");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		double total = 0;

		try {
			tx.begin();

			TarjetaCredito tarjeta = em.find(TarjetaCredito.class, idTarjeta);

			List<Promocion> promos = new ArrayList<Promocion>();
			TypedQuery<Promocion> promosBD = em.createQuery("select p from Promocion p", Promocion.class);
			promos = promosBD.getResultList();

			CarritoCompra carrito = new CarritoCompra(promos);
			carrito.setMedioDePago(tarjeta.getEntidadBancaria());
			Producto prod = null;

			for (Long idProducto : productos) {
				prod = em.find(Producto.class, idProducto);
				carrito.addProduct(prod);
			}
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
