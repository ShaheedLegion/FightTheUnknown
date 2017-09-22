package inspirational.designs.fighttheunknown;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;

public class StartScreenActivity extends Activity implements LoadingListener {

	private Button startBtn = null;
	private ProgressBar progressBar = null;
	private StartScreenActivity g_activity = null;

	public StartScreenActivity() {
		super();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public int getWidth() {
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		
		return width;
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public int getHeight() {
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int height = size.y;
		
		return height;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_start);

		startBtn = (Button) findViewById(R.id.dummy_button);
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		g_activity = this;

		if (startBtn != null) {
			startBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(StartScreenActivity.this,
							FullscreenActivity.class);
					Bundle params = intent.getExtras();
					if (params == null) {
						Log.d("info", "Intent bundle was null");
						params = new Bundle();
					}

					int w = g_activity.getWidth();
					int h = g_activity.getHeight();
					params.putInt("width", w);
					params.putInt("height", h);
					
					Log.d("info", "Putting w[" + w + "] h[" + h + "]");
					
					if (intent.getExtras() == null) {
						intent.putExtras(params);
					}
					
					startActivity(intent);
				}
			});
			startBtn.setVisibility(View.INVISIBLE);
		}

		GameSingleton.getInstance().setup(this, this, getWidth(), getHeight());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		
		GameSingleton.getInstance().stop();
		Log.d("info", "Activity destoyed");
	}

	private void updateProgress(int current, int total) {
		// Update the progress bar.
		Log.d("info", "loading progress [" + current + "][" + total + "]");
		if (progressBar != null) {
			progressBar.setMax(total);
			progressBar.setProgress(current);
		}
	}

	@Override
	public void LoadingProgress(int current, int total) {
		final int c = current;
		final int t = total;
		Handler refresh = new Handler(Looper.getMainLooper());
		refresh.post(new Runnable() {
			public void run() {
				updateProgress(c, t);
			}
		});
	}

	private void completeProgress(Bitmap[] resources, int soundResources[], int musicResources[], int total) {
		// Update the progress bar.
		Log.d("info", "loading progress [" + total + "][" + total + "]");
		if (progressBar != null) {
			progressBar.setMax(total);
			progressBar.setProgress(total);
		}

		ResourceSingleton singleton = ResourceSingleton.getInstance();
		singleton.SetResources(resources);
		singleton.SetSoundResources(soundResources);
		singleton.SetMusicResources(musicResources);
		GameSingleton.getInstance().resourcesReady(getBaseContext());

		if (startBtn != null) {
			startBtn.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void OnLoadingComplete(Bitmap[] resources, int soundResources[], int musicResources[],
			int total) {
		final Bitmap r[] = resources;
		final int s[] = soundResources;
		final int m[] = musicResources;
		final int t = total;

		Handler refresh = new Handler(Looper.getMainLooper());
		refresh.post(new Runnable() {
			public void run() {
				completeProgress(r, s, m, t);
			}
		});
	}

}
