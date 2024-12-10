package com.godpalace.gamegl.sound;

import javax.sound.sampled.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

public class MusicPlayer {
    private final URL soundURL;
    private final Thread soundThread;
    private boolean isPlaying, isPaused, isClosed;
    private int bufferSize;

    public MusicPlayer(String soundPath) throws MalformedURLException {
        this(new File(soundPath));
    }

    public MusicPlayer(File soundFile) throws MalformedURLException {
        this(soundFile.toURI().toURL());
    }

    public MusicPlayer(URL soundURL) {
        this.soundURL = soundURL;
        this.soundThread = new Thread(new SoundThread());

        init();
    }

    private void init() {
        this.isPlaying = this.isPaused = this.isClosed = false;
        this.bufferSize = 4096;
    }

    public void setBufferSize(int bufferSize) {
        if (bufferSize <= 0)
            throw new IllegalArgumentException("Buffer size must be positive");

        this.bufferSize = bufferSize;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public URL getSoundURL() {
        return soundURL;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void play() {
        if (isClosed) {
            throw new IllegalStateException("Sound is closed");
        }

        if (isPlaying) {
            throw new IllegalStateException("Sound is already playing");
        }

        isPlaying = true;
        soundThread.start();
    }

    public void waitUntilDone() {
        try {
            synchronized (soundThread) {
                soundThread.wait();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void resume() {
        if (isPaused) {
            isPaused = false;

            synchronized (soundURL) {
                soundURL.notify();
            }
        } else {
            throw new IllegalStateException("Sound is not paused");
        }
    }

    public void pause() {
        if (isPlaying) {
            if (!isPaused) {
                isPaused = true;
            } else {
                throw new IllegalStateException("Sound is already paused");
            }
        } else {
            throw new IllegalStateException("Sound is not playing");
        }
    }

    public void stop() {
        close();
    }

    public void close() {
        isPlaying = false;
        isPaused = false;
        isClosed = true;
    }

    private class SoundThread implements Runnable {
        @Override
        public void run() {
            try (AudioInputStream in = AudioSystem.getAudioInputStream(soundURL)) {
                AudioFormat format = in.getFormat();

                DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
                SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);

                line.open(format);
                line.start();

                int len;
                byte[] buffer = new byte[bufferSize];

                while ((len = in.read(buffer)) != -1) {
                    if (!isPlaying) {
                        line.drain();
                        line.stop();
                        line.close();
                        break;
                    } else if (isPaused) {
                        line.drain();

                        synchronized (soundURL) {
                            soundURL.wait();
                        }
                    }

                    line.write(buffer, 0, len);
                }

                line.drain();
                line.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                synchronized (soundThread) {
                    soundThread.notify();
                }

                close();
            }
        }
    }
}
