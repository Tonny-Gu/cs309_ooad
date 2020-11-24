package com.sustech.dboj.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.text.SimpleDateFormat;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter( );
        ObjectMapper mapper = converter.getObjectMapper( );
        Hibernate5Module hibernate5Module = new Hibernate5Module( );
        mapper.registerModule( hibernate5Module );
        mapper.setDateFormat( new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ) );
        return converter;
    }

    @Autowired
    private FileTypeInterceptor fileTypeInterceptor;

    @Override
    public void addInterceptors( InterceptorRegistry registry){
        registry.addInterceptor(fileTypeInterceptor);
    }
}
