package modelXml;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import vocabs.NS;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * Created by aarunova on 12/11/16.
 */

@XmlRootElement
public class MetaData implements Deserializable{

    private Collection<InfoItem> infoItems = new ArrayList<>();

    public MetaData(){}

    public MetaData(Collection<InfoItem> infoItems) {
        super();
        this.infoItems = infoItems;
    }

    public Collection<InfoItem> getInfoItems() {
        return infoItems;
    }

    @XmlElement (name = "InfoItem")
    public void setInfoItems(Collection<InfoItem> infoItems) {
        this.infoItems = infoItems;
    }


    public Model serialize (String baseIri) {

        Model model = ModelFactory.createDefaultModel();

        Resource subject = model.createResource();

        model.setNsPrefix("dct", NS.DCT)
                .setNsPrefix("odf", NS.ODF)
                .setNsPrefix("rdf", NS.RDF);

        subject.addProperty(RDF.type, ResourceFactory.createResource(NS.ODF + "MetaData"));

        Collection<Model> infoItemModels = new HashSet<>();
        infoItems.forEach(infoitem -> infoItemModels.add(infoitem.serialize(baseIri)));

        return model;
    }

    @Override
    public MetaData deserialize (Resource subject, Collection<Statement> statements) {

        MetaData metaDataClass = new MetaData();
        InfoItem infoItemClass = new InfoItem();

        Collection<InfoItem> infoitems = new ArrayList<>();

        for (Statement statement : statements) {

            Property property = statement.getPredicate();
            Resource object = ResourceFactory.createResource(statement.getObject().toString());

            if (subject.equals(statement.getSubject())) {

                if (property.toString().contains("infoitem")) {
                    infoitems.add(infoItemClass.deserialize(object, statements));
                }
            }
        }

        metaDataClass.setInfoItems(infoitems);

        return metaDataClass;
    }
}
