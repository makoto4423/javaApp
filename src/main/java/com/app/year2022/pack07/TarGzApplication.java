package com.app.year2022.pack07;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class TarGzApplication {

    public static void main(String[] args) throws IOException {
        Path source = Paths.get("C:\\Users\\lawli\\Desktop\\static\\npm\\@v2-business-components$bussion$1.0.0.tgz");
        //解压到哪
        Path target = Paths.get("C:\\Users\\lawli\\Desktop");

        if (Files.notExists(source)) {
            throw new IOException("您要解压的文件不存在");
        }

        //InputStream输入流，以下四个流将tar.gz读取到内存并操作
        //BufferedInputStream缓冲输入流
        //GzipCompressorInputStream解压输入流
        //TarArchiveInputStream解tar包输入流
        try (InputStream fi = Files.newInputStream(source);
             BufferedInputStream bi = new BufferedInputStream(fi);
             GzipCompressorInputStream gzi = new GzipCompressorInputStream(bi);
             TarArchiveInputStream ti = new TarArchiveInputStream(gzi)) {

            ArchiveEntry entry;
            while ((entry = ti.getNextEntry()) != null) {

                //获取解压文件目录，并判断文件是否损坏
                Path newPath = zipSlipProtect(entry, target);

                if (!entry.isDirectory()) {
                    //再次校验解压文件目录是否存在
                    Path parent = newPath.getParent();
                    if (parent != null) {
                        if (Files.notExists(parent)) {
                            Files.createDirectories(parent);
                        }
                    }
                    // 将解压文件输入到TarArchiveInputStream，输出到磁盘newPath目录
                    Files.copy(ti, newPath, StandardCopyOption.REPLACE_EXISTING);

                }
            }
        }
    }

    private static   Path zipSlipProtect(ArchiveEntry entry,Path targetDir)
            throws IOException {

        Path targetDirResolved = targetDir.resolve(entry.getName());
        Path normalizePath = targetDirResolved.normalize();

        if (!normalizePath.startsWith(targetDir)) {
            throw new IOException("压缩文件已被损坏: " + entry.getName());
        }

        return normalizePath;
    }


}
