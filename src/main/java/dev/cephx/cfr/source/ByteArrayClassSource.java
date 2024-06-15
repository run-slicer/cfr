package dev.cephx.cfr.source;

import org.benf.cfr.reader.api.ClassFileSource;
import org.benf.cfr.reader.bytecode.analysis.parse.utils.Pair;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record ByteArrayClassSource(Map<String, byte[]> classes) implements ClassFileSource {
    public ByteArrayClassSource(byte[]... classes) {
        this(
                Arrays.stream(classes)
                        .map(b -> Map.entry(ClassFileUtil.getClassName(b), b))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
        );
    }

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

        final byte[] data = classes.get(name);
        return data != null ? new Pair<>(data, path) : null;
    }

    public String single() {
        if (this.classes.size() != 1) {
            throw new IllegalArgumentException("Expected a single class, got " + this.classes.size());
        }

        return this.classes.keySet().iterator().next();
    }
}
