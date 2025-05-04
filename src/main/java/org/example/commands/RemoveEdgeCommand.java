package org.example.commands;

import org.example.AccessGraph;
import picocli.CommandLine;

@CommandLine.Command(name = "rm", description = "Удаление связи")
public class RemoveEdgeCommand implements Runnable {
    @CommandLine.Option(names = {"-f", "--from"}, required = true, description = "Главная сущность")
    String from;
    @CommandLine.Option(names = {"-t", "--target"}, required = true, description = "Вспомогательная сущность")
    String to;

    @Override
    public void run() {
        AccessGraph graph = new AccessGraph();
        graph.removeEdge(from, to);
        System.out.printf("Связь между %s и %s удалена\n", from, to);
    }
}