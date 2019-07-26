package com.oecoo.utils;

import com.oecoo.gf.util.PropertiesUtil;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by gf on 2018/5/1.
 */
public class FTPUtil {

    private static final Logger logger = LoggerFactory.getLogger(FTPUtil.class);

    private static String ftpAddress = PropertiesUtil.getProperty("ftp.server.ip");
    private static String ftpUser = PropertiesUtil.getProperty("ftp.user");
    private static String ftpPwd = PropertiesUtil.getProperty("ftp.pass");
    private static String remotePath = PropertiesUtil.getProperty("ftp.file.remotePath");


    public static boolean uploadFile(List<File> files) throws IOException {
        FTPUtil ftpUtil = new FTPUtil(ftpAddress,21,ftpUser,ftpPwd);
        String ftpFiledir = FTPUtil.remotePath;
        logger.info("开始与FTP Server建立连接");
        boolean isSuccess = ftpUtil.uploadFile(ftpFiledir,files);
        logger.info("FTP Server 结束上传 ，上传结果:{}",isSuccess);
        return isSuccess;
    }
    private boolean uploadFile (String remotePath,List<File> files) throws IOException {
        boolean isSuccess = true;
        FileInputStream fis = null;
        if(connectFtp(this.ip,this.port,this.user,this.pwd)){
            //连接ftp服务器成功
            try {
                ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.enterLocalActiveMode();
                for (File fileItem: files) {
                    fis = new FileInputStream(fileItem);
                    isSuccess = ftpClient.storeFile(fileItem.getName(),fis);
                }
            } catch (IOException e) {
                logger.error("------------上传ftp服务器异常----------",e);
                isSuccess = false;
            }finally {
                fis.close();
                ftpClient.disconnect();
            }
        }
        return isSuccess;
    }
    /**
     * 连接ftp服务器
     * @param ip
     * @param port
     * @param user
     * @param pwd
     * @return
     */
    private boolean connectFtp(String ip,int port,String user,String pwd){
        boolean resultStatus = false;
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(ip,port);
            resultStatus = ftpClient.login(user,pwd);
        } catch (IOException e) {
            logger.error("连接FTP服务器异常",e);
        }
        return resultStatus;
    }

    private String ip;
    private int port;
    private String user;
    private String pwd;
    private FTPClient ftpClient;

    public FTPUtil(String ip, int port, String user, String pwd) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.pwd = pwd;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
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

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }
}
