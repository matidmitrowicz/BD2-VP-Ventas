package ar.unrn.tp.modelo;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Producto {
	@Id
	@GeneratedValue
	private Long idProducto;

	private String codigo;
	private String descripcion;
	private String marcaProducto;
	@ManyToOne(fetch = FetchType.EAGER)
	private Categoria categoria;
	private double precio;

	protected Producto() {

	}

	public Producto(String codigo, String descripcion, String marcaProducto, Categoria categoria, double precio) {
		if (esDatoVacio(codigo) || esDatoNulo(codigo)) {
			throw new RuntimeException("Campo codigo vacio");
		}
		if (esDatoVacio(descripcion) || esDatoNulo(descripcion)) {
			throw new RuntimeException("Campo descripcion vacio");
		}
		if (esDatoVacio(marcaProducto) || esDatoNulo(marcaProducto)) {
			throw new RuntimeException("Campo marca vacio");
		}
		if (esDatoNulo(categoria)) {
			throw new RuntimeException("Campo categoria vacio");
		}
		if ((precio <= 0) || esDatoNulo(precio)) {
			throw new RuntimeException("precio invalido");
		}
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.categoria = categoria;
		this.marcaProducto = marcaProducto;
		this.precio = precio;
	}

	private boolean esDatoVacio(String dato) {
		return dato.equals("");
	}

	private boolean esDatoNulo(Object dato) {
		return dato == null;
	}

	public void cambiarID(Long idProducto) {
		this.idProducto = idProducto;
	}

	public void cambiarCodigo(String codigo) {
		this.codigo = codigo;
	}

	public void cambiarDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void cambiarMarca(String marca) {
		this.marcaProducto = marca;
	}

	public void cambiarPrecio(double precio) {
		this.precio = precio;
	}

	public void cambiarCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	private Long getIdProducto() {
		return idProducto;
	}

	private void setIdProducto(Long idProducto) {
		this.idProducto = idProducto;
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

	private Categoria getCategoria() {
		return categoria;
	}

	private void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	private double getPrecio() {
		return precio;
	}

	private void setPrecio(double precio) {
		this.precio = precio;
	}

	public String verCodigo() {
		return this.codigo;
	}

	public String verDescripcion() {
		return this.descripcion;
	}

	public String verMarca() {
		return this.marcaProducto;
	}

	public double obtenerPrecio() {
		return this.precio;
	}

	@Override
	public String toString() {
		return "Producto [idProducto=" + idProducto + ", codigo=" + codigo + ", descripcion=" + descripcion
				+ ", marcaProducto=" + marcaProducto + ", categoria=" + categoria + ", precio=" + precio + "]";
	}

}
