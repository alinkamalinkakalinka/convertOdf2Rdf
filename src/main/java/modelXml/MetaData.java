package modelXml;

import com.complexible.pinto.annotations.RdfId;
import com.complexible.pinto.annotations.RdfsClass;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by aarunova on 12/11/16.
 */

@RdfsClass("odf:MetaData")
@XmlRootElement
public class MetaData {

    private List<InfoItem> infoItem;

    public MetaData(){}

    public MetaData(List<InfoItem> infoItem) {
        super();
        this.infoItem = infoItem;
    }

    @RdfId
    @XmlElement
    public List<InfoItem> getInfoItem() {
        return infoItem;
    }

    public void setInfoItem(List<InfoItem> infoItem) {
        this.infoItem = infoItem;
    }
}
