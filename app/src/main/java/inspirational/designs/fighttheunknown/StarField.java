package inspirational.designs.fighttheunknown;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

/**
 * Created by shaheed on 2017/09/19.
 */
class StarField {

    private class Star {
        public int x;
        public int y;

        public Star(int w, int h) {
            x = (int) (Math.random() * w);
            y = (int) (Math.random() * h);
        }

        public void draw(Canvas c, Paint p, int w, int h, int d, int delta) {
            y += d;
            if (y > h) {
                y = -1;
                x = (int) (Math.random() * w);
            }
            x += delta;
            if (x > w) {
                x -= w;
            }
            if (x < 0) {
                x += w;
            }

            c.drawPoint(x, y, p);
        }

    }

    private int width;
    private int height;
    private int depth;
    private int delta;
    //private Trigger twinkle = null;
    private Star stars[];
    //private boolean visible;
    private Paint paint = null;

    public StarField(int w, int h, int d, boolean v) {
        width = w;
        height = h;
        depth = d;
        //twinkle = new Trigger(50 * depth);
        paint = new Paint();
        delta = 0;

        paint.setAntiAlias(true);
        int grayLevel = depth * 64;
        paint.setARGB(255, grayLevel, grayLevel, grayLevel);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(d * 3);

        //visible = v;

        stars = new Star[height / 10];
        Log.d("info", "creating star field :" + stars.length);

        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star(width, height);
        }
    }

    public void draw(Canvas c, int w, int h) {
        // do some stuff to draw the stars.
        //if (twinkle.triggered(4)) {
        //    visible = !visible;
        //    twinkle.reset();
        //}

        //if (!visible)
        //    return;

        for (Star s : stars) {
            s.draw(c, paint, w, h, depth, delta);
        }
    }

    public void updateSpeed(float playerSpeedX, float playerSpeedY) {
        delta = (int)Math.ceil((double)-(playerSpeedX * (0.25 * depth)));
    }
}
