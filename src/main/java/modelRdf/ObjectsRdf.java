package modelRdf;

import com.complexible.pinto.Identifiable;
import com.complexible.pinto.impl.IdentifiableImpl;
import org.openrdf.model.Resource;

import java.util.Objects;

/**
 * Created by aarunova on 2/12/17.
 */

public class ObjectsRdf implements Identifiable{

    private String id;
    private String object;

    private Identifiable mIdentifiable = new IdentifiableImpl();

    @Override
    public Resource id() {
        return mIdentifiable.id();
    }

    @Override
    public void id(final Resource theResource) {
        mIdentifiable.id(theResource);
    }

    @Override
    public boolean equals(final Object theObj) {
        if (this == theObj) {
            return true;
        }
        if (theObj == null || getClass() != theObj.getClass()) {
            return false;
        }

        ObjectsRdf that = (ObjectsRdf) theObj;

        return id == that.id
                && object == that.object;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, object);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setObject(String object) {
        this.object = object;
    }
}
