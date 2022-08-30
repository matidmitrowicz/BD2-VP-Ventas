package ar.unrn.tp.api;

import java.util.List;

import ar.unrn.tp.modelo.Producto;

public interface ProductoService {

	// validar que sea una categoría existente y que codigo no se repita
	void crearProducto(String codigo, String descripcion, String marca, double precio, Long IdCategoría);

	// validar que sea un producto existente
	void modificarProducto(Long idProducto, String codigo, String descripcion, String marca, double precio,
			Long IdCategoría);

	// Devuelve todos los productos
	List<Producto> listarProductos();

	void crearCategoria(String nombre);

}
