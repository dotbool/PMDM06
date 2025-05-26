package martinezruiz.javier.pmdm06;

import android.content.Context;

import martinezruiz.javier.pmdm06.persistence.GimActivitiesLocalDataSource;
import martinezruiz.javier.pmdm06.persistence.repository.GimActivitiesRepository;

public class AppContainer {

    public AppContainer(Context ctx) {

        GimActivitiesLocalDataSource gimActivitiesLocalDataSource =  new GimActivitiesLocalDataSource(ctx);
        gimActivitiesRepository = new GimActivitiesRepository(gimActivitiesLocalDataSource);
    }

    public GimActivitiesRepository gimActivitiesRepository;
}
