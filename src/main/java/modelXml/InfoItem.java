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

@XmlRootElement (name = "InfoItem")
public class InfoItem implements Deserializable{

    private String name1;
    private String name;
    private String udef;
    private Collection<Value> values = new ArrayList<>();
    private String description;
    private Collection<MetaData> metaData = new ArrayList<>();

    public InfoItem(){}

    public InfoItem(String name, String name1, String udef, List<Value> values, String description, Collection<MetaData> metaData){
        this.name = name;
        this.name = name1;
        this.udef = udef;
        this.values = values;
        this.description = description;
        this.metaData = metaData;
    }

    public String getUdef() {
        return udef;
    }

    @XmlAttribute (name = "udef")
    public void setUdef(String udef) {
        this.udef = udef;
    }


    public Collection<Value> getValues() {
        return values;
    }

    @XmlElement (name = "value")
    public void setValues(Collection<Value> values) {
        this.values = values;
    }


    public String getDescription() {
        return description;
    }

    @XmlElement (name = "description")
    public void setDescription(String description) {
        this.description = description;
    }


    public Collection<MetaData> getMetaData() {
        return metaData;
    }

    @XmlElement (name = "metaData")
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


    public String getName1() {
        return name1;
    }

    @XmlElement (name = "name")
    public void setName1(String name1) {
        this.name1 = name1;
    }


    public Model serialize (String infoItemBaseIri) {

        Model model = ModelFactory.createDefaultModel();
        Resource subject = model.createResource(infoItemBaseIri + getName());

        model.setNsPrefix("rdf", NS.RDF);
        subject.addProperty(RDF.type, ResourceFactory.createResource(NS.ODF + "InfoItem"));

        HashMap<String, String> elementsAndAttributes = new HashMap<>();
        elementsAndAttributes.put(NS.DCT + "description", getDescription());
        elementsAndAttributes.put(NS.ODF + "name", getName());
        elementsAndAttributes.put(NS.DCT + "title", getName1());
        elementsAndAttributes.put(NS.ODF + "udef", getUdef());

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

        // VALUE MODEL
        if (getValues() != null) {
            Collection<Model> valueModels = new HashSet<>();
            getValues().forEach(value -> valueModels.add(value.serialize()));

            valueModels.forEach(valueModel -> {
                Resource valueId = ModelHelper.getIdToConnectWith(valueModel, "Value");
                if (valueId != null) {
                    subject.addProperty(ResourceFactory.createProperty(NS.ODF + "value"), valueId);
                }
            });

            valueModels.forEach(valueModel -> model.add(valueModel));
        }


        // METADATA MODEL
        if (getMetaData() != null) {
            Collection<Model> metaDataModels = new HashSet<>();
            getMetaData().forEach(metaDataValue -> metaDataModels.add(metaDataValue.serialize(subject + "/")));

            metaDataModels.forEach(metsDataModel -> {
                Resource metadataId = ModelHelper.getIdToConnectWith(metsDataModel, "MetaData");
                if (metadataId != null) {
                    subject.addProperty(ResourceFactory.createProperty(NS.ODF + "metadata"), metadataId);
                }
            });

            metaDataModels.forEach(metadataModel -> model.add(metadataModel));
        }


        return model;
    }

    @Override
    public InfoItem deserialize (Resource subject, Collection<Statement> statements) {

        InfoItem infoItemClass = new InfoItem();
        Value valueClass = new Value();
        MetaData metaDataClass = new MetaData();

        Collection<Value> values = new ArrayList<>();
        Collection<MetaData> metaDatas = new ArrayList<>();

        for (Statement statement : statements) {

            Property property = statement.getPredicate();
            Resource object = ResourceFactory.createResource(statement.getObject().toString());

            if (subject.equals(statement.getSubject())) {

                if (property.toString().contains("name")) {
                    infoItemClass.setName(object.toString());
                }

                //TODO: name1 ????
                if (property.toString().contains("name1")) {
                    infoItemClass.setName1(object.toString());
                }

                if (property.toString().contains("udef")) {
                    infoItemClass.setUdef(object.toString());
                }

                if (property.toString().contains("description")) {
                    infoItemClass.setDescription(object.toString());
                }

                if (property.toString().contains("metadata")) {
                    metaDatas.add(metaDataClass.deserialize(object, statements));
                }

                if (property.toString().contains("value")) {
                    values.add(valueClass.deserialize(object,statements));
                }
            }
        }

        infoItemClass.setValues(values);
        infoItemClass.setMetaData(metaDatas);

        //TODO: no dataValue

        return infoItemClass;
    }
}
