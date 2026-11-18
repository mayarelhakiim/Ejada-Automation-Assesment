package com.ejada.automation.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JsonDataReader {

    private static final String DATA_FILE_PATH = "src/test/resources/testdata/invalid_credentials.json";
    private static final JsonNode testDataArray;

    static {
        ObjectMapper mapper = new ObjectMapper();
        try {
            testDataArray = mapper.readTree(new File(DATA_FILE_PATH));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read test data file: " + DATA_FILE_PATH, e);
        }
    }

    public static String getUsername(int index) {
        return getNode(index).get("username").asText();
    }

    public static String getPassword(int index) {
        return getNode(index).get("password").asText();
    }

    public static String getExpectedError(int index) {
        return getNode(index).get("expectedError").asText();
    }

    private static JsonNode getNode(int index) {
        if (index < 0 || index >= testDataArray.size()) {
            throw new IndexOutOfBoundsException("Invalid dataset index: " + index);
        }
        return testDataArray.get(index);
    }
}