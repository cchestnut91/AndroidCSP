package csp.experiencepush.com.mycsp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;

import java.util.List;

/**
 * Created by cchestnut on 10/3/14.
 */
public class EmptyListingsAdapter extends ArrayAdapter<String>{

    private static LayoutInflater inflater;
    private Context context;
    public Filter filter;
    private int resource;

    public EmptyListingsAdapter(Context ctx, int resourceID, List<String> objects) {
        super(ctx, resourceID, objects);

        resource = resourceID;
        inflater = LayoutInflater.from(ctx);
        context = ctx;

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = (RelativeLayout)inflater.inflate(resource, null);

        return convertView;
    }
}
