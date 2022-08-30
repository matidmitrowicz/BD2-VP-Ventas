package ar.unrn.tp.modelo;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Entity;

@Entity
public class PromocionMedioDePago extends Promocion {

	private String promoMedioDePago;

	protected PromocionMedioDePago() {
	}

	public PromocionMedioDePago(LocalDate fechaInicio, LocalDate fechaFin, double descuento, String medioDePago) {
		super(fechaInicio, fechaFin, descuento);
		this.promoMedioDePago = medioDePago;
	}

	@Override
	public double devolverMontoDescontado(List<Producto> productos, String medioDePago) {
		double monto = 0;

		if (this.promoMedioDePago.equals(medioDePago)) {
			if (fechaDentroDePromocion()) {
				for (Producto producto : productos) {
					monto += producto.obtenerPrecio();
				}

				monto *= this.descuento;
			}
		}

		return monto;
	}

	private String getPromoMedioDePago() {
		return promoMedioDePago;
	}

	private void setPromoMedioDePago(String promoMedioDePago) {
		this.promoMedioDePago = promoMedioDePago;
	}

}
