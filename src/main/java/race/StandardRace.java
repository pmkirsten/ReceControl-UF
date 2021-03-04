package race;

import utils.Utils;

/**
 * Esta clase extiende de la clase {@link Race}, indicando que este tipo de
 * carerras duran un número de horas determinadas.
 */
public class StandardRace extends Race {

	/**
	 * Constante que indica los puntos que gana el coche que haya quedado en
	 * primer lugar
	 */
	public static final int FIRST_SCORE = 5;

	/**
	 * Constante que indica los puntos que gana el coche que haya quedado en
	 * segundo lugar
	 */
	public static final int SECOND_SCORE = 3;

	/**
	 * Constante que indica los puntos que gana el coche que haya quedado en
	 * tercer lugar
	 */
	public static final int THIRD_SCORE = 1;

	/**
	 * Variable que indica el número de horas que dura la carrera
	 */
	protected int raceHours;

	/**
	 * Array que contiene las constantes de las puntuaciones en orden
	 */
	private final int[] scoreArray = {StandardRace.FIRST_SCORE, StandardRace.SECOND_SCORE, StandardRace.THIRD_SCORE};

	/**
	 * Crea una nueva carrera estándar
	 *
	 * @param name
	 *            El nombre de la carrera
	 * @param hours
	 *            El número de horas que dura la carrera
	 */
	public StandardRace(String name, int hours) {
		super(name);
		this.raceHours = hours;
	}


	@Override
	public String getPodiumForTournament() {

		int size = 3;

		this.sortCarsByDistanceAndReverse();

		if (this.getCarList().size() < size) {
			size = this.getCarList().size();
		}
		StringBuilder builder = new StringBuilder();
		builder.append("\t\tNombre: ");
		builder.append(this.getName());
		builder.append("\n");
		builder.append("\t\tPodio: ");
		builder.append("\n");
		for (int i = 0; i < size; i++) {
			ScoreCar sc = this.getCarList().get(i);
			builder.append("\t\t\t");
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
	 * Obtiene el número de horas de una carrera
	 *
	 * @return El número de horas de una carrera
	 */
	public int getRaceHours() {
		return this.raceHours;
	}

	/**
	 * Obtiene el array de puntuaciones de una carrera estándar
	 *
	 * @return el array de puntuaciones de una carrera estándar
	 */
	public int[] getScoreArray() {
		return this.scoreArray;
	}

	@Override
	public void givePoints() {
		int size = 3;

		this.sortCarsByDistanceAndReverse();

		if (this.getCarList().size() < size) {
			size = this.getCarList().size();
		}

		for (int i = 0; i < size; i++){
			ScoreCar sc = this.getCarList().get(i);
			sc.addScoreToCar(this.getScoreArray()[i]);
		}
	}

	/**
	 * Establece el número de horas de una carrera
	 *
	 * @param raceHours
	 *            El número de horas de una carrera
	 */
	public void setRaceHours(int raceHours) {
		this.raceHours = raceHours;
	}


	@Override
	public void startRace() {
		int minutes = this.getRaceHours()*60;
		for (int i = 0 ; i < minutes; i++) {
			for(ScoreCar sc :this.getCarList()) {
				sc.driveInRace(Utils.getRandomNumberInRange(0, 2));
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getName());
		builder.append(" - ");
		builder.append(this.getRaceHours());
		builder.append("h.");
		builder.append(" (Estándar)");
		return builder.toString();
	}

}

