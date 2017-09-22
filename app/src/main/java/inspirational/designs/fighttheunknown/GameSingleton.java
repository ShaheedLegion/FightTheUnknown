package inspirational.designs.fighttheunknown;

import android.app.Activity;
import android.content.Context;

/**
 * Created by shaheed on 2017/09/21.
 */

class GameSingleton {
    private static final GameSingleton ourInstance = new GameSingleton();
    private GameLoaderThread loader = null;
    int width;
    int height;

    static GameSingleton getInstance() {
        return ourInstance;
    }

    private GameSingleton() {
    }

    public void setup(Activity activity, LoadingListener listener, int w, int h) {
        width = w;
        height = h;
        loader = new GameLoaderThread(activity, listener);
        loader.setRunning(true);
        loader.start();
    }

    public void stop() {
        if (loader.isAlive()) {
            try{
                loader.setRunning(false);
            } catch(Exception e) {}
        }
    }

    public void resourcesReady(Context context) {
        getGameObject(context).init(width, height);
    }

    public GameObject getGameObject(Context context) {
        return GameObject.getInstance(context);
    }
}
