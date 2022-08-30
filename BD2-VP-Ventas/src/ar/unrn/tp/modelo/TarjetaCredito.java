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
		super();

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

	public Long getIdCobrable() {
		return id;
	}

	public String getNumeroTarjeta() {
		return numeroTarjeta;
	}

	public String getEntidadBancaria() {
		return entidadBancaria;
	}

	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	public double getMontoTotalAGastar() {
		return montoTotalAGastar;
	}

	@Override
	public String toString() {
		return "TarjetaCredito [numeroTarjeta=" + numeroTarjeta + ", entidadBancaria=" + entidadBancaria
				+ ", fechaVencimiento=" + fechaVencimiento + ", montoTotalAGastar=" + montoTotalAGastar + "]";
	}

}
