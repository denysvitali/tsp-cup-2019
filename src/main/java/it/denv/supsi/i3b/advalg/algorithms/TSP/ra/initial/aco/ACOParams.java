package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.aco;

public interface ACOParams {
	double getALPHA();
	double getBeta();
	double getPD();
	double getPE();
	double getQ0();

	void setAlpha(double alpha);
	void setBeta(double beta);
}
