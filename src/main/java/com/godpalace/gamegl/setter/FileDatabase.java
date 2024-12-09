package com.godpalace.gamegl.setter;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class FileDatabase {
    private FileDatabase() {}

    private static void checkAnnotation(Class<?> clazz) throws Exception {
        // TODO: Check if class is annotated with @Database
        if (!clazz.isAnnotationPresent(Database.class))
            throw new Exception("Class " + clazz.getName() + " is not annotated with @Database");
    }

    public static void init(Class<?> clazz) throws Exception {
        checkAnnotation(clazz);

        Database db = clazz.getAnnotation(Database.class);
        File file = new File(db.path());

        // TODO: Load fields with @Data annotation
        List<Field> data = new ArrayList<>();
        Field[] fields = clazz.getFields();

        for (Field field : fields) {
            int modifiers = field.getModifiers();

            if (field.isAnnotationPresent(Data.class) &&
                    field.getType() == String.class &&
                    Modifier.isStatic(modifiers) &&
                    Modifier.isPublic(modifiers) &&
                    !Modifier.isFinal(modifiers)) {

                data.add(field);
            } else {
                throw new Exception("Field " + field.getName() +
                        " is not annotated with @Data or is not a public static final String field");
            }
        }

        if (file.exists()) {
            Properties properties = new Properties();
            properties.load(new BufferedReader(new FileReader(file)));

            // TODO: Load data from file and set fields
            for (Field field : data) {
                String name = field.getName();

                if (properties.containsKey(name)) {
                    String value = properties.getProperty(name);
                    field.set(null, value);
                }
            }
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                Properties properties = new Properties();

                for (Field field : data) {
                    String name = field.getName();
                    String value = field.get(null).toString();

                    properties.setProperty(name, value);
                }

                properties.store(new BufferedWriter(new FileWriter(file)), null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
    }
}
