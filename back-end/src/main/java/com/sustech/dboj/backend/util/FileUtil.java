package com.sustech.dboj.backend.util;


import org.springframework.web.multipart.MultipartFile;

import java.io.*;


public class FileUtil {
    public static void fileStore( MultipartFile file , String path ) throws IOException {
        String uploadLocation = "./ReceFile/";
        BufferedOutputStream out = new BufferedOutputStream(
                new FileOutputStream( new File( uploadLocation + path ) ) );
        out.write( file.getBytes( ) );
        out.flush( );
        out.close( );
    }

}
