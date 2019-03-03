package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.aco;

import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.Edge;

import java.util.*;
import java.util.stream.Collectors;

public class Ant {
	private List<Integer> tabuList = new LinkedList<>();
	private Set<Integer> unvisitedNodes = new TreeSet<>();
	private AntColony ac;

	private static final int NOT_CALCULATED = -1;

	private double[][] probs;

	public Ant(AntColony ac){
		this.ac = ac;
		probs = new double[ac.sizeNodes()][ac.sizeNodes()];
		for(int i=0; i<ac.sizeNodes(); i++){
			probs[i][i] = NOT_CALCULATED;
		}
	}

	public void visit(int node){
		tabuList.add(node);
		unvisitedNodes.remove(node);
	}

	public int[] getVisitableNodes(){
		if(unvisitedNodes.size() == 1){
			return new int[]{ac.getTarget()};
		}

		return unvisitedNodes.stream().filter(
				c -> c == ac.getTarget()
		)
		.mapToInt(Integer::intValue)
		.toArray();
	}

	public List<Integer> getTabuList(){
		return tabuList;
	}

	public boolean hasArc(int i, int j){
		int r = -1;
		int s = -1;
		boolean a = true;

		for(Integer k : tabuList){
			r = s;
			s = k;

			if(r == i && s == j){
				return true;
			}
		}

		return false;
	}

	public double getProb(int i, int j){
		if(probs[i][i] == NOT_CALCULATED){
			calculateProb(i);
		}

		return probs[i][j];
	}

	public void calculateProb(int i){
		double summedProb = 0.0;
		int[] visitableNodes = getVisitableNodes();
		List<int[]> visitableNodesI = Arrays.asList(visitableNodes);

		for(int k=0; k<ac.sizeNodes(); k++){
			if(visitableNodesI.contains(k) && k != i){
				double probN =
					ac.getPheromone(i, k) *
					Math.pow(ac.getN(i, k), ac.getBeta());
				summedProb += probN;
				probs[i][k] = probN;
			} else {
				probs[i][k] = 0;
			}
		}

		// Calculate final vals
		for(int k=0; k<ac.sizeNodes(); k++) {
			if (visitableNodesI.contains(k) && k != i) {
				probs[i][k] = probs[i][k] / summedProb;
			}
		}

		probs[i][i] = 0;
	}

	public ArrayList<Edge<Double>> getAntDecisionTable(int i){
		ArrayList<Edge<Double>> decisionTable = new ArrayList<>();

		for(int j = 0; j<ac.sizeNodes(); j++){
			if(i == j){
				continue;
			}

			decisionTable.add(new Edge<>(i, j, ac.getPheromone(i, j)));
		}

		return decisionTable;
	}

	public int pathLength() {
		int length = 0;
		int i = tabuList.get(0);
		int j = tabuList.get(1);

		for(int k = 2; k<tabuList.size(); k++){
			length += ac.getDistances()[i][j];

			i = j;
			j = tabuList.get(k);
		}

		return length;
	}
}
