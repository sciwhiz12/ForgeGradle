package net.minecraftforge.gradle.mcp.mapping;

import javax.annotation.Nullable;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.function.Function;
import java.util.zip.ZipOutputStream;

import com.google.common.base.Preconditions;
import de.siegmar.fastcsv.writer.CsvWriter;
import de.siegmar.fastcsv.writer.LineDelimiter;
import de.siegmar.fastcsv.writer.QuoteStrategy;
import net.minecraftforge.gradle.common.util.Utils;

public interface IMappingInfo {
    String getChannel();

    String getVersion();

    Collection<IDocumentedNode> getClasses();

    Collection<IDocumentedNode> getFields();

    Collection<IDocumentedNode> getMethods();

    Collection<INode> getParameters();

    interface INode {
        String getOriginal();

        String getMapped();

        @Nullable
        String getMeta(String name);
    }

    interface IDocumentedNode extends INode {
        @Nullable
        String getJavadoc();
    }

    // Excepts there is a meta field of 'side' with a number
    static void writeMCPZip(File outputZip, IMappingInfo mappings) throws IOException {
        Preconditions.checkArgument(outputZip.isFile(), "Output zip must be a file");
        if (outputZip.exists() && !outputZip.delete()) {
            throw new IOException("Could not delete existing file " + outputZip);
        }

        Function<? super Writer, CsvWriter> createCsvWriter = (writer) ->
                CsvWriter.builder()
                        .quoteStrategy(QuoteStrategy.ALWAYS)
                        .lineDelimiter(LineDelimiter.LF)
                        .build(new FilterWriter(writer) {
                            @Override
                            public void close() throws IOException {
                                // Prevent flushing
                                super.flush();
                            }
                        });

        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(outputZip));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(zipOut))) {

            // TODO: deduplicate code

            // Classes
            Collection<IDocumentedNode> classes = mappings.getClasses();
            if (!classes.isEmpty()) {
                zipOut.putNextEntry(Utils.getStableEntry("classes.csv"));
                try (CsvWriter csv = createCsvWriter.apply(writer)) {
                    csv.writeRow("searge", "name", "side", "desc");
                    for (IDocumentedNode classNode : classes) {
                        csv.writeRow(classNode.getOriginal(), classNode.getMapped(),
                                classNode.getMeta("side"), classNode.getJavadoc());
                    }
                }
                zipOut.closeEntry();
            }

            // Methods
            Collection<IDocumentedNode> methods = mappings.getMethods();
            if (!methods.isEmpty()) {
                zipOut.putNextEntry(Utils.getStableEntry("methods.csv"));
                try (CsvWriter csv = createCsvWriter.apply(writer)) {
                    csv.writeRow("searge", "name", "side", "desc");
                    for (IDocumentedNode methodNode : methods) {
                        csv.writeRow(methodNode.getOriginal(), methodNode.getMapped(),
                                methodNode.getMeta("side"), methodNode.getJavadoc());
                    }
                }
                zipOut.closeEntry();
            }

            // Fields
            Collection<IDocumentedNode> fields = mappings.getFields();
            if (!fields.isEmpty()) {
                zipOut.putNextEntry(Utils.getStableEntry("fields.csv"));
                try (CsvWriter csv = createCsvWriter.apply(writer)) {
                    csv.writeRow("searge", "name", "side", "desc");
                    for (IDocumentedNode fieldNode : fields) {
                        csv.writeRow(fieldNode.getOriginal(), fieldNode.getMapped(),
                                fieldNode.getMeta("side"), fieldNode.getJavadoc());
                    }
                }
                zipOut.closeEntry();
            }

            // Parameters
            Collection<INode> parameters = mappings.getParameters();
            if (!parameters.isEmpty()) {
                zipOut.putNextEntry(Utils.getStableEntry("fields.csv"));
                try (CsvWriter csv = createCsvWriter.apply(writer)) {
                    csv.writeRow("param", "name", "side");
                    for (INode paramNode : parameters) {
                        csv.writeRow(paramNode.getOriginal(), paramNode.getMapped(),
                                paramNode.getMeta("side"));
                    }
                }
                zipOut.closeEntry();
            }
        }
    }
}
