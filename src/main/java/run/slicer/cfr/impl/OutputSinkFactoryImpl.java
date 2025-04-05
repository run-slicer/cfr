package run.slicer.cfr.impl;

import org.benf.cfr.reader.api.OutputSinkFactory;
import org.benf.cfr.reader.api.SinkReturns;

import java.util.Collection;
import java.util.List;

public final class OutputSinkFactoryImpl implements OutputSinkFactory {
    private Throwable exception;
    private String output;

    @Override
    public List<SinkClass> getSupportedSinks(SinkType sinkType, Collection<SinkClass> collection) {
        return List.of(SinkClass.EXCEPTION_MESSAGE, SinkClass.STRING);
    }

    @Override
    public <T> Sink<T> getSink(SinkType sinkType, SinkClass sinkClass) {
        return switch (sinkType) {
            case JAVA -> (s -> this.output = s.toString());
            case EXCEPTION -> (s -> {
                this.exception = s instanceof SinkReturns.ExceptionMessage ex
                        ? ex.getThrownException()
                        : new Exception(s.toString());
            });
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
