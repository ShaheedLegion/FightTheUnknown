package inspirational.designs.fighttheunknown;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

@SuppressLint("InlinedApi")
public class FullscreenActivity extends Activity {

	private FullscreenActivity g_activity = null;
	private GameSurface g_view = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		g_activity = this;
		setContentView(R.layout.activity_fullscreen);
		g_view = (GameSurface) findViewById(R.id.fullscreen_content);

		if (g_view != null) {
			int w = getIntent().getExtras().getInt("width");
			int h = getIntent().getExtras().getInt("height");
			Log.d("info", "initializing content view.");
			Log.d("info", "Setting dimensions on Gamesurface w[" + w + "] h[" + h + "]");
			g_view.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					g_view.HandleTouch(event);
					return true;
				}
			});
			//g_view.initialize(w, h);
		}
	}

	@SuppressLint("InlinedApi")
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			getWindow().getDecorView().setSystemUiVisibility(
					View.SYSTEM_UI_FLAG_LAYOUT_STABLE
							| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
							| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
							| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
							| View.SYSTEM_UI_FLAG_FULLSCREEN
							| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d("info", "destroying fullscreen activity");

		if (g_view != null) {
			g_view.stop();
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		Log.d("info", "Saving state");

		if (g_view != null) {
			g_view.Save();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.d("info", "Restoring state");

		if (g_view != null) {
			g_view.Restore();
		}
	}

}
