package dev.cephx.cfr;

import org.benf.cfr.reader.api.CfrDriver;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;

import java.util.List;

interface Exported extends JSObject {
    String decompile(String name, byte[] b) throws Throwable;
}

public class Main {
    public static void main(String[] args) {
        exportAPI(new Exported() {
            @Override
            public String decompile(String name, byte[] b) throws Throwable {
                final OutputSinkFactoryImpl sinkFactory = new OutputSinkFactoryImpl();
                new CfrDriver.Builder()
                        .withClassFileSource(new ByteArrayClassSource(name, b))
                        .withOutputSink(sinkFactory)
                        .build()
                        .analyse(List.of(name));

                return sinkFactory.outputOrThrow();
            }
        });
    }

    @JSBody(params = "o", script = "$rt_exports.api = o;")
    private static native void exportAPI(Exported o);
}
