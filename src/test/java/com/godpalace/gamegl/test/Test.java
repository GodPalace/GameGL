package com.godpalace.gamegl.test;

import com.godpalace.gamegl.setter.Data;
import com.godpalace.gamegl.setter.Database;
import com.godpalace.gamegl.setter.FileDatabase;

import java.io.File;

@Database(path = ".\\Test.db")
public class Test {
    @Data
    public static String name;

    @Data
    public static File file;

    public static void main(String[] args) throws Exception {
        FileDatabase.init(Test.class);

        System.out.println(name);
        System.out.println(file.exists());
        System.out.println(file.getName());
        System.out.println(file.getAbsolutePath());
        System.out.println(file.length());
    }
}
