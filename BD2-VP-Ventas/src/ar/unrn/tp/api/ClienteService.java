package ar.unrn.tp.api;

import java.time.LocalDate;
import java.util.List;

import ar.unrn.tp.modelo.TarjetaCredito;

public interface ClienteService {

	// validar que el dni no se repita
	void crearCliente(String nombre, String apellido, String dni, String email);

	// validar que sea un cliente existente
	void modificarCliente(Long idCliente, String nombre, String apellido, String dni, String email);

	// validar que sea un cliente existente
	void agregarTarjeta(Long idCliente, String nro, String marca, double saldo, LocalDate fechaVencimiento);

	// Devuelve las tarjetas de un cliente específico
	List<TarjetaCredito> listarTarjetas(Long idCliente);

}
