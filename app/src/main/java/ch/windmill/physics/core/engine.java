package ch.windmill.physics.core;

import java.util.ArrayList;

/**
 * Created by jaunerc on 24.08.15.
 */
public class engine {
    public final int pixelsPerMeter = 10;

    private ArrayList<Figure> figures;
    private long startTime, lastTimeMs, frameDuration;

    public engine() {
        figures = new ArrayList<>();
        startTime = System.currentTimeMillis();
        lastTimeMs = -1;
        frameDuration = -1;
    }

    public ArrayList<Figure> getFigures() {
        return figures;
    }

    public void setFigures(ArrayList<Figure> figures) {
        this.figures = figures;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getLastTimeMs() {
        return lastTimeMs;
    }

    public void setLastTimeMs(long lastTimeMs) {
        this.lastTimeMs = lastTimeMs;
    }

    public long getFrameDuration() {
        return frameDuration;
    }

    public void setFrameDuration(long frameDuration) {
        this.frameDuration = frameDuration;
    }

    public void handleFrame() {
        long curTime = System.currentTimeMillis();

        if(lastTimeMs < 0) {
            lastTimeMs = curTime;
            return;
        }

        frameDuration = curTime - lastTimeMs;
        lastTimeMs = curTime;

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
    }
}
