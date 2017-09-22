package inspirational.designs.fighttheunknown;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

/**
 * Created by shaheed on 2017/09/19.
 */
class Player {
    private class Projectile {
        private int px = 0;
        private int py = 0;
        private int w = 0;
        private int h = 0;
        private int screenw = 0;
        private int screenh = 0;
        private int speed = 0;
        private int lifetime = 0;

        public Projectile(int yspeed) {
            speed = yspeed;
        }

        public void Emit(int x, int y, int sw, int sh, int scw, int sch, int life) {
            px = x - (sw / 2);
            py = y;
            w = sw;
            h = sh;
            screenw = scw;
            screenh = sch;
            lifetime = life;
        }

        public int getX() {
            --lifetime;
            if (lifetime <= 0) {
                lifetime = 0;
            }

            return px;
        }

        public int getY() {
            py += speed;
            if (py > (screenh)) {
                lifetime = 0;
                py = -h;
            }
            return py;
        }

        public int getLifetime() {
            return lifetime;
        }
    }

    ;

    private class Emitter {
        private Projectile bullets[] = null;
        private int nextEmit = 0;
        private int currentEmit = 0;
        private int projectileW = 0;
        private int projectileH = 0;

        public Emitter(int num, int freq, int sw, int sh) {
            nextEmit = freq;
            projectileW = sw;
            projectileH = sh;
            bullets = new Projectile[num];
            for (int i = 0; i < num; i++) {
                bullets[i] = new Projectile(-10);
            }
        }

        public void Emit(int x, int y, int screenw, int screenh, int lifetime) {
            currentEmit++;
            if (currentEmit < nextEmit) {
                return;
            }

            currentEmit = 0;
            for (Projectile p : bullets) {
                if (p.getLifetime() == 0) {
                    p.Emit(x, y, projectileW, projectileH, screenw, screenh, lifetime);
                    //Log.d("info", "emitting sprite");
                    return;    //emit one sprite only
                }
            }
        }

        public void Draw(Canvas c, Bitmap bitmap, int w, int h) {
            for (Projectile p : bullets) {
                if (p.getLifetime() > 0) {
                    float left = (float) p.getX();
                    float top = (float) p.getY();
                    c.drawBitmap(bitmap, left, top, null);
                }
            }
        }
    }

    int playerIdx;
    int playerFrames;
    int playerCurrentFrame;
    int x;
    int y;
    int sw;
    int sh;
    int width;
    int height;
    float cx;
    float cy;
    private Emitter projectiles = null;

    // This makes the speed a little higher so the movement is fluid.
    private final float g_speed = 2.25f;

    public Player(int frames, int w, int h, int iw, int ih) {
        playerFrames = frames;
        playerIdx = 0;
        playerCurrentFrame = 0;
        width = w;
        height = h;
        Log.d("info", "Setting player ship idx " + playerIdx);
        sw = iw;
        sh = ih;
        x = (w - (iw / playerFrames)) / 2;
        y = (h - ih) / 2;
        cx = 0;
        cy = 0;

        projectiles = new Emitter(100, 10, 16, 16);
    }

    public int getSpriteX() {
        x += (int) cx;
        if (x < 0)
            x = 0;
        if (x + (sw / playerFrames) > width)
            x = (width - (sw / playerFrames));

        projectiles.Emit(x + ((sw / playerFrames) / 2), y, width, height, 500);

        return x;
    }

    public int getSpriteY() {
        y += (int) cy;
        if (y < 0)
            y = 0;
        if ((y + sh) > height)
            y = (height - sh);

        return y;
    }

    public int getSpriteEndW() {
        return (x + (sw / playerFrames));
    }

    public int getSpriteEndH() {
        return (y + sh);
    }

    public int getFrameX() {
        return (sw / playerFrames) * playerCurrentFrame;
    }

    public int getFrameY() {
        return 0;
    }

    public int getFrameW() {
        return sw / playerFrames;
    }

    public int getFrameH() {
        return sh;
    }

    public void UpdateSpeed(float a, float b) {
        // Deal with direction changes.
        cx = a * g_speed;
        cy = b * g_speed;
    }

    public float getSpeedX() {
        return cx;
    }
    public float getSpeedY() {
        return cy;
    }


    public void DrawProjectiles(Canvas c, Bitmap b, int w, int h) {
        projectiles.Draw(c, b, w, h);
    }

    public void DrawPlayer(Canvas c, Bitmap b, int w, int h) {
        Rect src = new Rect(getFrameX(), getFrameY(), getFrameW(),
                getFrameH());
        Rect dst = new Rect(getSpriteX(), getSpriteY(), getSpriteEndW(),
                getSpriteEndH());
        c.drawBitmap(b, src, dst, null);
    }
}
