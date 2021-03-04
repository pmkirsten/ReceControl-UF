package race;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import utils.Input;
import utils.ProgramExporter;
import utils.Utils;

/**
 * La clase Control permite manejar los torneos, carreras, garajes y coches como
 * un control central
 */
public class Control {

	/**
	 * Constante que sirve para indicar la lista de garajes en la exportación e
	 * importación
	 */
	public static final String GARAGES = "garages";

	/**
	 * Constante que sirve para indicar la lista de carreras en la exportación e
	 * importación
	 */
	public static final String RACES = "races";

	/**
	 * Constante que sirve para indicar la lista de torneos en la exportación e
	 * importación
	 */
	public static final String TOURNAMENT = "tournament";

	/**
	 * Variable que contiene la lista de garajes
	 */
	protected List<Garage> garageList = new ArrayList<>();

	/**
	 * Variable que contiene la lista de carreras
	 */
	protected List<Race> raceList = new ArrayList<>();

	/**
	 * Variable que contiene la lista de torneos
	 */
	protected List<Tournament> tournamentList = new ArrayList<>();

	/**
	 * Constructor de la clase {@link Control}
	 */
	public Control() {}

	/**
	 * Crea un nuevo coche y lo añade a un garaje existente
	 */
	public void addCarToGarage() {
		System.out.println("================");
		System.out.println("= Añadir Coche =");
		System.out.println("================");
		String brand = Input.string("Introduce la marca del coche: ");
		String model = Input.string("Introduce el modelo del coche: ");
		System.out.println("Selecciona el garaje al que pertenece el coche:");
		int[] selected = Utils.showAndSelectFromList(this.getGarageList(), false);
		this.getGarageList().get(selected[0]).registerCarToGarage(new ScoreCar(brand, model));
	}

	/**
	 * Crea un nuevo garage y lo añade a la lista de garajes que maneja control
	 */
	public void addGarageToList() {
		System.out.println("=================");
		System.out.println("= Añadir Garaje =");
		System.out.println("=================");
		String name = Input.string("Introduce el nombre del garaje: ");
		Garage g = new Garage(name);
		if (!this.getGarageList().contains(g)) {
			this.getGarageList().add(g);
		}

	}

	/**
	 * Añade a la carrera seleccionada los garages sleccionado
	 */
	public void addGarageToRace() {
		System.out.println("=================================");
		System.out.println("= Agregar garajes a una carrera =");
		System.out.println("=================================");
		System.out.println("");
		int[] selected = Utils.showAndSelectFromList(this.getRaceList(), true);
		if (selected[0] >= 0) {
			int[] selectedGarages = Utils.showAndSelectFromList(this.getGarageList(), true, true);
			Race r = this.getRaceList().get(selected[0]);
			for (int i = 0; i < selectedGarages.length; i++) {
				r.registerGarage(this.getGarageList().get(selectedGarages[i]));
			}
			r.registerCars();
		}
	}

	/**
	 * Añade un garaje de los disponibles en la lista de garajes, a los garajes
	 * participantes en el torneo
	 */
	public void addGarageToTournament() {
		System.out.println("===============================");
		System.out.println("= Agregar garajes a un torneo =");
		System.out.println("===============================");
		System.out.println("");
		int[] selected = Utils.showAndSelectFromList(this.getTournamentList(), true);
		if (selected[0] >= 0) {
			Tournament t = this.getTournamentList().get(selected[0]);
			int[] selectedGarages = Utils.showAndSelectFromList(this.getGarageList(), true, true, t.getGarageList());
			for (int i = 0; i < selectedGarages.length; i++) {
				t.getGarageList().add(this.getGarageList().get(selectedGarages[i]));
			}
		}
	}

	/**
	 * Crea una nueva carrera y la añade a la lista de carreras disponibles
	 */
	public void addRaceToList() {
		System.out.println("==================");
		System.out.println("= Añadir Carrera =");
		System.out.println("==================");
		String name = Input.string("Introduce el nombre de la carrera: ");
		String type = Input.string("Introduce el tipo de carrera.\n\"DR\" - Eliminación \\ \"SR\" - Estándar (Por defecto) : ");
		String secondArgumentMessage;
		if (type.equalsIgnoreCase(Race.RACE_TYPE_DEATHRACE)) {
			secondArgumentMessage = "Introduce las vueltas previas antes de la eliminación: ";
		} else {
			secondArgumentMessage = "Introduce la duración en horas de la carrera: ";
		}
		int secondArgument = Input.integer(secondArgumentMessage);
		Race r;
		if (type.equalsIgnoreCase(Race.RACE_TYPE_DEATHRACE)) {
			r = new DeathRace(name, secondArgument);
		} else {
			r = new StandardRace(name, secondArgument);
		}
		if (!this.getRaceList().contains(r)) {
			this.raceList.add(r);
		}

	}

