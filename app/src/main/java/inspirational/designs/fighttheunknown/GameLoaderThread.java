package inspirational.designs.fighttheunknown;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class GameLoaderThread extends Thread {

	private boolean myThreadRun = false;
	private Activity g_activity = null;
	private int g_resourceIds[] = null;
	private int g_soundIds[] = null;
	private int g_musicIds[] = null;
	private Bitmap g_resources[] = null;
	private int g_soundResources[] = null;
	static final int g_res_num = 7;
	static final int g_sound_res = 5;
	static final int g_music_res = 6;
	private LoadingListener g_listener = null;

	public GameLoaderThread(Activity activity, LoadingListener listener) {
		super();
		g_listener = listener;
		g_activity = activity;
		g_resourceIds = new int[g_res_num];
		g_resources = new Bitmap[g_res_num];
		g_soundIds = new int[g_sound_res];
		g_soundResources = new int[g_sound_res];
		g_musicIds = new int[g_music_res];

		g_resourceIds[0] = R.drawable.ships_2;
		g_resourceIds[1] = R.drawable.ships_asteroids_0;
		g_resourceIds[2] = R.drawable.ships_biomech_0;
		g_resourceIds[3] = R.drawable.ships_human_0;
		g_resourceIds[4] = R.drawable.ships_saucer_0;
		g_resourceIds[5] = R.drawable.ships_void_0;
		g_resourceIds[6] = R.drawable.projectile_1;

		g_musicIds[0] = R.raw.stage_intro;
		g_musicIds[1] = R.raw.stage_theme;
		g_musicIds[2] = R.raw.stage_clear;
		g_musicIds[3] = R.raw.power_up;
		g_musicIds[4] = R.raw.boss_battle;
		g_musicIds[5] = R.raw.mid_boss;
		
		g_soundIds[0] = R.raw.explosion2;
		g_soundIds[1] = R.raw.explosion9;
		g_soundIds[2] = R.raw.laser_shoot;
		g_soundIds[3] = R.raw.power_up;
		g_soundIds[4] = R.raw.power_up4;
	}

	public void setRunning(boolean b) {
		Log.d("info", "Thread state set to running");
		myThreadRun = b;
	}

	@Override
	public void run() {
		ResourceSingleton singleton = ResourceSingleton.getInstance();
		int currentImage = 0;
		int currentSound = 0;
		if (g_activity == null) {
			Log.d("info", "Could not load resources, activity is null.");
			return;
		}

		boolean doneImages = false;

		while (myThreadRun) {
			try {
				if (!doneImages) {
					g_resources[currentImage] = BitmapFactory.decodeResource(
							g_activity.getResources(),
							g_resourceIds[currentImage]);

					if (g_listener != null) {
						g_listener.LoadingProgress(currentImage + currentSound,
								g_res_num + g_sound_res);
					}

					currentImage++;

					if (currentImage == (g_res_num)) {
						doneImages = true;
					}
				}

				if (doneImages) {
					g_soundResources[currentSound] = singleton.GetSoundPool()
							.load(g_activity.getApplicationContext(),
									g_soundIds[currentSound], 1);

					if (g_listener != null) {
						g_listener.LoadingProgress(currentImage + currentSound,
								g_res_num + g_sound_res);
					}

					currentSound++;

					if (currentSound == g_sound_res) {
						if (g_listener != null) {
							g_listener.OnLoadingComplete(g_resources,
									g_soundResources, g_musicIds, g_res_num + g_sound_res);
						}
						myThreadRun = false;
					}
				}

				sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
			}
		}
		interrupt();
		Log.d("info", "Loader thread stopped");
	}
}
