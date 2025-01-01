package com.godpalace.gamegl.protection;

import com.godpalace.gamegl.desktop.DesktopGraphics;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.URL;

public class ProcessProtection {
    private int pid;
    private final String exePath;
    private boolean isProtected = false;

    private final Object lock = new Object();

    private native boolean IsProcessRunning(int pid);
    private static native int GetProcessId(String name);

    static {
        File file = new File("ProcessProtection.dll");

        try {
            URL url = DesktopGraphics.class.getResource("/dll/ProcessProtection.dll");
            if (url == null) throw new RuntimeException("Failed to find ProcessProtection.dll");

            InputStream in = url.openStream();
            FileOutputStream out = new FileOutputStream(file);
            byte[] buffer = new byte[10240];
            int len;

            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }

            in.close();
            out.close();

            System.load(file.getAbsolutePath());
        } catch (Exception e) {
            throw new RuntimeException("Failed to load ProcessProtection.dll", e);
        }
    }

    public ProcessProtection(int pid, String exePath) {
        if (!new File(exePath).exists())
            throw new IllegalArgumentException("Invalid executable path: " + exePath);

        this.pid = pid;
        this.exePath = exePath;

        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();

        if (pid == 0) {
            try {
                Process process = Runtime.getRuntime().exec(exePath);
                this.pid = (int) process.pid();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        if (pid == Integer.parseInt(runtimeMXBean.getName().split("@")[0])) {
            Runtime.getRuntime().addShutdownHook(new Thread(this::restart));
        } else {
            new Thread(this::protect).start();
        }
    }

    public static int getProcessId(String name) {
        return GetProcessId(name);
    }

    public void start() {
        isProtected = true;

        synchronized (lock) {
            lock.notifyAll();
        }
    }

    public void stop() {
        isProtected = false;
    }

    public boolean isProtected() {
        return isProtected;
    }

    public int getPid() {
        return pid;
    }

    private void protect() {
        while (true) {
             try {
                 synchronized (lock) {
                     if (!isProtected) {
                         lock.wait();
                     }
                 }

                 if (!IsProcessRunning(pid)) {
                     Process process = Runtime.getRuntime().exec(exePath);
                     pid = (int) process.pid();
                 }

                 synchronized (this) {
                     wait(1000);
                 }
             } catch (Exception e) {
                 throw new RuntimeException(e);
             }
        }
    }

    private void restart() {
        try {
            Runtime.getRuntime().exec(exePath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
