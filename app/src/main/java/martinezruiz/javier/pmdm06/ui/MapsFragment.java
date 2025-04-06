package martinezruiz.javier.pmdm06.ui;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import martinezruiz.javier.pmdm06.R;
import martinezruiz.javier.pmdm06.SensorTest;
import martinezruiz.javier.pmdm06.models.ControlPoint;
import martinezruiz.javier.pmdm06.viewmodels.ControlPointsViewModel;

public class MapsFragment extends Fragment {

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            googleMap.getUiSettings().setCompassEnabled(true);
            map = googleMap;

            if(controlPointsList != null &&  !controlPointsList.isEmpty()){


                controlPointsList.forEach(c -> {
                    LatLng latLng = new LatLng(c.getLatitude(), c.getLongitude());
                    googleMap.addMarker(new MarkerOptions().position(latLng).title(c.getName()));
                });

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                                currentPosition.latitude, currentPosition.longitude),
                        googleMap.getMaxZoomLevel()));
            }

            sensorTest.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    Log.d(evt.getNewValue()+" new value", evt.getNewValue()+ "new value");

                    if(evt.getPropertyName().equals("mValuesAccel")){

                        mValuesAccel = applyLowPassFilter((float[])evt.getNewValue(), mValuesAccel);
//                        System.arraycopy((float[])evt.getNewValue(), 0, mValuesAccel, 0, 3);


                    }
                    else if(evt.getPropertyName().equals("mValuesMagnet")){
                        mValuesMagnet = applyLowPassFilter((float[]) evt.getNewValue(), mValuesMagnet);
//                        System.arraycopy((float[])evt.getNewValue(), 0, mValuesMagnet, 0, 3);

                    }
                    SensorManager.getRotationMatrix(mRotationMatrix, null, mValuesAccel, mValuesMagnet);
                    SensorManager.getOrientation(mRotationMatrix, mValuesOrientation);

                    float incl = mValuesOrientation[1];
                    float gradosa = incl * 57.295f;
                    float grados = Math.abs(incl * 57.295f);
                    Log.d(gradosa+"", "GRADOS");
//                    grados = grados >= 90 ? 90: grados;


                    float bearing = mValuesOrientation[0];
                    float degree = bearing * 57.295f;
                    float zoom = map.getCameraPosition().zoom;

                            map.moveCamera(CameraUpdateFactory.newCameraPosition(
                                    CameraPosition.builder()
                                            .target(currentPosition)
                                            .bearing(degree)
                                            .tilt(grados)
                                            .zoom(zoom)
                                            .build()));

                }
            });


        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        controlPointsViewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.Factory.from(ControlPointsViewModel.initializer))
                .get(ControlPointsViewModel.class);
        sensorTest = new SensorTest(requireActivity());



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        controlPointsViewModel.getControlPointsList().observe(getViewLifecycleOwner(), controlPoints -> {
            controlPointsList = controlPoints;
        });

        controlPointsViewModel.getCurrentPosition().observe(getViewLifecycleOwner(), currentPosition -> {
            this.currentPosition = currentPosition;
        });

//        controlPointsViewModel.getInclinacion().observe(getViewLifecycleOwner(), incl ->{
//            float grados = incl * 57.295f;
//
//            map.moveCamera(CameraUpdateFactory.newCameraPosition(
//                    CameraPosition.builder()
//                            .target(currentPosition)
//                            .zoom(map.getMaxZoomLevel())
//                            .tilt(-(grados)).build()));
//        });


        fragmentView = inflater.inflate(R.layout.fragment_maps, container, false);



        return fragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }



    }

    private float[] applyLowPassFilter(float[] input, float[] output) {
        if ( output == null ) return input;

        for ( int i=0; i<input.length; i++ ) {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }
        return output;
    }

    private static final float ALPHA = 0.5f;



    ControlPointsViewModel controlPointsViewModel;
    List<ControlPoint> controlPointsList = new ArrayList<>();
    LatLng currentPosition;
    SensorTest sensorTest;
    View fragmentView;
    GoogleMap map;

    float[] mValuesMagnet      = new float[3];
    float[] mValuesAccel       = new float[3];
    final float[] mValuesOrientation = new float[3];
    final float[] mRotationMatrix    = new float[9];

}