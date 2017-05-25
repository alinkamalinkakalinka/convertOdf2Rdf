package model;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.datatypes.xsd.impl.XSDAbstractDateTimeType;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import vocabs.NS;
import vocabs.ODFClass;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.*;
import javax.xml.namespace.QName;
import java.util.*;

import static utils.ModelHelper.getOtherAttributesModel;

/**
 * Created by aarunova on 12/11/16.
 */

//@XmlRootElement(name = "id")
public class QlmID implements Deserializable, Serializable{

    //TODO: gregorian calendar ????

    private String id;
    private String tagType;
    private String startDate;
    private String endDate;
    private Map<QName, String> otherAttributes = new HashMap<QName, String>();

    public QlmID(String id,
                 String tagType,
                 String startDate,
                 String endDate,
                 Map<QName, String> otherAttributes) {
        this.id = id;
        this.tagType = tagType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.otherAttributes = otherAttributes;
    }

    public QlmID() {
    }


    public String getId() {
        return id;
    }

    @XmlValue
    public void setId(String id) {
        this.id = id;
    }


    public String getTagType() {
        return tagType;
    }

    @XmlAttribute (name = "tagType")
    public void setTagType(String tagType) {
        this.tagType = tagType;
    }


    public String getStartDate() {
        return startDate;
    }

    @XmlAttribute (name = "startDate")
    @XmlSchemaType(name = "dateTime")
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }


    public String getEndDate() {
        return endDate;
    }

    @XmlAttribute (name = "endDate")
    @XmlSchemaType(name = "dateTime")
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Map<QName, String> getOtherAttributes() {
        return otherAttributes;
    }

    @XmlAnyAttribute
    public void setOtherAttributes(Map<QName, String> otherAttributes) {
        this.otherAttributes = otherAttributes;
    }

    public Model serialize () {
        return serialize(null, null);
    }


    @Override
    public Model serialize(String objectBaseIri, String infoItemBaseIri) {

        Model model = ModelFactory.createDefaultModel();
        Resource subject = model.createResource();

        model.setNsPrefix("rdf", NS.RDF);
        subject.addProperty(RDF.type, ResourceFactory.createResource(NS.ODF + ODFClass.QLMID));

        HashMap<String, String> elementsAndAttributes = new HashMap<>();
        elementsAndAttributes.put(NS.TIME + "startDate", getStartDate());
        elementsAndAttributes.put(NS.TIME + "endDate", getEndDate());

        model.setNsPrefix("dct", NS.DCT)
                .setNsPrefix("time", NS.TIME)
                .setNsPrefix("odf", NS.ODF)
                .setNsPrefix("rdf", NS.RDF);


        if (getId() != null) {
            model.setNsPrefix("odf", NS.ODF);
            subject.addProperty(ResourceFactory.createProperty(NS.ODF, "idValue"), getId());
        }

        if (getTagType() != null) {
            model.setNsPrefix("dct", NS.DCT);
            subject.addProperty(ResourceFactory.createProperty(NS.DCT, "tagType"), getTagType());
        }

        for (Map.Entry<String, String> entry : elementsAndAttributes.entrySet()) {
            if (entry.getValue() != null) {
                model.setNsPrefix("time", NS.TIME);
                //TODO: parse date
                Literal dateValue = ResourceFactory.createTypedLiteral(DatatypeConverter.parseDateTime(entry.getValue()).getTime());
                subject.addProperty(ResourceFactory.createProperty(entry.getKey()), dateValue);
            }
        }

        //OTHER ATTRIBUTES
        if (getOtherAttributes() != null) {
            Model otherAttributesModel = getOtherAttributesModel(subject, getOtherAttributes());
            model.add(otherAttributesModel);
        }

        return model;
    }

    @Override
    public QlmID deserialize (Resource subject, Collection<Statement> statements) {

        QlmID qlmIDClass = new QlmID();

        for (Statement statement : statements) {

            Property property = statement.getPredicate();
            Resource object = ResourceFactory.createResource(statement.getObject().toString());

            if (subject.toString().equals(statement.getSubject().toString())) {

                if (property.toString().contains("idValue")) {
                    qlmIDClass.setId(object.toString());
                }

                if (property.toString().contains("tagType")) {
                    qlmIDClass.setTagType(object.toString());
                }

                if (property.toString().contains("startDate")) {
                    qlmIDClass.setStartDate(object.toString());
                }

                if (property.toString().contains("endDate")) {
                    qlmIDClass.setStartDate(object.toString());
                }

                if (ifOptionalProperty(property)) {
                    Map<QName, String> optionalAttribute = new HashMap<>();
                    optionalAttribute.put(QName.valueOf("idType"), object.toString());
                    qlmIDClass.setOtherAttributes(optionalAttribute);
                }
            }
        }

        return qlmIDClass;
    }

    private boolean ifOptionalProperty (Property property) {

        boolean ifOptionalProperty = true;

        List<String> idProperties = new ArrayList<>();
        idProperties.add("idValue");
        idProperties.add("tagType");
        idProperties.add("startDate");
        idProperties.add("endDate");
        idProperties.add("type");

        for (String idProperty : idProperties) {
            if (property.toString().contains(idProperty)) {
                ifOptionalProperty = false;
            }
        }

        return ifOptionalProperty;
    }
}
