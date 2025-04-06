package martinezruiz.javier.pmdm06;

import android.content.Context;

import martinezruiz.javier.pmdm06.persistence.ControlPointsLocalDataSource;
import martinezruiz.javier.pmdm06.persistence.repository.ControlPointRepository;

public class AppContainer {

    public AppContainer(Context ctx) {

        ControlPointsLocalDataSource controlPointsLocalDataSource =  new ControlPointsLocalDataSource(ctx);
        controlPointRepository = new ControlPointRepository(controlPointsLocalDataSource);
    }

    public ControlPointRepository controlPointRepository;
}
