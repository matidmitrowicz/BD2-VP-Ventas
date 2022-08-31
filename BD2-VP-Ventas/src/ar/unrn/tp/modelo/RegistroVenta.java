package ar.unrn.tp.modelo;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class RegistroVenta {

	@Id
	@GeneratedValue
	private Long idVenta;
	private Date fecha;
	@ManyToOne(fetch = FetchType.EAGER)
	private Cliente cliente;
	private String metodoPago;
	private double montoTotal;

	@OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	private List<ProductoVendido> misProductos;

	protected RegistroVenta() {
	}

	public RegistroVenta(LocalDate fechaVenta, Cliente cliente, String metodoPago,
			List<ProductoVendido> productosSeleccionados, double montoTotal) {
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

	private Long getIdVenta() {
		return idVenta;
	}

	private void setIdVenta(Long idVenta) {
		this.idVenta = idVenta;
	}

	private Cliente getCliente() {
		return cliente;
	}

	private void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	private String getMetodoPago() {
		return metodoPago;
	}

	private void setMetodoPago(String metodoPago) {
		this.metodoPago = metodoPago;
	}

	private List<ProductoVendido> getMisProductos() {
		return misProductos;
	}

	private void setMisProductos(List<ProductoVendido> misProductos) {
		this.misProductos = misProductos;
	}

	private void setMontoTotal(double montoTotal) {
		this.montoTotal = montoTotal;
	}

	@Override
	public String toString() {
		return "RegistroVenta [idVenta=" + idVenta + ", fecha=" + fecha + ", cliente=" + cliente + ", metodoPago="
				+ metodoPago + ", montoTotal=" + montoTotal + ", misProductos=" + misProductos + "]";
	}

}
