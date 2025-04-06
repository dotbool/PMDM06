package martinezruiz.javier.pmdm06;

import android.app.Application;

public class Pmdm06Application  extends Application {

    @Override
    public void onCreate() {
        appContainer = new AppContainer(this);
        super.onCreate();
    }

    public AppContainer appContainer;
}
