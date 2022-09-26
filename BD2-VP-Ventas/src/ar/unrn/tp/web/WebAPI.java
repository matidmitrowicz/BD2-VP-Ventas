package ar.unrn.tp.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ar.unrn.tp.api.ClienteService;
import ar.unrn.tp.api.DescuentoService;
import ar.unrn.tp.api.ProductoService;
import ar.unrn.tp.api.VentaService;
import ar.unrn.tp.excepciones.UpdateInProcessException;
import ar.unrn.tp.modelo.Categoria;
import ar.unrn.tp.modelo.Producto;
import ar.unrn.tp.modelo.Promocion;
import ar.unrn.tp.modelo.RegistroVenta;
import ar.unrn.tp.modelo.TarjetaCredito;
import io.javalin.Javalin;
import io.javalin.http.Handler;

public class WebAPI {

	private ClienteService clientes = null;
	private ProductoService productos = null;
	private VentaService ventas = null;
	private DescuentoService promos = null;
	private int webPort;

	public WebAPI(ClienteService clientes, ProductoService productos, DescuentoService promos, VentaService ventas,
			int webPort) {
		this.clientes = clientes;
		this.productos = productos;
		this.ventas = ventas;
		this.promos = promos;
		this.webPort = webPort;
	}

	public void start() {
		Javalin app = Javalin.create(config -> {
			config.enableCorsForAllOrigins();
		}).start(this.webPort);
		app.get("/productos", traerProductos());
		app.get("/tarjetas/{id}", traerTarjetas());
		app.get("/descuentos", traerPromocionesActivas());
		app.post("/monto", calcularMonto());
		app.post("/compra", realizarCompra());
		app.post("/productUpdate", productUpdate()); // modifica el producto

		app.get("/updateProducto", productoModificable()); // retorna un producto y categorias
		app.get("/ventas", traerVentas());

		app.exception(UpdateInProcessException.class, (e, ctx) -> {
			ctx.json(Map.of("result", "error", "message", e.getMessage()));
		});

		app.exception(Exception.class, (e, ctx) -> {
			ctx.json(Map.of("result", "error", "message", "Ups... algo se rompió.: " + e.getMessage()));
			// log error in a stream...
		});
	}

	private Handler productUpdate() {
		return ctx -> {
			ProductoDTO dto = ctx.bodyAsClass(ProductoDTO.class);

			this.productos.modificarProducto(Long.parseLong(dto.getIdProducto()), dto.getCodigo(), dto.getDescripcion(),
					dto.getMarca(), Double.parseDouble(dto.getPrecio()), Long.parseLong(dto.getCategoria()),
					Long.parseLong(dto.getVersion()));

			ctx.json(Map.of("result", "success", "message", "Producto actualizado"));

		};
	}

	private Handler productoModificable() {
		return ctx -> {
			var productos = this.productos.listarProductos();
			var categorias = this.productos.listarCategorias();

			var listaCategorias = new ArrayList<Map<String, Object>>();

			for (Categoria c : categorias) {
				listaCategorias.add(c.toMap());
			}

			Producto p = productos.get(0); // primer producto de la bd

			ctx.json(Map.of("result", "success", "producto", p.toMap(), "categorias", listaCategorias));
		};
	}

	private Handler traerProductos() {
		return ctx -> {
			var productos = this.productos.listarProductos();
			var list = new ArrayList<Map<String, Object>>();
			for (Producto p : productos) {
				list.add(p.toMap());
			}
			ctx.json(Map.of("result", "success", "productos", list));
		};
	}

	private Handler traerPromocionesActivas() {
		return ctx -> {
			var promoVigentes = this.promos.promosActivas();
			var list = new ArrayList<Map<String, Object>>();
			for (Promocion p : promoVigentes) {
				list.add(p.toMap());
			}
			ctx.json(Map.of("result", "success", "promociones", list));
		};
	}

	private Handler traerTarjetas() {
		return ctx -> {
			var id = Long.valueOf(ctx.pathParam("id")); // id del cliente
			var tarjetas = this.clientes.listarTarjetas(id);
			var list = new ArrayList<Map<String, Object>>();
			for (TarjetaCredito t : tarjetas) {
				list.add(t.toMap());
			}
			ctx.json(Map.of("result", "success", "tarjetas", list));
		};
	}

	private Handler calcularMonto() {
		return ctx -> {
			RegistroVentaDTO ventadto = ctx.bodyAsClass(RegistroVentaDTO.class);
			List<Long> productos = new ArrayList<Long>();

			for (String prod : ventadto.getProductosVendidos()) {
				Long idProducto = Long.parseLong(prod);
				productos.add(idProducto);
			}

			Long idTarjeta = Long.parseLong(ventadto.getTarjeta());

			double monto = this.ventas.calcularMonto(productos, idTarjeta);

			ctx.json(Map.of("result", "success", "message", "El monto a pagar es de $ ", "monto", monto));
		};
	}

	private Handler realizarCompra() {
		return ctx -> {

			RegistroVentaDTO ventadto = ctx.bodyAsClass(RegistroVentaDTO.class);
			Long idCliente = Long.parseLong(ventadto.getCliente());
			Long idTarjeta = Long.parseLong(ventadto.getTarjeta());
			List<Long> productos = new ArrayList<Long>();
			for (String prod : ventadto.getProductosVendidos()) {
				Long idProducto = Long.parseLong(prod);
				productos.add(idProducto);
			}

			this.ventas.realizarVenta(idCliente, productos, idTarjeta);

			ctx.json(Map.of("result", "success", "message", "Su compra se genero con exito"));
		};
	}

	private Handler traerVentas() {
		return ctx -> {
			var ventas = this.ventas.ventas();
			var list = new ArrayList<Map<String, Object>>();
			for (RegistroVenta v : ventas) {
				list.add(v.toMap());
			}
			ctx.json(Map.of("result", "success", "ventas", list));
		};
	}

}
