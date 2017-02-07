package com.chenxi.tools.qnsync;

import com.chenxi.tools.qnsync.persistence.Persistence;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 扫描“同步文件夹”发现新增文件
 *
 * Created by ChenXi on 2017/1/24.
 */
public class NewFileFinder {
    public List<String> getNewFiles(String syncDir) throws IOException {
        File rootDir = new File(syncDir);
        Collection collection = FileUtils.listFiles(rootDir, null, true);
        List<String> list = new ArrayList<>();
        for (Object o : collection) {
            File file = (File) o;
            // normalization:
            //  1.remove the prefix of rootDirectory's name
            //  2.replace '\' with the '/'
            String resultName = file.toString().substring(rootDir.getPath().length() + 1);
            resultName = resultName.replace("\\", "/");
            list.add(resultName);
        }

        // remove the old files
        List<String> oldList = getOldFiles();
        if (oldList != null) {
            list.removeAll(oldList);
        }

        return list;
    }

    // 得到已经同步过的文件列表
    private List<String> getOldFiles() throws IOException {
        return Persistence.read();
    }
}
