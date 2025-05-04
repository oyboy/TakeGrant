package org.example.commands;

import org.example.AccessGraph;
import picocli.CommandLine;

import java.util.*;

@CommandLine.Command(name = "closure", description = "Замыкание графа доступа (take/grant)")
public class ClosureCommand implements Runnable {
    private final AccessGraph accessGraph = new AccessGraph();

    @Override
    public void run() {
        Map<String, Map<String, Set<String>>> graph = accessGraph.read();

        System.out.println("=== Замыкание прав доступа (Access Closure) ===");
        Map<String, Map<String, Set<String>>> closure = buildClosure(graph);
        for (var from : closure.keySet()) {
            for (var to : closure.get(from).keySet()) {
                Set<String> rights = closure.get(from).get(to);
                System.out.printf("%s -> %s : %s%n", from, to, rights);
            }
        }
        accessGraph.write(closure);

        System.out.println("=== Замыкание информационных потоков (Flow Closure) ===");
        Map<String, Set<String>> flow = buildFlowClosure(graph);
        for (var entry : flow.entrySet()) {
            System.out.printf("%s -> %s%n", entry.getKey(), entry.getValue());
        }
        accessGraph.writeFlow(flow);
    }

    private Map<String, Map<String, Set<String>>> buildClosure(Map<String, Map<String, Set<String>>> graph) {
        Map<String, Map<String, Set<String>>> closure = deepCopy(graph);
        boolean changed;
        do {
            changed = false;
            List<String> fromList = new ArrayList<>(closure.keySet());

            for (String from : fromList) {
                List<String> viaList = new ArrayList<>(closure.getOrDefault(from, Map.of()).keySet());

                for (String via : viaList) {
                    Set<String> viaRights = closure.get(from).get(via);
                    if (viaRights.contains("t")) {
                        List<String> targetList = new ArrayList<>(closure.getOrDefault(via, Map.of()).keySet());
                        for (String target : targetList) {
                            for (String right : closure.get(via).get(target)) {
                                changed |= addRight(closure, from, target, right);
                            }
                        }
                    }

                    if (viaRights.contains("g")) {
                        List<String> targetList = new ArrayList<>(closure.keySet());
                        for (String target : targetList) {
                            Set<String> rightsFromS = closure.getOrDefault(from, Map.of()).getOrDefault(target, Set.of());
                            for (String right : rightsFromS) {
                                changed |= addRight(closure, via, target, right);
                            }
                        }
                    }
                }
            }
        } while (changed);
        return closure;
    }
    private Map<String, Set<String>> buildFlowClosure(Map<String, Map<String, Set<String>>> graph) {
        Map<String, Set<String>> flow = new HashMap<>();

        for (String from : graph.keySet()) {
            Set<String> reachable = new HashSet<>();
            Deque<String> stack = new ArrayDeque<>();
            stack.push(from);

            while (!stack.isEmpty()) {
                String current = stack.pop();
                for (Map.Entry<String, Set<String>> entry : graph.getOrDefault(current, Map.of()).entrySet()) {
                    String neighbor = entry.getKey();
                    Set<String> rights = entry.getValue();

                    if ((rights.contains("t") || rights.contains("g")) && !reachable.contains(neighbor)) {
                        reachable.add(neighbor);
                        stack.push(neighbor);
                    }
                }
            }
            flow.put(from, reachable);
        }
        return flow;
    }

    private boolean addRight(Map<String, Map<String, Set<String>>> graph, String from, String to, String right) {
        graph.putIfAbsent(from, new HashMap<>());
        graph.get(from).putIfAbsent(to, new HashSet<>());
        return graph.get(from).get(to).add(right);
    }

    private Map<String, Map<String, Set<String>>> deepCopy(Map<String, Map<String, Set<String>>> original) {
        Map<String, Map<String, Set<String>>> copy = new HashMap<>();
        for (var entry : original.entrySet()) {
            Map<String, Set<String>> inner = new HashMap<>();
            for (var innerEntry : entry.getValue().entrySet()) {
                inner.put(innerEntry.getKey(), new HashSet<>(innerEntry.getValue()));
            }
            copy.put(entry.getKey(), inner);
        }
        return copy;
    }
}