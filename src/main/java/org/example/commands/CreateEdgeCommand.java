package org.example.commands;

import org.example.AccessGraph;
import picocli.CommandLine;

@CommandLine.Command(name = "cr", description = "Создание сущности/связи с правами")
public class CreateEdgeCommand implements Runnable {
    @CommandLine.Option(names = {"-r", "--rule"}, required = true, description = "Правило")
    String rule;
    @CommandLine.Option(names = {"-f", "--from"}, required = true, description = "Главная сущность")
    String from;
    @CommandLine.Option(names = {"-t", "--target"}, required = true, description = "Вспомогательная сущность")
    String to;
    @CommandLine.Option(names = {"-e", "--entity"}, required = true, description = "Тип сущности")
    String type;

    @Override
    public void run() {
        AccessGraph graph = new AccessGraph();
        graph.addEdge(from, to, rule);
        System.out.printf("Добавлено правило '%s' от %s к %s (%s)\n", rule, from, to, type);
    }
}