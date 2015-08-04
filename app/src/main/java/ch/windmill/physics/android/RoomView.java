package ch.windmill.physics.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

import ch.windmill.physics.core.AABB;
import ch.windmill.physics.core.Vector2D;

/**
 * Created by jaunerc on 04.08.2015.
 */
public class RoomView extends SurfaceView implements SurfaceHolder.Callback {
    private ArrayList<AABB> objects;
    private AnimationThread thread;
    private Paint pObj;

    public RoomView(Context context) {
        super(context);
        initDrawables();
        getHolder().addCallback(this);
        setFocusable(true);
    }

    private void initDrawables() {
        objects = new ArrayList<>();
        objects.add(new AABB(new Vector2D(30,30),new Vector2D(100,100), 5, 35));

        pObj = new Paint();
        pObj.setColor(Color.RED);
        pObj.setStyle(Paint.Style.FILL);
    }

    /**
     * This is called immediately after the surface is first created.
     * Implementations of this should start up whatever rendering code
     * they desire.  Note that only one thread can ever draw into
     * a {@link Surface}, so you should not draw into the Surface here
     * if your normal rendering will be in another thread.
     *
     * @param holder The SurfaceHolder whose surface is being created.
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new AnimationThread(getHolder(), this);
        thread.setRun(true);
        thread.start();
    }

    /**
     * This is called immediately after any structural changes (format or
     * size) have been made to the surface.  You should at this point update
     * the imagery in the surface.  This method is always called at least
     * once, after {@link #surfaceCreated}.
     *
     * @param holder The SurfaceHolder whose surface has changed.
     * @param format The new PixelFormat of the surface.
     * @param width  The new width of the surface.
     * @param height The new height of the surface.
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    /**
     * This is called immediately before a surface is being destroyed. After
     * returning from this call, you should no longer try to access this
     * surface.  If you have a rendering thread that directly accesses
     * the surface, you must ensure that thread is no longer touching the
     * Surface before returning from this function.
     *
     * @param holder The SurfaceHolder whose surface is being destroyed.
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        thread.setRun(false);
        try {
            thread.join();
        } catch (InterruptedException e) {}
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.WHITE);
        for(AABB aabb : objects) {
            canvas.drawRect(aabb.getMin().getX(), aabb.getMin().getY(),aabb.getMax().getX(), aabb.getMax().getY(), pObj);
        }
    }

    private class AnimationThread extends Thread {
        private SurfaceHolder surfaceHolder;
        private RoomView view;
        private boolean run;

        public AnimationThread(final SurfaceHolder surfaceHolder, final RoomView view) {
            this.surfaceHolder = surfaceHolder;
            this.view = view;
            run = false;
        }

        @Override
        public void run() {
            Canvas c;

            while(run) {
                c = null;

                try {
                    c = surfaceHolder.lockCanvas(null);
                    synchronized (surfaceHolder) {
                        view.onDraw(c);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if(c != null) {
                        surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
        }

        public void setRun(final boolean run) {
            this.run = run;
        }
    }
}
