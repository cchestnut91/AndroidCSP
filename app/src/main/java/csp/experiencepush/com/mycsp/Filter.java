package csp.experiencepush.com.mycsp;

import android.content.Context;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by cchestnut on 9/29/14.
 */
public class Filter implements Parcelable, Serializable {

    public boolean favorite;
    public boolean images;
    public boolean checkLocation;
    public boolean cable;
    public boolean hardwood;
    public boolean fridge;
    public boolean laundry;
    public boolean oven;
    public boolean air;
    public boolean balcony;
    public boolean carport;
    public boolean dish;
    public boolean fence;
    public boolean fire;
    public boolean garage;
    public boolean internet;
    public boolean microwave;
    public boolean closet;

    public Location userLocation;

    private float lon;
    private float lat;
    private float acc;
    private float bea;

    public int beds;
    public int baths;
    public int month;
    public int year;
    public String[] keyWords;
    public int[] unitIDS;
    public Date available;

    public double lowRent;
    public double highRent;

    public double nearbyRange;

    public Filter(Parcel in){
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in){
        favorite = in.readByte() != 0;
        images = in.readByte() != 0;
        checkLocation = in.readByte() != 0;
        cable = in.readByte() != 0;
        hardwood = in.readByte() != 0;
        fridge = in.readByte() != 0;
        laundry = in.readByte() != 0;
        oven = in.readByte() != 0;
        air = in.readByte() != 0;
        balcony = in.readByte() != 0;
        carport = in.readByte() != 0;
        dish = in.readByte() != 0;
        fence = in.readByte() != 0;
        fire = in.readByte() != 0;
        garage = in.readByte() != 0;
        internet = in.readByte() != 0;
        microwave = in.readByte() != 0;
        closet = in.readByte() != 0;

        beds = in.readInt();
        baths = in.readInt();
        month = in.readInt();
        year = in.readInt();
        keyWords = in.createStringArray();
        available = (Date)in.readSerializable();
        lowRent = in.readDouble();
        highRent = in.readDouble();

        nearbyRange = in.readDouble();

    }

    public void writeToParcel(Parcel out, int flags){
        out.writeByte((byte) (favorite ? 1 : 0));
        out.writeByte((byte) (images ? 1 : 0));
        out.writeByte((byte) (checkLocation ? 1 : 0));
        out.writeByte((byte) (cable ? 1 : 0));
        out.writeByte((byte) (hardwood ? 1 : 0));
        out.writeByte((byte) (fridge ? 1 : 0));
        out.writeByte((byte) (laundry ? 1 : 0));
        out.writeByte((byte) (oven ? 1 : 0));
        out.writeByte((byte) (air ? 1 : 0));
        out.writeByte((byte) (balcony ? 1 : 0));
        out.writeByte((byte) (carport ? 1 : 0));
        out.writeByte((byte) (dish ? 1 : 0));
        out.writeByte((byte) (fence ? 1 : 0));
        out.writeByte((byte) (fire ? 1 : 0));
        out.writeByte((byte) (garage ? 1 : 0));
        out.writeByte((byte) (internet ? 1 : 0));
        out.writeByte((byte) (microwave ? 1 : 0));
        out.writeByte((byte) (closet ? 1 : 0));

        out.writeInt(beds);
        out.writeInt(baths);
        out.writeInt(month);
        out.writeInt(year);
        out.writeStringArray(keyWords);
        out.writeSerializable(available);
        out.writeDouble(lowRent);
        out.writeDouble(highRent);
        out.writeDouble(nearbyRange);
    }

    public static final Parcelable.Creator<Filter> CREATOR = new Parcelable.Creator<Filter>() {

        public Filter createFromParcel(Parcel in) {
            return new Filter(in);
        }

        public Filter[] newArray(int size) {
            return new Filter[size];
        }

    };

    @Override
    public int describeContents() {
        return 0;
    }

    public Filter (){
        super();

        this.favorite = false;
        this.images = false;
        this.checkLocation = false;
        this.cable = false;
        this.hardwood = false;
        this.fridge = false;
        this.laundry = false;
        this.oven = false;
        this.air = false;
        this.balcony = false;
        this.carport = false;
        this.dish = false;
        this.fence = false;
        this.fire = false;
        this.garage = false;
        this.internet = false;
        this.microwave = false;
        this.closet = false;

        this.nearbyRange = 600;

        this.beds = 0;
        this.baths = 0;

        this.month = 0;
        this.year = 0;
    }

