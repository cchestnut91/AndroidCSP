package csp.experiencepush.com.mycsp;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by cchestnut on 9/10/14.
 */
public class MainMenuAdapter extends ArrayAdapter<MainMenuListItem>{
    private static LayoutInflater inflater;
    private Context context;
    private int resource;

    public MainMenuAdapter(Context ctx, int resourceID, List<MainMenuListItem> objects) {
        super(ctx, resourceID, objects);
        resource = resourceID;
        inflater = LayoutInflater.from(ctx);
        context = ctx;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = (RelativeLayout)inflater.inflate(resource, null);
        MainMenuListItem Item = getItem(position);
        TextView itemTitle = (TextView)convertView.findViewById(R.id.main_menu_list_title);
        itemTitle.setText(Item.getTitle());
        ImageView imageView = (ImageView)convertView.findViewById(R.id.main_menu_list_image);
        String uri = "drawable/"+Item.getImageName();
        int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
        Drawable image = context.getResources().getDrawable(imageResource);
        imageView.setImageDrawable(image);

        return convertView;
    }
}
