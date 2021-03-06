package it.denv.supsi.i3b.advalg.algorithms.TSP;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.algorithms.Coordinate;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.Edge;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.RoutingAlgorithm;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.spanningtree.KruskalMST;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.spanningtree.SpanningTree;
import it.denv.supsi.i3b.advalg.utils.PyPlotUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class TSP {

	private int CANDIDATES_SIZE;

	public TSP(int candidates_size){
		this.CANDIDATES_SIZE = candidates_size;
	}

	public void setCandidatesSize(int CANDIDATES_SIZE) {
		this.CANDIDATES_SIZE = CANDIDATES_SIZE;
	}

	public int getCandidatesSize() {
		return CANDIDATES_SIZE;
	}

	public Route run(TSPData data, RoutingAlgorithm ra){
		init(data);
		return ra.route(data.getStartNode(), data);
	}

	public String writeRoute(Route r) throws IOException {
		File outputfile = File.createTempFile("tsp-cup_", ".dat");
		FileOutputStream fos = new FileOutputStream(outputfile);
		for(Coordinate c : r.getCoords()){
			fos.write(String.format("%f %f\n", c.getX(), c.getY()).getBytes());
		}
		fos.close();
		return outputfile.getPath();

	}

	private int[][] calculateDistances(TSPData data) {
		int size = data.getCoordinates().size();
		int[][] distances = new int[size][size];

		ArrayList<Coordinate> values = new ArrayList<>(data.getCoordinates().values());

		for(int i=0; i<size; i++){
			for(int j=0; j<=i; j++){
				if(i == j){
					distances[i][j] = 0;
					distances[j][i] = 0;
				} else {
					Coordinate a = values.get(i);
					Coordinate b = values.get(j);

					int distance = (int) (Math.sqrt(
							Math.pow(a.getX() - b.getX(), 2) +
							Math.pow(a.getY() - b.getY(), 2)
					) + 0.5);

					distances[i][j] = distance;
					distances[j][i] = distance;
				}
			}
		}
		return distances;
	}

	public void init(TSPData data) {

		if(data.getInit()){
			return;
		}


		int[][] distances = calculateDistances(data);
		data.setDistances(distances);

		// Calculate HM<Integer, TS>
		HashMap<Integer, Set<Edge>> candidate_list = new HashMap<>();
		TreeSet<Edge> edges = new TreeSet<>();

		long s1 = System.currentTimeMillis();

		int all = 0;
		for(int i=0; i<distances.length; i++){
			ArrayList<Edge> nn = new ArrayList<>();
			for(int j=i+1; j<distances.length; j++){
				Edge e = new Edge(i, j, distances[i][j]);
				edges.add(e);
				nn.add(e);
				all++;
			}
			nn.sort(Comparator.comparing(Edge::getWeight));
			candidate_list.put(i,
					nn.stream()
					.limit(CANDIDATES_SIZE)
					.collect(Collectors.toCollection(HashSet::new)));
		}

		/*
		 	Given the Kruskal's MST, assign to each CL entry the previous / next
		 	node in the MST.
		 */

		SpanningTree st = KruskalMST.compute(data.getDim(), new ArrayList<>(edges));


		/*try {
			PyPlotUtils.plotST(st, data);
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.exit(1);*/


		for(Edge e : st.getEdges()){
			candidate_list.get(e.getU()).add(e);
			candidate_list.get(e.getV()).add(e.invert());
		}

		/*
		 	We're going to keep only the V part of the edge, to reduce space
		 	and time complexity.
		 */

		HashMap<Integer, int[]> clHM = new HashMap<>();
		for(int i = 0; i<data.getDim(); i++){
			clHM.put(i, candidate_list.get(i).stream().mapToInt(Edge::getV)
				.toArray()
			);
		}



		long s2 = System.currentTimeMillis();

		System.out.println("Computed HM<I, A<Edge>> in " + (s2-s1) + " ms");

		data.setCL(clHM);
		//data.setEdges(edges);
		//printDistanceMatrix(data.getCoordinates().size(), distances);

		data.setStartNode(1);
		data.setInit(true);
	}
}
