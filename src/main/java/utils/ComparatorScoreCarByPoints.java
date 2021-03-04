package utils;

import java.util.Comparator;

import race.ScoreCar;

/**
 * Sirve para comparar {@link ScoreCar}, teniendo como criterio de ordenación su
 * puntuación
 *
 */
public class ComparatorScoreCarByPoints implements Comparator<ScoreCar>{

	@Override
	public int compare(ScoreCar o1, ScoreCar o2) {
		int points1 = o1.getScore();
		int points2 = o2.getScore();

		if (points1 < points2) {
			return -1;
		}else if (points1 == points2) {
			return 0;
		}else{
			return 1;
		}

	}
}
