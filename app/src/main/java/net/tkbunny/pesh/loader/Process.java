package net.tkbunny.pesh.loader;

import net.tkbunny.pesh.loader.Hook;

import java.util.ArrayList;

import java.lang.ProcessBuilder;

import org.graalvm.polyglot.*;
import org.graalvm.polyglot.HostAccess.Export;
import org.graalvm.polyglot.proxy.*;

public class Process {
    @Export
    public final String[] command;
    private final java.lang.Process process;

    Process(String... process) throws java.io.IOException {
        this.command = process;

        ProcessBuilder builder = new ProcessBuilder(process);
        this.process = builder.start();
    }

    @Export
    public void writeToStdIn(String string)  {
        try {
            this.process.getOutputStream().write(string.getBytes());
        } catch(java.io.IOException e) {}
    }
}
