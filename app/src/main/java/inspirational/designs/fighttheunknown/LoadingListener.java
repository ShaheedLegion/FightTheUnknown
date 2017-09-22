package inspirational.designs.fighttheunknown;

import android.graphics.Bitmap;

public interface LoadingListener {

	public abstract void LoadingProgress(int current, int total);

	public abstract void OnLoadingComplete(Bitmap resources[],
			int soundResources[], int musicResources[], int total);
}
