package ba.ekapic1.stonebase.filter;

import ba.ekapic1.stonebase.model.Field;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CompositeField implements Field {
    private final List<Field> fields;

    CompositeField(final Field... fields) {
        this.fields = Arrays.asList(fields);
    }

    public static Field of(final Field... fields) {
        return new CompositeField(fields);
    }

    @Override
    public String getFieldName() {
        throw new IllegalStateException("Detected attempt to get field name of a composite field.");
    }

    @Override
    public boolean isComposite() {
        return true;
    }

    @Override
    public List<Field> components() {
        return Collections.unmodifiableList(fields);
    }
}
