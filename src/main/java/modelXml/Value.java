package modelXml;

import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.datatypes.TypeMapper;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import vocabs.NS;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import java.util.Collection;

/**
 * Created by aarunova on 12/11/16.
 */

@XmlRootElement (name = "value")
public class Value implements Deserializable{

    private String type;
    private String dateTime;
    private long unixTime;
    private String value;

    public Value(){}

    public Value(String type, String dateTime, long unixTime, String value){
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
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }


    public long getUnixTime() {
        return unixTime;
    }

    @XmlAttribute
    public void setUnixTime(long unixTime) {
        this.unixTime = unixTime;
    }


    public String getValue() {
        return value;
    }

    @XmlValue
    public void setValue(String value) {
        this.value = value;
    }


    public Model serialize () {

        RDFDatatype datatype = TypeMapper.getInstance().getTypeByName(type);

        Literal createdValue = ResourceFactory.createTypedLiteral(DatatypeConverter.parseDateTime(dateTime).getTime());
        Literal dataValue = ResourceFactory.createTypedLiteral(value, datatype);

        Model model = ModelFactory.createDefaultModel();
        Resource subject = model.createResource();

        model.setNsPrefix("dct", NS.DCT)
                .setNsPrefix("odf", NS.ODF)
                .setNsPrefix("rdf", NS.RDF);

        subject.addProperty(RDF.type, ResourceFactory.createResource(NS.ODF + "Value"))
                .addProperty(ResourceFactory.createProperty(NS.DCT + "created"), createdValue)
                .addProperty(ResourceFactory.createProperty(NS.ODF + "dataValue"), dataValue);

        return model;
    }



    @Override
    public Value deserialize (Resource subject, Collection<Statement> statements) {
        Value valueClass = new Value();

        for (Statement statement : statements) {

            Property property = statement.getPredicate();
            Resource object = ResourceFactory.createResource(statement.getObject().toString());

            if (subject.equals(statement.getSubject())) {

                if (property.toString().contains("type")){
                    valueClass.setType(object.toString());
                }

                if (property.toString().contains("created")) {
                    valueClass.setDateTime(object.toString());
                }

                if (property.toString().contains("dataValue")) {
                    valueClass.setValue(object.toString());
                }
            }
        }

        return valueClass;
    }
}
