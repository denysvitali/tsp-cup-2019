package it.denv.supsi.i3b.advalg.ra.to;

import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.SwappablePath;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class SwappablePathTest {
	@Test
	public void swap1() {
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

	@Test
	public void swap2() {
		int i = 0;
		int j = 1;
		int k = 2;
		int l = 3;
		int m = 4;
		int n = 5;
		SwappablePath sp = new SwappablePath(new int[]{
				0, 1, 2, 3, 4, 5, 0
		});


		assertArrayEquals(
				new int[]{
						0, 2, 1, 3, 4, 5, 0
				}, sp.tswap(i, k).getPathArr());

		assertArrayEquals(
				new int[]{
						0, 2, 1, 4, 3, 5, 0
				}, sp
						.tswap(i, k)
						.tswap(j, m)
						.tswap(l, n)
						.getPathArr());
	}
}
