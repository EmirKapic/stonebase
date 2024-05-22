package ba.ekapic1.stonebase.model;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public interface Field {
    String getFieldName();

    boolean isComposite();

    default List<Field> components() {
        return Collections.emptyList();
    }
}
