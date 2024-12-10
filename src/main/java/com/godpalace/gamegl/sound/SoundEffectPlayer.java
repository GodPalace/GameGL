package com.godpalace.gamegl.sound;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SoundEffectPlayer {
    private final ThreadPoolExecutor executor;
    private final byte[] soundData;
    private boolean isClosed;

    public SoundEffectPlayer(File file, int poolSize) throws MalformedURLException {
        this(file.toURI().toURL(), poolSize);
    }

    private static byte[] getSoundData(URL soundURL) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             InputStream in = soundURL.openStream()) {

            byte[] buffer = new byte[4096];
            int len;

            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }

            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public SoundEffectPlayer(URL soundURL, int poolSize) {
        this.executor = new ThreadPoolExecutor(poolSize, poolSize * 2,
                5, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

        this.soundData = getSoundData(soundURL);
        this.isClosed = false;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void close() {
        isClosed = true;
        executor.shutdown();
        executor.shutdownNow();
    }

    public void play() {
        if (isClosed) {
            throw new IllegalStateException("SoundEffectPlayer is closed");
        }

        executor.execute(new SoundPlayerTask());
    }

    public void waitUntilDone() throws InterruptedException, RuntimeException {
        if (!executor.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS)) {
            throw new RuntimeException("SoundEffectPlayer executor did not terminate");
        }
    }

    private class SoundPlayerTask implements Runnable {
        @Override
        public void run() {
            try (AudioInputStream in = AudioSystem.getAudioInputStream(
                    new ByteArrayInputStream(soundData))) {

                DataLine.Info info = new DataLine.Info(SourceDataLine.class, in.getFormat());
                SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
                line.open(in.getFormat());
                line.start();

                byte[] buffer = new byte[4096];
                int len;

                while ((len = in.read(buffer)) != -1) {
                    line.write(buffer, 0, len);
                }

                line.drain();
                line.stop();
                line.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
