package csp.experiencepush.com.mycsp;

import android.graphics.Bitmap;
import android.location.Location;

import java.util.Date;
import java.util.Map;

/**
 * Created by cchestnut on 9/11/14.
 */
public class Listing {
    public Listing (Map infoIn) {
        super();

        this.address = (String)infoIn.get("address");
        String addressShort = (String)infoIn.get("address");
        addressShort = addressShort.split(":")[0];
        addressShort = addressShort.replaceAll("Road", "Rd.");
        addressShort = addressShort.replaceAll("Drive", "Dr.");
        addressShort = addressShort.replaceAll("Street", "St.");
        addressShort = addressShort.replaceAll("Court", "Ct.");
        addressShort = addressShort.replaceAll("Avenue", "Ave.");
        addressShort = addressShort.replaceAll("Lane", "Ln.");
        addressShort = addressShort.replaceAll("Place", "Pl.");
        addressShort = addressShort.replaceAll("North", "N.");
        addressShort = addressShort.replaceAll("South", "S.");
        addressShort = addressShort.replaceAll("East", "E.");
        addressShort = addressShort.replaceAll("Road", "W..");
        addressShort = addressShort.replaceAll("Road", "Rd.");


    }

    private String address;
    private String addressShort;
    private String town;
    private String descrip;
    private String heat;
    private int beds;
    private int baths;
    private float sqft;
    private float rent;
    private int unitID;
    private Date available;
    private String[] imageSrc;
    private Bitmap[] imageArray;
    private Location location;
    private Property property;

    private boolean favorite;
    private boolean cable;
    private boolean hardwood;
    private boolean refrigerator;
    private boolean laundry;
    private boolean oven;
    private boolean virtualTour;
    private boolean airConditioning;
    private boolean balcony;
    private boolean carport;
    private boolean dishwasher;
    private boolean fenced;
    private boolean fireplace;
    private boolean garage;
    private boolean internet;
    private boolean microwave;
    private boolean walkCloset;

    private Date start;
    private Date stop;

    public Map features(){

    }

    public void loadFirstImage(String srcIn){

    }

    public boolean isNowBetweenDates(){

    }

// Getters

    public boolean isFavorite(){
        return this.favorite;
    }
    public boolean isCable(){
        return this.cable;
    }
    public boolean isHardwood(){
        return this.hardwood;
    }
    public boolean isRefrigerator(){
        return this.refrigerator;
    }
    public boolean isLaundry(){
        return this.laundry;
    }
    public boolean isOven(){
        return this.oven;
    }
    public boolean isVirtualTour(){
        return this.virtualTour;
    }
    public boolean isAirConditioning(){
        return this.airConditioning;
    }
    public boolean isBalcony(){
        return this.balcony;
    }
    public boolean isCarport(){
        return this.carport;
    }
    public boolean isDishwasher(){
        return this.dishwasher;
    }
    public boolean isFenced(){
        return this.fenced;
    }
    public boolean isFireplace(){
        return this.fireplace;
    }
    public boolean isGarage(){
        return this.garage;
    }
    public boolean isInternet(){
        return this.internet;
    }
    public boolean isMicrowave(){
        return this.microwave;
    }
    public boolean isWalkCloset(){
        return this.walkCloset;
    }

    public String[] getImageSrc(){
        return this.imageSrc;
    }

    public Bitmap[] getImageArray(){
        return this.imageArray;
    }

    public Location getLocation(){
        return this.location;
    }

    public Property getProperty(){
        return this.property;
    }

    public Date getAvailable(){
        return this.available;
    }

    public Date getStart(){
        return this.start;
    }

    public Date getStop(){
        return this.stop;
    }

    public float getSqft(){
        return this.sqft;
    }

    public float getRent(){
        return this.rent;
    }

    public int getBeds(){
        return this.beds;
    }

    public int getBaths(){
        return this.baths;
    }

    public int getUnitID(){
        return this.unitID;
    }

    public String getAddress(){
        if (this.address != null){
            return this.address;
        }
        return null;
    }

    public String getAddressShort(){
        return this.addressShort;
    }

    public String getTown(){
        return this.town;
    }

    public String getDescrip(){
        return this.descrip;
    }

    public String getHeat(){
        return this.heat;
    }
}
