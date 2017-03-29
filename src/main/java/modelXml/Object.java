package modelXml;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import utils.ModelHelper;
import vocabs.NS;

import javax.xml.bind.annotation.*;
import javax.xml.namespace.QName;
import java.util.*;

/**
 * Created by aarunova on 12/11/16.
 */

@XmlRootElement (name = "Object")
@XmlType(propOrder = {
        "id",
        "description",
        "infoItems",
        "objects"
})
public class Object implements Deserializable, Serializable{

    private QlmID id;
    private String type;
    private String udef;
    private Description description;
    private Collection<InfoItem> infoItems = new ArrayList<>();
    private Collection<Object> objects = new ArrayList<>();
    private Map<QName, String> otherAttributes = new HashMap<QName, String>();

    //TODO: take care other attributes in deserialization and serialization
    //TODO: id as a list

    public Object(QlmID id,
                  String type,
                  String udef,
                  Description description,
                  Collection<InfoItem> infoItems,
                  Collection<Object> objects,
                  Map<QName, String> otherAttributes){
        this.id = id;
        this.type = type;
        this.udef = udef;
        this.description = description;
        this.infoItems = infoItems;
        this.objects = objects;
        this.otherAttributes = otherAttributes;
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


    public Description getDescription() {
        return description;
    }

    @XmlElement (name = "description")
    public void setDescription(Description description) {
        this.description = description;
    }

    public QlmID getId() { return id; }

    public Map<QName, String> getOtherAttributes() {
        return otherAttributes;
    }

    @XmlAnyAttribute
    public void setOtherAttributes(Map<QName, String> otherAttributes) {
        this.otherAttributes = otherAttributes;
    }

    @XmlElement (name = "id", required = true)
    public void setId(QlmID id) {
        this.id = id;
    }


    @Override
    public Model serialize (String objectBaseIri, String infoItemBaseIri) {

        Model model = ModelFactory.createDefaultModel();
        Resource subject = model.createResource(objectBaseIri + id.getId());

        model.setNsPrefix("rdf", NS.RDF);
        model.add(subject, RDF.type, ResourceFactory.createResource(NS.ODF + "Object"));

        //todo: namespaces
        HashMap<String, String> elementsAndAttributes = new HashMap<>();
        //elementsAndAttributes.put("skos:notation", id);
        elementsAndAttributes.put(NS.ODF + "type", getType());
        //elementsAndAttributes.put(NS.DCT + "description", getDescription());
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
        idModel.add(getId().serialize(null, null));

        Resource idValue = idModel.listStatements().next().getSubject();
        subject.addProperty(ResourceFactory.createProperty(NS.ODF, "id"), idValue);

        model.add(idModel);

        //DESCRIPTION MODEL
        if (getDescription() != null) {
            Model descriptionModel = ModelFactory.createDefaultModel();
            descriptionModel.add(getDescription().serialize());

            Resource descriptionValue = descriptionModel.listStatements().next().getSubject();
            subject.addProperty(ResourceFactory.createProperty(NS.ODF, "description"), descriptionValue);

            model.add(descriptionModel);
        }

        // INFOITEM MODEL
        Collection<Model> infoItemModels = new HashSet<>();
        String objRelatedInfoItemBaseIri = infoItemBaseIri + id.getId() + "/";

        getInfoItems().forEach(infoItem -> infoItemModels.add(infoItem.serialize(null, objRelatedInfoItemBaseIri)));

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
        Description descriptionClass = new Description();

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

                if (property.toString().contains(NS.ODF + "description")) {
                    objectClass.setDescription(descriptionClass.deserialize(object, statements));
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
