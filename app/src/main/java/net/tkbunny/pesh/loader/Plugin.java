package net.tkbunny.pesh.loader;

import net.tkbunny.pesh.loader.Hook;
import net.tkbunny.pesh.loader.Process;

import java.util.ArrayList;

import org.graalvm.polyglot.*;
import org.graalvm.polyglot.Context.Builder;
import org.graalvm.polyglot.HostAccess.Export;
import org.graalvm.polyglot.io.IOAccess;
import org.graalvm.polyglot.proxy.*;

public class Plugin {
    private Context context;
    private static ArrayList<Plugin> plugins = new ArrayList<Plugin>();
    @Export
    public final String name;
    public ArrayList<Hook> hooks;

    @Export
    public Hook createHook(String hookName) {
        Hook newHook = new Hook(this.name, hookName);
        hooks.add(newHook);
        return newHook;
    }

    @Export
    public void hook(String hookName, Value function) {
        String[] split = hookName.split(".");
        if (split.length < 2) {
            throw new RuntimeException("Hook names must be in the format plugin.name");
        }

        for (int i = 0; i < plugins.size(); i++) {
            Plugin pluginToTest = plugins.get(i);

            if (pluginToTest.name == split[0]) {
                for (int j = 0; j < plugins.get(i).hooks.size(); j++) {
                    Hook hookToTest = plugins.get(i).hooks.get(j);

                    if (hookToTest.name == split[1]) {
                        hookToTest.hook(function);
                    }
                }
            }
        }
    }

    @Export
    public Process startProcess(String... command) throws java.io.IOException {
        return new Process(command);
    }

    @Export
    public void stopShell() {
        net.tkbunny.pesh.loader.App.running = false;
    }

    @Export
    public InputStreamWrapper getStdIn() {
        return new InputStreamWrapper(System.in);
    }

    @Export
    public OutputStreamWrapper getStdOut() {
        return new OutputStreamWrapper(System.out);
    }

    Plugin(String code, String name) {
        Builder builder = Context.newBuilder("js");
        builder.allowCreateProcess(true);
        builder.allowCreateThread(true);
        builder.allowEnvironmentAccess(EnvironmentAccess.NONE);
        builder.allowExperimentalOptions(false);
        builder.allowHostAccess(HostAccess.EXPLICIT);
        builder.allowHostClassLoading(false);
        builder.allowInnerContextOptions(true);
        builder.allowIO(IOAccess.NONE);
        builder.allowNativeAccess(false);
        builder.allowValueSharing(true);

        this.context = builder.build();
        this.name = name;
        this.hooks = new ArrayList<Hook>();

        Value bindings = this.context.getBindings("js");
        bindings.putMember("plugin", this);

        try {
            context.eval("js", code);
        } catch (Exception e) {
            System.out.println("\u001b[31mGot exception " + e + " while running plugin " + this.name + "\u001b[0m");
        }
    }
}
