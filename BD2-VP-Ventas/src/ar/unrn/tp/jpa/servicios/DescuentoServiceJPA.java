package ar.unrn.tp.jpa.servicios;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import ar.unrn.tp.api.DescuentoService;
import ar.unrn.tp.modelo.PromocionMarcaProducto;
import ar.unrn.tp.modelo.PromocionMedioDePago;

public class DescuentoServiceJPA implements DescuentoService {

	// Promocion para Tarjetas de Credito
	@Override
	public void crearDescuentoSobreTotal(String marcaTarjeta, LocalDate fechaDesde, LocalDate fechaHasta,
			double porcentaje) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-objectdb");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();

			TypedQuery<PromocionMedioDePago> query = em.createQuery(
					"select p from PromocionMedioDePago p where p.promoMedioDePago = :marcaTarjeta and p.fechaInicio == :fechaDesde and p.fechaFin == :fechaHasta",
					PromocionMedioDePago.class);
			query.setParameter("marcaTarjeta", marcaTarjeta);
			query.setParameter("fechaDesde", fechaDesde);
			query.setParameter("fechaHasta", fechaHasta);
			List<PromocionMedioDePago> promosTarjetas = query.getResultList();

			if (promosTarjetas.size() > 0) {
				throw new RuntimeException("La promo para esa tarjeta durante esas fechas ya se encuentra registrada.");
			}

			PromocionMedioDePago promoTarjeta = new PromocionMedioDePago(fechaDesde, fechaHasta, porcentaje,
					marcaTarjeta);

			em.persist(promoTarjeta);

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

	// Promocion para Marcas de productos
	@Override
	public void crearDescuento(String marcaProducto, LocalDate fechaDesde, LocalDate fechaHasta, double porcentaje) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-objectdb");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();

			TypedQuery<PromocionMarcaProducto> query = em.createQuery(
					"select p from PromocionMarcaProducto p where p.marcaEnDescuento = :marcaProducto and p.fechaInicio == :fechaDesde and p.fechaFin == :fechaHasta",
					PromocionMarcaProducto.class);
			query.setParameter("marcaProducto", marcaProducto);
			query.setParameter("fechaDesde", fechaDesde);
			query.setParameter("fechaHasta", fechaHasta);
			List<PromocionMarcaProducto> promosTarjetas = query.getResultList();

			if (promosTarjetas.size() > 0) {
				throw new RuntimeException("La promo para esa marca durante esas fechas ya se encuentra registrada.");
			}

			PromocionMarcaProducto promoMarca = new PromocionMarcaProducto(fechaDesde, fechaHasta, porcentaje,
					marcaProducto);

			em.persist(promoMarca);

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

}
