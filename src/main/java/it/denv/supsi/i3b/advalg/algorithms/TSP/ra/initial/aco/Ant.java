package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.aco;

import com.sun.source.tree.Tree;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.Edge;

import java.util.*;

public class Ant {
	private List<Integer> tabuList = new LinkedList<>();
	private Set<Integer> unvisitedNodes = new TreeSet<>();
	private int currentNode = -1;
	private AntColony ac;
	private AntStatus as = AntStatus.STOPPED;

	private static final int NOT_CALCULATED = -1;

	private double[][] probs;

	public Ant(AntColony ac){
		this.ac = ac;
		reset();
		visit(0);
	}

	private int getProbabilisticRandom(double r, int i){
		// R ranges from 0 to 1
		int j = -1;

		if(probs[i][i] == NOT_CALCULATED){
			calculateProb(i);
		}

		double sum = 0.0;

		for(int k=0; k<probs[i].length; k++){
			if(r > sum && r <= sum + probs[i][k]) {
				return k;
			}
			sum += probs[i][k];
		}

		System.out.println(sum);
		assert(Math.abs(sum - 1.0) < 0.01);

		return j;
	}

	public AntStatus getStatus() {
		return as;
	}

	public AntStatus doStep(){
		int i = currentNode;
		int nextNode = -1;

		// Visit a city
		int[] visitableNodes = getVisitableNodes();

		if(visitableNodes.length == 0){
			this.as = AntStatus.STOPPED;
			visit(tabuList.get(0));
			return this.as;
		}

		double randomValue = Math.random();

		if(randomValue <= this.ac.getQ0()){
			// Get max [T_{il}] \cdot [n_{jl}]^Beta
			// TODO: Implement
			Edge<Double> max = getMax(i);
			nextNode = max.getSecond();
		} else {
			double r2 = Math.random();
			nextNode = getProbabilisticRandom(r2, i);
		}

		visit(nextNode);

		int j = currentNode;

		currentNode = nextNode;

		/*double oldPV = ac.getPheromoneValues()[i][j];
		double initialPV = ac.getInitialPheromoneValue(i, j);
		double newPV = (1-ac.getAlpha()) * oldPV + initialPV;

		ac.getPheromoneValues()[i][j] = newPV;*/
		return this.as;
	}

	public void visit(int j){

		int i = currentNode;

		if(i != -1){
			ac.setPheromone(i, j,
					(1-ac.PL) *
							ac.getPheromone(i, j)
							+ ac.PL * ac.getiPV()
			);
		}

		tabuList.add(j);
		unvisitedNodes.remove(j);
		currentNode = j;

		probs = new double[ac.sizeNodes()][ac.sizeNodes()];

		for(int k=0; k<ac.sizeNodes(); k++){
			probs[k][k] = NOT_CALCULATED;
		}
	}

	public int[] getVisitableNodes(){
		if(unvisitedNodes.size() == 1){
			return new int[]{ac.getTarget()};
		}

		return unvisitedNodes.stream().filter(
				c -> c != ac.getTarget()
		)
		.mapToInt(Integer::intValue)
		.toArray();
	}

	public List<Integer> getTabuList(){
		return tabuList;
	}

	public boolean hasArc(int i, int j){
		int r;
		int s = -1;

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
		List<Integer> visitableNodesI = new ArrayList<>(visitableNodes.length);

		for (int visitableNode : visitableNodes) {
			visitableNodesI.add(visitableNode);
		}

		for(int k=0; k<ac.sizeNodes(); k++){
			if(visitableNodesI.contains(k) && k != i){


				double probN =
					ac.getT(ac.getTime())[i][k] *
					Math.pow(ac.getN(i, k), ac.getBeta());
				summedProb += probN;
				probs[i][k] = probN;
			} else {
				probs[i][k] = 0;
			}
			probs[k][i] = probs[i][k];
		}

		// Calculate final vals
		for(int k=0; k<ac.sizeNodes(); k++) {
			if (visitableNodesI.contains(k) && k != i) {
				System.out.println("1: " + probs[i][k]);
				probs[i][k] = probs[i][k] / summedProb;
				System.out.println("2: " + probs[i][k]);
			}
		}

		System.out.println("-----");

		probs[i][i] = 0;
	}

	public Edge<Double> getMax(int i){
		TreeSet<Edge<Double>> decisionTable = new TreeSet<>();

		for(int u : getVisitableNodes()){
			if(i == u){
				// Paranoid Check
				continue;
			}

			decisionTable.add(new Edge<>(i, u,
					// T_{il} \cdot n{jl}
					ac.getPheromone(i, u) *
							Math.pow(
									ac.getN(i, u),
									ac.getBeta()
							)
					));
		}

		return decisionTable.last();
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

	public void reset() {
		currentNode = ac.getTarget();
		tabuList.clear();
		unvisitedNodes.clear();

		probs = new double[ac.sizeNodes()][ac.sizeNodes()];
		for(int i=0; i<ac.sizeNodes(); i++){
			probs[i][i] = NOT_CALCULATED;
		}

		for(int i=0; i<ac.sizeNodes(); i++){
			unvisitedNodes.add(i);
		}

		this.as = AntStatus.RUNNING;
	}
}
