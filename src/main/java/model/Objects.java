package model;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import vocabs.NS;
import vocabs.ODFClass;
import vocabs.ODFProp;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collection;

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
            subject.addProperty(ResourceFactory.createProperty(NS.DCT + ODFProp.VERSION), getVersion());
        }

        model.add(subject, RDF.type, ResourceFactory.createResource(NS.ODF + ODFClass.OBJECTS));

        if (getObjects() != null) {
            // NESTED OBJECT MODEL
            Collection<Model> nestedObjectsModels = getNestedObjectsModels(subject,
                    getObjects(),
                    infoItemBaseIri,
                    String.valueOf(Objects.class.hashCode()));
            nestedObjectsModels.forEach(nestedObjectsModel -> model.add(nestedObjectsModel));
        }

        return model;
    }

    public Objects deserialize(Collection<Statement> statements) {
        return deserialize(null, statements);
    }

    @Override
    public Objects deserialize(Resource subject, Collection<Statement> statements) {

        Objects objectsClass = new Objects();
        Object objectClass = new Object();

        Collection<Object> objects = new ArrayList<>();

        Collection<Resource> rootObjects = getRootObjects(statements);

        for (Resource rootObject : rootObjects) {

            Object object = objectClass.deserialize(rootObject, statements);
            objects.add(object);
        }

        objectsClass.setObjects(objects);

        return objectsClass;
    }

    private Collection<Resource> getRootObjects (Collection<Statement> statements) {

        Collection<String> stringRootObjects = new ArrayList<>();
        Collection<Resource> rootObjects = new ArrayList<>();
        String mainRootObject = "";

        for (Statement statement : statements) {

            if (statement.getPredicate().equals(RDF.type) &&
                statement.getObject().toString().contains("Objects")) {
                mainRootObject = statement.getSubject().toString();
            }
        }

        for (Statement statement : statements) {

            if (statement.getSubject().toString().equals(mainRootObject)&&
                statement.getPredicate().toString().contains(ODFProp.OBJECT)) {
                stringRootObjects.add(statement.getObject().toString());
            }
        }

        for (String stringRootObject : stringRootObjects) {

            Resource rootObject = ResourceFactory.createResource(stringRootObject);
            rootObjects.add(rootObject);
        }

        return rootObjects;

    }
}
