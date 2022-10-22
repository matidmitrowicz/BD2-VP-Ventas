package ar.unrn.tp.modelo;

import java.util.Date;
import java.util.List;

public class RegistroVentaCache {

	private Long idVenta;
	private Date fecha;
	private String metodoPago;
	private double montoTotal;
	private List<ProductoVendido> misProductos;
	private String numYearId;

	public RegistroVentaCache(Long idVenta, Date fecha, String metodoPago, double montoTotal,
			List<ProductoVendido> misProductos, String numYearId) {

		this.idVenta = idVenta;
		this.fecha = fecha;
		this.metodoPago = metodoPago;
		this.montoTotal = montoTotal;
		this.misProductos = misProductos;
		this.numYearId = numYearId;
	}

	public Long getIdVenta() {
		return idVenta;
	}

	public void setIdVenta(Long idVenta) {
		this.idVenta = idVenta;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getMetodoPago() {
		return metodoPago;
	}

	public void setMetodoPago(String metodoPago) {
		this.metodoPago = metodoPago;
	}

	public double getMontoTotal() {
		return montoTotal;
	}

	public void setMontoTotal(double montoTotal) {
		this.montoTotal = montoTotal;
	}

	public List<ProductoVendido> getMisProductos() {
		return misProductos;
	}

	public void setMisProductos(List<ProductoVendido> misProductos) {
		this.misProductos = misProductos;
	}

	public String getNumYearId() {
		return numYearId;
	}

	public void setNumYearId(String numYearId) {
		this.numYearId = numYearId;
	}

	@Override
	public String toString() {
		return "RegistroVentaCache [idVenta=" + idVenta + ", fecha=" + fecha + ", metodoPago=" + metodoPago
				+ ", montoTotal=" + montoTotal + ", misProductos=" + misProductos + ", numYearId=" + numYearId + "]";
	}

}
