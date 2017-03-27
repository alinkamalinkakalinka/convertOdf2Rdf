package modelXml;

import org.apache.jena.rdf.model.*;
import org.eclipse.rdf4j.model.BNode;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.RDF;
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

    public Model serialize(ValueFactory vf) {

        HashMap<String, String> elementsAndAttributes = new HashMap<>();
        elementsAndAttributes.put("time:startDate", startDate);
        elementsAndAttributes.put("time:endDate", endDate);

        BNode subject = vf.createBNode();

        ModelBuilder builder = new ModelBuilder();
        builder.setNamespace("dct", NS.DCT)
                .setNamespace("time", NS.TIME)
                .setNamespace("odf", NS.ODF)
                .setNamespace("rdf", RDF.NAMESPACE);

        builder.subject(subject)
                .add("rdf:type", "odf:QlmID");

        if (id != null) {
            builder.add("odf:idValue", id);
        }

        for (Map.Entry<String, String> entry : elementsAndAttributes.entrySet()) {
            if (entry.getValue() != null) {
                Literal dateValue = vf.createLiteral(DatatypeConverter.parseDateTime(entry.getValue()).getTime());
                builder.add(entry.getKey(), dateValue);
            }
        }

        return builder.build();
    }


    public org.apache.jena.rdf.model.Model serialize() {

        HashMap<String, String> elementsAndAttributes = new HashMap<>();
        elementsAndAttributes.put(NS.TIME + "startDate", startDate);
        elementsAndAttributes.put(NS.TIME + "endDate", endDate);

        org.apache.jena.rdf.model.Model model = ModelFactory.createDefaultModel();

        Resource subject = model.createResource();

        model.setNsPrefix("dct", NS.DCT)
                .setNsPrefix("time", NS.TIME)
                .setNsPrefix("odf", NS.ODF)
                .setNsPrefix("rdf", RDF.NAMESPACE);

        subject.addProperty(org.apache.jena.vocabulary.RDF.type, ResourceFactory.createResource(NS.ODF + "QlmID"));

        if (id != null) {
            subject.addProperty(ResourceFactory.createProperty(NS.ODF, "idValue"), id);
        }

        for (Map.Entry<String, String> entry : elementsAndAttributes.entrySet()) {
            if (entry.getValue() != null) {
                org.apache.jena.rdf.model.Literal dateValue = ResourceFactory.createTypedLiteral(DatatypeConverter.parseDateTime(entry.getValue()).getTime());
                subject.addProperty(ResourceFactory.createProperty(entry.getKey()), dateValue);
            }
        }

        return model;
    }

    @Override
    public QlmID deserialize (org.apache.jena.rdf.model.Resource subject, Collection<Statement> statements) {

        QlmID qlmIDClass = new QlmID();

        for (Statement statement : statements) {

            Property property = statement.getPredicate();
            org.apache.jena.rdf.model.Resource object = ResourceFactory.createResource(statement.getObject().toString());

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

        return qlmIDClass;
    }
}
