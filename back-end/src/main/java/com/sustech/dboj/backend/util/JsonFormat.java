package com.sustech.dboj.backend.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sustech.dboj.backend.domain.Submission;
import com.sustech.dboj.backend.domain.TestCase;

import java.util.List;


public class JsonFormat {
    public static String submitFormat( Submission submission , List<TestCase> testCases ) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper( );
        objectMapper.setSerializationInclusion( JsonInclude.Include.NON_NULL );
        submission.setStudent( null );
        submission.setContest( null );
        submission.setQuestion( null );

        String submissionJson = objectMapper.writeValueAsString( submission );

        StringBuilder Cases = new StringBuilder( ",testCases:[" );
        for (TestCase testCase : testCases) {
            testCase.setInitDB( null );
            testCase.setQuestion( null );
            testCase.setQuestion( null );
            Cases.append( objectMapper.writeValueAsString( testCase ) );
        }
        Cases.append( "]" );
        submissionJson = submissionJson.substring( 0 , submissionJson.length( ) - 1 ) + Cases + "}";
        return submissionJson;
    }

    public static String initFormat( TestCase testCase ) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper( );
        objectMapper.setSerializationInclusion( JsonInclude.Include.NON_NULL );
        String language = testCase.getQuestion( ).getDbType( );
        testCase.setQuestion( null );
        String testCaseJson = objectMapper.writeValueAsString( testCase );
        testCaseJson = testCaseJson.substring( 0 , testCaseJson.length( ) - 1 ) + ",\"language\":\"" + language + "\"}";
        return testCaseJson;
    }
}
