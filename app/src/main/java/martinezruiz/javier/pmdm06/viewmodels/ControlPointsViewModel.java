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
import martinezruiz.javier.pmdm06.models.ControlPointProgress;
import martinezruiz.javier.pmdm06.models.GimActivity;
import martinezruiz.javier.pmdm06.models.ErrorData;
import martinezruiz.javier.pmdm06.persistence.repository.GimActivitiesRepository;

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

    public ControlPointsViewModel(GimActivitiesRepository gimActivitiesRepository) {
        this.gimActivitiesRepository = gimActivitiesRepository;
        controlPointProgressArrayList = new ArrayList<>();

        currentPosition = new MutableLiveData<>();
        controlPointsProgressList = new MutableLiveData<>();
        errorData = new MutableLiveData<>();
        gimActivityArrayList = gimActivitiesRepository.getControlPointsList();
        gimActivityArrayList.forEach(ga -> { controlPointProgressArrayList.add(new ControlPointProgress(ga));});
        controlPointsProgressList.postValue(controlPointProgressArrayList);
    }

    public static final ViewModelInitializer<ControlPointsViewModel> initializer = new ViewModelInitializer<>(
            ControlPointsViewModel.class,
            creationExtras -> {
                Pmdm06Application app = (Pmdm06Application) creationExtras.get(APPLICATION_KEY);
                assert app != null;
                return new ControlPointsViewModel(app.appContainer.gimActivitiesRepository);
            }
    );

    public LiveData<List<ControlPointProgress>> getControlPointsProgressList() {
        return controlPointsProgressList;
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


    public void setControlPointsProgressList(ArrayList<ControlPointProgress> list){
        controlPointsProgressList.postValue(list);
    }

    private MutableLiveData<LatLng> currentPosition;
    private ArrayList<GimActivity> gimActivityArrayList;
    private ArrayList<ControlPointProgress> controlPointProgressArrayList;
    private final MutableLiveData<List<ControlPointProgress>> controlPointsProgressList;
    private MutableLiveData<ErrorData> errorData;
//    CompositeDisposable disposables = new CompositeDisposable();
    GimActivitiesRepository gimActivitiesRepository;
}
