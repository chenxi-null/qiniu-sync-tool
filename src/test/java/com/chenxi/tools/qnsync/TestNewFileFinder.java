package com.chenxi.tools.qnsync;

import org.junit.Test;

import java.util.List;

/**
 * Created by ChenXi on 2017/1/24.
 */
public class TestNewFileFinder {
    private NewFileFinder newFileFinder = new NewFileFinder();

    @Test
    public void getNewFiles() throws Exception {
        final String SYNC_DIR = "D:/ChenXi/Pictures/qiniu-sync";
        List<String> newFiles = newFileFinder.getNewFiles(SYNC_DIR);
        System.out.println(newFiles);
    }
}
