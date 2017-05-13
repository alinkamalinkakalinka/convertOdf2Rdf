package model;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import vocabs.NS;

import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.namespace.QName;
import java.lang.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static utils.ModelHelper.getOtherAttributesModel;

/**
 * Created by aarunova on 3/29/17.
 */

@XmlRootElement (name = "description")
public class Description implements Deserializable, Serializable{

    private String description;
    private String lang;
    private Map<QName, String> otherAttributes = new HashMap<QName, String>();

    public Description() {}

    public Description(String description,
                       String lang,
                       Map<QName, String> otherAttributes) {
        this.description = description;
        this.lang = lang;
        this.otherAttributes = otherAttributes;
    }

    public String getDescription() {
        return description;
    }

    @XmlValue
    public void setDescription(String description) {
        this.description = description;
    }

    public String getLang() {
        return lang;
    }

    @XmlAttribute (name = "lang")
    public void setLang(String lang) {
        this.lang = lang;
    }

    public Map<QName, String> getOtherAttributes() {
        return otherAttributes;
    }

    @XmlAnyAttribute
    public void setOtherAttributes(Map<QName, String> otherAttributes) {
        this.otherAttributes = otherAttributes;
    }

    public Model serialize (){
        return serialize(null, null);
    }

    @Override
    public Model serialize(String objectBaseIri, String infoItemBaseIri) {

        Model model = ModelFactory.createDefaultModel();
        Resource subject = model.createResource();

        model.setNsPrefix("rdf", NS.RDF);
        subject.addProperty(RDF.type, ResourceFactory.createResource(NS.ODF + "Description"));

        if (getDescription() != null) {
            model.setNsPrefix("dct", NS.DCT);
            subject.addProperty(ResourceFactory.createProperty(NS.DCT, "description"),
                    ResourceFactory.createLangLiteral(getDescription(), getLang()));
        }

        //OTHER ATTRIBUTES
        if (getOtherAttributes() != null) {
            Model otherAttributesModel = getOtherAttributesModel(subject, getOtherAttributes());
            model.add(otherAttributesModel);
        }

        return model;
    }

    @Override
    public Description deserialize(Resource subject, Collection<Statement> statements) {

        Description descriptionClass = new Description();

        for (Statement statement : statements) {

            Property property = statement.getPredicate();
            Resource object = ResourceFactory.createResource(statement.getObject().toString());

            if (subject.toString().equals(statement.getSubject().toString())) {

                if (property.toString().contains("description")) {
                    descriptionClass.setDescription(object.toString());
                }

                if (property.toString().contains("language")) {
                    descriptionClass.setLang(object.toString());
                }
            }
        }

        return descriptionClass;
    }

    //TODO: descriptionValue or just value??? namespace
}
