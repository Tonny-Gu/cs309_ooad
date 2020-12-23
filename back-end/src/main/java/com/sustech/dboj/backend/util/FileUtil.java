package com.sustech.dboj.backend.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Objects;

public class FileUtil {
    public static void fileStore( MultipartFile file, String path) throws IOException {
        String uploadLocation = "./ReceFile/";
        BufferedOutputStream out = new BufferedOutputStream(
                new FileOutputStream( new File( uploadLocation +  path ) ) );
        out.write( file.getBytes( ) );
        out.flush( );
        out.close( );
    }
}
