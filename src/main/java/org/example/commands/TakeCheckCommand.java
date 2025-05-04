package org.example.commands;

import org.example.AccessGraph;
import picocli.CommandLine;

@CommandLine.Command(name = "tk", description = "Проверка возможности получения права (take)")
public class TakeCheckCommand implements Runnable {
    @CommandLine.Option(names = {"-r", "--rule"}, required = true, description = "Правило")
    String rule;
    @CommandLine.Option(names = {"-f", "--from"}, required = true, description = "Главная сущность")
    String from;
    @CommandLine.Option(names = {"-t", "--target"}, required = true, description = "Вспомогательная сущность")
    String to;
    @CommandLine.Option(names = {"-v", "--via"}, required = true, description = "Промежуточная сущность")
    String via;

    @Override
    public void run() {
        AccessGraph graph = new AccessGraph();
        boolean hasTake = graph.getRights(from, via).contains("t");
        boolean targetHasRight = graph.getRights(via, to).contains(rule);
        boolean result = hasTake && targetHasRight;
        System.out.println(result ? "True" : "False");
    }
}