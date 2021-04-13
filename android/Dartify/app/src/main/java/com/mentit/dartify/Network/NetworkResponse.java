package com.mentit.dartify.Network;

import org.json.JSONException;
import org.json.JSONObject;

public class NetworkResponse {

    private Boolean error;
    private String mensaje;
    private JSONObject datos;

    public NetworkResponse(String response) {
        try {
            JSONObject mainObject = new JSONObject(response);
            error = mainObject.getBoolean("error");
            mensaje = mainObject.getString("mensaje");
            datos = mainObject.getJSONObject("datos");
        } catch (JSONException e) {
            error = true;
            mensaje = e.getMessage() + e.getLocalizedMessage();
            datos = new JSONObject();
            e.printStackTrace();
        }
    }

    public Boolean error() {
        return error;
    }

    public String getMensaje() {
        return mensaje;
    }

    public JSONObject getDatos() {
        return datos;
    }
}
