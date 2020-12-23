package com.sustech.dboj.backend.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sustech.dboj.backend.domain.Question;
import com.sustech.dboj.backend.domain.Submission;
import com.sustech.dboj.backend.domain.TestCase;

import java.util.Base64;
import java.util.List;


public class JsonFormat {
    public static String submitFormat( Submission submission , Question question , List<TestCase> testCases ) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper( );
        objectMapper.setSerializationInclusion( JsonInclude.Include.NON_NULL );
        submission.setStudent( null );
        submission.setContest( null );
        submission.setQuestion( null );
        String submissionJson = objectMapper.writeValueAsString( submission );
        String ansCode = question.getAnswerCode( );
        ansCode = new String( Base64.getDecoder( ).decode( ansCode ) );
        String ansJson = objectMapper.writeValueAsString( ansCode );
        ansJson = "\"ansCode\":" + ansJson;
        String ansCodeJson = "," + ansJson;
        String extension = "";
        String extenCode = question.getExtension( );
        if ( extenCode != null ) {
            String extenJson = objectMapper.writeValueAsString( extenCode );
            extenJson = "\"extension\":" + extenJson;
            extension = "," + extenJson;
            extension = extension.replace( "\r\n" , "\n" );
        }
        ansCodeJson = ansCodeJson.replace( "\r\n" , "\n" );
        StringBuilder Cases = new StringBuilder( ",\"testCases\":[" );
        for (TestCase testCase : testCases) {
            testCase.setInitDB( null );
            testCase.setQuestion( null );
            testCase.setQuestion( null );
            Cases.append( objectMapper.writeValueAsString( testCase ) ).append( "," );
        }
        Cases.deleteCharAt( Cases.length( ) - 1 );
        Cases.append( "]" );
        submissionJson = submissionJson.substring( 0 , submissionJson.length( ) - 1 ) + ansCodeJson + extension + Cases + "}";
        return submissionJson;
    }

    public static String initFormat( TestCase testCase , String language ) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper( );
        objectMapper.setSerializationInclusion( JsonInclude.Include.NON_NULL );
        objectMapper.configure( JsonGenerator.Feature.ESCAPE_NON_ASCII , true );
        String testCaseJson = objectMapper.writeValueAsString( testCase );
        testCaseJson = testCaseJson.substring( 0 , testCaseJson.length( ) - 1 ) + ",\"language\":\"" + language + "\"}";
        return testCaseJson;
    }
}
