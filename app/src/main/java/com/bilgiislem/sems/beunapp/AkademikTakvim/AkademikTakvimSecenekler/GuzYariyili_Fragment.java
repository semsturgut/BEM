package com.bilgiislem.sems.beunapp.AkademikTakvim.AkademikTakvimSecenekler;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bilgiislem.sems.beunapp.AkademikTakvim.AkademikTakvimAdapter.FeedItem;
import com.bilgiislem.sems.beunapp.AkademikTakvim.AkademikTakvimAdapter.MyRecyclerAdapter;
import com.bilgiislem.sems.beunapp.DHE_Sources.ServiceHandler;
import com.bilgiislem.sems.beunapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class GuzYariyili_Fragment extends android.support.v4.app.Fragment {

    private static final String TAG_DONEM = "donem";
    private static final String TAG_KATEGORIID = "kategori_id";
    private static final String TAG_TAKVIM = "takvim";
    private static final String TAG_BASLIK = "baslik";
    private static final String TAG_BASLANGIC = "baslangic";
    private static final String TAG_BITIS = "bitis";
    private static final String TAG_GUN = "gun";
    private static final String TAG_AY = "ay";
    private static final String TAG_YIL = "yil";
    private static final String TAG_RENK = "renk";


    JSONArray AllJSONTakvimData = null, JSONTakvimSelectedData = null, JSONBitisData = null;
    JSONObject JSONBaslangicData = null;
    RecyclerView recyclerView;
    private List<FeedItem> feedsList;
    private MyRecyclerAdapter adapter;
    TextView loadingData;
    String urlJSONData = "http://w3.beun.edu.tr/akademik_takvim/veri/?yil=2015";

    public GuzYariyili_Fragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_guz_yariyili, container, false);
        loadingData = (TextView) view.findViewById(R.id.loading_data);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        new GetJSON().execute();
        return view;
    }

    private class GetJSON extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            ServiceHandler sh = new ServiceHandler();
            String AllJSONStr = sh.makeServiceCall(urlJSONData, ServiceHandler.GET);
            if (AllJSONStr != null) {
                try {
                    JSONObject AllJSONObj = new JSONObject(AllJSONStr);
                    AllJSONTakvimData = AllJSONObj.getJSONArray(TAG_TAKVIM);
                    feedsList = new ArrayList<>();
                    for (int i = 0; i < AllJSONTakvimData.length(); i++) {
                        JSONObject AllJSONTakvimObj = AllJSONTakvimData.getJSONObject(i);
                        String AllKategoriIDStr = AllJSONTakvimObj.getString(TAG_KATEGORIID);
                        if (AllKategoriIDStr.equals("10")) {
                            JSONTakvimSelectedData = AllJSONTakvimObj.getJSONArray(TAG_TAKVIM);
                            for (int j = 0; j < JSONTakvimSelectedData.length(); j++) {
                                JSONObject JSONTakvimObj = JSONTakvimSelectedData.getJSONObject(j);
                                JSONBaslangicData = JSONTakvimObj.getJSONObject(TAG_BASLANGIC);
                                String donemStr = JSONTakvimObj.getString(TAG_DONEM);
                                if (donemStr.equals("1")) {
                                    String baslikStr = JSONTakvimObj.getString(TAG_BASLIK);
                                    FeedItem item = new FeedItem();
                                    if (!JSONBaslangicData.toString().equals("")) {
                                        String firstDay = JSONBaslangicData.getString(TAG_GUN);
                                        String firstMonth = JSONBaslangicData.getString(TAG_AY);
                                        String firstYear = JSONBaslangicData.getString(TAG_YIL);
                                        item.setFirstDay(firstDay);
                                        item.setFirstMonth(firstMonth);
                                        item.setFirstYear(firstYear);
                                        Log.d("firstday", firstDay);
                                    } else {
                                        String firstDay = "";
                                        String firstMonth = "";
                                        String firstYear = "";
                                        item.setFirstDay(firstDay);
                                        item.setFirstMonth(firstMonth);
                                        item.setFirstYear(firstYear);
                                    }

                                    item.setTitle(baslikStr);
                                    feedsList.add(item);
                                }
                            }
                        }
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
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter = new MyRecyclerAdapter(getActivity(), feedsList);
            recyclerView.setAdapter(adapter);
            loadingData.setVisibility(View.GONE);
        }
    }
}