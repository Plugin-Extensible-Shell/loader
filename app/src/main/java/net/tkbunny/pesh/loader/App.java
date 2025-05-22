package net.tkbunny.pesh.loader;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.lang.StringBuilder;
import java.util.Objects;

public class App {
    private static final String configDirName = "pesh";
    private static final String OS = System.getProperty("os.name");
    private static final String home = System.getProperty("user.home");
    public static boolean running = true;

    public static Path getConfigDirectory() {
        if (OS.startsWith("Windows")) {
            return Paths.get(home, configDirName);
        } else {
            return Paths.get(home, ".config", configDirName);
        }
    }

    public static Path getPluginDirectory() {
        String configDir = getConfigDirectory().toString();
        return Paths.get(configDir, "plugins");
    }

    public static String readFile(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder builder = new StringBuilder();

            String line = reader.readLine();
            String ls = System.lineSeparator();
            while (line != null) {
                builder.append(line);
                builder.append(ls);

                line = reader.readLine();
            }

            return builder.toString();
        } catch(IOException e) {
            System.out.println("\u001b[31mFailed to read plugin " + file.getName() + ": " + e.getMessage() + "\u001b[0m");
            return "";
        }
    }

    public static void main(String[] args) {
        File pluginDir = new File(getPluginDirectory().toString());
        pluginDir.mkdirs();

        try {
            //noinspection DataFlowIssue
            for (File plugin : pluginDir.listFiles()) {
                new Plugin(readFile(plugin), plugin.getName().replaceAll("\\.js$", ""));
            }
        } catch(Exception e) {
            System.out.println("\u001b[31mFailed to read plugins: " + e.getMessage() + "\u001b[0m");
        }

        while (running) {
            //
        }
        // TODO: Create an internal plugin for keypresses and signals
    }
}
