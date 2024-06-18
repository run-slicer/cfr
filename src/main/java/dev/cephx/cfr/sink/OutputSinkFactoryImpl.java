package dev.cephx.cfr.sink;

import org.benf.cfr.reader.api.OutputSinkFactory;

import java.util.Collection;
import java.util.List;

public final class OutputSinkFactoryImpl implements OutputSinkFactory {
    private Throwable exception;
    private String output;

    @Override
    public List<SinkClass> getSupportedSinks(SinkType sinkType, Collection<SinkClass> collection) {
        return List.of(SinkClass.values());
    }

    @Override
    public <T> Sink<T> getSink(SinkType sinkType, SinkClass sinkClass) {
        return switch (sinkType) {
            case JAVA -> (s -> this.output = s.toString());
            case EXCEPTION -> (s -> this.exception = (Throwable) s);
            default -> (s -> {});
        };
    }

    public Throwable exception() {
        return this.exception;
    }

    public String output() {
        return this.output;
    }

    public String outputOrThrow() throws Throwable {
        if (this.exception != null) {
            throw this.exception;
        }

        return this.output;
    }
}