	/**
	 * Añade una carrera de la lista de carreras disponibles al torneo
	 * seleccionado de la lista de torneos
	 */
	public void addRaceToTournament() {
		System.out.println("============================");
		System.out.println("= Añadir carrera al torneo =");
		System.out.println("============================");
		System.out.println("Selecciona el torneo: ");
		int[] selected = Utils.showAndSelectFromList(this.getTournamentList(), false);
		Tournament t = this.getTournamentList().get(selected[0]);
		this.addRaceToTournament(t);

	}

	/**
	 * Añade al torneo pasado por parámetro la carrera seleccionada de la lista
	 * de carreras disponibles
	 *
	 * @param t
	 *            El torneo seleccionado
	 */
	public void addRaceToTournament(Tournament t) {
		System.out.println("Selecciona las carreras que se le añadirán: ");
		int[] selectedRaces = Utils.showAndSelectFromList(this.getRaceList(), true, true, t.getRaceList());
		for (int i = 0; i < selectedRaces.length; i++) {
			Race race = this.getRaceList().get(selectedRaces[i]).clone();
			t.getRaceList().add(race);
		}
	}

	/**
	 * Crea un nuevo torneo y lo añade a la lista de torneos disponibles
	 */
	public void addTournamentToList() {
		System.out.println("=================");
		System.out.println("= Añadir Torneo =");
		System.out.println("=================");
		String name = Input.string("Introduce el nombre del torneo: ");
		Tournament t = new Tournament(name);
		this.addRaceToTournament(t);
		this.getTournamentList().add(t);
	}

	/**
	 * Exporta todos los datos de la aplicación a un formato {@link JSONObject}
	 * a un fichero concreto
	 *
	 * @param f
	 *            El fichero a que se va a exportar
	 */
	public void exportAppData() {
		ProgramExporter.baseSkeletonToExport(this.exportAppDataToString(), new File(".appData.json"));
		System.out.println("Programa finalizado.");
	}

	/**
	 * Exporta los datos de la aplicación a un fichero específico
	 */
	public void exportAppDataToFile() {
		String filename = Input.string("Introduce el nombre del fichero para exportar: ");
		if (filename.equals(".appData.json")) {
			filename = "appData.json";
		}
		ProgramExporter.baseSkeletonToExport(this.exportAppDataToString(), new File(filename));
		System.out.println("Programa finalizado.");
	}

	/**
	 * Crea un {@link String} con el {@link JSONObject} que se se obtiene al
	 * exportar toda la información de la aplicación
	 *
	 * @return Un {@link String} del {@link JSONObject} de la información de
	 *         toda la aplicación
	 */
	public String exportAppDataToString() {
		JSONObject obj = new JSONObject();
		obj.put(Control.GARAGES, this.exportGaragesArray());
		obj.put(Control.RACES, this.exportRacesArray());
		obj.put(Control.TOURNAMENT, this.exportTournamentArray());
		return obj.toJSONString();
	}

	/**
	 * Exporta los datos de los garajes y los coches a un fichero que le indica
	 * el usuario
	 */
	public JSONArray exportGaragesArray() {
		JSONArray list = new JSONArray();
		for (Garage g : this.getGarageList()) {
			list.add(g.exportGarageWithCarsToJson());
		}
		return list;
	}

	/**
	 * Exporta los datos de los garajes y los coches a un fichero que le indica
	 * el usuario
	 */
	public void exportGaragesDataToFile() {
		String filename = Input.string("Introduce el nombre del fichero para exportar: ");
		if (filename.equals(".appData.json")) {
			filename = "appData.json";
		}
		JSONObject exportedObject = ProgramExporter.exportArrayToObject(Control.GARAGES, this.exportGaragesArray());
		ProgramExporter.baseSkeletonToExport(exportedObject.toJSONString(), new File(filename));
	}

