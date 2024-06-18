package dev.cephx.cfr;

import org.benf.cfr.reader.api.ClassFileSource;
import org.benf.cfr.reader.bytecode.analysis.parse.utils.Pair;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

record ClassSourceImpl(Function<String, byte[]> source) implements ClassFileSource {
    @Override
    public void informAnalysisRelativePathDetail(String usePath, String classFilePath) {
    }

    @Override
    public Collection<String> addJar(String jarPath) {
        return List.of();
    }

    @Override
    public String getPossiblyRenamedPath(String path) {
        return path;
    }

    @Override
    public Pair<byte[], String> getClassFileContent(String path) {
        String name = path;

        final int extIndex = path.indexOf(".class");
        if (extIndex != -1) {
            name = path.substring(0, extIndex);
        }

        final byte[] b = this.source.apply(name);
        return b != null ? new Pair<>(b, path) : null;
    }
}
