package com.socket.server.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {

	private FileUtils() {
	}
	
    public static boolean write(InputStream inStream, String target) {
        if (inStream == null || target.length() == 0) {
            return false;
        }
        boolean isSuccess = false;
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(target);
            byte[] buff = new byte[1024];
            int len;
            while ((len = inStream.read(buff)) > -1) {
                out.write(buff, 0, len);
            }
            out.flush();
            isSuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                } finally {
                    out = null;
                }
            }
        }
        return isSuccess;
    }
    
    public static boolean createDirectory(File file) {
    	if (file == null) {
    		return false;
    	}
    	File parent = file.getParentFile();
    	if (parent != null && !parent.exists()) {
    		return file.mkdir();
    	}
    	return false;
    }
	
}
