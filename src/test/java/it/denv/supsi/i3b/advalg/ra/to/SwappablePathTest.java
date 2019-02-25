package it.denv.supsi.i3b.advalg.ra.to;

import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.to.SwappablePath;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class SwappablePathTest {
	@Test
	public void swap1(){
		SwappablePath sp = new SwappablePath(new int[]{
				0, 1, 2, 3, 4, 5, 6, 7, 0
		});

		SwappablePath sp2 = sp.swap(3, 6);

		int finalPath[] = sp2.getPath().stream().mapToInt(Integer::intValue)
				.toArray();

		assertArrayEquals(new int[]{
				0, 1, 2, 6, 5, 4, 3, 7, 0
		}, finalPath);
	}
}
