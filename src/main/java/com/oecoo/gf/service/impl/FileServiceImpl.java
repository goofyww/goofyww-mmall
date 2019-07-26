package com.oecoo.gf.service.impl;

import com.google.common.collect.Lists;
import com.oecoo.gf.service.IFileService;
import com.oecoo.gf.util.FTPUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by gf on 2018/5/1.
 */
@Service("iFileService")
@Slf4j
public class FileServiceImpl implements IFileService {

    public String upload(MultipartFile file, String path) {
        String fileName = file.getOriginalFilename();
        //                     获得文件名
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
        //                     获得文件名的扩展名(格式)后缀 eg: jpg
        String uploadFileName = UUID.randomUUID().toString() + "." + fileExtensionName;
        //                     使用UUID随机生成一个不可重复的的文件名 + .+后缀格式
        log.info("开始上传文件,上传文件的文件名:{},上传的路径:{},新文件名:{}", fileName, path, uploadFileName);

        File fileDir = new File(path);//  <--------- 这是【路径】
        if (!fileDir.exists()) {//目录不存在
            fileDir.setWritable(true);//设置可读
            fileDir.mkdirs();//创建这个目录
        }

        File targetFile = new File(path, uploadFileName);//   <-------这是tomcat创建好路径下的【文件】

        try {
            file.transferTo(targetFile);//Spring 将文件上传到Tomcat的指定文件夹下
            //上传本地文件夹成功

            FTPUtil.uploadFile(Lists.<File>newArrayList(targetFile));
            //上传Ftp服务器完成

            targetFile.delete();
            //删除本地tomcat本地文件
        } catch (IOException e) {
            log.error("----------------上传文件异常---------------", e);
            return null;
        }


        return targetFile.getName();
    }

//    public static void main(String[] args) {
//        String path = "adsffds.bc.jpg";
//        String fileName = path.substring(path.lastIndexOf(".")+1);
//        String uploadFileName = UUID.randomUUID().toString()+"."+fileName;
//        System.out.println(uploadFileName);
//    }

}
