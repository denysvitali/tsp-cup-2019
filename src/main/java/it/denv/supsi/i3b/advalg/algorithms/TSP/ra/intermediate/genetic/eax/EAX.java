package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.genetic.eax;

import it.denv.supsi.i3b.advalg.algorithms.NotImplementedException;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.genetic.Individual;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.genetic.Population;
import it.denv.supsi.i3b.advalg.utils.GnuPlotUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class EAX {
	private static final boolean DEBUG = true;

	public static ABCycle[] generateABCycles(Individual A, Individual B){
		ABCycle[] cycles = new ABCycle[10];

		// TODO: Implement
		throw new NotImplementedException();
		//return cycles;
	}

	public static ABCycle[] rand(ABCycle[] cycles, Random r, double v) {
		ABCycle[] endCycles = new ABCycle[cycles.length];

		int i=0;
		if(r.nextDouble() > v){
			endCycles[i] = cycles[i];
			i++;
		}


		ABCycle[] returnCycle = new ABCycle[i];
		System.arraycopy(endCycles, 0, returnCycle, 0, i);

		return returnCycle;
	}

	public static ABCycle[] heur(ABCycle[] cycles, Population population) {
		// TODO: Implement Heuristic EAX
		//population.data.
		throw new NotImplementedException();
	}

	public static Individual crossover(Individual A, Individual B, Population p) {
		EAXGraph g = merge(A, B, p);

		if(DEBUG) {
			GnuPlotUtils.plotEAXGraph(g, p.getData());
		}

		// 1. Generate AB-Cycles
		ABCycle[] cycles = generateABCycles(A, B);

		// 2. Select a subset of the AB-Cycles (E-Set)

		// 	a)	Use EAX(heuristic)

		ABCycle[] eSetHeur = heur(cycles, p);

		// If no children that are better than both A and B are found
		// 	b) 	Use EAX(rand)

		/* 		i) 	Produce more children until either
					an improved child is found or 100 children
					are produced.
		 */
		ABCycle[] eSetRand = EAX.rand(cycles, p.getRandom(), 0.5);

		// 3. Return the generated child
		throw new NotImplementedException();
	}

	private static EAXGraph merge(Individual a, Individual b, Population p) {
		boolean[] a_genes = a.getGenes();
		boolean[] b_genes = b.getGenes();

		System.out.println("A: " + a.prettyPrintGenes());
		System.out.println("B: " + b.prettyPrintGenes());

		// Do not merge two equal individuals
		assert(!(Arrays.equals(a_genes, b_genes)));

		EAXGraph g = new EAXGraph();

		/*
			Parse Genes

			Given a path (e.g: 1, 2, 6, 3, 5, 4, 1),
			the incidence matrix is the following:

			0 1 0 1 0 0
			X 0 0 0 0 1
			X X 0 0 1 1
			X X X 0 1 0
			X X X X 0 0
			X X X X X 0

			We'll consider only the upper part of the incidence
			matrix since the TSP problem is asymmetric.

					We can then discard the i = j column, since
			a city can't be visited twice.

			This gives us the following matrix:

				1 2 3 4 5 6

			1	X 1 0 1 0 0
			2	X X 0 0 0 1
			3	X X X 0 1 1
			4	X X X X 1 0
			5	X X X X X 0
			6	X X X X X X

		*/

		ABEdge[] edges_a = getEdges(a_genes, true, p);
		ABEdge[] edges_b = getEdges(b_genes, false, p);

		for(ABEdge e : edges_a){
			g.addEdge(e);
		}

		for(ABEdge e : edges_b){
			g.addEdge(e);
		}

		return g;
	}

	private static ABEdge[] getEdges(boolean[] genes, boolean is_A, Population p) {
		int n = p.getDimension();
		ABEdge[] edges = new ABEdge[n];

		int k = 0;
		int l = 0;
		for(int i=0; i<p.getDimension(); i++){
			for(int j=i+1; j<p.getDimension(); j++){
				if(genes[k]){
					edges[l] = new ABEdge(i, j, is_A);
					l++;
				}
				k++;
			}
		}

		return edges;

	}
}
