package net.tkbunny.pesh.loader;

import java.lang.ProcessBuilder;

import org.graalvm.polyglot.HostAccess.Export;

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

    public InputStreamWrapper getStdOut(String string) {
        return new InputStreamWrapper(this.process.getInputStream());
    }

    // TODO: Input/Output Stream Wrapper Classes
}
