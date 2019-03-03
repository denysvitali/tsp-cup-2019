package it.denv.supsi.i3b.advalg.ra.to;

import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.SwappablePath;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class SwappablePathTest {
	@Test
	public void swap1(){
		SwappablePath sp = new SwappablePath(new int[]{
				0, 1, 2, 3, 4, 5, 6, 7, 0
		});

		SwappablePath sp2 = sp.swap(3, 6);

		assertArrayEquals(new int[]{
				0, 1, 2, 6, 5, 4, 3, 7, 0
		}, sp2.getPathArr());

		sp2 = sp2.swap(3, 6);
		assertArrayEquals(new int[]{
				0, 1, 2, 3, 4, 5, 6, 7, 0
		}, sp2.getPathArr());
	}
}
