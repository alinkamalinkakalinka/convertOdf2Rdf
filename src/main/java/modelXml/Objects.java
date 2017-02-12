package modelXml;

import com.complexible.pinto.annotations.RdfId;
import com.complexible.pinto.annotations.RdfProperty;
import com.complexible.pinto.annotations.RdfsClass;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by aarunova on 12/11/16.
 */

@RdfsClass("odf:Objects")
@XmlRootElement (name = "Objects")
public class Objects {

    private String name;
    private List<Object> object;

    public Objects() {}

    public Objects(String name, List<Object> object) {
        super();
        this.name = name;
        this.object = object;
    }

    @RdfId
    @XmlAttribute
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @RdfId
    @RdfProperty("odf:object")
    @XmlElement (name = "Object")
    public List<Object> getObject() {
        return object;
    }

    public void setObject(List<Object> object) {
        this.object = object;
    }
}
