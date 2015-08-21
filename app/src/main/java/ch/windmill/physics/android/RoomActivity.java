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
import ch.windmill.physics.core.Circle;
import ch.windmill.physics.core.Figure;
import ch.windmill.physics.core.Rectangle;
import ch.windmill.physics.core.Vector2D;

/**
 * Created by jaunerc on 04.08.2015.
 */
public class RoomActivity extends Activity implements SurfaceHolder.Callback{

    private SurfaceView surface;
    private SurfaceHolder holder;
    private ArrayList<Figure> figures;
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

        // Figures
        figures = new ArrayList<>();
        figures.add(new Circle(300, 500, 60));
        figures.add(new Circle(500, 520, 60));

        figures.get(0).setVelocity(new Vector2D(12,0));
        figures.get(1).setVelocity(new Vector2D(-12,0));
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
        gameLoop = new GameLoop();
        gameLoop.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Figure.screenHeight = height;
        Figure.screenWidth = width;
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

        //figures.get(0).draw(c,circlePaint);
        for(Figure f : figures) {
            f.draw(c,circlePaint);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        try {
            Figure.screenHeight = 0;
            Figure.screenWidth = 0;
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

                    for(int i = 0; i < figures.size(); i++) {
                        figures.get(i).updatePosition();
                        for(int j = 0; j < figures.size(); j++) {
                            if(j != i) {
                                Figure f = figures.get(j);
                                if (f instanceof Rectangle) {
                                    figures.get(i).collisionDetect((Rectangle) f);
                                } else if (f instanceof Circle) {
                                    figures.get(i).collisionDetect((Circle) f);
                                }
                            }
                        }
                    }
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