package csp.experiencepush.com.mycsp;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;


public class ListingDetailActivity extends Activity {

    public Listing listing;
    boolean wasFavorite;
    int imgPos;
    Context ctx;
    AQuery aq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aq = new AQuery(this);
        ctx = this;
        setContentView(R.layout.activity_listing_detail);
        listing = (Listing)getIntent().getParcelableExtra("Listing");

        if (listing.getLocation() == null){
            listing.geocodeLocation(this);
        }
        wasFavorite = listing.isFavorite(this);
        imgPos = 0;

// Images for preview

        TextView addressText = (TextView)findViewById(R.id.address_text);
        addressText.setText(listing.getAddressShort());
// Marquee Label?

        TextView townText = (TextView)findViewById(R.id.town_text);
        townText.setText(listing.getTown());
        TextView rentText = (TextView)findViewById(R.id.rent_text);
        rentText.setText("$"+(int)listing.getRent());

        SimpleDateFormat sdf = new SimpleDateFormat("MMM. d yyyy");
        String dateText = sdf.format(listing.getAvailable());

        TextView dateView = (TextView)findViewById(R.id.available_text);
        dateView.setText("Available "+dateText);

        TextView detailsText = (TextView)findViewById(R.id.detail_text);
        detailsText.setText(listing.getDescrip());

