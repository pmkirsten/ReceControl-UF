package race;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import utils.ComparatorScoreCarByDistance;
import utils.Utils;

/**
 * La clase abstracta sirve como clase base para que se implemente cualquier
 * tipo de carrera. En una carrera, participan una lista de garajes, en la que
 * se seleccionan una lista de coches participantes, que provienen de esos
 * garajes. Si sólo participa un garaje, todos los coches de ese garaje
 * participan. Si es más de un garaje el que participa, se selecciona
 * aleatoriamente un coche de cada uno de los garajes
 *
 */
public abstract class Race {

	/**
	 * Constante que hace referencia a la lista de coches ({@link ScoreCar})
	 * participantes en una carrera para la exportación / importación
	 */
	public static final String CAR_LIST = "carList";

	/**
	 * Constante que hace referencia a la lista de garages ({@link Garage})
	 * participantes para la exportación / importación
	 */
	public static final String GARAGE_LIST = "garageList";

	/**
	 * Contante que hace referencia al nombre de una carrera para la exportación
	 * / importación
	 */
	public static final String NAME = "name";

	/**
	 * Constante que hace referencia al tipo de una carrera para la exportación
	 * / importación
	 */
	public static final String RACE_TYPE = "type";

	/**
	 * Constante que hace referencia al tipo de carrera de eliminación para la
	 * exportación / importación
	 */
	public static final String RACE_TYPE_DEATHRACE = "DR";

	/**
	 * Constante que hace referencia al tipo de carrera estándar para la
	 * exportación / importación
	 */
	public static final String RACE_TYPE_STANDARDRACE = "SR";

	/**
	 * Constante que hace referencia al segundo parámetro del constructor para
	 * la exportación / importación
	 */
	public static final String SECOND_ARGUMENT = "secondArgument";

	/**
	 * Variable que hace referencia a la lista de coches participantes en una
	 * carrera
	 */
	protected List<ScoreCar> carList;

	/**
	 * Variable que se utiliza para guardar un comparador que permite ordenar
	 * los {@link ScoreCar} según la distancia que han recorrido
	 */
	protected Comparator<ScoreCar> comparator = new ComparatorScoreCarByDistance();

	/**
	 * Variable que hace referencia a la lista de garajes participantes en una
	 * carrera
	 */
	protected List<Garage> garageList;

	/**
	 * Varibale que hace referencia al nombre de la carrera
	 */
	protected String name;

	/**
	 * Contructor de la clase {@link Race}
	 *
	 * @param name
	 */
	public Race(String name) {
		this.name = name;
		this.init();
	}

	/**
	 * Método genérico para exportar una carrera (sin lista de {@link Garage} y
	 * de {@link ScoreCar} participantes)
	 *
	 * @return Un objeto {@link JSONObject}
	 */
	public JSONObject exportRaceInfo() {

		JSONObject export = new JSONObject();
		export.put(Race.NAME, this.getName());
		if (this instanceof StandardRace) {
			export.put(Race.RACE_TYPE, Race.RACE_TYPE_STANDARDRACE);
			export.put(Race.SECOND_ARGUMENT, ((StandardRace) this).getRaceHours());
		} else {
			export.put(Race.RACE_TYPE, Race.RACE_TYPE_DEATHRACE);
			export.put(Race.SECOND_ARGUMENT, ((DeathRace) this).getLapsToStart());
		}
		return export;
	}

