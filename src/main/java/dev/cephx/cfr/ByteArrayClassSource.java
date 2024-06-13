package dev.cephx.cfr;

import org.benf.cfr.reader.api.ClassFileSource;
import org.benf.cfr.reader.bytecode.analysis.parse.utils.Pair;

import java.util.Collection;
import java.util.List;

public record ByteArrayClassSource(String name, byte[] data) implements ClassFileSource {
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
        if (this.name.equals(path.substring(0, path.indexOf(".class")))) {
            return new Pair<>(this.data, path);
        }

        return null;
    }
}
