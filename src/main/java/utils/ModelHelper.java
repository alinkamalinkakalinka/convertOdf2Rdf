package utils;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import vocabs.NS;

/**
 * Created by aarunova on 3/27/17.
 */
public class ModelHelper {

    public static Resource getIdToConnectWith (Model model, String classType) {

        if (model != null && model != null) {

            Resource id = null;
            StmtIterator iterator = model.listStatements();
            while (iterator.hasNext()) {
                Statement stmt = iterator.nextStatement();
                if (stmt.getObject().toString().contains(classType)) {
                    id = stmt.getSubject();
                }
            }

            if (id.toString().contains("readable")) {
                System.out.println("catch2");
            }

            return id;
        }

        return null;
    }

    public static Resource getIdConnectWith (Model model, String classType, String propertyType) {

        if (model != null && model != null) {

            Resource id = null;
            HashMap<String, Resource> potentialIds = new HashMap<>();

            StmtIterator iterator = model.listStatements();

            while (iterator.hasNext()) {
                Statement stmt = iterator.nextStatement();

                if (stmt.getObject().toString().contains(classType)) {
                    potentialIds.put(stmt.getSubject().toString(), stmt.getSubject());
                }
            }

            StmtIterator iterator2 = model.listStatements();

            while (iterator2.hasNext()) {
                Statement stmt = iterator2.nextStatement();

                if (potentialIds.containsKey(stmt.getObject().toString()) && stmt.getPredicate().toString().contains(propertyType)) {
                    potentialIds.remove(stmt.getObject().toString());
                }
            }

            for (Map.Entry<String, Resource> entry : potentialIds.entrySet()) {
                id = entry.getValue();
            }

            return id;
        }

        return null;

    }

    public static Model getOtherAttributesModel (Resource subject, Map<QName, String> attributesMap) {

        Model otherAttributesModel = ModelFactory.createDefaultModel();
        otherAttributesModel.setNsPrefix("ca", NS.ATTR);

        for (Map.Entry<QName, String> entry : attributesMap.entrySet()) {
            otherAttributesModel.add(subject, ResourceFactory.createProperty(NS.ATTR + entry.getKey()), entry.getValue());
        }

        return otherAttributesModel;
    }
}
