package race;

import org.json.simple.JSONObject;

/**
 * Esta clase hace referencia al coche que correr� en las carreras. Los coches
 * contienen el nombre del garaje al que pertenecen
 */
public class ScoreCar {

	/**
	 * Esta es la constante que sirve para indicar cu�l es la clave para la
	 * importaci�n y exportaci�n de la marca del coche
	 */
	public static final String BRAND = "brand";

	/**
	 * Esta es la constante que sirve para indicar cu�l es la clave para la
	 * importaci�n y exportaci�n del garaje a la que pertenece el coche
	 */
	public static final String GARAGE = "garage";

	/**
	 * Esta es la constante que sirve para indicar cu�l es la velocidad m�xima a
	 * la que puede circular el coche
	 */

	public static final int MAX_SPEED = 200;
	/**
	 * Esta es la constante que sirve para indicar cu�l es la clave para la
	 * importaci�n y exportaci�n del modelo del coche
	 */
	public static final String MODEL = "model";

	/**
	 * Variable que almacena la marca del coche
	 */
	protected String brand;

	/**
	 * Variable que almacena la distancia que ha recorrido el coche
	 */
	protected double distance = 0.0;

	/**
	 * Variable qie almacena el garaje al que pertenece el coche
	 */
	protected String garage = "";

	/**
	 * Variable que almacena el modelo del coche
	 */
	protected String model;

	/**
	 * Variable que almacena la puntuaci�n del coche
	 */
	protected int score = 0;

	/**
	 * Variable que alamacena la velocidad actual del coche
	 */
	protected int velocity = 0;

	/**
	 * Contructor de la clase {@link ScoreCar}
	 *
	 * @param brand
	 *            La marca del coche
	 * @param model
	 *            El modelo del coche
	 */
	public ScoreCar(String brand, String model) {
		this.brand = brand;
		this.model = model;
	}

	/**
	 * Incrementa la velocidad del coche, de 5 en 5, siempre y cuando no haya
	 * alcanzado la velocidad m�xima del coche, declarada en la variable
	 * {@link #MAX_SPEED}. A continuaci�n, modifica la distancia recorrida del
	 * coche, sum�ndosela a la actual, mediante el m�todo
	 * {@link #calculateDistance()}
	 */
	public void accelerate() {
		if (this.getVelocity() < ScoreCar.MAX_SPEED) {
			this.setVelocity(this.getVelocity() + 5);
		}
		this.setDistance(this.getDistance() + this.calculateDistance());
	}

	/**
	 * A�ade la puntuaci�n que se le pasa por par�metro a la puntuaci�n del
	 * coche
	 *
	 * @param score
	 *            La puntuaci�n que se le incrementa al coche
	 */
	public void addScoreToCar(int score) {
		this.setScore(this.getScore() + score);
	}

	/**
	 * Decrementa la velocidad del coche, de 5 en 5, siempre y cuando no se haya
	 * detenido. A continuaci�n modifica la distancia recorridad del coche,
	 * increment�ndosela a la actual, mediante el m�todo
	 * {@link ScoreCar#calculateDistance()}
	 */
	public void brake() {
		if (this.getVelocity() > 0) {
			this.setVelocity(this.getVelocity() - 5);
		}
		this.setDistance(this.getDistance() + this.calculateDistance());
	}

	/**
	 * Transforma los km/h, obtenidos en el m�todo {@link #getVelocity()}, en
	 * m/s
	 *
	 * @return La velocidad actual en m/s
	 */
	public double calculateDistance() {
		return this.getVelocity() * 16.667;

	}

	/**
	 * Seg�n el par�metro recibido por entrada el coche acelera o frena, usando
	 * los m�todos {@link #accelerate()} y {@link #brake()}, respectivamente. El
	 * coche acelerar� cuando el par�metro pasado por entrada sea distinto de 0
	 *
	 * @param option
	 *            Opci�n que se le pasa por par�metro, para decidir si el coche
	 *            acelera o no
	 */
	public void driveInRace(int option) {
		if (option != 0) {
			this.accelerate();
		} else {
			this.brake();
		}
	}

	/**
	 * Compara si el coche que se pasa por par�metro es igual al coche actual.
	 * Un coche es igual a otro si tienen la misma marca, modelo y garaje
	 */
	@Override
	public boolean equals(Object sc) {
		if (sc instanceof ScoreCar) {
			boolean brand = this.getBrand().equalsIgnoreCase(((ScoreCar) sc).getBrand());
			boolean model = this.getModel().equalsIgnoreCase(((ScoreCar) sc).getModel());
			boolean garage = this.getGarage().equalsIgnoreCase(((ScoreCar) sc).getGarage());

			return (brand && model && garage);
		}
		return false;
	}

