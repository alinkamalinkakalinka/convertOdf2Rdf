/*
package modelRdf;

import com.complexible.pinto.Identifiable;
import com.complexible.pinto.impl.IdentifiableImpl;
import com.google.common.collect.Sets;
import org.openrdf.model.Resource;

import java.util.List;
import java.util.Objects;
import java.util.Set;

*/
/**
 * Created by aarunova on 2/12/17.
 *//*

public class ObjectsRdf implements Identifiable{

    private String name;
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

        return name == that.name
                && object == that.object;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, object);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setObject(String object) {
        this.object = object;
    }
}
*/