	/**
	 * Exporta los datos de las carreras, con los garajes y coches que
	 * participan en ella en un objeto {@link JSONArray}
	 *
	 * @return El {@link JSONArray} con la información de las carreras
	 */
	public JSONArray exportRacesArray() {
		JSONArray list = new JSONArray();
		for (Race r : this.getRaceList()) {
			list.add(r.exportRaceWithGarageAndCarsToJson());
		}
		return list;
	}

	/**
	 * Exporta los datos de las carreras, con los garajes y coches que
	 * participan en ella
	 */
	public void exportRacesDataToFile() {
		String filename = Input.string("Introduce el nombre del fichero para exportar: ");
		if (filename.equals(".appData.json")) {
			filename = "appData.json";
		}
		JSONObject exportedObject = ProgramExporter.exportArrayToObject(Control.RACES, this.exportRacesArray());
		ProgramExporter.baseSkeletonToExport(exportedObject.toJSONString(), new File(filename));
	}

	/**
	 * Exporta los datos de los torneos, con las carreras, garajes y coche que
	 * participan en ella en un formato {@link JSONArray}
	 *
	 * @return El {@link JSONArray} que contiene la información de los torneos
	 */
	public JSONArray exportTournamentArray() {
		JSONArray list = new JSONArray();
		for (Tournament t : this.getTournamentList()) {
			list.add(t.exporterTournamentToJson());
		}
		return list;
	}

	/**
	 * Exporta los datos de los torneos, con las carreras, garajes y coche que
	 * participan en ella en un fichero que le indica el usuario
	 */
	public void exportTournamentDataToFile() {
		String filename = Input.string("Introduce el nommbre del fichero para exportar: ");
		if (filename.equals(".appData.json")) {
			filename = "appData.json";
		}
		JSONObject exportedObject = ProgramExporter.exportArrayToObject(Control.TOURNAMENT, this.exportTournamentArray());
		ProgramExporter.baseSkeletonToExport(exportedObject.toJSONString(), new File(filename));

	}

	/**
	 * Devuelve el {@link Garage} de la lista de {@link #garageList} pasándole
	 * por parámetro el nombre del garaje
	 *
	 * @param name
	 *            El nombre del garaje
	 * @return El garaje cuyo nombre coincide con el parámetro de entrada
	 */
	public Garage getGarageFromList(String name) {
		for (Garage g : this.getGarageList()) {
			if (g.getName().equalsIgnoreCase(name)) {
				return g;
			}
		}
		return null;
	}

	/**
	 * Devuelve la lista de garajes que contiene
	 *
	 * @return La lista de garajes
	 */
	public List<Garage> getGarageList() {
		return this.garageList;
	}

	/**
	 * Devuelve la lista de carreras
	 *
	 * @return La lista de carreras
	 */
	public List<Race> getRaceList() {
		return this.raceList;
	}

	/**
	 * Devuelve la lista de torneos disponibles
	 *
	 * @return la lista de torneos disponibles
	 */
	public List<Tournament> getTournamentList() {
		return this.tournamentList;
	}

	/**
	 * Importa todos los datos de una aplicación de un formato JSON
	 *
	 * @throws ParseException
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public void importAppData() throws FileNotFoundException, IOException, ParseException {
		this.importAppDataFromFile(".appData.json");
	}

	/**
	 * Importa toda la información de la aplicación a través de un
	 * {@link String}, que es la representación de un {@link JSONObject} con la
	 * información de la aplicación
	 *
	 * @param string
	 *            Un {@link String}, representación de un {@link JSONObject}
	 */
	public void importAppDataFromFile(String string) {
		try {
			if (string.isEmpty()) {
				string = Input.string("Introduce el nombre del fichero para importar: ");
			}
			JSONObject appData = (JSONObject) ProgramExporter.getParser().parse(new FileReader(string));
			this.importGarageData(appData);
			this.importRaceData(appData);
			this.importTournamentData(appData);
		} catch (FileNotFoundException e) {
			System.err.println("No se ha podido encontrar el fichero \"" + string + "\" para cargar la información previa");
		} catch (IOException e) {
			System.err.println("Existión un error mientras se leía el fichero \"" + string + "\" para cargar la información previa");
		} catch (ParseException e) {
			System.err.println("No se ha podido leer el fichero \"" + string + "\" para cargar la información previa");
		}
	}

