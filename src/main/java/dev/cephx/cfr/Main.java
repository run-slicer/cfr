package dev.cephx.cfr;

import dev.cephx.cfr.source.ByteArrayClassSource;
import dev.cephx.cfr.source.CompositeClassSource;
import dev.cephx.cfr.source.OutputSinkFactoryImpl;
import dev.cephx.cfr.source.VMClassSource;
import org.benf.cfr.reader.api.CfrDriver;
import org.teavm.jso.JSByRef;
import org.teavm.jso.JSExport;

import java.util.List;

public class Main {
    @JSExport
    public static String decompile(@JSByRef byte[] b, DecompilerOptions options) throws Throwable {
        final OutputSinkFactoryImpl sinkFactory = new OutputSinkFactoryImpl();

        final var source = new ByteArrayClassSource(b);
        final var analyzerSource = new ByteArrayClassSource(options.getClasses());
        new CfrDriver.Builder()
                .withClassFileSource(
                        new CompositeClassSource(
                                source,
                                analyzerSource,
                                VMClassSource.INSTANCE
                        )
                )
                .withOutputSink(sinkFactory)
                .build()
                .analyse(List.of(source.single()));

        return sinkFactory.outputOrThrow();
    }
}
