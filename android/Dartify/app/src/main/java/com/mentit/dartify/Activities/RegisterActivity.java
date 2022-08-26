package com.mentit.dartify.Activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog.Builder;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.mentit.dartify.HelperClasses.PermissionCallback;
import com.mentit.dartify.HelperClasses.SharedPreferenceUtils;
import com.mentit.dartify.Models.ElementoLista;
import com.mentit.dartify.Models.POJO.User.Foto;
import com.mentit.dartify.Models.POJO.User.Usuario;
import com.mentit.dartify.Models.PerfilCard;
import com.mentit.dartify.Models.ViewModel.PerfilCardViewModel;
import com.mentit.dartify.Network.NetworkResponse;
import com.mentit.dartify.Network.NetworkService;
import com.mentit.dartify.R;
import com.mentit.dartify.Tasks.User.DownloadFacebookImagesTask;
import com.mentit.dartify.Tasks.User.GetUserProfileTask;
import com.mentit.dartify.util.Valores;

import org.joda.time.LocalDate;
import org.joda.time.Years;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import es.dmoral.toasty.Toasty;
import io.apptik.widget.MultiSlider;

public class RegisterActivity extends AppCompatActivity implements
        GetUserProfileTask.OnTaskCompleted,
        PermissionCallback.IPermissionResponse,
        AccessToken.AccessTokenRefreshCallback,
        DownloadFacebookImagesTask.OnTaskCompleted,
        FacebookCallback<LoginResult> {
    private Context context;
    private CheckBox checkTOS;

    private Usuario usuarioRegistrar;

    private String estado;
    private String prefer;
    private String saludo;

    private int rangoMin;
    private int rangoMax;

    private EditText editTextName;
    private EditText editTextMail;
    private TextView textViewBirthday;
    private RadioButton radioButtonMale;
    private RadioButton radioButtonFemale;
    private Spinner spinnerEstados;
    private Spinner spinnerPrefiero;
    private MultiSlider sliderRange1;
    private TextView textViewRangeEdad;
    private EditText editTextSaludo;
    private Button botonRegistrar;

    private List<ElementoLista> sexoData = new ArrayList<>();
    private List<ElementoLista> estadoubicacionData = new ArrayList<>();

    private PerfilCardViewModel pviewmodel;
    private long buttonLastTimeClick = 0;
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Bundle b = getIntent().getExtras();
        usuarioRegistrar = (Usuario) b.getSerializable("userlogueado");
        context = this;
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager, this);

        if (usuarioRegistrar.getId() != -1) {
            setContentView(R.layout.blank_layout);
            cargarFotosFacebook();
        } else {

            setContentView(R.layout.activity_register);

            editTextName = findViewById(R.id.editTextName);
            editTextMail = findViewById(R.id.editTextEmail);
            textViewBirthday = findViewById(R.id.textViewBirthday);
            radioButtonFemale = findViewById(R.id.radioButtonGenderFemale);
            radioButtonMale = findViewById(R.id.radioButtonGenderMale);
            spinnerEstados = findViewById(R.id.spinnerState);
            spinnerPrefiero = findViewById(R.id.spinnerPrefer);
            botonRegistrar = findViewById(R.id.buttonRegister);
            checkTOS = findViewById(R.id.checkTOS);
            sliderRange1 = findViewById(R.id.range_slider1);
            textViewRangeEdad = findViewById(R.id.textViewRange2);
            editTextSaludo = findViewById(R.id.editTextSaludo);

            fillSexo();
            fillEstadoUbicacion();

            ArrayAdapter<ElementoLista> adapter1;
            adapter1 = new ArrayAdapter<>(this, R.layout.spinner_item, estadoubicacionData);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerEstados.setAdapter(adapter1);

            ArrayAdapter<ElementoLista> adapter2;
            adapter2 = new ArrayAdapter<>(this, R.layout.spinner_item, sexoData);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerPrefiero.setAdapter(adapter2);

            //Configurar nombre
            editTextName.setText(usuarioRegistrar.getFirstname());

            if (usuarioRegistrar.getFirstname().length() > 0) {
                editTextName.setEnabled(false);
            }

            //Configurar email
            editTextMail.setText(usuarioRegistrar.getEmail());

            if (usuarioRegistrar.getEmail().length() > 0) {
                editTextMail.setEnabled(false);
            }

            //Configurar fecha de nacimiento
            textViewBirthday.setText(usuarioRegistrar.getFechaNacimiento());

            if (usuarioRegistrar.getFechaNacimiento().length() != 10) {
                textViewBirthday.setText(R.string.presstoselect);
                textViewBirthday.setOnClickListener(v -> datePicker());
            }

            //Configurar sexo
            radioButtonFemale.setEnabled(false);
            radioButtonMale.setEnabled(false);

            if (usuarioRegistrar.getGender().equals("male")) {
                radioButtonMale.setChecked(true);
            } else if (usuarioRegistrar.getGender().equals("female")) {
                radioButtonFemale.setChecked(true);
            } else {
                radioButtonMale.setEnabled(true);
                radioButtonFemale.setEnabled(true);
            }

            //Configurar slider de edad
            sliderRange1.setMin(18);
            sliderRange1.setMax(99);

            rangoMin = sliderRange1.getMin();
            rangoMax = sliderRange1.getMax();

            actualizarViewEdad();

            sliderRange1.setOnThumbValueChangeListener(slideListener);

            //Configurar boton TOS y boton de registro
            botonRegistrar.setEnabled(false);
            checkTOS.setOnClickListener(botonTOSClick);
            botonRegistrar.setOnClickListener(botonRegistroClick);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        pviewmodel = ViewModelProviders.of(this).get(PerfilCardViewModel.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
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

    private View.OnClickListener botonTOSClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            checkTOS.setChecked(false);
            showTOS();
        }
    };

    private View.OnClickListener botonRegistroClick = v -> {
        try {
            //Bloqueo de botón
            if (SystemClock.elapsedRealtime() - buttonLastTimeClick < Valores.TIEMPO_LONG) return;
            buttonLastTimeClick = SystemClock.elapsedRealtime();

            registrar();
        } catch (IOException e) {
            Log.d("IOEXCEPTION", e.getMessage());
            e.printStackTrace();
        }
    };

    private void datePicker() {
        Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        int dia = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int mes = Calendar.getInstance().get(Calendar.MONTH);
        int anio = Calendar.getInstance().get(Calendar.YEAR);

        try {
            String[] partes = usuarioRegistrar.getFechaNacimiento().split("-");
            anio = Integer.parseInt(partes[0]);
            mes = Integer.parseInt(partes[1]) - 1;
            dia = Integer.parseInt(partes[2]);
        } catch (Exception w) {

        }

        DatePickerDialog datePickerDialog;
        datePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, (view, year, month, dayOfMonth) -> {
            Calendar newDate = Calendar.getInstance();
            newDate.set(year, month, dayOfMonth);
            usuarioRegistrar.setFechaNacimiento(dateFormatter.format(newDate.getTime()));
            textViewBirthday.setText(usuarioRegistrar.getFechaNacimiento());
            int edad = calcularEdad(usuarioRegistrar.getFechaNacimiento());
            int edadmin = 0;
            int edadmax = 0;

            edadmin = edad - 5;
            edadmin = edadmin < 18 ? 18 : edadmin;

            edadmax = edad + 5;
            edadmax = edadmax > 99 ? 99 : edadmax;

            sliderRange1.clearThumbs();
            sliderRange1.addThumbOnPos(0, edadmin);
            sliderRange1.addThumbOnPos(1, edadmax);
        }, anio, mes, dia);

        datePickerDialog.show();
    }

    private void showTOS() {
        String tos = "";
        try {
            Resources res = getResources();
            InputStream is = res.openRawResource(R.raw.terms);
            byte[] b = new byte[is.available()];
            is.read(b);
            tos = new String(b);
        } catch (Exception e) {
        }

        Builder builder = new Builder(this);
        builder.setTitle(R.string.TOSTitle);
        builder.setMessage(tos);

        builder.setPositiveButton(R.string.positiveButton, (dialog, which) -> {
            checkTOS.setChecked(true);
            botonRegistrar.setEnabled(true);
            Log.d("CLICK dialog", "ACEPTAR");
        });

        builder.setNegativeButton(R.string.negativeButton, (dialog, which) -> {
            checkTOS.setChecked(false);
            botonRegistrar.setEnabled(false);
            Log.d("CLICK DIALOG", "CANCELAR");
        });

        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void actualizarViewEdad() {
        textViewRangeEdad.setText(rangoMin + " - " + rangoMax);
    }

    public static int calcularEdad(String fecha) {
        String[] partes = fecha.split("-");

        LocalDate fechaNacimiento = new LocalDate(Integer.parseInt(partes[0]), Integer.parseInt(partes[1]), Integer.parseInt(partes[2]));
        LocalDate hoy = new LocalDate();
        int edad = Years.yearsBetween(fechaNacimiento, hoy).getYears();

        return edad;
    }

    private void registrar() throws IOException {

        estado = ((ElementoLista) spinnerEstados.getSelectedItem()).getID() + "";
        prefer = ((ElementoLista) spinnerPrefiero.getSelectedItem()).getID() + "";

        if (!Patterns.EMAIL_ADDRESS.matcher(editTextMail.getText().toString()).matches()) {
            Toasty.error(this, getString(R.string.wrongemail), Toast.LENGTH_SHORT, true).show();
            return;
        }

        if (!radioButtonFemale.isChecked() && !radioButtonMale.isChecked()) {
            Toasty.error(this, getString(R.string.choosesex), Toast.LENGTH_SHORT, true).show();
            return;
        }

        if (textViewBirthday.getText().toString().length() != 10) {
            Toasty.error(this, getString(R.string.choosebirthday), Toast.LENGTH_SHORT, true).show();
            return;
        }

        if (calcularEdad(textViewBirthday.getText().toString()) < 18) {
            Toasty.error(this, getString(R.string.mustbelegal), Toast.LENGTH_SHORT, true).show();
            return;
        }

        if (prefer.equals("-1")) {
            Toasty.success(this, "Elige el sexo que prefieres conocer", Toast.LENGTH_LONG, true).show();
            return;
        }

        Set<String> permissions = AccessToken.getCurrentAccessToken().getPermissions();

        if (!permissions.contains("user_photos")) {
            Toasty.success(this, "Debes autorizar el permiso de fotografías ", Toast.LENGTH_LONG, true).show();
            LoginManager.getInstance().logInWithReadPermissions(RegisterActivity.this, Arrays.asList("user_photos"));
            return;
        }

        usuarioRegistrar.setEmail(editTextMail.getText().toString());
        usuarioRegistrar.setFechaNacimiento(textViewBirthday.getText().toString());
        saludo = editTextSaludo.getText().toString();

        if (radioButtonFemale.isChecked()) {
            usuarioRegistrar.setGender("1");
        }

        if (radioButtonMale.isChecked()) {
            usuarioRegistrar.setGender("0");
        }

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("facebookid", usuarioRegistrar.getFacebookId());
            jsonObject.put("strfirstname", usuarioRegistrar.getFirstname());
            jsonObject.put("stremail", usuarioRegistrar.getEmail());
            jsonObject.put("datefechanacimiento", usuarioRegistrar.getFechaNacimiento());
            jsonObject.put("strgender", usuarioRegistrar.getGender());
            jsonObject.put("strestado", estado);
            jsonObject.put("strprefer", prefer);
            jsonObject.put("numrangomin", rangoMin);
            jsonObject.put("numrangomax", rangoMax);
            jsonObject.put("strsaludo", saludo);

        } catch (JSONException e) {
            Log.d("postbody", e.getMessage());
        }

        registrarUsuario(getString(R.string.usuario_registro), jsonObject);
    }

    public void registrarUsuario(final String query, final JSONObject bodydata) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<String> credentials = new ArrayList<>();
                credentials.add("DARTIFY-API-KEY");
                credentials.add("REGISTRO");

                final NetworkResponse respuesta = NetworkService.INSTANCE.put(query, bodydata, credentials);

                if (!respuesta.error()) {
                    RegisterActivity.this.runOnUiThread(() -> {
                        try {
                            long tmpid;
                            tmpid = Long.parseLong(respuesta.getDatos().get("userid").toString());
                            usuarioRegistrar.setId(tmpid);
                            cargarFotosFacebook();
                        } catch (JSONException w) {
                            RegisterActivity.this.runOnUiThread(() -> Toasty.error(RegisterActivity.this, respuesta.getMensaje(), Toast.LENGTH_SHORT, true).show());
                        }
                    });
                } else {
                    RegisterActivity.this.runOnUiThread(() -> Toasty.error(RegisterActivity.this, respuesta.getMensaje(), Toast.LENGTH_SHORT, true).show());
                }
            }
        }).start();
    }

    private void cargarFotosFacebook() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("userid", usuarioRegistrar.getId());

            JSONArray jsonArray = new JSONArray();
            int max = usuarioRegistrar.getFotografias().size() >= 5 ? 5 : usuarioRegistrar.getFotografias().size();

            for(int index = 0; index < max; index++){
                Foto item = usuarioRegistrar.getFotografias().get(index);
                JSONObject foto = new JSONObject();
                foto.put("width", item.getAncho());
                foto.put("height", item.getAlto());
                foto.put("url", item.getSource());

                jsonArray.put(foto);
            }

            jsonObject.put("fotos", jsonArray);

        } catch (JSONException e) {
            Log.d("postbody", e.getMessage());
        }

        guardarFotos(getString(R.string.usuario_fotografias), jsonObject);
    }

    public void guardarFotos(final String query, final JSONObject bodydata) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<String> credentials = new ArrayList<>();
                credentials.add("DARTIFY-API-KEY");
                credentials.add("REGISTRO");

                final NetworkResponse respuesta = NetworkService.INSTANCE.put(query, bodydata, credentials);

                if (!respuesta.error()) {
                    RegisterActivity.this.runOnUiThread(() -> new GetUserProfileTask(RegisterActivity.this, usuarioRegistrar.getId()).execute(""));
                } else {
                    RegisterActivity.this.runOnUiThread(() -> Toasty.error(RegisterActivity.this, respuesta.getMensaje(), Toast.LENGTH_SHORT, true).show());
                }
            }
        }).start();
    }

    @Override
    public void OnTaskCompletedProfile(JSONObject datos) {
        try {
            Usuario u = new Usuario(datos);

            PerfilCard p = new PerfilCard(u.getId());
            p.setStrfirstname(u.getFirstname());
            p.setStremail(u.getEmail());
            p.setDatefechanacimiento(u.getFechaNacimiento());
            p.setStrgender(u.getGender());
            p.setUbicacion(u.getUbicacion());
            p.setPrefer(u.getPrefer());
            p.setMinedad(u.getMinedad());
            p.setMaxedad(u.getMaxedad());
            p.setFacebookid(u.getFacebookId());
            p.setWeight(u.getWeight());
            p.setHeight(u.getHeight());
            p.setSkin(u.getSkin());
            p.setHair(u.getHair());
            p.setHaircolor(u.getHairColor());
            p.setRace(u.getRace());
            p.setOccupation(u.getOccupation());
            p.setScholarship(u.getScholarship());
            p.setAlcohol(u.getAlcohol());
            p.setSmoke(u.getSmoke());
            p.setSpirituality(u.getSpirituality());
            p.setSpirituality(u.getSpirituality());
            p.setCivilstate(u.getCivilState());
            p.setSiblings(u.getSiblings());

            pviewmodel.insert(p);

            SharedPreferenceUtils.getInstance(context).setValue(context.getString(R.string.user_id), u.getId());
            SharedPreferenceUtils.getInstance(context).setValue(context.getString(R.string.user_name), u.getFirstname());
        } catch (Exception w) {
        }

        startActivity(new Intent(this, PhotosetActivity.class));
        finish();
    }

    @Override
    public void OnTokenRefreshed(AccessToken accessToken) {
        if (accessToken == null) {
            LoginManager.getInstance().logOut();
            Intent loginIntent = new Intent(RegisterActivity.this, FacebookLoginActivity.class);
            startActivity(loginIntent);
        } else {
            new DownloadFacebookImagesTask(RegisterActivity.this, accessToken).execute("");
        }
    }

    @Override
    public void OnTokenRefreshFailed(FacebookException exception) {

    }

    @Override
    public void onCompleted(GraphResponse response) {
        if (response.getError() != null) {
            Toasty.error(this, "Hubo un error con los permisos", Toast.LENGTH_LONG, true).show();
        }
        AccessToken.refreshCurrentAccessTokenAsync(RegisterActivity.this);
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        AccessToken.refreshCurrentAccessTokenAsync(this);
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onError(FacebookException error) {

    }

    @Override
    public void OnTaskCompleted(ArrayList lista) {
        usuarioRegistrar.setFotografias(lista);
        Toasty.info(this, "Permiso actualizado", Toast.LENGTH_SHORT, true).show();
    }

    private void fillSexo() {
        sexoData.clear();
        sexoData.add(new ElementoLista(-1, "-"));
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
}