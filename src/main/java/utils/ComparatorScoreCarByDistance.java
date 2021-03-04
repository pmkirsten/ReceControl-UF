package utils;

import java.util.Comparator;

import race.ScoreCar;

/**
 * Sirve para comparar {@link ScoreCar}, teniendo como criterio de ordenación su
 * distancia
 *
 */
public class ComparatorScoreCarByDistance implements Comparator<ScoreCar>{

	@Override
	public int compare(ScoreCar o1, ScoreCar o2) {
		double distance1 = o1.getDistance();
		double distance2 = o2.getDistance();
		if (distance1 > distance2) {
			return 1;
		}else if (distance1 == distance2) {
			return 0;
		}else{
			return -1;
		}


	}

}
