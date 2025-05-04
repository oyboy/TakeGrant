package org.example.commands;

import org.example.AccessGraph;
import picocli.CommandLine;

@CommandLine.Command(name = "gr", description = "Проверка разрешения на передачу права (grant)")
public class GrantCheckCommand implements Runnable {
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
        boolean hasGrant = graph.getRights(from, via).contains("g");
        boolean subjectHasRight = graph.getRights(from, to).contains(rule);
        boolean result = hasGrant && subjectHasRight;
        System.out.println(result ? "True" : "False");
    }
}