        RadioGroup radioGroup = (RadioGroup)findViewById(R.id.detail_subview_selector);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup arg0, int id) {
                ScrollView detailView = (ScrollView)findViewById(R.id.details_scroll_view);
                GridView amenitiesView = (GridView)findViewById(R.id.details_amenities);
                RelativeLayout mapView = (RelativeLayout)findViewById(R.id.details_map);
                switch (id) {
                    case -1:
                        break;
                    case R.id.details_selector:
                        detailView.setVisibility(View.VISIBLE);
                        amenitiesView.setVisibility(View.GONE);
                        mapView.setVisibility(View.GONE);
                        break;
                    case R.id.amenities_selector:
                        detailView.setVisibility(View.GONE);
                        amenitiesView.setVisibility(View.VISIBLE);
                        mapView.setVisibility(View.GONE);
                        break;
                    case R.id.map_selector:
                        detailView.setVisibility(View.GONE);
                        amenitiesView.setVisibility(View.GONE);
                        mapView.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
            }
        });

        GoogleMap map = ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.map)).getMap();

        if (listing.getLocation() != null){

            Location loc = listing.getLocation();
            LatLng listingLocation = new LatLng(loc.getLatitude(), loc.getLongitude());

            //map.setMyLocationEnabled(true);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(listingLocation, 13));
            map.addMarker(new MarkerOptions().title(listing.getAddressShort()).snippet(listing.getTown()).position(listingLocation));
            map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {

                    String uri = String.format(Locale.ENGLISH, "geo:%f,%f", marker.getPosition().latitude,marker.getPosition().longitude);
                    uri = String.format(Locale.ENGLISH, "https://www.google.com/maps/dir//%f,%f", marker.getPosition().latitude, marker.getPosition().longitude);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    ctx.startActivity(intent);
                }
            });
        } else {
// Disable Map View
            SegmentedControlButton mapButton = (SegmentedControlButton)findViewById(R.id.map_selector);
        }

        GridView grid = (GridView)findViewById(R.id.details_amenities);
        List<Amenity> amenities = listing.features();
        AmenityAdapter adapter = new AmenityAdapter(this, R.layout.amenity_grid_cell, amenities);

        grid.setAdapter(adapter);

        Button contactButton = (Button)findViewById(R.id.contact_button);
        contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(ctx, view);
                MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.contact_button_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.contact_call:

                                try {
                                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                                    callIntent.setData(Uri.parse("tel:6072776961"));
                                    startActivity(callIntent);
                                } catch (ActivityNotFoundException e) {
                                    Log.e("helloandroid dialing example", "Call failed", e);
                                }
                                return true;
                            case R.id.contact_email:
                                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

                                emailIntent.setType("plain/text");

                                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"info@cspmanagement.com"});

                                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Listing at "+((ListingDetailActivity)ctx).listing.getAddressShort());

                                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,"I'd like to speak to someone about this property.\nComments:\n\nProperty Details\nAddress: "+((ListingDetailActivity)ctx).listing.getAddressShort()+"\nUnit ID: "+((ListingDetailActivity)ctx).listing.getUnitID()+"\nFound with My CSP for Android");

                                ctx.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                                return true;

                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
            }
        });

        final SliderLayout sliderShow = (SliderLayout) findViewById(R.id.sliderView);
        if (listing.getImageSrc().length > 1 && isNetworkOnline()){
            sliderShow.stopAutoCycle();
            sliderShow.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Visible);
            sliderShow.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator));

            for (String url: listing.getImageSrc()){

                DefaultSliderView sliderView = new DefaultSliderView(this);
                sliderView.image(url);
/*
                sliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                    @Override
                    public void onSliderClick(BaseSliderView baseSliderView) {
                        SliderLayout fullSlider = (SliderLayout) findViewById(R.id.sliderViewFull);
                        fullSlider.setVisibility(View.VISIBLE);
                        fullSlider.addSlider(sliderShow.getCurrentSlider());
                    }
                });
*/
                sliderShow.addSlider(sliderView);
                //PhotoViewAttacher mAttacher = new PhotoViewAttacher((ImageView)sliderView.getView().findViewById(R.id.daimajia_slider_image));
            }
        } else {
            sliderShow.setVisibility(View.INVISIBLE);

            final ImageView mainView =((ImageView)findViewById(R.id.imageView));
            mainView.setVisibility(View.VISIBLE);
            /*
            mainView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final ImageView fullImage = (ImageView) findViewById(R.id.imageViewFull);
                    fullImage.setVisibility(View.VISIBLE);
                    fullImage.setScaleType(ImageView.ScaleType.FIT_CENTER);

                    if (listing.getImageSrc().length == 1) {
                        aq.id(R.id.imageViewFull).image(listing.getImageSrc()[0]);
                    } else {
                        aq.id(R.id.imageViewFull).image(R.drawable.background);
                    }
                    //PhotoViewAttacher mAttacher = new PhotoViewAttacher(fullImage);
                    fullImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            fullImage.setVisibility(View.GONE);
                        }
                    });
                }
            });
            */
            if (listing.getImageSrc().length >= 1) {

                String buildiumID = String.valueOf(listing.getBuildiumID());

                File listingDir = this.getDir("listingImages", this.MODE_PRIVATE);
                if ((new File(listingDir, buildiumID)).exists()) {
                    File firstImage = new File(listingDir, buildiumID);
                    aq.id(R.id.imageView).image(firstImage, 500);
                } else {
                    aq.id(R.id.imageView).image(listing.getImageSrc()[0]);
                }
            } else {
                aq.id(R.id.imageView).image(R.drawable.background);
            }
            //PhotoViewAttacher mAttacher = new PhotoViewAttacher((ImageView)findViewById(R.id.imageView));
        }
    }

    public boolean isNetworkOnline() {
        boolean status=false;
        try{
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            if (netInfo != null && netInfo.getState()==NetworkInfo.State.CONNECTED) {
                status= true;
            }else {
                netInfo = cm.getNetworkInfo(1);
                if(netInfo!=null && netInfo.getState()==NetworkInfo.State.CONNECTED)
                    status= true;
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return status;

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // The activity is about to be destroyed.

        if (this.wasFavorite != listing.favorite){
// Send API update
//

            SharedPreferences settings = getSharedPreferences("CSPPrefsFile", 0);
            final String UUID = settings.getString("userUUID", "-1");
            final PushRESTAPI api = new PushRESTAPI();

            List<String> favs = new ArrayList<String>(settings.getStringSet("Favorites", null));
            if (listing.favorite){

                Thread thread = new Thread(new Runnable(){
                        @Override
                        public void run(){
                            try {
                                api.addUserFavorite(UUID, String.valueOf(listing.getUnitID()));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                });
                thread.start();

                favs.add(String.valueOf(this.listing.getUnitID()));
            } else {

                Thread thread = new Thread(new Runnable(){
                    @Override
                    public void run(){
                        try {
                            api.removeUserFavorite(UUID, String.valueOf(listing.getUnitID()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();

                favs.remove(String.valueOf(this.listing.getUnitID()));
            }


            SharedPreferences.Editor editor = settings.edit();
            editor.putStringSet("Favorites", new HashSet<String>(favs));
            editor.commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.listing_detail, menu);
        MenuItem favoriteButton = (MenuItem)menu.getItem(0);
        if (this.listing.isFavorite(this)){
            favoriteButton.setIcon(R.drawable.blue_star);
            this.listing.favorite = true;
        } else {
            favoriteButton.setIcon(R.drawable.blue_star_empty);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.favorite_button){
            this.listing.favorite = !this.listing.favorite;
            if (this.listing.favorite){
                item.setIcon(R.drawable.blue_star);
            } else {
                item.setIcon(R.drawable.blue_star_empty);
            }
        }
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
