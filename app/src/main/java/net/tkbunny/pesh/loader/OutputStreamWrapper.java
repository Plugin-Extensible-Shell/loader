package net.tkbunny.pesh.loader;

import org.graalvm.polyglot.HostAccess.Export;

import java.io.IOException;
import java.io.OutputStream;

public class OutputStreamWrapper {
    public final OutputStream stream;

    public OutputStreamWrapper(OutputStream stream) {
        this.stream = stream;
    }

    @Export
    public void writeByte(byte b) throws IOException {
        stream.write(b);
    }

    @Export
    public void writeChar(char c) throws IOException {
        stream.write((int) c);
    }

    @Export
    public void close() throws IOException {
        stream.close();
    }
}
