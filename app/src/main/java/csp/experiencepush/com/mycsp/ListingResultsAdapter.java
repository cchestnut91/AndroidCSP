package csp.experiencepush.com.mycsp;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by cchestnut on 10/3/14.
 */
public class ListingResultsAdapter extends ArrayAdapter<Listing>{

    private static LayoutInflater inflater;
    private Context context;
    public Filter filter;
    private int resource;
    Map<String, Bitmap> imagePreviews;
    AQuery aq;

        public ListingResultsAdapter(Context ctx, int resourceID, List<Listing> objects) {
            super(ctx, resourceID, objects);

            resource = resourceID;
            inflater = LayoutInflater.from(ctx);
            context = ctx;
                    /*
            imagePreviews = new HashMap<String, Bitmap>();

                    /*
            for (Listing list: objects){


                if (!imagePreviews.containsKey(buildiumID)){
                    File listingDir = context.getDir("listingImages", context.MODE_PRIVATE);
                    if ((new File(listingDir, buildiumID)).exists()){
                        File firstImage = new File(listingDir, buildiumID);
                        long size = firstImage.length();
                        InputStream is = null;
                        try {
                            is = new FileInputStream(firstImage);
                            //is = context.openFileInput(firstImage.getPath());
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        byte[] b = new byte[1024];
                        int bytesRead = 0;
                        try {
                            bytesRead = is.read(b);
                        } catch (IOException e) {
                            bytesRead = -1;
                            e.printStackTrace();
                        }
                        while (bytesRead != -1) {
                            bos.write(b, 0, bytesRead);
                            try {
                                bytesRead = is.read(b);
                            } catch (IOException e) {
                                bytesRead = -1;
                                e.printStackTrace();
                            }
                        }
                        byte[] bytes = bos.toByteArray();
                        int imageSize = bytes.length;

                        //Decode image size
                        BitmapFactory.Options opts = new BitmapFactory.Options();
                        opts.inJustDecodeBounds = true;

                        opts.inDither=false;                     //Disable Dithering mode
                        opts.inInputShareable=true;              //Which kind of reference will be used to recover the Bitmap data after being clear, when it will be used in the future
                        opts.inTempStorage=new byte[32 * 1024];
                        opts.inSampleSize = 8;

                        Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
                        bytes = null;
                        bos = null;
                        imagePreviews.put(buildiumID, bm);

                        System.out.print("Image Exists");
                    }
                }
            }

                    */
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if (position == 1){
            String pause = "";
        }

        convertView = (RelativeLayout)inflater.inflate(resource, null);
        Listing Item = getItem(position);
        aq = new AQuery(convertView);

        TextView addressView = (TextView)convertView.findViewById(R.id.address_text);
        addressView.setText(Item.getAddressShort());

        if (Item.getBeds() == 1){
            ((TextView)convertView.findViewById(R.id.bedrooms_text)).setText(Item.getBeds() + " Bedroom");
        } else {
            ((TextView)convertView.findViewById(R.id.bedrooms_text)).setText(Item.getBeds() + " Bedrooms");
        }

        ((TextView)convertView.findViewById(R.id.rent_text)).setText("$" + (int)Item.getRent());


        String buildiumID = String.valueOf(Item.getBuildiumID());
        File listingDir = context.getDir("listingImages", context.MODE_PRIVATE);
        if ((new File(listingDir, buildiumID)).exists()) {
            File firstImage = new File(listingDir, buildiumID);
            aq.id(R.id.imageView).image(firstImage.getPath(), true, true, 200, R.drawable.background);
        } else {
            aq.id(R.id.imageView).image(R.drawable.background);
        }
        //aq.id(R.id.imageView).image(Item.getImageSrc()[0], true, true, 200, R.drawable.background);
            return convertView;
    }
}