	/**
	 * Método genérico para exportar una carrera (con lista de {@link Garage} y
	 * de {@link ScoreCar} participantes)
	 *
	 * @return Un objeto {@link JSONObject}
	 */
	public JSONObject exportRaceWithGarageAndCarsToJson() {
		JSONObject export = new JSONObject();
		export.put(Race.NAME, this.getName());
		if (this instanceof StandardRace) {
			export.put(Race.RACE_TYPE, Race.RACE_TYPE_STANDARDRACE);
			export.put(Race.SECOND_ARGUMENT, ((StandardRace) this).getRaceHours());
		} else {
			export.put(Race.RACE_TYPE, Race.RACE_TYPE_DEATHRACE);
			export.put(Race.SECOND_ARGUMENT, ((DeathRace) this).getLapsToStart());
		}

		JSONArray garageList = new JSONArray();
		for (Garage g : this.getGarageList()) {
			garageList.add(g.exportGarageToJson());
		}
		export.put(Race.GARAGE_LIST, garageList);

		JSONArray carList = new JSONArray();
		for (ScoreCar sc : this.getCarList()) {
			carList.add(sc.exportCarToJson());
		}
		export.put(Race.CAR_LIST, carList);

		return export;

	}

	/**
	 * Devuelve la lista de {@link ScoreCar} que participan en una carrera
	 *
	 * @return La lista de {@link ScoreCar} que participan en una carrera
	 */
	public List<ScoreCar> getCarList() {
		return this.carList;
	}

	/**
	 * Devuelve el comparador que se establece para ordenar por distancia varios
	 * {@link ScoreCar}
	 *
	 * @return el comparador de {@link ScoreCar} según distancia
	 */
	public Comparator<ScoreCar> getComparator() {
		return this.comparator;
	}

	/**
	 * Devuelve la lista de {@link Garage} que participan en una carrera
	 *
	 * @return la lista de {@link Garage} que participan en una carrera
	 */
	public List<Garage> getGarageList() {
		return this.garageList;
	}

	/**
	 * Devuelve el nombre de la carrera
	 *
	 * @return El nombre de la carrera
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Devuelve el podio de una carrera. El podio de una carrera son los 3
	 * {@link ScoreCar} que más distancia han recorrido en una carrera. Se
	 * ordena la lista {@link #carList} mediante el método
	 * {@link #sortCarsByDistanceAndReverse()}
	 *
	 * @return El podio de una carrera
	 */
	public String getPodium() {
		int size = 3;

		this.sortCarsByDistanceAndReverse();

		if (this.getCarList().size() < size) {
			size = this.getCarList().size();
		}
		StringBuilder builder = new StringBuilder();
		builder.append("Carrera: ");
		builder.append(this.getName());
		builder.append("\n");
		if (this instanceof DeathRace) {
			builder.append("Tipo: DR - Vueltas previas: ");
			builder.append(((DeathRace) this).getLapsToStart());
			builder.append("\n");
		} else {
			builder.append("Tipo: SR - Horas de carrera: ");
			builder.append(((StandardRace) this).getRaceHours());
			builder.append("\n");
		}
		builder.append("Podio: ");
		builder.append("\n");
		for (int i = 0; i < size; i++) {
			ScoreCar sc = this.getCarList().get(i);
			builder.append("\t");
			builder.append("• ");
			builder.append(i + 1);
			builder.append("º puesto: ");
			builder.append(sc.getDetails());
			builder.append(" con una distancia de ");
			builder.append(Utils.formatLocalNumber(sc.getDistance()));
			builder.append(" m.\n");
		}
		return builder.toString();
	}


	/**
	 * Obtiene el podio de una carrera, como resumen para un torneo
	 *
	 * @return El podio de una carrera
	 */
	public abstract String getPodiumForTournament();

	/**
	 * Este método abstracto servirá para otorgar puntos a un {@link ScoreCar}
	 * que participe en la carrera
	 */
	public abstract void givePoints();

	/**
	 * Este método inicializa las variables que hacen referencia a las listas de
	 * {@link Garage} y de {@link ScoreCar} a listas de tipo {@link ArrayList}
	 */
	public void init() {
		this.garageList = new ArrayList<>();
		this.carList = new ArrayList<>();

	}

