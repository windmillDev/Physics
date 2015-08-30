package ch.windmill.physics.core;

/**
 * Created by jaunerc on 30.08.15.
 */
public class Engine2 {
    public final static float FPS = 25;
    public final static float DELTATIME = 1 / FPS;

    private class AnimateThread extends Thread {
        private volatile boolean running = true;

        @Override
        public void run() {
            while (running) {

            }
        }

        public void safeStop() {
            running = false;
            interrupt();
        }
    }
}
