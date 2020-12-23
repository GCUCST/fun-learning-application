package cn.cst;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import java.net.URISyntaxException;
import java.util.Vector;

public class Main {
  public static void main(String[] args) throws JSchException, SftpException, URISyntaxException {
    SftpUtil sftp = new SftpUtil();
    ChannelSftp connect = sftp.connect("localhost", 2222, "foo", "pass");
    Vector vector = sftp.listFiles("sftp-file", connect);
    for (Object a : vector) {
      System.out.println(a);
    }
    sftp.download("/sftp-file", "readme.txt", "read.txt", connect);
    System.out.println("finish");
    connect.disconnect();
  }
}
