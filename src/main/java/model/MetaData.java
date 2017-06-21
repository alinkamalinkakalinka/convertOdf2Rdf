package model;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import vocabs.NS;
import vocabs.ODFClass;
import vocabs.ODFProp;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by aarunova on 12/11/16.
 */

@XmlRootElement
public class MetaData extends ModelGenerator implements Deserializable, Serializable {

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

    public Model serialize (String infoItemBaseIri) {
        return serialize(null, infoItemBaseIri);
    }

    @Override
    public Model serialize (String objectBaseIri, String infoItemBaseIri) {

        Model model = ModelFactory.createDefaultModel();
        Resource subject = model.createResource();

        Object object = new Object();

        model.setNsPrefix("rdf", NS.RDF);
        model.setNsPrefix("odf", NS.ODF);

        subject.addProperty(RDF.type, ResourceFactory.createResource(NS.ODF + ODFClass.METADATA));

        if (getInfoItems() != null) {
            Collection<Model> infoItemModels = getInfoItemModels(getInfoItems(), infoItemBaseIri, subject);
            infoItemModels.forEach(infoItemModel -> model.add(infoItemModel));
        }

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

            if (subject.toString().equals(statement.getSubject().toString())) {

                if (property.toString().contains(ODFProp.INFOITEM)) {
                    infoitems.add(infoItemClass.deserialize(object, statements));
                }
            }
        }

        metaDataClass.setInfoItems(infoitems);

        return metaDataClass;
    }
}
