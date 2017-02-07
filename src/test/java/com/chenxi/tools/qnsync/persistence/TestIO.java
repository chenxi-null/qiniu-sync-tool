package com.chenxi.tools.qnsync.persistence;

import com.chenxi.tools.qnsync.conf.ConfigHolder;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created by ChenXi on 2017/1/24.
 */
public class TestIO {
    @Test
    public void read() throws Exception {
        List<String> read = Persistence.read();
        System.out.println(read);
    }

    @Test
    public void write() throws Exception {
        Persistence.write(Arrays.asList("/a/b.txt", "/c/d/demo.png"), true);
        System.out.println(FileUtils.readFileToString(ConfigHolder.PERSISTENCE));
    }
}
