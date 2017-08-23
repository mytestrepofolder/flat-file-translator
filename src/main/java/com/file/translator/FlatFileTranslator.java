package com.file.translator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Plabon.Kakoti
 * @version 1.0
 * 
 */
public class FlatFileTranslator {

	// All three files are read from the folder: "files"
	private static final String VENDOR_FILE = "\\files\\vendor.txt";
	private static final String COL_MAPPING = "\\files\\columnMapping.txt";
	private static final String ID_MAPPING = "\\files\\idMpping.txt";

	public static void main(String[] args) {
		localFileRead();
	}

	/**
	 * Processes the Vendor specific and Mapping files.
	 * 
	 * @return void
	 */
	public static void localFileRead() {

		String delimiter = "\t";
		Map<String, String> idMap = new HashMap<>();
		Map<String, String> colMap = new HashMap<>();
		List<String> tempList = new ArrayList<>();
		List<String> reqColList = new ArrayList<>();
		try {
			String currentDir = Paths.get(".").toAbsolutePath().toString();
			String absPath = currentDir.substring(0, currentDir.length() - 1).replace("\\", "\\\\");
			// Get the list of column headers only
			List<String> columnHeaderList = reaFileColumnHeaders(absPath);

			int colNameIndex;
			try (Stream<String> colLines = Files.lines(Paths.get(absPath + COL_MAPPING))) {
				colLines.filter(line -> line.contains(delimiter)).forEach(line -> colMap.putIfAbsent(line.split(delimiter)[0], line.split(delimiter)[1]));
			}
			List<String> displayColList = new ArrayList<String>();
			for (String colname : columnHeaderList) {
				if (null != colMap.get(colname)) {
					displayColList.add(colMap.get(colname));
				} else {
					displayColList.add(colname);
					colNameIndex = displayColList.indexOf(colname);
					List<String> lines = Files.readAllLines(Paths.get(absPath + VENDOR_FILE));
					if (!reqColList.isEmpty()) {
						lines.clear();
						lines.addAll(reqColList);
						reqColList.clear();
					}
					for (String row : lines) {
						String[] colValues = row.split("\t");
						tempList = Arrays.asList(colValues).stream().collect(Collectors.toList());
						tempList.remove(colNameIndex);
						String line = String.join("\t", tempList);
						reqColList.add(line);
					}
					displayColList.remove(colname);
				}

			}
			// Perform the ID/Row mapping
			List<String> newList = rowMapping(delimiter, idMap, reqColList, absPath);

			// Print the final output file
			printOutputFile(displayColList, newList);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param displayColList
	 * @param newList
	 * @return void
	 */
	public static void printOutputFile(List<String> displayColList, List<String> newList) {
		String displayColumnHeader = String.join("\t", displayColList);
		System.out.println("Converted File :");
		System.out.println("------------------");
		newList.add(0, displayColumnHeader);
		newList.stream().forEach(System.out::println);
	}

	/**
	 * @return List<String>
	 * @param absPath
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static List<String> reaFileColumnHeaders(String absPath) throws FileNotFoundException, IOException {
		BufferedReader brTest = new BufferedReader(new FileReader(absPath + VENDOR_FILE));
		String columnHeader = brTest.readLine();
		brTest.close();
		List<String> columnHeaderList = new ArrayList<String>(Arrays.asList(columnHeader.split("\t")));
		return columnHeaderList;
	}

	/**
	 * @param delimiter
	 * @param idMap
	 * @param reqColList
	 * @param absPath
	 * @return List<String>
	 * @throws IOException
	 */
	public static List<String> rowMapping(String delimiter, Map<String, String> idMap, List<String> reqColList, String absPath) throws IOException {

		try (Stream<String> lines = Files.lines(Paths.get(absPath + ID_MAPPING))) {
			System.out.println("-----------ID Mapping File----------- ");
			lines.filter(line -> line.contains(delimiter)).forEach(line -> idMap.putIfAbsent(line.split(delimiter)[0], line.split(delimiter)[1]));

		}
		// Read the columnMapping File
		System.out.println("Vendor File :");
		System.out.println("------------------");
		List<String> lines = Files.readAllLines(Paths.get(absPath + VENDOR_FILE));
		lines.stream().forEach(System.out::println);
		System.out.println(System.lineSeparator());

		List<String> newList = null;
		if (!reqColList.isEmpty()) {
			newList = reqColList.stream()
					.flatMap(line -> idMap.entrySet().stream().filter(e -> line.startsWith(e.getKey())).map(filteredEntry -> line.replace(filteredEntry.getKey(), filteredEntry.getValue())))
					.collect(Collectors.toList());
		} else {
			newList = lines.stream()
					.flatMap(line -> idMap.entrySet().stream().filter(e -> line.startsWith(e.getKey())).map(filteredEntry -> line.replace(filteredEntry.getKey(), filteredEntry.getValue())))
					.collect(Collectors.toList());
		}
		return newList;
	}
}
