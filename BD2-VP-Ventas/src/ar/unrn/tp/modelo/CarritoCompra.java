package ar.unrn.tp.modelo;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CarritoCompra {

	private List<Producto> productosSeleccionados;
	private Cliente cliente;
	private Date fechaCompra;
	private List<Promocion> promociones;
	private String medioDePago;

	public CarritoCompra(Cliente cliente, LocalDate fechaCompra) {
		super();
		this.cliente = cliente;
		this.setFechaCompra(java.sql.Date.valueOf(fechaCompra));
		this.productosSeleccionados = new ArrayList<Producto>();
		this.promociones = new ArrayList<Promocion>();
	}

	public CarritoCompra(List<Promocion> promociones) {
		this.promociones = promociones;
		this.setFechaCompra(java.sql.Date.valueOf(LocalDate.now()));
	}

	public List<Producto> getProductosSeleccionados() {
		return productosSeleccionados;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public List<Promocion> getPromociones() {
		return promociones;
	}

	public void agregarPromo(List<Promocion> promociones) {
		this.promociones = promociones;
	}

	public String getMedioDePago() {
		return medioDePago;
	}

	public void setMedioDePago(String medioDePago) {
		this.medioDePago = medioDePago;
	}

	public void addProduct(Producto product) {
		this.productosSeleccionados.add(product);

	}

	public Date getFechaCompra() {
		return fechaCompra;
	}

	public void setFechaCompra(Date fechaCompra) {
		this.fechaCompra = fechaCompra;
	}

	private double calcularDescuento() {
		double descuento = 0;

		for (Promocion promo : this.getPromociones()) {
			descuento += promo.devolverMontoDescontado(productosSeleccionados, medioDePago);
		}
//		System.out.println(descuento);
		return descuento;
	}

	public double montoTotal() {
		double total = 0;

		for (Producto producto : this.getProductosSeleccionados()) {
			total += producto.obtenerPrecio();
		}
		return total - this.calcularDescuento();
	}

	public RegistroVenta pagarCarrito(String metodoPago) {
		try {
			setMedioDePago(metodoPago);
			TarjetaCredito tarjeta = getCliente().obtenerTarjetaEntidad(medioDePago);
			tarjeta.debitar(montoTotal());
			RegistroVenta venta = new RegistroVenta(LocalDate.now(), cliente, metodoPago, productosSeleccionados,
					montoTotal());
			return venta;
		} catch (RuntimeException e) {
			throw e;
		}
	}

//	public void pagarCarrito2(String metodoPago) {
//		try {
//			setMedioDePago(metodoPago);
//			TarjetaCredito tarjeta = getCliente().getTarjeta(metodoPago);
//			System.out.println(tarjeta);
//			System.out.println(montoTotal());
//			System.out.println(cliente);
//			System.out.println(metodoPago);
//			System.out.println(tarjeta.getMontoTotalAGastar());
//			tarjeta.debitar(montoTotal());
//			System.out.println(tarjeta.getMontoTotalAGastar());
//
//		} catch (RuntimeException e) {
//			throw e;
//		}
//	}

}
