package ar.unrn.tp.excepciones;

public class UpdateInProcessException extends Exception {

	private String msj;

	public UpdateInProcessException(String msj) {
		super(msj);
	}

}
