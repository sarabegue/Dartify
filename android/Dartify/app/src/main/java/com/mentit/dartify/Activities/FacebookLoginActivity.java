package com.mentit.dartify.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.mentit.dartify.HelperClasses.SharedPreferenceUtils;
import com.mentit.dartify.Models.POJO.User.Usuario;
import com.mentit.dartify.Network.NetworkResponse;
import com.mentit.dartify.Network.NetworkService;
import com.mentit.dartify.R;
import com.mentit.dartify.Tasks.User.DownloadFacebookImagesTask;
import com.mentit.dartify.util.Valores;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class FacebookLoginActivity extends AppCompatActivity implements DownloadFacebookImagesTask.OnTaskCompleted {
    private ImageView ivfondo;
    private CallbackManager callbackManager;
    private AccessToken accessToken;
    private LoginResult facebookLoginResult;
    private LoginButton loginButton;

    private long buttonLastTimeClick = 0;

    private Usuario userLogueado;
    private ArrayList listaFotosFacebook;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        context = this;

        AccessToken token = AccessToken.getCurrentAccessToken();

        if (token != null) {
            AccessToken.refreshCurrentAccessTokenAsync();

            SharedPreferenceUtils.getInstance(context).setValue("useridchat", 0L);
            if (getIntent().getExtras() != null) {
                SharedPreferenceUtils.getInstance(context).setValue("useridchat",
                        Long.parseLong(getIntent().getExtras().getString("userid1"))
                );
            }
            
            Intent i;
            i = new Intent(this, MainActivity.class);
            startActivity(i);
        }

        setContentView(R.layout.activity_facebook_login);
        ivfondo = findViewById(R.id.ivfondo);
        ivfondo.setBackgroundResource(getRandomBackground());

        userLogueado = new Usuario();

        loginButton = findViewById(R.id.login_button);

        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, facebookCallback);
        loginButton.setPermissions(Arrays.asList("email", "user_gender", "public_profile", "user_birthday", "user_photos"));
    }

    @Override
    public void onResume() {
        super.onResume();
        long userid1;
        userid1 = SharedPreferenceUtils.getInstance(context).getLongValue(this.getString(R.string.user_id), 0);

        if (userid1 > 0) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Bloqueo de botón
        if (SystemClock.elapsedRealtime() - buttonLastTimeClick < Valores.TIEMPO_SUPERLONG) return;
        buttonLastTimeClick = SystemClock.elapsedRealtime();

        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private FacebookCallback<LoginResult> facebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(final LoginResult loginResult) {
            loginButton.setVisibility(View.INVISIBLE);
            String queryFields = "id,name,first_name,middle_name,last_name,email,gender,birthday,cover,picture";
            facebookLoginResult = loginResult;
            accessToken = loginResult.getAccessToken();

            GraphRequest request = GraphRequest.newMeRequest(accessToken, graphRequestQueryUserData);

            Bundle params = new Bundle();
            params.putString("fields", queryFields);
            request.setParameters(params);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
            Toasty.error(FacebookLoginActivity.this, "Inicio de sesión cancelado", Toast.LENGTH_SHORT, true).show();
        }

        @Override
        public void onError(FacebookException error) {
            Log.d("FACEBOOK", "onerror");
            Toasty.error(FacebookLoginActivity.this, "Error de conexión", Toast.LENGTH_SHORT, true).show();
        }
    };

    private GraphRequest.GraphJSONObjectCallback graphRequestQueryUserData = new GraphRequest.GraphJSONObjectCallback() {
        @Override
        public void onCompleted(JSONObject object, GraphResponse response) {
            try {
                userLogueado.setFacebookId(facebookLoginResult.getAccessToken().getUserId());
            } catch (Exception e) {
            }

            try {
                userLogueado.setFirstname(object.getString("first_name"));
            } catch (Exception e) {
            }

            try {
                userLogueado.setGender(object.getString("gender"));
            } catch (Exception e) {
            }

            try {
                userLogueado.setEmail(object.getString("email"));
            } catch (Exception e) {
            }

            try {
                String bday = object.getString("birthday");
                if (bday.length() > 0) {
                    String[] partes = bday.split("/");
                    bday = partes[2] + "-" + partes[0] + "-" + partes[1];
                }
                userLogueado.setFechaNacimiento(bday);
            } catch (Exception e) {
            }

            new DownloadFacebookImagesTask(FacebookLoginActivity.this, accessToken).execute("");
        }
    };

    @Override
    public void OnTaskCompleted(ArrayList lfb) {
        //Imágenes descargadas
        listaFotosFacebook = lfb;
        revisarRegistroPrevio(getString(R.string.usuario_facebookuser) + userLogueado.getFacebookId());
    }

    private void revisarRegistroPrevio(final String query) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<String> credentials = new ArrayList<>();
                credentials.add("DARTIFY-API-KEY");
                credentials.add("REGISTRO");

                final NetworkResponse respuesta = NetworkService.INSTANCE.get(query, credentials);

                if (respuesta == null) {
                    FacebookLoginActivity.this.runOnUiThread(() -> Toasty.error(FacebookLoginActivity.this, "Error de conexión", Toast.LENGTH_SHORT, true).show());
                    return;
                }

                if (respuesta != null || !respuesta.error()) {
                    FacebookLoginActivity.this.runOnUiThread(() -> {
                        userLogueado.setId(new Usuario(respuesta.getDatos()).getId());
                        userLogueado.setFotografias(listaFotosFacebook);

                        navegacionFlujo2();
                    });
                } else {
                    FacebookLoginActivity.this.runOnUiThread(() -> Toasty.error(FacebookLoginActivity.this, respuesta.getMensaje(), Toast.LENGTH_SHORT, true).show());
                }
            }
        }).start();
    }

    /**
     * Si usuario está registrado validar fotografías.
     * Si usuario no está registrado enviarlo al registro.
     */
    private void navegacionFlujo2() {
        Intent i = new Intent(this, RegisterActivity.class);
        i.putExtra("userlogueado", userLogueado);
        startActivity(i);
    }

    private int getRandomBackground() {
        int numero = (int) (Math.random() * 10);

        switch (numero % 3) {
            case 0: {
                return R.drawable.background1;
            }
            case 1: {
                return R.drawable.background2;
            }
            default: {
                return R.drawable.background3;
            }
        }
    }
}