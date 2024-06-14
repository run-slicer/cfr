package dev.cephx.cfr.bind;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

public interface ClasspathEntry extends JSObject {
    @JSProperty
    String getName();

    @JSProperty
    byte[] getData();
}
