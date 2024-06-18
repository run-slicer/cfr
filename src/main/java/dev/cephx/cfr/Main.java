package dev.cephx.cfr;

import dev.cephx.cfr.source.ByteArrayClassSource;
import dev.cephx.cfr.source.CompositeClassSource;
import dev.cephx.cfr.sink.OutputSinkFactoryImpl;
import dev.cephx.cfr.source.SimpleClassSource;
import dev.cephx.cfr.source.VMClassSource;
import org.benf.cfr.reader.api.CfrDriver;
import org.teavm.jso.JSByRef;
import org.teavm.jso.JSExport;
import org.teavm.jso.core.JSObjects;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    @JSExport
    public static String decompile(@JSByRef byte[] b, DecompilerOptions options) throws Throwable {
        if (options == null || JSObjects.isUndefined(options)) {
            options = JSObjects.create();
        }

        final var sinkFactory = new OutputSinkFactoryImpl();
        final var source = new ByteArrayClassSource(b);
        new CfrDriver.Builder()
                .withClassFileSource(
                        new CompositeClassSource(
                                source,
                                new SimpleClassSource(options.getSource()::get),
                                VMClassSource.INSTANCE
                        )
                )
                .withOutputSink(sinkFactory)
                .withOptions(
                        Arrays.stream(options.getOptions())
                                .collect(Collectors.toMap(
                                        DecompilerOptions.Option::getName,
                                        DecompilerOptions.Option::getValue
                                ))
                )
                .build()
                .analyse(List.of(source.name()));

        return sinkFactory.outputOrThrow();
    }
}
