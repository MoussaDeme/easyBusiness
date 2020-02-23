package com.projet.easybusiness.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.projet.easybusiness.Annonce;
import com.projet.easybusiness.R;
import com.projet.easybusiness.SeeAd;
import com.projet.easybusiness.SeeAllAd;
import com.projet.easybusiness.helper_request.ApiListAnnonceAdapter;
import com.projet.easybusiness.helper_request.HelperClass;
import com.projet.easybusiness.helper_request.ResponseAnnonces;
import com.projet.easybusiness.recycler_view_helper.AnnonceAdapter;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AllAdsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AllAdsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllAdsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View v;
    private OnFragmentInteractionListener mListener;
    protected ArrayList<Annonce> listAnnonce;
    protected Map<String,Annonce> annonces=new HashMap<>();
    private RecyclerView recyclerView;
    public AllAdsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllAdsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllAdsFragment newInstance(String param1, String param2){
        AllAdsFragment fragment = new AllAdsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TextView text = getActivity().findViewById(R.id.textView3);
        //Log.i("test",text.getText().toString());
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_all_ads, container, false);
        try {
            // makeHttpRequest("https://ensweb.users.info.unicaen.fr/android-api/mock-api/liste.json");

            makeHttpRequest("https://ensweb.users.info.unicaen.fr/android-api/?apikey=21913373&method=listAll");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void makeHttpRequest(String url){

        OkHttpClient client = new OkHttpClient();
        Request req=  new Request.Builder().url(url).build();
        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try(ResponseBody responseBody=  response.body()){
                    if(!response.isSuccessful()){
                        throw  new IOException("La requete n'a pas aboutit");
                    }
                    final String body = responseBody.string();

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            parseAds(body);
                        }
                    });
                }
            }
        });
    }

    public void parseAds(String body){
        // declaration de l'instance de moshi qui va gerer le parsing
        Moshi moshi= new Moshi.Builder().add(new ApiListAnnonceAdapter()).build();
        JsonAdapter<ResponseAnnonces> adapter = moshi.adapter(ResponseAnnonces.class);

        try{
            ResponseAnnonces response = adapter.fromJson(body);
            listAnnonce=response.getAnnonces();
            AnnonceAdapter adapterListAnnonce= new AnnonceAdapter(listAnnonce);
            fillMap();
            RecyclerView recyclerView= v.findViewById(R.id.recycleViewAnnonces);
            // on inserer une linear view afin d'afficher les éléments sur une ligne
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            recyclerView.setAdapter(adapterListAnnonce);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public void itemClick(View v){
        String titreClick;
        TextView clicked = (TextView)v.findViewById(R.id.idAnnonce);
        titreClick=clicked.getText().toString();

        Intent next= new Intent(v.getContext(), SeeAd.class);
       //Log.i("titret",titreClick);
        next.putExtra("idAnnonce", annonces.get(titreClick));
        startActivity(next);
    }*/
    public void fillMap(){
        for(int i=0; i<this.listAnnonce.size() ;i++){
            Annonce ad= this.listAnnonce.get(i);
            ad.setImage(HelperClass.changeToPng(ad.getImages()));
            annonces.put(ad.getId(),ad);
            Log.i("roooo",ad.getId());
        }
    }
}
