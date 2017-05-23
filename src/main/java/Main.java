/**
 * Created by aarunova on 12/11/16.
 */

public class Main {

    public static void main(String[] args) {

        if (args.length < 3) {
            System.out.println("Enter all required parameters: odf2rdf or rdf2odf, input filepath and output filepath");
            System.exit(0);
        }

        String converter = args[0];
        String inputFilepath = args[1];
        String outputFilepath = args[2];

        if ("odf2rdf".equals(converter)) {
            Serializer serializer = new Serializer();
            serializer.serialize(inputFilepath, outputFilepath);
        } else if ("rdf2odf".equals(converter)) {
            Deserializer deserializer = new Deserializer();
            deserializer.deserialize(inputFilepath, outputFilepath);
        } else {
            System.exit(0);
        }

    }


}
