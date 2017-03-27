package modelXml;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import vocabs.NS;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by aarunova on 12/11/16.
 */

@XmlRootElement(name = "id")
public class QlmID implements Deserializable{

    private String id;
    private String tagType;
    private String startDate;
    private String endDate;

    public QlmID(String id, String tagType, String startDate, String endDate) {
        this.id = id;
        this.tagType = tagType;
        this.startDate = startDate;
        this.endDate = endDate;
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
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }


    public String getEndDate() {
        return endDate;
    }

    @XmlAttribute (name = "endDate")
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }


    public Model serialize() {

        Model model = ModelFactory.createDefaultModel();
        Resource subject = model.createResource();

        model.setNsPrefix("rdf", NS.RDF);
        subject.addProperty(RDF.type, ResourceFactory.createResource(NS.ODF + "QlmID"));

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
            //TODO: namespace ???
            model.setNsPrefix("dct", NS.DCT);
            subject.addProperty(ResourceFactory.createProperty(NS.DCT, "tagType"), getTagType());
        }

        for (Map.Entry<String, String> entry : elementsAndAttributes.entrySet()) {
            if (entry.getValue() != null) {
                model.setNsPrefix("time", NS.TIME);
                //TODO: dateTime xsd type???
                Literal dateValue = ResourceFactory.createTypedLiteral(DatatypeConverter.parseDateTime(entry.getValue()).getTime());
                subject.addProperty(ResourceFactory.createProperty(entry.getKey()), dateValue);
            }
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
            }
        }

        //TODO: no tagType

        return qlmIDClass;
    }
}
