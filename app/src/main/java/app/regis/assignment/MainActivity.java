package app.ellie.assignment;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    public static FragmentManager fragmentManager;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private TextView text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager =  getSupportFragmentManager();
        if (findViewById(R.id.FragmentContainer) != null) {
            if (savedInstanceState != null) {
                return;
            }
            FragmentTransaction ft = fragmentManager.beginTransaction();
            SaveFragment F1 = new SaveFragment();
            ft.replace(R.id.FragmentContainer, F1, null)
                    .addToBackStack(null)
                    .commit();
        }

    }

    public void nfc(){
        text = findViewById(R.id.id);
        final String text = this.text.getText().toString();

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter == null){
            Toast.makeText(this, "No NFC", Toast.LENGTH_LONG).show();
            return;
        }

        pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this,
                        this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

    }

    @Override
    public void onResume() {
        super.onResume();

        if(nfcAdapter != null){
            if (!nfcAdapter.isEnabled()) showWirelessSettings();
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(nfcAdapter != null){
            nfcAdapter.disableForegroundDispatch(this);

        }
    }

    private void showWirelessSettings() {
        Toast.makeText(this, "You need to enable NFC", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        startActivity(intent);
    }

}
