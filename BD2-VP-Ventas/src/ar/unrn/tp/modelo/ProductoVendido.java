package ar.unrn.tp.modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ProductoVendido {

	@Id
	@GeneratedValue
	private Long id;

	private String codigo;
	private String descripcion;
	private String marcaProducto;
	private double precio;

	protected ProductoVendido() {

	}

	public ProductoVendido(Producto prod) {

		this.codigo = prod.verCodigo();
		this.descripcion = prod.verDescripcion();
		this.marcaProducto = prod.verMarca();
		this.precio = prod.obtenerPrecio();
	}

	private Long getId() {
		return id;
	}

	private void setId(Long id) {
		this.id = id;
	}

	private String getCodigo() {
		return codigo;
	}

	private void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	private String getDescripcion() {
		return descripcion;
	}

	private void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	private String getMarcaProducto() {
		return marcaProducto;
	}

	private void setMarcaProducto(String marcaProducto) {
		this.marcaProducto = marcaProducto;
	}

	private double getPrecio() {
		return precio;
	}

	private void setPrecio(double precio) {
		this.precio = precio;
	}

	@Override
	public String toString() {
		return "ProductoVendido [id=" + id + ", codigo=" + codigo + ", descripcion=" + descripcion + ", marcaProducto="
				+ marcaProducto + ", precio=" + precio + "]";
	}

}
