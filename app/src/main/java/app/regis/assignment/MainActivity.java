package app.regis.assignment;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static FragmentManager fragmentManager;
    private final String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    DatabaseHelper mydb;
    EditText sName, password, email;
    private Button button, viewBtn, updatebtn, delete;
    private Spinner spinner1, spinner2;
    private DatabaseHelper db;
    private boolean isValid = true;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private TextView text;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mydb = new DatabaseHelper(this);
        button = findViewById(R.id.save);
        viewBtn = findViewById(R.id.readBtn);
        updatebtn = findViewById(R.id.update);
        delete = findViewById(R.id.delete);
        ReadData();

        sName = findViewById(R.id.name);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        db = new DatabaseHelper(this);
        text = findViewById(R.id.id);




        Spinner session = findViewById(R.id.session_spinner);
        AdapterView.OnItemSelectedListener listener = session.getOnItemSelectedListener();
        session.setOnItemSelectedListener(listener);
        List<String> sessions = new ArrayList<String>();
        sessions.add("Session 1");
        sessions.add("Session 2");
        sessions.add("Session 3");

        ArrayAdapter<String> adapterSession = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, sessions);
        adapterSession.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        session.setAdapter(adapterSession);

        Spinner dept = findViewById(R.id.dept_spinner);
        AdapterView.OnItemSelectedListener listener1 = session.getOnItemSelectedListener();
        session.setOnItemSelectedListener(listener1);
        List<String> deptList = new ArrayList<String>();
        deptList.add("Department 1");
        deptList.add("Department 2");
        deptList.add("Department 3");

        ArrayAdapter<String> deptAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, deptList);
        deptAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dept.setAdapter(deptAdapter);

        nfc();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateFieldsAndSave();
            }
        });
        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateandUpdate();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteElement();
            }
        });


    }

    private void validateFieldsAndSave() {
        final String name = sName.getText().toString();
        final String ID = text.getText().toString();
        final String password = this.password.getText().toString();
        final String email = this.email.getText().toString();

        if (name.trim().isEmpty()) {
            sName.setError("Name should not be empty");
            isValid = false;
        }
        if (!email.trim().matches(EMAIL_PATTERN) ||
                email.trim().isEmpty()) {
            this.email.setError("Invalid email");
            isValid = false;
        }
        if (password.trim().isEmpty()) {
            this.password.setError("Invalid password");
            isValid = false;
        }

        if (!isValid) return;

        //SAVE IN THE DATABASE
        if (db.insertData(name, password, email)!=-1) {
//            FragmentTransaction ft = fragmentManager.beginTransaction();
//            SuccessFragment fragment = new SuccessFragment();
//            final Bundle args = new Bundle();
//            args.putString(SuccessFragment.NAME_PARAM,ID);
//            args.putString(SuccessFragment.NAME_PARAM,name);
//            args.putString(SuccessFragment.EMAIL_PARAM,email);
//            args.putString(SuccessFragment.PASSWORD_PARAM,password);
//            fragment.setArguments(args);
//            ft.replace(R.id.FragmentContainer, fragment, null)
//                    .addToBackStack(null)
//                    .commit();
            Toast.makeText(this, "saved successfully", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "they was an issue", Toast.LENGTH_SHORT).show();
            return;
        }

    }

    private void ValidateandUpdate(){
        final String name = sName.getText().toString();
        final String password = this.password.getText().toString();
        final String email = this.email.getText().toString();

        if (name.trim().isEmpty()) {
            sName.setError("Name should not be empty");
            isValid = false;
        }
        if (!email.trim().matches(EMAIL_PATTERN) ||
                email.trim().isEmpty()) {
            this.email.setError("Invalid email");
            isValid = false;
        }
        if (password.trim().isEmpty()) {
            this.password.setError("Invalid password");
            isValid = false;
        }

        if (!isValid) return;
        //update IN THE DATABASE
        if (db.updateData(name, password, email)!=-1) {
            Toast.makeText(this, "update successfully", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "student Don't exist in system", Toast.LENGTH_SHORT).
                    show();
            return;
        }

    }

    public void deleteElement(){
        final String name = sName.getText().toString();
        if (!name.isEmpty()) {
            db.deleteData(name);
            Toast.makeText(this, "value Deleted", Toast.LENGTH_SHORT).show();}
        else Toast.makeText(this, "Please enter  name", Toast.LENGTH_SHORT).show();
    }


    public void ReadData(){
        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor results = mydb.ReadData();
                if (results.getCount() == 0){
                    showMessage("Error", "Nothing to show");
                }
                StringBuffer sb = new StringBuffer();
                while (results.moveToNext()){
                    sb.append("ReadNo : "+ results.getString(0) + "\n" );
                    sb.append("Name : "+ results.getString(1) + "\n" );
                    sb.append("Password : "+ results.getString(2) + "\n" );
                    sb.append("Email : "+ results.getString(3) + "\n\n" );
                }
                showMessage("Students Record", sb.toString());
            }
        });
    }

    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder((this));
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }


    public void nfc(){

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
    public void onNewIntent(Intent intent) {
        setIntent(intent);
        resolveIntent(intent);
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

    public void displayMsgs(NdefMessage[] msgs) {
        if (msgs == null || msgs.length == 0) return;

        StringBuilder builder = new StringBuilder();
        List<ParsedNdefRecord> records = NdefMessageParser.parse(msgs[0]);
        final int size = records.size();

        for (int i = 0; i < size; i++) {
            ParsedNdefRecord record = records.get(i);
            String str = record.str();
            builder.append(str).append("\n");
        }

        text.setText(builder.toString());

    }

    public void showWirelessSettings() {
        Toast.makeText(this, "You need to enable NFC", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        startActivity(intent);
    }








    public void resolveIntent(Intent intent) {
        String action = intent.getAction();

        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action) || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action) ||
                NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)){
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs;
            if (rawMsgs != null){
                msgs = new NdefMessage[rawMsgs.length];

                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            }else {
                byte[] empty = new byte[0];
                byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
                Tag tag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                byte[] payload = dumpTagData(tag).getBytes();
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload);
                NdefMessage msg = new NdefMessage(new NdefRecord[]{record});
                msgs = new NdefMessage[]{msg};

            }

            displayMsgs(msgs);
        }
    }

    public String dumpTagData(Tag tag) {
        StringBuilder sb = new StringBuilder();
        byte[] id = tag.getId();
        sb.append("ID (hex): ").append(getHex(id)).append('\n');
        sb.append("ID (reversed hex): ").append(getReversed(id)).append('\n');
        sb.append("ID (dec): ").append(getDec(id)).append('\n');
        sb.append("ID (reversed dec): ").append(getReversed(id)).append('\n');

        String prefix = "android.nfc.tech.";
        sb.append("Technologies: ");
        for (String tech : tag.getTechList()) {
            sb.append(tech.substring(prefix.length()));
            sb.append(", ");
        }

        sb.delete(sb.length() - 2, sb.length());

        for (String tech : tag.getTechList()) {
            if (tech.equals(MifareClassic.class.getName())) {
                sb.append('\n');
                String type = "Unknown";

                try {
                    MifareClassic mifareTag = MifareClassic.get(tag);

                    switch (mifareTag.getType()) {
                        case MifareClassic.TYPE_CLASSIC:
                            type = "Classic";
                            break;
                        case MifareClassic.TYPE_PLUS:
                            type = "Plus";
                            break;
                        case MifareClassic.TYPE_PRO:
                            type = "Pro";
                            break;
                    }
                    sb.append("Mifare Classic type: ");
                    sb.append(type);
                    sb.append('\n');

                    sb.append("Mifare size: ");
                    sb.append(mifareTag.getSize() + " bytes");
                    sb.append('\n');

                    sb.append("Mifare sectors: ");
                    sb.append(mifareTag.getSectorCount());
                    sb.append('\n');

                    sb.append("Mifare blocks: ");
                    sb.append(mifareTag.getBlockCount());
                } catch (Exception e) {
                    sb.append("Mifare classic error: " + e.getMessage());
                }
            }

            if (tech.equals(MifareUltralight.class.getName())) {
                sb.append('\n');
                MifareUltralight mifareUlTag = MifareUltralight.get(tag);
                String type = "Unknown";
                switch (mifareUlTag.getType()) {
                    case MifareUltralight.TYPE_ULTRALIGHT:
                        type = "Ultralight";
                        break;
                    case MifareUltralight.TYPE_ULTRALIGHT_C:
                        type = "Ultralight C";
                        break;
                }
                sb.append("Mifare Ultralight type: ");
                sb.append(type);
            }
        }

        return sb.toString();
    }

    private String getHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; --i) {
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
            if (i > 0) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    private long getDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = 0; i < bytes.length; ++i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    private long getReversed(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = bytes.length - 1; i >= 0; --i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }







}
