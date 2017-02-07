package com.chenxi.tools.qnsync;

import java.io.File;
import java.net.URISyntaxException;

/**
 * Created by ChenXi on 2017/1/25.
 */
public class TestJavaPath {
    public static void main(String[] args) throws URISyntaxException {
        System.out.println(getRootPath(null));

        String path = TestJavaPath.class.getResource("/log4j.properties").getPath();
        File file = new File(path);
        System.out.println(file.exists());

        System.out.println(new File("").getAbsoluteFile());

        File file2 = new File(TestJavaPath.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
        System.out.println(file2.getAbsoluteFile().getAbsoluteFile());
    }

    private static String getRootPath(String path) {
        String rootPath = TestJavaPath.class.getResource("/").getPath();
        if (path == null) return rootPath;
        return rootPath + path;
    }
}
