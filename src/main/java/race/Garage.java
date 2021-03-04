package race;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import utils.ProgramExporter;
import utils.Utils;

/**
 * Clase que contiene la información de un garaje y la lista de coches asociados
 * a dicho garaje
 */
public class Garage {

	/**
	 * Constante que hace referencia a la lista de los coches que pertenecen a
	 * un garaje cuando se realiza la exportación / importación
	 */
	public static final String CAR_LIST = "carList";

	/**
	 * Constante que hace referencia al nombre del garaje cuando se realiza la
	 * exportación / importación
	 */
	public static final String NAME = "name";

	/**
	 * Lista que contiene los coches ({@link ScoreCar}) que pertenen al garaje
	 */
	List<ScoreCar> garageCarList = new ArrayList<>();

	/**
	 * Almacena el nombre del garaje
	 */
	protected String name;

	/**
	 * Contructor de la clase {@link Garage}
	 *
	 * @param name
	 *            El nombre del garaje
	 */
	public Garage(String name) {
		this.name = name;
		// Se comenta para evitar que se creen coches automáticamente
		// this.init();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Garage) {
			if (((Garage) obj).getName().equalsIgnoreCase(this.getName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Exporta el objeto garaje a un objeto {@link JSONObject} (sin la lista de
	 * {@link ScoreCar} pertenecientes a un garaje)
	 *
	 * @return Convierte el garaje en un {@link JSONObject}
	 */
	public JSONObject exportGarageToJson() {
		JSONObject export = new JSONObject();
		export.put(Garage.NAME, this.getName());

		return export;

	}

	/**
	 * Exporta el objeto garaje a un objeto {@link JSONObject} (con la lista de
	 * {@link ScoreCar} pertenecientes a un garaje)
	 *
	 * @return Convierte el garaje en un {@link JSONObject}
	 */
	public JSONObject exportGarageWithCarsToJson() {
		JSONObject export = new JSONObject();
		export.put(Garage.NAME, this.getName());

		JSONArray carList = new JSONArray();
		for (ScoreCar sc : this.getGarageCarList()) {
			carList.add(sc.exportCarToJson());
		}

		export.put(Garage.CAR_LIST, carList);

		return export;

	}

	/**
	 * Devuelve una {@link Lista} con los {@link ScoreCar} que pertenecen a un
	 * garaje
	 *
	 * @return La lista de {@link ScoreCar} que pertenecen al garaje
	 */
	public List<ScoreCar> getGarageCarList() {
		return this.garageCarList;
	}

	/**
	 * Devuelve el nombre del garaje
	 *
	 * @return El nombre del garaje
	 */
	public String getName() {
		return this.name;
	}


	/**
	 * Escoje un {@link ScoreCar} aleatorio de la lista de coches asociados al
	 * garaje
	 *
	 * @return Un {@link ScoreCar} aleatorio de la lista de coches
	 *         pertenecientes al garaje
	 */
	public ScoreCar getRandomCarFromList() {
		int minValue = 0;
		int maxValue = this.getGarageCarList().size();
		int index = Utils.getRandomNumberInRange(minValue, maxValue - 1);
		return this.getGarageCarList().get(index);
	}

	/**
	 * Crea 3 coches y los añade a la lista de coches del garaje
	 */
	public void init() {
		ScoreCar sc1 = new ScoreCar("Citroën", "Xsara");
		this.registerCarToGarage(sc1);
		this.registerCarToGarage(new ScoreCar("Seat", "Arosa"));
		this.registerCarToGarage(new ScoreCar("Volkswagen", "Golf"));

	}

	/**
	 * Añade el {@link ScoreCar} que se pasa por parámetro se añade a la lista
	 * de de coches
	 *
	 * @param sc
	 *            El coche a añadir a la lista de coches
	 */
	public void registerCarToGarage(ScoreCar sc) {
		sc.setGarage(this.getName());
		this.getGarageCarList().add(sc);
	}

	/**
	 * Elimina el {@link ScoreCar} que se pasa por parámetro de la lista de
	 * coches del garaje.
	 *
	 * @param sc
	 *            El {@link ScoreCar} a eliminar
	 */
	public void removeCarFromGarage(ScoreCar sc) {
		if (this.getGarageCarList().contains(sc)) {
			this.getGarageCarList().remove(sc);
		}
	}

	/**
	 * Establece el nombre del garaje
	 *
	 * @param name
	 *            El nombre del garaje
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.getName();
	}

	/**
	 * Crea un nuevo garaje a partir de un objeto {@link JSONObject}
	 *
	 * @param garageJson
	 *            el objeto {@link JSONObject} para construr el garaje
	 * @return un objeto {@link Garage}
	 */
	public static Garage importGarageFromJSONObject(JSONObject garageJson) {
		String name = (String) garageJson.get(Garage.NAME);
		JSONArray carList = (JSONArray) garageJson.get(Garage.CAR_LIST);
		Garage g = new Garage(name);
		if (carList != null) {
			for (Object actual : carList) {
				JSONObject car = (JSONObject) actual;
				ScoreCar sc = ScoreCar.importCarFromJson(car);
				g.registerCarToGarage(sc);
			}
		}
		return g;
	}

	public static void main(String[] args) {
		Garage w = new Garage("Rodovogo");
		for (ScoreCar sc : w.getGarageCarList()) {
			sc.getDetails();
		}

		w.getRandomCarFromList().getDetails();

		JSONObject exportGarageToJson = w.exportGarageToJson();
		String exportGarageToJsonString = exportGarageToJson.toJSONString();
		System.out.println(exportGarageToJsonString);
		JSONObject toImport = ProgramExporter.stringToJsonObject(exportGarageToJsonString);
		Garage h = Garage.importGarageFromJSONObject(toImport);
		h.getGarageCarList();
	}
}
