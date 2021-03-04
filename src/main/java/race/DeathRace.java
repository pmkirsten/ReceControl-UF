package race;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import utils.ProgramExporter;
import utils.Utils;

/**
 * Esta es una carrera de eliminaci�n, que extiende de la clase {@link Race}. La
 * carrera de eliminaci�n eliminar� un corredor de la carrera por cada vuleta
 * que de a mayores de las iniciales. Ej: Si una carrera de elimnaci�n tiene 7
 * vueltas, todos los participantes dar�n 7 vueltas. En la 8� vuelta, se
 * eleiminar� al coche que vaya en la �ltima posici�n, y as� sucesivamente hasta
 * que s�lo quede 1
 *
 */
public class DeathRace extends Race {

	/**
	 * Lista de {@link ScoreCar} que contiene la lista de coches que quedan
	 * participando en la carrera. De esta lista se eliminan los coches seg�n su
	 * posici�n
	 */
	protected List<ScoreCar> deathList = new ArrayList<>();

	/**
	 * Variable que indica cuantas vueltas quedan hasta el final
	 */
	protected int lapsToStart;

	/**
	 * Contructor de una carrera de eliminaci�n
	 *
	 * @param name
	 *            El nombre de la carrera
	 * @param lapsToStart
	 *            Las vueltas iniciales antes de comenzar la eliminaci�n de
	 *            participantes
	 */
	public DeathRace(String name, int lapsToStart) {
		super(name);
		this.lapsToStart = lapsToStart;
	}

	/**
	 * Devuelve la lista de coches de la que se van eliminando los participante
	 * conforme avanza la carera
	 *
	 * @return
	 */
	public List<ScoreCar> getDeathList() {
		return this.deathList;
	}

	/**
	 * M�todo para obtener el n�mero de vueltas iniciales
	 *
	 * @return Las vueltas iniciales antes de empezar el proceso de eliminaci�n
	 */
	public int getLapsToStart() {
		return this.lapsToStart;
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
			builder.append("� ");
			builder.append(i + 1);
			builder.append("� puesto: ");
			builder.append(sc.getDetails());
			builder.append("\n");
		}
		return builder.toString();

	}

	@Override
	public void givePoints() {
		this.sortCarsByDistance(this.getCarList());

		for (int i = 0; i < this.getCarList().size(); i++) {
			ScoreCar sc = this.getCarList().get(i);
			sc.addScoreToCar(i);
		}
	}

	/**
	 * Establece el n�mero de vueltas que se debe dar antes de que comienze la
	 * eliminaci�n de los {@link ScoreCar} participantes
	 *
	 * @param lapsToStart
	 */
	public void setLapsToStart(int lapsToStart) {
		this.lapsToStart = lapsToStart;
	}

	@Override
	public void startRace() {
		int laps = this.getLapsToStart();
		for (int i = 0; i < laps; i++) {
			for (ScoreCar sc : this.getCarList()) {
				sc.driveInRace(Utils.getRandomNumberInRange(0, 2));
			}
		}

		this.getDeathList().addAll(this.getCarList());
		int eliminationLaps = this.getDeathList().size() - 1;

		for (int i = 0; i < eliminationLaps; i++) {
			for (ScoreCar sc : this.getCarList()) {
				sc.driveInRace(Utils.getRandomNumberInRange(0, 2));
			}
			this.sortCarsByDistance(this.getDeathList());
			this.getDeathList().remove(0);
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getName());
		builder.append(" - ");
		builder.append(this.getLapsToStart());
		builder.append(" vueltas previas.");
		builder.append(" (Eliminaci�n)");
		return builder.toString();
	}

	public static void main(String[] args) {
		DeathRace dr = new DeathRace("Carreira da Morte", 20);
		Garage g = new Garage("�scalle Lura");
		dr.registerGarage(g);
		dr.registerCars();
		dr.startRace();
		for (ScoreCar sc : dr.getDeathList()) {
			sc.getDetails();
		}

		ProgramExporter pe = new ProgramExporter();
		pe.exportPodiumRace(dr, new File("race.txt"));
	}

}
