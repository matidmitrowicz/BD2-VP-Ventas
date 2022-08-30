package ar.unrn.tp.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Cliente {

	@Id
	@GeneratedValue
	private Long id;

	private String nombre;
	private String apellido;
	private String dni;
	private String email;

	@OneToMany(cascade = CascadeType.PERSIST)
	private List<TarjetaCredito> tarjetas;

	protected Cliente() {

	}

	public Cliente(String nombre, String apellido, String dni, String email) {
		if (esDatoVacio(nombre) || esDatoNulo(nombre)) {
			throw new RuntimeException("Se intento crear un cliente sin nombre");
		}
		if (esDatoVacio(apellido) || esDatoNulo(apellido)) {
			throw new RuntimeException("Se intento crear un cliente sin apellido");
		}
		if (esDatoVacio(dni) || esDatoNulo(dni)) {
			throw new RuntimeException("Se intento crear un cliente sin DNI");
		}
		if (!isValidEmailAddress(email)) {
			throw new RuntimeException("Se intento crear un cliente con un email invalido");
		}
		this.nombre = nombre;
		this.apellido = apellido;
		this.dni = dni;
		this.email = email;
		this.tarjetas = new ArrayList<TarjetaCredito>();
	}

	private boolean esDatoVacio(String dato) {
		return dato.equals("");
	}

	private boolean esDatoNulo(Object dato) {
		return dato == null;
	}

	// https://stackoverflow.com/questions/624581/what-is-the-best-java-email-address-validation-method
	public boolean isValidEmailAddress(String email) {
		String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
		java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
		java.util.regex.Matcher m = p.matcher(email);
		return m.matches();
	}

	public boolean agregarTarjeta(TarjetaCredito tarjeta) {
		return this.tarjetas.add(tarjeta);
	}

	public boolean eliminarTarjeta(TarjetaCredito tarjeta) {
		return this.tarjetas.remove(tarjeta);
	}

	public void cambiarNombre(String nombre) {
		if (esDatoVacio(nombre) || esDatoNulo(nombre)) {
			throw new RuntimeException("Se intento crear un cliente sin nombre");
		}
		this.nombre = nombre;
	}

	public void cambiarApellido(String apellido) {
		if (esDatoVacio(apellido) || esDatoNulo(apellido)) {
			throw new RuntimeException("Se intento crear un cliente sin apellido");
		}
		this.apellido = apellido;
	}

	public void cambiarDni(String dni) {
		if (esDatoVacio(dni) || esDatoNulo(dni)) {
			throw new RuntimeException("Se intento crear un cliente sin DNI");
		}
		this.dni = dni;
	}

	public void cambiarEmail(String email) {
		if (!isValidEmailAddress(email)) {
			throw new RuntimeException("Se intento crear un cliente con un email invalido");
		}
		this.email = email;
	}

	public TarjetaCredito obtenerTarjetaEntidad(String metodoPago) {
		TarjetaCredito tarjeta = null;

		for (TarjetaCredito tarjetaCredito : tarjetas) {
			if (tarjetaCredito.getEntidadBancaria().equals(metodoPago)) {
				tarjeta = tarjetaCredito;
			}
		}

		return tarjeta;
	}

	public TarjetaCredito getTarjetaByID(Long idTarjeta) {
		for (TarjetaCredito tarjetaCliente : tarjetas) {
			if (tarjetaCliente.getIdCobrable().equals(idTarjeta)) {
				return tarjetaCliente;
			}
		}

		throw new RuntimeException("El cliente no posee la tarjeta");
	}

	private Long getId() {
		return id;
	}

	private void setId(Long id) {
		this.id = id;
	}

	private String getNombre() {
		return nombre;
	}

	private void setNombre(String nombre) {
		this.nombre = nombre;
	}

	private String getApellido() {
		return apellido;
	}

	private void setApellido(String apellido) {
		this.apellido = apellido;
	}

	private String getDni() {
		return dni;
	}

	private void setDni(String dni) {
		this.dni = dni;
	}

	private String getEmail() {
		return email;
	}

	private void setEmail(String email) {
		this.email = email;
	}

	private List<TarjetaCredito> getTarjetas() {
		return tarjetas;
	}

	private void setTarjetas(List<TarjetaCredito> tarjetas) {
		this.tarjetas = tarjetas;
	}

	public boolean tarjetaValida(TarjetaCredito tarjeta) {
		for (TarjetaCredito tarjetaCliente : tarjetas) {
			if (tarjetaCliente.equals(tarjeta)) {
				return true;
			}
		}
		return false;
	}

}
