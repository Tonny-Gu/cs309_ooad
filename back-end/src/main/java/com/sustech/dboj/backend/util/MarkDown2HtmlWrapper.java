package com.sustech.dboj.backend.util;

import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.gitlab.GitLabExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.options.DataHolder;


import com.vladsch.flexmark.util.options.MutableDataSet;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class MarkDown2HtmlWrapper {

    private static String MD_CSS = null;

    static {
        try {
            File file = new File( "./style/github-markdown.css" );
            MD_CSS = FileUtils.readFileToString( file , "utf-8" );
            MD_CSS = "<style type=\"text/css\">\n" + MD_CSS + "\n</style>\n";
        } catch (Exception e) {
            MD_CSS = "";
        }
    }

    public String markdown2Html( InputStream stream ) throws IOException {
        DataHolder OPTIONS = new MutableDataSet()
                .set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create(), GitLabExtension.create( )))
                .toImmutable();
//        MutableDataSet OPTIONS = new MutableDataSet( );
//        ArrayList<Extension> extensions = new ArrayList<>( );
//        extensions.add( GitLabExtension.create( ) );// enable math support
//        extensions.add( TablesExtension.create( ) );// enable table support

//        OPTIONS.setFrom( ParserEmulationProfile.MARKDOWN );
//        OPTIONS.set( Parser.EXTENSIONS , extensions );// enable extensions

        String htmlContent = IOUtils.toString( stream , StandardCharsets.UTF_8 );
        Parser parser = Parser.builder( OPTIONS ).build( );
        HtmlRenderer renderer = HtmlRenderer.builder( OPTIONS ).build( );
        Node document = parser.parse( htmlContent );
        MarkdownEntity entity = new MarkdownEntity( );
        entity.setCss( MD_CSS );
        entity.setHtml( renderer.render( document ) );
        entity.addDivStyle( "class" , "markdown-body " );
        return entity.getHtml().toString( );
    }

    public String markdown2html_demo( InputStream stream ) throws IOException {
        MutableDataSet options = new MutableDataSet( );

        // uncomment to set optional extensions
        options.set( Parser.EXTENSIONS , Arrays.asList( TablesExtension.create( ) , StrikethroughExtension.create( ) ) );

        // uncomment to convert soft-breaks to hard breaks
        options.set( HtmlRenderer.SOFT_BREAK , "<br />\n" );

        Parser parser = Parser.builder( options ).build( );
        HtmlRenderer renderer = HtmlRenderer.builder( options ).build( );

        // You can re-use parser and renderer instances
//        System.out.println(IOUtils.toString(stream, StandardCharsets.UTF_8 ));
        Node document = parser.parse( IOUtils.toString( stream , StandardCharsets.UTF_8 ) );
        return "<p>" + renderer.render( document ) + "</p>";  // "<p>This is <em>Sparta</em></p>\n"

    }

}
