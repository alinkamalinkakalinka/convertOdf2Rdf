package utils;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

/**
 * Created by aarunova on 3/27/17.
 */
public class ModelHelper {

    public static Resource getIdToConnectWith (Model model, String modelType) {

        if (model != null && model != null) {

            Resource id = null;
            StmtIterator iterator = model.listStatements();
            while (iterator.hasNext()) {
                Statement stmt = iterator.nextStatement();
                if (stmt.getObject().toString().contains(modelType)) {
                    id = stmt.getSubject();
                }
            }

            return id;
        }

        return null;
    }
}
