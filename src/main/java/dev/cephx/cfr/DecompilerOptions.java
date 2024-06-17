package dev.cephx.cfr;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSByRef;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

public interface DecompilerOptions extends JSObject {
    @JSProperty
    @JSByRef
    byte[][] getClasses();

    @JSBody(script = "return Object.entries(this.options).map(e => { return { name: e[0], value: e[1] }; });")
    Option[] getOptions();

    interface Option extends JSObject {
        @JSProperty
        String getName();

        @JSProperty
        String getValue();
    }
}
