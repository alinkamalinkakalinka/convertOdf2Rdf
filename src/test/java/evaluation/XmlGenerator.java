package evaluation;

import model.*;
import model.Object;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Alina on 6/5/2017.
 */
public class XmlGenerator {

    @Test
    public void generateXml() throws JAXBException, FileNotFoundException {

        Objects objectsClass = new Objects();

        Collection<Object> objects = new ArrayList<>();

        for (int i=1; i<100000; i++) {
            Object object = new Object();

            QlmID id = new QlmID();
            id.setId("SmartFridge" + i);

            Collection<QlmID> ids = new ArrayList<>();
            ids.add(id);

            object.setId(ids);

            InfoItem infoItem = new InfoItem();
            infoItem.setName("accuracy" + i);

            Value value = new Value();
            value.setValue(String.valueOf(i));
            value.setType("xs:double");

            Collection<Value> values = new ArrayList<>();
            values.add(value);

            infoItem.setValues(values);

            Collection<InfoItem> infoItems = new ArrayList<>();
            infoItems.add(infoItem);

            MetaData metaData = new MetaData();
            metaData.setInfoItems(infoItems);

            Collection<MetaData> metaDatas = new ArrayList<>();
            metaDatas.add(metaData);

            InfoItem infoItemMain = new InfoItem();
            infoItemMain.setName("PowerConsumption" + i);
            infoItemMain.setMetaData(metaDatas);

            Collection<InfoItem> infoItemsMain = new ArrayList<>();
            infoItemsMain.add(infoItemMain);

            object.setInfoItems(infoItemsMain);

            objects.add(object);

        }

        objectsClass.setObjects(objects);

        JAXBContext contextObj = JAXBContext.newInstance(objectsClass.getClass());
        Marshaller marshallerObj = contextObj.createMarshaller();
        marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        marshallerObj.marshal(objectsClass, new FileOutputStream("test.xml"));
    }

}