	/**
	 * Registra los coches en una carrera. Si sólo participa un garaje
	 * ({@link Garage}) en la carrera, registra en la carrera todos los
	 * {@link ScoreCar} que pertenecen a ese garaje. Si participan en la carrera
	 * más de un garaje, se registra sólo un {@link ScoreCar} de cada uno de los
	 * garajes.
	 */
	public void registerCars() {
		if (this.getGarageList().size() == 1) {
			Garage w = this.getGarageList().get(0);
			List<ScoreCar> carListFromGarage = w.getGarageCarList();
			this.getCarList().clear();
			this.getCarList().addAll(carListFromGarage);
		} else {
			for (Garage w : this.getGarageList()) {
				this.getCarList().add(w.getRandomCarFromList());
			}
		}
	}

	/**
	 * Se registra la lista de {@link ScoreCar} pasada como parámetro en la
	 * lista de {@link ScoreCar} que participan en una carrera
	 *
	 * @param listCars
	 *            Lista de {@link ScoreCar} que se registrarán en una carrera
	 */
	public void registerCars(List<ScoreCar> listCars) {
		this.getCarList().addAll(listCars);
	}

	/**
	 * Registra un {@link Garage} en la lista de garajes participantes en una
	 * carrera
	 *
	 * @param g
	 *            El garaje a registrar
	 */
	public void registerGarage(Garage g) {
		this.getGarageList().add(g);
	}

	/**
	 * Elimina un {@link Garage} de la lista de garajes participantes en una
	 * carrera
	 *
	 * @param g
	 *            El garaje a eliminar de una carrera
	 */
	public void removeGarageFromList(Garage g) {
		this.getGarageList().remove(g);
	}

	/**
	 * Reinicia todos los contadores ({@link ScoreCar#restartCounters()}) de los
	 * coches participantes en una carrera
	 */
	public void resetCars() {
		for (ScoreCar sc : this.getCarList()) {
			sc.restartCounters();
		}
	}

	/**
	 * Invierte la posición de los elementos de una lista pasada por parámetro.
	 * El primer elemento será el último, el segundo será el penúltimo, etc
	 *
	 * @param listToSort
	 *            La lista que será invertida
	 */
	public void reverseList(List<ScoreCar> listToSort) {
		Collections.reverse(listToSort);
	}

	/**
	 * Establece la lista de {@link ScoreCar} participantes en una carrera
	 * recibidos por parámetro
	 *
	 * @param carList
	 *            la lista de coches {@link ScoreCar}
	 */
	public void setCarList(List<ScoreCar> carList) {
		this.carList = carList;
	}

	/**
	 * Establece un comparador para comparar los {@link ScoreCar} de una carrera
	 *
	 * @param comparator
	 */
	public void setComparator(Comparator<ScoreCar> comparator) {
		this.comparator = comparator;
	}

	/**
	 * Establece la lista de garajes participantes en una carrera
	 *
	 * @param garageList
	 *            La lista de garajes participantes en una carrera
	 */
	public void setGarageList(List<Garage> garageList) {
		this.garageList = garageList;
	}

	/**
	 * Establece el nombre de una carrera
	 *
	 * @param name
	 *            El nombre de una carrera
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Establece los puntos del podio de una carrera
	 */
	public void setPodiumPoints() {

	}

	/**
	 * Ordena la lista de {@link ScoreCar} participantes en la carrera,
	 * ordenados de menor a mayor distancia recorrida, según el comparador usado
	 * en {@link #getComparator()}
	 *
	 * @param listToSort
	 *            La lista a ordenar
	 */
	public void sortCarsByDistance(List<ScoreCar> listToSort) {
		Collections.sort(listToSort, this.getComparator());
	}

	/**
	 * Ordena la lista de de {@link ScoreCar} participantes en una carrera, de
	 * menor a mayor distancia, y luego invierte la posición de los elementos de
	 * la lista
	 */
	public void sortCarsByDistanceAndReverse() {
		this.sortCarsByDistanceAndReverse(this.getCarList());
	}

