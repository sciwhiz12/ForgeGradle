package net.minecraftforge.gradle.mcp.mapping;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;

public class MappingInfo implements IMappingInfo {
    private final String channel;
    private final String version;
    private final Collection<IDocumentedNode> classes;
    private final Collection<IDocumentedNode> fields;
    private final Collection<IDocumentedNode> methods;
    private final Collection<INode> params;

    public MappingInfo(String channel, String version,
                       Collection<IDocumentedNode> classes, Collection<IDocumentedNode> fields,
                       Collection<IDocumentedNode> methods, Collection<INode> params) {
        this.channel = channel;
        this.version = version;
        this.classes = classes;
        this.fields = fields;
        this.methods = methods;
        this.params = params;
    }

    @Override
    public String getChannel() {
        return channel;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public Collection<IDocumentedNode> getClasses() {
        return classes;
    }

    @Override
    public Collection<IDocumentedNode> getFields() {
        return fields;
    }

    @Override
    public Collection<IDocumentedNode> getMethods() {
        return methods;
    }

    @Override
    public Collection<INode> getParameters() {
        return params;
    }

    public static class Node implements IDocumentedNode {
        private final String original;
        private final String mapped;
        private final Map<String, String> meta;
        @Nullable
        private final String javadoc;

        public Node(String original, String mapped, Map<String, String> meta, @Nullable String javadoc) {
            this.original = original;
            this.mapped = mapped;
            this.meta = meta;
            this.javadoc = javadoc;
        }

        @Override
        public String getOriginal() {
            return original;
        }

        @Override
        public String getMapped() {
            return mapped;
        }

        @Nullable
        @Override
        public String getMeta(String name) {
            return meta.get(name);
        }

        @Nullable
        @Override
        public String getJavadoc() {
            return javadoc;
        }
    }
}
