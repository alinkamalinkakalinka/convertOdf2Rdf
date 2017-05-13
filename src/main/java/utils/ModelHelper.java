package utils;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

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
            ArrayList<Resource> potentialIds = new ArrayList<>();

            StmtIterator iterator = model.listStatements();

            while (iterator.hasNext()) {
                Statement stmt = iterator.nextStatement();

                if (stmt.getObject().toString().contains(classType)) {
                    potentialIds.add(stmt.getSubject());
                }
            }

            while (iterator.hasNext()) {
                Statement stmt = iterator.nextStatement();

                if (potentialIds.contains(stmt.getObject()) && stmt.getPredicate().toString().contains(propertyType)) {
                    potentialIds.remove(stmt.getObject());
                }
            }

            id = potentialIds.get(0);

            return id;
        }

        return null;

    }
}
