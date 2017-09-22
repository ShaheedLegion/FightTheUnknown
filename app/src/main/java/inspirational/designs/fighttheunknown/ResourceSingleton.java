package inspirational.designs.fighttheunknown;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.SoundPool;

public class ResourceSingleton {

	private static ResourceSingleton instance = null;
	private Bitmap resources[] = null;
	private int soundResources[] = null;
	private int musicResources[] = null;
	private SoundPool g_pool = null;
	private DBHelper g_database = null;

	private ResourceSingleton() {
		super();
		g_pool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
	}

	public void SetResources(Bitmap res[]) {
		resources = res;
	}

	public void SetSoundResources(int res[]) {
		soundResources = res;
	}

	public void SetMusicResources(int res[]) {
		musicResources = res;
	}
	
	public Bitmap[] GetResources() {
		return resources;
	}

	public int[] GetSoundResources() {
		return soundResources;
	}

	public int[] GetMusicResources() {
		return musicResources;
	}
	
	public SoundPool GetSoundPool() {
		return g_pool;
	}

	public static ResourceSingleton getInstance() {
		if (instance == null)
			instance = new ResourceSingleton();

		return instance;
	}

	public DBHelper GetDBHelper(Context c) {
		if (g_database == null)
			g_database = new DBHelper(c);

		return g_database;
	}
}
