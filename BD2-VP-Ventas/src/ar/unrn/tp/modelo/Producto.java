package ar.unrn.tp.modelo;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

@Entity
public class Producto {
	@Id
	@Column(name = "producto_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long idProducto;

	private String codigo;
	private String descripcion;
	private String marcaProducto;
	private double precio;
	@ManyToOne(fetch = FetchType.EAGER) // @ManyToOne > un producto una categoria
	@JoinColumn(name = "categoria_id")
	private Categoria categoria;

	@Version
	private Long version;

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

	private Long getVersion() {
		return version;
	}

	private void setVersion(Long version) {
		this.version = version;
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

	public void versionUpdate(Long version) {
		this.version = version;
	}

	public Map<String, Object> toMap() {
		return Map.of("idProducto", idProducto, "codigo", codigo, "descripcion", descripcion, "marca", marcaProducto,
				"precio", precio, "categoria", categoria.toMap(), "version", version);
	}

	@Override
	public String toString() {
		return "Producto [idProducto=" + idProducto + ", codigo=" + codigo + ", version=" + version + ", descripcion="
				+ descripcion + ", marcaProducto=" + marcaProducto + ", categoria=" + categoria + ", precio=" + precio
				+ "]";
	}

}
