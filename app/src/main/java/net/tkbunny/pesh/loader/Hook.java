package net.tkbunny.pesh.loader;

import java.util.ArrayList;

import org.graalvm.polyglot.*;
import org.graalvm.polyglot.HostAccess.Export;
import org.graalvm.polyglot.proxy.*;

public class Hook {
    @Export
    public final String name;
    @Export
    public final String group;
    private ArrayList<Value> functions;

    Hook(String group, String name) {
        this.name = name;
        this.group = group;
        this.functions = new ArrayList<Value>();
    }

    @Export
    public void hook(Value function) {
        functions.add(function);
    }

    @Export
    public void fire(Value arg) {
        for (int i = 0; i < functions.size(); i++) {
            try {
                functions.get(i).execute(arg);
            } catch(Exception e) {
                System.out.println("\u001b[31mGot exception " + e + " while running hook " + this.group + "." + this.name + "\u001b[0m");
            }
        }
    }
}
