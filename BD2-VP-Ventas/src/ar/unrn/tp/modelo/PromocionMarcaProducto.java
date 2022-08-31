package ar.unrn.tp.modelo;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Entity;

@Entity
public class PromocionMarcaProducto extends Promocion {

	private String marcaEnDescuento;

	protected PromocionMarcaProducto() {
	}

	public PromocionMarcaProducto(LocalDate fechaInicio, LocalDate fechaFin, double descuento,
			String marcaEnDescuento) {
		super(fechaInicio, fechaFin, descuento);
		this.marcaEnDescuento = marcaEnDescuento;
	}

	@Override
	public double devolverMontoDescontado(List<Producto> productos, String mediodePago) {
		double monto = 0;

		if (fechaDentroDePromocion()) {
			for (Producto producto : productos) {
				if (producto.verMarca().equals(this.marcaEnDescuento)) {
					monto += producto.obtenerPrecio() * this.descuento;
				}
			}
		}

		return monto;
	}

	private String getMarcaEnDescuento() {
		return marcaEnDescuento;
	}

	private void setMarcaEnDescuento(String marcaEnDescuento) {
		this.marcaEnDescuento = marcaEnDescuento;
	}

	@Override
	public String toString() {
		return "PromocionMarcaProducto [marcaEnDescuento=" + marcaEnDescuento + ", idPromocion=" + idPromocion
				+ ", fechaInicio=" + fechaInicio + ", fechaFin=" + fechaFin + ", descuento=" + descuento + "]";
	}

}
