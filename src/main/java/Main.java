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

public class Main {

    public static void main(String[] args){

        try {

            File file = new File("/Users/aarunova/Desktop/thesis/odfTordf/object_with_sub_objects.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Objects.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Objects objects= (Objects) jaxbUnmarshaller.unmarshal(file);

            Model aGraph = RDFMapper.create().writeValue(objects);
            System.out.println(aGraph);

            System.out.println(objects.getObject().get(0).getObject().get(1).getId());

        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }
}
