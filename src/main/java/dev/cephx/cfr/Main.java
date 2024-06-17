package dev.cephx.cfr;

import dev.cephx.cfr.source.ByteArrayClassSource;
import dev.cephx.cfr.source.CompositeClassSource;
import dev.cephx.cfr.source.OutputSinkFactoryImpl;
import dev.cephx.cfr.source.VMClassSource;
import org.benf.cfr.reader.api.CfrDriver;
import org.teavm.jso.JSByRef;
import org.teavm.jso.JSExport;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Main {
    @JSExport
    public static String decompile(@JSByRef byte[] b, DecompilerOptions options) throws Throwable {
        final OutputSinkFactoryImpl sinkFactory = new OutputSinkFactoryImpl();

        final var source = new ByteArrayClassSource(b);
        new CfrDriver.Builder()
                .withClassFileSource(
                        new CompositeClassSource(
                                source,
                                new ByteArrayClassSource(options.getClasses()),
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
                .analyse(source.names());

        return sinkFactory.outputOrThrow();
    }
}
