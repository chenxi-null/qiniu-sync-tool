package com.chenxi.tools.qnsync.conf;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * Created by ChenXi on 2017/1/24.
 */
public class ConfigHolder {
    public static final Logger logger = LoggerFactory.getLogger(ConfigHolder.class);

    public static final File CONFIG;
    public static final File PERSISTENCE;

    public static final String SYNC_DIR;
    public static final String PSHELL;
    public static final String BUCKET;
    public static final String DOMAIN;

    static {
        File rootDir;
        try {
            // jar包路径, 如: \path\to\demo.jar, 如果是直接运行class文件，得到的则是根加载目录，如: ...\target\classes\
            String rootPath0 = ConfigHolder.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            logger.trace(rootPath0);
            File rootPathFile0 = new File(rootPath0);
            // jar包所在目录
            rootDir = rootPathFile0.getParentFile();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new RuntimeException();
        }

        CONFIG = new File(rootDir, "conf/config.json");
        PERSISTENCE = new File(rootDir, "conf/persistence/persistence.json");

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map;
        try {
            map = mapper.readValue(CONFIG, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new RuntimeException();
        }
        SYNC_DIR = map.get("sync-dir");
        PSHELL = map.get("pshell");
        BUCKET = map.get("bucket");
        DOMAIN = map.get("domain");
    }
}
