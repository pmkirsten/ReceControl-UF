package utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Utils {

	/**
	 * Formatea el {@link Double} que se pasa por parámetro, con el formato #.##
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
	 * Obtiene un número aleatorio selecionado entre los valores pasado por
	 * parámetros. Si el límite inferior es superior al mayor, se intercambia
	 *
	 * @param min
	 *            El límite inferior del rango
	 * @param max
	 *            El límite superior del rango
	 * @return Un número aleatorio entre el rango seleccionado.
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
	 * Muestra una lista con índices, y permite que muestre un mensaje para
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
	 * Muestra una lista con índices, y permite que muestre un mensaje para
	 * esperar, y excluye los elementos de la primera lista que estén presentes
	 * en la segunda.
	 *
	 * @param list
	 *            La lista a ser mostrada
	 * @param wait
	 *            <code>true</code> para esperar despues de mostrar la lista,
	 *            <code>false</code> en caso contrario.
	 * @param excludeElements
	 *            Excluye los elementos que existan en esta lista de la lista
	 *            pasada por parámetro
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
	 * Devuelve un array de un elemento con la opción seleccionada de la lista,
	 * y con la opción de poder cancelar esa opción
	 *
	 * @param list
	 *            Lista de la que se va a seleccionar
	 * @param cancel
	 *            <code>true</code> para poder cancelar la selección,
	 *            <code>false</code> en caso contrario
	 * @return Un array de un elemento con la posición que ocupa el elemento
	 *         seleccionado en la lista
	 */
	public static <T> int[] showAndSelectFromList(List<T> list, boolean cancel) {
		return Utils.showAndSelectFromList(list, cancel, false);
	}

	/**
	 * Devuelve un array de mútiples elementos con la opción seleccionada de la
	 * lista, y con la opción de poder cancelar esa opción, o bien devolver un
	 * array con las posiciones de las selecciones
	 *
	 * @param list
	 *            Lista de la que se va a seleccionar
	 * @param cancel
	 *            <code>true</code> para poder cancelar la selección,
	 *            <code>false</code> en caso contrario. En caso de que el
	 *            parámetro de multipleReturn este a <code>true</code> este
	 *            parámetro no tiene funcionalidad
	 * @param multipleReturn
	 *            <code>true</code> para que el array pueda contener múltiples
	 *            selecciones, <code>false</code> en caso contrario.
	 * @return Una lista con uno o múltiples elementos seleccionado de la lista
	 */
	public static <T> int[] showAndSelectFromList(List<T> list, boolean cancel, boolean multipleReturn) {
		return Utils.showAndSelectFromList(list, cancel, multipleReturn, null);
	}

	/**
	 * Devuelve un array de mútiples elementos con la opción seleccionada de la
	 * lista, y con la opción de poder cancelar esa opción, o bien devolver un
	 * array con las posiciones de las selecciones. Se pueden excluir los
	 * elementos de la segunda lista
	 *
	 * @param list
	 *            Lista de la que se va a seleccionar
	 * @param cancel
	 *            <code>true</code> para poder cancelar la selección,
	 *            <code>false</code> en caso contrario. En caso de que el
	 *            parámetro de multipleReturn este a <code>true</code> este
	 *            parámetro no tiene funcionalidad
	 * @param multipleReturn
	 *            <code>true</code> para que el array pueda contener múltiples
	 *            selecciones, <code>false</code> en caso contrario.
	 * @param excludeElements
	 *            Elementos de la lista que serán excluidas.
	 * @return Una lista con uno o múltiples elementos seleccionado de la lista
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
				selected = Input.integer("La opción no es válida, por favor, escoje una opción válida: ");
			}
			int[] selection = new int[1];
			selection[0] = selected - 1;
			return selection;
		} else {
			String auxSelected = Input.string("\nSeleccione los elementos deseados, separándolos por , : ");
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
	 * Comprueba si el índice que se pasa como primer parámetro está dentro de
	 * los límites de una lista de parámetros. Devuelve <code>true</code> si el
	 * índice es mayor o igual a 0 y menor o igual al tamaño de la lista,
	 * <code>false</code> en caso contrario
	 *
	 * @param i
	 *            El índice que se pasa por parámetro
	 * @param size
	 *            El tamaño de la lista de elementos
	 * @return <code>true</code> si el índice es mayor o igual a 0 y menor o
	 *         igual al tamaño de la lista, <code>false</code> en caso contrario
	 */
	public static boolean checkSelection(int i, int size) {
		if ((i >= 1) && (i <= size)) {
			return true;
		}
		return false;
	}
}
