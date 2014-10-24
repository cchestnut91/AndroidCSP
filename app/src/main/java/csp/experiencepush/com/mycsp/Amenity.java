package csp.experiencepush.com.mycsp;

/**
 * Created by cchestnut on 10/7/14.
 */
public class Amenity {

    public String type;
    public String value;

    public String getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    public Amenity(String type, String value){
        super();
        this.type = type;
        this.value = value;
    }
}
