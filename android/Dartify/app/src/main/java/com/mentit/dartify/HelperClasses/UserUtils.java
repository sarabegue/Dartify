package com.mentit.dartify.HelperClasses;

import android.content.Context;

import com.mentit.dartify.BuildConfig;
import com.mentit.dartify.Models.POJO.User.Foto;
import com.mentit.dartify.Models.POJO.User.Usuario;
import com.mentit.dartify.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserUtils {

    public UserUtils() {
    }

    public final void storeUserProfile(JSONObject datos, Context context) {
        try {
            Usuario usuario = new Usuario(datos);

            SharedPreferenceUtils.getInstance(context).setValue(context.getString(R.string.user_name), usuario.getFirstname());
            SharedPreferenceUtils.getInstance(context).setValue(context.getString(R.string.user_email), usuario.getEmail());
            SharedPreferenceUtils.getInstance(context).setValue(context.getString(R.string.user_birthday), usuario.getFechaNacimiento());
            SharedPreferenceUtils.getInstance(context).setValue(context.getString(R.string.user_gender), usuario.getGender());
            SharedPreferenceUtils.getInstance(context).setValue(context.getString(R.string.user_location), usuario.getUbicacion());
            SharedPreferenceUtils.getInstance(context).setValue(context.getString(R.string.user_prefer), usuario.getPrefer());
            SharedPreferenceUtils.getInstance(context).setValue(context.getString(R.string.user_minage), usuario.getMinedad());
            SharedPreferenceUtils.getInstance(context).setValue(context.getString(R.string.user_maxage), usuario.getMaxedad());

            SharedPreferenceUtils.getInstance(context).setValue(context.getString(R.string.user_facebookid), usuario.getFacebookId());
            SharedPreferenceUtils.getInstance(context).setValue(context.getString(R.string.user_id), usuario.getId());
            SharedPreferenceUtils.getInstance(context).setValue(context.getString(R.string.user_name), usuario.getFirstname());
            SharedPreferenceUtils.getInstance(context).setValue(context.getString(R.string.user_email), usuario.getEmail());
            SharedPreferenceUtils.getInstance(context).setValue(context.getString(R.string.user_birthday), usuario.getFechaNacimiento());
            SharedPreferenceUtils.getInstance(context).setValue(context.getString(R.string.user_gender), usuario.getGender());

            SharedPreferenceUtils.getInstance(context).setValue(context.getString(R.string.user_location), usuario.getUbicacion());
            SharedPreferenceUtils.getInstance(context).setValue(context.getString(R.string.user_prefer), usuario.getPrefer());
            SharedPreferenceUtils.getInstance(context).setValue(context.getString(R.string.user_minage), usuario.getMinedad());
            SharedPreferenceUtils.getInstance(context).setValue(context.getString(R.string.user_maxage), usuario.getMaxedad());

            SharedPreferenceUtils.getInstance(context).setValue(context.getString(R.string.user_weight), usuario.getWeight());
            SharedPreferenceUtils.getInstance(context).setValue(context.getString(R.string.user_height), usuario.getHeight());
            SharedPreferenceUtils.getInstance(context).setValue(context.getString(R.string.user_skincolor), usuario.getSkin());
            SharedPreferenceUtils.getInstance(context).setValue(context.getString(R.string.user_hairtype), usuario.getHair());
            SharedPreferenceUtils.getInstance(context).setValue(context.getString(R.string.user_haircolor), usuario.getHairColor());
            SharedPreferenceUtils.getInstance(context).setValue(context.getString(R.string.user_race), usuario.getRace());
            SharedPreferenceUtils.getInstance(context).setValue(context.getString(R.string.user_occupation), usuario.getOccupation());
            SharedPreferenceUtils.getInstance(context).setValue(context.getString(R.string.user_scholarship), usuario.getScholarship());
            SharedPreferenceUtils.getInstance(context).setValue(context.getString(R.string.user_alcohol), usuario.getAlcohol());
            SharedPreferenceUtils.getInstance(context).setValue(context.getString(R.string.user_smoke), usuario.getSmoke());
            SharedPreferenceUtils.getInstance(context).setValue(context.getString(R.string.user_spirituality), usuario.getSpirituality());
            SharedPreferenceUtils.getInstance(context).setValue(context.getString(R.string.user_civilstate), usuario.getCivilState());
            SharedPreferenceUtils.getInstance(context).setValue(context.getString(R.string.user_siblings), usuario.getSiblings());
        } catch (Exception w) {
        }
    }

    public final ArrayList<Foto> getPhotos(JSONObject datos) {
        String path = BuildConfig.BASE_URL;
        ArrayList<Foto> lista = new ArrayList<>();
        try {
            JSONArray fotosprincipales = (JSONArray) datos.get("fotosprincipales");

            for (int index = 0; index < fotosprincipales.length(); index++) {
                JSONObject o = (JSONObject) fotosprincipales.get(index);
                long id = Long.parseLong(o.get("id").toString());
                String source = path + o.get("strsource").toString();
                int orden = Integer.parseInt(o.get("numorden").toString());

                Foto f = new Foto(id, source, orden);
                lista.add(f);
            }
        } catch (JSONException w) {
        }

        return lista;
    }

    public final void storeUserPhotos(JSONObject datos, Context context) {
        try {
            String path = BuildConfig.BASE_URL;

            JSONArray fotosprincipales = (JSONArray) datos.get("fotosprincipales");
            JSONArray fotossecundarias = (JSONArray) datos.get("fotossecundarias");

            for (int index = 0; index < fotosprincipales.length(); index++) {
                JSONObject o = (JSONObject) fotosprincipales.get(index);
                long id = Long.parseLong(o.get("id").toString());
                String source = path + o.get("strsource").toString();
                int orden = Integer.parseInt(o.get("numorden").toString());

                switch (index) {
                    case 0: {
                        SharedPreferenceUtils.getInstance(context).setValue(context.getString(R.string.photo_favorite0_id), id);
                        SharedPreferenceUtils.getInstance(context).setValue(context.getString(R.string.photo_favorite0_source), source);
                        SharedPreferenceUtils.getInstance(context).setValue(context.getString(R.string.photo_favorite0_order), orden);
                        break;
                    }
                    case 1: {
                        SharedPreferenceUtils.getInstance(context).setValue(context.getString(R.string.photo_favorite1_id), id);
                        SharedPreferenceUtils.getInstance(context).setValue(context.getString(R.string.photo_favorite1_source), source);
                        SharedPreferenceUtils.getInstance(context).setValue(context.getString(R.string.photo_favorite1_order), orden);
                        break;
                    }
                    case 2: {
                        SharedPreferenceUtils.getInstance(context).setValue(context.getString(R.string.photo_favorite2_id), id);
                        SharedPreferenceUtils.getInstance(context).setValue(context.getString(R.string.photo_favorite2_source), source);
                        SharedPreferenceUtils.getInstance(context).setValue(context.getString(R.string.photo_favorite2_order), orden);
                        break;
                    }
                    case 3: {
                        SharedPreferenceUtils.getInstance(context).setValue(context.getString(R.string.photo_favorite3_id), id);
                        SharedPreferenceUtils.getInstance(context).setValue(context.getString(R.string.photo_favorite3_source), source);
                        SharedPreferenceUtils.getInstance(context).setValue(context.getString(R.string.photo_favorite3_order), orden);
                        break;
                    }
                    case 4: {
                        SharedPreferenceUtils.getInstance(context).setValue(context.getString(R.string.photo_favorite4_id), id);
                        SharedPreferenceUtils.getInstance(context).setValue(context.getString(R.string.photo_favorite4_source), source);
                        SharedPreferenceUtils.getInstance(context).setValue(context.getString(R.string.photo_favorite4_order), orden);
                        break;
                    }
                }
            }

            SharedPreferenceUtils.getInstance(context).setValue(context.getString(R.string.photo_database), fotossecundarias.toString());
        } catch (JSONException w) {
        }
    }
}
