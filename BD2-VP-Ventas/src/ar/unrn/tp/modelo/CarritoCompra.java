package ar.unrn.tp.modelo;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ar.unrn.tp.excepciones.DebitarCardException;

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

	public CarritoCompra(Cliente cliente, List<Promocion> promociones) {
		super();
		if (cliente == null) {
			throw new RuntimeException("Missing client");
		}
		this.cliente = cliente;
		this.setFechaCompra(java.sql.Date.valueOf(LocalDate.now()));
		this.productosSeleccionados = new ArrayList<Producto>();
		this.promociones = promociones;
	}

	public CarritoCompra(List<Promocion> promociones) {
		this.promociones = promociones;
		this.setFechaCompra(java.sql.Date.valueOf(LocalDate.now()));
		this.productosSeleccionados = new ArrayList<Producto>();
	}

	private boolean esDatoVacio(String dato) {
		return dato.equals("");
	}

	private boolean esDatoNulo(Object dato) {
		return dato == null;
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

	public void agregarListaProducto(List<Producto> listadoProductos) {
		this.productosSeleccionados = listadoProductos;
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

	public double montoTotal() throws DebitarCardException {
		double total = 0;
		if (esDatoVacio(medioDePago) || esDatoNulo(medioDePago)) {
			throw new DebitarCardException("Falta ingresar la Tarjeta");
		}

		for (Producto producto : this.getProductosSeleccionados()) {
			total += producto.obtenerPrecio();
		}
		return total - this.calcularDescuento();
	}

	public RegistroVenta finalizarVenta(TarjetaCredito tarjeta) throws DebitarCardException {
		try {
			setMedioDePago(tarjeta.obtenerEntidadBancaria());
			if (!cliente.tarjetaValida(tarjeta)) {
				throw new RuntimeException("La tarjeta no le pertenece al cliente");
			}
			tarjeta.debitar(montoTotal());
			List<ProductoVendido> pVendidos = new ArrayList<>();
			for (Producto producto : productosSeleccionados) {
				ProductoVendido prodV = new ProductoVendido(producto);
				pVendidos.add(prodV);
			}
			RegistroVenta venta = new RegistroVenta(LocalDate.now(), cliente, getMedioDePago(), pVendidos,
					montoTotal());
			return venta;
		} catch (RuntimeException e) {
			throw e;
		}
	}

}
