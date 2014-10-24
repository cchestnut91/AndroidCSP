package csp.experiencepush.com.mycsp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.androidquery.AQuery;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by cchestnut on 10/17/14.
 */
public class ListItemAdapter  extends ArrayAdapter<ListItem> {

    private static LayoutInflater inflater;
    private Context context;
    public Filter filter;
    private int resource;
    AQuery aq;
    private SharedPreferences prefs;

    public ListItemAdapter(Context ctx, int resourceID, List<ListItem> objects) {
        super(ctx, resourceID, objects);

        resource = resourceID;
        inflater = LayoutInflater.from(ctx);
        context = ctx;
        prefs = context.getSharedPreferences("CSPPrefsFile", 0);

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ListItem Item = getItem(position);
        String viewType = Item.getType();
        if (viewType.equals("header")){
            this.resource = R.layout.search_settings_row_header;
            convertView = (RelativeLayout)inflater.inflate(resource, null);
            TextView itemTitle = (TextView)convertView.findViewById(R.id.header_text);
            itemTitle.setText(Item.getTitle());
        } else if (viewType.equals("footer")){
            this.resource = R.layout.search_settings_row_footer;
            convertView = (RelativeLayout) inflater.inflate(resource, null);
            TextView itemTitle = (TextView) convertView.findViewById(R.id.footer_text);
            itemTitle.setText(Item.getTitle());
        } else if (viewType.equals("switch")){
            this.resource = R.layout.search_settings_row_check;
            convertView = (RelativeLayout) inflater.inflate(resource, null);
            TextView itemTitle = (TextView) convertView.findViewById(R.id.check_row_title);
            itemTitle.setText(Item.getTitle());
            Switch checkBox = (Switch) convertView.findViewById(R.id.checkBox);
            if (Item.getTitle().equals("Enable Nearby Notifications")){
                checkBox.setChecked(prefs.getBoolean("allowBeacons", true));
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        boolean newValue = ((Switch) compoundButton).isChecked();
                        final SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("allowBeacons", newValue);
                        editor.commit();
                    }
                });
            }
        } else if (viewType.equals("button")){
            this.resource = R.layout.item_button_row;
            convertView = (RelativeLayout)inflater.inflate(resource, null);
            Button btn = (Button)convertView.findViewById(R.id.button);
            btn.setText(Item.getTitle());
            if (Item.getTitle().equals("Clear Defaults")){
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
// Confirm remove defaults
                        new AlertDialog.Builder(context).setTitle("Clear Preferences?").setMessage("Preferences will be adjusted to default values").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).setPositiveButton("I'm sure", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                File saveFile;
                                saveFile = new File(context.getFilesDir(), "savedFilter");
                                saveFile.delete();
                            }
                        }).show();
                    }
                });
            } else if (Item.getTitle().equals("Clear Favorites")){
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
// Confirm remove favorites
                        new AlertDialog.Builder(context).setTitle("Are you sure?").setMessage("Deleting favorites cannot be undone").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final String uuid = prefs.getString("userUUID", "-1");
                                final List<String> oldFavs = new ArrayList<String>(prefs.getStringSet("Favorites", null));
                                final PushRESTAPI api = new PushRESTAPI();

                                Thread thread = new Thread(new Runnable(){
                                    @Override
                                    public void run(){
                                        for (String oldFav : oldFavs) {
                                            try {
                                                api.removeUserFavorite(uuid, oldFav);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                });
                                thread.start();

                                final SharedPreferences.Editor editor = prefs.edit();
                                editor.putStringSet("Favorites", new HashSet<String>(new ArrayList<String>()));
                                editor.commit();
                            }
                        }).show();
                    }
                });
            }
        } else if (viewType.equals("link") || viewType.equals("web") || viewType.equals("call")){
            this.resource = R.layout.item_link_row;
            convertView = (RelativeLayout)inflater.inflate(resource, null);
            TextView text = (TextView)convertView.findViewById(R.id.row_title);
            text.setText(Item.getTitle());
        }




        return convertView;
    }
}