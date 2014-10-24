package csp.experiencepush.com.mycsp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class InfoActivity extends Activity {
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);


        final List<ListItem> itemList = new ArrayList<ListItem>();
        itemList.add(new ListItem("Tenant Log In" , "header", ""));
        itemList.add(new ListItem("Log In", "web", "https://cspmgmt.managebuilding.com/"));
        itemList.add(new ListItem("By logging in to the CSP Management On-Line Portal tenants have access to pay rent, submit work orders, etc.", "footer", ""));
        itemList.add(new ListItem("NYSEG" , "header", ""));
        itemList.add(new ListItem("View Website", "web", "http://www.nyseg.com/"));
        itemList.add(new ListItem("Call 1-800-572-1111", "call", "18005721111"));
        itemList.add(new ListItem("Ithaca City Water & Sewer", "header", ""));
        itemList.add(new ListItem("View Website", "web", "http://www.cityofithaca.org/departments/dpw/water/index.cfm"));
        itemList.add(new ListItem("Call: 607-272-1717", "call", "6072721717"));
        itemList.add(new ListItem("Fax: 607-277-5028", "link", ""));
        itemList.add(new ListItem("Report Emergency: 607-273-4680", "call", "6072734680"));
        itemList.add(new ListItem("Police", "header", ""));
        itemList.add(new ListItem("Call Non Emergency Assistance", "call", "6072723245"));
        itemList.add(new ListItem("Call HQ: 607-272-9973", "call", "6072729973"));
        itemList.add(new ListItem("In an emergency dial 9-1-1", "footer", ""));
        itemList.add(new ListItem("Local Government", "header", ""));
        itemList.add(new ListItem("Ithaca City Website", "web", "http://www.cityofithaca.org/"));
        itemList.add(new ListItem("108 Green Street, Ithaca NY", "link", ""));
        itemList.add(new ListItem("Call: 607-274-6501", "call", "6072746501"));


        ListItemAdapter adapter = new ListItemAdapter(this, R.layout.search_settings_row_check, itemList);

        ListView itemListView = (ListView)findViewById(R.id.preferences_list_view);
        itemListView.setAdapter(adapter);
        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ListItem selected = itemList.get(i);
                String type = selected.getType();
                if (type.equals("call")){
                    String link = "tel:"+selected.getValue();
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse(link));
                    startActivity(callIntent);
                } else if (type.equals("web")){
                    String url = selected.getValue();
                    Intent intent = new Intent(context, WebActivity.class);
                    intent.putExtra("url", url);
                    String title;
                    if (url.contains("cspmgmt")){
                        title = "CSP Login";
                    } else if (url.contains("nyseg")){
                        title = "NYSEG";
                    } else if (url.contains("water")){
                        title = "Ithaca Water and Sewer";
                    } else if (url.contains("cityofithaca")){
                        title = "City of Ithaca";
                    } else {
                        title = "Web";
                    }
                    intent.putExtra("title", title);
                    startActivity(intent);
                } else if (selected.getTitle().equals("108 Green Street, Ithaca NY")){
                    String uri = String.format(Locale.ENGLISH, "https://www.google.com/maps/place/108+W+Green+St,+Ithaca,+NY+14850");
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(intent);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.preference, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
}