	/**
	 * Importa los datos de los garajes y los coches de un fichero
	 * {@link JSONObject}
	 *
	 * @param appData
	 *            el {@link JSONObject} que contiene la información de los
	 *            garages
	 */
	public void importGarageData(JSONObject appData) {
		JSONArray garagesList = (JSONArray) appData.get(Control.GARAGES);
		for (Object obj : garagesList) {
			Garage g = Garage.importGarageFromJSONObject((JSONObject) obj);
			if (!this.getGarageList().contains(g)) {
				this.getGarageList().add(g);
			}
		}
	}


	/**
	 * Importa los datos de los garajes y los coches de un fichero que se le
	 * indica
	 */
	public void importGarageDataFromFile() {
		String filename = "";
		try {
			filename = Input.string("Introduce el nombre del fichero para importar: ");
			JSONObject garageData = (JSONObject) ProgramExporter.getParser().parse(new FileReader(filename));
			this.importGarageData(garageData);
		} catch (FileNotFoundException e) {
			System.err.println("No se ha podido encontrar el fichero \"" + filename + "\" para cargar la información previa");
		} catch (IOException e) {
			System.err.println("Existión un error mientras se leía el fichero \"" + filename + "\" para cargar la información previa");
		} catch (ParseException e) {
			System.err.println("No se ha podido leer el fichero \"" + filename + "\" para cargar la información previa");
		}
	}

	/**
	 * Importa todos los datos de las carreras de un {@link JSONObject} que se
	 * pasa por parámetro
	 *
	 * @param raceData
	 *            Un {@link JSONObject} con información sobre las carreras
	 */
	public void importRaceData(JSONObject raceData) {
		JSONArray racesList = (JSONArray) raceData.get(Control.RACES);
		for (Object obj : racesList) {
			Race r = Race.importRace((JSONObject) obj);
			for (Garage g : r.getGarageList()) {
				if (!this.getGarageList().contains(g)) {
					this.getGarageList().add(g);
				}

				for (ScoreCar sc : r.getCarList()) {
					if (sc.getGarage().equalsIgnoreCase(g.getName())) {
						Garage garage = this.getGarageFromList(g.getName());
						if (!garage.getGarageCarList().contains(sc)) {
							garage.registerCarToGarage(sc);
						}
					}
				}
			}
			if (!this.getRaceList().contains(r)) {
				this.getRaceList().add(r);
			}
		}
	}

	/**
	 * Importa los datos de las carreras de un fichero que se le indica
	 */
	public void importRacesDataFromFile() {
		String filename = "";
		try {
			filename = Input.string("Introduce el nombre del fichero para importar: ");
			JSONObject raceData = (JSONObject) ProgramExporter.getParser().parse(new FileReader(filename));
			this.importRaceData(raceData);
		} catch (FileNotFoundException e) {
			System.err.println("No se ha podido encontrar el fichero \"" + filename + "\" para cargar la información previa");
		} catch (IOException e) {
			System.err.println("Existión un error mientras se leía el fichero \"" + filename + "\" para cargar la información previa");
		} catch (ParseException e) {
			System.err.println("No se ha podido leer el fichero \"" + filename + "\" para cargar la información previa");
		}
	}

	/**
	 * Importa los torneos de un {@link JSONObject} pasado por parámetro
	 *
	 * @param tournament
	 *            un {@link JSONObject} con la información de todos los torneos
	 */
	public void importTournamentData(JSONObject tournamentData) {
		JSONArray tournamentList = (JSONArray) tournamentData.get(Control.TOURNAMENT);
		for (Object obj : tournamentList) {
			Tournament t = Tournament.importTournament((JSONObject) obj);
			if (!this.getTournamentList().contains(t)) {
				this.getTournamentList().add(t);
			}
		}
	}

