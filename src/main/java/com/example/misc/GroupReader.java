package com.example.misc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GroupReader {
    public ArrayList<Group> readGroups() {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Parse JSON into a List of Pokemon objects
            ArrayList<Group> groups = objectMapper.readValue(
                new File("src\\main\\resources\\com\\example\\assets\\stocks\\group.json"),
                new TypeReference<ArrayList<Group>>() {}
            );

            return groups;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Return an empty list in case of an exception
        }
    }
}
