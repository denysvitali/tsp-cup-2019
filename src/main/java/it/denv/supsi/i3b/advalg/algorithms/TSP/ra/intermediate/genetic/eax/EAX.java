package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.genetic.eax;

import it.denv.supsi.i3b.advalg.algorithms.NotImplementedException;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.Edge;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.genetic.GeneticAlgorithm;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.genetic.Individual;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.genetic.Population;
import it.denv.supsi.i3b.advalg.utils.GnuPlotUtils;
import it.denv.supsi.i3b.advalg.utils.PyPlotUtils;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class EAX {
	private static final boolean DEBUG = true;

	public static ArrayList<ABCycle> generateABCycles(EAXGraph g, GeneticAlgorithm ga){
		ArrayList<ABCycle> abCycles = new ArrayList<>();
		ABCycle cycle;

		do {
			cycle = generateABCycle(g, ga);
			if(cycle != null) {
				abCycles.add(cycle);
			}
		} while(cycle != null);

		if(DEBUG){
			//GnuPlotUtils.plotABCycles(abCycles, ga.getData());
		}

		return abCycles;
	}

	/*public static boolean containsABCycle(ArrayList<ABEdge> edges){

		for(int i=0; i<edges.size(); i++) {
			int startCity = edges.get(i).getU();

			for(int j=i; j<edges.size(); j++){
				if(edges.get(j).getV() == startCity){
					return true;
				}
			}
		}

		return false;
	}*/

	public static ABCycle generateABCycle(EAXGraph g, GeneticAlgorithm ga) {
		ABCycle cycle = null;
		ArrayList<ABEdge> edges = g.getEdges();
		ArrayList<ABEdge> tmpEdges = new ArrayList<>();

		if(edges.size() == 0){
			return null;
		}

		ABEdge startEdge = edges.get(ga.getRandom().nextInt(edges.size()));
		boolean lastA = startEdge.isA();
		tmpEdges.add(startEdge);
		int length = 1;

		int currentCity = startEdge.getV();

		while(length % 2 != 0 || (cycle = ABCycle.getCycle(tmpEdges)) == null){
			Optional<ABEdge> e;

			if(lastA){
				e = g.getCity(currentCity)
						.stream().filter(a -> !a.isA()).findAny();
			} else {
				e = g.getCity(currentCity)
						.stream().filter(a -> a.isA()).findAny();
			}

			if(e.isEmpty()){
				// Get another edge, if possible
				boolean finalLastA = lastA;
				e = edges.stream().filter(a -> a.isA() == finalLastA)
						.findAny();

				if(e.isEmpty()){
					break;
				}
			}

			ABEdge edge = e.get();
			lastA = edge.isA();
			length++;

			int prevCity = currentCity;
			if(currentCity == edge.getU()){
				currentCity = edge.getV();
				// Normal edge
				tmpEdges.add(edge);
			} else {
				currentCity = edge.getU();
				// Reverse Edge, set Ref!
				tmpEdges.add(new ABEdge(prevCity, currentCity,
						edge.isA(), edge));
			}
		}

		if(cycle == null){
			return null;
		}

		for(ABEdge e : cycle.getPath()){
			if(e.getRef() != null){
				e = e.getRef();
			}

			int u = e.getU();
			int v = e.getV();

			g.getCity(u).remove(e);
			g.getCity(v).remove(e);
			g.removeEdge(e);

			if(e.isA()){
				g.removeFromA(e);
			} else {
				g.removeFromB(e);
			}
		}

		return cycle;
	}

	public static ArrayList<ABCycle> rand(ArrayList<ABCycle> cycles, Random r, double v) {
		ArrayList<ABCycle> endCycles = new ArrayList<>();

		for(ABCycle c : cycles){
			if(r.nextDouble() > v){
				endCycles.add(c);
			}
		}

		return endCycles;
	}

	public static ArrayList<ABCycle> heur(ArrayList<ABCycle> cycles, Population population) {
		// TODO: Implement Heuristic EAX
		//population.data.
		throw new NotImplementedException();
	}

	public static Individual crossover(Individual A, Individual B, Population p) {
		/*
			0.	Let G_{AB} be the undirected multigraph (overlapped edges,
				one from E_A and one from E_B, are distinguished) defined as
				G_{AB} = (V, E_A \\union E_B). [1]

			0.	Denote a pair of parents as Tour-A and Tour-B, and define
				G_{AB} as a graph constructed by merging Tour-A and Tour-B [2]
		 */

		EAXGraph g = merge(A, B, p);
		EAXGraph g2 = g.clone();

		if(DEBUG) {
			//GnuPlotUtils.plotEAXGraph(g, p.getData());
		}

		/*
			1.	Partition all edges of G_{AB} into AB-cycles, where an
				AB-Cycle is defined as a cycle in G_{AB}, such that edges of
				E_A and edges of E_B are alternately linked. [1]
			--------------------------------------------------------------------

		 	1. 	Divide edges on G_{AB} into AB-Cycles, where an AB-Cycle
		 		is defined as a closed loop on G_{AB} that can be generated
		 		by alternating tracing edges of tour-A and tour-B [2]
		 	-------------------------------------------------------------------
		 */
		ArrayList<ABCycle> cycles = generateABCycles(g, p.getGA());
		ArrayList<ABCycle> newCycles = new ArrayList<>();

		for (ABCycle cycle : cycles) {
			if (cycle.getPath().size() != 2) {
				/*
					AB-Cycles constructed of two edges are neglected because
					they're "ineffective".
				 */
				newCycles.add(cycle);
			}
		}
		cycles = newCycles;

		/*
			2.	Select a subset of the AB-Cycles (E-Set)
			-------------------------------------------------------------------
				Construct an E-Set by selecting AB-Cycles according to a given
				selection strategy, where an E-Set is defined as the union of
				AB-cycles. Note that this selection strategy determines a
				version of EAX. [1]

				Construct an E-Set by selecting AB-Cycles according to a given
				rule. [2]


			a) 	Use EAX(heuristic)
		 */

		//ArrayList<ABCycle> eSetHeur = EAX.heur(cycles, p);

		/*
			If no children that are better than both A and B are found
			b) 	Use EAX(rand)
				i) 	Produce more children until either
					an improved child is found or 100 children
					are produced.
		 */
		ArrayList<ABCycle> randABCycles = EAX.rand(cycles, p.getRandom(), 0.5);

		/*
			3.	Generate an intermediate solution from p_A by removing
				the edges of E_A and adding the edges of E_B in the E-Set,
				i.e, generate an intermediate solution by
				E_C := (E_A \setminus (\text{E-Set} \cap E_A)) \cup (\text{E-Set} \cap E_B)
				An intermediate solution consists of one or more subtours. [1]
		*/

		/*
		 	E-Set is an union of edges from the selected AB-Cycles.
		 */
		HashSet<ABEdge> eSet = new HashSet<>(); // TODO: Fix

		for(ABCycle c : randABCycles){
			for(ABEdge e : c.getPath()){
				if(e.getRef() != null){
					e = e.getRef();
				}

				eSet.add(e);
			}
		}

		ArrayList<ABEdge> eRight = new ArrayList<>();
		ArrayList<ABEdge> eLeft = new ArrayList<>(g2.getAList());

		for(ABEdge edge : eSet){
			if(!edge.isA()){
				continue;
			}

			// E-Set \cap E_A

			if(edge.getRef() != null){
				edge = edge.getRef();
			}

			eLeft.remove(edge);

			// E_A \setminus (E-Set \cap E_A)
		}

		for(ABEdge edge : eSet){
			if(edge.isA()){
				continue;
			}

			if(edge.getRef() != null){
				edge = edge.getRef();
			}

			eRight.add(edge);
		}


		ABCycle intermediate = new ABCycle();
		intermediate.addAll(eLeft);
		intermediate.addAll(eRight);

		PyPlotUtils.plotABCycle(intermediate, p.getData());

		// Given an intermediate solution, get the subtours
		ArrayList<ABCycle> subtours = intermediate.intoSubtours();
		PyPlotUtils.plotABCycles(subtours, p.getData());
		subtours.stream().map(ABCycle::getPath).forEach(System.out::println);

		/*
			4.	Generate an offspring solution by connecting all subtours
				into a tour (details are given later)

			5.	If a further offspring solution is generated, then go to Step 2.
				Otherwise, terminate the procedure.
		 */

		System.out.println("DONE");
		throw new NotImplementedException();

		/*
			References:
			[1] Nagata and Kobayashi: A Powerful Genetic Algorithm Using EAX for
				the TSP
			[2] PPSN IX: Yuichi Nagata: New EAX Crossover for Large TSP Instances
		 */
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
