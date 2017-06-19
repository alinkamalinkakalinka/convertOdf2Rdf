package evaluation;

import org.apache.jena.ext.com.google.common.util.concurrent.Service;
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

        String fileName1 = "test.ttl";
        String fileName2 = "test.ttl";

        Collection<Statement> statements1 = ModelHelper.getStatementsFromFile(fileName1);
        Collection<Statement> statements2 = ModelHelper.getStatementsFromFile(fileName2);

        Model model = ModelFactory.createDefaultModel();

        for (Statement statement1 : statements1) {
            model.add(statement1);
        }

        for (Statement statement2 : statements2) {
            model.add(statement2);
        }

        String queryString = "SELECT ?x \n" +
                "  {\n" +
                "     ?x  a ?y .\n" +
                "  }";

        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        ResultSet results = qexec.execSelect();
        while (results.hasNext()) {
            QuerySolution soln = results.nextSolution();
            System.out.println(soln);
        }
    }
}
