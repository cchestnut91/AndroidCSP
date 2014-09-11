package csp.experiencepush.com.mycsp;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.Map;

/**
 * Created by cchestnut on 9/11/14.
 */
public class DownloadListingsTask extends AsyncTask<String, Void, JSONArray> {
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

    }

}
