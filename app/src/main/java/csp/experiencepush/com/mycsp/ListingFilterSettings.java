package csp.experiencepush.com.mycsp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class ListingFilterSettings extends Activity {

    public Filter filter;
    SearchSettingsAdapter adapter;
    ArrayList<Listing> listings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_filter_settings);

        Intent intent = getIntent();
        listings = getIntent().getParcelableArrayListExtra("Listings");

        filter = new Filter(this);

        List<SearchSettingsListItem> itemList = new ArrayList<SearchSettingsListItem>();
        itemList.add(new SearchSettingsListItem("ROOMS", "header", 0));
        itemList.add(new SearchSettingsListItem("Beds", "selector", filter.beds));
        itemList.add(new SearchSettingsListItem("Baths", "selector", filter.baths));
        itemList.add(new SearchSettingsListItem("RENT", "header", 0));
        itemList.add(new SearchSettingsListItem("Minimum Rent", "textInput", (int)filter.lowRent));
        itemList.add(new SearchSettingsListItem("Maximum Rent", "textInput", (int)filter.highRent));
        itemList.add(new SearchSettingsListItem("Only listings with rent in between these two values will be shown", "footer", 0));
        itemList.add(new SearchSettingsListItem("MOVE IN DATE", "header", 0));
        itemList.add(new SearchSettingsListItem("Month Available", "picker", 0));
        itemList.add(new SearchSettingsListItem("Some listings may not be shown until closer to their Move-In date. Check the app regularly to see new listings available in the future", "footer", 0));
        itemList.add(new SearchSettingsListItem("PREFERENCES", "header", 0));
        itemList.add(new SearchSettingsListItem("Near Me", "switch", 0));
        itemList.add(new SearchSettingsListItem("Favorite", "switch", 1));
        itemList.add(new SearchSettingsListItem("Images", "switch", 2));
        itemList.add(new SearchSettingsListItem("Listings fitting these preferences will be shown", "footer", 0));
        itemList.add(new SearchSettingsListItem("AMENITIES", "header", 0));
        itemList.add(new SearchSettingsListItem("Cable", "switch", 3));
        itemList.add(new SearchSettingsListItem("Hardwood Floors", "switch", 4));
        itemList.add(new SearchSettingsListItem("Refrigerator", "switch", 5));
        itemList.add(new SearchSettingsListItem("Laundry On Site", "switch", 6));
        itemList.add(new SearchSettingsListItem("Oven", "switch", 7));
        itemList.add(new SearchSettingsListItem("Air Conditioning", "switch", 8));
        itemList.add(new SearchSettingsListItem("Balcony / Patio", "switch", 9));
        itemList.add(new SearchSettingsListItem("Carport", "switch", 10));
        itemList.add(new SearchSettingsListItem("Dishwasher", "switch", 11));
        itemList.add(new SearchSettingsListItem("Fenced Yard", "switch", 12));
        itemList.add(new SearchSettingsListItem("Fireplace", "switch", 13));
        itemList.add(new SearchSettingsListItem("Garage", "switch", 14));
        itemList.add(new SearchSettingsListItem("High Speed Internet", "switch", 15));
        itemList.add(new SearchSettingsListItem("Microwave", "switch", 16));
        itemList.add(new SearchSettingsListItem("Walk-In Closet", "switch", 17));

        adapter = new SearchSettingsAdapter(this, R.layout.search_settings_row_selector, itemList);
        adapter.filter = filter;

        ListView itemListView = (ListView)findViewById(R.id.advanced_search_list_view);
        itemListView.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.listing_filter_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.search_settings_go_button){
            Filter newFilter = adapter.filter;

            ArrayList<Listing> filteredListings = filter.filterListings(listings.toArray(new Listing[listings.size()]), false, this);

            Intent results = new Intent(this, DisplayListingResults.class);
            results.putParcelableArrayListExtra("Listings", filteredListings);
            startActivity(results);
        }
        return super.onOptionsItemSelected(item);
    }
}
