package ar.unrn.tp.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class VentasTest {

	@Test
	public void calculoConPromoMarcaCaducada() {
		// Productos
		Categoria ropa = new Categoria("Ropa Deportiva");
		Categoria calzado = new Categoria("Calzado");
		Producto prod1 = new Producto("1", "Pantalon", "nike", ropa, 500);
		Producto prod2 = new Producto("2", "Zapatilla", "adidas", calzado, 500);
		// Promocion
		LocalDate fin1 = LocalDate.now();
		LocalDate inicio1 = fin1.minusDays(5);
		Promocion promoMarcaNike = new PromocionMarcaProducto(inicio1, fin1, 0.05, "nike");
		List<Promocion> promosActivas = new ArrayList<>();
		promosActivas.add(promoMarcaNike);
		// Cliente + tarjeta
		Cliente mati = new Cliente("matias", "Dmitrowicz", "123", "mati@gmail.com");
		mati.agregarTarjeta(new TarjetaCredito("123456", "Visa", fin1, 10000));
		// Carrito
		CarritoCompra miCarrito = new CarritoCompra(mati, fin1);
		miCarrito.addProduct(prod1);
		miCarrito.addProduct(prod2);
		miCarrito.agregarPromo(promosActivas);
		// VERIFICACION
		assertEquals(1000, miCarrito.montoTotal());
	}

	@Test
	public void calcularConPromoNike() {
		// Productos
		Categoria ropa = new Categoria("Ropa Deportiva");
		Categoria calzado = new Categoria("Calzado");
		Producto prod1 = new Producto("1", "Pantalon", "nike", ropa, 500);
		Producto prod2 = new Producto("2", "Zapatilla", "adidas", calzado, 500);
		// Promocion
		LocalDate now = LocalDate.now();
		LocalDate nowMinus = now.minusDays(1);
		LocalDate nowPlus = now.plusDays(3);
		Promocion promoMarcaNike = new PromocionMarcaProducto(nowMinus, nowPlus, 0.05, "nike");
		List<Promocion> promosActivas = new ArrayList<>();
		promosActivas.add(promoMarcaNike);
		// Cliente + tarjeta
		Cliente mati = new Cliente("matias", "Dmitrowicz", "123", "mati@gmail.com");
		mati.agregarTarjeta(new TarjetaCredito("123456", "Visa", nowPlus, 10000));
		// Carrito
		CarritoCompra miCarrito = new CarritoCompra(mati, LocalDate.now().plusDays(1));
		miCarrito.addProduct(prod1);
		miCarrito.addProduct(prod2);
		miCarrito.agregarPromo(promosActivas);
		// VERIFICACION
		assertEquals(975, miCarrito.montoTotal());
	}

	@Test
	public void calcularConDescuentoMediodePago() {
		// Productos
		Categoria ropa = new Categoria("Ropa Deportiva");
		Categoria calzado = new Categoria("Calzado");
		Producto prod1 = new Producto("1", "Pantalon", "nike", ropa, 500);
		Producto prod2 = new Producto("2", "Zapatilla", "adidas", calzado, 500);
		// Promocion
		LocalDate now = LocalDate.now();
		LocalDate nowMinus = now.minusDays(1);
		LocalDate nowPlus3 = LocalDate.now().plusDays(3);
		LocalDate nowPlus1 = LocalDate.now().plusDays(1);
		Promocion promoMetodoPago = new PromocionMedioDePago(nowMinus, nowPlus3, 0.08, "Visa");
		List<Promocion> promos = new ArrayList<>();
		promos.add(promoMetodoPago);
		// Cliente + tarjeta
		Cliente mati = new Cliente("matias", "Dmitrowicz", "123", "mati@gmail.com");
		mati.agregarTarjeta(new TarjetaCredito("123456", "Visa", nowPlus3, 10000));
		// Carrito
		CarritoCompra miCarrito = new CarritoCompra(mati, nowPlus1);
		miCarrito.addProduct(prod1);
		miCarrito.addProduct(prod2);
		miCarrito.agregarPromo(promos);
		miCarrito.setMedioDePago("Visa");
		// VERIFICACION
		assertEquals(920, miCarrito.montoTotal());
	}

	@Test
	public void calcularMontoDescuentoComarcaDescuentoMemeCard() {
		// Productos
		Categoria ropa = new Categoria("Ropa Deportiva");
		Categoria calzado = new Categoria("Calzado");
		Categoria verduleria = new Categoria("verduleria");
		Producto prod1 = new Producto("1", "pantalon", "nike", ropa, 500);
		Producto prod2 = new Producto("2", "zapatilla", "adidas", calzado, 500);
		Producto prod3 = new Producto("3", "manzanas", "Comarca", verduleria, 500);
		Producto prod4 = new Producto("4", "peras", "Comarca", verduleria, 500);

		LocalDate now = LocalDate.now();
		LocalDate nowMinus = now.minusDays(1);
		LocalDate nowPlus = now.plusDays(3);
		Promocion promoMetodoPago = new PromocionMedioDePago(nowMinus, nowPlus, 0.08, "MemeCard");
		Promocion promoMarcaComarca = new PromocionMarcaProducto(nowMinus, nowPlus, 0.05, "Comarca");
		List<Promocion> promos = new ArrayList<>();
		promos.add(promoMetodoPago);
		promos.add(promoMarcaComarca);
		// Cliente
		Cliente mati = new Cliente("matias", "Dmitrowicz", "123", "mati@gmail.com");
		mati.agregarTarjeta(new TarjetaCredito("123456", "Visa", nowPlus, 10000));
		// Carrito
		CarritoCompra miCarrito = new CarritoCompra(mati, now.plusDays(1));
		miCarrito.addProduct(prod1);
		miCarrito.addProduct(prod2);
		miCarrito.addProduct(prod3);
		miCarrito.addProduct(prod4);
		miCarrito.agregarPromo(promos);
		miCarrito.setMedioDePago("MemeCard");
		// 2000*0.08 = 160
		// La promo producto -> (500+500)*0.05 = 50
		// Descuento total 210
		// Monto total: 2000 - 210 = 1790

		assertEquals(1790, miCarrito.montoTotal());
	}

	@Test
	public void pagarCarro() {
		// Productos
		Categoria ropa = new Categoria("Ropa Deportiva");
		Categoria calzado = new Categoria("Calzado");
		Categoria verduleria = new Categoria("verduleria");
		Producto prod1 = new Producto("1", "pantalon", "nike", ropa, 500);
		Producto prod2 = new Producto("2", "zapatilla", "adidas", calzado, 500);
		Producto prod3 = new Producto("3", "manzanas", "Comarca", verduleria, 500);
		Producto prod4 = new Producto("4", "peras", "Comarca", verduleria, 500);
		List<Promocion> promos = new ArrayList<>();

		LocalDate now = LocalDate.now();
		LocalDate nowMinus = now.minusDays(1);
		LocalDate nowPlus = now.plusDays(3);
		// Cliente
		TarjetaCredito tarjeta = new TarjetaCredito("123456", "MemeCard", nowPlus, 3500);
		Cliente mati = new Cliente("matias", "Dmitrowicz", "123", "mati@gmail.com");
		mati.agregarTarjeta(tarjeta);
		// Carrito
		CarritoCompra miCarrito = new CarritoCompra(mati, now.plusDays(1));
		miCarrito.addProduct(prod1);
		miCarrito.addProduct(prod2);
		miCarrito.addProduct(prod3);
		miCarrito.addProduct(prod4);
		miCarrito.agregarPromo(promos);
		miCarrito.setMedioDePago("MemeCard");

		/*
		 * * Total 2000 * La tarjeta "MemeCard" del cliente esta activa y tiene
		 * suficiente para pagar
		 */
		assertEquals(2000, miCarrito.finalizarVenta(tarjeta).getMontoTotal());

		Promocion promoMarcaComarca = new PromocionMarcaProducto(nowMinus, now.plusDays(3), 0.5, "Comarca");
		promos.add(promoMarcaComarca);

		assertEquals(1500, miCarrito.finalizarVenta(tarjeta).getMontoTotal());
	}

	@Test
	public void crearProductoValido() {
		Categoria calzado = new Categoria("Calzado");
		// expected runtime expection
		Producto prod = new Producto("1", "Pantalon", "nike", calzado, 500);
		Producto prod1 = new Producto("1", "Pantalon", "nike", null, 500);
		Producto prod2 = new Producto("1", null, "nike", calzado, 500);
		Producto prod3 = new Producto("1", "Pantalon", "nike", calzado, (Double) null);
	}

	@Test
	public void crearClienteValido() {
		// Cliente valido
		Cliente cli = new Cliente("matias", "Dmitrowicz", "123", "mati@gmail.com");
		// Cliente sin DNI
		Cliente cli1 = new Cliente("matias", "Dmitrowicz", null, "mati@gmail.com");
		// Cliente sin nombre
		Cliente cli2 = new Cliente(null, "Dmitrowicz", "123", "mati@gmail.com");
		// Cliente sin apellido
		Cliente cli3 = new Cliente("matias", null, "123", "mati@gmail.com");
		// Cliente mail invalido
		Cliente cli4 = new Cliente("matias", "Dmitrowicz", "123", "asd");
	}

	@Test
	public void promocionValida() {
		LocalDate now = LocalDate.now();
		LocalDate nowPlus = now.plusDays(1);
		Promocion promoMetodoPago = new PromocionMedioDePago(now, nowPlus, 0.08, "MemeCard");
		Promocion promoMetodoPago1 = new PromocionMedioDePago(nowPlus, now, 0.08, "MemeCard");
	}

}
