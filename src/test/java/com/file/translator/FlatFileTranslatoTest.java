package com.file.translator;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FlatFileTranslatoTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testReaFileColumnHeaders() throws FileNotFoundException, IOException {

		List<String> columnHeaderList = new ArrayList<>();
		columnHeaderList.add("COL0");
		columnHeaderList.add("COL1");
		columnHeaderList.add("COL2");
		columnHeaderList.add("COL3");
		String currentDir = Paths.get(".").toAbsolutePath().toString();
		String absPath = currentDir.substring(0, currentDir.length() - 1).replace("\\", "\\\\");
		List<String> colHedears = FlatFileTranslator.reaFileColumnHeaders(absPath);
		assertEquals(colHedears, columnHeaderList);
	}

	@Test
	public void testRowMapping() throws IOException {

		String delimiter = "\t";
		Map<String, String> idMap = new HashMap<>();
		List<String> expectedRowList = Arrays.asList("OURID1	VAL11	VAL13", "OURID2	VAL21 	VAL23", "OURID4	VAL41 	VAL43");
		List<String> reqColList = Arrays.asList("COL0	COL1	COL3", "ID1	VAL11	VAL13", "ID2	VAL21 	VAL23", "ID3	VAL31 	VAL33", "ID4	VAL41 	VAL43");
		String currentDir = Paths.get(".").toAbsolutePath().toString();
		String absPath = currentDir.substring(0, currentDir.length() - 1).replace("\\", "\\\\");
		List<String> rows = FlatFileTranslator.rowMapping(delimiter, idMap, reqColList, absPath);
		assertEquals(expectedRowList, rows);

	}
}