	/**
	 * Permite exportar el objeto coche a un objeto {@link JSONObject}
	 *
	 * @return El {@link JSONObject} que se obtiene de exportar un objeto coche
	 */
	public JSONObject exportCarToJson() {
		JSONObject export = new JSONObject();
		export.put(ScoreCar.BRAND, this.getBrand());
		export.put(ScoreCar.MODEL, this.getModel());
		export.put(ScoreCar.GARAGE, this.getGarage());
		return export;
	}

	/**
	 * Devuelve la marca del coche
	 *
	 * @return La marca del coche
	 */
	public String getBrand() {
		return this.brand;
	}

	/**
	 * Devuelve una cadena de texto con los dettales de un coche, la marca, el
	 * modelo y, si existe un garaje, el garaje a que pertenece
	 *
	 * @return Devuelve una cadena con los detalles del coche
	 */
	public String getDetails() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getBrand());
		builder.append(" ");
		builder.append(this.getModel());
		if (!this.getGarage().isEmpty()) {
			builder.append(" ");
			builder.append("(");
			builder.append(this.getGarage());
			builder.append(")");
		}
		return builder.toString();
	}

	/**
	 * Devuelve la distancia actual del coche
	 *
	 * @return La distancia actual del coche
	 */
	public double getDistance() {
		return this.distance;
	}

	/**
	 * Devuelve el garaje actual del coche
	 *
	 * @return El garaje actual del coche
	 */
	public String getGarage() {
		return this.garage;
	}

	/**
	 * Devuelve el modelo actual del coche
	 *
	 * @return El modelo actual del coche
	 */
	public String getModel() {
		return this.model;
	}

	/**
	 * Devuelve la puntuci�n actual del coche
	 *
	 * @return La puntuaci�n actual del coche
	 */
	public int getScore() {
		return this.score;
	}

	/**
	 * Devuelve la velocidad actual del coche
	 *
	 * @return La velocidad actual del coche
	 */
	public int getVelocity() {
		return this.velocity;
	}

	/**
	 * Reinicia los contadores de velocidad y distancia del coche
	 */
	public void restartCounters() {
		this.setVelocity(0);
		this.setDistance(0.0);
	}

	/**
	 * Reinicia la puntuaci�n del coche
	 */
	public void restartScore() {
		this.setScore(0);
	}

	/**
	 * Establece la marca del coche
	 *
	 * @param brand
	 *            La marca del coche
	 */
	public void setBrand(String brand) {
		this.brand = brand;
	}

	/**
	 * Establece la distancia del coche
	 *
	 * @param distance
	 *            La distancia del coche
	 */
	public void setDistance(double distance) {
		this.distance = distance;
	}

	/**
	 * Establece el garaje del coche
	 *
	 * @param garage
	 *            El garaje del coche
	 */
	public void setGarage(String garage) {
		this.garage = garage;
	}

	/**
	 * Establece el modelo del coche
	 *
	 * @param model
	 *            El modelo del coche
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * Establece la puntuaci�n del coche
	 *
	 * @param score
	 *            La puntuaci�n del coche
	 */
	public void setScore(int score) {
		this.score = score;
	}

	/**
	 * Establece la velocidad actual del coche
	 *
	 * @param velocity
	 *            La velocidad del coche
	 */
	public void setVelocity(int velocity) {
		this.velocity = velocity;
	}

	/**
	 * Crea un nuevo coche a partir de un objeto {@link JSONObject}
	 *
	 * @param importedCarJson
	 *            el objeto {@link JSONObject} que contiene la informaci�n del
	 *            coche a crear
	 * @return El {@link ScoreCar} con la informaci�n del {@link JSONObject} que
	 *         se le pasa por par�metro
	 */
	public static ScoreCar importCarFromJson(JSONObject importedCarJson) {
		String brand = (String) importedCarJson.get(ScoreCar.BRAND);
		String model = (String) importedCarJson.get(ScoreCar.MODEL);
		String garage = (String) importedCarJson.get(ScoreCar.GARAGE);

		ScoreCar sc = new ScoreCar(brand, model);
		sc.setGarage(garage);

		return sc;

	}

	@Override
	public String toString() {
		return this.getDetails();
	}

	public static void main(String[] args) {
		ScoreCar sc = new ScoreCar("Citro�n", "Xsara");
		System.out.println(sc.getDetails());
		// String exportedCar =
		// ExporterImporterJson.jsonObjectToString(sc.exportCarToJson());
		// System.out.println(exportedCar);
		// JSONObject exportedJsonCar =
		// ExporterImporterJson.stringToJsonObject(exportedCar);
		// ScoreCar importedCar = ScoreCar.importCarFromJson(exportedJsonCar);
		// System.out.println(importedCar.getDetails());

	}

}
