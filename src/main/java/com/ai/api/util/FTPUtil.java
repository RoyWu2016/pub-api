package com.ai.api.util;


import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;

/**
 * Project Name    : Public-API
 * Package Name    : com.ai.api.util
 * Creation Date   : 2016/8/12 15:02
 * Author          : Jianxiong.Cai
 * Purpose         : TODO
 * History         : TODO
 */
public class FTPUtil {
    private static FTPClient ftpClient = new FTPClient();
    private static String encoding = System.getProperty("file.encoding");

    public static boolean uploadFile(String url, int port,
                                     String username,String password,
                                     String path, String filename, InputStream input) {
        boolean result = false;
        try {
            int reply;
//            ftpClient.connect(url);//use default port
            ftpClient.connect(url, port);// connect to ftp server
            ftpClient.login(username, password);//login
            ftpClient.setControlEncoding(encoding);
            reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {//login fail
                System.out.println("login fail!!!");
                ftpClient.disconnect();
                return result;
            }
            boolean change = ftpClient.changeWorkingDirectory(path); //change working path
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            if (change) {
                result = ftpClient.storeFile(new String(filename.getBytes(encoding),"iso-8859-1"), input);
                if (result) {
                    System.out.println("upload success!");
                }
            }
            input.close();
            ftpClient.logout();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return result;
    }

    public static boolean downloadFile(String url, int port,
                                       String username,String password,
                                       String remotePath, String fileName,String localPath) {
        boolean result = false;
        try {
            int reply;
            ftpClient.setControlEncoding(encoding);
            ftpClient.connect(url, port);
            ftpClient.login(username, password);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                System.out.println("FTP server refused connection.");
                return result;
            }
            File localFile = new File(localPath+fileName);
            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(localFile));
            boolean b = ftpClient.retrieveFile(remotePath+fileName, outputStream);
            outputStream.close();
            if (b){
                System.out.print("success download file :"+fileName);
            }
            ftpClient.logout();
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return result;
    }
}
