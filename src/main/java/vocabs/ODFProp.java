package vocabs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aarunova on 4/9/17.
 */
public class ODFProp {

    public final static String OBJECT = "object";
    public final static String INFOITEM = "infoItem";
    public final static String VALUE = "value";
    public final static String METADATA = "metadata";
    public final static String ID = "id";
    public final static String NAME = "name";

    public final static String DESCRIPTION = "description";
    public final static String TYPE = "type";
    public final static String LANGUAGE = "language";
    public final static String CREATED = "created";
    public final static String UNIXTIME = "unixTime";
    public final static String DATAVALUE = "dataValue";
    public final static String IDVALUE = "idValue";
    public final static String TAGTYPE = "tagType";
    public final static String STARTDATE = "startDate";
    public final static String ENDDATE = "endDate";
    public final static String VERSION = "version";
    public final static String IDTYPE = "idType";

    public final static List<String> objectProperties = new ArrayList<>();
    public final static List<String> infoItemProperties = new ArrayList<>();
    public final static List<String> descriptionProperties = new ArrayList<>();
    public final static List<String> valueProperties = new ArrayList<>();
    public final static List<String> idProperties = new ArrayList<>();



    static {
        objectProperties.add(DESCRIPTION);
        objectProperties.add(ID);
        objectProperties.add(INFOITEM);
        objectProperties.add(TYPE);
        objectProperties.add(OBJECT);

        infoItemProperties.add(DESCRIPTION);
        infoItemProperties.add(NAME);
        infoItemProperties.add(TYPE);
        infoItemProperties.add(METADATA);
        infoItemProperties.add(VALUE);

        descriptionProperties.add(DESCRIPTION);
        descriptionProperties.add(LANGUAGE);
        descriptionProperties.add(TYPE);

        valueProperties.add(TYPE);
        valueProperties.add(CREATED);
        valueProperties.add(UNIXTIME);
        valueProperties.add(DATAVALUE);

        idProperties.add(IDVALUE);
        idProperties.add(TAGTYPE);
        idProperties.add(STARTDATE);
        idProperties.add(ENDDATE);
        idProperties.add(TYPE);

    }


}
