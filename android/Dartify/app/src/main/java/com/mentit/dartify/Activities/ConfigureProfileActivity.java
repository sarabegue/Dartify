package com.mentit.dartify.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.mentit.dartify.Adapters.ListaAdapter;
import com.mentit.dartify.HelperClasses.SharedPreferenceUtils;
import com.mentit.dartify.Models.ElementoLista;
import com.mentit.dartify.Models.PerfilCard;
import com.mentit.dartify.Models.ViewModel.MatchCardViewModel;
import com.mentit.dartify.Models.ViewModel.PerfilCardViewModel;
import com.mentit.dartify.Network.NetworkResponse;
import com.mentit.dartify.Network.NetworkService;
import com.mentit.dartify.R;
import com.mentit.dartify.util.Valores;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import io.apptik.widget.MultiSlider;

public class ConfigureProfileActivity extends AppCompatActivity {
    Handler mh = new Handler();
    PerfilCard perfil = null;

    private TextView textViewRangeEdad;
    private EditText edittextSaludo;
    private Spinner spinnerPrefer;
    private Spinner spinnerState;

    private Spinner spinnerHeight;
    private Spinner spinnerWeight;
    private Spinner spinnerSkin;
    private Spinner spinnerHairType;
    private Spinner spinnerHairColor;
    private Spinner spinnerRace;
    private Spinner spinnerOccupation;
    private Spinner spinnerScholarship;
    private Spinner spinnerAlcohol;
    private Spinner spinnerSmoke;
    private Spinner spinnerSpirituality;
    private Spinner spinnerCivilStatus;
    private Spinner spinnerSiblings;
    private MultiSlider sliderRange1;
    private Button buttonGuardar;
    private long buttonLastTimeClick = 0;

    private int prefer;
    private int rangoMin;
    private int rangoMax;
    private int location;
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

    private List<ElementoLista> sexoData = new ArrayList<>();
    private List<ElementoLista> estadoubicacionData = new ArrayList<>();
    private List<ElementoLista> alcoholData = new ArrayList<>();
    private List<ElementoLista> fumaData = new ArrayList<>();
    private List<ElementoLista> pielData = new ArrayList<>();
    private List<ElementoLista> estaturaData = new ArrayList<>();
    private List<ElementoLista> pesoData = new ArrayList<>();
    private List<ElementoLista> hairtypeData = new ArrayList<>();
    private List<ElementoLista> hairData = new ArrayList<>();
    private List<ElementoLista> raceData = new ArrayList<>();
    private List<ElementoLista> occupationData = new ArrayList<>();
    private List<ElementoLista> spiritualityData = new ArrayList<>();
    private List<ElementoLista> civilstateData = new ArrayList<>();
    private List<ElementoLista> siblingsData = new ArrayList<>();
    private List<ElementoLista> scholarData = new ArrayList<>();

    private ListaAdapter adapter1;
    private ListaAdapter adapter2;
    private ListaAdapter adapter3;
    private ListaAdapter adapter4;
    private ListaAdapter adapter5;
    private ListaAdapter adapter6;
    private ListaAdapter adapter7;
    private ListaAdapter adapter8;
    private ListaAdapter adapter9;
    private ListaAdapter adapter10;
    private ListaAdapter adapter11;
    private ListaAdapter adapter12;
    private ListaAdapter adapter13;
    private ListaAdapter adapter14;
    private ListaAdapter adapter15;

