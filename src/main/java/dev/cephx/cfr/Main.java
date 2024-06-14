package dev.cephx.cfr;

import dev.cephx.cfr.bind.DecompilerOptions;
import dev.cephx.cfr.source.ByteArrayClassSource;
import dev.cephx.cfr.source.CompositeClassSource;
import dev.cephx.cfr.source.OutputSinkFactoryImpl;
import dev.cephx.cfr.source.VMClassSource;
import org.benf.cfr.reader.api.CfrDriver;
import org.benf.cfr.reader.api.ClassFileSource;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

interface Exported extends JSObject {
    String decompile(String name, DecompilerOptions options) throws Throwable;
}

public class Main {
    public static void main(String[] args) {
        exportAPI(new Exported() {
            @Override
            public String decompile(String name, DecompilerOptions options) throws Throwable {
                final OutputSinkFactoryImpl sinkFactory = new OutputSinkFactoryImpl();
                final ClassFileSource source = new ByteArrayClassSource(
                        Arrays.stream(options.getClasses())
                                .map(e -> Map.entry(toInternalName(e.getName()), e.getData()))
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                );

                new CfrDriver.Builder()
                        .withClassFileSource(new CompositeClassSource(source, VMClassSource.INSTANCE))
                        .withOutputSink(sinkFactory)
                        .build()
                        .analyse(List.of(toInternalName(name)));

                return sinkFactory.outputOrThrow();
            }
        });
    }

    private static String toInternalName(String name) {
        return name.replace('.', '/');
    }

    @JSBody(params = "o", script = "$rt_exports.api = o;")
    private static native void exportAPI(Exported o);
}
