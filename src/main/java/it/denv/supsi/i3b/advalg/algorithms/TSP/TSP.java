package it.denv.supsi.i3b.advalg.algorithms.TSP;

import it.denv.supsi.i3b.advalg.algorithms.Coordinate;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;

public class TSP {
	public TSP(){

	}

	public void run(TSPData data){
		double[][] distances = calculateDistances(data);

	}

	private double[][] calculateDistances(TSPData data) {
		int size = data.getCoordinates().size();
		double[][] distances = new double[size][size];
		for(int i=0; i<size/2; i++){
			for(int j=0; j<size/2; j++){
				System.out.println(i + "," + j);

				if(i == j){
					distances[i][j] = 0;
					distances[j][i] = 0;
				} else {
					Coordinate a = data.getCoordinates().get(i);
					Coordinate b = data.getCoordinates().get(j);

					double distance = Math.sqrt(
							Math.pow(a.getX() - b.getX(), 2) +
							Math.pow(a.getY() - b.getY(), 2)
					);
					distances[i][j] = distance;
					distances[j][i] = distance;
				}
			}
		}
		return distances;
	}
}
