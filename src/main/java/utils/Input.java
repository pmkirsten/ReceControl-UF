package utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Input {
	/**
	 * Devuelve lo introducido por teclado como {@link String}
	 *
	 * @return Un {@link String} con la informaci�n introducida por teclado
	 */
	public static String init() {
		String buffer = "";
		InputStreamReader stream = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(stream);
		try {
			buffer = reader.readLine();
		} catch (Exception e) {
			System.out.append("Dato non v�lido.");
		}
		return buffer;
	}

	/**
	 * Devuelve el {@link Integer} introducido por teclado
	 *
	 * @return El {@link Integer} introducido por teclado
	 */
	public static int integer() {
		return Input.integer(null);
	}

	/**
	 * Devuelve el {@link Integer} intoducido por par�metro, ense�ando un
	 * mensaje previo
	 *
	 * @param message
	 *            El mensaje a mostrar antes de la introducci�n de los datos
	 * @return El {@link Integer} introducido por par�metro
	 */
	public static int integer(String message) {
		if (message != null) {
			System.out.print(message);
		}
		int value = -1;
		try {
			value = Integer.parseInt(Input.init());
		} catch (Exception e) {
			return Input.integer("Dato no v�lido, introd�zcalo de nuevo: ");
		}
		return value;
	}

	/**
	 * El {@link Double} introducido por teclado
	 *
	 * @return El {@link Double} introducido por telcado
	 */
	public static double real() {
		return Input.real(null);
	}

	/**
	 * El {@link Double} introducido por teclado, mostrando un mensaje previo
	 *
	 * @param message
	 *            El mensaje a mostrar
	 * @return El {@link Double} introducido por telcado
	 */
	public static double real(String message) {
		if (message != null) {
			System.out.print(message);
		}
		double value = 0.0;
		try {
			value = Double.parseDouble(Input.init());
		} catch (Exception e) {
			return Input.real("Dato no v�lido, introd�zcalo de nuevo: ");
		}
		return value;
	}

	/**
	 * El {@link String} introducido por teclado
	 *
	 * @return El {@link String} introducido por teclado
	 */
	public static String string() {
		return Input.string(null);
	}

	/**
	 * El {@link String} introducido por teclado, precedido de un mensaje
	 *
	 * @param message
	 *            El mensaje a mostrar
	 * @return El {@link String} introducido por teclado
	 */
	public static String string(String message) {
		if (message != null) {
			System.out.print(message);
		}
		String value = Input.init();
		return value;
	}

	/**
	 * El {@link Character} introducido por par�metro, en caso de ser m�ltiples
	 * {@link Character} como entrada, s�lo devuelve el primero
	 *
	 * @return El {@link Character} introducido por par�metro, en caso de ser
	 *         m�ltiples {@link Character} como entrada, s�lo devuelve el
	 *         primero
	 */
	public static char character() {
		return Input.character(null);
	}

	/**
	 * El {@link Character} introducido por par�metro, con un mensaje previo. En
	 * caso de ser m�ltiples {@link Character} como entrada, s�lo devuelve el
	 * primero
	 *
	 * @param message
	 *            El mensaje a mostar
	 *
	 * @return El {@link Character} introducido por par�metro, en caso de ser
	 *         m�ltiples {@link Character} como entrada, s�lo devuelve el
	 *         primero
	 */
	public static char character(String message) {
		if (message != null) {
			System.out.print(message);
		}
		String valor = Input.init();
		return valor.charAt(0);
	}
}