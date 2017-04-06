package model;

import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;

import java.lang.Object;
import java.util.Collection;

/**
 * Created by aarunova on 3/27/17.
 */
public interface Deserializable {

     Object deserialize(Resource subject, Collection<Statement> statements);
}
