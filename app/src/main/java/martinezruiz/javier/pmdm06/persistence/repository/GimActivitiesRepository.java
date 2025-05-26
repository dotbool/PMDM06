package martinezruiz.javier.pmdm06.persistence.repository;

import java.util.ArrayList;

import martinezruiz.javier.pmdm06.models.GimActivity;
import martinezruiz.javier.pmdm06.persistence.GimActivitiesLocalDataSource;

public class GimActivitiesRepository {

    public GimActivitiesRepository(GimActivitiesLocalDataSource gimActivitiesLocalDataSource) {
        this.gimActivitiesLocalDataSource = gimActivitiesLocalDataSource;
    }

    public ArrayList<GimActivity> getControlPointsList(){
        return gimActivitiesLocalDataSource.getGimActivities();
    }

    GimActivitiesLocalDataSource gimActivitiesLocalDataSource;
}
