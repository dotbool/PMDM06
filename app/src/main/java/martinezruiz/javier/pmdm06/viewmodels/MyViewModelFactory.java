package martinezruiz.javier.pmdm06.viewmodels;


import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import martinezruiz.javier.pmdm06.persistence.repository.GimActivitiesRepository;


public class MyViewModelFactory implements ViewModelProvider.Factory {
    private GimActivitiesRepository gimActivitiesRepository = null;

    public MyViewModelFactory(GimActivitiesRepository gimActivitiesRepository) {
        this.gimActivitiesRepository = gimActivitiesRepository;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        if(modelClass.isAssignableFrom(ControlPointsViewModel.class)){
            return (T) new ControlPointsViewModel(gimActivitiesRepository);
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