package model;

import com.complexible.pinto.annotations.RdfId;
import com.complexible.pinto.annotations.RdfProperty;
import com.complexible.pinto.annotations.RdfsClass;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by aarunova on 12/11/16.
 */

@RdfsClass("odf:InfoItem")
@XmlRootElement (name = "InfoItem")
public class InfoItem {

    private String name1;
    private String name;
    private String udef;
    private List<String> value;
    private String description;
    private MetaData metaData;

    public InfoItem(){}

    public InfoItem(String name, String name1, String udef, List<String> value, String description, MetaData metaData){
        this.name = name;
        this.name = name1;
        this.udef = udef;
        this.value = value;
        this.description = description;
        this.metaData = metaData;
    }

    @RdfId
    @RdfProperty("odf:udef")
    @XmlAttribute
    public String getUdef() {
        return udef;
    }

    public void setUdef(String udef) {
        this.udef = udef;
    }


    @RdfId
    @RdfProperty("odf:value")
    @XmlElement (name = "value")
    public List<String> getValue() {
        return value;
    }

    public void setValue(List<String> value) {
        this.value = value;
    }

    @RdfId
    @RdfProperty("dct:description")
    @XmlElement
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @RdfId
    @RdfProperty("odf:metaData")
    @XmlElement
    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

    @RdfId
    @RdfProperty ("odf:name")
    @XmlAttribute
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @RdfId
    @RdfProperty("odf:name")
    @XmlElement
    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }
}
