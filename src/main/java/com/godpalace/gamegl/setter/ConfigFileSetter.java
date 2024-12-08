package com.godpalace.gamegl.setter;

import java.io.*;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class ConfigFileSetter implements Setter<String> {
    private final Properties properties;
    private final File file;

    private boolean isClosed;
    private AutoSaveListener autoSaveListener;

    public ConfigFileSetter(String filename) {
        this(filename, 5, TimeUnit.MINUTES);
    }

    public ConfigFileSetter(String filename, int autoSaveTime, TimeUnit timeUnit) {
        this(new File(filename), autoSaveTime, timeUnit);
    }

    public ConfigFileSetter(File file, int autoSaveTime, TimeUnit timeUnit) {
        this.properties = new Properties();
        this.file = file;
        this.isClosed = false;
        this.autoSaveListener = null;

        if (file.exists()) {
            try {
                properties.load(new BufferedReader(new FileReader(file)));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        if (autoSaveTime >= 0 && timeUnit != null) {
            new Thread(() -> {
                while (!isClosed) {
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                        properties.store(writer, null);

                        if (autoSaveListener != null) {
                            autoSaveListener.onAutoSave(System.currentTimeMillis());
                        }

                        TimeUnit.SECONDS.sleep(timeUnit.toSeconds(autoSaveTime));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                properties.store(new BufferedWriter(new FileWriter(file)), null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }));
    }

    public void setAutoSaveListener(AutoSaveListener autoSaveListener) {
        this.autoSaveListener = autoSaveListener;
    }

    @Override
    public void close() throws IOException {
        if (isClosed)
            throw new IOException("Already closed");

        isClosed = true;
    }

    @Override
    public void set(String key, String value) {
        properties.setProperty(key, value);
    }

    @Override
    public String get(String key) {
        if (!properties.containsKey(key))
            throw new RuntimeException("Key not found: " + key);

        return properties.getProperty(key);
    }

    @Override
    public void reset() {
        properties.clear();
    }

    @Override
    public void finish() throws IOException {
        properties.store(new BufferedWriter(new FileWriter(file)), null);
    }
}
