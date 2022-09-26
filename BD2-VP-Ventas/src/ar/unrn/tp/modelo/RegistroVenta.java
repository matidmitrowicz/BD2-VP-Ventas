package ar.unrn.tp.modelo;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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

	@OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER) // @OneToMany > una venta muchos productos
	@JoinColumn(name = "venta_id")
	private List<ProductoVendido> misProductos;

	// Numero y año de la venta
	private String numYearId;

	protected RegistroVenta() {
	}

	public RegistroVenta(LocalDate fechaVenta, Cliente cliente, String metodoPago,
			List<ProductoVendido> productosSeleccionados, double montoTotal) {
		if (cliente == null) {
			throw new RuntimeException("Missing client");
		}
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

	public String obtenerFecha() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return dateFormat.format(fecha);
	}

	@Override
	public String toString() {
		return "RegistroVenta [idVenta=" + idVenta + ", fecha=" + fecha + ", cliente=" + cliente + ", metodoPago="
				+ metodoPago + ", montoTotal=" + montoTotal + ", misProductos=" + misProductos + "]";
	}

	public Map<String, Object> toMap() {
//		return Map.of("id", idVenta, "fechaHora", obtenerFecha(), "productos", misProductos, "tarjeta", metodoPago,
//				"total", montoTotal);
		var map = new HashMap<String, Object>(Map.of("id", idVenta, "fecha", obtenerFecha(), "tarjeta", metodoPago,
				"total", montoTotal, "numYearId", numYearId));

		if (this.misProductos != null && this.misProductos.size() > 0) {
			map.put("productos", misProductos.stream().map((e) -> e.toMap()).collect(Collectors.toList()));
		}
		return map;
	}

	private String getNumYearId() {
		return numYearId;
	}

	private void setNumYearId(String numYearId) {
		this.numYearId = numYearId;
	}

	public void setNumberYearFormat(String formatoNumYear) {
		this.numYearId = formatoNumYear;

	}

}
