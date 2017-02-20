/**
 * Created by aarunova on 12/11/16.
 */

import com.complexible.pinto.RDFMapper;
//import modelRdf.ObjectsRdf;
import modelXml.Objects;
import org.openrdf.model.Model;
import tr.com.srdc.ontmalizer.XSD2OWLMapper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URISyntaxException;

public class Main {


    private final Helper helper = new Helper();

    public static void main(String[] args) throws FileNotFoundException {

        try {

            Helper helper = new Helper();

            String fileName = "infoitem_values.xml";

            File file = helper.getFile(fileName);

            JAXBContext jaxbContext = JAXBContext.newInstance(Objects.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Objects objects= (Objects) jaxbUnmarshaller.unmarshal(file);

            Model aGraph = RDFMapper.builder()
                    .build().writeValue(objects);

            ModelModifier modelModifier = new ModelModifier();
            ModelWraper modelWraper = new ModelWraper();

            Model aGraph2 = modelModifier.modifyModel(aGraph);

            aGraph2.subjects();
            System.out.println(aGraph2.subjects());

            //ObjectsRdf objects1 = RDFMapper.create().readValue(aGraph,
                    //ObjectsRdf.class,
                    //SimpleValueFactory.getInstance().createIRI("ex:UniqueTargetID_1"));

            modelWraper.wrapModel(aGraph);



        } catch (JAXBException e) {
            e.printStackTrace();
        }


    }


}
