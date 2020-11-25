package com.sustech.dboj.backend.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sustech.dboj.backend.domain.Question;
import com.sustech.dboj.backend.domain.Submission;
import com.sustech.dboj.backend.domain.TestCase;


public class JsonFormat {
    public String submitFormat( Submission submission , TestCase[] testCases ) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper( );
        objectMapper.setSerializationInclusion( JsonInclude.Include.NON_NULL );
        submission.setStudent( null );
        submission.setContest( null );
        submission.setQuestion( null );

        String submissionJson = objectMapper.writeValueAsString( submission );
        submissionJson = submissionJson.substring( 0 , submissionJson.length( ) - 1 ) + ", ansCode:\"" + testCases[0].getAnswerCode( ) + "\"}";
        for (TestCase testCase : testCases) {
            testCase.setInitDB( null );
            testCase.setQuestion( null );
            testCase.setAnswerCode( null );
            submissionJson = submissionJson.substring( 0 , submissionJson.length( ) - 1 ) + objectMapper.writeValueAsString( testCase ) + "}";
        }
        return submissionJson;
    }

    public String initFormat( Question question , TestCase[] testCases ) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper( );
        objectMapper.setSerializationInclusion( JsonInclude.Include.NON_NULL );
        question.setAuthor( null );
        question.setContent( null );
        question.setDegree( null );
        question.setName( null );
        question.setContests( null );
        String submissionJson = objectMapper.writeValueAsString( question );
    }
}
