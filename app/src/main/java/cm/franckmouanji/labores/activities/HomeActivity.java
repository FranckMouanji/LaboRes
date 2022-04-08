package cm.franckmouanji.labores.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.text.ParseException;

import cm.franckmouanji.labores.R;
import cm.franckmouanji.labores.databinding.ActivityHomeBinding;
import cm.franckmouanji.labores.systeme.ActionAboutUser;
import cm.franckmouanji.labores.systeme.Controller;
import cm.franckmouanji.labores.systeme.DialogInformAdd;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        binding.appBarMain.toolbar.findViewById(R.id.show_program).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Controller.createPdf("program", Controller.listReservation, HomeActivity.this);
                } catch (FileNotFoundException | ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        //getInformation to connexion
        Intent intent = getIntent();
        if(intent.hasExtra("connexion")){
            String message = intent.getStringExtra("connexion");
            if(message.equals("false")){
                DialogInformAdd.fatalErrorDialog(HomeActivity.this);
            }
        }

        //get nombre utilisateur
        ActionAboutUser.recupererNombreUtilisateur();

        Controller.haveStoragePermission(HomeActivity.this);

        Controller.veriVersion(HomeActivity.this);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_historique, R.id.nav_a_propos)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}