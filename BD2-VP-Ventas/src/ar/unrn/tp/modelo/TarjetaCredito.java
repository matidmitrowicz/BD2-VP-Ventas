package ar.unrn.tp.modelo;

import java.sql.Date;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class TarjetaCredito {

	@Id
	@GeneratedValue
	protected Long id;

	private String numeroTarjeta;
	private String entidadBancaria;
	protected Date fechaVencimiento;
	private double montoTotalAGastar;

	protected TarjetaCredito() {
		super();
	}

	public TarjetaCredito(String numeroTarjeta, String entidadBancaria, LocalDate fechaVencimiento,
			double montoTotalAGastar) {

		this.numeroTarjeta = numeroTarjeta;
		this.entidadBancaria = entidadBancaria;
		this.fechaVencimiento = java.sql.Date.valueOf(fechaVencimiento);
		this.montoTotalAGastar = montoTotalAGastar;
	}

	public boolean validarFondos(double monto) {
		return this.montoTotalAGastar >= monto;
	}

	public boolean estaActiva() {
		return this.fechaVencimiento.after(java.sql.Date.valueOf(LocalDate.now()));
	}

	public void debitar(double monto) {
		if (estaActiva() && validarFondos(monto)) {
			this.montoTotalAGastar -= monto;
		} else {
			throw new RuntimeException("Tarjeta inactiva o saldo insuficiente");
		}
	}

	public String obtenerNumeroTarjeta() {
		return numeroTarjeta;
	}

	public String obtenerEntidadBancaria() {
		return entidadBancaria;
	}

	public Date obtenerFechaVencimiento() {
		return fechaVencimiento;
	}

	public double obtenerSaldoDisponible() {
		return montoTotalAGastar;
	}

	private Long getId() {
		return id;
	}

	private void setId(Long id) {
		this.id = id;
	}

	private String getNumeroTarjeta() {
		return numeroTarjeta;
	}

	private void setNumeroTarjeta(String numeroTarjeta) {
		this.numeroTarjeta = numeroTarjeta;
	}

	private String getEntidadBancaria() {
		return entidadBancaria;
	}

	private void setEntidadBancaria(String entidadBancaria) {
		this.entidadBancaria = entidadBancaria;
	}

	private Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	private void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	private double getMontoTotalAGastar() {
		return montoTotalAGastar;
	}

	private void setMontoTotalAGastar(double montoTotalAGastar) {
		this.montoTotalAGastar = montoTotalAGastar;
	}

	@Override
	public String toString() {
		return "TarjetaCredito [numeroTarjeta=" + numeroTarjeta + ", entidadBancaria=" + entidadBancaria
				+ ", fechaVencimiento=" + fechaVencimiento + ", montoTotalAGastar=" + montoTotalAGastar + "]";
	}

}
