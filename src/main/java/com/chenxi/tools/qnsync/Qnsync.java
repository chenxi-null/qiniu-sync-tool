package com.chenxi.tools.qnsync;

import com.chenxi.tools.qnsync.persistence.Persistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.chenxi.tools.qnsync.conf.ConfigHolder.DOMAIN;
import static com.chenxi.tools.qnsync.conf.ConfigHolder.SYNC_DIR;

/**
 * 程序入口
 *
 * TODO 增加功能：1.在配置文件(config.json)里设置logger等级 2.可以使用命令行自定义配置文件路径
 * TODO 不知道用java写命令行程序是不是很蠢，只不是要把它转为成exe文件呢？
 *
 * Created by ChenXi on 2017/1/24.
 */
public class Qnsync {
    public static final Logger logger = LoggerFactory.getLogger(Qnsync.class);

    public static void main(String[] args) throws IOException {
        NewFileFinder newFileFinder = new NewFileFinder();
        PshellInvoker pshellInvoker = new PshellInvoker();

        // 1. scan the sync-directory to find the new files that will be uploaded
        List<String> newFiles = newFileFinder.getNewFiles(SYNC_DIR);
        if (newFiles.size() == 0) {
            System.out.println("没有新增的文件需要上传！");
            return;
        }
        logger.info("these files is about to upload: \n {} \n", newFiles);


        // 2. invoke pshell to upload new files
        List<PshellInvoker.Data> failedList = pshellInvoker.invoke(newFiles);
        List<String> successList = newFiles.stream()
                .filter(t -> {
                    for (PshellInvoker.Data data : failedList) {
                        if (data.getFileName().equals(t)) return false;
                    }
                    return true;
                }).collect(Collectors.toList());


        // 3. update persistence.json
        Persistence.write(successList);


        // 4. write the link of files to clipboard of system
        // template: ![Alt text](image-url)
        StringBuffer sb = new StringBuffer();
        successList.forEach(e -> sb.append(String.format("![Alt text](%s/%s)\n", DOMAIN, e)));
        String contents = sb.toString();
        Clipboard clipboard = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable transferable = new StringSelection(contents);
        clipboard.setContents(transferable, null);


        // 5. echo message
        if (failedList.size() == 0) {
            System.out.println("全部上传成功:");
            newFiles.forEach(s -> System.out.println("\t" + s));
        } else if (failedList.size() == newFiles.size()) {
            System.out.println("全部上传失败:");
            failedList.forEach(s -> System.out.println("\t" + s.getFileName()));
        } else {
            System.out.println("下列文件没有上传成功:");
            failedList.forEach(s -> System.out.println("\t" + s.getFileName()));
            System.out.println("已成功上传的文件:");
            successList.forEach(e -> System.out.println("\t" + e));
        }

    }

}
