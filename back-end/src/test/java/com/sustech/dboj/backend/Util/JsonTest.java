package com.sustech.dboj.backend.Util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sustech.dboj.backend.domain.Question;
import com.sustech.dboj.backend.domain.Submission;
import com.sustech.dboj.backend.domain.TestCase;
import com.sustech.dboj.backend.util.JsonFormat;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class JsonTest {

    @Test
    public void test01() throws JsonProcessingException {
        String json = "{\"testCase\":[{\"id\": 1,\"env\": \"xxx\",\"status\":\"Accept\", \"info\":\"qwe\"},{\"id\": 2,\"env\": \"yyy\",\"status\":\"Accept\", \"info\":\"qwe\"},{\"id\": 3,\"env\": \"zzz\",\"status\":\"Accept\", \"info\":\"qwe\"}]}";
        ObjectNode node = new ObjectMapper( ).readValue( json , ObjectNode.class );
        System.out.println( node.get( "testCase" ).get( 0 ) );// get the 0th testcase
        System.out.println( node.get( "testCase" ).size( ) );// list size
    }

    @Test
    public void test02() {
        try {
            ObjectMapper mapper = new ObjectMapper( );
            ObjectNode root1 = mapper.createObjectNode( );
            root1.put( "nodekey1" , 1 );
            root1.put( "nodekey2" , 2 );
            System.out.println( root1.toString( ) );
            //Create the root node
            ObjectNode root = mapper.createObjectNode( );
            //Create a child node
            ObjectNode node1 = mapper.createObjectNode( );
            node1.put( "case" , 1 );
            node1.put( "status" , "Accept" );
            //Bind the child nodes
            root.put( "child" , node1 );
            //Array of nodes
            ArrayNode arrayNode = mapper.createArrayNode( );
            arrayNode.add( node1 );
            arrayNode.add( 1 );
            //Bind array node
            root.put( "arraynode" , arrayNode );
            System.out.println( mapper.writeValueAsString( root ) );
            // 得到的输出信息
            // {"child":{"nodekey1":1,"nodekey2":2},"arraynode":[{"nodekey1":1,"nodekey2":2},1]}




        } catch (JsonProcessingException e) {
            e.printStackTrace( );
        } catch (Exception ignored) {
        }

    }

    @Test
    public void test03() throws JsonProcessingException {
        // info json create test
        ObjectMapper mapper = new ObjectMapper( );
        ArrayNode root = mapper.createArrayNode( );
        int arrLen = 5;
        for (int i =0;i < arrLen;i++){
            ObjectNode node = mapper.createObjectNode( );
            node.put( "case" , 1 );
            node.put( "status" , "Accept" );
            root.add( node );
        }
        System.out.println( mapper.writeValueAsString( root )  );
    }

    @Test
    public void test04() throws JsonProcessingException {
        TestCase ts = new TestCase();
        ts.setId( 111 );
        ts.setEnv( "xxx" );
        Question qs = new Question();
        ts.setInitDB( "yyy" );
        qs.setDbType( "postgresql" );
        ts.setQuestion( qs );
        System.out.println(JsonFormat.initFormat( ts ,qs.getDbType() ));

    }

    @Test
    public void test5() throws JsonProcessingException {
        TestCase ts = new TestCase();
        Submission sb = new Submission();
        Question question = new Question();
        question.setAnswerCode( "aW5zZXJ0IGludG8gc3R1ZmYgdmFsdWVzICgnc2FtcGxlJyk7CnNlbGVjdCAqIGZyb20gc3R1ZmY7" );
        question.setExtension( "def judger(sample_code:str, testee_code:str, form:dict, db_warp) -> bool:\n" +
                "    before = db_warp.exec_sql(sample_code)\n" +
                "    db_warp.exec_sql(testee_code, ret=False)\n" +
                "    after = db_warp.exec_sql(sample_code)\n" +
                "    return before == after" );
        List<TestCase> testCaseList = new ArrayList<>(  );
        testCaseList.add( ts );
        System.out.println( JsonFormat.submitFormat( sb, question, testCaseList) );

    }
}
