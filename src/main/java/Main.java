/**
 * Created by aarunova on 12/11/16.
 */

public class Main {

    public static void main(String[] args) {

        if (args.length < 4) {
            System.out.println("Enter all required parameters: 1) odf2rdf or rdf2odf, 2) input filepath, 3) output filepath and 4) rdf format of input or output file");
            System.exit(0);
        }

        String converter = args[0];
        String inputFilepath = args[1];
        String outputFilepath = args[2];
        String rdfFormat = args[3];

        if ("odf2rdf".equals(converter)) {
            Serializer serializer = new Serializer();
            serializer.serialize(inputFilepath, outputFilepath, rdfFormat);
        } else if ("rdf2odf".equals(converter)) {
            Deserializer deserializer = new Deserializer();
            deserializer.deserialize(inputFilepath, outputFilepath, rdfFormat);
        } else {
            System.exit(0);
        }

    }


}
