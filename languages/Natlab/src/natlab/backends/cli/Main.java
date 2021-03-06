package natlab.backends.cli;

import natlab.tame.*;
import natlab.tame.valueanalysis.*;
import natlab.tame.valueanalysis.aggrvalue.*;
import natlab.tame.valueanalysis.basicmatrix.*;
import natlab.toolkits.filehandling.*;
import natlab.toolkits.path.*;

import java.io.*;

public class Main
{
	public static void main(String[] args) throws Throwable
	{
		if (args.length == 0)
		{
			System.out.println("Usage: <program> source [output] [type]");
			System.out.println("  outputs defaults to {source}.tir.xml");
			System.out.println("  type defaults to DOUBLE&1*1&REAL");
			return;
		}

		String sourceFilePath = args[0];
		String outputFilePath = args.length >= 2 ? args[1] : getDefaultOutputFilePath(sourceFilePath);
		GenericFile gFile = GenericFile.create(sourceFilePath);
		FileEnvironment env = new FileEnvironment(gFile); // get path
		BasicTamerTool tool = new BasicTamerTool();
		tool.setDoIntOk(false);

		args = new String[] { args.length >= 3 ? args[2] : "DOUBLE&1*1&REAL" };
		ValueAnalysis<AggrValue<BasicMatrixValue>> analysis = tool.analyze(args, env);

		XMLTIRSerializer.write(analysis, new FileOutputStream(outputFilePath));
	}

	private static String getDefaultOutputFilePath(String sourceFilePath)
	{
		int lastDirectorySeparatorIndex = sourceFilePath.lastIndexOf(System.getProperty("file.separator"));
		int lastDotIndex = sourceFilePath.lastIndexOf('.');
		if (lastDotIndex > lastDirectorySeparatorIndex)
			sourceFilePath = sourceFilePath.substring(0, lastDotIndex);

		return sourceFilePath + ".tirxml";
	}
}