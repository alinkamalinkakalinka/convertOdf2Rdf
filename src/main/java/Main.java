/**
 * Created by aarunova on 12/11/16.
 */

import com.complexible.pinto.RDFMapper;
import model.Objects;
import org.openrdf.model.Model;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileNotFoundException;

public class Main {



    public static void main(String[] args) throws FileNotFoundException {

        try {

            File file = new File("/Users/aarunova/Desktop/thesis/convertOdf2Rdf/src/main/resources/infoitem_values.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Objects.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Objects objects= (Objects) jaxbUnmarshaller.unmarshal(file);

            Model aGraph = RDFMapper.builder()
                    .build().writeValue(objects);

            ModelModifier modelModifier = new ModelModifier();
            ModelWraper modelWraper = new ModelWraper();

            //modelModifier.modifyModel(aGraph);
            modelWraper.wrapModel(aGraph);


        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }


}
