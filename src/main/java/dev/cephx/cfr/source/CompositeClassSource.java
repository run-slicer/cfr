package dev.cephx.cfr.source;

import org.benf.cfr.reader.api.ClassFileSource;
import org.benf.cfr.reader.bytecode.analysis.parse.utils.Pair;

import java.io.IOException;
import java.util.Collection;

public record CompositeClassSource(ClassFileSource... sources) implements ClassFileSource {
    @Override
    public void informAnalysisRelativePathDetail(String usePath, String classFilePath) {
        for (final ClassFileSource source : this.sources) {
            source.informAnalysisRelativePathDetail(usePath, classFilePath);
        }
    }

    @Override
    public Collection<String> addJar(String jarPath) {
        for (final ClassFileSource source : this.sources) {
            final Collection<String> res = source.addJar(jarPath);
            if (res != null) {
                return res;
            }
        }
        return null;
    }

    @Override
    public String getPossiblyRenamedPath(String path) {
        for (final ClassFileSource source : this.sources) {
            final String res = source.getPossiblyRenamedPath(path);
            if (res != null) {
                return res;
            }
        }
        return null;
    }

    @Override
    public Pair<byte[], String> getClassFileContent(String path) throws IOException {
        for (final ClassFileSource source : this.sources) {
            final Pair<byte[], String> res = source.getClassFileContent(path);
            if (res != null) {
                return res;
            }
        }
        return null;
    }
}
