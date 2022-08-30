package ar.unrn.tp.modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Categoria {

	@Id
	@GeneratedValue
	private Long idCategoria;

	private String nombre; // Ropa deportiva, Calzado, etc.

	protected Categoria() {
		super();
	}

	public Categoria(String nombreCategoria) {
		if (esDatoVacio(nombreCategoria) || esDatoNulo(nombreCategoria)) {
			throw new RuntimeException("Se intento crear una categoria sin nombre");
		}
		this.nombre = nombreCategoria;
	}

	private boolean esDatoVacio(String dato) {
		return dato.equals("");
	}

	private boolean esDatoNulo(Object dato) {
		return dato == null;
	}

	private Long getIdCategoria() {
		return idCategoria;
	}

	private void setIdCategoria(Long idCategoria) {
		this.idCategoria = idCategoria;
	}

	private String getNombre() {
		return nombre;
	}

	private void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Override
	public String toString() {
		return "Categoria [idCategoria=" + idCategoria + ", nombre=" + nombre + "]";
	}

}
