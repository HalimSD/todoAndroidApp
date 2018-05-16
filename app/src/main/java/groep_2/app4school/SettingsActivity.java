package groep_2.app4school;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final Switch wifiSwitch = (Switch) findViewById(R.id.switch1);
        TextView textView = (TextView) findViewById(R.id.textView);
        Boolean switchState = wifiSwitch.isChecked();
        if (wifiSwitch.isChecked()){
            textView.setText("Wifi is on");
        }

        wifiSwitch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                final TextView textView = (TextView) findViewById(R.id.textView);
                final Boolean switchState = wifiSwitch.isChecked();

                if (!switchState) {
                    textView.setText("Wifi is off");
                    wifiManager.setWifiEnabled(false);
                }

                if (switchState) {
                    textView.setText("Wifi is on");
                    wifiManager.setWifiEnabled(true);
                }
            }
        });
    }
}
