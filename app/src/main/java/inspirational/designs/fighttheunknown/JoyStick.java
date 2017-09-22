package inspirational.designs.fighttheunknown;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.Log;

/**
 * Created by shaheed on 2017/09/19.
 */
class JoyStick {
    private int x;
    private int y;
    private int w;
    private int h;
    private boolean isDragging;
    private float digitX;
    private float digitY;
    private int innerPadding;
    private int handleRadius;
    private int handleInnerBoundaries;
    private int sensitivity;
    private Paint circlePaint;
    private Paint handlePaint;
    private float joyStickX;
    private float joyStickY;
    private double intervalsX;
    private double intervalsY;

    public JoyStick() {
        x = -1;
        y = -1;
        w = -1;
        h = -1;
        isDragging = false;
        digitX = 0;
        digitY = 0;
        innerPadding = 0;
        handleRadius = 0;
        handleInnerBoundaries = 0;
        sensitivity = 0;
        joyStickX = 0;
        joyStickY = 0;
        intervalsX = 0;
        intervalsY = 0;
    }

    public void setBounds(int i, int i1, int i2, int i3) {
        // Here we make sure that we have a perfect circle
        int measuredWidth = i2;
        int measuredHeight = i3;
        int d = Math.min(measuredWidth, measuredHeight);

        handleRadius = (int) (d * 0.25);
        handleInnerBoundaries = handleRadius;

        x = i;
        y = i1;
        w = d;
        h = d;

        digitX = (float)x + (float) w / 2;
        digitY = (float)y + (float) h / 2;
        joyStickX = digitX;
        joyStickY = digitY;

        sensitivity = 10;

        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(Color.GRAY);
        circlePaint.setStrokeWidth(3);
        circlePaint.setStyle(Paint.Style.STROKE);

        handlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        handlePaint.setColor(Color.DKGRAY);
        handlePaint.setStrokeWidth(1);
        handlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        handlePaint.setShader(new RadialGradient(digitX, digitY,
               handleRadius, Color.DKGRAY, Color.GRAY, Shader.TileMode.CLAMP));
    }

    public void digitDown() {
        isDragging = true;
    }
    public void digitUp() {
        isDragging = false;

        int px = x + (w / 2);
        int py = y + (h / 2);
        double constant = (double)handleRadius / 10.75;
        intervalsX = constant;
        intervalsY = constant;
    }

    public void Draw(Canvas canvas) {
        int px = x + (w / 2);
        int py = y + (h / 2);
        int radius = Math.min(px, py);

        // Draw the background
        canvas.drawCircle(px, py, radius - innerPadding, circlePaint);

        // Draw the handle
        canvas.drawCircle((int) digitX, (int) digitY,
                handleRadius, handlePaint);

        canvas.save();

        if (!isDragging) {
            float centerX = (float)x + (float) w / 2;
            float centerY = (float)y + (float) h / 2;
            if (digitX > centerX) {
                digitX -= intervalsX;
            }
            if (digitX < centerX){
                digitX += intervalsX;
            }
            if (digitY > centerY) {
                digitY -= intervalsY;
            }
            if (digitY < centerY) {
                digitY += intervalsY;
            }
            intervalsX *= 0.99;
            intervalsY *= 0.99;
        }
        updateJoystick(digitX, digitY);
    }

    private void updateJoystick(float dx, float dy) {
        int px = x + (w / 2);
        int py = y + (h / 2);
        int radius = Math.min(px, py) - handleInnerBoundaries;

        digitX = (dx - px);
        digitX = Math.max(Math.min(digitX, radius), -radius) + px;

        digitY = (dy - py);
        digitY = Math.max(Math.min(digitY, radius), -radius) + py;

        joyStickX = (float) Math.floor((digitX - px) / radius * sensitivity);
        joyStickY = (float) Math.floor((digitY - py) / radius * sensitivity);
    }

    public void digitMove(float dx, float dy) {
        updateJoystick(dx, dy);
    }

    public float getJoyX() {
        return joyStickX;
    }
    public float getJoyY() {
        return joyStickY;
    }
}