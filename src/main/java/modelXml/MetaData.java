package modelXml;

import com.complexible.pinto.annotations.RdfId;
import com.complexible.pinto.annotations.RdfsClass;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;
import org.eclipse.rdf4j.model.BNode;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import vocabs.NS;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Created by aarunova on 12/11/16.
 */

//@RdfsClass("odf:MetaData")
@XmlRootElement
public class MetaData {

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

    public Model serialize(ValueFactory vf, String baseIri) {
        BNode subject = vf.createBNode();

        ModelBuilder builder = new ModelBuilder();
        builder.setNamespace("dct", NS.DCT)
                .setNamespace("odf", NS.ODF)
                .setNamespace("rdf", RDF.NAMESPACE)

                .subject(subject)
                .add("rdf:type", "odf:MetaData");

        Collection<Model> infoItemModels = new HashSet<>();
        infoItems.forEach(infoitem -> infoItemModels.add(infoitem.serialize(vf, baseIri)));

        infoItemModels.forEach(model -> {
            builder.add("odf:infoitem", model.iterator().next().getSubject());
        });

        Model metadataModel = builder.build();
        infoItemModels.forEach(infoItemModel -> metadataModel.addAll(infoItemModel));

        return metadataModel;
    }

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
