package ar.unrn.tp.modelo;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "Tarjeta")
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

	@Override
	public String toString() {
		return "PromocionMedioDePago [promoMedioDePago=" + promoMedioDePago + ", idPromocion=" + id + ", fechaInicio="
				+ fechaInicio + ", fechaFin=" + fechaFin + ", descuento=" + descuento + "]";
	}

	@Override
	public Map<String, Object> toMap() {
		return Map.of("id", this.id, "fechaInicio", this.fechaInicio.toString(), "fechaFin", this.fechaFin.toString(),
				"descuento", this.descuento, "tipoDescuento", this.promoMedioDePago);
	}

}
