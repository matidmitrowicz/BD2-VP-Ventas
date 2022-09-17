package ar.unrn.tp.web;

public class RegistroVentaDTO {

	private String cliente;
	private String[] productosVendidos;
	private String tarjeta;

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public String[] getProductosVendidos() {
		return productosVendidos;
	}

	public void setProductosVendidos(String[] productosVendidos) {
		this.productosVendidos = productosVendidos;
	}

	public String getTarjeta() {
		return tarjeta;
	}

	public void setTarjeta(String tarjeta) {
		this.tarjeta = tarjeta;
	}

}
