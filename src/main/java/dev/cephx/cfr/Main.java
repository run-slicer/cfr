package dev.cephx.cfr;

import dev.cephx.cfr.impl.ClassFileSourceImpl;
import dev.cephx.cfr.impl.OutputSinkFactoryImpl;
import org.benf.cfr.reader.api.CfrDriver;
import org.teavm.jso.JSExport;
import org.teavm.jso.core.JSObjects;

import java.util.List;

public class Main {
    @JSExport
    public static String decompile(String name, Options options) throws Throwable {
        return decompile0(name, options == null || JSObjects.isUndefined(options) ? JSObjects.create() : options);
    }

    private static String decompile0(String name, Options options) throws Throwable {
        final var sinkFactory = new OutputSinkFactoryImpl();
        new CfrDriver.Builder()
                .withClassFileSource(new ClassFileSourceImpl(options.source()::get))
                .withOutputSink(sinkFactory)
                .withOptions(options.rawOptions())
                .build()
                .analyse(List.of(name));

        return sinkFactory.outputOrThrow();
    }
}
