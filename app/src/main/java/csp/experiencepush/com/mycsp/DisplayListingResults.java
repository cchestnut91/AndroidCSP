package csp.experiencepush.com.mycsp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class DisplayListingResults extends Activity {

    private ArrayList<Listing> allListings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_listing_results);

        if (allListings == null){
            this.allListings= getIntent().getParcelableArrayListExtra("Listings");
        }
        final String actionID = this.getIntent().getStringExtra("actionID");
        if (actionID != null){

            SharedPreferences settings = getSharedPreferences("CSPPrefsFile", 0);
            final SharedPreferences.Editor editor = settings.edit();

            final String UUID= settings.getString("userUUID", "-1");
            final PushRESTAPI api = new PushRESTAPI();
            Thread thread = new Thread(new Runnable(){
                @Override
                public void run(){
                    try {
                        api.updateTriggeredBeaconAction(actionID, "1");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }

        ListView itemListView = (ListView)findViewById(R.id.results_list_view);
        if (allListings.size() == 0){
            List<String> emptyArray = new ArrayList<String>();
            emptyArray.add("String");
            EmptyListingsAdapter adapter = new EmptyListingsAdapter(this, R.layout.listing_result_empty, emptyArray);
            itemListView.setAdapter(adapter);
        } else {
            ListingResultsAdapter adapter = new ListingResultsAdapter(this, R.layout.listing_result_row, allListings);

            final Context ctx = this;
            itemListView.setAdapter(adapter);

            itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Intent showDetails= new Intent(ctx, ListingDetailActivity.class);
                    showDetails.putExtra("Listing", (Parcelable) allListings.get(i));
                    startActivity(showDetails);
                }
            });
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.display_listing_results, menu);
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
        return super.onOptionsItemSelected(item);
    }
}
