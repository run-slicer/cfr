package run.slicer.cfr;

import org.teavm.jso.JSExceptions;
import run.slicer.cfr.impl.ClassFileSourceImpl;
import run.slicer.cfr.impl.OutputSinkFactoryImpl;
import org.benf.cfr.reader.api.CfrDriver;
import org.teavm.interop.Async;
import org.teavm.interop.AsyncCallback;
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
        return new JSPromise<>((resolve, reject) -> {
            new Thread(() -> {
                try {
                    final var sinkFactory = new OutputSinkFactoryImpl();
                    new CfrDriver.Builder()
                            .withClassFileSource(new ClassFileSourceImpl(name0 -> source0(options, name0)))
                            .withOutputSink(sinkFactory)
                            .withOptions(options.rawOptions())
                            .build()
                            .analyse(List.of(name));

                    resolve.accept(JSString.valueOf(sinkFactory.outputOrThrow()));
                } catch (Throwable e) {
                    reject.accept(JSExceptions.getJSException(e));
                }
            }).start();
        });
    }

    @Async
    private static native byte[] source0(Options options, String name);

    private static void source0(Options options, String name, AsyncCallback<byte[]> callback) {
        options.source(name)
                .then(b -> {
                    callback.complete(b == null || JSObjects.isUndefined(b) ? null : unwrapByteArray(b));
                    return null;
                })
                .catchError(err -> {
                    callback.error(new Exception(err.toString()));
                    return null;
                });
    }

    @JSBody(params = {"data"}, script = "return data;")
    private static native @JSByRef byte[] unwrapByteArray(Uint8Array data);
}
