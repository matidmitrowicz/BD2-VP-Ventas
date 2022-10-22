package ar.unrn.tp.modelo;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import ar.unrn.tp.excepciones.DebitarCardException;

@Entity
@Table(name = "tarjetas_credito")
public class TarjetaCredito {

	@Id
	@Column(name = "tarjeta_id")
//	@GeneratedValue(strategy = GenerationType.AUTO)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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

	public void debitar(double monto) throws DebitarCardException {
		if (estaActiva() && validarFondos(monto)) {
			this.montoTotalAGastar -= monto;
		} else {
			throw new DebitarCardException("Tarjeta inactiva o saldo insuficiente");
		}
	}

	public String obtenerNumeroTarjeta() {
		return numeroTarjeta;
	}

	public String obtenerEntidadBancaria() {
		return entidadBancaria;
	}

	public String obtenerFechaVencimiento() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return dateFormat.format(fechaVencimiento);
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

	public Map<String, Object> toMap() {
		return Map.of("id", id, "numero", numeroTarjeta, "entidadBancaria", entidadBancaria, "fechaVencimiento",
				obtenerFechaVencimiento(), "saldo", montoTotalAGastar);
	}

}
