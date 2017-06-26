package evaluation;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Statement;
import org.junit.Test;
import utils.ModelHelper;

import java.util.Collection;

/**
 * Created by Alina on 6/5/2017.
 */
public class SPARQLTest {

    @Test
    public void sparqlTest() {

        String fileName1 = "src/test/resources/test/input/rdf/metadataSparql1.ttl";
        String fileName2 = "src/test/resources/test/input/rdf/metadataSparql2.ttl";
        String fileName3 = "src/test/resources/test/input/rdf/metadataSparql3.ttl";

        Collection<Statement> statements1 = ModelHelper.getStatementsFromFile(fileName1, "TTL");
        Collection<Statement> statements2 = ModelHelper.getStatementsFromFile(fileName2, "TTL");
        Collection<Statement> statements3 = ModelHelper.getStatementsFromFile(fileName3, "TTL");

        Model model = ModelFactory.createDefaultModel();

        for (Statement statement1 : statements1) {
            model.add(statement1);
        }

        for (Statement statement2 : statements2) {
            model.add(statement2);
        }

        for (Statement statement3 : statements3) {
            model.add(statement3);
        }


        String queryString = "PREFIX dct: <http://purl.org/dc/terms/>" +
                "PREFIX odf: <http://eis-biotope.iais.fraunhofer.de/odf#>" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
                "SELECT ?device \n" +
                "WHERE" +
                "  {" +
                "   ?object rdf:type odf:Object." +
                "   ?object odf:id ?id." +
                "   ?id odf:idValue ?device." +
                "   ?object odf:infoItem ?infoItem." +
                "   ?infoItem dct:name \"PowerConsumption\"." +
                "   ?infoItem odf:value ?value." +
                "   ?value odf:dataValue ?consumption." +
                "  }" +
                "ORDER BY DESC(?consumption) LIMIT 1";

        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        ResultSet results = qexec.execSelect();
        while (results.hasNext()) {
            QuerySolution soln = results.nextSolution();
            System.out.println(soln);
        }
    }
}
