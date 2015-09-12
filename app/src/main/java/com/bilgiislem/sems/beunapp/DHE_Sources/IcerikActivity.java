package com.bilgiislem.sems.beunapp.DHE_Sources;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.support.v7.widget.Toolbar;

import com.bilgiislem.sems.beunapp.DHE_Sources.GridViewSources.GridViewActivity;
import com.bilgiislem.sems.beunapp.MainAndWeb.MainActivity;
import com.bilgiislem.sems.beunapp.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;


public class IcerikActivity extends AppCompatActivity {

    private static final String TAG_ICERIK = "icerik";
    String baslik_plus, http_plus;
    WebView webView;
    Button galleryButton;
    String url;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baslik_plus = getIntent().getStringExtra("baslik");
        http_plus = getIntent().getStringExtra("adres");
        url = "http://w3.beun.edu.tr/veri" + http_plus;

        setContentView(R.layout.icerik_layout);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(baslik_plus);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        galleryButton = (Button) findViewById(R.id.gallery_button);
        webView = (WebView) findViewById(R.id.icerik_http_text);
        galleryButton.setVisibility(View.GONE);

        new JSONParse().execute();
    }


    private class JSONParse extends AsyncTask<String, String, String> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(IcerikActivity.this);
            pDialog.setProgressStyle(ProgressDialog.THEME_HOLO_DARK);
            pDialog.setTitle(getString(R.string.loading));
            pDialog.setMessage(getString(R.string.waitfor));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
            return jsonStr;
        }

        @Override
        protected void onPostExecute(String json) {
            try {
                JSONObject jsonObj = new JSONObject(json);
                String icerik = jsonObj.getString(TAG_ICERIK);
                if (!jsonObj.getJSONArray("fotograf").toString().equals("[]")) {
                    galleryButton.setVisibility(View.VISIBLE);
                    galleryButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent galleryintent = new Intent(IcerikActivity.this, GridViewActivity.class);
                            galleryintent.putExtra("url", url);
                            galleryintent.putExtra("baslik", baslik_plus);
                            startActivity(galleryintent);
                        }
                    });
                }
                String texticerik = html2text(icerik);
                if (texticerik.startsWith("http://") || texticerik.startsWith("w3")) {
                    webView.loadUrl(texticerik);

                } else if (icerik.contains("/dosyalar/")) {
                    String replacedicerik = icerik.replaceAll("../../../..", "http://w3.beun.edu.tr/");
                    webView.loadDataWithBaseURL(null, replacedicerik, "text/html", "utf-8", null);
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.setWebViewClient(new MyWebViewClient());
                } else {
                    webView.loadDataWithBaseURL(null, icerik, "text/html", "utf-8", null);
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.setWebViewClient(new MyWebViewClient());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        private class MyWebViewClient extends WebViewClient {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
               finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public static String html2text(String html) {
        return Jsoup.parse(html).text();
    }
}