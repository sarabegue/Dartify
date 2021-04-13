package com.mentit.dartify.Tasks.User;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.mentit.dartify.Models.POJO.User.Foto;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class DownloadFacebookImagesTask extends AsyncTask<String, Void, String> {
    private Context context;
    private OnTaskCompleted taskCompleted;

    private AccessToken token;
    private String facebookid;
    private int cantidadImagenes = 100;
    private String albumid = "";

    ArrayList<Foto> listaFotos;

    public DownloadFacebookImagesTask(Context activityContext, AccessToken tok) {
        context = activityContext;
        taskCompleted = (OnTaskCompleted) activityContext;

        token = tok;

        if (tok == null) {
            token = AccessToken.getCurrentAccessToken();
        }

        facebookid = token.getUserId();
    }

    @Override
    protected String doInBackground(String... strings) {
        String graphPath = "";
        graphPath = "/" + facebookid + "/albums";
        new GraphRequest(token, graphPath, null, HttpMethod.GET, graphRequestQueryPhotoAlbums).executeAsync();

        return "";
    }

    private GraphRequest.Callback graphRequestQueryPhotoAlbums = new GraphRequest.Callback() {
        @Override
        public void onCompleted(GraphResponse response) {
            String graphPath;

            try {
                JSONArray ja = (JSONArray) response.getJSONObject().get("data");

                for (int index = 0; index < ja.length(); index++) {
                    JSONObject i = (JSONObject) ja.get(index);

                    String albumname = ((JSONObject) ja.get(index)).get("name").toString().toUpperCase();

                    if (albumname.contains("PROFILE") || albumname.contains("PERFIL")) {
                        Log.d("ALBUM_INFO", ((JSONObject) ja.get(index)).get("id").toString());
                        Log.d("ALBUM_INFO", ((JSONObject) ja.get(index)).get("name").toString());
                        albumid = ((JSONObject) ja.get(index)).get("id").toString();
                    }
                }

            } catch (Exception e) {
            }

            albumid = albumid.equals("") ? facebookid + "" : albumid;

            Bundle paramsImg = new Bundle();
            paramsImg.putString("fields", "images");
            graphPath = "/" + albumid + "/photos?type=uploaded";
            new GraphRequest(token, graphPath, paramsImg, HttpMethod.GET, graphRequestQueryPhotos).executeAsync();
        }
    };

    private GraphRequest.Callback graphRequestQueryPhotos = new GraphRequest.Callback() {
        @Override
        public void onCompleted(GraphResponse response) {
            try {
                JSONArray jsondata = (JSONArray) response.getJSONObject().get("data");

                int totalFotos = jsondata.length() < cantidadImagenes ? jsondata.length() : cantidadImagenes;

                listaFotos = new ArrayList<>();
                for (int index = 0; index < totalFotos; index++) {
                    Object o = jsondata.get(index);
                    String photoid = new JSONObject(o.toString()).get("images").toString();
                    JSONObject jso = new JSONObject((new JSONArray(photoid)).get(0).toString());

                    Foto f = new Foto();
                    f.setSource(jso.getString("source"));
                    f.setAlto(jso.getInt("height"));
                    f.setAncho(jso.getInt("width"));
                    listaFotos.add(f);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            taskCompleted.OnTaskCompleted(listaFotos);
        }
    };

    public interface OnTaskCompleted {
        void OnTaskCompleted(ArrayList lista);
    }
}