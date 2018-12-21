package com.iptv.core.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.iptv.core.common.Configuration;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public class FtpUtil {
    private static FTPClient ftpClient;
    private static String _ip;
    private static int _port;
    private static String _userName;
    private static String _password;

    static {
        _ip = Configuration.webCfg.getProperty("ftp.ip");
        _port = Integer.valueOf(Configuration.webCfg.getProperty("ftp.port")).intValue();
        _userName = Configuration.webCfg.getProperty("ftp.user");
        _password = Configuration.webCfg.getProperty("ftp.pwd");
    }

    public FtpUtil() {
    }

    private static FTPClient getFTPClient() {
        if(ftpClient == null) {
            ftpClient = new FTPClient();
        }

        boolean result = true;

        try {
            ftpClient.connect(_ip, _port);
            System.out.println("FTP信息：" + _ip + "|" + _port);
            if(ftpClient.isConnected()) {
                boolean flag = ftpClient.login(_userName, _password);
                if(flag) {
                    ftpClient.setControlEncoding("UTF-8");
                    ftpClient.setFileType(2);
                    ftpClient.enterLocalPassiveMode();
                } else {
                    result = false;
                }
            } else {
                result = false;
            }

            return result?ftpClient:null;
        } catch (Exception var2) {
            var2.printStackTrace();
            return null;
        }
    }

    private static FTPClient getFTPClient(String ip, int port, String uName, String pwd) {
        FTPClient fc = new FTPClient();
        boolean result = true;

        try {
            fc.connect(ip, port);
            if(fc.isConnected()) {
                boolean flag = fc.login(uName, pwd);
                if(flag) {
                    fc.setControlEncoding("UTF-8");
                    fc.setFileType(2);
                    fc.enterLocalPassiveMode();
                } else {
                    result = false;
                }
            } else {
                result = false;
            }

            return result?fc:null;
        } catch (Exception var7) {
            var7.printStackTrace();
            return null;
        }
    }

    private static void close(InputStream in, OutputStream out, FTPClient ftpClient) {
        if(in != null) {
            try {
                in.close();
            } catch (IOException var6) {
                var6.printStackTrace();
                System.out.println("Input stream close error!");
            }
        }

        if(out != null) {
            try {
                out.close();
            } catch (IOException var5) {
                var5.printStackTrace();
                System.out.println("Onput stream close error!");
            }
        }

        if(ftpClient != null) {
            try {
                ftpClient.logout();
                ftpClient.disconnect();
            } catch (IOException var4) {
                var4.printStackTrace();
                System.out.println("Ftp client stream close error!");
            }
        }

    }

    public static boolean upload(String ip, int port, String uName, String uPwd, String fileName, String localPath, String remotePath) {
        boolean result = true;
        FileInputStream in = null;
        FTPClient ftpClient = getFTPClient(ip, port, uName, uPwd);

        try {
            File file = new File(localPath + fileName);
            in = new FileInputStream(file);
            ftpClient.changeWorkingDirectory(remotePath);
            result = ftpClient.storeFile(fileName, in);
            boolean var12 = result;
            return var12;
        } catch (IOException var15) {
            var15.printStackTrace();
        } finally {
            close(in, (OutputStream)null, ftpClient);
        }

        return false;
    }

    public static boolean upload(String fileName, String localPath, String remotePath) {
        boolean result = true;
        FileInputStream in = null;
        ftpClient = getFTPClient();

        try {
            File file = new File(localPath + fileName);
            in = new FileInputStream(file);
            result = ftpClient.changeWorkingDirectory(remotePath);
            result = ftpClient.storeFile(fileName, in);
            boolean var7 = result;
            return var7;
        } catch (IOException var10) {
            var10.printStackTrace();
        } finally {
            close(in, (OutputStream)null, ftpClient);
        }

        return false;
    }

    public static boolean upload(InputStream in, String fileName, String remotePath) {
        boolean result = true;
        ftpClient = getFTPClient();

        try {
            result = ftpClient.changeWorkingDirectory(remotePath);
            if(result) {
                result = ftpClient.storeFile(fileName, in);
            }

            boolean var6 = result;
            return var6;
        } catch (IOException var9) {
            var9.printStackTrace();
        } finally {
            close(in, (OutputStream)null, ftpClient);
        }

        return false;
    }

    public static boolean download(String ip, int port, String uName, String uPwd, String fileName, String localPath, String remotePath) {
        boolean result = true;
        FileOutputStream out = null;
        FTPClient ftpClient = getFTPClient(ip, port, uName, uPwd);

        try {
            File file = new File(localPath + fileName);
            out = new FileOutputStream(file);
            ftpClient.changeWorkingDirectory(remotePath);
            ftpClient.retrieveFile(fileName, out);
            boolean var12 = result;
            return var12;
        } catch (IOException var15) {
            var15.printStackTrace();
        } finally {
            close((InputStream)null, out, ftpClient);
        }

        return false;
    }

    public static boolean download(String fileName, String localPath, String remotePath) {
        boolean result = true;
        FileOutputStream out = null;
        ftpClient = getFTPClient();

        try {
            File file = new File(localPath + fileName);
            out = new FileOutputStream(file);
            ftpClient.changeWorkingDirectory(remotePath);
            ftpClient.retrieveFile(fileName, out);
            boolean var7 = result;
            return var7;
        } catch (IOException var10) {
            var10.printStackTrace();
        } finally {
            close((InputStream)null, out, ftpClient);
        }

        return false;
    }

    public static String readFile(String fileName, String remotePath) {
        InputStream ins = null;
        StringBuilder builder = null;
        ftpClient = getFTPClient();

        try {
            ftpClient.changeWorkingDirectory(remotePath);
            ins = ftpClient.retrieveFileStream(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(ins, "UTF-8"));
            builder = new StringBuilder(150);

            String line;
            while((line = reader.readLine()) != null) {
                builder.append(line);
            }

            reader.close();
            ftpClient.getReply();
        } catch (IOException var9) {
            var9.printStackTrace();
        } finally {
            close(ins, (OutputStream)null, ftpClient);
        }

        return builder.toString();
    }

    public static String readFile(String ip, int port, String uName, String uPwd, String fileName, String remotePath) {
        InputStream ins = null;
        StringBuilder builder = null;
        FTPClient ftpClient = getFTPClient(ip, port, uName, uPwd);

        try {
            ftpClient.changeWorkingDirectory(remotePath);
            ins = ftpClient.retrieveFileStream(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(ins, "UTF-8"));
            builder = new StringBuilder(150);

            String line;
            while((line = reader.readLine()) != null) {
                builder.append(line);
            }

            reader.close();
            ftpClient.getReply();
        } catch (IOException var14) {
            var14.printStackTrace();
        } finally {
            close(ins, (OutputStream)null, ftpClient);
        }

        return builder.toString();
    }

    public static InputStream readFileStream(String filePath) {
        InputStream ins = null;
        ftpClient = getFTPClient();
        String dir = filePath.replaceAll("((/\\w+)*.*)/(.*)", "$1");
        String fileName = filePath.replaceAll("((/\\w+)*.*)/(.*)", "$3");

        try {
            ftpClient.changeWorkingDirectory(dir);
            ins = ftpClient.retrieveFileStream(fileName);
            ftpClient.getReply();
        } catch (IOException var8) {
            var8.printStackTrace();
        } finally {
            close((InputStream)null, (OutputStream)null, ftpClient);
        }

        return ins;
    }

    public static boolean dirExist(String dirName) {
        ftpClient = getFTPClient();
        boolean flag = false;

        try {
            flag = ftpClient.changeWorkingDirectory(dirName);
        } catch (Exception var6) {
            var6.printStackTrace();
        } finally {
            close((InputStream)null, (OutputStream)null, ftpClient);
        }

        return flag;
    }

    public static boolean fileExist(String path) throws IOException {
        boolean flag = false;
        ftpClient = getFTPClient();
        FTPFile[] ftpFileArr = ftpClient.listFiles(path);
        if(ftpFileArr.length > 0) {
            flag = true;
        }

        return flag;
    }

    public static boolean mkdir(String multiDir) {
        ftpClient = getFTPClient();
        boolean res = false;
        multiDir = multiDir.endsWith("/")?multiDir:multiDir + "/";
        String[] dirs = multiDir.split("/");

        try {
            ftpClient.changeWorkingDirectory("/");

            for(int i = 1; dirs != null && i < dirs.length; ++i) {
                if(!ftpClient.changeWorkingDirectory(dirs[i]) && ftpClient.makeDirectory(dirs[i]) && !ftpClient.changeWorkingDirectory(dirs[i])) {
                    return false;
                }

                res = true;
            }

            return res;
        } catch (IOException var7) {
            var7.printStackTrace();
            return res;
        } finally {
            close((InputStream)null, (OutputStream)null, ftpClient);
        }
    }

    public static boolean mkdir(String dirName, String parentPath) {
        ftpClient = getFTPClient();
        boolean flag = false;
        boolean res = false;

        try {
            flag = ftpClient.changeWorkingDirectory(parentPath);
            if(flag) {
                res = ftpClient.makeDirectory(dirName);
            } else {
                System.out.println("上级目录不存在");
            }
        } catch (Exception var8) {
            var8.printStackTrace();
        } finally {
            close((InputStream)null, (OutputStream)null, ftpClient);
        }

        return res;
    }

    public static boolean delDir(String path) {
        ftpClient = getFTPClient();

        try {
            ftpClient.deleteFile(path);
            if(ftpClient.changeWorkingDirectory(path)) {
                FTPFile[] ftpFiles = ftpClient.listFiles();
                if(ftpFiles == null || ftpFiles.length <= 0) {
                    ftpClient.removeDirectory(path);
                }

                FTPFile[] var5 = ftpFiles;
                int var4 = ftpFiles.length;

                for(int var3 = 0; var3 < var4; ++var3) {
                    FTPFile ftpFile = var5[var3];
                    ftpClient.deleteFile(path + "/" + ftpFile.getName());
                    ftpClient.removeDirectory(path);
                }
            }
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        return true;
    }

    public static void main(String[] args) {
        boolean res = delDir("/test");
        System.out.println("res:" + res);
    }
}
