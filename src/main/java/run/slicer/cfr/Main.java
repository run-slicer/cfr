package run.slicer.cfr;

import run.slicer.cfr.impl.ClassFileSourceImpl;
import run.slicer.cfr.impl.OutputSinkFactoryImpl;
import org.benf.cfr.reader.api.CfrDriver;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSByRef;
import org.teavm.jso.JSExport;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.core.JSPromise;
import org.teavm.jso.core.JSString;
import org.teavm.jso.typedarrays.Uint8Array;

import java.util.List;

public class Main {
    @JSExport
    public static JSPromise<JSString> decompile(String name, Options options) {
        return decompile0(name, options == null || JSObjects.isUndefined(options) ? JSObjects.create() : options);
    }

    private static JSPromise<JSString> decompile0(String name, Options options) {
        return JSPromise.callAsync(() -> {
            final var sinkFactory = new OutputSinkFactoryImpl();
            new CfrDriver.Builder()
                    .withClassFileSource(new ClassFileSourceImpl(name0 -> source0(options, name0)))
                    .withOutputSink(sinkFactory)
                    .withOptions(options.rawOptions())
                    .build()
                    .analyse(List.of(name));

            return JSString.valueOf(sinkFactory.outputOrThrow());
        });
    }

    private static byte[] source0(Options options, String name) {
        final Uint8Array b = options.source(name).await();
        return b == null || JSObjects.isUndefined(b) ? null : unwrapByteArray(b);
    }

    @JSBody(params = {"data"}, script = "return data;")
    private static native @JSByRef(optional = true) byte[] unwrapByteArray(Uint8Array data);
}
