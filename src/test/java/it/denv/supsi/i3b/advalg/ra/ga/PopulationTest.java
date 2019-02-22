package it.denv.supsi.i3b.advalg.ra.ga;

import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.ga.Individual;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.ga.Population;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class PopulationTest {
	@Test
	public void testCrossover() {
		TSPData empty = null;
		Individual i1 = new Individual(empty);
		Individual i2 = new Individual(empty);

		i1.setGenes(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
		i2.setGenes(new int[]{9, 8, 7, 6, 5, 4, 3, 2, 1});

		Individual i3 = Population.getOffspring(i1, i2, 5, 8);

		int[] genes = i3.getGenes();

		assertArrayEquals(new int[]{9,5,4,3,2,6,7,8,1}, genes);

	}
}
