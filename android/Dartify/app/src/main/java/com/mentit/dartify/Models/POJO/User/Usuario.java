package com.mentit.dartify.Models.POJO.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Usuario implements Serializable {
    private long id = -1;
    private String facebookid = "";
    private String stremail = "";
    private String datefechanacimiento = "";
    private String strgender = "";
    private String strfirstname = "";
    private int prefer = 0;
    private int minedad = 0;
    private int maxedad = 0;
    private int ubicacion = 0;

    private int height;
    private int weight;
    private int skin;
    private int hair;
    private int haircolor;
    private int race;
    private int occupation;
    private int scholarship;
    private int alcohol;
    private int smoke;
    private int spirituality;
    private int civilstate;
    private int siblings;

    private ArrayList<Foto> listaFotos = new ArrayList<>();

    public Usuario() {

    }

    public Usuario(JSONObject json) {
        try {
            id = json.getLong("id");
            facebookid = json.getString("facebookid");
            stremail = json.getString("stremail");
            strfirstname = json.getString("strfirstname");
            strgender = json.getString("strgender");
            datefechanacimiento = json.getString("datefechanacimiento");
            prefer = json.getInt("numsexobuscado");
            minedad = json.getInt("numminedad");
            maxedad = json.getInt("nummaxedad");
            ubicacion = json.getInt("numubicacion");

            try {
                height = json.getInt("numestatura");
            } catch (Exception w) {
            }
            try {
                weight = json.getInt("numpeso");
            } catch (Exception w) {
            }
            try {
                skin = json.getInt("numcolorpiel");
            } catch (Exception w) {
            }
            try {
                hair = json.getInt("numtipocabello");
            } catch (Exception w) {
            }
            try {
                haircolor = json.getInt("numcolorcabello");
            } catch (Exception w) {
            }
            try {
                race = json.getInt("numraza");
            } catch (Exception w) {
            }
            try {
                occupation = json.getInt("numocupacion");
            } catch (Exception w) {
            }
            try {
                scholarship = json.getInt("numnivelestudios");
            } catch (Exception w) {
            }
            try {
                alcohol = json.getInt("numalcohol");
            } catch (Exception w) {
            }
            try {
                smoke = json.getInt("numcigarro");
            } catch (Exception w) {
            }
            try {
                spirituality = json.getInt("numespiritualidad");
            } catch (Exception w) {
            }
            try {
                civilstate = json.getInt("numestadocivil");
            } catch (Exception w) {
            }
            try {
                siblings = json.getInt("numhijos");
            } catch (Exception w) {
            }

        } catch (Exception w) {
        }
    }

    public void setFotografias(JSONObject fotografias) {
        JSONArray datos;
        listaFotos = new ArrayList<>();
        try {
            for (int index = 0; index < fotografias.length(); index++) {
                JSONObject fotoobject = (JSONObject) fotografias.get(fotografias.names().getString(index));
                Foto f = new Foto(fotoobject);

                listaFotos.add(f);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setFotografias(ArrayList<Foto> lista) {
        listaFotos = lista;
    }

    public ArrayList<Foto> getFotografias() {
        return listaFotos;
    }

    public String getFacebookId() {
        return facebookid;
    }

    public void setFacebookId(String facebookid) {
        this.facebookid = facebookid;
    }

    public String getEmail() {
        return stremail;
    }

    public void setEmail(String stremail) {
        this.stremail = stremail;
    }

    public String getFechaNacimiento() {
        return datefechanacimiento;
    }

    public void setFechaNacimiento(String datefechanacimiento) {
        this.datefechanacimiento = datefechanacimiento;
    }

    public String getGender() {
        return strgender;
    }

    public void setGender(String strgender) {
        this.strgender = strgender;
    }

    public String getFirstname() {
        return strfirstname;
    }

    public void setFirstname(String strfirstname) {
        this.strfirstname = strfirstname;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPrefer() {
        return prefer;
    }

    public int getMinedad() {
        return minedad;
    }

    public int getMaxedad() {
        return maxedad;
    }

    public int getUbicacion() {
        return ubicacion;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getSkin() {
        return skin;
    }

    public void setSkin(int skin) {
        this.skin = skin;
    }

    public int getHair() {
        return hair;
    }

    public void setHair(int hair) {
        this.hair = hair;
    }

    public int getHairColor() {
        return haircolor;
    }

    public void setHairColor(int haircolor) {
        this.haircolor = haircolor;
    }

    public int getRace() {
        return race;
    }

    public void setRace(int race) {
        this.race = race;
    }

    public int getOccupation() {
        return occupation;
    }

    public void setOccupation(int occupation) {
        this.occupation = occupation;
    }

    public int getScholarship() {
        return scholarship;
    }

    public void setScholarship(int scholarship) {
        this.scholarship = scholarship;
    }

    public int getAlcohol() {
        return alcohol;
    }

    public void setAlcohol(int alcohol) {
        this.alcohol = alcohol;
    }

    public int getSmoke() {
        return smoke;
    }

    public void setSmoke(int smoke) {
        this.smoke = smoke;
    }

    public int getSpirituality() {
        return spirituality;
    }

    public void setSpirituality(int spirituality) {
        this.spirituality = spirituality;
    }

    public int getCivilState() {
        return civilstate;
    }

    public void setCivilState(int civilstate) {
        this.civilstate = civilstate;
    }

    public int getSiblings() {
        return siblings;
    }

    public void setSiblings(int siblings) {
        this.siblings = siblings;
    }
}
