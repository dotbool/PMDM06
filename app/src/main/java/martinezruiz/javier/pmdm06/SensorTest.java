package martinezruiz.javier.pmdm06;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class SensorTest implements SensorEventListener, DefaultLifecycleObserver {


    public SensorTest(Context ctx) {

        this.ctx = ctx;
        sensorManager = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        geoMagnetic = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);


    }

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        sensorManager.registerListener(this, geoMagnetic, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause(@NonNull LifecycleOwner owner) {
        sensorManager.unregisterListener(this, geoMagnetic);
        sensorManager.unregisterListener(this, accelerometer);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {


        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
//                System.arraycopy(event.values, 0, mValuesAccel, 0, 3);
                support.firePropertyChange("mValuesAccel", 0, event.values);

                break;

            case Sensor.TYPE_MAGNETIC_FIELD:
//                System.arraycopy(event.values, 0, mValuesMagnet, 0, 3);
                support.firePropertyChange("mValuesMagnet", 0, event.values);

                break;
        }


//        SensorManager.getRotationMatrix(mRotationMatrix, null, mValuesAccel, mValuesMagnet);
//        SensorManager.getOrientation(mRotationMatrix, mValuesOrientation);
//        support.firePropertyChange("mValuesAccel", 0, event.values);
//        support.firePropertyChange("mValuesMagnet", 0, event.values);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    public final SensorManager sensorManager;
    public final Sensor accelerometer;
    private final Sensor geoMagnetic;

    private Context ctx;

        final float[] mValuesMagnet      = new float[3];
        final float[] mValuesAccel       = new float[3];
        final float[] mValuesOrientation = new float[3];
        final float[] mRotationMatrix    = new float[9];




    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }




}
