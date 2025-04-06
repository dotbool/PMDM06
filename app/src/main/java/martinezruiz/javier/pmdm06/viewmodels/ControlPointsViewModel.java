package martinezruiz.javier.pmdm06.viewmodels;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import martinezruiz.javier.pmdm06.Pmdm06Application;
import martinezruiz.javier.pmdm06.models.ControlPoint;
import martinezruiz.javier.pmdm06.models.ErrorData;
import martinezruiz.javier.pmdm06.persistence.repository.ControlPointRepository;

public class ControlPointsViewModel extends ViewModel {

    private enum Messages {
        ERROR_MESSAGE("Error al procesar el ControlPoint"),
        SUCCES_MESSAGE("Congratulations!");

        private Messages (String message){
            this.message = message;
        }
        private String message;

        public String getMessage() {
            return message;
        }
    }

    public ControlPointsViewModel(ControlPointRepository controlPointRepository) {
        this.controlPointRepository = controlPointRepository;

        inclinacion = new MutableLiveData<>();
        currentPosition = new MutableLiveData<>();
        controlPointsList = new MutableLiveData<>();
        errorData = new MutableLiveData<>();
        controlPointArrayList = controlPointRepository.getControlPointsList();
        controlPointsList.postValue(controlPointArrayList);
    }

    public static final ViewModelInitializer<ControlPointsViewModel> initializer = new ViewModelInitializer<>(
            ControlPointsViewModel.class,
            creationExtras -> {
                Pmdm06Application app = (Pmdm06Application) creationExtras.get(APPLICATION_KEY);
                assert app != null;
                return new ControlPointsViewModel(app.appContainer.controlPointRepository);
            }
    );

    public LiveData<List<ControlPoint>> getControlPointsList() {
        return controlPointsList;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d("GameFragment", "GameViewModel destroyed!");
    }

    public LiveData<LatLng> getCurrentPosition(){
        Log.d("OBTENIENDO CURRENT POSITION", "EN EL LIVE DATA");
        return currentPosition;
    }

    public void setCurrentPosition(LatLng position){
        currentPosition.setValue(position);
    }

    public LiveData<Float> getInclinacion(){
        return inclinacion;
    }

    public void setInclinacion(float incl){
        inclinacion.setValue(incl);
    }


    private MutableLiveData<Float> inclinacion;
    private MutableLiveData<LatLng> currentPosition;
    private ArrayList<ControlPoint> controlPointArrayList;
    private final MutableLiveData<List<ControlPoint>> controlPointsList;
    private MutableLiveData<ErrorData> errorData;
//    CompositeDisposable disposables = new CompositeDisposable();
    ControlPointRepository controlPointRepository;
}
