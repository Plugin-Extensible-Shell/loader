package net.tkbunny.pesh.loader;

import net.tkbunny.pesh.loader.Hook;

import java.util.ArrayList;

import org.graalvm.polyglot.*;
import org.graalvm.polyglot.Context.Builder;
import org.graalvm.polyglot.HostAccess.Export;
import org.graalvm.polyglot.io.IOAccess;
import org.graalvm.polyglot.proxy.*;

public class Plugin {
    private Context context;
    @Export
    public final String name;
    public ArrayList<Hook> hooks;

    @Export
    public Hook createHook(String hookName) {
        Hook newHook = new Hook(this.name, hookName);
        hooks.add(newHook);
        return newHook;
    }

    Plugin(String code, String name) {
        Builder builder = Context.newBuilder("js");
        builder.allowCreateProcess(true);
        builder.allowCreateThread(false);
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
