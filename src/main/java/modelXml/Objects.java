package modelXml;

import org.apache.jena.rdf.model.Statement;
import org.apache.jena.vocabulary.RDF;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by aarunova on 12/11/16.
 */

//@RdfsClass("odf:Objects")
@XmlRootElement (name = "Objects")
public class Objects {

    private String name;
    private Collection<Object> objects = new ArrayList<>();

    public Objects() {}

    public Objects(String name, Collection<Object> objects) {
        super();
        this.name = name;
        this.objects = objects;
    }


    public String getName() {
        return name;
    }

    @XmlAttribute
    public void setName(String name) {
        this.name = name;
    }


    public Collection<Object> getObjects() {
        return objects;
    }

    @XmlElement (name = "Object")
    public void setObjects(Collection<Object> objects) {
        this.objects = objects;
    }


    public Objects deserialize (Collection<Statement> statements, String baseOdfURI) {

        Objects objectsClass = new Objects();
        Object objectClass = new Object();

        for (Statement statement : statements) {

            if (statement.getPredicate().equals(RDF.type)
                    && (baseOdfURI + "Object").equals(statement.getObject().toString())) {

                Object object = objectClass.deserialize(statement.getSubject(), statements);

                objects.add(object);
            }
        }

        objectsClass.setObjects(objects);

        return objectsClass;

    }
}
