package com.chenxi.tools.qnsync;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.chenxi.tools.qnsync.conf.ConfigHolder.BUCKET;
import static com.chenxi.tools.qnsync.conf.ConfigHolder.PSHELL;
import static com.chenxi.tools.qnsync.conf.ConfigHolder.SYNC_DIR;

/**
 * Created by ChenXi on 2017/1/24.
 */
public class PshellInvoker {
    public static final Logger logger = LoggerFactory.getLogger(PshellInvoker.class);

    /**
     * Usage: qshell fput <Bucket> <Key> <LocalFile> [<Overwrite>] [<MimeType>] [<UpHost>]
     *
     * @param files
     * @return 列表(java.util.List)：上传失败的文件名和命令行回显信息, 如果没有上传失败的则返回一个空列表，而不是null。
     * @throws IOException
     */
    public List<Data> invoke(List<String> files) throws IOException {
        List<Data> failList = new ArrayList<>();

        for (String fileName : files) {
            StringBuffer buffer = new StringBuffer();
            buffer.append(PSHELL)
                    .append(" fput ")
                    .append(BUCKET + " ")
                    .append(fileName + " ")
                    .append(SYNC_DIR + "/" + fileName);
            String cmd = buffer.toString();
            logger.info("command: {}", cmd);

            Process process = Runtime.getRuntime().exec(cmd);
            InputStream stream = process.getInputStream();
            String echo = IOUtils.toString(stream, "utf-8");

            if (isSuccessful(echo)) {
                logger.debug("echo successful-msg:\n {} ", echo);
            } else {
                logger.warn("echo failed-msg:\n {}", echo);
                failList.add(new Data(fileName, echo));
            }
        }

        return failList;
    }

    public class Data {
        private String fileName;
        private String echo;

        public Data(String fileName, String echo) {
            this.fileName = fileName;
            this.echo = echo;
        }

        public String getFileName() {
            return fileName;
        }

        public String getEcho() {
            return echo;
        }
    }

    private boolean isSuccessful(String echo) {
        return echo.contains("Progress: 100%") && echo.contains("success!");
    }
}
