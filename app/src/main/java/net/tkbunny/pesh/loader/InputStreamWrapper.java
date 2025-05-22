package net.tkbunny.pesh.loader;

import org.graalvm.polyglot.HostAccess.Export;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class InputStreamWrapper {
    public final InputStream stream;
    public final BufferedReader reader;

    public InputStreamWrapper(InputStream stream) {
        this.stream = stream;
        this.reader = new BufferedReader(new InputStreamReader(stream));
    }

    @Export
    public Byte readByte() throws IOException {
        int read = stream.read();

        if (read == -1) {
            return null;
        }

        return (byte) read;
    }

    @Export
    public Character readChar() throws IOException {
        int read = stream.read();

        if (read == -1) {
            return null;
        }

        return (char) read;
    }

    @Export
    public String readLine() throws IOException {
        return reader.readLine();
    }

    @Export
    public int available() throws IOException {
        return stream.available();
    }

    @Export
    public void transferTo(OutputStreamWrapper outputStream) throws IOException {
        stream.transferTo(outputStream.stream);
    }

    @Export
    public void close() throws IOException {
        stream.close();
    }
}
