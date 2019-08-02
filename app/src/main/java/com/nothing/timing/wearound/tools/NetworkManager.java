package com.nothing.timing.wearound.tools;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class NetworkManager {

    private static final String TAG = "Network Manager Says";

    public String getResponse(String stringUrl) {

        BufferedReader reader;
        StringBuilder response = new StringBuilder();
        HttpURLConnection conn = null;
        URL url;

        try {
            url = new URL(stringUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            Log.e(TAG, "CONNECTING....");
            InputStream stream = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));

            String line = reader.readLine();
            response.append(line);
            line = reader.readLine();


            Log.e(TAG, "FINISHED CONNECTING");
            while (line != null) {

                response.append("\n").append(line);
                line = reader.readLine();
            }

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return response.toString();
    }
}
