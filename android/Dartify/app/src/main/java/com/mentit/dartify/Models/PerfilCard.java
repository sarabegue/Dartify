package com.mentit.dartify.Models;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "PerfilCard", indices = {@Index(value = {"userid"}, unique = true)})
public class PerfilCard {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private long userid;
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

    public PerfilCard(long userid) {
        this.userid = userid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public String getFacebookid() {
        return facebookid;
    }

    public void setFacebookid(String facebookid) {
        this.facebookid = facebookid;
    }

    public String getStremail() {
        return stremail;
    }

    public void setStremail(String stremail) {
        this.stremail = stremail;
    }

    public String getDatefechanacimiento() {
        return datefechanacimiento;
    }

    public void setDatefechanacimiento(String datefechanacimiento) {
        this.datefechanacimiento = datefechanacimiento;
    }

    public String getStrgender() {
        return strgender;
    }

    public void setStrgender(String strgender) {
        this.strgender = strgender;
    }

    public String getStrfirstname() {
        return strfirstname;
    }

    public void setStrfirstname(String strfirstname) {
        this.strfirstname = strfirstname;
    }

    public int getPrefer() {
        return prefer;
    }

    public void setPrefer(int prefer) {
        this.prefer = prefer;
    }

    public int getMinedad() {
        return minedad;
    }

    public void setMinedad(int minedad) {
        this.minedad = minedad;
    }

    public int getMaxedad() {
        return maxedad;
    }

    public void setMaxedad(int maxedad) {
        this.maxedad = maxedad;
    }

    public int getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(int ubicacion) {
        this.ubicacion = ubicacion;
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

    public int getHaircolor() {
        return haircolor;
    }

    public void setHaircolor(int haircolor) {
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

    public int getCivilstate() {
        return civilstate;
    }

    public void setCivilstate(int civilstate) {
        this.civilstate = civilstate;
    }

    public int getSiblings() {
        return siblings;
    }

    public void setSiblings(int siblings) {
        this.siblings = siblings;
    }
}
