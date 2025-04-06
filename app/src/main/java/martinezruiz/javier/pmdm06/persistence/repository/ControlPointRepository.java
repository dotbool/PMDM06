package martinezruiz.javier.pmdm06.persistence.repository;

import java.util.ArrayList;

import martinezruiz.javier.pmdm06.models.ControlPoint;
import martinezruiz.javier.pmdm06.persistence.ControlPointsLocalDataSource;

public class ControlPointRepository {

    public ControlPointRepository(ControlPointsLocalDataSource controlPointsLocalDataSource) {
        this.controlPointsLocalDataSource = controlPointsLocalDataSource;
    }

    public ArrayList<ControlPoint> getControlPointsList(){
        return controlPointsLocalDataSource.getControlPoints();
    }

    ControlPointsLocalDataSource controlPointsLocalDataSource;
}