	/**
	 * Importa los datos de los torneos de un fichero que se le indica
	 */
	public void importTournamentDataFromFile() {
		String filename = "";
		try {
			filename = Input.string("Introduce el nombre del fichero para importar: ");
			JSONObject tournament = (JSONObject) ProgramExporter.getParser().parse(new FileReader(filename));
			this.importTournamentData(tournament);
		} catch (FileNotFoundException e) {
			System.err.println("No se ha podido encontrar el fichero \"" + filename + "\" para cargar la información previa");
		} catch (IOException e) {
			System.err.println("Existión un error mientras se leía el fichero \"" + filename + "\" para cargar la información previa");
		} catch (ParseException e) {
			System.err.println("No se ha podido leer el fichero \"" + filename + "\" para cargar la información previa");
		}
	}

	/**
	 * Lista los {@link ScoreCar} que pertenecen a un garaje
	 */
	public void listCarFromGarage() {
		System.out.println("=================");
		System.out.println("= Listar Coches =");
		System.out.println("=================");
		System.out.println("Selecciona el garaje a visualizar.");
		int selected[] = Utils.showAndSelectFromList(this.getGarageList(), true);
		if (!(selected[0] < 0)) {
			Utils.showFromList(this.getGarageList().get(selected[0]).getGarageCarList(), true);
		}

	}

	/**
	 * Método que permite listar los garages de la lista {@link #garageList}
	 */
	public void listGarages() {
		System.out.println("===================");
		System.out.println("= Listado Garajes =");
		System.out.println("===================");
		Utils.showFromList(this.getGarageList(), true);
	}

	/**
	 * Lista todas las carreras de añadidas de la lista {@link #raceList}
	 */
	public void listRaces() {
		System.out.println("====================");
		System.out.println("= Listado Carreras =");
		System.out.println("====================");
		Utils.showFromList(this.getRaceList(), true);

	}

	/**
	 * Método que permite listar todos los torneos que existen en
	 * {@link #tournamentList}
	 */
	public void listTournaments() {
		System.out.println("===================");
		System.out.println("= Listado Torneos =");
		System.out.println("===================");
		Utils.showFromList(this.getTournamentList(), true);

	}

	/**
	 * Registra de manera aleatoria los coches de los garajes en la lista de
	 * coches de un torneo. Participarán un coche de cada garaje, a no ser que
	 * sólo exista un garaje, en cuyo caso participarian todos los coches de ese
	 * garaje. A continuación, se registra cada garaje en una carrera, y se le
	 * añade a la carrera los mismos coches que participan en el torneo
	 */
	public void registerCarsFromGarageToTornament() {
		System.out.println("================================");
		System.out.println("= Registar coches en un torneo =");
		System.out.println("================================");
		System.out.println("Selecciona el torneo: ");
		int[] selected = Utils.showAndSelectFromList(this.getTournamentList(), false);
		Tournament t = this.getTournamentList().get(selected[0]);
		t.registerGaragesInEachRace();
		if (t.getGarageList().size() == 1) {
			Garage w = this.getGarageFromList(t.getGarageList().get(0).toString());
			List<ScoreCar> carListFromGarage = w.getGarageCarList();
			t.getTournamentCarList().addAll(carListFromGarage);
		} else {
			for (Garage w : t.getGarageList()) {
				Garage g = this.getGarageFromList(w.toString());
				t.getTournamentCarList().add(g.getRandomCarFromList());
			}
		}
		for (Race r : t.getRaceList()) {
			r.registerCars(t.getTournamentCarList());
		}

	}

	/**
	 * Permite añadir un coche a un garaje existente
	 */
	public void removeCarFromGarage() {
		System.out.println("==================");
		System.out.println("= Eliminar Coche =");
		System.out.println("==================");
		System.out.println("Selecciona el garaje al que pertenece el coche:");
		int selected[] = Utils.showAndSelectFromList(this.getGarageList(), true);
		if (!(selected[0] < 0)) {
			int[] selectedCar = Utils.showAndSelectFromList(this.getGarageList().get(selected[0]).getGarageCarList(), true);
			if (!(selectedCar[0] < 0)) {
				this.getGarageList().get(selected[0]).getGarageCarList().remove(selectedCar[0]);
			}
		}

	}

	/**
	 * Elimina un garaje de la lista de garajes
	 */
	public void removeGarageFromList() {
		System.out.println("====================");
		System.out.println("= Eliminar Garajes =");
		System.out.println("====================");
		System.out.println("Selecciona el garaje a eliminar.");
		int selected[] = Utils.showAndSelectFromList(this.getGarageList(), true);
		if (!(selected[0] < 0)) {
			this.getGarageList().remove(selected[0]);
		}
	}

