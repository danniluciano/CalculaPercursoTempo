package br.com.danielaluciano.calculapercurso_tempo;

import android.Manifest;
import android.Manifest.permission;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button permissaoButton;
    private Button ativarGPSButton;
    private Button desativarGPSButton;
    private Button iniciarPercursoButton;
    private Button terminarPercusoButton;
    private TextView valorDistanciaTextView;
    private Chronometer cronometro;
    private LocationManager locationManager;
    private static final int REQ_PERMISSAO_GPS = 1;
    private boolean teste = false;
    private boolean testeGPS = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permissaoButton = (Button)
                findViewById(R.id.permissaoButton);
        ativarGPSButton = (Button)
                findViewById(R.id.ativarGPSButton);
        desativarGPSButton = (Button)
                findViewById(R.id.desativarGPSButton);
        iniciarPercursoButton = (Button)
                findViewById(R.id.iniciarPercursoButton);
        terminarPercusoButton = (Button)
                findViewById(R.id.terminarPercusoButton);
        valorDistanciaTextView = (TextView)
                findViewById(R.id.valorDistanciaTextView);
        cronometro = (Chronometer)
                findViewById(R.id.cronometro);
        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
    }

    private MeuObservadorDeLocalizacoes observer
            = new MeuObservadorDeLocalizacoes();

    private class MeuObservadorDeLocalizacoes
            implements LocationListener {


        @Override
        public void onLocationChanged(Location location) {
            double latitude =
                    location.getLatitude();
            double longitude =
                    location.getLongitude();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQ_PERMISSAO_GPS){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0,observer);
                }
            }
            else{
                Toast.makeText(this,
                        getString(R.string.explicacao_gps),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            //Permissão Button
            case R.id.permissaoButton:
                if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED){ //Se o usuário já permitiu mandar mensagem de aviso
                    Toast.makeText(this,getString(R.string.gps_ja_ativado),Toast.LENGTH_SHORT).show();
                }
                else{
                    ActivityCompat.requestPermissions(this,new String[]{ //Senão permitiu, faz a solicitação
                            permission.ACCESS_FINE_LOCATION }, REQ_PERMISSAO_GPS);
                }
                break;

            //Ativar GPS Button
            case R.id.ativarGPSButton:
                if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED){ //Se já foi permitido ativa
                    testeGPS = true;
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0,observer);
                    Toast.makeText(this,getString(R.string.ativando_gps),Toast.LENGTH_SHORT).show();

                }
                else{ //Senão avisa que precisa permitir para ativar
                    Toast.makeText(this,getString(R.string.explicacao_gps),Toast.LENGTH_SHORT).show();
                }
                break;

            //Desativar GPS Button
            case R.id.desativarGPSButton:
                if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED){ //Se já foi permitido ativa
                    testeGPS = false;
                    locationManager.removeUpdates(observer);
                    Toast.makeText(this,getString(R.string.desativando_gps),Toast.LENGTH_SHORT).show();
                }
                else{ //Senão avisa que precisa permitir para ativar
                    Toast.makeText(this,getString(R.string.gps_ja_desligado),Toast.LENGTH_SHORT).show();
                }
                break;

            //Iniciar Percuso Button
            case R.id.iniciarPercursoButton:
                if (teste == true) {
                    Toast.makeText(this,getString(R.string.percurso_ja_iniciado),Toast.LENGTH_SHORT).show();
                }
                else if (testeGPS == false) {
                    Toast.makeText(this,getString(R.string.desativando_gps),Toast.LENGTH_SHORT).show();
                }

                else {
                    teste = true;
                    cronometro.setBase(SystemClock.elapsedRealtime());
                    cronometro.start();
                }
                break;

            //Terminar Percuso Button
            case R.id.terminarPercusoButton:
            if (teste == false) {
                Toast.makeText(this,getString(R.string.percurso_ja_pausado),Toast.LENGTH_SHORT).show();
            }
            else{
                teste = false;
                cronometro.stop();
            }
                break;
            default:
                break;
        }

    }


}