    public Filter(Context context){
        super();
        File saveFile;
        saveFile =new File(context.getFilesDir(), "savedFilter");
        if (saveFile.exists()) {
            try {
                ObjectInputStream input;
                input = new ObjectInputStream(new FileInputStream(saveFile));
                Filter defaultFilter = (Filter) input.readObject();
                this.favorite = defaultFilter.favorite;
                this.images = defaultFilter.images;
                this.checkLocation = defaultFilter.checkLocation;
                this.cable = defaultFilter.cable;
                this.hardwood = defaultFilter.hardwood;
                this.fridge = defaultFilter.fridge;
                this.laundry = defaultFilter.laundry;
                this.oven = defaultFilter.oven;
                this.air = defaultFilter.air;
                this.balcony = defaultFilter.balcony;
                this.carport = defaultFilter.carport;
                this.dish = defaultFilter.dish;
                this.fence = defaultFilter.fence;
                this.fire = defaultFilter.fire;
                this.garage = defaultFilter.garage;
                this.internet = defaultFilter.internet;
                this.microwave = defaultFilter.microwave;
                this.closet = defaultFilter.closet;

                this.nearbyRange = defaultFilter.nearbyRange;

                this.beds = defaultFilter.beds;
                this.baths = defaultFilter.baths;

                this.month = defaultFilter.month;
                this.year = defaultFilter.year;
            } catch (StreamCorruptedException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            this.favorite = false;
            this.images = false;
            this.checkLocation = false;
            this.cable = false;
            this.hardwood = false;
            this.fridge = false;
            this.laundry = false;
            this.oven = false;
            this.air = false;
            this.balcony = false;
            this.carport = false;
            this.dish = false;
            this.fence = false;
            this.fire = false;
            this.garage = false;
            this.internet = false;
            this.microwave = false;
            this.closet = false;

            this.nearbyRange = 600;

            this.month = 0;
            this.year = 0;
        }

    }

    public ArrayList<Listing> filterListings(Listing[] listings, boolean override, Context context){
        ArrayList<Listing> toRet = new ArrayList<Listing>();

        for (Listing check : listings){

            if (!override){
                boolean date = check.isNowBetweenDates();
                if (date == false){
                    continue;
                }
            }

            if (this.checkLocation){
                if (userLocation == null || check.getLocation() == null){
                    continue;
                }

                float distance = this.userLocation.distanceTo(check.getLocation());
                if (distance > nearbyRange){
                    continue;
                }

            }

            if (this.year != 0){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy", Locale.US);
                String y = sdf.format(check.getAvailable());
                if (!(Integer.parseInt(y) <= 0)){
                    continue;
                }
                SimpleDateFormat mdf = new SimpleDateFormat("M", Locale.US);
                String m = mdf.format(check.getAvailable());

                if (!(m == String.valueOf(this.month)) && !(Integer.parseInt(y) < this.year)){
                    continue;
                }
            }

            if (this.lowRent != 0 && this.lowRent > check.getRent()){
                continue;
            }

            if (this.highRent != 0 && this.highRent < check.getRent()){
                continue;
            }

// #Check for Location

            if (this.beds > check.getBeds()){
                continue;
            }

            if (this.baths > check.getBaths()){
                continue;
            }

            if (this.images){
                if (check.getImageSrc().length == 0){
                    continue;
                }
            }


            if (this.favorite && !check.isFavorite(context)){
                continue;
            }

            if (this.cable && !check.hasCable()){
                continue;
            }

            if (this.hardwood && !check.hasHardwood()){
                continue;
            }

            if (this.fridge && !check.hasFridge()){
                continue;
            }

            if (this.laundry && !check.hasLaundry()){
                continue;
            }

            if (this.oven && !check.hasOven()){
                continue;
            }

            if (this.air && !check.hasAir()){
                continue;
            }

            if (this.balcony && !check.hasBalcony()){
                continue;
            }

            if (this.carport && !check.hasCarPort()){
                continue;
            }

            if (this.dish && !check.hasDish()){
                continue;
            }

            if (this.fence && !check.isFenced()){
                continue;
            }

            if (this.fire && !check.hasFire()){
                continue;
            }

            if (this.garage && !check.hasGarage()){
                continue;
            }

            if (this.internet && !check.hasInternet()){
                continue;
            }

            if (this.microwave && !check.hasMicrowave()){
                continue;
            }

            if (this.closet && !check.hasCloset()){
                continue;
            }

            if (this.keyWords != null){
                boolean found = false;

                for (String keyword: this.keyWords){
                    if (keyword != " "){
                        if (check.getAddress().toLowerCase().contains(keyword.toLowerCase())){
                            found = true;
                        } else if (check.getDescrip().toLowerCase().contains(keyword.toLowerCase())){
                            found = true;
                        }
                    }
                }

                if (!found){
                    continue;
                }
            }

            toRet.add(check);
        }

// #Try again if no Listings in range less than 1KM
        if (toRet.size() == 0 && checkLocation && nearbyRange < 1000){
            nearbyRange += 100;
            toRet = this.filterListings(listings, override, context);
        }
        nearbyRange = 600;

        return toRet;
    }

    public ArrayList<Listing> getSpecific(Listing[] listings, Context context, boolean filter){
        ArrayList<Listing> ret = new ArrayList<Listing>();
        for (Listing check : listings){

            if (contains(check.getUnitID())){
                ret.add(check);
                if (ret.size() == this.unitIDS.length){
                    break;
                }
            }
        }


        if (filter){
            ArrayList<Listing> toRet = new Filter(context).filterListings(ret.toArray(new Listing[ret.size()]), true, context);
            return toRet;
        }
// #Load first image for each listing in toRet
        return ret;
    }

    final private boolean contains(final int candidate) {
        for (int i = 0; i < this.unitIDS.length; i++) {
            if (candidate == unitIDS[i])
                return true;
        }
        return false;
    }


    public boolean[] getAmenities(){
        return new boolean[]{this.checkLocation, this.favorite, this.images, this.cable, this.hardwood, this.fridge, this.laundry, this.oven, this.air, this.balcony, this.carport, this.dish, this.fence, this.fire, this.garage, this.internet, this.microwave, this.closet};
    }

    public void setBool(int pos, boolean value) {
        switch (pos) {
            case 0:
                setCheckLocation(value);
                break;
            case 1:
                setFavorite(value);
                break;
            case 2:
                setImages(value);
                break;
            case 3:
                setCable(value);
                break;
            case 4:
                setHardwood(value);
                break;
            case 5:
                setFridge(value);
                break;
            case 6:
                setLaundry(value);
                break;
            case 7:
                setOven(value);
                break;
            case 8:
                setAir(value);
                break;
            case 9:
                setBalcony(value);
                break;
            case 10:
                setCarport(value);
                break;
            case 11:
                setDish(value);
                break;
            case 12:
                setFence(value);
                break;
            case 13:
                setFire(value);
                break;
            case 14:
                setGarage(value);
                break;
            case 15:
                setInternet(value);
                break;
            case 16:
                setMicrowave(value);
                break;
            case 17:
                setCloset(value);
                break;


            default:
                break;
        }
    }

    public void setBeds(int bedsIn){
        this.beds = bedsIn;
    }
    public void setBaths(int baths){
        this.baths = baths;
    }
    public void setMonth(int month){
        this.month = month;
    }
    public void setYear (int year){
        this.year = year;
    }
    public void setKeyWords(String[] keyWords){
        this.keyWords = keyWords;
    }
    public void setUnitIDS(int[] unitIDS){
        this.unitIDS = unitIDS;
    }
    public void setAvailable(Date available){
        this.available = available;
    }
    public void setLowRent(double lowRent){
        this.lowRent = lowRent;
    }
    public void setHighRent(double highRent){
        this.highRent = highRent;
    }
    public void setNearbyRange(double range){
        this.nearbyRange = range;
    }

    public boolean getFavorite(){
        return this.favorite;
    }
    public boolean getImages(){
        return this.images;
    }
    public boolean getCheckLocation(){
        return this.checkLocation;
    }
    public boolean getCable(){
        return this.cable;
    }
    public boolean getHardwood(){
        return this.hardwood;
    }
    public boolean getFridge(){
        return this.fridge;
    }
    public boolean getLaundry(){
        return this.laundry;
    }
    public boolean getOven(){
        return this.oven;
    }
    public boolean getAir(){
        return this.air;
    }
    public boolean getBalcony(){
        return this.balcony;
    }
    public boolean getCarport(){
        return this.carport;
    }
    public boolean getDish(){
        return this.dish;
    }
    public boolean getFence(){
        return this.fence;
    }
    public boolean getFire(){
        return this.fire;
    }
    public boolean getGarage(){
        return this.garage;
    }
    public boolean getInternet(){
        return this.internet;
    }
    public boolean getMicrowave(){
        return this.microwave;
    }
    public boolean getCloset(){
        return this.closet;
    }
    public void setFavorite(boolean favorite){
        this.favorite = favorite;
    }
    public void setImages(boolean images){
        this.images = images;
    }
    public void setCheckLocation(boolean checkLocation){
        this.checkLocation = checkLocation;
    }
    public void setCable(boolean cable){
        this.cable = cable;
    }
    public void setHardwood(boolean hardWood){
        this.hardwood = hardWood;
    }
    public void setFridge(boolean fridge){
        this.fridge = fridge;
    }
    public void setLaundry(boolean laundry){
        this.laundry = laundry;
    }
    public void setOven(boolean oven){
        this.oven= oven;
    }
    public void setAir(boolean air){
        this.air = air;
    }
    public void setBalcony(boolean balcony){
        this.balcony= balcony;
    }
    public void setCarport(boolean carport){
        this.carport= carport;
    }
    public void setDish(boolean dish){
        this.dish = dish;
    }
    public void setFence(boolean fence){
        this.fence = fence;
    }
    public void setFire(boolean fire){
        this.fire = fire;
    }
    public void setGarage(boolean garage){
        this.garage= garage;
    }
    public void setInternet(boolean internet){
        this.internet= internet;
    }
    public void setMicrowave(boolean microwave){
        this.microwave= microwave;
    }
    public void setCloset(boolean closet){
        this.closet = closet;
    }

}