	/**
	 * Elimina los garajes que se seleccionan de la carrera seleccionada
	 */
	public void removeGarageFromRace() {
		System.out.println("===================================");
		System.out.println("= Eliminar garajes de una carrera =");
		System.out.println("===================================");
		System.out.println("");
		int[] selected = Utils.showAndSelectFromList(this.getRaceList(), true);
		if (selected[0] >= 0) {
			Race r = this.getRaceList().get(selected[0]);
			int[] selectedGarages = Utils.showAndSelectFromList(r.getGarageList(), true, true);
			for (int i = 0; i < selectedGarages.length; i++) {
				r.getGarageList().remove(selectedGarages[i]);
			}
			r.registerCars();
		}
	}

	/**
	 * Elimina del torneo seleccionado el garaje deseado.
	 */
	public void removeGarageFromTournament() {
		System.out.println("=================================");
		System.out.println("= Eliminar garajes de un torneo =");
		System.out.println("=================================");
		System.out.println("");
		int[] selected = Utils.showAndSelectFromList(this.getTournamentList(), true);
		if (selected[0] >= 0) {
			Tournament t = this.getTournamentList().get(selected[0]);
			int[] selectedGarages = Utils.showAndSelectFromList(t.getGarageList(), true, true);
			for (int i = 0; i < selectedGarages.length; i++) {
				t.getGarageList().remove(selectedGarages[i]);
			}
		}
	}

	/**
	 * Selecciona una carrera y la elimina de la lista de carreras.
	 */
	public void removeRaceFromList() {
		System.out.println("====================");
		System.out.println("= Eliminar Carrera =");
		System.out.println("====================");
		System.out.println("Selecciona la carrera a eliminar.");
		int selected[] = Utils.showAndSelectFromList(this.getRaceList(), true);
		if (!(selected[0] < 0)) {
			this.getRaceList().remove(selected[0]);
		}
	}

	/**
	 * Elimina la carrera seleccionada del torneo seleccionado
	 */
	public void removeRaceFromTournament() {
		System.out.println("==================================");
		System.out.println("= Eliminar carrera de un torneo =");
		System.out.println("==================================");
		System.out.println("Selecciona un torneo:");
		int[] selected = Utils.showAndSelectFromList(this.getTournamentList(), true);
		if (selected[0] >= 0) {
			Tournament t = this.getTournamentList().get(selected[0]);
			int[] selectedRace = Utils.showAndSelectFromList(t.getRaceList(), true, false);
			t.getRaceList().remove(selectedRace[0]);

		}

	}

	/**
	 * Elimina un torneo de la lista de torneos
	 */
	public void removeTournamentFromList() {
		System.out.println("===================");
		System.out.println("= Eliminar Torneo =");
		System.out.println("===================");
		System.out.println("Selecciona el torneo a eliminar.");
		int selected[] = Utils.showAndSelectFromList(this.getTournamentList(), true);
		if (!(selected[0] < 0)) {
			this.getTournamentList().remove(selected[0]);
		}
	}

	/**
	 * Establece la lista de garajes pasada por parámetro
	 *
	 * @param garageList
	 *            La lista de garajes
	 */
	public void setGarageList(List<Garage> garageList) {
		this.garageList = garageList;
	}

	/**
	 * Establece la lista de carreras
	 *
	 * @param raceList
	 *            La lista de carreras a establecer
	 */
	public void setRaceList(List<Race> raceList) {
		this.raceList = raceList;
	}

	/**
	 * Establece la lista de torneos disponibles
	 *
	 * @param tournamentList
	 */
	public void setTournamentList(List<Tournament> tournamentList) {
		this.tournamentList = tournamentList;
	}

