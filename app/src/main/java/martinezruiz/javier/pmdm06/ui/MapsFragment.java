package martinezruiz.javier.pmdm06.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;

import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.materialswitch.MaterialSwitch;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import martinezruiz.javier.pmdm06.R;
import martinezruiz.javier.pmdm06.SensorTest;
import martinezruiz.javier.pmdm06.databinding.FragmentMapsBinding;
import martinezruiz.javier.pmdm06.models.ControlPointProgress;
import martinezruiz.javier.pmdm06.viewmodels.ControlPointsViewModel;

public class MapsFragment extends Fragment implements GoogleMap.OnInfoWindowClickListener {

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


            try {
                // Customise the styling of the base map using a JSON object defined
                // in a raw resource file.
                boolean success = googleMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                requireContext(), R.raw.style_json));

                if (!success) {
                    Log.e("CARGA ESTILO", "Style parsing failed.");
                }
            } catch (Resources.NotFoundException e) {
                Log.e("CARGA MAPA", "Can't find style. Error: ", e);
            }


            map.setInfoWindowAdapter(new MyInfoWindowAdapter(requireContext()));
            map.setOnInfoWindowClickListener(MapsFragment.this);
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(requireContext(),
                    android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            map.setMyLocationEnabled(true);
            }

            if(controlPointsProgressList != null &&  !controlPointsProgressList.isEmpty()){


                controlPointsProgressList.forEach(cpp -> {
                    LatLng latLng = new LatLng(cpp.getGimActivity().getLatitude(), cpp.getGimActivity().getLongitude());

                    Marker marker = googleMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(cpp.getGimActivity().getName())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.iconsbandicoot))
                            .snippet(cpp.getGimActivity().getActivity()+".\n"+ cpp.getGimActivity().getGoal()));
                    assert marker != null;
                    marker.setTag(cpp);

                    markerArrayList.add(marker);
                });


            }

            sensorTest.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {

                    if(evt.getPropertyName().equals("mValuesAccel")){
                        mValuesAccel = applyLowPassFilter((float[])evt.getNewValue(), mValuesAccel);
                    }
                    else if(evt.getPropertyName().equals("mValuesMagnet")){
                        mValuesMagnet = applyLowPassFilter((float[]) evt.getNewValue(), mValuesMagnet);

                    }
                    SensorManager.getRotationMatrix(mRotationMatrix, null, mValuesAccel, mValuesMagnet);
                    SensorManager.getOrientation(mRotationMatrix, mValuesOrientation);

                    float incl = mValuesOrientation[1];
                    float grados = Math.abs(incl * 57.295f);
                    float bearing = mValuesOrientation[0];
                    float bearingInDegrees = Math.round((float) (Math.toDegrees(bearing)+360) % 360);

                    float zoom = map.getCameraPosition().zoom;
//
//                    switchLocation.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                            if(switchLocation.isChecked()){
//                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
//                                            currentPosition.latitude, currentPosition.longitude),
//                                    googleMap.getMaxZoomLevel()));
//
//
//                            }
//                        }
//                    });

                    if(switchLocation.isChecked()) {
                        if (System.currentTimeMillis() - lastUpdate > updateCoolDown) {

                            map.animateCamera(CameraUpdateFactory.newCameraPosition(
                                    CameraPosition.builder()
                                            .target(currentPosition)
                                            .bearing(bearingInDegrees)
                                            .tilt(grados)
                                            .zoom(googleMap.getMaxZoomLevel())
                                            .build()));

                            lastUpdate = new Date().getTime();
                        }
                    }


//
//                    controlPointsList.forEach(cp ->{
//                        if ((Math.abs(currentPosition.latitude) - Math.abs(cp.getLatitude())) <= 0.001
//                                && (Math.abs(currentPosition.longitude) - Math.abs(cp.getLongitude())) <= 0.001) {
//                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
//                                            currentPosition.latitude, currentPosition.longitude),
//                                    googleMap.getMaxZoomLevel()));
//                        }
//
//                    });
//
//                                if (System.currentTimeMillis() - lastUpdate > updateCoolDown) {
//
//                                    map.moveCamera(CameraUpdateFactory.newCameraPosition(
//                                            CameraPosition.builder()
//                                                    .target(currentPosition)
//                                                    .bearing(bearingInDegrees)
//                                                    .tilt(grados)
//                                                    .zoom(zoom)
//                                                    .build()));
//                                    lastUpdate = new Date().getTime();
//                                }

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
//        binding = MyInfoWindowBinding.inflate(getLayoutInflater());
//        binding = DataBindingUtil.setContentView(requireActivity(), R.layout.my_info_window);
        sensorTest = new SensorTest(requireActivity());
        getLifecycle().addObserver(sensorTest);



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        controlPointsViewModel.getControlPointsProgressList().observe(getViewLifecycleOwner(), controlPoints -> {
//            controlPointsProgressList = controlPoints;
            updateList((ArrayList<ControlPointProgress>) controlPoints);


            
        });

        controlPointsViewModel.getCurrentPosition().observe(getViewLifecycleOwner(), currentPosition -> {
            this.currentPosition = currentPosition;
        });

        FragmentMapsBinding binding = FragmentMapsBinding.inflate(inflater, container, false);
        switchLocation = binding.switchLocation;


        return binding.getRoot();
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

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {

        ControlPointProgress cpp = (ControlPointProgress) marker.getTag();
        cpp.setProgress(16);
        controlPointsViewModel.setControlPointsProgressList(controlPointsProgressList);
        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));



        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
//        builder.setMessage(marker.getSnippet()+". Progress "+ (Integer) marker.getTag()+ "%");

        builder.create();
        builder.show();


    }


    private float[] applyLowPassFilter(float[] input, float[] output) {
        if ( output == null ) return input;

        for ( int i=0; i < input.length; i++ ) {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }
        return output;
    }

    private void updateList(ArrayList<ControlPointProgress> newList){

        if(controlPointsProgressList.isEmpty()){
            controlPointsProgressList = newList;
        }
        else {
            controlPointsProgressList.stream().peek(cpp -> {

                for (ControlPointProgress newCpp : newList) {
                    cpp.setProgress(newCpp.getProgress());
                }

            });
        }

        controlPointsProgressList.forEach(cpp -> Log.d(cpp.getProgress()+"PROGRESS", cpp.getProgress()+"PROGRESS" ));
    }


    private static final float ALPHA = 0.5f;


    boolean inZone = false;
    ControlPointsViewModel controlPointsViewModel;
    ArrayList<ControlPointProgress> controlPointsProgressList = new ArrayList<>();
    LatLng currentPosition;
    SensorTest sensorTest;
    View fragmentView;
    GoogleMap map;

    float[] mValuesMagnet      = new float[3];
    float[] mValuesAccel       = new float[3];
    final float[] mValuesOrientation = new float[3];
    final float[] mRotationMatrix    = new float[9];
    long updateCoolDown = 50;
    long lastUpdate;

    ArrayList<Marker> markerArrayList = new ArrayList<>();
    MaterialSwitch switchLocation;


}