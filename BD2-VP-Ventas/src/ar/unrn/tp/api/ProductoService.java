package ar.unrn.tp.api;

import java.util.List;

import ar.unrn.tp.modelo.Producto;

public interface ProductoService {

	// validar que sea una categor�a existente y que codigo no se repita
	void crearProducto(String codigo, String descripcion, String marca, double precio, Long IdCategor�a);

	// validar que sea un producto existente
	void modificarProducto(Long idProducto, String codigo, String descripcion, String marca, double precio,
			Long IdCategor�a);

	// Devuelve todos los productos
	List<Producto> listarProductos();

	void crearCategoria(String nombre);

}
