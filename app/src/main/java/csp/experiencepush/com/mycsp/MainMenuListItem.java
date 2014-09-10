package csp.experiencepush.com.mycsp;

/**
 * Created by cchestnut on 9/10/14.
 */
public class MainMenuListItem {
    public MainMenuListItem (String title, String imageName) {
        super();
        this.title = title;
        this.imageName = imageName;
    }

    private String title;
    private String imageName;

    public String getTitle(){
        return this.title;
    }

    public String getImageName(){
        return this.imageName;
    }

    public void setTitle(String titleIn){
        this.title = titleIn;
    }

    public void setImageName(String imageNameIn){
        this.imageName = imageNameIn;
    }
}
