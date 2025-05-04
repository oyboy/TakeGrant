package org.example.commands;

import org.example.GraphVisualizer;
import picocli.CommandLine;

@CommandLine.Command(name = "visualize", description = "Визуализация графа в отдельном окне")
public class VisualizeCommand implements Runnable {
    @Override
    public void run() {
        new GraphVisualizer().display();
    }
}