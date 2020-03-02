package app.ellie.assignment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    public static FragmentManager fragmentManager;


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
}
