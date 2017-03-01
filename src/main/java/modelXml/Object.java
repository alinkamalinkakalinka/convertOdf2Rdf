package modelXml;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
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

//@RdfsClass("odf:Object")
@XmlRootElement (name = "Object")
public class Object {

    private String id;
    private String type;
    private String udef;
    private String description;
    private Collection<InfoItem> infoItems = new ArrayList<>();
    private Collection<Object> objects = new ArrayList<>();

    public Object(String id, String type, String udef, String description, List<InfoItem> infoItems, Collection<Object> objects){
        this.id = id;
        this.type = type;
        this.udef = udef;
        this.description = description;
        this.infoItems = infoItems;
        this.objects = objects;
    }

    public Object(){}


    public String getType() {
        return type;
    }

    @XmlAttribute
    public void setType(String type) {
        this.type = type;
    }


    public Collection<InfoItem> getInfoItems() {
        return infoItems;
    }

    @XmlElement (name = "InfoItem")
    public void setInfoItems(Collection<InfoItem> infoItems) {
        this.infoItems = infoItems;
    }


    public Collection<Object> getObjects() {
        return objects;
    }

    @XmlElement (name = "Object")
    public void setObjects(Collection<Object> objects) {
        this.objects = objects;
    }


    public String getUdef() {
        return udef;
    }

    @XmlAttribute (name = "udef")
    public void setUdef(String udef) {
        this.udef = udef;
    }


    public String getDescription() {
        return description;
    }

    @XmlElement (name = "description")
    public void setDescription(String description) {
        this.description = description;
    }


    public String getId() { return id; }

    @XmlElement (name = "id")
    public void setId(String id) {
        this.id = id;
    }


    public Model serialize(ValueFactory vf, String objectBaseIri, String infoItemBaseIri) {

        IRI subject = vf.createIRI(objectBaseIri + id);

        ModelBuilder builder = new ModelBuilder();
        builder.setNamespace("dct", NS.DCT)
                .setNamespace("odf", NS.ODF)
                .setNamespace("rdf", RDF.NAMESPACE)

                .subject(subject)
                .add("rdf:type", "odf:Object")
                .add("skos:notation", id);

        Collection<Model> infoItemModels = new HashSet<>();
        String objRelatedInfoItemBaseIri = infoItemBaseIri + id + "/";
        infoItems.forEach(infoitem -> infoItemModels.add(infoitem.serialize(vf, objRelatedInfoItemBaseIri)));

        Collection<Model> nestedObjectsModels = new HashSet<>();
        String nestedObjectsBaseIri = subject.toString() + "/";
        objects.forEach(object -> nestedObjectsModels.add(object.serialize(vf, nestedObjectsBaseIri, objRelatedInfoItemBaseIri)));

        infoItemModels.forEach(model -> {
            builder.add("odf:infoitem", model.iterator().next().getSubject());
        });

        nestedObjectsModels.forEach(model -> {
            builder.add("odf:object", model.iterator().next().getSubject());
        });

        Model objectModel = builder.build();
        infoItemModels.forEach(infoItemModel -> objectModel.addAll(infoItemModel));
        nestedObjectsModels.forEach(nestedObjectsModel -> objectModel.addAll(nestedObjectsModel));

        return objectModel;
    }

}
