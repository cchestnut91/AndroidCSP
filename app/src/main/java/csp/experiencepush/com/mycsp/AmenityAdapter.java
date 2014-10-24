package csp.experiencepush.com.mycsp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by cchestnut on 10/7/14.
 */
public class AmenityAdapter extends ArrayAdapter<Amenity> {

    private static LayoutInflater inflater;
    private Context context;
    public Filter filter;
    private int resource;

    public AmenityAdapter(Context ctx, int resourceID, List<Amenity> objects) {
        super(ctx, resourceID, objects);

        resource = resourceID;
        inflater = LayoutInflater.from(ctx);
        context = ctx;

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = (RelativeLayout)inflater.inflate(resource, null);
        Amenity Item = getItem(position);

        TextView title = (TextView)convertView.findViewById(R.id.amenity_text);
        ImageView image = (ImageView)convertView.findViewById(R.id.amenity_image);
        if (position == 0){
            title.setText(Item.getValue());
            image.setImageResource(R.drawable.bedroom);
        } else if (position == 1){
            title.setText(Item.getValue());
            image.setImageResource(R.drawable.bath);
        } else {
            title.setText(Item.getValue());
            String uri = "drawable/"+Item.getType();
            int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
            image.setImageResource(imageResource);
        }


        return convertView;
    }
}
