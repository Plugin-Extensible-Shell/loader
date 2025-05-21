package net.tkbunny.pesh.loader;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

public class App {
    private static final String configDirName = "pesh";
    private static final String OS = System.getProperty("os.name");
    private static final String home = System.getProperty("user.home");

    public static Path getConfigDirectory() {
        if (OS.startsWith("Windows")) {
            return Paths.get(home, configDirName);
        } else {
            return Paths.get(home, ".config", configDirName);
        }
    }

    public static void main(String[] args) {
        // TODO: Load all the plugins
        // TODO: Create an internal plugin for handling keypresses and signals
    }
}
