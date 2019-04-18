package it.denv.supsi.i3b.advalg.utils;

import it.denv.supsi.i3b.advalg.algorithms.Coordinate;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.genetic.eax.ABCycle;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.genetic.eax.ABEdge;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PyPlotUtilsTest {

	@Test
	void plotABCycle() {
		TSPData d = mock(TSPData.class);
		HashMap<Integer, Coordinate> pointsHM = new HashMap<>();

		pointsHM.put(1, new Coordinate(1, 1,1));
		pointsHM.put(2, new Coordinate(2, -1,1));
		pointsHM.put(3, new Coordinate(3, 1,-1));
		pointsHM.put(4, new Coordinate(4, -1,-1));

		pointsHM.put(5, new Coordinate(5, 2,2));
		pointsHM.put(6, new Coordinate(6, -2,2));
		pointsHM.put(7, new Coordinate(7, -2,-2));
		pointsHM.put(8, new Coordinate(8, 2,-2));


		when(d.getCoordinates()).thenReturn(
				pointsHM
		);

		ABCycle abCycle = new ABCycle();
		// First Triangle
		abCycle.add(new ABEdge(0,1, false));
		abCycle.add(new ABEdge(1,2, false));
		abCycle.add(new ABEdge(2,0, false));

		// Square
		abCycle.add(new ABEdge(4,5, false));
		abCycle.add(new ABEdge(5, 6, false));
		abCycle.add(new ABEdge(6, 7, false));
		abCycle.add(new ABEdge(7, 4, false));



		PyPlotUtils.plotABCycle(abCycle, d);
	}
}