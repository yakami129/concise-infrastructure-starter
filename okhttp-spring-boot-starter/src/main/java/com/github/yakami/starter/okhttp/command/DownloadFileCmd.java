package com.github.yakami.starter.okhttp.command;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * Created by alan on 2022/10/17.
 * 下载文件命令
 */
@Slf4j
public class DownloadFileCmd {

    private final OkHttpClient okHttpClient;

    public DownloadFileCmd(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    /**
     * 下载文件到目录
     *
     * @param url               文件URL
     * @param directoryFilePath 保存到文件目录路径
     * @return
     */
    public boolean downloadFile(String url, String directoryFilePath) {
        return downloadFile(url, FileUtil.file(directoryFilePath));
    }

    /**
     * 下载文件到目录
     *
     * @param url           文件URL
     * @param fileAlias     文件别名
     * @param directoryFile 保存到文件目录路径
     * @return
     */
    public boolean downloadFile(String url, String fileAlias, File directoryFile) {

        // 进行文件夹相关校验
        checkDirectoryFile(directoryFile);

        boolean result = true;
        // 写入到文件夹中
        Request request = new Request.Builder().url(url).build();
        try (Response execute = okHttpClient.newCall(request).execute()) {
            witerDirectoryFile(url, fileAlias, directoryFile, execute);
        } catch (IOException e) {
            log.warn("[WARN] witerDirectoryFile is error , url:{},directoryPath:{}", url, directoryFile.getPath());
            result = false;
        }

        return result;
    }

    /**
     * 下载文件到目录
     *
     * @param url           文件URL
     * @param directoryFile 保存到文件目录
     * @return
     */
    public boolean downloadFile(String url, File directoryFile) {
        return downloadFile(url,null, directoryFile);
    }

    private void witerDirectoryFile(String url, String fileAlias, File directoryFile, Response execute) throws IOException {

        // 获取文件名称和文件字节流
        final byte[] fileBytes = execute.body().bytes();
        final String disposition = execute.header("Content-Disposition");

        // 获取文件名称
        String fileName;
        if (StringUtils.isNotBlank(fileAlias)) {
            fileName = fileAlias;
        } else {
            fileName = getFileName(url, disposition);
        }

        // 在目录下面创建一个文件
        final File newFile = FileUtil.file(directoryFile.getPath(), fileName);
        FileUtil.writeBytes(fileBytes, newFile);
    }

    private String getFileName(String url, String disposition) {
        String fileName = StrUtil.subAfter(disposition, "filename=", true);
        if (StringUtils.isBlank(fileName)) {
            final String path = url;
            // 从路径中获取文件名
            fileName = StrUtil.subSuf(path, path.lastIndexOf('/') + 1);
            if (StrUtil.isBlank(fileName)) {
                // 编码后的路径做为文件名
                fileName = URLUtil.encodeQuery(path, CharsetUtil.CHARSET_UTF_8);
            } else {
                // issue#I4K0FS@Gitee
                fileName = URLUtil.decode(fileName, CharsetUtil.CHARSET_UTF_8);
            }
        }
        return fileName;
    }

    private void checkDirectoryFile(File directoryFile) {
        // 校验文件夹是否存在
        if (Objects.isNull(directoryFile)) {
            throw new IllegalArgumentException("directoryFile is null");
        }

        // 检查file是否为文件夹
        if (!directoryFile.isDirectory()) {
            throw new IllegalArgumentException("directoryFile is not directory");
        }
    }


}
