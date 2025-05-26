package martinezruiz.javier.pmdm06.ui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import martinezruiz.javier.pmdm06.R;
import martinezruiz.javier.pmdm06.databinding.MyInfoWindowBinding;

public class MyInfoWindowAdapter extends View implements  GoogleMap.InfoWindowAdapter{

    public MyInfoWindowAdapter(Context context) {
        super(context);

        binding = MyInfoWindowBinding.inflate(LayoutInflater.from(context));

    }

    @Nullable
    @Override
    public View getInfoContents(@NonNull Marker marker) {
        return null;
    }

    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        binding.tittle.setText(marker.getTitle());
        return binding.getRoot();
    }

    MyInfoWindowBinding binding;
}
