package com.oecoo.gf.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by gf on 2018/5/1.
 */
@Slf4j
public class FTPUtil {

    private static String ftpip = PropertiesUtil.getProperty("ftp.server.ip");
    private static String ftpUser = PropertiesUtil.getProperty("ftp.user");
    private static String ftpPwd = PropertiesUtil.getProperty("ftp.pass");

    /**
     * 上传文件
     * @param files  List<File> ----->上传的文件可以是一个集合
     * @return
     */
    public static boolean uploadFile(List<File> files) throws IOException {
       FTPUtil ftpUtil = new FTPUtil(ftpip,21,ftpUser,ftpPwd);
       log.info("开始与FTP Server建立连接");
       boolean isSuccess = ftpUtil.uploadFile("image",files);
       log.info("FTP Server 结束上传 ，上传结果:{}",isSuccess);
       return isSuccess;
    }
    /**
     * 上传文件封装
     * @param remotePath    ftpServer   远程路径
     * @param file          上传的文件
     * @return
     */
    private boolean uploadFile(String remotePath,List<File> file) throws IOException {
        boolean uploaded = true;
        FileInputStream fis = null;
        if(connectServer(this.ip,this.port,this.user,this.pwd)){

            try {
                ftpClient.changeWorkingDirectory(remotePath);//更改工作目录    （文件在远程哪个目录进行操作）
                ftpClient.setBufferSize(1024);               //设置缓冲区
                ftpClient.setControlEncoding("UTF-8");       //设置字符集
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);//设置文件类型为 二进制
                ftpClient.enterLocalActiveMode();            //设置ftp进入活动(可用)模式
                for (File fileItem:file) {
                    fis = new FileInputStream(fileItem);     //将文件加到input流中
                    ftpClient.storeFile(fileItem.getName(),fis);//上传   参数为: 1.文件名  2.输入流(流中的文件位于计算机内存中)
                }
            } catch (IOException e) {
                log.error("-----------------上传ftp远程文件异常------------------",e);
                uploaded = false;
                e.printStackTrace();
            }finally {
                fis.close();
                ftpClient.disconnect();
            }

        }
        return uploaded;
    }

    private boolean connectServer(String ip,int port,String user,String  pwd){
        boolean isSuccess = false;//默认返回 false
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(ip);
            isSuccess = ftpClient.login(user,pwd);//login 成功 true  失败 false
        } catch (IOException e) {
          log.error("----------连接FtpServer异常---------",e);
        }
        return isSuccess;
    }

    private String ip;
    private int port;
    private String user;
    private String pwd;
    private FTPClient ftpClient;

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }

    public FTPUtil(String ip, int port, String user, String pwd) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.pwd = pwd;
    }

    public String getip() {
        return ip;
    }

    public void setip(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
