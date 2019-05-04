package it.denv.supsi.i3b.advalg;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

public class TestGenerator {

	@Test
	public void doTestGen(){
		String problems = TestGenerator.class.getResource("/problems")
				.getPath();

		File problemsFolder = new File(problems);
		File[] problemsFiles = problemsFolder.listFiles();

		Arrays.stream(problemsFiles).map(File::getName)
				.filter(e -> e.endsWith(".tsp"))
				.sorted(String::compareToIgnoreCase)
				.map(e -> e.substring(0, e.length()-4))
				.map(e -> "\t// " + e + "\n" +
						"\n" +
						"\t@Test\n" +
						"\tpublic void " + e + "ACS_SF() {\n" +
						"\t\trunACSThreaded(runACS(\""+e+"\"));\n" +
						"\t}\n" +
						"\n" +
						"\t@Test\n" +
						"\tpublic void " + e + "SA_SF() {\n" +
						"\t\trunSAThreaded(runSA(\""+e+"\"));\n" +
						"\t}\n")
				.forEach(System.out::println);
	}

	@Test
	public void doSolverTestGen(){
		String problems = TestGenerator.class.getResource("/problems")
				.getPath();

		File problemsFolder = new File(problems);
		File[] problemsFiles = problemsFolder.listFiles();

		Arrays.stream(problemsFiles).map(File::getName)
				.filter(e -> e.endsWith(".tsp"))
				.sorted(String::compareToIgnoreCase)
				.map(e -> e.substring(0, e.length()-4))
				.map(e -> "\t// " + e + "\n" +
						"\n" +
						"\t@Test(timeout = 181000)\n" +
						"    public void " + e + "() {\n" +
						"        TSPRunnerTest."+e+"();\n" +
						"    }\n")
				.forEach(System.out::println);
	}

	private static String getTestString(String e) {
		try {
			URLConnection urlConn = new URL("http://ded1.denv.it:12538/api/v1/getResults?problem="
					+ e).openConnection();
			String s = new String(urlConn.getInputStream().readAllBytes());
			urlConn.connect();

			JSONObject obj = new JSONObject(s);

			String jsonString = obj.getString("json");
			if(jsonString.equals("")){
				return "\t// " + e + "\n" +
						"\n" +
						"\t@Test\n" +
						"\tpublic void " + e + "() {\n" +
						"\tSystem.out.println(\"Not implemented\");\n" +
						"\t}\n";
			}

			JSONObject obj2 = new JSONObject(jsonString);

			JSONArray algorithms = obj2.getJSONArray("algorithms");
			if(algorithms.length() == 4){
				// RNN + 2Opt + SA + Two Opt => R2S2
				JSONObject sa = algorithms.getJSONObject(2);
				int sa_seed = sa.getInt("seed");
				String sa_mode = sa.getString("mode");

				// Params:
				JSONObject params = sa.getJSONObject("params");
				double sa_alpha = params.getDouble("alpha");
				int sa_r = params.getInt("r");
				double sa_st = params.getDouble("start_temperature");


				return "\t// " + e + "\n" +
						"\n" +
						"\t@Test\n" +
						"\tpublic void " + e + "() {\n" +
						"\t\twriteTestResult(runR2S2(\"" + e + "\"," +
						"SimulatedAnnealing.Mode." +
						sa_mode + "," +
						sa_seed + "," +
						sa_alpha + "," +
						sa_r + "," +
						sa_st +
						"));\n" +
						"\t}\n";
			}


			return "";
		} catch (IOException ex){
			ex.printStackTrace();
			return "";
		}
	}

	@Test
	public void doSATestGen(){
		String problems = TestGenerator.class.getResource("/problems")
				.getPath();

		File problemsFolder = new File(problems);
		File[] problemsFiles = problemsFolder.listFiles();

		assert problemsFiles != null;
		Arrays.stream(problemsFiles).map(File::getName)
				.filter(e -> e.endsWith(".tsp"))
				.sorted(String::compareToIgnoreCase)
				.map(e -> e.substring(0, e.length()-4))
				.map(TestGenerator::getTestString)
				.forEach(System.out::println);
	}
}
