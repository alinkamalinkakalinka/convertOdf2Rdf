package modelXml;

import org.apache.jena.rdf.model.*;
import org.apache.jena.rdf.model.Statement;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import vocabs.NS;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;

/**
 * Created by aarunova on 12/11/16.
 */

//@RdfsClass("odf:Object")
@XmlRootElement (name = "Object")
public class Object {

    private QlmID id;
    private String type;
    private String udef;
    private String description;
    private Collection<InfoItem> infoItems = new ArrayList<>();
    private Collection<Object> objects = new ArrayList<>();

    public Object(QlmID id, String type, String udef, String description, Collection<InfoItem> infoItems, Collection<Object> objects){
        this.id = id;
        this.type = type;
        this.udef = udef;
        this.description = description;
        this.infoItems = infoItems;
        this.objects = objects;
    }

    public Object(){}


    public String getType() {
        return type;
    }

    @XmlAttribute
    public void setType(String type) {
        this.type = type;
    }


    public Collection<InfoItem> getInfoItems() {
        return infoItems;
    }

    @XmlElement (name = "InfoItem")
    public void setInfoItems(Collection<InfoItem> infoItems) {
        this.infoItems = infoItems;
    }


    public Collection<Object> getObjects() {
        return objects;
    }

    @XmlElement (name = "Object")
    public void setObjects(Collection<Object> objects) {
        this.objects = objects;
    }


    public String getUdef() {
        return udef;
    }

    @XmlAttribute (name = "udef")
    public void setUdef(String udef) {
        this.udef = udef;
    }


    public String getDescription() {
        return description;
    }

    @XmlElement (name = "description")
    public void setDescription(String description) {
        this.description = description;
    }


    public QlmID getId() { return id; }

    @XmlElement (name = "id")
    public void setId(QlmID id) {
        this.id = id;
    }

    public Model serialize(ValueFactory vf, String objectBaseIri, String infoItemBaseIri) {

        IRI subject = vf.createIRI(objectBaseIri + id.getId());

        //todo: namespaces
        HashMap<String, String> elementsAndAttributes = new HashMap<>();
        //elementsAndAttributes.put("skos:notation", id);
        elementsAndAttributes.put("odf:type", type);
        elementsAndAttributes.put("dct:description", description);
        elementsAndAttributes.put("odf:udef", udef);

        ModelBuilder builder = new ModelBuilder();

        builder.setNamespace("dct", NS.DCT)
                .setNamespace("odf", NS.ODF)
                .setNamespace("rdf", RDF.NAMESPACE);

        builder.subject(subject)
                .add("rdf:type", "odf:Object");
                //.add("skos:notation", id);

        for (Map.Entry<String, String> entry : elementsAndAttributes.entrySet()) {
            if (entry.getValue() != null) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }

        Model objectModel = builder.build();

        Model idModel = new ModelBuilder().build();
        idModel.addAll(id.serialize(vf));
        for (Namespace namespace : idModel.getNamespaces()) {
            objectModel.setNamespace(namespace.getPrefix(), namespace.getName());
        }


        Resource idValue = idModel.iterator().next().getSubject();
        builder.add("odf:id", idValue);


        Collection<Model> infoItemModels = new HashSet<>();
        String objRelatedInfoItemBaseIri = infoItemBaseIri + id.getId() + "/";
        //infoItems.forEach(infoitem -> infoItemModels.add(infoitem.serialize(vf, objRelatedInfoItemBaseIri)));

        for(InfoItem infoItem : infoItems) {
            Model infoItemModel = infoItem.serialize(vf, objRelatedInfoItemBaseIri);
            infoItemModels.add(infoItemModel);
            for (Namespace namespace : infoItemModel.getNamespaces()) {
                objectModel.setNamespace(namespace.getPrefix(), namespace.getName());
            }
        }



        Collection<Model> nestedObjectsModels = new HashSet<>();
        String nestedObjectsBaseIri = subject.toString() + "/";
        //objects.forEach(object -> nestedObjectsModels.add(object.serialize(vf, nestedObjectsBaseIri, objRelatedInfoItemBaseIri)));

        for(Object object : objects) {
            Model nestedObjectsModel = object.serialize(vf, nestedObjectsBaseIri, objRelatedInfoItemBaseIri);
            nestedObjectsModels.add(nestedObjectsModel);
            for (Namespace namespace : nestedObjectsModel.getNamespaces()) {
                objectModel.setNamespace(namespace.getPrefix(), namespace.getName());
            }
        }


        infoItemModels.forEach(model -> {
            builder.add("odf:infoitem", model.iterator().next().getSubject());
        });

        nestedObjectsModels.forEach(model -> {
            builder.add("odf:object", model.iterator().next().getSubject());
        });


        objectModel.addAll(idModel);
        infoItemModels.forEach(infoItemModel -> objectModel.addAll(infoItemModel));
        nestedObjectsModels.forEach(nestedObjectsModel -> objectModel.addAll(nestedObjectsModel));

        return objectModel;
    }





    public Object deserialize(org.apache.jena.rdf.model.Resource subject, Collection<Statement> statements) {

        Object objectClass = new Object();
        QlmID qlmIDClass = new QlmID();
        InfoItem infoItemClass = new InfoItem();

        Collection<InfoItem> infoItems = new ArrayList<>();
        Collection<Object> objects = new ArrayList<>();

        for (Statement statement : statements) {

            Property property = statement.getPredicate();
            org.apache.jena.rdf.model.Resource object = ResourceFactory.createResource(statement.getObject().toString());

            if (subject.equals(statement.getSubject())) {
                if (property.toString().contains("type")) {
                    objectClass.setType(object.toString());
                }

                if (property.toString().contains("udef")) {
                    objectClass.setUdef(object.toString());
                }

                if (property.toString().contains("description")) {
                    objectClass.setDescription(object.toString());
                }

                if (property.toString().contains("id")) {
                    QlmID id = qlmIDClass.deserialize(object, statements);
                    objectClass.setId(id);
                }

                if (property.toString().contains("infoitem")) {
                    infoItems.add(infoItemClass.deserialize(object, statements));
                }

                if (property.toString().contains("object")) {
                    objects.add(objectClass.deserialize(object, statements));
                }

            }


        }

        objectClass.setInfoItems(infoItems);
        objectClass.setObjects(objects);

        return objectClass;
    }

}
