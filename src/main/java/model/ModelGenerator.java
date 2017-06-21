package model;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

import utils.Loggable;
import utils.ModelHelper;
import vocabs.NS;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by aarunova on 3/29/17.
 */

public abstract class ModelGenerator implements Loggable{


    //DESCRIPTION MODEL
    protected Model getDescriptionModel(Description description,
                                        Resource subject) {

        Model descriptionModel = ModelFactory.createDefaultModel();
        descriptionModel.add(description.serialize());
        addConnectingProperty(subject, descriptionModel, "Description", "description");

        return descriptionModel;
    }

    //INFOITEM MODEL
    protected Collection<Model> getInfoItemModels (Collection<InfoItem> infoItems,
                                                   String infoItemBaseIri,
                                                   Resource subject) {

        Collection<Model> infoItemModels = new HashSet<>();
        String objRelatedInfoItemBaseIri = infoItemBaseIri;

        infoItems.forEach(infoItem -> infoItemModels.add(infoItem.serialize(objRelatedInfoItemBaseIri)));

        for (Model infoItemModel : infoItemModels) {
            addConnectingProperty(subject, infoItemModel, "InfoItem", "infoItem");
        }

        /*infoItemModels.forEach(infoItemModel -> {
            addConnectingProperty(subject, infoItemModel, "InfoItem", "infoItem");
        });*/

        return infoItemModels;
    }

    //OBJECT MODEL
    protected Collection<Model> getNestedObjectsModels (Resource subject,
                                                     Collection<Object> objects,
                                                     String infoItemBaseIri,
                                                     String id) {

        Collection<Model> nestedObjectsModels = new HashSet<>();
        String nestedObjectsBaseIri = subject.toString() + "/";
        String objRelatedInfoItemBaseIri = infoItemBaseIri + id + "/";

        for (Object nestedObject : objects) {
            Model objectModel = nestedObject.serialize(nestedObjectsBaseIri, objRelatedInfoItemBaseIri);
            nestedObjectsModels.add(objectModel);
        }

        /*objects.forEach(nestedObject -> nestedObjectsModels
                .add(nestedObject.serialize(nestedObjectsBaseIri, objRelatedInfoItemBaseIri)));*/

        /*nestedObjectsModels.forEach(nestedObjectsModel -> {
            addConnectingProperty(subject, nestedObjectsModel, "Object", "object");
        });*/

        for (Model nestedObjectModel : nestedObjectsModels) {
            addConnectingProperty(subject, nestedObjectModel, "Object", "object");
        }

        return nestedObjectsModels;
    }

    //ID MODEL
    protected Collection<Model> getIdModels(Resource subject,
                                            Collection<QlmID> ids,
                                            String propertyName) {
        Collection<Model> idModels = new HashSet<>();
        ids.forEach(nameValue -> idModels.add(nameValue.serialize()));

        idModels.forEach(idModel -> {
            addConnectingProperty(subject, idModel, "QlmID", propertyName);
            Resource idValue = ModelHelper.getIdConnectWith(idModel, "QlmID", "id");
            if (idValue != null) {
                subject.addProperty(ResourceFactory.createProperty(NS.ODF, propertyName), idValue);
            }
        });

        return idModels;
    }

    //VALUE MODEL
    protected Collection<Model> getValueModels(Resource subject,
                                               Collection<Value> values){
        Collection<Model> valueModels = new HashSet<>();
        values.forEach(value -> valueModels.add(value.serialize()));

        valueModels.forEach(valueModel -> {
            addConnectingProperty(subject, valueModel, "Value", "value");
        });

        return valueModels;
    }

    //METADATA MODEL
    protected Collection<Model> getMetaDataModels(Resource subject,
                                              Collection<MetaData> metaDatas) {
        Collection<Model> metaDataModels = new HashSet<>();
        String metaDataUri = subject + "/" + metaDatas.hashCode() + "/";
        metaDatas.forEach(metaDataValue -> metaDataModels.add(metaDataValue.serialize(metaDataUri)));

        metaDataModels.forEach(metaDataModel -> {
            addConnectingProperty(subject, metaDataModel, "MetaData", "metadata");
        });

        return metaDataModels;
    }


    protected void addConnectingProperty (Resource subject,
                                          Model model,
                                          String classTypeName,
                                          String propertyName) {

        Resource connectingValueId = ModelHelper.getIdConnectWith(model, classTypeName, propertyName);
        if (connectingValueId != null) {
            subject.addProperty(ResourceFactory.createProperty(NS.ODF + propertyName), connectingValueId);
        }
    }

}


