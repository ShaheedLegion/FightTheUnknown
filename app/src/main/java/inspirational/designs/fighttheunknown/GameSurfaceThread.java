package inspirational.designs.fighttheunknown;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class GameSurfaceThread extends Thread {
	private SurfaceHolder myThreadSurfaceHolder;
	private GameSurface myThreadSurfaceView;
	private boolean myThreadRun = false;
	private static GameSurfaceThread instance = null;

	private GameSurfaceThread(SurfaceHolder surfaceHolder,
			GameSurface surfaceView) {
		myThreadSurfaceHolder = surfaceHolder;
		myThreadSurfaceView = surfaceView;
	}

	public void setRunning(boolean b) {
		myThreadRun = b;
		Log.d("info", "Setting thread state to " + b);
	}

	public boolean wasStarted() {
		Log.d("info", "Querying thread state " + myThreadRun);
		return myThreadRun;
	}

	@SuppressLint("WrongCall")
	@Override
	public void run() {

		while (myThreadRun) {
			Canvas c = null;

			try {
				c = myThreadSurfaceHolder.lockCanvas(null);
				synchronized (myThreadSurfaceHolder) {
					myThreadSurfaceView.onDraw(c);
				}
				sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				// do this in a finally so that if an exception is thrown
				// during the above, we don't leave the Surface in an
				// inconsistent state
				if (c != null) {
					myThreadSurfaceHolder.unlockCanvasAndPost(c);
				}
			}
		}
		try {
			interrupt();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.d("info", "Thread has ended");
		instance = null;
	}

	public static GameSurfaceThread GetInstance(SurfaceHolder surfaceHolder,
			GameSurface surfaceView) {

		if (instance == null) {
			Log.d("info", "Creating new thread instance.");
			instance = new GameSurfaceThread(surfaceHolder, surfaceView);
		}

		return instance;
	}
}