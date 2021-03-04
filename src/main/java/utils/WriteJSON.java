package utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import race.ScoreCar;

public class WriteJSON {

	/**
	 * Escribe un JSON con una información mínima para probar el funcionamiento
	 */
	public void writeJSON() {
		JSONObject obj = new JSONObject();
		obj.put("brand", "Citroën");
		obj.put("model", "Xsara");
		obj.put("garage", "Rodovogo");

		JSONArray list = new JSONArray();
		list.add("msg 1");
		list.add("msg 2");
		list.add("msg 3");

		obj.put("messages", list);

		JSONObject obj2 = new JSONObject();
		obj2.put("cancion", "La Macarena");
		obj2.put("autor", "Los del Río");

		obj.put("canción", obj2);


		try (FileWriter file = new FileWriter("test.json")) {

			file.write(obj.toJSONString());
			file.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.print(obj);
	}

	/**
	 * Lee un JSON con una información mínima para probar su funcionamiento
	 */
	public void readJSON () {


		JSONParser parser = new JSONParser();

		try {

			Object obj = parser.parse(new FileReader("test.json"));

			JSONObject jsonObject = (JSONObject) obj;
			System.out.println(jsonObject);

			String brand = (String) jsonObject.get("brand");
			System.out.println(brand);

			String model = (String) jsonObject.get("model");
			System.out.println(model);

			String garage = (String) jsonObject.get("garage");
			System.out.println(garage);

			ScoreCar sc = new ScoreCar(brand, model);
			sc.setGarage(garage);

			System.out.println(sc.getDetails());

			// loop array
			//            JSONArray msg = (JSONArray) jsonObject.get("messages");
			//            Iterator<String> iterator = msg.iterator();
			//            while (iterator.hasNext()) {
			//                System.out.println(iterator.next());
			//            }

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		WriteJSON wj = new WriteJSON();
		wj.writeJSON();
		wj.readJSON();
	}
}

