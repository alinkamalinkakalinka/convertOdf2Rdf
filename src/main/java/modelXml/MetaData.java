package modelXml;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import utils.ModelHelper;
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

        model.setNsPrefix("rdf", NS.RDF);

        subject.addProperty(RDF.type, ResourceFactory.createResource(NS.ODF + "MetaData"));

        if (getInfoItems() != null) {
            model.setNsPrefix("odf", NS.ODF);

            Collection<Model> infoItemModels = new HashSet<>();
            getInfoItems().forEach(infoitem -> infoItemModels.add(infoitem.serialize(baseIri)));


            infoItemModels.forEach(infoItemModel -> {
                Resource infoItemId = ModelHelper.getIdToConnectWith(infoItemModel, "InfoItem");
                if (infoItemId != null) {
                    subject.addProperty(ResourceFactory.createProperty(NS.ODF + "infoitem"), infoItemId);
                }
            });

            infoItemModels.forEach(metadataModel -> model.add(metadataModel));
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
