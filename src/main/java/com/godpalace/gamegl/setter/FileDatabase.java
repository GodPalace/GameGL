package com.godpalace.gamegl.setter;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class FileDatabase {
    private FileDatabase() {}

    private static void checkAnnotation(Class<?> clazz) {
        // TODO: Check if class is annotated with @Database
        if (!clazz.isAnnotationPresent(Database.class))
            throw new IllegalArgumentException(
                    "Class " + clazz.getName() + " is not annotated with @Database");
    }

    private static void checkPath(String path) {
        if (path == null || !path.endsWith(".db"))
            throw new IllegalArgumentException("Invalid path: " + path);
    }

    private static boolean checkField(Field field) {
        // TODO: Check if field is ok

        int modifiers = field.getModifiers();
        return field.isAnnotationPresent(Data.class) &&
                Serializable.class.isAssignableFrom(field.getType()) &&
                Modifier.isStatic(modifiers);
    }

    private static Variable readField(InputStream in) throws Exception {
        // TODO: read field from file
        int len;
        int nameLength, valueLength;
        byte[] lengthBytes, nameBytes, valueBytes;

        lengthBytes = new byte[4];
        len = in.read(lengthBytes, 0, lengthBytes.length);
        if (len == -1) return null;
        nameLength = (lengthBytes[0] & 0xff) << 24 | (lengthBytes[1] & 0xff) << 16 |
                (lengthBytes[2] & 0xff) << 8 | (lengthBytes[3] & 0xff);

        lengthBytes = new byte[4];
        len = in.read(lengthBytes, 0, lengthBytes.length);
        if (len == -1) return null;
        valueLength = (lengthBytes[0] & 0xff) << 24 | (lengthBytes[1] & 0xff) << 16 |
                (lengthBytes[2] & 0xff) << 8 | (lengthBytes[3] & 0xff);

        nameBytes = new byte[nameLength];
        len = in.read(nameBytes, 0, nameLength);
        if (len == -1) return null;

        valueBytes = new byte[valueLength];
        len = in.read(valueBytes, 0, valueLength);
        if (len == -1) return null;

        // TODO: get field name
        String name = new String(nameBytes, 0, nameLength, StandardCharsets.UTF_8);

        // TODO: get field value
        ByteArrayInputStream bin = new ByteArrayInputStream(valueBytes);
        ObjectInputStream oin = new ObjectInputStream(bin);
        Object value = oin.readObject();

        // TODO: clean up
        oin.close();
        bin.close();

        return new Variable(name, value);
    }

    private static void writeField(OutputStream out, Field field) throws Exception {
        // TODO: write field to file
        byte[] lengthBytes, nameBytes, valueBytes;
        int nameLength, valueLength;

        // TODO: get field name
        String name = field.getName();
        nameBytes = name.getBytes(StandardCharsets.UTF_8);
        nameLength = nameBytes.length;

        // TODO: get field value
        Object value = field.get(null);
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ObjectOutputStream oout = new ObjectOutputStream(bout);
        oout.writeObject(value);
        oout.flush();
        valueBytes = bout.toByteArray();
        valueLength = valueBytes.length;

        oout.close();
        bout.close();

        // TODO: write name length to file
        lengthBytes = new byte[4];
        lengthBytes[0] = (byte) (nameLength >> 24 & 0xff);
        lengthBytes[1] = (byte) (nameLength >> 16 & 0xff);
        lengthBytes[2] = (byte) (nameLength >> 8 & 0xff);
        lengthBytes[3] = (byte) (nameLength & 0xff);
        out.write(lengthBytes, 0, lengthBytes.length);

        // TODO: write value length to file
        lengthBytes = new byte[4];
        lengthBytes[0] = (byte) (valueLength >> 24 & 0xff);
        lengthBytes[1] = (byte) (valueLength >> 16 & 0xff);
        lengthBytes[2] = (byte) (valueLength >> 8 & 0xff);
        lengthBytes[3] = (byte) (valueLength & 0xff);
        out.write(lengthBytes, 0, lengthBytes.length);

        // TODO: write name to file
        out.write(nameBytes, 0, nameLength);

        // TODO: write value to file
        out.write(valueBytes, 0, valueLength);
    }

    private static void save(File file, List<Field> fields) {
        try (FileOutputStream out = new FileOutputStream(file);
             GZIPOutputStream gout = new GZIPOutputStream(out)) {

            for (Field field : fields) {
                writeField(gout, field);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void init(Class<?> clazz) throws Exception {
        checkAnnotation(clazz);

        Database db = clazz.getAnnotation(Database.class);
        checkPath(db.path());

        File file = new File(db.path());
        boolean exists = file.exists() && file.length() > 0;

        // TODO: read all variables from file
        HashMap<String, Object> variables = new HashMap<>();
        if (exists) {
            GZIPInputStream in = new GZIPInputStream(new FileInputStream(file));
            Variable var;

            while ((var = readField(in)) != null) {
                variables.put(var.name, var.value);
            }

            in.close();
        }

        // TODO: list all fields and create table with them
        List<Field> fields = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);

            if (checkField(field)) {
                if (exists && variables.containsKey(field.getName())) {
                    field.set(null, variables.get(field.getName()));
                }

                fields.add(field);
            } else {
                throw new IllegalArgumentException("Invalid field: " + field.getName());
            }
        }

        // TODO: add shutdown hook to save variables to file
        Runtime.getRuntime().addShutdownHook(new Thread(() -> save(file, fields)));

        // TODO: set auto save timer
        long time = db.autoSaveTime();

        if (time != 0) {
            if (time < 0) {
                throw new IllegalArgumentException("Invalid auto save time: " + time);
            }

            TimeUnit unit = db.autoSaveTimeUnit();
            long delay = unit.toMillis(time);

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    save(file, fields);
                }
            }, delay, delay);
        }
    }

    protected record Variable(String name, Object value) {
    }
}
