package com.sustech.dboj.backend.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Objects;

public class IOUtil {
    @Value("${servlet.multipart.location}")
    private static String uploadLocation;
    public static void fileStore( MultipartFile file, String path) throws IOException {
        BufferedOutputStream out = new BufferedOutputStream(
                new FileOutputStream( new File( uploadLocation +  path ) ) );
        out.write( file.getBytes( ) );
        out.flush( );
        out.close( );
    }
}
