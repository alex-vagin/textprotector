package com.lb.textprotector;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
	private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
	
	private static final String INPUT_FILE_NAME  = "i";
	private static final String OUTPUT_FILE_NAME = "o";
	private static final String KEY              = "k";
	private static final String HELP             = "h";
	private static final String ENCODE           = "e";
	private static final String DECODE           = "d";
	private static final Options options = new Options();
	private static final CommandLineParser parser = new DefaultParser();
	private static CommandLine cmd;
	
	static {
		options.addOption(HELP, false, "Print this message");
		options.addOption(Option.builder(INPUT_FILE_NAME).required().hasArg().argName("INPUT_FILE_NAME").desc("Input file name").build());
		options.addOption(Option.builder(OUTPUT_FILE_NAME).required().hasArg().argName("OUTPUT_FILE_NAME").desc("Output file name").build());
		options.addOption(Option.builder(KEY).hasArg().required().argName("KEY").desc("Key").build());
		options.addOption(Option.builder(ENCODE).argName("Encode").desc("Encode mode").build());
		options.addOption(Option.builder(DECODE).argName("Decode").desc("Decode mode").build());
	}
	
	private static void printUsage() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp( "java -jar textprotector-1.0.jar -i <Input file name> -o <Output file name> -k <Key>", options);
	}
	
	private static boolean checkMandatoryArg(String arg, String msg) {
		boolean result = cmd.hasOption(arg);
		checkCondition(result, msg);
		return result;
	}
	
	private static boolean checkCondition(boolean condition, String msg) {
		if (!condition) {
			printUsage();
			LOGGER.error(msg);
		}
		return condition;
	}
	
	public static void main(String[] args) {
		try {
			cmd = parser.parse(options, args);
			if (checkCondition(cmd.hasOption(ENCODE) || cmd.hasOption(DECODE), "You have to define mode - encode or decode")) {
				String inputFileName = cmd.getOptionValue(INPUT_FILE_NAME);
				String outputFileName = cmd.getOptionValue(OUTPUT_FILE_NAME);
				String key = cmd.getOptionValue(KEY);

				if (cmd.hasOption(ENCODE)) {
					new Encryptor().setInputFileName(inputFileName).setOutputFileName(outputFileName).setKey(key).crypt();
				} else {
					new Decryptor().setInputFileName(inputFileName).setOutputFileName(outputFileName).setKey(key).crypt();
				}
			}
		} catch (ParseException e) {
			LOGGER.error("Invalid commandline parameters", e);
		}
	}
}
