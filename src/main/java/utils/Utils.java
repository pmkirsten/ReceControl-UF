package utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Utils {

	/**
	 * Formatea el {@link Double} que se pasa por par�metro, con el formato #.##
	 *
	 * @param d
	 *            el valor a transformar
	 * @return El double formateado
	 */
	public static String formatLocalNumber(double d) {
		DecimalFormat dF = new DecimalFormat("#.##");
		return dF.format(d);
	}

	/**
	 * Obtiene un n�mero aleatorio selecionado entre los valores pasado por
	 * par�metros. Si el l�mite inferior es superior al mayor, se intercambia
	 *
	 * @param min
	 *            El l�mite inferior del rango
	 * @param max
	 *            El l�mite superior del rango
	 * @return Un n�mero aleatorio entre el rango seleccionado.
	 */
	public static int getRandomNumberInRange(int min, int max) {
		if (min > max) {
			int aux = max;
			max = min;
			min = aux;
		}

		return ((int) (Math.random() * ((max - min) + 1)) + min);
	}

	/**
	 * Muestra una lista con �ndices, y permite que muestre un mensaje para
	 * esperar
	 *
	 * @param list
	 *            La lista a ser mostrada
	 * @param wait
	 *            <code>true</code> para esperar despues de mostrar la lista,
	 *            <code>false</code> en caso contrario.
	 */
	public static <T> void showFromList(List<T> list, boolean wait) {
		Utils.showFromList(list, wait, null);
	}

	/**
	 * Muestra una lista con �ndices, y permite que muestre un mensaje para
	 * esperar, y excluye los elementos de la primera lista que est�n presentes
	 * en la segunda.
	 *
	 * @param list
	 *            La lista a ser mostrada
	 * @param wait
	 *            <code>true</code> para esperar despues de mostrar la lista,
	 *            <code>false</code> en caso contrario.
	 * @param excludeElements
	 *            Excluye los elementos que existan en esta lista de la lista
	 *            pasada por par�metro
	 */
	public static <T> void showFromList(List<T> list, boolean wait, List<T> excludeElements) {
		StringBuilder builder = new StringBuilder();
		List auxList = new ArrayList<>();
		auxList.addAll(list);
		if (excludeElements != null) {
			auxList.removeAll(excludeElements);
		}
		for (int i = 0; i < auxList.size(); i++) {
			builder.append("\t");
			builder.append(i + 1);
			builder.append(". ");
			builder.append(auxList.get(i).toString());
			builder.append("\n");
		}
		System.out.print(builder.toString());
		if (wait) {
			String toRet = Input.string("\nPulse \"Enter\" para continuar...");
		}
	}

	/**
	 * Devuelve un array de un elemento con la opci�n seleccionada de la lista,
	 * y con la opci�n de poder cancelar esa opci�n
	 *
	 * @param list
	 *            Lista de la que se va a seleccionar
	 * @param cancel
	 *            <code>true</code> para poder cancelar la selecci�n,
	 *            <code>false</code> en caso contrario
	 * @return Un array de un elemento con la posici�n que ocupa el elemento
	 *         seleccionado en la lista
	 */
	public static <T> int[] showAndSelectFromList(List<T> list, boolean cancel) {
		return Utils.showAndSelectFromList(list, cancel, false);
	}

	/**
	 * Devuelve un array de m�tiples elementos con la opci�n seleccionada de la
	 * lista, y con la opci�n de poder cancelar esa opci�n, o bien devolver un
	 * array con las posiciones de las selecciones
	 *
	 * @param list
	 *            Lista de la que se va a seleccionar
	 * @param cancel
	 *            <code>true</code> para poder cancelar la selecci�n,
	 *            <code>false</code> en caso contrario. En caso de que el
	 *            par�metro de multipleReturn este a <code>true</code> este
	 *            par�metro no tiene funcionalidad
	 * @param multipleReturn
	 *            <code>true</code> para que el array pueda contener m�ltiples
	 *            selecciones, <code>false</code> en caso contrario.
	 * @return Una lista con uno o m�ltiples elementos seleccionado de la lista
	 */
	public static <T> int[] showAndSelectFromList(List<T> list, boolean cancel, boolean multipleReturn) {
		return Utils.showAndSelectFromList(list, cancel, multipleReturn, null);
	}

	/**
	 * Devuelve un array de m�tiples elementos con la opci�n seleccionada de la
	 * lista, y con la opci�n de poder cancelar esa opci�n, o bien devolver un
	 * array con las posiciones de las selecciones. Se pueden excluir los
	 * elementos de la segunda lista
	 *
	 * @param list
	 *            Lista de la que se va a seleccionar
	 * @param cancel
	 *            <code>true</code> para poder cancelar la selecci�n,
	 *            <code>false</code> en caso contrario. En caso de que el
	 *            par�metro de multipleReturn este a <code>true</code> este
	 *            par�metro no tiene funcionalidad
	 * @param multipleReturn
	 *            <code>true</code> para que el array pueda contener m�ltiples
	 *            selecciones, <code>false</code> en caso contrario.
	 * @param excludeElements
	 *            Elementos de la lista que ser�n excluidas.
	 * @return Una lista con uno o m�ltiples elementos seleccionado de la lista
	 */
	public static <T> int[] showAndSelectFromList(List<T> list, boolean cancel, boolean multipleReturn, List<T> excludeElements) {
		boolean check = true;
		Utils.showFromList(list, false, excludeElements);
		StringBuilder builder = new StringBuilder();
		if (!multipleReturn) {
			builder.append("\nSeleccione el elemento deseado");
			if (cancel) {
				builder.append(", 0 para salir");
			}
			builder.append(": ");
			int selected = Input.integer(builder.toString());
			while (!Utils.checkSelection(selected, list.size()) && (selected != 0)) {
				selected = Input.integer("La opci�n no es v�lida, por favor, escoje una opci�n v�lida: ");
			}
			int[] selection = new int[1];
			selection[0] = selected - 1;
			return selection;
		} else {
			String auxSelected = Input.string("\nSeleccione los elementos deseados, separ�ndolos por , : ");
			auxSelected.trim();
			auxSelected = auxSelected.replace(" ", "");
			String[] split = auxSelected.split(",");
			return Utils.parseIntArray(split);
		}
	}

	/**
	 * Transforma todos los elementos del array de tipo String y devuelve el
	 * array con los elementos transformados en elementos de tipo entero
	 *
	 * @param arr
	 *            un array de elementos de tipo {@link String}
	 * @return un array de elementos de tipo {@link Integer}
	 */
	static int[] parseIntArray(String[] arr) {
		ArrayList<Integer> list = new ArrayList<>();
		for (int i = 0; i < arr.length; i++) {
			list.add(Integer.valueOf(arr[i]) - 1);
		}
		return list.stream().mapToInt(i -> i).toArray();
	}

	/**
	 * Comprueba si el �ndice que se pasa como primer par�metro est� dentro de
	 * los l�mites de una lista de par�metros. Devuelve <code>true</code> si el
	 * �ndice es mayor o igual a 0 y menor o igual al tama�o de la lista,
	 * <code>false</code> en caso contrario
	 *
	 * @param i
	 *            El �ndice que se pasa por par�metro
	 * @param size
	 *            El tama�o de la lista de elementos
	 * @return <code>true</code> si el �ndice es mayor o igual a 0 y menor o
	 *         igual al tama�o de la lista, <code>false</code> en caso contrario
	 */
	public static boolean checkSelection(int i, int size) {
		if ((i >= 1) && (i <= size)) {
			return true;
		}
		return false;
	}
}