	/**
	 * Muestra el menú principal de la aplicación, donde pueden gestionarse los
	 * coches, garajes, carreras y torneos
	 */
	public void showMainMenu() {
		try {
			this.importAppData();
		} catch (FileNotFoundException e) {
			System.err.println("No se ha podido encontrar el fichero \".appData.json\" para cargar la información previa");
		} catch (IOException e) {
			System.err.println("Existión un error mientras se leía el fichero \".appData.json\" para cargar la información previa");
		} catch (ParseException e) {
			System.err.println("No se ha podido leer el fichero \".appData.json\" para cargar la información previa");
		}
		int opt = -1;
		do {
			System.out.println("===================");
			System.out.println("= Control Carrera =");
			System.out.println("===================\n");
			System.out.println("1.- Gestionar garajes");
			System.out.println("2.- Gestionar carreras");
			System.out.println("3.- Gestionar torneos");
			System.out.println("4.- Exportar datos");
			System.out.println("5.- Importar datos");
			System.out.println("6.- Salir");
			opt = Input.integer("\nIntroduce la opción que desee: ");
			System.out.println("\n");
			switch (opt) {
			case 1:
				this.showManageGaragesMenu();
				break;
			case 2:
				this.showManagerRaceMenu();
				break;
			case 3:
				this.showManagerTournamentMenu();
				break;
			case 4:
				this.exportAppDataToFile();
				break;
			case 5:
				this.importAppDataFromFile("");
				break;
			case 6:
				this.exportAppData();
				break;
			default:
				System.out.println("No existe esa opción, seleccione otra.");
			}
		} while (opt != 6);
	}

	/**
	 * Muestra un menú para poder gestionar los coches y los garajes
	 */
	public void showManageGaragesMenu() {
		int opt = -1;
		do {
			System.out.println("===================");
			System.out.println("= Gestión Garajes =");
			System.out.println("===================\n");
			System.out.println("1.- Listar garajes");
			System.out.println("2.- Añadir garajes");
			System.out.println("3.- Eliminar garajes");
			System.out.println("4.- Listar coches de un garaje");
			System.out.println("5.- Añadir coche a un garaje");
			System.out.println("6.- Eliminar coche de un garaje");
			System.out.println("7.- Exportar datos");
			System.out.println("8.- Importar datos");
			System.out.println("9.- Atrás");
			opt = Input.integer("\nIntroduce la opción que desee: ");
			System.out.println("\n");
			switch (opt) {
			case 1:
				this.listGarages();
				break;
			case 2:
				this.addGarageToList();
				break;
			case 3:
				this.removeGarageFromList();
				break;
			case 4:
				this.listCarFromGarage();
				break;
			case 5:
				this.addCarToGarage();
				break;
			case 6:
				this.removeCarFromGarage();
				break;
			case 7:
				this.exportGaragesDataToFile();
				break;
			case 8:
				this.importGarageDataFromFile();
				break;
			case 9:
				break;
			default:
				System.out.println("No existe esa opción, seleccione otra.");
			}
		} while (opt != 9);

	}

	/**
	 * Muetsra el menu de gestión de carreras
	 */
	public void showManagerRaceMenu() {
		int opt = -1;
		do {
			System.out.println("====================");
			System.out.println("= Gestión Carreras =");
			System.out.println("====================\n");
			System.out.println("1.- Listar carreras");
			System.out.println("2.- Información de una carrera");
			System.out.println("3.- Añadir carrera");
			System.out.println("4.- Eliminar carrera");
			System.out.println("5.- Agregar garaje a una carrera");
			System.out.println("6.- Eliminar garaje de una carrera");
			System.out.println("7.- Empezar carrera");
			System.out.println("8.- Exportar datos");
			System.out.println("9.- Importar datos");
			System.out.println("10.- Atrás");
			opt = Input.integer("\nIntroduce la opción que desee: ");
			System.out.println("\n");
			switch (opt) {
			case 1:
				this.listRaces();
				break;
			case 2:
				this.showRaceDetails();
				break;
			case 3:
				this.addRaceToList();
				break;
			case 4:
				this.removeRaceFromList();
				break;
			case 5:
				this.addGarageToRace();
				break;
			case 6:
				this.removeGarageFromRace();
				break;
			case 7:
				this.startSelectedRace();
				break;
			case 8:
				this.exportRacesDataToFile();
				break;
			case 9:
				this.importRacesDataFromFile();
				break;
			case 10:
				break;
			default:
				System.out.println("No existe esa opción, seleccione otra.");
			}
		} while (opt != 10);

	}

