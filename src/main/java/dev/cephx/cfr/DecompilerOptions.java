package dev.cephx.cfr;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSByRef;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;

public interface DecompilerOptions extends JSObject {
    @JSBody(script = "return this.options ? Object.entries(this.options) : [];")
    Option[] getOptions();

    // can't use the logical OR, it breaks the TeaVM minifier
    @JSBody(script = "return this.source ? this.source : (() => { return null; });")
    Source getSource();

    interface Option extends JSObject {
        @JSBody(script = "return this[0];")
        String getName();

        @JSBody(script = "return this[1];")
        String getValue();
    }

    @JSFunctor
    interface Source extends JSObject {
        @JSByRef
        byte[] get(String name);
    }
}
