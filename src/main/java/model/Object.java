package model;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import utils.ModelHelper;
import utils.RegexHelper;
import vocabs.NS;
import vocabs.ODFClass;
import vocabs.ODFProp;

import javax.xml.bind.annotation.*;
import javax.xml.namespace.QName;
import java.util.*;

import static utils.ModelHelper.getOtherAttributesModel;

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
public class Object extends ModelGenerator implements Deserializable, Serializable{

    private Collection<QlmID> id;
    private String type;
    private Description description;
    private Collection<InfoItem> infoItems = new ArrayList<>();
    private Collection<Object> objects = new ArrayList<>();
    private Map<QName, String> otherAttributes = new HashMap<QName, String>();

    //TODO: take care other attributes in deserialization and serialization
    //TODO: id as a list

    public Object(Collection<QlmID> id,
                  String type,
                  Description description,
                  Collection<InfoItem> infoItems,
                  Collection<Object> objects,
                  Map<QName, String> otherAttributes){
        this.id = id;
        this.type = type;
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

    public Description getDescription() {
        return description;
    }

    @XmlElement (name = "description")
    public void setDescription(Description description) {
        this.description = description;
    }

    public Collection<QlmID> getId() { return id; }

    public Map<QName, String> getOtherAttributes() {
        return otherAttributes;
    }

    @XmlAnyAttribute
    public void setOtherAttributes(Map<QName, String> otherAttributes) {
        this.otherAttributes = otherAttributes;
    }

    @XmlElement (name = "id", required = true)
    public void setId(Collection<QlmID> id) {
        this.id = id;
    }


    @Override
    public Model serialize (String objectBaseIri, String infoItemBaseIri) {

        Model model = ModelFactory.createDefaultModel();
        String idValue = "";
        if (getId().size() < 2) {
            idValue = getId().iterator().next().getId().replaceAll("[^a-zA-Z^0-9]", "");
        } else {
            idValue = String.valueOf(getId().hashCode());
        }

        Resource subject = model.createResource(objectBaseIri +idValue);

        model.setNsPrefix("rdf", NS.RDF);
        model.add(subject, RDF.type, ResourceFactory.createResource(NS.ODF + ODFClass.OBJECT));

        HashMap<String, String> elementsAndAttributes = new HashMap<>();
        elementsAndAttributes.put(NS.DC + ODFProp.TYPE, getType());


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
        Collection<Model> idModels = getIdModels(subject, getId(), ODFProp.ID);
        idModels.forEach(idModel -> model.add(idModel));

        //DESCRIPTION MODEL
        if (getDescription() != null) {
            Model descriptionModel = getDescriptionModel(getDescription(), subject);
            model.add(descriptionModel);
        }

        // INFOITEM MODEL
        Collection<Model> infoItemModels = getInfoItemModels(getInfoItems(), infoItemBaseIri + idValue + "/", subject);
        infoItemModels.forEach(infoItemModel -> model.add(infoItemModel));

        // NESTED OBJECT MODEL
        Collection<Model> nestedObjectsModels = getNestedObjectsModels(subject, getObjects(), infoItemBaseIri, idValue);
        nestedObjectsModels.forEach(nestedObjectsModel -> model.add(nestedObjectsModel));

        //OTHER ATTRIBUTES
        if (getOtherAttributes() != null) {
            Model otherAttributesModel = getOtherAttributesModel(subject, getOtherAttributes());
            model.add(otherAttributesModel);
        }

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
        Collection<QlmID> ids = new ArrayList<>();

        for (Statement statement : statements) {

            Property property = statement.getPredicate();
            Resource object = ResourceFactory.createResource(statement.getObject().toString());

            if (subject.toString().equals(statement.getSubject().toString())) {
                if (property.toString().contains(ODFProp.TYPE) && !object.toString().contains(ODFClass.OBJECT)) {
                    objectClass.setType(object.toString());
                }

                if (property.toString().contains(NS.ODF + ODFProp.DESCRIPTION)) {
                    objectClass.setDescription(descriptionClass.deserialize(object, statements));
                }

                if (property.toString().contains(ODFProp.ID)) {
                    ids.add(qlmIDClass.deserialize(object, statements));
                }

                if (property.toString().contains(ODFProp.INFOITEM)) {
                    infoItems.add(infoItemClass.deserialize(object, statements));
                }

                if (property.toString().contains(ODFProp.OBJECT)) {
                    objects.add(objectClass.deserialize(object, statements));
                }

                if (ModelHelper.ifOptionalProperty(property, ODFProp.objectProperties)) {
                    Map<QName, String> optionalAttribute = new HashMap<>();
                    optionalAttribute.put(QName.valueOf(RegexHelper.getOptionalProperty(property.toString())), object.toString());
                    objectClass.setOtherAttributes(optionalAttribute);
                }

            }

        }

        objectClass.setInfoItems(infoItems);
        objectClass.setObjects(objects);
        objectClass.setId(ids);

        return objectClass;
    }

}
