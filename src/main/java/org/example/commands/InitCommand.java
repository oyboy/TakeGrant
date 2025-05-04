package org.example.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

@CommandLine.Command(name="init", description = "Инициализация директорий")
public class InitCommand implements Runnable {
    @Override
    public void run() {
        try {
            File dir = new File("./system");
            if (!dir.exists()) dir.mkdir();

            File accessFile = new File("./system/access.json");
            File flowFile = new File("./system/flow.json");

            ObjectMapper mapper = new ObjectMapper();
            if (accessFile.createNewFile()) {
                mapper.writeValue(accessFile, new HashMap<>());
            }
            if (flowFile.createNewFile()) {
                mapper.writeValue(flowFile, new HashMap<>());
            }

            System.out.println("Инициализация завершена.");

        } catch (IOException io) {
            System.err.println("Не удалось создать файлы: " + io.getMessage());
        }
    }
}