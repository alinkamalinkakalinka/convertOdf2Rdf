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

@XmlRootElement (name = "InfoItem")
@XmlType(propOrder = {
        "name1",
        "description",
        "metaData",
        "values"
})
public class InfoItem extends ModelGenerator implements Deserializable, Serializable {

    private Collection<QlmID> name1;
    private String name;
    private Collection<Value> values = new ArrayList<>();
    private Description description;
    private Collection<MetaData> metaData = new ArrayList<>();
    private Map<QName, String> otherAttributes = new HashMap<QName, String>();

    public InfoItem(){}

    public InfoItem(String name,
                    Collection<QlmID> name1,
                    List<Value> values,
                    Description description,
                    Collection<MetaData> metaData,
                    Map<QName, String> otherAttributes){
        this.name = name;
        this.name1 = name1;
        this.values = values;
        this.description = description;
        this.metaData = metaData;
        this.otherAttributes = otherAttributes;
    }

    public Collection<Value> getValues() {
        return values;
    }

    @XmlElement (name = "value")
    public void setValues(Collection<Value> values) {
        this.values = values;
    }


    public Description getDescription() {
        return description;
    }

    @XmlElement (name = "description")
    public void setDescription(Description description) {
        this.description = description;
    }


    public Collection<MetaData> getMetaData() {
        return metaData;
    }

    @XmlElement (name = "MetaData")
    public void setMetaData(Collection<MetaData> metaData) {
        this.metaData = metaData;
    }


    public String getName() {
        return name;
    }

    @XmlAttribute (name = "name")
    public void setName(String name) {
        this.name = name;
    }


    public Collection<QlmID> getName1() {
        return name1;
    }

    @XmlElement (name = "name")
    public void setName1(Collection<QlmID> name1) {
        this.name1 = name1;
    }

    public Map<QName, String> getOtherAttributes() {
        return otherAttributes;
    }

    @XmlAnyAttribute
    public void setOtherAttributes(Map<QName, String> otherAttributes) {
        this.otherAttributes = otherAttributes;
    }

    public Model serialize (String infoItemBaseIri) {
        return serialize(null, infoItemBaseIri);
    }

    @Override
    public Model serialize (String objectBaseIri, String infoItemBaseIri) {

        Model model = ModelFactory.createDefaultModel();
        Resource subject = model.createResource(infoItemBaseIri + getName().replaceAll("[^a-zA-Z^0-9]", ""));

        model.setNsPrefix("rdf", NS.RDF);
        subject.addProperty(RDF.type, ResourceFactory.createResource(NS.ODF + ODFClass.INFOITEM));

        HashMap<String, String> elementsAndAttributes = new HashMap<>();
        elementsAndAttributes.put(NS.DCT + ODFProp.NAME, getName());

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
        if (getName1() != null) {
            Collection<Model> idModels = getIdModels(subject, getName1(), "name");
            idModels.forEach(idModel -> model.add(idModel));
        }


        //DESCRIPTION MODEL
        if (getDescription() != null) {
            Model descriptionModel = getDescriptionModel(getDescription(), subject);
            model.add(descriptionModel);
        }


        // VALUE MODEL
        if (getValues() != null) {
            Collection<Model> valueModels = getValueModels(subject, getValues());
            valueModels.forEach(valueModel -> model.add(valueModel));
        }


        // METADATA MODEL
        if (getMetaData() != null) {
            Collection<Model> metaDataModels = getMetaDataModels(subject, getMetaData());
            metaDataModels.forEach(metadataModel -> model.add(metadataModel));
        }

        //OTHER ATTRIBUTES
        if (getOtherAttributes() != null) {
            Model otherAttributesModel = getOtherAttributesModel(subject, getOtherAttributes());
            model.add(otherAttributesModel);
        }

        return model;
    }

    @Override
    public InfoItem deserialize (Resource subject, Collection<Statement> statements) {

        InfoItem infoItemClass = new InfoItem();
        Value valueClass = new Value();
        MetaData metaDataClass = new MetaData();
        Description descriptionClass = new Description();
        QlmID qlmIDClass = new QlmID();

        Collection<Value> values = new ArrayList<>();
        Collection<MetaData> metaDatas = new ArrayList<>();
        Collection<QlmID> ids = new ArrayList<>();

        for (Statement statement : statements) {

            Property property = statement.getPredicate();
            Resource object = ResourceFactory.createResource(statement.getObject().toString());

            if (subject.equals(statement.getSubject())) {

                if (property.toString().contains(NS.DCT + ODFProp.NAME)) {
                    infoItemClass.setName(object.toString());
                }

                if (property.toString().contains(NS.ODF + ODFProp.NAME)) {
                    ids.add(qlmIDClass.deserialize(object, statements));
                }

                if (property.toString().contains(NS.ODF + ODFProp.DESCRIPTION)) {
                    infoItemClass.setDescription(descriptionClass.deserialize(object, statements));
                }

                if (property.toString().contains(NS.ODF + ODFProp.METADATA)) {
                    metaDatas.add(metaDataClass.deserialize(object, statements));
                }

                if (property.toString().contains(NS.ODF + ODFProp.VALUE)) {
                    values.add(valueClass.deserialize(object,statements));
                }

                if (ModelHelper.ifOptionalProperty(property, ODFProp.infoItemProperties)) {
                    Map<QName, String> optionalAttribute = new HashMap<>();
                    optionalAttribute.put(QName.valueOf(RegexHelper.getOptionalProperty(property.toString())), object.toString());
                    infoItemClass.setOtherAttributes(optionalAttribute);
                }
            }
        }

        infoItemClass.setValues(values);
        infoItemClass.setMetaData(metaDatas);
        infoItemClass.setName1(ids);

        return infoItemClass;
    }
}
