package com.sustech.dboj.backend.Util;

import com.sustech.dboj.backend.util.MarkDown2HtmlWrapper;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MarkDown2HtmlTest {

    @Test
    void test01() throws IOException {
        System.out.println("-----------test from csdn-----------" );
        MarkDown2HtmlWrapper mhw = new MarkDown2HtmlWrapper();
        InputStream is = null;
        try {
            is = new FileInputStream("./src/test/testresources/testmd.md");
        } catch (FileNotFoundException e) {
            e.printStackTrace( );
        }
        System.out.println( mhw.markdown2Html( is ) );
    }

    @Test
    void test02() throws IOException {
        System.out.println("-----------test demo-----------" );
        MarkDown2HtmlWrapper mhw = new MarkDown2HtmlWrapper();
        InputStream is = null;
        try {
            is = new FileInputStream("./src/test/testresources/testmd.md");
        } catch (FileNotFoundException e) {
            e.printStackTrace( );
        }
        System.out.println( mhw.markdown2html_demo( is ) );
    }
}
