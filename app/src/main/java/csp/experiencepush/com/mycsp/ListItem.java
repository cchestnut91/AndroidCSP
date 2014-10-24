package csp.experiencepush.com.mycsp;

/**
 * Created by cchestnut on 10/17/14.
 */
public class ListItem {

    private String title;
    private String type;
    private String value;

    public ListItem (String title, String type, String value){
        super();
        this.value = value;
        this.title = title;
        this.type = type;
    }

    public String getValue(){
        return this.value;
    }

    public String getTitle(){
        return this.title;
    }

    public String getType(){
        return this.type;
    }

    public void setValue(String valueIn){
        this.value = valueIn;
    }

    public void setTitle(String titleIn){
        this.title = titleIn;
    }

    public void setType(String typeIn){
        this.type = typeIn;
    }
}
