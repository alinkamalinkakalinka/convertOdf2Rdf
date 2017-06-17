package model;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import utils.ModelHelper;
import utils.RegexHelper;
import vocabs.NS;
import vocabs.ODFClass;
import vocabs.ODFProp;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.*;
import javax.xml.namespace.QName;
import java.util.*;

import static org.apache.jena.datatypes.xsd.XSDDatatype.XSDdateTime;
import static utils.ModelHelper.getOtherAttributesModel;

/**
 * Created by aarunova on 12/11/16.
 */

public class QlmID implements Deserializable, Serializable{

    private String id;
    private String tagType;
    private String startDate;
    private String endDate;
    private String idType;
    private Map<QName, String> otherAttributes = new HashMap<QName, String>();

    public QlmID(String id,
                 String tagType,
                 String startDate,
                 String endDate,
                 String idType, Map<QName, String> otherAttributes) {
        this.id = id;
        this.tagType = tagType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.idType = idType;
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

    public String getIdType() {
        return idType;
    }

    @XmlAttribute (name = "idType")
    public void setIdType(String idType) {
        this.idType = idType;
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
        elementsAndAttributes.put(NS.TIME + ODFProp.STARTDATE, getStartDate());
        elementsAndAttributes.put(NS.TIME + ODFProp.ENDDATE, getEndDate());


        if (getId() != null) {
            model.setNsPrefix("odf", NS.ODF);
            subject.addProperty(ResourceFactory.createProperty(NS.ODF, ODFProp.IDVALUE), getId());
        }

        if (getTagType() != null) {
            model.setNsPrefix("dct", NS.DCT);
            subject.addProperty(ResourceFactory.createProperty(NS.DCT, ODFProp.TAGTYPE), getTagType());
        }

        if (getIdType() != null) {
            model.setNsPrefix("odf", NS.ODF);
            subject.addProperty(ResourceFactory.createProperty(NS.ODF, ODFProp.IDTYPE), getIdType());
        }

        for (Map.Entry<String, String> entry : elementsAndAttributes.entrySet()) {
            if (entry.getValue() != null) {
                model.setNsPrefix("time", NS.TIME);
                Literal dateValue = ResourceFactory.createTypedLiteral(entry.getValue(), XSDDatatype.XSDdateTime);
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

                if (property.toString().contains(ODFProp.IDVALUE)) {
                    qlmIDClass.setId(object.toString());
                }

                if (property.toString().contains(ODFProp.IDTYPE)) {
                    qlmIDClass.setIdType(object.toString());
                }

                if (property.toString().contains(ODFProp.TAGTYPE)) {
                    qlmIDClass.setTagType(object.toString());
                }

                if (property.toString().contains(ODFProp.STARTDATE)) {
                    qlmIDClass.setStartDate(object.toString());
                }

                if (property.toString().contains(ODFProp.ENDDATE)) {
                    qlmIDClass.setStartDate(object.toString());
                }

                if (ModelHelper.ifOptionalProperty(property, ODFProp.idProperties)) {
                    Map<QName, String> optionalAttribute = new HashMap<>();
                    optionalAttribute.put(QName.valueOf(RegexHelper.getOptionalProperty(property.toString())), object.toString());
                    qlmIDClass.setOtherAttributes(optionalAttribute);
                }
            }
        }

        return qlmIDClass;
    }

}
