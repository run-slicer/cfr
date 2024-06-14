package dev.cephx.cfr.bind;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;

public interface DecompilerOptions extends JSObject {
    // this is way longer than it needs to be, because TeaVM's script analyzer is dumb
    @JSBody(script = "return Object.entries(this.classes).map(e => { return { name: e[0], data: e[1] }; });")
    ClasspathEntry[] getClasses();
}
