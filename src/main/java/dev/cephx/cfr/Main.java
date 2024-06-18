package dev.cephx.cfr;

import org.benf.cfr.reader.api.CfrDriver;
import org.teavm.jso.JSExport;
import org.teavm.jso.core.JSObjects;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    @JSExport
    public static String decompile(String name, DecompilerOptions options) throws Throwable {
        return decompile0(name, options == null || JSObjects.isUndefined(options) ? JSObjects.create() : options);
    }

    private static String decompile0(String name, DecompilerOptions options) throws Throwable {
        final var sinkFactory = new OutputSinkFactoryImpl();
        new CfrDriver.Builder()
                .withClassFileSource(new ClassSourceImpl(options::source))
                .withOutputSink(sinkFactory)
                .withOptions(
                        Arrays.stream(options.getOptions())
                                .collect(Collectors.toMap(DecompilerOptions.Option::getName, DecompilerOptions.Option::getValue))
                )
                .build()
                .analyse(List.of(name));

        return sinkFactory.outputOrThrow();
    }
}
