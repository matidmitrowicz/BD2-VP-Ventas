package ar.unrn.tp.modelo;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class RegistroVenta {

	@Id
	@GeneratedValue
	private Long idVenta;

	private Date fecha;

	private Cliente cliente;
	private String metodoPago;
	private double montoTotal;

	@OneToMany(cascade = CascadeType.PERSIST)
	private List<Producto> misProductos;

	protected RegistroVenta() {
	}

	public RegistroVenta(LocalDate fechaVenta, Cliente cliente, String metodoPago,
			List<Producto> productosSeleccionados, double montoTotal) {
		this.fecha = java.sql.Date.valueOf(fechaVenta);
		this.cliente = cliente;
		this.metodoPago = metodoPago;
		this.misProductos = productosSeleccionados;
		this.montoTotal = montoTotal;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public double getMontoTotal() {
		return montoTotal;
	}

	@Override
	public String toString() {
		return "RegistroVenta [fecha=" + fecha + ", cliente=" + cliente + ", metodoPago=" + metodoPago
				+ ", misProductos=" + misProductos + ", montoTotal=" + montoTotal + "]";
	}

}
