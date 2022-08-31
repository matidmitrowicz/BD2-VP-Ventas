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
//		clienteService.agregarTarjeta(1L, "1", "MasterCard", 3000, LocalDate.now().plusDays(90));

		List<TarjetaCredito> tarjetasCli = clienteService.listarTarjetas(1L);
		for (TarjetaCredito tjc : tarjetasCli) {
			System.out.println("Tarjeta cliente : " + tjc.toString());
		}

//		DescuentoServiceJPA promoService = new DescuentoServiceJPA();
//		promoService.crearDescuento("Nike", LocalDate.now().minusDays(1), LocalDate.now().plusDays(5), 0.05);
//		promoService.crearDescuentoSobreTotal("MemeCard", LocalDate.now().minusDays(1), LocalDate.now().plusDays(5),
//				0.05);

		ProductoServiceJPA productService = new ProductoServiceJPA();

//		productService.crearCategoria("Ropa Deportiva");
//		productService.crearProducto("50", "Pantalon", "Nike", 1000, 5L); 		//ID6
//		productService.crearProducto("52", "Remera", "Nike", 1000, 5L);			//ID7
//		productService.crearProducto("54", "Pantalon", "Adidas", 1000, 5L);		//ID8
//		productService.crearProducto("56", "Remera", "Adidas", 1000, 5L);		//ID9
//		productService.modificarProducto(6L, "3", "Pantalon gym", "Nike", 1500, 5L);

		List<Producto> products = productService.listarProductos();
		for (Producto prod : products) {
			System.out.println(prod.toString());
		}

		// Product List
		List<Long> productos = new ArrayList<Long>();
		productos.add(6L);
//		productos.add(11L);
//		for (Long id : productos)
//			System.out.println(id);

		VentaServiceJPA servicioVenta = new VentaServiceJPA();
		System.out.println(servicioVenta.calcularMonto(productos, 2L));
//		servicioVenta.realizarVenta(1L, productos, 2L);
//
		List<RegistroVenta> ventas = servicioVenta.ventas();
		for (RegistroVenta registroVenta : ventas) {
			System.out.println(registroVenta.toString());
		}
	}

}
