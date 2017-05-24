package model;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import utils.RegexHelper;
import vocabs.NS;
import vocabs.ODFClass;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.*;
import javax.xml.namespace.QName;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static utils.ModelHelper.getOtherAttributesModel;

/**
 * Created by aarunova on 12/11/16.
 */

@XmlRootElement (name = "value")
public class Value implements Deserializable, Serializable{

    private String type;
    private String dateTime;
    private String unixTime; //TODO: Long
    private String value;
    private Map<QName, String> otherAttributes = new HashMap<QName, String>();

    public Value(){}

    public Value(String type,
                 String dateTime,
                 String unixTime,
                 String value,
                 Map<QName, String> otherAttributes){
        this.type = type;
        this.dateTime = dateTime;
        this.unixTime = unixTime;
        this.value = value;
    }


    public String getType() {
        return type;
    }

    @XmlAttribute (name = "type")
    public void setType(String type) {
        this.type = type;
    }


    public String getDateTime() {
        return dateTime;
    }

    @XmlAttribute (name = "dateTime")
    @XmlSchemaType(name = "dateTime")
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }


    public String getUnixTime() {
        return unixTime;
    }

    @XmlAttribute
    public void setUnixTime(String unixTime) {
        this.unixTime = unixTime;
    }


    public String getValue() {
        return value;
    }

    @XmlValue
    public void setValue(String value) {
        this.value = value;
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
    public Model serialize (String objectBaseIri, String infoItemBaseIri) {

        Model model = ModelFactory.createDefaultModel();
        Resource subject = model.createResource();

        model.setNsPrefix("rdf", NS.RDF);
        subject.addProperty(RDF.type, ResourceFactory.createResource(NS.ODF + ODFClass.VALUE));

        if (getDateTime() != null || getValue() != null || getUnixTime() != null) {
            model.setNsPrefix("xsd", NS.XSD);

            if (getUnixTime() != null) {
                Literal unixTimeValue = ResourceFactory.createTypedLiteral(unixTime, XSDDatatype.XSDint);
                subject.addProperty(ResourceFactory.createProperty(NS.TIME + "unixTime"), unixTimeValue);
            }

            if (getDateTime() != null) {
                Literal createdValue = ResourceFactory.createTypedLiteral(
                        getDateTime(),
                        XSDDatatype.XSDdateTime);

                model.setNsPrefix("dct", NS.DCT);
                subject.addProperty(ResourceFactory.createProperty(NS.DCT + "created"), createdValue);
            }

            if (getValue() != null) {
                XSDDatatype datatype = null;

                if (getType() != null) {
                    datatype = new XSDDatatype(RegexHelper.getObjectType(getType()), String.class);
                }

                Literal dataValue = ResourceFactory.createTypedLiteral(getValue(), datatype);

                model.setNsPrefix("odf", NS.ODF);
                subject.addProperty(ResourceFactory.createProperty(NS.ODF + "dataValue"), dataValue);

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
    public Value deserialize (Resource subject, Collection<Statement> statements) {
        Value valueClass = new Value();

        for (Statement statement : statements) {

            Property property = statement.getPredicate();
            Resource object = ResourceFactory.createResource(statement.getObject().toString());

            if (subject.toString().equals(statement.getSubject().toString())) {

                if (property.toString().contains("type") && !object.toString().contains("Value")){
                    valueClass.setType(object.toString());
                }

                if (property.toString().contains("created")) {
                    valueClass.setDateTime(RegexHelper.getLiteralValue(object.toString()));
                }

                if (property.toString().contains("unixTime")) {
                    valueClass.setUnixTime(RegexHelper.getLiteralValue(object.toString()));
                }

                if (property.toString().contains("dataValue")) {

                    if (object.toString().contains("Schema")) {
                        valueClass.setValue(RegexHelper.getLiteralValue(object.toString()));
                        valueClass.setType("xs:" + RegexHelper.getLitralType(object.toString()));
                    } else {
                        valueClass.setValue(object.toString());
                    }
                }
            }
        }

        return valueClass;
    }
}
