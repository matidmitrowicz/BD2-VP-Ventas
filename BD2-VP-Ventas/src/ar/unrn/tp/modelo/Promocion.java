package ar.unrn.tp.modelo;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@DiscriminatorColumn(name = "tipo")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Promocion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;

	protected Date fechaInicio;
	protected Date fechaFin;
	protected double descuento;

	protected Promocion() {
	}

	protected Promocion(LocalDate fechaInicio, LocalDate fechaFin, double descuento) {
		if (fechaInicio.equals(fechaFin) || fechaInicio.isAfter(fechaFin)) {
			throw new RuntimeException("Se intento crear una promocion con fechas invalidas");
		}
		if (descuento <= 0 || descuento > 1) {
			throw new RuntimeException(
					"Se intento crear una promocion con un descuento invalido.. EJ, descuento de 5% = 5/100 => 0.05");
		}

		this.fechaInicio = java.sql.Date.valueOf(fechaInicio);
		this.fechaFin = java.sql.Date.valueOf(fechaFin);
		this.descuento = descuento;
	}

	protected boolean fechaDentroDePromocion() {
		return ((fechaInicio.before(java.sql.Date.valueOf(LocalDate.now())))
				&& (fechaFin.after(java.sql.Date.valueOf(LocalDate.now()))));
	}

	public abstract double devolverMontoDescontado(List<Producto> productos, String metodoPago);

	public abstract Map<String, Object> toMap();

	public double getDescuento() {
		return descuento;
	}

	public void setDescuento(float descuento) {
		this.descuento = descuento;
	}

	private Long getIdPromocion() {
		return id;
	}

	private void setIdPromocion(Long idPromocion) {
		this.id = idPromocion;
	}

	private Date getFechaInicio() {
		return fechaInicio;
	}

	private void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	private Date getFechaFin() {
		return fechaFin;
	}

	private void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	private void setDescuento(double descuento) {
		this.descuento = descuento;
	}

}
