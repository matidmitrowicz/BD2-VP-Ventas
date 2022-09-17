package ar.unrn.tp.modelo;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Categoria {

	@Id
	@Column(name = "categoria_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
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

	public Map<String, Object> toMap() {
		return Map.of("idCategoria", idCategoria, "nombre", nombre);
	}

	@Override
	public String toString() {
		return "Categoria [Nombre=" + nombre + "]";
	}

}
