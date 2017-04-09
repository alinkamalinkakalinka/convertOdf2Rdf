package model;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import utils.ModelHelper;
import vocabs.NS;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by aarunova on 3/29/17.
 */

public abstract class ModelGenerator {

    protected Model getDescriptionModel(Description description,
                                        Resource subject) {

        Model descriptionModel = ModelFactory.createDefaultModel();
        descriptionModel.add(description.serialize());
        addConnectingProperty(subject, descriptionModel, "Description", "description");

        return descriptionModel;
    }

    protected Collection<Model> getInfoItemModels (Collection<InfoItem> infoItems,
                                                   String infoItemBaseIri,
                                                   String id,
                                                   Resource subject) {

        Collection<Model> infoItemModels = new HashSet<>();
        String objRelatedInfoItemBaseIri = infoItemBaseIri + id + "/";

        infoItems.forEach(infoItem -> infoItemModels.add(infoItem.serialize(objRelatedInfoItemBaseIri)));
        infoItemModels.forEach(infoItemModel -> {
            addConnectingProperty(subject, infoItemModel, "InfoItem", "infoItem");
        });

        return infoItemModels;
    }

    protected Collection<Model> getNestedObjectsModels (Resource subject,
                                                     Collection<Object> objects,
                                                     String infoItemBaseIri,
                                                     String id) {

        Collection<Model> nestedObjectsModels = new HashSet<>();
        String nestedObjectsBaseIri = subject.toString() + "/";
        String objRelatedInfoItemBaseIri = infoItemBaseIri + id + "/";

        objects.forEach(nestedObject -> nestedObjectsModels
                .add(nestedObject.serialize(nestedObjectsBaseIri, objRelatedInfoItemBaseIri)));

        nestedObjectsModels.forEach(nestedObjectsModel -> {
            addConnectingProperty(subject, nestedObjectsModel, "Object", "object");
        });

        return nestedObjectsModels;
    }

    protected Collection<Model> getIdModels(Resource subject,
                                            Collection<QlmID> ids,
                                            String propertyName) {
        Collection<Model> idModels = new HashSet<>();
        ids.forEach(nameValue -> idModels.add(nameValue.serialize()));

        idModels.forEach(idModel -> {
            addConnectingProperty(subject, idModel, "QlmID", propertyName);
            Resource idValue = ModelHelper.getIdToConnectWith(idModel, "QlmID");
            if (idValue != null) {
                subject.addProperty(ResourceFactory.createProperty(NS.ODF, propertyName), idValue);
            }
        });

        return idModels;
    }

    protected Collection<Model> getValueModels(Resource subject,
                                               Collection<Value> values){
        Collection<Model> valueModels = new HashSet<>();
        values.forEach(value -> valueModels.add(value.serialize()));

        valueModels.forEach(valueModel -> {
            addConnectingProperty(subject, valueModel, "Value", "value");
        });

        return valueModels;
    }

    protected Collection<Model> getMetaDataModels(Resource subject,
                                              Collection<MetaData> metaDatas) {
        Collection<Model> metaDataModels = new HashSet<>();
        metaDatas.forEach(metaDataValue -> metaDataModels.add(metaDataValue.serialize(subject + "/")));

        metaDataModels.forEach(metaDataModel -> {
            addConnectingProperty(subject, metaDataModel, "MetaData", "metadata");
        });

        return metaDataModels;
    }

    protected void addConnectingProperty (Resource subject,
                                          Model model,
                                          String classTypeName,
                                          String propertyName) {

        Resource connectingValueId = ModelHelper.getIdToConnectWith(model, classTypeName);
        if (connectingValueId != null) {
            subject.addProperty(ResourceFactory.createProperty(NS.ODF + propertyName), connectingValueId);
        }
    }

}


