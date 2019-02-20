package it.denv.supsi.i3b.advalg.algorithms.TSP;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.algorithms.Coordinate;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.RoutingAlgorithm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class TSP {
	public TSP(){

	}

	public Route run(TSPData data, RoutingAlgorithm ra){
		int[][] distances = calculateDistances(data);
		data.setDistances(distances);
		//printDistanceMatrix(data.getCoordinates().size(), distances);

		return ra.route(1, data);
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
		for(int i=0; i<size; i++){
			for(int j=0; j<=i; j++){
				if(i == j){
					distances[i][j] = 0;
					distances[j][i] = 0;
				} else {
					Coordinate a = data.getCoordinates().get(i);
					Coordinate b = data.getCoordinates().get(j);

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

	// Thanks @MrRobospoccio

	public static void printDistanceMatrix(int size, int[][] distMatrix){
		for(int i = 0; i < size; i++){
			for (int j = 0; j < size; j++){
				System.out.print(distMatrix[i][j] + "\t");
			}
			System.out.println(" ");
		}
	}
}
