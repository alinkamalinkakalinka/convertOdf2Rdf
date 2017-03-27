package modelXml;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import vocabs.NS;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;

/**
 * Created by aarunova on 12/11/16.
 */

@XmlRootElement (name = "Object")
public class Object implements Deserializable{

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

    public Model serialize (String objectBaseIri, String infoItemBaseIri) {

        //todo: namespaces
        HashMap<String, String> elementsAndAttributes = new HashMap<>();
        //elementsAndAttributes.put("skos:notation", id);
        elementsAndAttributes.put(NS.ODF + "type", type);
        elementsAndAttributes.put(NS.DCT + "description", description);
        elementsAndAttributes.put(NS.ODF + "udef", udef);

        Model model = ModelFactory.createDefaultModel();
        Resource subject = model.createResource(objectBaseIri + id.getId());

        model.setNsPrefix("dct", NS.DCT)
                .setNsPrefix("odf", NS.ODF)
                .setNsPrefix("rdf", NS.RDF);

        model.add(subject, RDF.type, ResourceFactory.createResource(NS.ODF + "Object"));


        for (Map.Entry<String, String> entry : elementsAndAttributes.entrySet()) {
            if (entry.getValue() != null) {
                model.add(subject, ResourceFactory.createProperty(entry.getKey()), entry.getValue());
            }
        }

        //id Model
        Model idModel = ModelFactory.createDefaultModel();
        idModel.add(id.serialize());


        Resource idValue = idModel.listStatements().next().getSubject();
        subject.addProperty(ResourceFactory.createProperty(NS.ODF, "id"), idValue);

        //infoitem Model
        Collection<Model> infoItemModels = new HashSet<>();
        String objRelatedInfoItemBaseIri = infoItemBaseIri + id.getId() + "/";

        for(InfoItem infoItem : infoItems) {
            Model infoItemModel = infoItem.serialize(objRelatedInfoItemBaseIri);
            infoItemModels.add(infoItemModel);
        }


        Collection<Model> nestedObjectsModels = new HashSet<>();
        String nestedObjectsBaseIri = subject.toString() + "/";

        for(Object object : objects) {
            Model nestedObjectsModel = object.serialize(nestedObjectsBaseIri, objRelatedInfoItemBaseIri);
            nestedObjectsModels.add(nestedObjectsModel);
        }


        infoItemModels.forEach(infoItemModel -> {
            Resource infoItemId = null;
            StmtIterator iterator = infoItemModel.listStatements();
            while (iterator.hasNext()) {
                Statement stmt = iterator.nextStatement();
                if (stmt.getObject().toString().contains("InfoItem")) {
                    infoItemId = stmt.getSubject();
                }
            }
            subject.addProperty(ResourceFactory.createProperty(NS.ODF + "infoitem"), infoItemId);
        });

        nestedObjectsModels.forEach(nestedObjectsModel -> {
            subject.addProperty(ResourceFactory.createProperty(NS.ODF + "object"), nestedObjectsModel.listStatements().next().getSubject());
        });


        model.add(idModel);
        infoItemModels.forEach(infoItemModel -> model.add(infoItemModel));
        nestedObjectsModels.forEach(nestedObjectsModel -> model.add(nestedObjectsModel));

        return model;
    }

    @Override
    public Object deserialize(Resource subject, Collection<Statement> statements) {

        Object objectClass = new Object();
        QlmID qlmIDClass = new QlmID();
        InfoItem infoItemClass = new InfoItem();

        Collection<InfoItem> infoItems = new ArrayList<>();
        Collection<Object> objects = new ArrayList<>();

        for (Statement statement : statements) {

            Property property = statement.getPredicate();
            Resource object = ResourceFactory.createResource(statement.getObject().toString());

            if (subject.equals(statement.getSubject())) {
                if (property.toString().contains("type") && !object.toString().contains("Object")) {
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
