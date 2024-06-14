package dev.cephx.cfr.source;

import org.benf.cfr.reader.api.ClassFileSource;
import org.benf.cfr.reader.bytecode.analysis.parse.utils.Pair;

import java.util.Collection;
import java.util.List;

public final class VMClassSource implements ClassFileSource {
    public static final ClassFileSource INSTANCE = new VMClassSource();

    private VMClassSource() {
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

        return switch (name) {
            case "java/lang/Object" -> new Pair<>(VMConstants.JAVA_LANG_OBJECT, path);
            case "java/lang/String" -> new Pair<>(VMConstants.JAVA_LANG_STRING, path);
            case "java/lang/System" -> new Pair<>(VMConstants.JAVA_LANG_SYSTEM, path);
            default -> null;
        };
    }
}
