package model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by aarunova on 12/11/16.
 */

@XmlRootElement (name = "Objects")
public class Objects {

    private String name;
    private String version;
    private Collection<Object> objects = new ArrayList<>();

    public Objects() {}

    public Objects(String name, String version, Collection<Object> objects) {
        super();
        this.name = name;
        this.version = version;
        this.objects = objects;
    }


    public String getName() {
        return name;
    }

    @XmlAttribute
    public void setName(String name) {
        this.name = name;
    }


    public Collection<Object> getObjects() {
        return objects;
    }

    @XmlElement (name = "Object")
    public void setObjects(Collection<Object> objects) {
        this.objects = objects;
    }

    public String getVersion() {
        return version;
    }

    @XmlAttribute (name = "version")
    public void setVersion(String version) {
        this.version = version;
    }
}
