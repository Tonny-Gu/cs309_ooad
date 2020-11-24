package com.sustech.dboj.backend.config;

import com.sustech.dboj.backend.controller.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 全局文件类型拦截器
 */
@Component
public class FileTypeInterceptor extends HandlerInterceptorAdapter {
    private static final Logger logger = LoggerFactory.getLogger( FileTypeInterceptor.class );
    @Override
    public boolean preHandle( HttpServletRequest request,
                              HttpServletResponse response, Object handler)throws Exception {
        boolean flag= true;
        // 判断是否为文件上传请求
        if (request instanceof MultipartHttpServletRequest ) {
            MultipartHttpServletRequest multipartRequest =
                    (MultipartHttpServletRequest) request;
            Map<String, MultipartFile> files =
                    multipartRequest.getFileMap();
            //对多部件请求资源进行遍历
            for (String formKey : files.keySet( )) {
                MultipartFile multipartFile =
                        multipartRequest.getFile( formKey );
                assert multipartFile != null;
                String filename = multipartFile.getOriginalFilename( );
                //判断是否为限制文件类型
                assert filename != null;
                if ( !checkFile( filename ) ) {
                    //限制文件类型，请求转发到原始请求页面，并携带错误提示信息
                    logger.info( "Invalid file type: {}", filename );
                    flag = false;
                }
            }
        }
        return flag;
    }
    /**
     * 判断是否为允许的上传文件类型,true表示允许
     */
    private boolean checkFile(String fileName) {
        String suffixList = "md,sql,txt";
        String suffix = fileName.substring(fileName.lastIndexOf(".")
                + 1, fileName.length());
        return suffixList.contains( suffix.trim( ).toLowerCase( ) );
    }
}


