package com.chenxi.tools.qnsync.persistence;

import com.chenxi.tools.qnsync.conf.ConfigHolder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChenXi on 2017/1/24.
 */
public class Persistence {

    public static List<String> read() throws IOException {
        // check empty of persistence.json
        String content = FileUtils.readFileToString(ConfigHolder.PERSISTENCE);
        if (StringUtils.isBlank(content)) {
            return new ArrayList<>();
        }

        ObjectMapper mapper = new ObjectMapper();
        List list = mapper.readValue(ConfigHolder.PERSISTENCE, List.class);
        return list;
    }

    public static void write(List<String> files) throws IOException {
        write(files, true);
    }

    public static void write(List<String> files, boolean append) throws IOException {
        List<String> resultFiles = new ArrayList<>();
        if (append) {
            List<String> origFiles = read();
            resultFiles.addAll(origFiles);
        }
        resultFiles.addAll(files);

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(ConfigHolder.PERSISTENCE, resultFiles);
    }
}
