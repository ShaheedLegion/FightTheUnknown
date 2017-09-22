package inspirational.designs.fighttheunknown;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;

public class GameObject {
	private Bitmap resources[] = null;
	private int soundResources[] = null;

	private Player g_player = null;
	private StarField star_fields[] = null;
	private CountDownTimer g_timer = null;
	private static GameObject instance = null;
	private Context g_context = null;
    private JoyStick g_joyStick = null;

	private GameObject(Context context) {
		ResourceSingleton singleton = ResourceSingleton.getInstance();
		resources = singleton.GetResources();
		soundResources = singleton.GetSoundResources();
		g_context = context;
        g_joyStick = new JoyStick();
	}

	private CountDownTimer createTimer(int duration, int interval) {
		return new CountDownTimer(duration, interval) {

			@Override
			public void onTick(long millisUntilFinished) {
				// Don't really care about intermittent notifications
				Log.d("info", "Time left for sound track "
						+ millisUntilFinished);
			}

			@Override
			public void onFinish() {
				// SoundPool pool =
				// ResourceSingleton.getInstance().GetSoundPool();
				// if (pool != null) {
				// pool.play(soundResources[1], 1.0f, 1.0f, 4, 0, 1.0f);
				// //g_timer = createTimer(9000, 1000);
				// }
			}

		};
	}

	public void init(int w, int h) {
		if (star_fields == null) {
			int nf = 4;
			star_fields = new StarField[nf]; // arbitrary assignment

			star_fields[0] = new StarField(w, h, 1, true);
			star_fields[1] = new StarField(w, h, 1, false);
			star_fields[2] = new StarField(w, h, 2, true);
			star_fields[3] = new StarField(w, h, 3, false);
		}

		if (g_player == null) {
			g_player = new Player(10, w, h, resources[0].getWidth(),
					resources[0].getHeight());

			Log.d("info", "Constructed player, idx : " + g_player.playerIdx);
			Log.d("info", "getting resource desc w["
					+ resources[g_player.playerIdx].getWidth() + "] h["
					+ resources[g_player.playerIdx].getHeight() + "]");
			Log.d("info", "player x[" + g_player.getSpriteX() + "] y["
					+ g_player.getSpriteY() + "] w[" + g_player.getSpriteEndW()
					+ "] h[" + g_player.getSpriteEndH() + "]");
		}

		if (g_joyStick == null) { g_joyStick = new JoyStick(); }
		if (g_joyStick != null) {
            g_joyStick.setBounds(0, (h / 2)+(h / 4), w / 4, h / 4);
        }

		ResourceSingleton res = ResourceSingleton.getInstance();
		if (res != null) {
			int musics[] = res.GetMusicResources();
			MediaPlayer player = MediaPlayer.create(g_context, musics[0]);
			player.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					ResourceSingleton res = ResourceSingleton.getInstance();
					if (res != null) {
						int musics[] = res.GetMusicResources();
						MediaPlayer player2 = MediaPlayer.create(g_context,
								musics[0]);
						player2.start();
					}
				}

			});
			player.start(); // immediately start playing the music.
			g_timer = createTimer(12000, 1000).start();
		}
	}

	public void draw(Canvas c, int w, int h) {
		c.drawRGB(0, 0, 0);

        g_player.UpdateSpeed(g_joyStick.getJoyX(), g_joyStick.getJoyY());
        float playerSpeedX = g_player.getSpeedX();
        float playerSpeedY = g_player.getSpeedY();

		for (StarField s : star_fields) {
			s.draw(c, w, h);
            s.updateSpeed(playerSpeedX, playerSpeedY);
		}

		// c.drawBitmap(resources[g_player.playerIdx], g_player.x, g_player.y,
		// null);
		g_player.DrawProjectiles(c, resources[resources.length - 1], w, h);
		g_player.DrawPlayer(c, resources[g_player.playerIdx], w, h);
        g_joyStick.Draw(c);
	}

	public void HandleTouch(MotionEvent e) {
		int action = e.getAction();
		switch(action) {
			case (MotionEvent.ACTION_DOWN) :
				Log.d("msg","Action was DOWN");
                g_joyStick.digitDown();
				break;
			case (MotionEvent.ACTION_MOVE) :
				Log.d("msg","Action was MOVE");
                g_joyStick.digitMove(e.getX(), e.getY());
				break;
			case (MotionEvent.ACTION_UP) :
				Log.d("msg","Action was UP");
                g_joyStick.digitUp();
				break;
			default:
                g_joyStick.digitUp();
				break;
		}
	}

	public static GameObject getInstance(Context c) {
		if (instance == null)
			instance = new GameObject(c);
		return instance;
	}

	public void save(DBHelper helper) {
		/*
		 * helper.insertObject(22, cx, cy, offx, offy); Log.d("info",
		 * "Saving object " + cx + " " + cy);
		 */
	}

	public void restore(ArrayList<DBHelper.DBHelperObject> objects) {
		/*
		 * for (DBHelper.DBHelperObject obj : objects) { if (obj.type == 22) {
		 * cx = obj.x; cy = obj.y; offx = obj.dx; offy = obj.dy; return; } }
		 */
	}

}
