package csp.experiencepush.com.mycsp;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.Date;

/**
 * Created by cchestnut on 9/29/14.
 */
public class Filter implements Serializable {

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

    public int beds;
    public int baths;
    public String month;
    public String year;
    public String[] keyWords;
    public int[] unitIDS;
    public Date available;

    public double lowRent;
    public double highRent;

    public double nearbyRange;

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

        this.month = null;
        this.year = null;
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

            this.beds = 0;
            this.baths = 0;

            this.month = null;
            this.year = null;
        }

    }

    public void setBeds(int bedsIn){
        this.beds = bedsIn;
    }
    public void setBaths(int baths){
        this.baths = baths;
    }
    public void setMonth(String month){
        this.month = month;
    }
    public void setYear (String year){
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
