package ar.unrn.tp.main;

import java.util.ArrayList;
import java.util.List;

import ar.unrn.tp.jpa.servicios.ClienteServiceJPA;
import ar.unrn.tp.jpa.servicios.ProductoServiceJPA;
import ar.unrn.tp.jpa.servicios.VentaServiceJPA;
import ar.unrn.tp.modelo.Producto;
import ar.unrn.tp.modelo.RegistroVenta;
import ar.unrn.tp.modelo.TarjetaCredito;

public class Main {

	public static void main(String[] args) {

		ClienteServiceJPA clienteService = new ClienteServiceJPA();
//		clienteService.crearCliente("Juan", "Perez", "1234", "juan@mail.com");
//		clienteService.modificarCliente(1L, "JuanNuevo", "PerezNuevo", "1234", "juanNuevo@mail.com");
//		clienteService.agregarTarjeta(1L, "1", "MasterCard", 500, LocalDate.now().plusDays(90));

		List<TarjetaCredito> tarjetasCli = clienteService.listarTarjetas(1L);
		for (TarjetaCredito tjc : tarjetasCli) {
			System.out.println(tjc.toString());
		}

//		DescuentoServiceJPA promoService = new DescuentoServiceJPA();
//		promoService.crearDescuento("Nike", LocalDate.now(), LocalDate.now().plusDays(10), 0.05);
//		promoService.crearDescuentoSobreTotal("MemeCard", LocalDate.now().minusDays(5), LocalDate.now().plusDays(5),
//				0.05);
		ProductoServiceJPA productService = new ProductoServiceJPA();
//		productService.crearCategoria("Ropa Deportiva");
//		productService.crearProducto("1", "Pantalon", "Nike", 500, 9L);
//		productService.crearProducto("2", "Remera", "Nike", 500, 9L);
		productService.modificarProducto(11L, "3", "Remera algodon", "Nike", 1000, 9L);

		List<Producto> products = productService.listarProductos();
		for (Producto prod : products) {
			System.out.println(prod.toString());
		}

		List<Long> productosComprados = new ArrayList<>();
		productosComprados.add(10L);
		productosComprados.add(11L);

		VentaServiceJPA servicioVenta = new VentaServiceJPA();
		servicioVenta.realizarVenta(1L, productosComprados, 2L);

		List<RegistroVenta> ventas = servicioVenta.ventas();
	}

}
