package ar.unrn.tp.modelo;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class NumberYearVenta {

	@Id
	@GeneratedValue
	private long id;

	private int number;
	private int year;

	public NumberYearVenta() {
		this.number = 1;
		this.year = LocalDateTime.now().getYear();
	}

	public String formatoNumYear() {
		return number + "-" + year;
	}

	public void nextValue() {
		this.number += 1;
	}

	private Long getId() {
		return id;
	}

	private void setId(Long id) {
		this.id = id;
	}

	private int getNumber() {
		return number;
	}

	private void setNumber(int number) {
		this.number = number;
	}

	private int getYear() {
		return year;
	}

	private void setYear(int year) {
		this.year = year;
	}

}
