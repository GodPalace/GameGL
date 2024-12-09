package com.godpalace.gamegl.test;

import com.godpalace.gamegl.setter.Data;
import com.godpalace.gamegl.setter.Database;
import com.godpalace.gamegl.setter.FileDatabase;

@Database(path = ".\\Test.properties")
public class Test {
    @Data
    public static String name = "Godpalace";

    @Data
    public static String password = "123456";

    public static void main(String[] args) throws Exception {
        FileDatabase.init(Test.class);
    }
}