	/**
	 * Muestra un menú para poder gestionar los torneos
	 */
	public void showManagerTournamentMenu() {
		int opt = -1;
		do {
			System.out.println("===================");
			System.out.println("= Gestión Torneos =");
			System.out.println("===================\n");
			System.out.println("1.- Listar torneos");
			System.out.println("2.- Información de un torneo");
			System.out.println("3.- Añadir un torneo");
			System.out.println("4.- Eliminar un torneo");
			System.out.println("5.- Añadir carrera al torneo");
			System.out.println("6.- Eliminar carrera del torneo");
			System.out.println("7.- Agregar garaje a un torneo");
			System.out.println("8.- Eliminar garaje de un torneo");
			System.out.println("9.- Registar coches de los garages en el torneo");
			System.out.println("10.- Empezar torneo");
			System.out.println("11.- Exportar datos");
			System.out.println("12.- Importar datos");
			System.out.println("13.- Atrás");
			opt = Input.integer("\nIntroduce la opción que desee: ");
			System.out.println("\n");
			switch (opt) {
			case 1:
				this.listTournaments();
				break;
			case 2:
				this.showTournamentsDetails();
				break;
			case 3:
				this.addTournamentToList();
				break;
			case 4:
				this.removeTournamentFromList();
				break;
			case 5:
				this.addRaceToTournament();
				break;
			case 6:
				this.removeRaceFromTournament();
				break;
			case 7:
				this.addGarageToTournament();
				break;
			case 8:
				this.removeGarageFromTournament();
				break;
			case 9:
				this.registerCarsFromGarageToTornament();
				break;
			case 10:
				this.startSelectedTournament();
				break;
			case 11:
				this.exportTournamentDataToFile();
				break;
			case 12:
				this.importTournamentDataFromFile();
				break;
			case 13:
				break;
			default:
				System.out.println("No existe esa opción, seleccione otra.");
			}
		} while (opt != 13);

	}

	/**
	 * Muestra los detalles de una carrera seleccionada
	 */
	public void showRaceDetails() {
		System.out.println("====================");
		System.out.println("= Detalles Carrera =");
		System.out.println("====================");
		int[] selected = Utils.showAndSelectFromList(this.getRaceList(), true);
		if (selected[0] >= 0) {
			StringBuilder builder = new StringBuilder();
			builder.append(this.getRaceList().get(selected[0]).getPodium());
			System.out.println(builder.toString());
			String toRet = Input.string("Pulse \"Enter\" para continuar...");
		}
	}


	/**
	 * Muetsra los detalles del torneo seleccionado
	 */
	public void showTournamentsDetails() {
		System.out.println("===================");
		System.out.println("= Detalles Torneo =");
		System.out.println("===================");
		int[] selected = Utils.showAndSelectFromList(this.getTournamentList(), true);
		if (selected[0] >= 0) {
			StringBuilder builder = new StringBuilder();
			builder.append(this.getTournamentList().get(selected[0]).getTournamentInfo());
			System.out.println(builder.toString());
			String toRet = Input.string("Pulse \"Enter\" para continuar...");
		}
	}

	/**
	 * Comienza la carrera que se seleccione
	 */
	public void startSelectedRace() {
		System.out.println("=======================");
		System.out.println("= Empezar una carrera =");
		System.out.println("========================");
		System.out.println("");
		int[] selected = Utils.showAndSelectFromList(this.getRaceList(), true);
		if (selected[0] >= 0) {
			Race r = this.getRaceList().get(selected[0]);
			r.resetCars();
			r.startRace();
			System.out.println(r.getPodium());
			String toRet = Input.string("Pulse \"Enter\" para continuar...");
		}
	}

	/**
	 * Inicia el el torneo seleccionado
	 */
	public void startSelectedTournament() {
		System.out.println("=====================");
		System.out.println("= Empezar un torneo =");
		System.out.println("=====================");
		System.out.println("");
		int[] selected = Utils.showAndSelectFromList(this.getTournamentList(), true);
		if (selected[0] >= 0) {
			Tournament t = this.getTournamentList().get(selected[0]);
			for (Race race : t.getRaceList()) {
				race.startRace();
				race.givePoints();
				race.resetCars();
			}
			t.sortAndReverseListByPoints(t.getTournamentCarList());
			System.out.println(t.getTournamentInfo());
			String toRet = Input.string("Pulse \"Enter\" para continuar...");
		}
	}

	public static void main(String[] args) {
		Control c = new Control();
		c.showMainMenu();
	}

}
