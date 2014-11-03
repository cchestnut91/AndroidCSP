package csp.experiencepush.com.mycsp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.androidquery.AQuery;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by cchestnut on 9/11/14.
 */
public class Listing implements Parcelable, Serializable {

    private String address;
    private String addressShort;
    private String town;
    private String descrip;
    private String heat;
    private int beds;
    private float baths;
    private float sqft;
    private float rent;
    private int unitID;
    private int buildiumID;
    private Date available;
    private String[] imageSrc;
    private Bitmap[] imageArray;

    private float lon;
    private float lat;

    public boolean favorite = false;
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

    public Listing (Parcel in){
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in){
        address = in.readString();
        addressShort = in.readString();
        town = in.readString();
        descrip = in.readString();
        heat = in.readString();
        beds = in.readInt();
        baths = in.readFloat();
        sqft = in.readFloat();
        rent = in.readFloat();
        unitID = in.readInt();
        buildiumID = in.readInt();
        available = (Date)in.readSerializable();
        imageSrc =
        in.createStringArray();
        lon = in.readFloat();
        lat = in.readFloat();
        favorite = in.readByte() != 0;
        cable = in.readByte() != 0;
        hardwood = in.readByte() != 0;
        refrigerator = in.readByte() != 0;
        laundry = in.readByte() != 0;
        oven = in.readByte() != 0;
        airConditioning = in.readByte() != 0;
        balcony = in.readByte() != 0;
        carport = in.readByte() != 0;
        dishwasher = in.readByte() != 0;
        fenced = in.readByte() != 0;
        fireplace = in.readByte() !=0;
        garage = in.readByte() != 0;
        internet = in.readByte() != 0;
        microwave = in.readByte() != 0;
        walkCloset = in.readByte() != 0;
        start = (Date)in.readSerializable();
        stop = (Date)in.readSerializable();
    }

    public void writeToParcel(Parcel out, int flags){
        out.writeString(address);
        out.writeString(addressShort);
        out.writeString(town);
        out.writeString(descrip);
        out.writeString(heat);
        out.writeInt(beds);
        out.writeFloat(baths);
        out.writeFloat(sqft);
        out.writeFloat(rent);
        out.writeInt(unitID);
        out.writeInt(buildiumID);
        out.writeSerializable(available);
        out.writeStringArray(imageSrc);
        out.writeFloat(lon);
        out.writeFloat(lat);
        out.writeByte((byte) (favorite ? 1 : 0));
        out.writeByte((byte) (cable ? 1 : 0));
        out.writeByte((byte) (hardwood ? 1 : 0));
        out.writeByte((byte) (refrigerator ? 1 : 0));
        out.writeByte((byte) (laundry ? 1 : 0));
        out.writeByte((byte) (oven ? 1 : 0));
        out.writeByte((byte) (airConditioning ? 1 : 0));
        out.writeByte((byte) (balcony ? 1 : 0));
        out.writeByte((byte) (carport ? 1 : 0));
        out.writeByte((byte) (dishwasher ? 1:0));
        out.writeByte((byte) (fenced ? 1 : 0));
        out.writeByte((byte) (fireplace ? 1 : 0));
        out.writeByte((byte) (garage ? 1 : 0));
        out.writeByte((byte) (internet ? 1 : 0));
        out.writeByte((byte) (microwave ? 1 : 0));
        out.writeByte((byte) (walkCloset ? 1:0));
        out.writeSerializable(start);
        out.writeSerializable(stop);
    }
    public static final Parcelable.Creator<Listing> CREATOR = new Parcelable.Creator<Listing>() {

        public Listing createFromParcel(Parcel in) {
            return new Listing(in);
        }

        public Listing[] newArray(int size) {
            return new Listing[size];
        }

    };

    @Override
    public int describeContents() {
        return 0;
    }

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
        if (this.town.contains("Uptown Village")){
            this.town = "Ithaca, NY";
        }
        this.town = this.town.replaceAll(" NY", " NY,");
        this.addressShort = addressShort.split(",")[0];

