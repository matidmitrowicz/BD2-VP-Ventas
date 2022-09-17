package ar.unrn.tp.jpa.servicios;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import ar.unrn.tp.api.ClienteService;
import ar.unrn.tp.modelo.Cliente;
import ar.unrn.tp.modelo.TarjetaCredito;

public class ClienteServiceJPA implements ClienteService {

	private String persistence;

	public ClienteServiceJPA(String persistence) {
		this.persistence = persistence;
	}

	@Override
	public void crearCliente(String nombre, String apellido, String dni, String email) {

		EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistence);
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();

			TypedQuery<Cliente> query = em.createQuery("select c from Cliente c where c.dni = :dni", Cliente.class);
			query.setParameter("dni", dni);
			List<Cliente> clientes = query.getResultList();

			if (clientes.size() != 0) {
				throw new RuntimeException("El DNI ingresado ya se encuentra registrado en el sistema.");
			}

			Cliente p = new Cliente(nombre, apellido, dni, email);
			em.persist(p);

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
	public void modificarCliente(Long idCliente, String nombre, String apellido, String dni, String email) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistence);
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();

			Cliente cliente = em.getReference(Cliente.class, idCliente); // Sirve para validar tmb ? <<<<<<<<<
			cliente.cambiarNombre(nombre);
			cliente.cambiarApellido(apellido);
			cliente.cambiarDni(dni);
			cliente.cambiarEmail(email);

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
	public void agregarTarjeta(Long idCliente, String nro, String marca, double saldo, LocalDate fechaVencimiento) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistence);
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();

			Cliente cliente = em.getReference(Cliente.class, idCliente);
			TarjetaCredito tarjeta = new TarjetaCredito(nro, marca, fechaVencimiento, saldo);
			cliente.agregarTarjeta(tarjeta);

			em.persist(cliente); // Es necesario ? <<<<

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
	public List<TarjetaCredito> listarTarjetas(Long idCliente) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistence);
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		List<TarjetaCredito> tarjetas = new ArrayList<TarjetaCredito>();

		try {
			tx.begin();

			Cliente cliente = em.getReference(Cliente.class, idCliente); // idCliente existe ?

			TypedQuery<TarjetaCredito> query = em
					.createQuery("select t from Cliente c join c.tarjetas t where c.id = :id", TarjetaCredito.class);
			query.setParameter("id", idCliente);
			tarjetas = query.getResultList();

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
		return tarjetas;
	}

}
