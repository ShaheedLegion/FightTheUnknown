package inspirational.designs.fighttheunknown;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {

	GameObject g_game = null;

	public GameSurface(Context context) {
		super(context);
		init();
	}

	public GameSurface(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public GameSurface(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		getHolder().addCallback(this);
		GameSurfaceThread.GetInstance(getHolder(), this);

		setFocusable(true); // make sure we get key events

		g_game = GameSingleton.getInstance().getGameObject(getContext());
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// Not much we can do in this instance ... probably recreate the
		// surface.
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		GameSurfaceThread thread = GameSurfaceThread.GetInstance(getHolder(),
				this);
		if (!thread.wasStarted()) {
			Log.d("info", "starting the game thread.");
			thread.setRunning(true);
			thread.start();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		GameSurfaceThread thread = GameSurfaceThread.GetInstance(getHolder(),
				this);
		thread.setRunning(false);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (canvas == null)
			return;

		g_game.draw(canvas, getWidth(), getHeight());
	}
	
	public void stop() {
		GameSurfaceThread thread = GameSurfaceThread.GetInstance(getHolder(),
				this);
		if (thread != null) {
			try {
				thread.setRunning(false);
			} catch (Exception e) {
			}
		}
		Log.d("info", "Stopped thread");
	}

	public void Save() {
		ResourceSingleton res = ResourceSingleton.getInstance();
		if (res != null) {
			DBHelper helper = res.GetDBHelper(getContext());
			if (helper != null) {
				g_game.save(helper);
			}
		}
	}

	public void Restore() {
		ResourceSingleton res = ResourceSingleton.getInstance();
		if (res != null) {
			DBHelper helper = res.GetDBHelper(getContext());
			if (helper != null) {
				ArrayList<DBHelper.DBHelperObject> objects = helper
						.getAllObjects();

				if (objects.isEmpty())
					return;

				g_game.restore(objects);
			}
		}
	}

    public void HandleTouch(MotionEvent event) {
		if (g_game != null) {
			g_game.HandleTouch(event);
		}
    }
}