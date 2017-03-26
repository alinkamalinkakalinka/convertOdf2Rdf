package modelXml;

import com.complexible.pinto.annotations.RdfsClass;
import org.apache.jena.rdf.model.*;
import org.apache.jena.rdf.model.Statement;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.query.algebra.In;
import vocabs.NS;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;

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

    @XmlElement (name = "value")
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

        HashMap<String, String> elementsAndAttributes = new HashMap<>();
        elementsAndAttributes.put("dct:description", description);
        elementsAndAttributes.put("odf:name", name);
        elementsAndAttributes.put("odf:name1", name1);
        elementsAndAttributes.put("odf:udef", udef);

        ModelBuilder builder = new ModelBuilder();
        builder.setNamespace("dct", NS.DCT)
                .setNamespace("odf", NS.ODF)
                .setNamespace("time", NS.TIME)
                .setNamespace("rdf", RDF.NAMESPACE);

        builder.subject(subject)
                .add("rdf:type", "odf:InfoItem");
                //.add("dct:title", name);

        Model infoItemModel = builder.build();

        for (Map.Entry<String, String> entry : elementsAndAttributes.entrySet()) {
            if (entry.getValue() != null) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }

        /*if (description != null){
            builder.add("dct:description", description);
        }*/

        Collection<Model> valueModels = new HashSet<>();
        //values.forEach(value -> valueModels.add(value.serialize(vf)));
        for(Value value : values) {
            Model valueModel = value.serialize(vf);
            valueModels.add(valueModel);
            for (Namespace namespace : valueModel.getNamespaces()) {
                infoItemModel.setNamespace(namespace.getPrefix(), namespace.getName());
            }
        }




        valueModels.forEach(model -> {
            Resource valueId = model.iterator().next().getSubject();
            builder.add("odf:value", valueId);
        });

        Collection<Model> metaDataModels = new HashSet<>();
        //metaData.forEach(md -> metaDataModels.add(md.serialize(vf, subject + "/")));
        for(MetaData metaDataValue : metaData) {
            Model metaDataModel = metaDataValue.serialize(vf, subject + "/");
            metaDataModels.add(metaDataModel);
            for (Namespace namespace : metaDataModel.getNamespaces()) {
                infoItemModel.setNamespace(namespace.getPrefix(), namespace.getName());
            }
        }


        metaDataModels.forEach(model -> {
            Resource metadataId = model.iterator().next().getSubject();
            builder.add("odf:metadata", metadataId);
        });


        valueModels.forEach(valueModel -> infoItemModel.addAll(valueModel));
        metaDataModels.forEach(metadataModel -> infoItemModel.addAll(metadataModel));

        return infoItemModel;
    }


    public InfoItem deserialize (org.apache.jena.rdf.model.Resource subject, Collection<Statement> statements) {

        InfoItem infoItemClass = new InfoItem();
        Value valueClass = new Value();
        MetaData metaDataClass = new MetaData();

        Collection<Value> values = new ArrayList<>();
        Collection<MetaData> metaDatas = new ArrayList<>();

        for (Statement statement : statements) {

            Property property = statement.getPredicate();
            org.apache.jena.rdf.model.Resource object = ResourceFactory.createResource(statement.getObject().toString());

            if (subject.equals(statement.getSubject())) {

                if (property.toString().contains("name")) {
                    infoItemClass.setName(object.toString());
                }

                //TODO: name1 ????
                if (property.toString().contains("name1")) {
                    infoItemClass.setName1(object.toString());
                }

                if (property.toString().contains("udef")) {
                    infoItemClass.setUdef(object.toString());
                }

                if (property.toString().contains("description")) {
                    infoItemClass.setDescription(object.toString());
                }

                if (property.toString().contains("metadata")) {
                    metaDatas.add(metaDataClass.deserialize(object, statements));
                }

                if (property.toString().contains("value")) {
                    values.add(valueClass.deserialize(object,statements));
                }
            }
        }

        infoItemClass.setValues(values);
        infoItemClass.setMetaData(metaDatas);

        return infoItemClass;
    }
}