    private PerfilCardViewModel mcvm;
    private MatchCardViewModel viewmodel;
    private long userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_configure_profile);

        edittextSaludo = findViewById(R.id.editTextSaludo);
        spinnerState = findViewById(R.id.spinnerState);
        spinnerPrefer = findViewById(R.id.spinnerPrefer);
        spinnerAlcohol = findViewById(R.id.spinnerAlcohol);
        spinnerSmoke = findViewById(R.id.spinnerSmoke);
        spinnerHeight = findViewById(R.id.spinnerHeight);
        spinnerWeight = findViewById(R.id.spinnerWeight);
        spinnerSkin = findViewById(R.id.spinnerSkin);
        spinnerHairType = findViewById(R.id.spinnerHairType);
        spinnerHairColor = findViewById(R.id.spinnerHairColor);
        spinnerRace = findViewById(R.id.spinnerRace);
        spinnerOccupation = findViewById(R.id.spinnerOccupation);
        spinnerScholarship = findViewById(R.id.spinnerScholarship);
        spinnerSpirituality = findViewById(R.id.spinnerSpirituality);
        spinnerCivilStatus = findViewById(R.id.spinnerCivilStatus);
        spinnerSiblings = findViewById(R.id.spinnerSiblings);
        sliderRange1 = findViewById(R.id.range_slider1);
        buttonGuardar = findViewById(R.id.buttonSave);
        textViewRangeEdad = findViewById(R.id.textViewRange2);

        fillSexo();
        fillEstadoUbicacion();
        fillAlcohol();
        fillFuma();
        fillPiel();
        fillEstatura();
        fillPeso();
        fillTipoCabello();
        fillCabello();
        fillRaza();
        fillOcupacion();
        fillEspiritualidad();
        fillEstadoCivil();
        fillHijos();
        fillEscolaridad();

        //cargar datos interfaz
        adapter1 = new ListaAdapter(this, R.layout.spinner_item, estadoubicacionData);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerState.setAdapter(adapter1);

        adapter2 = new ListaAdapter(this, R.layout.spinner_item, sexoData);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPrefer.setAdapter(adapter2);

        adapter3 = new ListaAdapter(this, R.layout.spinner_item, alcoholData);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAlcohol.setAdapter(adapter3);

        adapter4 = new ListaAdapter(this, R.layout.spinner_item, fumaData);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSmoke.setAdapter(adapter4);

        adapter5 = new ListaAdapter(this, R.layout.spinner_item, pielData);
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSkin.setAdapter(adapter5);

        adapter6 = new ListaAdapter(this, R.layout.spinner_item, estaturaData);
        adapter6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHeight.setAdapter(adapter6);

        adapter7 = new ListaAdapter(this, R.layout.spinner_item, hairtypeData);
        adapter7.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHairType.setAdapter(adapter7);

        adapter8 = new ListaAdapter(this, R.layout.spinner_item, hairData);
        adapter8.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHairColor.setAdapter(adapter8);

        adapter9 = new ListaAdapter(this, R.layout.spinner_item, raceData);
        adapter9.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRace.setAdapter(adapter9);

        adapter10 = new ListaAdapter(this, R.layout.spinner_item, occupationData);
        adapter10.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOccupation.setAdapter(adapter10);

        adapter11 = new ListaAdapter(this, R.layout.spinner_item, spiritualityData);
        adapter11.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSpirituality.setAdapter(adapter11);

        adapter12 = new ListaAdapter(this, R.layout.spinner_item, civilstateData);
        adapter12.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCivilStatus.setAdapter(adapter12);

        adapter13 = new ListaAdapter(this, R.layout.spinner_item, siblingsData);
        adapter13.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSiblings.setAdapter(adapter13);

        adapter14 = new ListaAdapter(this, R.layout.spinner_item, scholarData);
        adapter14.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerScholarship.setAdapter(adapter14);

        adapter15 = new ListaAdapter(this, R.layout.spinner_item, pesoData);
        adapter15.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWeight.setAdapter(adapter15);

        //configurar elementos de interfaz de acuerdo a las preferencias seleccionadas
        sliderRange1.setMin(18);
        sliderRange1.setMax(99);

        //configurar eventos
        sliderRange1.setOnThumbValueChangeListener(slideListener);
        buttonGuardar.setOnClickListener(botonGuardarClick);

        userid = SharedPreferenceUtils.getInstance(this).getLongValue(this.getString(R.string.user_id), 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        mcvm = ViewModelProviders.of(this).get(PerfilCardViewModel.class);
        viewmodel = ViewModelProviders.of(this).get(MatchCardViewModel.class);
        new GetPerfilAsyncTask(this, userid).execute();
    }

    public void cargarDatosUsuario(PerfilCard p) {
        if (p != null) {
            perfil = p;

            prefer = p.getPrefer();
            rangoMin = p.getMinedad();
            rangoMax = p.getMaxedad();
            location = p.getUbicacion();

            height = p.getHeight();
            weight = p.getWeight();
            skin = p.getSkin();
            hair = p.getHair();
            haircolor = p.getHaircolor();
            race = p.getRace();
            occupation = p.getOccupation();
            scholarship = p.getScholarship();
            alcohol = p.getAlcohol();
            smoke = p.getSmoke();
            spirituality = p.getSpirituality();
            civilstate = p.getCivilstate();
            siblings = p.getSiblings();

            spinnerState.setSelection(adapter1.getPosition(location));
            spinnerPrefer.setSelection(adapter2.getPosition(prefer));
            spinnerAlcohol.setSelection(adapter3.getPosition(alcohol));
            spinnerSmoke.setSelection(adapter4.getPosition(smoke));
            spinnerSkin.setSelection(adapter5.getPosition(skin));
            spinnerHeight.setSelection(adapter6.getPosition(height));
            spinnerHairType.setSelection(adapter7.getPosition(hair));
            spinnerHairColor.setSelection(adapter8.getPosition(haircolor));
            spinnerRace.setSelection(adapter9.getPosition(race));
            spinnerOccupation.setSelection(adapter10.getPosition(occupation));
            spinnerSpirituality.setSelection(adapter11.getPosition(spirituality));
            spinnerCivilStatus.setSelection(adapter12.getPosition(civilstate));
            spinnerSiblings.setSelection(adapter13.getPosition(siblings));
            spinnerScholarship.setSelection(adapter14.getPosition(scholarship));
            spinnerWeight.setSelection(adapter15.getPosition(weight));

            sliderRange1.clearThumbs();
            sliderRange1.addThumbOnPos(0, rangoMin);
            sliderRange1.addThumbOnPos(1, rangoMax);

            actualizarViewEdad();
        }
    }

    private View.OnClickListener botonGuardarClick = v -> {
        try {
            if (SystemClock.elapsedRealtime() - buttonLastTimeClick < Valores.TIEMPO_LONG) {
                return;
            }

            buttonLastTimeClick = SystemClock.elapsedRealtime();

            registrar();
        } catch (Exception e) {
            //Log.d("EXCEPTION", e.getMessage());
            //e.printStackTrace();
        }
    };

    private static class GetPerfilAsyncTask extends AsyncTask<Void, Void, Void> {
        private WeakReference<AppCompatActivity> weakActivity;
        private PerfilCardViewModel mcvm;
        private PerfilCard dato;
        private long userid;

        public GetPerfilAsyncTask(AppCompatActivity activity, long userid) {
            weakActivity = new WeakReference<>(activity);
            mcvm = ViewModelProviders.of(activity).get(PerfilCardViewModel.class);
            this.userid = userid;
        }

        @Override
        protected Void doInBackground(Void... params) {
            dato = mcvm.getPerfil(userid);
            return null;
        }

        @Override
        protected void onPostExecute(Void x) {
            Activity activity = weakActivity.get();

            if (activity == null) return;

            ((ConfigureProfileActivity) activity).cargarDatosUsuario(dato);
        }
    }

    private void registrar() throws IOException {
        String mensaje = edittextSaludo.getText().toString();

        prefer = ((ElementoLista) spinnerPrefer.getSelectedItem()).getID();
        location = ((ElementoLista) spinnerState.getSelectedItem()).getID();

        height = ((ElementoLista) spinnerHeight.getSelectedItem()).getID();
        weight = ((ElementoLista) spinnerWeight.getSelectedItem()).getID();
        skin = ((ElementoLista) spinnerSkin.getSelectedItem()).getID();
        hair = ((ElementoLista) spinnerHairType.getSelectedItem()).getID();
        haircolor = ((ElementoLista) spinnerHairColor.getSelectedItem()).getID();
        race = ((ElementoLista) spinnerRace.getSelectedItem()).getID();
        occupation = ((ElementoLista) spinnerOccupation.getSelectedItem()).getID();
        scholarship = ((ElementoLista) spinnerScholarship.getSelectedItem()).getID();
        alcohol = ((ElementoLista) spinnerAlcohol.getSelectedItem()).getID();
        smoke = ((ElementoLista) spinnerSmoke.getSelectedItem()).getID();
        spirituality = ((ElementoLista) spinnerSpirituality.getSelectedItem()).getID();
        civilstate = ((ElementoLista) spinnerCivilStatus.getSelectedItem()).getID();
        siblings = ((ElementoLista) spinnerSiblings.getSelectedItem()).getID();

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("userid", SharedPreferenceUtils.getInstance(this).getLongValue(getString(R.string.user_id), 0));

            jsonObject.put("numubicacion", location);
            jsonObject.put("numsexobuscado", prefer);
            jsonObject.put("numminedad", rangoMin);
            jsonObject.put("nummaxedad", rangoMax);

            jsonObject.put("numpeso", weight);
            jsonObject.put("numestatura", height);
            jsonObject.put("numcolorpiel", skin);
            jsonObject.put("numtipocabello", hair);
            jsonObject.put("numcolorcabello", haircolor);
            jsonObject.put("numraza", race);
            jsonObject.put("numocupacion", occupation);
            jsonObject.put("numnivelestudios", scholarship);
            jsonObject.put("numalcohol", alcohol);
            jsonObject.put("numcigarro", smoke);
            jsonObject.put("numespiritualidad", spirituality);
            jsonObject.put("numestadocivil", civilstate);
            jsonObject.put("numhijos", siblings);

            jsonObject.put("strsaludo", mensaje);

            perfil.setUbicacion(location);
            perfil.setPrefer(prefer);
            perfil.setMinedad(rangoMin);
            perfil.setMaxedad(rangoMax);

            perfil.setWeight(weight);
            perfil.setHeight(height);
            perfil.setSkin(skin);
            perfil.setHair(hair);
            perfil.setHaircolor(haircolor);
            perfil.setRace(race);
            perfil.setOccupation(occupation);
            perfil.setScholarship(scholarship);
            perfil.setAlcohol(alcohol);
            perfil.setSmoke(smoke);
            perfil.setSpirituality(spirituality);
            perfil.setCivilstate(civilstate);
            perfil.setSiblings(siblings);

            mcvm.update(perfil);

        } catch (JSONException e) {
            //Log.d("postbody", e.getMessage());
        }

        actualizarInformacion(getString(R.string.usuario_perfil), jsonObject);
    }

    public void actualizarInformacion(final String query, final JSONObject bodydata) {
        new Thread(() -> {
            final List<String> credentials = new ArrayList<>();
            credentials.add("DARTIFY-API-KEY");
            credentials.add("REGISTRO");

            final NetworkResponse respuesta = NetworkService.INSTANCE.put(query, bodydata, credentials);

            mh.post(() -> {
                if (!respuesta.error()) {
                    ConfigureProfileActivity.this.runOnUiThread(() -> {

                        viewmodel.deleteAll();

                        Intent i = new Intent(ConfigureProfileActivity.this, MainActivity.class);
                        startActivity(i);
                    });
                } else {
                    ConfigureProfileActivity.this.runOnUiThread(() -> Toasty.error(ConfigureProfileActivity.this, respuesta.getMensaje(), Toast.LENGTH_SHORT, true).show());
                }
            });
        }).start();
    }

    private void actualizarViewEdad() {
        textViewRangeEdad.setText(rangoMin + " - " + rangoMax);
    }

    private MultiSlider.OnThumbValueChangeListener slideListener = new MultiSlider.OnThumbValueChangeListener() {
        @Override
        public void onValueChanged(MultiSlider multiSlider, MultiSlider.Thumb thumb, int thumbIndex, int value) {
            if (thumbIndex == 0) {
                rangoMin = value;
                Log.d("EDAD min", value + "");
            }
            if (thumbIndex == 1) {
                rangoMax = value;
                Log.d("EDAD max", value + "");
            }

            actualizarViewEdad();
        }
    };

    private void fillSexo() {
        sexoData.clear();
        sexoData.add(new ElementoLista(0, "Hombre"));
        sexoData.add(new ElementoLista(1, "Mujer"));
        sexoData.add(new ElementoLista(2, "Ambos"));
    }

    private void fillEstadoUbicacion() {
        estadoubicacionData.clear();
        estadoubicacionData.add(new ElementoLista(0, "Cualquiera"));
        estadoubicacionData.add(new ElementoLista(1, "CDMX"));
        estadoubicacionData.add(new ElementoLista(2, "Aguascalientes"));
        estadoubicacionData.add(new ElementoLista(3, "Baja California"));
        estadoubicacionData.add(new ElementoLista(4, "Baja California Sur"));
        estadoubicacionData.add(new ElementoLista(5, "Campeche"));
        estadoubicacionData.add(new ElementoLista(6, "Coahuila"));
        estadoubicacionData.add(new ElementoLista(7, "Chiapas"));
        estadoubicacionData.add(new ElementoLista(8, "Chihuahua"));
        estadoubicacionData.add(new ElementoLista(9, "Durango"));
        estadoubicacionData.add(new ElementoLista(10, "Guanajuato"));
        estadoubicacionData.add(new ElementoLista(11, "Guerrero"));
        estadoubicacionData.add(new ElementoLista(12, "Hidalgo"));
        estadoubicacionData.add(new ElementoLista(13, "Jalisco"));
        estadoubicacionData.add(new ElementoLista(14, "México"));
        estadoubicacionData.add(new ElementoLista(15, "Michoacán"));
        estadoubicacionData.add(new ElementoLista(16, "Morelos"));
        estadoubicacionData.add(new ElementoLista(17, "Nayarit"));
        estadoubicacionData.add(new ElementoLista(18, "Nuevo León"));
        estadoubicacionData.add(new ElementoLista(19, "Oaxaca"));
        estadoubicacionData.add(new ElementoLista(20, "Puebla"));
        estadoubicacionData.add(new ElementoLista(21, "Querétaro"));
        estadoubicacionData.add(new ElementoLista(22, "Quintana Roo"));
        estadoubicacionData.add(new ElementoLista(23, "San Luis Potosí"));
        estadoubicacionData.add(new ElementoLista(24, "Sinaloa"));
        estadoubicacionData.add(new ElementoLista(25, "Sonora"));
        estadoubicacionData.add(new ElementoLista(26, "Tabasco"));
        estadoubicacionData.add(new ElementoLista(27, "Tamaulipas"));
        estadoubicacionData.add(new ElementoLista(28, "Tlaxcala"));
        estadoubicacionData.add(new ElementoLista(29, "Veracruz"));
        estadoubicacionData.add(new ElementoLista(30, "Yucatán"));
        estadoubicacionData.add(new ElementoLista(31, "Zacatecas"));
    }

    private void fillPiel() {
        pielData.clear();
        pielData.add(new ElementoLista(0, "-"));
        pielData.add(new ElementoLista(1, "Moreno"));
        pielData.add(new ElementoLista(2, "Blanco"));
    }

    private void fillEstatura() {
        estaturaData.clear();
        estaturaData.add(new ElementoLista(0, "-"));
        estaturaData.add(new ElementoLista(1, "Menos de 120 cm"));
        for (int index = 120; index < 200; index++) {
            ElementoLista e = new ElementoLista(index);
            e.setUnidad("cm");
            estaturaData.add(e);
        }
    }

    private void fillPeso() {
        pesoData.clear();
        pesoData.add(new ElementoLista(0, "-"));
        pesoData.add(new ElementoLista(1, "Menos de 45 kg"));
        for (int index = 45; index < 150; index++) {
            ElementoLista e = new ElementoLista(index);
            e.setUnidad("kg");
            pesoData.add(e);
        }
    }

    private void fillTipoCabello() {
        hairtypeData.clear();
        hairtypeData.add(new ElementoLista(0, "-"));
        hairtypeData.add(new ElementoLista(1, "Lacio"));
        hairtypeData.add(new ElementoLista(2, "Rizado"));
        hairtypeData.add(new ElementoLista(3, "Ondulado"));
    }

    private void fillCabello() {
        hairData.clear();
        hairData.add(new ElementoLista(0, "-"));
        hairData.add(new ElementoLista(1, "Negro"));
        hairData.add(new ElementoLista(2, "Castaño"));
        hairData.add(new ElementoLista(3, "Rubio"));
        hairData.add(new ElementoLista(4, "Rojizo"));
    }

    private void fillRaza() {
        raceData.clear();
        raceData.add(new ElementoLista(0, "-"));
        raceData.add(new ElementoLista(1, "Latino"));
        raceData.add(new ElementoLista(2, "Oriental"));
        raceData.add(new ElementoLista(3, "Chino"));
    }

    private void fillOcupacion() {
        occupationData.clear();
        occupationData.add(new ElementoLista(0, "-"));
        occupationData.add(new ElementoLista(1, "Desempleado"));
        occupationData.add(new ElementoLista(2, "Empresario"));
        occupationData.add(new ElementoLista(3, "Ejecutivo"));
        occupationData.add(new ElementoLista(4, "Estudiante"));
    }

    private void fillEspiritualidad() {
        spiritualityData.clear();
        spiritualityData.add(new ElementoLista(0, "-"));
        spiritualityData.add(new ElementoLista(1, "Católica"));
        spiritualityData.add(new ElementoLista(2, "Cristianismo"));
        spiritualityData.add(new ElementoLista(3, "Budista"));
        spiritualityData.add(new ElementoLista(4, "Testigo de Jehová"));
        spiritualityData.add(new ElementoLista(5, "Mormona"));
        spiritualityData.add(new ElementoLista(6, "Cábala"));
        spiritualityData.add(new ElementoLista(7, "Judaísmo"));
        spiritualityData.add(new ElementoLista(8, "Protestante"));
        spiritualityData.add(new ElementoLista(9, "Espiritual pero no religioso"));
        spiritualityData.add(new ElementoLista(10, "Ateo"));
        spiritualityData.add(new ElementoLista(11, "Agnosticismo"));
    }

    private void fillEstadoCivil() {
        civilstateData.clear();
        civilstateData.add(new ElementoLista(0, "-"));
        civilstateData.add(new ElementoLista(1, "Soltero"));
        civilstateData.add(new ElementoLista(2, "Casado"));
        civilstateData.add(new ElementoLista(3, "Divorciado"));
    }

    private void fillEscolaridad() {
        scholarData.clear();
        scholarData.add(new ElementoLista(0, "-"));
        scholarData.add(new ElementoLista(1, "Secundaria o menor"));
        scholarData.add(new ElementoLista(2, "Media Superior"));
        scholarData.add(new ElementoLista(3, "Superior"));
        scholarData.add(new ElementoLista(4, "Posgrado"));
    }

    private void fillHijos() {
        siblingsData.clear();
        siblingsData.add(new ElementoLista(0, "-"));
        siblingsData.add(new ElementoLista(1, "No"));
        siblingsData.add(new ElementoLista(2, "Sí"));
    }

    private void fillAlcohol() {
        alcoholData.clear();
        alcoholData.add(new ElementoLista(0, "-"));
        alcoholData.add(new ElementoLista(1, "Nunca"));
        alcoholData.add(new ElementoLista(2, "Socialmente"));
        alcoholData.add(new ElementoLista(3, "Regularmente"));
    }

    private void fillFuma() {
        fumaData.clear();
        fumaData.add(new ElementoLista(0, "-"));
        fumaData.add(new ElementoLista(1, "Nunca"));
        fumaData.add(new ElementoLista(2, "Socialmente"));
        fumaData.add(new ElementoLista(3, "Regularmente"));
    }

}
