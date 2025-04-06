package martinezruiz.javier.pmdm06.viewmodels;


import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import martinezruiz.javier.pmdm06.persistence.repository.ControlPointRepository;


public class MyViewModelFactory implements ViewModelProvider.Factory {
    private ControlPointRepository controlPointRepository = null;

    public MyViewModelFactory(ControlPointRepository controlPointRepository) {
        this.controlPointRepository = controlPointRepository;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        if(modelClass.isAssignableFrom(ControlPointsViewModel.class)){
            return (T) new ControlPointsViewModel(controlPointRepository);
        }
        else {
            return null;
        }
//        if (modelClass.isAssignableFrom(WordViewModel.class)) {
//            return (T) new WordViewModel(wordRepository);
//        } else  {
//            return (T) new CafeViewModel(cafeRepository);
//        }
    }
}