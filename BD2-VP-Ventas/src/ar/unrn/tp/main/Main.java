package ar.unrn.tp.main;

import ar.unrn.tp.api.ClienteService;
import ar.unrn.tp.api.DescuentoService;
import ar.unrn.tp.api.ProductoService;
import ar.unrn.tp.api.VentaService;
import ar.unrn.tp.jpa.servicios.ClienteServiceJPA;
import ar.unrn.tp.jpa.servicios.DescuentoServiceJPA;
import ar.unrn.tp.jpa.servicios.ProductoServiceJPA;
import ar.unrn.tp.jpa.servicios.VentaServiceJPA;
import ar.unrn.tp.web.WebAPI;

public class Main {

	public static void main(String[] args) {

//		String jpa = "jpa-objectdb";
		String jpa = "jpa-mysql";

		ClienteService clienteService = new ClienteServiceJPA(jpa);
		ProductoService productService = new ProductoServiceJPA(jpa);
		DescuentoService promoService = new DescuentoServiceJPA(jpa);
		VentaService servicioVenta = new VentaServiceJPA(jpa);

		WebAPI api = new WebAPI(clienteService, productService, promoService, servicioVenta, 1234);
		api.start();

//		var productos = productService.listarProductos();
//		var list = new ArrayList<Map<String, Object>>();
//		for (Producto p : productos) {
//			list.add(p.toMap());
//		}
//		System.out.println(list);

//		clienteService.crearCliente("Juan", "Perez", "1234", "juan@mail.com");
//		clienteService.modificarCliente(1L, "JuanNuevo", "PerezNuevo", "1234", "juanNuevo@mail.com");
//		clienteService.agregarTarjeta(1L, "100", "MemeCard", 5000, LocalDate.now().plusDays(90));
//
//		java.util.List<TarjetaCredito> tarjetasCli = clienteService.listarTarjetas(1L);
//		for (TarjetaCredito tjc : tarjetasCli) {
//			System.out.println("Tarjeta cliente : " + tjc.toString());
//		}
//
//		promoService.crearDescuento("Nike", LocalDate.now().minusDays(1), LocalDate.now().plusDays(5), 0.05);
//		promoService.crearDescuentoSobreTotal("MemeCard", LocalDate.now().minusDays(1), LocalDate.now().plusDays(5),
//				0.05);
//
//		java.util.List<Promocion> promo = promoService.promosActivas();
//		for (Promocion p : promo) {
//			System.out.println(p.toString());
//		}

//
//		productService.crearCategoria("Ropa Deportiva");
//		productService.crearCategoria("Calzado");
//		productService.crearProducto("50", "Pantalon", "Nike", 500, 2L); // ID6
//		productService.crearProducto("52", "Remera", "Nike", 500, 2L); // ID7
//		productService.crearProducto("54", "Pantalon", "Adidas", 500, 2L); // ID8
//		productService.crearProducto("56", "Remera", "Adidas", 500, 2L); // ID9
//		productService.modificarProducto(6L, "3", "Pantalon gym", "Nike", 1500, 5L);
//
//		java.util.List<Producto> products = productService.listarProductos();
//		for (Producto prod : products) {
//			System.out.println(prod.toString());
//		}
//
//		// Product List
//		java.util.List<Long> productos = new ArrayList<Long>();
//		productos.add(3L);
//		productos.add(4L);
//		productos.add(11L);
//		for (Long id : productos)
//			System.out.println(id);
//
//		System.out.println(servicioVenta.calcularMonto(productos, 1L));
//		servicioVenta.realizarVenta(1L, productos, 1L);
//
//		java.util.List<RegistroVenta> ventas = servicioVenta.ventas();
//		var list = new ArrayList<Map<String, Object>>();
//		for (RegistroVenta v : ventas) {
//			System.out.println(list.add(v.toMap()));
//		}
//		for (Map<String, Object> map : list) {
//			System.out.println(map);
//		}

//		for (RegistroVenta registroVenta : ventas) {
//			System.out.println(registroVenta.toString());
//		}

	}
}