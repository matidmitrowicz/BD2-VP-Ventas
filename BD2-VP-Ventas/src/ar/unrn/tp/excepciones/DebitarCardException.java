package ar.unrn.tp.excepciones;

public class DebitarCardException extends Exception {

	private String msj;

	public DebitarCardException(String msj) {
		super(msj);
	}
}
