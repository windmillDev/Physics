package ch.windmill.physics.android;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import ch.windmill.physics.R;
import ch.windmill.physics.core.BaseModel;
import ch.windmill.physics.core.CircleModel;
import ch.windmill.physics.core.Particle;
import ch.windmill.physics.core.Rectangle;

/**
 * Created by jaunerc on 04.08.2015.
 */
public class RoomActivity extends Activity implements SurfaceHolder.Callback{

    private SurfaceView surface;
    private SurfaceHolder holder;
    private CircleModel model = new CircleModel(20);
    private ArrayList<BaseModel> models;
    private GameLoop gameLoop;
    private Paint backgroundPaint, circlePaint;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.room);

        surface = (SurfaceView) findViewById(R.id.room_surface);
        holder = surface.getHolder();
        surface.getHolder().addCallback(this);

        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.WHITE);

        circlePaint = new Paint();
        circlePaint.setColor(Color.BLUE);
        circlePaint.setAntiAlias(true);


        // Models
        model.setAccel(0, 0);

        models = new ArrayList<>();
        models.add(new Rectangle(100,60));
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        model.setAccel(0, 0);


    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        gameLoop = new GameLoop();
        gameLoop.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        model.setSize(width, height);
        Particle.screenHeight = height;
        Particle.screenWidth = width;
    }

    private void draw() {
        Canvas c = null;
        try {

            c = holder.lockCanvas();

            if(c != null) {
                doDraw(c);
            }
        } finally {
            if(c != null) {
                holder.unlockCanvasAndPost(c);
            }
        }
    }

    private void doDraw(Canvas c) {
        int width = c.getWidth();
        int height = c.getHeight();
        c.drawRect(0,0,width,height,backgroundPaint);

        float circleX, circleY;
        synchronized (model.LOCK) {
            circleX = model.circlePixelX;
            circleY = model.circlePixelY;
        }
        c.drawCircle(circleX,circleY,model.getCircleRadius(),circlePaint);

        models.get(0).draw(c,circlePaint);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        try {
            model.setSize(0,0);
            Particle.screenHeight = 0;
            Particle.screenWidth = 0;
            gameLoop.safeStop();
        } finally {
            gameLoop = null;
        }
    }

    private class GameLoop extends Thread {
        private volatile boolean running = true;

        @Override
        public void run() {
            while (running) {
                try {
                    TimeUnit.MILLISECONDS.sleep(5);

                    draw();
                    model.updatePhysics();
                    models.get(0).updatePhysics();
                } catch (InterruptedException e) {
                    running = false;
                }
            }
        }

        public void safeStop() {
            running = false;
            interrupt();
        }
    }
}