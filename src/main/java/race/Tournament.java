package race;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import utils.ComparatorScoreCarByPoints;
import utils.ProgramExporter;

/**
 * Esta clase crea un nuevo torneo de un tipo específico de carreras, ya sean de
 * tipo {@link StandardRace} o {@link DeathRace}. Un torneo es una serie de
 * carreras en la que participan los mismos coches, y que van acumulando puntos
 * según la posición en la que queden. Al final del torneo, el {@link ScoreCar}
 * que tenga más puntos es el ganador
 *
 */
public class Tournament {

	/**
	 * Variable que se usa en la exportación / importación que guarda la lista
	 * de coches participantes en una carrera
	 */
	public static final String CARS_LIST = "carList";

	/**
	 * Variable que se usa en la exportación / importación que guarda la lista
	 * de garajes participantes en una carrera
	 */
	public static final String GARAGE_LIST = "garageList";

	/**
	 * Variable que se usa en la exportación / importación que guarda el nombre
	 * del torneo
	 */
	public static final String NAME = "name";

	/**
	 * Variable que se usa en la exportación / importación que guarda la lista
	 * de carreras de un torneo
	 */
	public static final String RACE_LIST = "raceList";

	/**
	 * Variable que guarda el comparador de coches del torneo, atendiendo a la
	 * puntuación que guarda cada uno de ellos internamente
	 * ({@link ScoreCar#getScore()})
	 */
	private final Comparator<ScoreCar> comparator = new ComparatorScoreCarByPoints();

	/**
	 * Variable que guarda la lista de garajes que participan en el torneo
	 */
	protected List<Garage> garageList = new ArrayList<>();

	/**
	 * Variable que guarda el nombre de la carrera
	 */
	protected String name;

	/**
	 * Variable que guarda la lista de carreras del torneo
	 */
	protected List<Race> raceList = new ArrayList<>();

	/**
	 * Variable que guarda la lista de coches participantes en el torneo
	 */
	protected List<ScoreCar> tournamentCarList = new ArrayList<>();

	/**
	 * Crea un nuevo torneo, al que se le pasa el nombre del torneo
	 *
	 * @param name
	 *            El nombre del torneo
	 */
	public Tournament(String name) {
		this.name = name;
	}

	/**
	 * Exporta un torneo y lo convierte en un objeto {@link JSONObject}
	 *
	 * @return El objeto {@link JSONObject} exportado
	 */
	public JSONObject exporterTournamentToJson() {
		JSONObject exporter = new JSONObject();
		exporter.put(Tournament.NAME, this.getName());

		JSONArray raceList = new JSONArray();
		for (Race r : this.getRaceList()) {
			raceList.add(r.exportRaceInfo());
		}
		exporter.put(Tournament.RACE_LIST, raceList);

		JSONArray garageList = new JSONArray();
		for (Garage g : this.getGarageList()) {
			garageList.add(g.exportGarageToJson());
		}
		exporter.put(Tournament.GARAGE_LIST, garageList);

		JSONArray carList = new JSONArray();
		for (ScoreCar sc : this.getTournamentCarList()) {
			carList.add(sc.exportCarToJson());
		}
		exporter.put(Tournament.CARS_LIST, carList);

		return exporter;
	}

	/**
	 * Devuelve el comparador usado para ordenar los {@link ScoreCar}
	 *
	 * @return El comparador usado para ordena los {@link ScoreCar}
	 */
	public Comparator<ScoreCar> getComparator() {
		return this.comparator;
	}

	/**
	 * Devuelve la lista de garajes ({@link Garage}) que participan en un torneo
	 *
	 * @return La lista de garrajes que participan en un torneo
	 */
	public List<Garage> getGarageList() {
		return this.garageList;
	}

	/**
	 * Devuelve el nombre del torneo
	 *
	 * @return El nombre del torneo
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Obtiene el podium de un torneo en forma de una cadena ({@link String})
	 *
	 * @return El podium de un torneo
	 */
	public String getPodiumTournament() {
		StringBuilder builder = new StringBuilder();

		for (ScoreCar sc : this.getTournamentCarList()) {
			builder.append("\t• ");
			builder.append(sc.getDetails());
			builder.append(" ");
			builder.append(sc.getScore());
			builder.append(" ptos.");
			builder.append("\n");

		}
		return builder.toString();
	}

	/**
	 * Devuelve la lista de carreras pertenecientes al torneo
	 *
	 * @return La lista de carreras pertenecientes al torneo
	 */
	public List<Race> getRaceList() {
		return this.raceList;
	}

	/**
	 * Devuelve la lista de coches ({@link ScoreCar}) participantes en un torneo
	 *
	 * @return Devuelve la lista de coches participantes en un torneo
	 */
	public List<ScoreCar> getTournamentCarList() {
		return this.tournamentCarList;
	}