	/**
	 * Ordena la lista que se le pasa por parámetro que contengan elementos
	 * {@link ScoreCar}, de menor a mayor distancia, y luego invierte la
	 * posición de los elementos de la lista
	 *
	 * @param listToSort
	 *            La lista a ordenar
	 */
	public void sortCarsByDistanceAndReverse(List<ScoreCar> listToSort) {
		this.sortCarsByDistance(listToSort);
		this.reverseList(listToSort);
	}

	/**
	 * Método para comenzar la carrera
	 */
	public abstract void startRace();

	/**
	 * Importa una carrera, creando una carrera de algún tipo específicoo
	 * (usando la polimorfia, al devolver un objeto de tipo {@link Race})
	 *
	 * @param raceToImport
	 *            El {@link JSONObject} que contiene la información de la
	 *            carrera crear
	 * @return Una carrera ({@link Race}) importada de un objecto
	 *         {@link JSONObject}
	 */
	public static Race importRace(JSONObject raceToImport) {
		String raceType = (String) raceToImport.get(Race.RACE_TYPE);
		String raceName = (String) raceToImport.get(Race.NAME);
		int secondArgument = ((Long) raceToImport.get(Race.SECOND_ARGUMENT)).intValue();

		Race r = null;

		if (raceType.equalsIgnoreCase(Race.RACE_TYPE_STANDARDRACE)) {
			r = new StandardRace(raceName, secondArgument);
		} else {
			r = new DeathRace(raceName, secondArgument);
		}

		JSONArray garageArray = (JSONArray) raceToImport.get(Race.GARAGE_LIST);

		if (garageArray != null) {
			for (Object g : garageArray) {
				JSONObject garageJson = (JSONObject) g;
				r.registerGarage(Garage.importGarageFromJSONObject(garageJson));
			}
		}

		JSONArray carArray = (JSONArray) raceToImport.get(Race.CAR_LIST);
		if (carArray != null) {
			List<ScoreCar> scoreCarList = new ArrayList<>();

			for (Object sc : carArray) {
				JSONObject carJson = (JSONObject) sc;
				ScoreCar scoreCar = ScoreCar.importCarFromJson(carJson);
				scoreCar.setGarage((String) carJson.get(ScoreCar.GARAGE));
				scoreCarList.add(scoreCar);
			}

			r.registerCars(scoreCarList);
		}

		return r;

	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Race) {
			if (obj instanceof DeathRace) {
				return this.checkEqualsDeathRace((DeathRace) obj);
			} else {
				return this.checkEqualsStandarRace((StandardRace) obj);
			}

		}
		return false;
	}

	protected boolean checkEqualsDeathRace(DeathRace dr) {
		if (this instanceof DeathRace) {
			boolean name = dr.getName().equalsIgnoreCase(this.getName());
			boolean laps = dr.getLapsToStart() == ((DeathRace) this).getLapsToStart();
			return name && laps;
		} else {
			return false;
		}
	}

	protected boolean checkEqualsStandarRace(StandardRace sr) {
		if (this instanceof StandardRace) {
			boolean name = sr.getName().equalsIgnoreCase(this.getName());
			boolean hours = sr.getRaceHours() == ((StandardRace) this).getRaceHours();
			return name && hours;
		} else {
			return false;
		}
	}

	@Override
	public Race clone() {
		if (this instanceof StandardRace) {
			return new StandardRace(this.getName(), ((StandardRace) this).getRaceHours());
		} else {
			return new DeathRace(this.getName(), ((DeathRace) this).getLapsToStart());
		}
	}

	public static void main(String[] args) {
		Race r = new DeathRace("Carreira do Morrazo", 5);
		r.registerGarage(new Garage("Rodovogo"));
		r.registerGarage(new Garage("Radavaga"));
		r.registerCars();
		r.startRace();
		r.sortCarsByDistance(r.getCarList());
		JSONObject exportRaceToJson = r.exportRaceWithGarageAndCarsToJson();
		System.out.println(exportRaceToJson.toJSONString());
		Race r2d2 = Race.importRace(exportRaceToJson);
		System.out.println(r2d2);
	}

}
