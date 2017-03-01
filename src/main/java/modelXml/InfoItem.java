package modelXml;

import com.complexible.pinto.annotations.RdfsClass;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import vocabs.NS;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Created by aarunova on 12/11/16.
 */

//@RdfsClass("odf:InfoItem")
@XmlRootElement (name = "InfoItem")
public class InfoItem {

    private String name1;
    private String name;
    private String udef;
    private Collection<Value> values = new ArrayList<>();
    private String description;
    private Collection<MetaData> metaData = new ArrayList<>();

    public InfoItem(){}

    public InfoItem(String name, String name1, String udef, List<Value> values, String description, Collection<MetaData> metaData){
        this.name = name;
        this.name = name1;
        this.udef = udef;
        this.values = values;
        this.description = description;
        this.metaData = metaData;
    }

    public String getUdef() {
        return udef;
    }

    @XmlAttribute (name = "udef")
    public void setUdef(String udef) {
        this.udef = udef;
    }


    public Collection<Value> getValues() {
        return values;
    }

    @XmlElement (name = "values")
    public void setValues(Collection<Value> values) {
        this.values = values;
    }


    public String getDescription() {
        return description;
    }

    @XmlElement (name = "description")
    public void setDescription(String description) {
        this.description = description;
    }


    public Collection<MetaData> getMetaData() {
        return metaData;
    }

    @XmlElement (name = "metaData")
    public void setMetaData(Collection<MetaData> metaData) {
        this.metaData = metaData;
    }


    public String getName() {
        return name;
    }

    @XmlAttribute (name = "name")
    public void setName(String name) {
        this.name = name;
    }


    public String getName1() {
        return name1;
    }

    @XmlElement (name = "name")
    public void setName1(String name1) {
        this.name1 = name1;
    }


    public Model serialize(ValueFactory vf, String infoItemBaseIri) {
        IRI subject = vf.createIRI(infoItemBaseIri + name);

        ModelBuilder builder = new ModelBuilder();
        builder.setNamespace("dct", NS.DCT)
                .setNamespace("odf", NS.ODF)
                .setNamespace("rdf", RDF.NAMESPACE)

                .subject(subject)
                .add("rdf:type", "odf:InfoItem")
                .add("dct:title", name);


        if (description != null){
            builder.add("dct:description", description);
        }

        Collection<Model> valueModels = new HashSet<>();
        values.forEach(value -> valueModels.add(value.serialize(vf)));

        valueModels.forEach(model -> {
            Resource valueId = model.iterator().next().getSubject();
            builder.add("odf:values", valueId);
        });

        Collection<Model> metaDataModels = new HashSet<>();
        metaData.forEach(md -> metaDataModels.add(md.serialize(vf, subject + "/")));

        metaDataModels.forEach(model -> {
            Resource metadataId = model.iterator().next().getSubject();
            builder.add("odf:metadata", metadataId);
        });

        Model infoItemModel = builder.build();
        valueModels.forEach(valueModel -> infoItemModel.addAll(valueModel));
        metaDataModels.forEach(metadataModel -> infoItemModel.addAll(metadataModel));

        return infoItemModel;
    }
}
