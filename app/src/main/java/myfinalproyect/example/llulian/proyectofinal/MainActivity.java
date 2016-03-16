package myfinalproyect.example.llulian.proyectofinal;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import android.content.pm.Signature;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    // GoogleApiClient es un objeto que sirve como punto de entrada para los servicios de Google Play
    GoogleApiClient client;

    // El objeto AdView se usa para mostrar la Vista de las publicidades
    private AdView adView;
    // Este objeto se usa desde el onActivityResult() para gestionar los callbacks del FacebookSdk
    private CallbackManager cM;
    // Botón de login
    private LoginButton lB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Se inicializa el FacebookSdk
        FacebookSdk.sdkInitialize(getApplicationContext());

        // Se gestiona la devolución del callback
        cM = CallbackManager.Factory.create();
        getFacebookKeyHash("I/Pbx660u92LhEjVVYs2+uUU1YE=");

        setContentView(R.layout.activity_main);

        // Se establece la relación entre el banner del layout y el adView que corresponde a la publicidad
        adView = (AdView)findViewById(R.id.ad_view);
        // Se establece la relación entre el LoginButton del layout y su respectivo objeto en el MainActivity.java
        lB = (LoginButton) findViewById(R.id.login_facebook);

        // Se realiza la solicitud de publicidad
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        adView.loadAd(adRequest);

        // registerCallback usado para controlar el resultado del login de Facebook
        lB.registerCallback(cM, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(MainActivity.this, "¡Inicio de sesión exitoso!", Toast.LENGTH_SHORT);
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "¡Inicio de sesión cancelado!", Toast.LENGTH_SHORT);
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(MainActivity.this, "¡Inicio de sesión no exitoso!", Toast.LENGTH_SHORT);
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    // Método con el que se valida si el key hash que se tiene es el correcto o no
    public void getFacebookKeyHash(String packageName) {

        try {
            // Se traen las firmas y se usa un ciclo for para recorrer a las mismas y obtener, entre ellas, el KeyHash
            PackageInfo info = getPackageManager().getPackageInfo(
                    packageName, PackageManager.GET_SIGNATURES);

            for(Signature signature : info.signatures){
                // Objeto usado para capturar la instancia SHA
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                // Se realiza un Log del KeyHash
                Log.e("KeyHash: ", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                System.out.println("KeyHash: " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        }catch(PackageManager.NameNotFoundException e){


        }catch(NoSuchAlgorithmException e){

        }

    }

    // Método usado para llamar el onActivityResult() y pasar los resultados del inicio de sesión al LoginManager a través del CallbackManager
    protected void onActivityResult(int reqCode, int resCode, Intent i){
        cM.onActivityResult(reqCode, resCode, i);
    }

    // MÉTODOS SOBREESCRITOS DEL BANNER PUBLICITARIO
    // Método para destruir la vista de la publicidad
    @Override
    protected void onDestroy() {
        if(adView != null){
            adView.destroy();
        }
        super.onDestroy();
    }

    // Método que pausa cualquier procesamiento extra asociado con el adView de la publicidad
    @Override
    protected void onPause() {
        if(adView != null){
            adView.pause();
        }
        super.onPause();
    }

    // Con este método se resume o retoma un adView después de que el procesamiento de este haya sido pausado con el método onPause()
    @Override
    protected void onResume() {
        if(adView != null){
            adView.resume();
        }
        super.onResume();
    }
}
