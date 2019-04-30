package it.denv.supsi.i3b.advalg;

import org.junit.jupiter.api.Test;

import java.io.File;
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
				.map(e -> String.format("\t// " + e + "\n" +
						"\n" +
						"\t@Test\n" +
						"\tpublic void " + e + "ACS_SF() {\n" +
						"\t\trunACSThreaded(runACS(\""+e+"\"));\n" +
						"\t}\n" +
						"\n" +
						"\t@Test\n" +
						"\tpublic void " + e + "SA_SF() {\n" +
						"\t\trunThreaded(runSA(\""+e+"\"));\n" +
						"\t}\n"))
				.forEach(System.out::println);
	}
}
