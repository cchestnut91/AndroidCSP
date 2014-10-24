package csp.experiencepush.com.mycsp;

/**
 * Created by cchestnut on 10/1/14.
 */
public class SearchSettingsListItem {

    private String title;
    private String type;
    private double value;

    public SearchSettingsListItem (String title, String type, double value){
        super();
        this.value = value;
        this.title = title;
        this.type = type;
    }

    public double getValue(){
        return this.value;
    }

    public String getTitle(){
        return this.title;
    }

    public String getType(){
        return this.type;
    }

    public void setValue(int valueIn){
        this.value = valueIn;
    }

    public void setTitle(String titleIn){
        this.title = titleIn;
    }

    public void setType(String typeIn){
        this.type = typeIn;
    }
}