        this.buildiumID = Integer.parseInt((String)infoIn.get("buildiumID"));
        this.unitID = Integer.parseInt((String)infoIn.get("unitID"));

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

        if (isNowBetweenDates()){
            geocodeLocation((Context) infoIn.get("Context"));
        }

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
            this.baths = Float.parseFloat((String)infoIn.get("baths"));
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

        if (infoIn.get("airConditioning") != null){
            this.airConditioning = true;
        }
        if (infoIn.get("balcony") != null){
            this.balcony = true;
        }
        if (infoIn.get("cable") != null){
            this.cable = true;
        }
        if (infoIn.get("carport") != null){
            this.carport = true;
        }
        if (infoIn.get("dishwasher") != null){
            this.dishwasher = true;
        }
        if (infoIn.get("fenced") != null){
            this.fenced = true;
        }
        if (infoIn.get("fireplace") != null){
            this.fireplace = true;
        }
        if (infoIn.get("garage") != null){
            this.garage = true;
        }
        if (infoIn.get("hardwood") != null){
            this.hardwood = true;
        }
        if (infoIn.get("internet") != null){
            this.internet = true;
        }
        if (infoIn.get("laundry") != null){
            this.laundry = true;
        }
        if (infoIn.get("microwave") != null){
            this.microwave = true;
        }
        if (infoIn.get("oven") != null){
            this.oven = true;
        }
        if (infoIn.get("refrigerator") != null){
            this.refrigerator = true;
        }
        if (infoIn.get("virtualTour") != null){
            this.virtualTour = true;
        }
        if (infoIn.get("walkCloset") != null){
            this.walkCloset = true;
        }

        this.imageSrc = ((ArrayList<String>)infoIn.get("listingsImage")).toArray(new String[((ArrayList<String>)infoIn.get("listingsImage")).size()]);

        if (this.imageSrc.length != 0 && isNowBetweenDates()){
            AQuery aq = (AQuery)infoIn.get("AQuery");
            String buildiumID = String.valueOf(this.getBuildiumID());
            File listingDir = ((Context)infoIn.get("Context")).getDir("listingImages", ((Context)infoIn.get("Context")).MODE_PRIVATE);
            File firstImage = new File(listingDir, buildiumID);
            Date modifified = new Date(firstImage.lastModified());
            Date now = new Date();
            long difference = now.getTime() - modifified.getTime();
            if (difference >= 86400000){
                try
                {
                    InputStream is = (InputStream) new URL(this.getImageSrc()[0]).getContent();
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 4;
                    Bitmap bmp = BitmapFactory.decodeStream(is);
                    is.close();
                    int size = bmp.getByteCount();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream(size);
                    bmp.compress(Bitmap.CompressFormat.PNG, 2, bos);
                    byte[] bArr = bos.toByteArray();
                    bos.flush();
                    bos.close();

                    BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(firstImage));
                    fos.write(bArr);
                    fos.flush();
                    fos.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                                /*
                                catch (FileNotFoundException e)
                                {
                                    e.printStackTrace();
                                }
                                catch (IOException e) {
                                    e.printStackTrace();
                                }
                                */
            }
        }

