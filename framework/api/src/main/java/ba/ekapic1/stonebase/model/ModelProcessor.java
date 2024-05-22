package ba.ekapic1.stonebase.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.QualifiedNameable;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.beans.Transient;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Generates enum classes for all classes annotated with {@link Model}.
 * These classes allow better type safety in filter queries.
 */
@SupportedAnnotationTypes("ba.ekapic1.stonebase.model.Model")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class ModelProcessor extends AbstractProcessor {
    private static final Logger logger = LoggerFactory.getLogger(ModelProcessor.class);

    @Override
    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
        for (final TypeElement annotation : annotations) {
            final Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);

            for (final Element element : annotatedElements) {
                if (ElementKind.CLASS != element.getKind()) {
                    logger.warn("Detected @Model annotation on element %s which is not a class. Ignoring it.".formatted(element.getSimpleName()));
                    continue;
                }

                final List<String> fieldNames = new ArrayList<>();

                final Set<Element> fieldElements = element.getEnclosedElements().stream()
                        .filter(el -> ElementKind.FIELD == el.getKind()).collect(Collectors.toSet());

                for (final Element fieldElement : fieldElements) {
                    final Set<Modifier> modifiers = element.getModifiers();

                    if (!modifiers.contains(Modifier.STATIC) && !modifiers.contains(Modifier.TRANSIENT) && element.getAnnotation(Transient.class) == null) {
                        // We only include the field if it isn't static or transient.

                        fieldNames.add(fieldElement.getSimpleName().toString());
                    }
                }

                // Qualified name = package name + class name
                final String typeName = ((QualifiedNameable) element).getQualifiedName().toString();

                generate(typeName, fieldNames);
            }
        }
        return false;
    }

    private void generate(final String typeName, final List<String> fieldNames) {
        try{
            final String generatedInstant = Clock.systemUTC().instant().toString();

            final String fileName = typeName + "Field";
            final JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(fileName);

            final int lastDotIndex = fileName.lastIndexOf(".");

            final String metaClassName = fileName.substring(lastDotIndex + 1);

            try(final PrintWriter out = new PrintWriter(sourceFile.openWriter())) {
                out.println("package " + fileName.substring(0, lastDotIndex) + ";");
                out.println();
                out.println("import ba.ekapic1.stonebase.model.Field;");
                out.println();
                out.println("import javax.annotation.processing.Generated;");
                out.println();
                out.println("@Generated(value = \"" + this.getClass().getName() + "\", date = \"" + generatedInstant + "\")");
                out.println("public enum " + metaClassName + " implements Field {");
                out.println();
                for (int i = 0; i < fieldNames.size(); i++) {
                    out.println("    " + toUpperSnakeCase(fieldNames.get(i)) + "(\"" + fieldNames.get(i) + "\")" + (i == fieldNames.size() - 1 ? ";" : ","));
                }
                out.println();
                out.println("    private final String fieldName;");
                out.println();
                out.println("    " + metaClassName + "(final String fieldName) {");
                out.println("        this.fieldName = fieldName;");
                out.println("    }");
                out.println();
                out.println("    @Override");
                out.println("    public String getFieldName() {");
                out.println("        return fieldName;");
                out.println("    }");
                out.println();
                out.println("    @Override");
                out.println("    public boolean isComposite() {");
                out.println("        return false;");
                out.println("    }");
                out.println("}");
            }
        } catch (final IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private String toUpperSnakeCase(final String camelCase) {
        final StringBuilder builder = new StringBuilder();

        boolean isFirstChar = true;
        for (final char ch : camelCase.toCharArray()) {
            if (!isFirstChar && Character.isUpperCase(ch)) {
                builder.append("_");
            }

            builder.append(Character.toUpperCase(ch));
            isFirstChar = false;
        }

        return builder.toString();
    }
}