	/**
	 * Devuelve toda la información del torneo,
	 *
	 * @return Toda la información del torneo
	 */
	public String getTournamentInfo() {
		StringBuilder builder = new StringBuilder();
		builder.append("Torneo: ");
		builder.append(this.getName());
		builder.append("\n");
		builder.append("\n");
		builder.append("Garajes: ");
		builder.append("\n");
		for (Garage g : this.getGarageList()) {
			builder.append("\t");
			builder.append("• ");
			builder.append(g.getName());
			builder.append("\n");
		}

		builder.append("\n");
		builder.append("Coches: ");
		builder.append("\n");
		for (ScoreCar sc : this.getTournamentCarList()) {
			builder.append("\t");
			builder.append("• ");
			builder.append(sc.getDetails());
			builder.append("\n");
		}

		builder.append("\n");
		builder.append("Carreras: ");
		builder.append("\n");
		for (int i = 0; i < this.getRaceList().size(); i++) {
			Race r = this.getRaceList().get(i);
			builder.append("\tCarrera #");
			builder.append(i + 1);
			builder.append("\n");
			builder.append(r.getPodiumForTournament());
			builder.append("\n");
		}
		builder.append("\n");
		builder.append("Clasificación del torneo: ");
		builder.append("\n");
		builder.append(this.getPodiumTournament());

		return builder.toString();
	}

	/**
	 * Registra el coche suministrado por parámetro en el torneo
	 *
	 * @param sc
	 *            El coche a registrar como participante en un torneo
	 */
	public void registerCarInTournament(ScoreCar sc) {
		this.getTournamentCarList().add(sc);

	}

	/**
	 * Registra los coches participantes en el torneo en todas las carreras de
	 * este último
	 */
	public void registerCarsInEachRace() {
		for (Race r : this.getRaceList()) {
			r.registerCars(this.getTournamentCarList());
		}

	}

	/**
	 * Registra los coches que participan en el torneo. Si sólo existe un garaje
	 * en el torneo, se registran todos los coches de ese garaje. Si existen más
	 * garajes, sólo se registra un coche aleatorio de cada uno de los garajes
	 */
	public void registerCarsInTournament() {
		if (this.getGarageList().size() == 1) {
			Garage w = this.getGarageList().get(0);
			List<ScoreCar> carListFromGarage = w.getGarageCarList();
			this.getTournamentCarList().addAll(carListFromGarage);
		} else {
			for (Garage w : this.getGarageList()) {
				this.getTournamentCarList().add(w.getRandomCarFromList());
			}
		}
	}

	/**
	 * Este método registra en las carreras que conforman el torneo todos los
	 * los garages que participan en dicho torneo, se obtienen los coches que se
	 * registrarán en el torneo ({@link #registerCarsInTournament()}) y se
	 * registran todos los coches que participan en el torneo en cada una de las
	 * carreras
	 */
	public void registerGaragesAndCars() {
		this.registerGaragesInEachRace();
		this.registerCarsInTournament();
		this.registerCarsInEachRace();
	}

	/**
	 * Registra todos los garajes de la lista de garajes participantes del
	 * torneo en cada uno de las carreras que conforman el torneo
	 */
	public void registerGaragesInEachRace() {
		for (Race r : this.getRaceList()) {
			for (Garage g : this.getGarageList()) {
				r.registerGarage(g);
			}
		}
	}

	/**
	 * Registra el garaje pasado por parámetro en la lista de garajes
	 * participantes en el torneo
	 *
	 * @param g
	 *            El garaje para registrar en el torneos
	 */
	public void registerGarageToTournament(Garage g) {
		this.getGarageList().add(g);

	}

	/**
	 * Registra la carrera pasada por parámetro en la lista de carreras
	 * participantes
	 *
	 * @param r
	 *            La carrera para registrar en el torneo
	 */
	public void registerRaceToTournament(Race r) {
		this.getRaceList().add(r);
	}

	/**
	 * Elimina el garaje pasado por parámetro de la lista de garajes
	 * participantes de un torneo
	 *
	 * @param g
	 *            El garaje a eliminar de la lista de garajes participantes de
	 *            un torneo
	 */
	public void removeGarageFromTournament(Garage g) {
		this.getGarageList().remove(g);

	}

	/**
	 * Elimina la carrera pasada por parámetro de la lista de carreras
	 * participantes de un torneo
	 *
	 * @param r
	 *            La carrera a eliminar de la lista de carreras
	 */
	public void removeRaceFromTournament(Race r) {
		this.getRaceList().remove(r);
	}

	/**
	 * Invierte la posición de los elementos de la lista pasada por parámetro
	 *
	 * @param list
	 *            La lista que invertirá la posición de los elementos
	 */
	private void reverseListByPoints(List<ScoreCar> list) {
		Collections.reverse(list);

	}

	/**
	 * Establece el nombre del torneo
	 *
	 * @param name
	 *            el nombre del torneo
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Establece la lista de carreras que conforma un torneo
	 *
	 * @param raceList
	 *            La lista de carreras que conforman un torneo
	 */
	public void setRaceList(List<Race> raceList) {
		this.raceList = raceList;
	}

