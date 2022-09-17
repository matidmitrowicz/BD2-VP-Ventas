package ar.unrn.tp.jpa.servicios;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import ar.unrn.tp.api.ProductoService;
import ar.unrn.tp.modelo.Categoria;
import ar.unrn.tp.modelo.Producto;

public class ProductoServiceJPA implements ProductoService {

	private String persistence;

	public ProductoServiceJPA(String persistence) {
		this.persistence = persistence;
	}

	@Override
	public void crearProducto(String codigo, String descripcion, String marca, double precio, Long IdCategoría) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistence);
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			// validar que sea una categoría existente y que codigo no se repita
			TypedQuery<Producto> query = em.createQuery("select p from Producto p where p.codigo = :codigo",
					Producto.class);
			query.setParameter("codigo", codigo);

			List<Producto> productos = new ArrayList<Producto>();
			productos = query.getResultList();
			if (productos.size() != 0) {
				throw new RuntimeException("El producto con ese codigo ya existe.");
			}

			Categoria categoria = em.find(Categoria.class, IdCategoría);

			Producto producto = new Producto(codigo, descripcion, marca, categoria, precio);
			em.persist(producto);

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
	public void modificarProducto(Long idProducto, String codigo, String descripcion, String marca, double precio,
			Long IdCategoría) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistence);
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Categoria categoria = em.getReference(Categoria.class, IdCategoría);
			Producto producto = em.getReference(Producto.class, idProducto);

			producto.cambiarID(idProducto);
			producto.cambiarCodigo(codigo);
			producto.cambiarDescripcion(descripcion);
			producto.cambiarMarca(marca);
			producto.cambiarPrecio(precio);
			producto.cambiarCategoria(categoria);

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
	public List<Producto> listarProductos() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistence);
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		List<Producto> productos = new ArrayList<Producto>();
		try {
			tx.begin();

			TypedQuery<Producto> query = em.createQuery("select p from Producto p", Producto.class);

			productos = query.getResultList();

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
		return productos;
	}

	@Override
	public void crearCategoria(String nombre) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistence);
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();

			TypedQuery<Categoria> query = em.createQuery("select c from Categoria c where c.nombre = :nombre",
					Categoria.class);
			query.setParameter("nombre", nombre);
			List<Categoria> categorias = query.getResultList();
			if (categorias.size() != 0) {
				throw new RuntimeException("La categoria con ese nombre ya existe.");
			}

			Categoria type = new Categoria(nombre);

			em.persist(type);

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
