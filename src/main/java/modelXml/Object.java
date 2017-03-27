package modelXml;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import utils.ModelHelper;
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

        Model model = ModelFactory.createDefaultModel();
        Resource subject = model.createResource(objectBaseIri + id.getId());

        model.setNsPrefix("rdf", NS.RDF);
        model.add(subject, RDF.type, ResourceFactory.createResource(NS.ODF + "Object"));

        //todo: namespaces
        HashMap<String, String> elementsAndAttributes = new HashMap<>();
        //elementsAndAttributes.put("skos:notation", id);
        elementsAndAttributes.put(NS.ODF + "type", getType());
        elementsAndAttributes.put(NS.DCT + "description", getDescription());
        elementsAndAttributes.put(NS.ODF + "udef", getUdef());

        //TODO: namespace udef !!!

        for (Map.Entry<String, String> entry : elementsAndAttributes.entrySet()) {
            if (entry.getValue() != null) {
                if (entry.getKey().contains(NS.DCT)) {
                    model.setNsPrefix("dct", NS.DCT);
                } else {
                    model.setNsPrefix("odf", NS.ODF);
                }
                subject.addProperty(ResourceFactory.createProperty(entry.getKey()), entry.getValue());
            }
        }

        // ID MODEL
        Model idModel = ModelFactory.createDefaultModel();
        idModel.add(getId().serialize());

        Resource idValue = idModel.listStatements().next().getSubject();
        subject.addProperty(ResourceFactory.createProperty(NS.ODF, "id"), idValue);

        model.add(idModel);

        // INFOITEM MODEL
        Collection<Model> infoItemModels = new HashSet<>();
        String objRelatedInfoItemBaseIri = infoItemBaseIri + id.getId() + "/";

        getInfoItems().forEach(infoItem -> infoItemModels.add(infoItem.serialize(objRelatedInfoItemBaseIri)));

        infoItemModels.forEach(infoItemModel -> {
            Resource infoItemId = ModelHelper.getIdToConnectWith(infoItemModel, "InfoItem");
            if (infoItemId != null) {
                subject.addProperty(ResourceFactory.createProperty(NS.ODF + "infoitem"), infoItemId);
            }
        });

        infoItemModels.forEach(infoItemModel -> model.add(infoItemModel));

        // NESTED OBJECT MODEL
        Collection<Model> nestedObjectsModels = new HashSet<>();
        String nestedObjectsBaseIri = subject.toString() + "/";

        getObjects().forEach(nestedObject -> nestedObjectsModels
                .add(nestedObject.serialize(nestedObjectsBaseIri, objRelatedInfoItemBaseIri)));

        nestedObjectsModels.forEach(nestedObjectsModel -> {
            Resource nestedObjectId = ModelHelper.getIdToConnectWith(nestedObjectsModel, "Object");
            if (nestedObjectId != null) {
                subject.addProperty(ResourceFactory.createProperty(NS.ODF + "object"), nestedObjectId);
            }
        });

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
