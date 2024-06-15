package dev.cephx.cfr;

import org.teavm.jso.JSByRef;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

public interface DecompilerOptions extends JSObject {
    @JSProperty
    @JSByRef
    byte[][] getClasses();
}
