package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AccessGraph {
    private static final String PATH = "./system/access.json";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public Map<String, Map<String, Set<String>>> read() {
        try {
            File file = new File(PATH);
            if (!file.exists()) return new HashMap<>();
            return objectMapper.readValue(file, new TypeReference<>() {});
        } catch (IOException e) {
            System.err.println("Ошибка чтения access.json: " + e.getMessage());
            return new HashMap<>();
        }
    }

    public void write(Map<String, Map<String, Set<String>>> graph) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(PATH), graph);
        } catch (IOException e) {
            System.err.println("Ошибка записи access.json: " + e.getMessage());
        }
    }
    public void writeFlow(Map<String, Set<String>> flow) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File("./system/flow.json"), flow);
        } catch (IOException e) {
            System.err.println("Ошибка записи flow.json: " + e.getMessage());
        }
    }


    public void addEdge(String from, String to, String right) {
        Map<String, Map<String, Set<String>>> graph = read();
        graph.putIfAbsent(from, new HashMap<>());
        graph.get(from).putIfAbsent(to, new HashSet<>());
        graph.get(from).get(to).add(right);
        write(graph);
    }

    public void removeEdge(String from, String to) {
        Map<String, Map<String, Set<String>>> graph = read();
        if (graph.containsKey(from)) {
            graph.get(from).remove(to);
            if (graph.get(from).isEmpty()) {
                graph.remove(from);
            }
            write(graph);
        }
    }

    public Set<String> getRights(String from, String to) {
        return read().getOrDefault(from, new HashMap<>()).getOrDefault(to, new HashSet<>());
    }
}
