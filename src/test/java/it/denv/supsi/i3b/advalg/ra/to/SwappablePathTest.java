package it.denv.supsi.i3b.advalg.ra.to;

import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.SwappablePath;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class SwappablePathTest {
	@Test
	public void twoOptSwap1() {
		SwappablePath sp1 = new SwappablePath(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 0});
		SwappablePath sp2 = sp1.twoOptSwap(3, 6); // Swap Edge 1-2 w/ Edge 3-4

		assertArrayEquals(new int[]{0, 1, 2, 6, 5, 4, 3, 7, 0}, sp2.getPathArr());
	}

	@Test
	public void threeOptSwap1() {
		SwappablePath sp1 = new SwappablePath(new int[]{1, 2, 3, 4, 5, 6});

		SwappablePath[] threeOpt = sp1.threeOptSwap(0, 2, 4);


		SwappablePath first = threeOpt[0];
		SwappablePath second = threeOpt[1];

		int[] expected = new int[]{1, 4, 5, 2, 3, 6};

		assertArrayEquals(expected, first.getPathArr());

		System.out.println("Expected: " + Arrays.toString(expected) + "\n" +
				"Obtained: " + Arrays.toString(first.getPathArr()));

		int[] expected2 = new int[]{1, 3, 2, 5, 4, 6};


		System.out.println("Expected2: " + Arrays.toString(expected2) + "\n" +
				"Obtained2: " + Arrays.toString(second.getPathArr()));


		assertArrayEquals(expected2, second.getPathArr());
	}

	@Test
	public void threeOptSwap2() {
		SwappablePath sp1 = new SwappablePath(new int[]{1, 2, 25, 3, 4, 45, 5, 6, 65});

		SwappablePath[] threeOpt25 = sp1.threeOptSwap(0, 3, 6);


		SwappablePath first = threeOpt25[0];
		SwappablePath second = threeOpt25[1];

		int[] expected = new int[]{1, 4, 45, 5, 2, 25, 3, 6, 65};


		System.out.println("Expected: " + Arrays.toString(expected) + "\n" +
				"Obtained: " + Arrays.toString(first.getPathArr()));

		assertArrayEquals(expected, first.getPathArr());

		System.out.println();

		int[] expected2 = new int[]{1, 3, 25, 2, 5, 45, 4, 6, 65};
		System.out.println("Original: " + Arrays.toString(sp1.getPathArr()) + "\n" +
				"Expected2: " + Arrays.toString(expected2) + "\n" +
				"Obtained2: " + Arrays.toString(second.getPathArr()));


		assertArrayEquals(expected2, second.getPathArr());
	}

	@Test
	void twoOptSwap () {
		SwappablePath sp1 = new SwappablePath(new int[]{0,2,1,3});
		int[] expected = new int[]{0,1,2,3};

		SwappablePath sp2 = sp1.twoOptSwap(1,2);
		System.out.println("Expected: " + Arrays.toString(expected) + "\n" +
				"Obtained: " + Arrays.toString(sp2.getPathArr()));
		assertArrayEquals(expected, sp2.getPathArr());

	}
}
