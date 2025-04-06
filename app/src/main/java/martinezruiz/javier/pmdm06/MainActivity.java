package martinezruiz.javier.pmdm06;

import static android.view.View.GONE;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationBarView;

import martinezruiz.javier.pmdm06.databinding.ActivityMainBinding;
import martinezruiz.javier.pmdm06.viewmodels.ControlPointsViewModel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        controlPointsViewModel = new ViewModelProvider(this,
                ViewModelProvider.Factory.from(ControlPointsViewModel.initializer))
                .get(ControlPointsViewModel.class);

        //Instanciamos el navHostFragment
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
        }

        NavigationUI.setupWithNavController(binding.navView, navController);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            binding.navView.setVisibility(GONE);
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        binding.navView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getTitle().toString()){
                    case "Settings":
                            navController.navigate(R.id.settings_fragment);
                        break;
                        case "Map":
                            getLocation();

                        break;
                        case "Summary":
                            navController.navigate(R.id.summary_fragment);
                        break;

                }
                     return true;
            }
        });



    }


    private void permissionDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Esta función necesita permisos de ubicación");
        // Add the buttons.
        builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                locationPermissionRequest.launch(new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                        }
                );
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {

                    Boolean fineLocationGranted = null;
                    Boolean coarseLocationGranted = null;

                    fineLocationGranted = result.getOrDefault(
                            Manifest.permission.ACCESS_FINE_LOCATION, false);
                    coarseLocationGranted = result.getOrDefault(
                            Manifest.permission.ACCESS_COARSE_LOCATION,false);

                    if (fineLocationGranted != null && fineLocationGranted) {

                        getLocation();

                    } else if (coarseLocationGranted != null && coarseLocationGranted) {
                        getLocation();
                    }
                    else {
                        // No location access granted.
                    }
                });




//    ------------------- NAVEGACIÓN -----------------------------------

    private void getLocation(){

        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {

                            if (location != null) {
                                currentLocation = location;
                                controlPointsViewModel.setCurrentPosition(new LatLng(location.getLatitude(), location.getLongitude()));
                                navController.navigate(R.id.map_fragment);
                            }
                        }
                    });
        }
        else{
            permissionDialog();
        }
    }


    private FusedLocationProviderClient fusedLocationClient;
    NavController navController;








    boolean hasPermisions;
    Location currentLocation;
    ControlPointsViewModel controlPointsViewModel;
}