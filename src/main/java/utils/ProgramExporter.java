package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import race.Race;
import race.Tournament;

public class ProgramExporter {

	/**
	 * Variable que almacena el parseador de {@link JSONParser}
	 */
	private static JSONParser parser = null;

	/**
	 * Exporta el podio de una carrera a un fichero pasado por parámetro
	 *
	 * @param r
	 *            La carrera a exportar el podio
	 * @param file
	 *            El fichero al que será exportado
	 */
	public void exportPodiumRace(Race r, File file) {
		String content = r.getPodium();
		ProgramExporter.baseSkeletonToExport(content, file);
	}

	/**
	 * Exporta el podio de un torneo a un fichero pasado por parámetro
	 *
	 * @param t
	 *            El torneo a exportar
	 * @param file
	 *            El fichero al que será exportado
	 */
	public void exportTournament(Tournament t, File file) {
		String content = t.getTournamentInfo();
		ProgramExporter.baseSkeletonToExport(content, file);
	}

	/**
	 * Exporta el contenido pasado por parámetro a un fichero
	 *
	 * @param content
	 *            El contenido a exportar
	 * @param f
	 *            El fichero al que será exportado
	 */
	public static void baseSkeletonToExport(String content, File f) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
			bw.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Convierte en un {@link JSONObject} un {@link JSONArray}, siendo la clave
	 * la cadena pasada por parámeto, en el valor el {@link JSONArray}
	 *
	 * @param listName
	 *            La clave pasada por parámetro
	 * @param array
	 *            El valor pasado por parámetro
	 * @return Un {@link JSONObject} compuesto por una clave de tipo
	 *         {@link String} y un valor de tipo {@link JSONArray}
	 */
	public static JSONObject exportArrayToObject(String listName, JSONArray array) {
		JSONObject obj = new JSONObject();
		obj.put(listName, array);
		return obj;
	}

	/**
	 * Devuelve el parse utilizado para la importación
	 *
	 * @return El {@link JSONParser} que se utiliza para la importación
	 */
	public static JSONParser getParser() {
		if (ProgramExporter.parser == null) {
			ProgramExporter.parser = new JSONParser();
		}

		return ProgramExporter.parser;
	}


	/**
	 * Convierte la representación de un {@link JSONObject} en {@link String} a
	 * un {@link JSONObject}
	 *
	 * @param importedJsonObjectString
	 *            la representación en una cadena de una {@link JSONObject}
	 * @return El {@link JSONObject}
	 */
	public static JSONObject stringToJsonObject(String importedJsonObjectString) {
		try {
			JSONObject parsedObject = (JSONObject) ProgramExporter.getParser().parse(importedJsonObjectString);
			return parsedObject;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}