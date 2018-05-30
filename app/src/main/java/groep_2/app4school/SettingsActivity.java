package groep_2.app4school;

//import android.app.Fragment;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final Switch wifiSwitch = (Switch) findViewById(R.id.switch1);
        TextView textWifi = (TextView) findViewById(R.id.textWifi);
        Boolean wifiSwitchState = wifiSwitch.isChecked();


        if (wifiSwitch.isChecked()) {
            textWifi.setText("Wifi is on");
        }

        wifiSwitch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                final TextView textWifi = (TextView) findViewById(R.id.textWifi);
                final Boolean wifiSwitchState = wifiSwitch.isChecked();

                if (!wifiSwitchState) {
                    textWifi.setText("Wifi is off");
                    wifiManager.setWifiEnabled(false);


                }

                if (wifiSwitchState) {
                    textWifi.setText("Wifi is on");
                    wifiManager.setWifiEnabled(true);
                }
            }
        });




        Switch silentSwitch = (Switch) findViewById(R.id.switch1);
        TextView textSilent = (TextView) findViewById(R.id.textSilent);
        final AudioManager am = (AudioManager) getSystemService(getApplicationContext().AUDIO_SERVICE);
        /*if (silentSwitch.isChecked()) {
            textSilent.setText("Phone is on silent mode");
        }*/
        silentSwitch.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                        && !notificationManager.isNotificationPolicyAccessGranted()) {

                    Intent intent = new Intent(
                            android.provider.Settings
                                    .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);

                    startActivity(intent);
                }


                //am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            }

        });

    }
}