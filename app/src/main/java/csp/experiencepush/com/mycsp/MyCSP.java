package csp.experiencepush.com.mycsp;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.AsyncTask;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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

        DownloadListingsTask task;
        task = new DownloadListingsTask(this);
        task.execute("http://experiencepush.com/csp_portal/rest/?PUSH_ID=123&call=getAllListings");
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

    public static class DownloadListingsTask extends AsyncTask<String, Void, JSONArray> {

        Activity activity;

        public DownloadListingsTask(Activity myActivity) {
            this.activity= myActivity;
        }

        protected JSONArray doInBackground(String... urls){
            JSONArray jObject = null;

            String jsonString = downloadFileFromInternet(urls[0]);

            if (jsonString != null){
                try {
                    Map<String, String> listings;

                    jObject = new JSONArray(jsonString);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return jObject;
        }



        private String downloadFileFromInternet(String url)
        {
            if(url == null /*|| url.isEmpty() == true*/)
                new IllegalArgumentException("url is empty/null");
            StringBuilder sb = new StringBuilder();
            InputStream inStream = null;
            try
            {
                url = urlEncode(url);
                URL link = new URL(url);
                inStream = link.openStream();
                int i;
                int total = 0;
                byte[] buffer = new byte[8 * 1024];
                while((i=inStream.read(buffer)) != -1)
                {
                    if(total >= (1024 * 1024))
                    {
                        return "";
                    }
                    total += i;
                    sb.append(new String(buffer,0,i));
                }
            }catch(Exception e )
            {
                e.printStackTrace();
                return null;
            }catch(OutOfMemoryError e)
            {
                e.printStackTrace();
                return null;
            }
            return sb.toString();
        }

        private String urlEncode(String url)
        {
            if(url == null /*|| url.isEmpty() == true*/)
                return null;
            url = url.replace("[", "");
            url = url.replace("]","");
            url = url.replaceAll(" ","%20");
            return url;
        }

        protected void onPostExecute(JSONArray result){
            for (int i = 0; i < result.length(); i++){
                JSONObject jsonInfo = null;
                try {
                    jsonInfo = result.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    Map<String, Object> info = toMap(jsonInfo);
                    Listing list = new Listing(info);
                    String address = list.getAddress();
                    if (address != null){
                        String filename = Integer.toString(list.getUnitID());
                        ObjectOutput out = null;

                        try {
                            File toFile;
                            toFile = new File(activity.getDir("Listings", MODE_PRIVATE),filename);
                            out = new ObjectOutputStream(new FileOutputStream(toFile));
                            out.writeObject(list);
                            out.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            String[] files;
            files = activity.getDir("Listings", MODE_PRIVATE).list();

            Log.v(TAG, "Count: "+files.length);

            try {
                ObjectInputStream input;
                input = new ObjectInputStream(new FileInputStream(new File(activity.getDir("Listings", MODE_PRIVATE), files[0])));
                Listing readListing= (Listing) input.readObject();
                Log.v("serialization","Person a="+readListing.getAddress());
                input.close();
            } catch (StreamCorruptedException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }

        public static Map<String, Object> toMap(JSONObject object) throws JSONException {
            Map<String, Object> map = new HashMap();
            Iterator keys = object.keys();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                map.put(key, fromJson(object.get(key)));
            }
            return map;
        }

        private static Object fromJson(Object json) throws JSONException {
            if (json == JSONObject.NULL) {
                return null;
            } else if (json instanceof JSONObject) {
                return toMap((JSONObject) json);
            } else if (json instanceof JSONArray) {
                return toList((JSONArray) json);
            } else {
                return json;
            }
        }

        public static List toList(JSONArray array) throws JSONException {
            List list;
            list = new ArrayList();
            for (int i = 0; i < array.length(); i++) {
                list.add(fromJson(array.get(i)));
            }
            return list;
        }

    }
}
