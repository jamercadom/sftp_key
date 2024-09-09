package org.example;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.io.FileInputStream;
import java.io.File;
import java.util.Properties;

public class SFTPUpload {

    public static void main(String[] args) {
        String localFile = "/Users/JAMERCADOM/Downloads/muebles.ndjson";
        String sftpHost = "172.22.209.27";
        String sftpUser = "sftpatg";
        String sftpPassword = "TfdaRAR5Cg";
        int sftpPort = 22;  // Default SFTP port
        String remoteDirectory = "/home/sftpatg/jamercadom";

        Session session = null;
        ChannelSftp channelSftp = null;

        try {
            JSch jsch = new JSch();
            session = jsch.getSession(sftpUser, sftpHost, sftpPort);
            session.setPassword(sftpPassword);

            // Configure the session with no host key verification
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            session.connect();

            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();

            // Get the file name from the local file path
            String fileName = new File(localFile).getName();

            // Upload the file with the same name as local
            try (FileInputStream fis = new FileInputStream(localFile)) {
                channelSftp.cd(remoteDirectory);
                channelSftp.put(fis, fileName);  // Use the local file name for the remote upload
                System.out.println("File uploaded successfully with the name: " + fileName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (channelSftp != null) {
                channelSftp.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }
    }
}
