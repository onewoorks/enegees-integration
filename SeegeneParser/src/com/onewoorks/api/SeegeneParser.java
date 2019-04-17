/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onewoorks.api;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author irwanibrahim
 */
public class SeegeneParser {

    /**
     * @param args the command line arguments
     */
    private static final String SAMPLE_CSV_FILE_PATH = "D:\\ExternalCode\\Onewoorks\\seegene\\samples\\SeegeneCSV.csv";
    private static String RESULT = "";

    public static void main(String[] args) throws IOException {

        try (
                //                Reader reader = Files.newBufferedReader(Paths.get(SAMPLE_CSV_FILE_PATH));
                Reader reader = Files.newBufferedReader(Paths.get(args[0]));
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);) {

            JSONObject obj = new JSONObject();
            List listExam = new LinkedList();
            Map result = new LinkedHashMap();
            List ll = new LinkedList();
            for (CSVRecord csvRecord : csvParser) {
                if (csvRecord.getRecordNumber() > 1) {
                    if (csvRecord.get(0).length() > 0) {
                        obj = new JSONObject();
                        result = new LinkedHashMap();
                        obj.put("sample_no", csvRecord.get(0));
                        obj.put("patient_id", csvRecord.get(1));
                        obj.put("name", csvRecord.get(2));
                        obj.put("well", csvRecord.get(3));
                        obj.put("auto_interpretation", csvRecord.get(6));
                        obj.put("remarks", csvRecord.get(7));
                        obj.put("test_kit", csvRecord.get(8));

                        result.put("indication", csvRecord.get(4));
                        result.put("target_name", "HPV" + csvRecord.get(4));
                        result.put("value", csvRecord.get(5));
                        ll.add(result);
                    }
                    if (csvRecord.get(0).length() == 0) {
                        Map m = new HashMap();
                        m.put("indication", csvRecord.get(4));
                        m.put("target_name", "HPV" + csvRecord.get(4));
                        m.put("value", csvRecord.get(5));
                        ll.add(m);
                    }

                    if (csvRecord.get(0).length() == 0 && csvRecord.get(4).equalsIgnoreCase("IC")) {
                        obj.put("target", ll);
                        listExam.add(obj);
                        String jsonString = JSONValue.toJSONString(listExam);
                        RESULT = jsonString;
                        ll = new LinkedList();
                    }
                }
            }
            Output();
        }
    }

    protected static String Output() {
        System.out.println(RESULT);
        return RESULT;
    }

}
