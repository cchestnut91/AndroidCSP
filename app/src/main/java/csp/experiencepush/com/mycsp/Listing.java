package csp.experiencepush.com.mycsp;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by cchestnut on 9/11/14.
 */
public class Listing {

    Context ctx;

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

    private boolean favorite = false;
    private boolean cable = false;
    private boolean hardwood = false;
    private boolean refrigerator = false;
    private boolean laundry = false;
    private boolean oven = false;
    private boolean virtualTour = false;
    private boolean airConditioning = false;
    private boolean balcony = false;
    private boolean carport = false;
    private boolean dishwasher = false;
    private boolean fenced = false;
    private boolean fireplace = false;
    private boolean garage = false;
    private boolean internet = false;
    private boolean microwave = false;
    private boolean walkCloset = false;

    private Date start;
    private Date stop;

    public Listing (Map infoIn) throws ParseException {
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
        addressShort = addressShort.replaceAll("West", "W..");
        addressShort = addressShort.replaceAll(", NY", " NY,");

        this.town = addressShort.split(",")[1];
        this.town = this.town.replaceAll(" NY", " NY,");
        this.addressShort = addressShort.split(",")[0];

        java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");

        String availableString = (String)infoIn.get("available");
        availableString = availableString.split("T")[0];
        String startString = (String)infoIn.get("listDate");
        startString = startString.split("T")[0];
        String stopString = (String)infoIn.get("unavailable");
        stopString= stopString.split("T")[0];
        try {
            this.available = df.parse(availableString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            this.start = df.parse(startString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            this.stop = df.parse(stopString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        /*
        if (isNowBetweenDates()){
            Geocoder coder = new Geocoder(ctx);

            List<Address> addresses;
            Location location1 = null;

            try {
                addresses = coder.getFromLocationName(this.addressShort, 5);
                if (addresses != null){
                    GeoPoint point =
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        */

        if (infoIn.get("description") != null){
            this.descrip = (String)infoIn.get("description");

            if (this.descrip.contains("To view the virtual tour")){
                String partA = this.descrip.split("To view the virtual")[0];
                String partB = this.descrip.split(">", 3)[2];
                this.descrip = partA + partB;
            }
            if (this.descrip.contains("<a")){
                this.descrip = this.descrip.split("<a")[0] + this.descrip.split(">")[this.descrip.split(">").length -1];
            }
        } else {
            this.descrip = "No Description Available";
        }

        if (infoIn.get("beds") != null){
            this.beds = Integer.parseInt((String)infoIn.get("beds"));
        } else {
            this.beds = 0;
        }

        if (infoIn.get("baths") != null){
            this.baths = Integer.parseInt((String)infoIn.get("baths"));
        } else {
            this.baths = 0;
        }

        if (infoIn.get("unitID") != null){
            this.unitID = Integer.parseInt((String)infoIn.get("unitID"));
        } else {
            this.unitID = 0;
        }

        if (infoIn.get("sqft") != null){
            this.sqft= Float.parseFloat((String)infoIn.get("sqft"));
        } else {
            this.sqft = 0;
        }

        if (infoIn.get("rent") != null){
            this.rent= Float.parseFloat((String)infoIn.get("rent"));
        } else {
            this.rent= 0;
        }

        if (infoIn.get("heat") != null){
            this.heat = (String)infoIn.get("heat");
        } else {
            this.heat = null;
        }

        /*
// Set Property and download images if necessary
OBJ-C
if ([infoIn[@"buildiumID"] isKindOfClass:[NSNull class]]){
        self.property = [[Property alloc] initWithID:[NSNumber numberWithInt:0]];
        [infoIn[@"properties"] setObject:self.property forKey:self.property.buildiumID];
    } else {
        if ([infoIn[@"properties"] objectForKey:[NSNumber numberWithInt:[infoIn[@"buildiumID"] intValue]]]){
            self.property = [infoIn[@"properties"] objectForKey:[NSNumber numberWithInt:[infoIn[@"buildiumID"] intValue]]];
            if (!self.property.firstImage && self.imageSrc.count != 0 && [self isNowBetweenDate:self.start andDate:self.stop]){
                [self loadFirstImage:self.imageSrc[0]];
            } else if (self.property.firstImage && self.imageSrc.count != 0 && [self isNowBetweenDate:self.start andDate:self.stop]){
                [self.imageArray addObject:self.property.firstImage];
            }
        } else {
            self.property = [[Property alloc] initWithID:[NSNumber numberWithInt:[infoIn[@"buildiumID"] intValue]]];
            if (self.imageSrc.count > 0 && [self isNowBetweenDate:self.start andDate:self.stop]){
                [self loadFirstImage:self.imageSrc[0]];
            }
            [infoIn[@"properties"] setObject:self.property forKey:self.property.buildiumID];
        }

    }

         */

        if (infoIn.get(airConditioning) != null){
            this.airConditioning = true;
        }
        if (infoIn.get(balcony) != null){
            this.balcony = true;
        }
        if (infoIn.get(cable) != null){
            this.cable = true;
        }
        if (infoIn.get(carport) != null){
            this.carport = true;
        }
        if (infoIn.get(dishwasher) != null){
            this.dishwasher = true;
        }
        if (infoIn.get(fenced) != null){
            this.fenced = true;
        }
        if (infoIn.get(fireplace) != null){
            this.fireplace = true;
        }
        if (infoIn.get(garage) != null){
            this.garage = true;
        }
        if (infoIn.get(hardwood) != null){
            this.hardwood = true;
        }
        if (infoIn.get(internet) != null){
            this.internet = true;
        }
        if (infoIn.get(laundry) != null){
            this.laundry = true;
        }
        if (infoIn.get(microwave) != null){
            this.microwave = true;
        }
        if (infoIn.get(oven) != null){
            this.oven = true;
        }
        if (infoIn.get(refrigerator) != null){
            this.refrigerator = true;
        }
        if (infoIn.get(virtualTour) != null){
            this.virtualTour = true;
        }
        if (infoIn.get(walkCloset) != null){
            this.walkCloset = true;
        }

    }

    public Map features(){
        Map<String, Object> ret = null;
        ret.put("Test", "Test");

        return ret;
    }

    public void loadFirstImage(String srcIn){

    }

    public boolean isNowBetweenDates(){
        Date now = new Date();

        return now.after(this.start) && now.before(this.stop);
    }

// Getters

    public boolean isFavorite(){
        return this.favorite;
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
        return this.address;
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
