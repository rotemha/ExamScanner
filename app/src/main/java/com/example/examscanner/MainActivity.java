package com.example.examscanner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.examscanner.communication.ContextProvider;
import com.example.examscanner.log.ESLogeerFactory;
import com.example.examscanner.persistence.local.AppDatabaseFactory;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.navigation.NavigationView;

import org.opencv.android.OpenCVLoader;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 0;
    private AppBarConfiguration mAppBarConfiguration;
    private MainActivityViewModel viewModel;
    private static boolean stubsMode = false;
    public static boolean testMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onResume();
        OpenCVLoader.initDebug();
        if(stubsMode)StubsSetupManager.setup(getBaseContext());
        if(testMode){
            AppDatabaseFactory.setTestMode();
        }
        ContextProvider.set(this.getApplicationContext());
        MainActivityViewModelFactory factory = new MainActivityViewModelFactory();
        viewModel = new ViewModelProvider(this,factory).get(MainActivityViewModel.class);
        if(!viewModel.isAuthenticated()){
            navigateToAuthentication();
        }else{
            createHome();
        }
        ESLogeerFactory.getInstance().logmem();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void navigateToAuthentication() {
        // Choose authentication providers
//        List<AuthUI.IdpConfig> providers = Arrays.asList(
//                new AuthUI.IdpConfig.EmailBuilder().build(),
//                new AuthUI.IdpConfig.GoogleBuilder().build());

// Create and launch sign-in intent
        startActivityForResult(
                viewModel.generateAuthenticationIntent(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                viewModel.authenticate();
                createHome();
            } else {
                ESLogeerFactory.getInstance().log(
                        TAG,
                        String.format(
                                "%s\n"+
                                        "response.getError().getErrorCode()=%d\n"+
                                        "response.getError().getMessage()=%s"
                                ,
                                response.getError().getErrorCode(),
                                response.getError().getMessage()
                        )
                );
            }
        }
    }

    public void createHome() {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }


}