        System.out.print("Test");
    }

    public Location getLocation(){
        if (this.lat == 0 && this.lon == 0){
            return null;
        }
        Location current = new Location("reverseGeocoded");
        current.setLatitude(this.lat);
        current.setLongitude(this.lon);
        current.setAccuracy(3333);
        current.setBearing(333);
        return current;
    }

    public void geocodeLocation(Context ctx){
        if (ctx != null){

            Geocoder geocoder = new Geocoder(ctx);

            try {
                List<Address> addresses = geocoder.getFromLocationName(this.address, 1);
                if (addresses.size() == 0){
                    String smallAddress = this.addressShort;
                    if (smallAddress.contains("-")){
                        smallAddress = smallAddress.split("-")[0];
                    }
                    addresses = geocoder.getFromLocationName(smallAddress+" "+this.town, 1);
                }
                if (addresses.size() != 0){
                    Address address = addresses.get(0);
                    this.lon = (float)address.getLongitude();
                    this.lat = (float)address.getLatitude();
                } else {
                    String test = "test";
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Amenity> features(){
        List<Amenity> ret = new ArrayList<Amenity>();

        ret.add(new Amenity("bedroom", this.beds + " Beds"));
        ret.add(new Amenity("Baths", (int)this.baths + " Baths"));
        if (this.heat != null){
            ret.add(new Amenity("heat",this.getHeat()));
        }
        if (this.airConditioning == true){
            ret.add(new Amenity("air_conditioning", "Air Conditioning"));
        }
        if (this.cable == true){
            ret.add(new Amenity("cable","Cable Ready"));
        }
        if (this.hardwood == true){
            ret.add(new Amenity("hardwood", "Hardwood Floors"));
        }
        if (this.hasFridge() == true){
            ret.add(new Amenity("fridge","Refrigerator"));
        }
        if (this.laundry == true){
            ret.add(new Amenity("laundry", "Laundry / Hookups"));
        }
        if (this.oven == true){
            ret.add(new Amenity("oven", "Oven / Range"));
        }
        if (this.balcony == true){
            ret.add(new Amenity("balcony","Balcony/Deck/Patio"));
        }
        if (this.carport == true){
            ret.add(new Amenity("parking", "Carport"));
        }
        if (this.dishwasher == true){
            ret.add(new Amenity("dish_washer", "Dishwasher"));
        }
        if (this.fenced == true){
            ret.add(new Amenity("fenced", "Fenced Yard"));
        }
        if (this.garage == true){
            ret.add(new Amenity("garage", "Garage Parking"));
        }
        if (this.fireplace == true){
            ret.add(new Amenity("fireplace", "Fireplace"));
        }
        if (this.internet == true){
            ret.add(new Amenity("internet", "High Speed Internet"));
        }
        if (this.microwave){
            ret.add(new Amenity("microwave", "Microwave"));
        }
        if (this.hasCloset() == true){
            ret.add(new Amenity("walk_closet", "Walk-In Closet"));
        }

        return ret;
    }

    public void loadFirstImage(String srcIn){

    }

    public boolean isNowBetweenDates(){
        Date now = new Date();
        boolean ret = now.after(this.start) && now.before(this.stop);
        return ret;
    }

// Getters

    public boolean hasCable(){
        return this.cable;
    }
    public boolean hasHardwood(){
        return this.hardwood;
    }
    public boolean hasFridge(){
        return this.refrigerator;
    }
    public boolean hasLaundry(){
        return this.laundry;
    }
    public boolean hasOven(){
        return this.oven;
    }
    public boolean hasAir(){
        return this.airConditioning;
    }
    public boolean hasBalcony(){
        return this.balcony;
    }
    public boolean hasCarPort(){
        return this.carport;
    }
    public boolean hasDish(){
        return this.dishwasher;
    }
    public boolean isFenced(){
        return this.fenced;
    }
    public boolean hasFire(){
        return this.fireplace;
    }
    public boolean hasGarage(){
        return this.garage;
    }
    public boolean hasInternet(){
        return this.internet;
    }
    public boolean hasMicrowave(){
        return this.microwave;
    }
    public boolean hasCloset(){
        return this.walkCloset;
    }
    public boolean getFavorite(){
        return this.favorite;
    }
    public boolean isFavorite(Context context){

        SharedPreferences settings = context.getSharedPreferences("CSPPrefsFile", 0);
        List<String> favs = new ArrayList<String>(settings.getStringSet("Favorites",null));
        if (favs == null){
            return false;
        }
        return favs.contains(String.valueOf(this.unitID));

    }

    public String[] getImageSrc(){
        return this.imageSrc;
    }

    public Bitmap[] getImageArray(){
        return this.imageArray;
    }

    public int getBuildiumID(){
        return this.buildiumID;
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

    public float getBaths(){
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
