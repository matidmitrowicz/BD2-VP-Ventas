package ar.unrn.tp.api;

import java.time.LocalDate;
import java.util.List;

import ar.unrn.tp.modelo.Promocion;

public interface DescuentoService {
	// validar que las fechas no se superpongan
	void crearDescuentoSobreTotal(String marcaTarjeta, LocalDate fechaDesde, LocalDate fechaHasta, double porcentaje);

	// validar que las fechas no se superpongan
	void crearDescuento(String marcaProducto, LocalDate fechaDesde, LocalDate fechaHasta, double porcentaje);

	List<Promocion> promosActivas();
}
