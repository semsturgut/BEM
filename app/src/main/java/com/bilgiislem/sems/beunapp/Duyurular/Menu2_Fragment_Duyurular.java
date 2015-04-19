package com.bilgiislem.sems.beunapp.Duyurular;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.bilgiislem.sems.beunapp.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONException;
import org.json.JSONObject;

public class Menu2_Fragment_Duyurular extends Fragment {

    public static String line = "";
    public static String sonuc = "";
    public static String last_text = "";

    public static void main(String[] args) throws IOException, JSONException {
        readFromOracle();
    }

    private static void readFromOracle() throws IOException, JSONException {
        URL url = new URL("http://w3.beun.edu.tr/veri/haberler/1222/");
        URLConnection httpUrlConnection = url.openConnection();
        InputStream inputStream = httpUrlConnection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream));
        while ((line = bufferedReader.readLine()) != null) {
            sonuc = sonuc + line;
        }
        JSONObject jsonobject = new JSONObject(sonuc);
        last_text = jsonobject.getString("icerik");
        /*
         * Document doc = Jsoup.connect("http://en.wikipedia.org/").get();
		 * Elements newsHeadlines = doc.select("td#yazilar");
		 */

        bufferedReader.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.menu2_layout, container, false);
        WebView webView = (WebView) rootview.findViewById(R.id.duyurular_page);
        webView.loadData(last_text, "text/html", "utc-8");
        return rootview;
    }
}