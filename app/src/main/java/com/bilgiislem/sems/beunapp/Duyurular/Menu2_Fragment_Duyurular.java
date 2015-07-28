package com.bilgiislem.sems.beunapp.Duyurular;


import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import android.widget.SimpleAdapter;

import com.bilgiislem.sems.beunapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

public class Menu2_Fragment_Duyurular extends ListFragment {

    private ProgressDialog pDialog;
    private static String url = "http://w3.beun.edu.tr/mobil-duyurular/";
    ListView listView;
    ImageView imageView;
    List<Integer> list = new ArrayList<>();
    private static final String TAG_S1 = "s1";
    private static final String TAG_BASLIK = "baslik";
    private static final String TAG_ADRES = "adres";
    JSONArray contacts = null;
    ArrayList<HashMap<String, String>> contactList;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu2_layout_duyurular, container, false);
        listView = new ListView(getActivity());


        imageView = (ImageView) view.findViewById(R.id.image_json_items);

        return view;
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {


        contactList = new ArrayList<HashMap<String, String>>();
        listView = getListView();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String adres2 = contactList.get(position).get("adres");
                String baslik2 = contactList.get(position).get("baslik");
                Intent intent = new Intent(getActivity(), Icerik_Activity.class);
                intent.putExtra("key", adres2);
                intent.putExtra("key2", baslik2);
                startActivity(intent);
            }
        });
        // Calling async task to get json
        new GetContacts().execute();
        super.onActivityCreated(savedInstanceState);
    }


    /**
     * Async task class to get json by making HTTP call
     */
    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setTitle(getActivity().getString(R.string.loading));
            pDialog.setMessage(getActivity().getString(R.string.waitfor));
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    contacts = jsonObj.getJSONArray(TAG_S1);
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        String baslik = c.getString(TAG_BASLIK);
                        String adres = c.getString(TAG_ADRES);
                        HashMap<String, String> contact = new HashMap<String, String>();
                        if (baslik.contains("<b>") || baslik.contains("<strong>")) {
                            list.add(i);
                        }
                        baslik = html2text(baslik);
                        contact.put(TAG_BASLIK, baslik);
                        contact.put(TAG_ADRES, adres);
                        contactList.add(contact);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
            ListAdapter adapter = new SimpleAdapter(
                    getActivity(), contactList,
                    R.layout.json_items, new String[]{TAG_BASLIK}, new int[]{R.id.name});
            setListAdapter(adapter);
        }
    }


    public static String html2text(String html) {
        return Jsoup.parse(html).text();
    }
}