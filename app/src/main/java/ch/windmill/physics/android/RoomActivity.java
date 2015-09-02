package ch.windmill.physics.android;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.concurrent.TimeUnit;

import ch.windmill.physics.R;
import ch.windmill.physics.core.Body;
import ch.windmill.physics.core.Circle;
import ch.windmill.physics.core.Engine;
import ch.windmill.physics.core.Rectangle;
import ch.windmill.physics.core.Vector2D;

/**
 * Created by jaunerc on 04.08.2015.
 */
public class RoomActivity extends Activity implements SurfaceHolder.Callback{

    private SurfaceView surface;
    private SurfaceHolder holder;
    //private ArrayList<Body> figures;
    private AnimationLoop animationLoop;
    private Paint backgroundPaint, circlePaint, greenPaint;
    private Engine engine;

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

        greenPaint = new Paint();
        greenPaint.setColor(Color.GREEN);
        greenPaint.setAntiAlias(true);

        // Figures
        //Body c1 = new Circle(300,500,60);
        //Body c2 = new Circle(500, 520, 60);
        Body c3 = new Circle(800,200,60);
        Body c4 = new Circle(500,250,60);
        c3.setVelocity(new Vector2D(-12, 0));
        c4.setVelocity(new Vector2D(12,4));

        Body r1 = new Rectangle(100,100,50,100);
        Body r2 = new Rectangle(400,130,50,100);
        Body r3 = new Rectangle(300,100,50,100);
        r1.setVelocity(new Vector2D(12,0));
        r2.setVelocity(new Vector2D(-10,-2));
        //r3.setMass(0);

        // Engine
        engine = new Engine();
        //engine.addFigure(r1);
        //engine.addFigure(r2);
        engine.addFigure(r3);
        engine.addFigure(c3);
        //engine.addFigure(c4);
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
        animationLoop = new AnimationLoop();
        animationLoop.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Body.screenHeight = height;
        Body.screenWidth = width;
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
        c.drawRect(0, 0, width, height, backgroundPaint);

        //engine.getBodies().get(0).draw(c, circlePaint);
        //engine.getBodies().get(1).draw(c, greenPaint);
        for(Body f : engine.getBodies()) {
            f.draw(c,circlePaint);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        try {
            Body.screenHeight = 0;
            Body.screenWidth = 0;
            animationLoop.safeStop();
        } finally {
            animationLoop = null;
        }
    }

    private class AnimationLoop extends Thread {
        private volatile boolean running = true;

        @Override
        public void run() {
            engine.start();
            while (running) {
                try {
                    TimeUnit.MILLISECONDS.sleep(0);

                    draw();

                    engine.handleFrame();
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