	/**
	 * Registra la lista de {@link ScoreCar} pasada por parámetro como lista de
	 * coches participantes en un torneo
	 *
	 * @param tournamentCarList
	 *            La lista de {@link ScoreCar} que participarán en el torneo
	 */
	public void setTournamentCarList(List<ScoreCar> tournamentCarList) {
		this.tournamentCarList = tournamentCarList;
	}

	/**
	 * Ordena la lista de {@link ScoreCar} participantes en el torneo según la
	 * puntuación, de menor a mayor, y luego invierte la lista
	 *
	 * @param list
	 *            La lista de {@link ScoreCar} que será reordenada
	 */
	public void sortAndReverseListByPoints(List<ScoreCar> list) {
		this.sortListByPoints(list);
		this.reverseListByPoints(list);
	}

	/**
	 * Ordena la lista de {@link ScoreCar} participantes en el torneo según la
	 * puntuación, de menor a mayor
	 *
	 * @param list
	 */
	private void sortListByPoints(List<ScoreCar> list) {
		Collections.sort(list, this.getComparator());

	}

	/**
	 * Transforma el objeto {@link JSONObject} en un nuevo torneo
	 *
	 * @param tournamentJsonObject
	 *            El {@link JSONObject} a transformar en {@link Tournament}
	 * @return Un torneo ({@link Tournament})
	 */
	public static Tournament importTournament(JSONObject tournamentJsonObject) {
		String name = (String) tournamentJsonObject.get(Tournament.NAME);
		Tournament t = new Tournament(name);

		JSONArray arrayRace = (JSONArray) tournamentJsonObject.get(Tournament.RACE_LIST);
		for (Object raceJsonObject : arrayRace) {
			JSONObject raceJson = (JSONObject) raceJsonObject;
			Race r = Race.importRace(raceJson);
			t.registerRaceToTournament(r);
		}

		JSONArray arrayGarage = (JSONArray) tournamentJsonObject.get(Tournament.GARAGE_LIST);
		for (Object garageJsonObject : arrayGarage) {
			JSONObject garageJson = (JSONObject) garageJsonObject;
			Garage g = Garage.importGarageFromJSONObject(garageJson);
			t.registerGarageToTournament(g);
		}

		JSONArray arrayCars = (JSONArray) tournamentJsonObject.get(Tournament.CARS_LIST);
		for (Object carsJsonObject: arrayCars) {
			JSONObject carsJson = (JSONObject) carsJsonObject;
			ScoreCar sc = ScoreCar.importCarFromJson(carsJson);
			t.registerCarInTournament(sc);
		}

		return t;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getName());
		builder.append(" (");
		builder.append(this.getRaceList().size());
		builder.append(" carreras)");
		return builder.toString();
	}

	public static void main(String[] args) {

		Tournament t = new Tournament("Pachanguita");
		Garage g = new Garage("Escudería Mari Carmen");
		Garage g1 = new Garage("Escudería Mari Pili");
		Garage g2 = new Garage("Escudería Mari Trini");
		Garage g3 = new Garage("Escudería Mari Loli");
		Garage g4 = new Garage("Escudería Mari Flori");
		Race r = new DeathRace("Pepa Pig", 20);
		Race r2 = new DeathRace("Raider", 20);
		Race r3 = new DeathRace("Chase", 20);
		Race r4 = new DeathRace("Marshall", 20);
		Race r5 = new DeathRace("Rocky", 20);
		Race r6 = new DeathRace("Zuma", 20);
		t.registerGarageToTournament(g);
		t.registerGarageToTournament(g1);
		t.registerGarageToTournament(g2);
		t.registerGarageToTournament(g3);
		t.registerGarageToTournament(g4);
		t.registerRaceToTournament(r);
		t.registerRaceToTournament(r2);
		t.registerRaceToTournament(r3);
		t.registerRaceToTournament(r4);
		t.registerRaceToTournament(r5);
		t.registerRaceToTournament(r6);
		t.registerGaragesAndCars();
		for (Race race : t.getRaceList()) {
			race.startRace();
			race.givePoints();
			//			System.out.print(race.getPodium());
			//			System.out.println();
			race.resetCars();
		}

		t.sortAndReverseListByPoints(t.getTournamentCarList());

		//		for (ScoreCar sc : t.getTournamentCarList()) {
		//			System.out.println(sc.getDetails() + " " + sc.getScore());
		//
		//		}

		JSONObject exportedTournament = t.exporterTournamentToJson();
		System.out.println(exportedTournament.toJSONString());

		Tournament t2 = Tournament.importTournament(exportedTournament);
		for (Race race : t2.getRaceList()) {
			race.startRace();
			race.givePoints();
			race.resetCars();
		}
		t.sortAndReverseListByPoints(t.getTournamentCarList());
		System.out.println(t2.getTournamentInfo());

		ProgramExporter pe = new ProgramExporter();
		pe.exportTournament(t2, new File("tournament.txt"));

	}

}
