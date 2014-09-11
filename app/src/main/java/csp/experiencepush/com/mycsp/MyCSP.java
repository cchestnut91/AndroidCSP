package csp.experiencepush.com.mycsp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static csp.experiencepush.com.mycsp.R.layout.main_screen_list_item;


public class MyCSP extends Activity {

    private int imgPos = 0;
    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_csp);

        final Context ctx = this;

        int highlightColor = ctx.getResources().getColor(R.color.highlight_color_filter);
        PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(highlightColor, PorterDuff.Mode.SRC_ATOP);

        Paint redHighLight = new Paint();
        redHighLight.setColorFilter(colorFilter);
        redHighLight.setAlpha(190);

        final ImageSwitcher switcher = (ImageSwitcher)findViewById(R.id.image_switcher);
        final ImageView image_a =(ImageView)findViewById(R.id.main_image_view_a);
        final ImageView image_b = (ImageView)findViewById(R.id.main_image_view_b);
        image_a.setColorFilter(colorFilter);
        image_b.setColorFilter(colorFilter);

        List<MainMenuListItem> itemList = new ArrayList<MainMenuListItem>();
        itemList.add(new MainMenuListItem("Locations Near Me", "location"));
        itemList.add(new MainMenuListItem("Advanced Search", "search"));
        itemList.add(new MainMenuListItem("My Favorites", "star"));
        itemList.add(new MainMenuListItem("Preferences", "profile"));
        itemList.add(new MainMenuListItem("Tenant Info", "lightbulb"));

        ListView itemListView = (ListView)findViewById(R.id.main_menu_list_view);
        itemListView.setAdapter(new MainMenuAdapter(ctx, R.layout.main_screen_list_item, itemList));

        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MainMenuListItem item = (MainMenuListItem)adapterView.getItemAtPosition(i);
                Toast.makeText(MyCSP.this, item.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

        final int[] imgArray = { R.drawable.background, R.drawable.scroll_b, R.drawable.scroll_a, R.drawable.scroll_c };


        Log.v(TAG, "index: "+imgPos);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                MyCSP.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imgPos += 1;
                        int res = imgArray[imgPos % 4];
                        if (switcher.getDisplayedChild() == 0){
                            image_b.setImageResource(res);
                            switcher.showNext();
                        } else {
                            image_a.setImageResource(res);
                            switcher.showPrevious();
                        }
                    }
                });
            }
        }, 10000, 10000);

        new DownloadListingsTask().execute("http://experiencepush.com/csp_portal/rest/?PUSH_ID=123&call=getAllListings");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_cs, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }
}
