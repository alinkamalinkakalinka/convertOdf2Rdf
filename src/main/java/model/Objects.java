package model;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import vocabs.NS;
import vocabs.ODFProp;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by aarunova on 12/11/16.
 */

@XmlRootElement (name = "Objects")
public class Objects extends ModelGenerator implements Serializable, Deserializable {

    private String name;
    private String version;
    private Collection<Object> objects = new ArrayList<>();

    public Objects() {}

    public Objects(String name, String version, Collection<Object> objects) {
        super();
        this.name = name;
        this.version = version;
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

    public String getVersion() {
        return version;
    }

    @XmlAttribute (name = "version")
    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public Model serialize(String objectBaseIri, String infoItemBaseIri) {

        Model model = ModelFactory.createDefaultModel();
        Resource subject;

        model.setNsPrefix("rdf", NS.RDF);

        if (getName() != null) {
            model.setNsPrefix("dct", NS.DCT);
            subject = model.createResource(objectBaseIri + getName());
            subject.addProperty(ResourceFactory.createProperty(NS.DCT + ODFProp.NAME), getName());
        } else {
            subject = model.createResource(objectBaseIri + String.valueOf(Objects.class.hashCode()));
        }

        if (getVersion() != null) {
            model.setNsPrefix("dct", NS.DCT);
            subject.addProperty(ResourceFactory.createProperty(NS.DCT + "version"), getVersion());
        }

        model.add(subject, RDF.type, ResourceFactory.createResource(NS.ODF + "Objects"));

        if (getObjects() != null) {
            // NESTED OBJECT MODEL
            Collection<Model> nestedObjectsModels = getNestedObjectsModels(subject,
                    getObjects(),
                    infoItemBaseIri,
                    String.valueOf(objects.hashCode()));
            nestedObjectsModels.forEach(nestedObjectsModel -> model.add(nestedObjectsModel));
        }

        return model;
    }

    @Override
    public Objects deserialize(Resource subject, Collection<Statement> statements) {

        return null;
    }
}
