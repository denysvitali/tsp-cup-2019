package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.genetic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class IndividualTest {

	@Test
	void getFit() {
		boolean[] genes = new boolean[]{
				true, false, true, false, false,
				false, false, false, true,
				false, true, true,
				true, false,
				false
		};

		Population pop = mock(Population.class);
		Individual i = new Individual(genes, pop);

		when(pop.getDimension()).thenReturn(6);
		when(pop.getIncidenceMatrix()).thenReturn(new int[][]{
				{ 100, 1, 100, 6, 100, 100},
				{ 100, 100, 100, 100, 100, 2 },
				{ 100, 100, 100, 100, 4, 3},
				{ 100, 100, 100, 100, 5, 100 },
				{ 100, 100, 100, 100, 100, 100 }
		});

		assertEquals(21.0, i.getFit());
	}
}