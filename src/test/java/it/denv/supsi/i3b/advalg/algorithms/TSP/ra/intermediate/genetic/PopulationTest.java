package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.genetic;

import it.denv.supsi.i3b.advalg.Route;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PopulationTest {
	@Test
	public void getGenes(){
		Route r = mock(Route.class);
		when(r.getPath()).thenReturn(new int[]{
				0,1,5,2,4,3,0
		});

		boolean[] genes = Population.getGenes(r);

		assertArrayEquals(new boolean[]{
			true, false, true, false, false,
				false, false, false, true,
				false, true, true,
				true, false,
				false
		}, genes);
	}

	@Test
	public void isValid(){
		assertTrue(Population.isValid(new boolean[]{
				true, false, true, false, false,
				false, false, false, true,
				false, true, true,
				true, false,
				false
		}));
	}

	@Test
	public void mutateGenes(){
		Route r = mock(Route.class);
		when(r.getPath()).thenReturn(new int[]{
				0,1,5,2,4,3,0
		});

		Population p = new Population(new GeneticAlgorithm(null),
				r,
				null);
		boolean[] new_genes = p.mutateGenes(new boolean[]{
				true, false, true, false, false,
				false, false, false, true,
				false, true, true,
				true, false,
				false
		});

		assertTrue(Population.isValid(new_genes));
	}
}