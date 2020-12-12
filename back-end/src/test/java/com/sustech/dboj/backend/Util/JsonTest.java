package com.sustech.dboj.backend.Util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

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
}
