package com.sustech.dboj.backend.util;

import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.profiles.pegdown.*;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.options.DataHolder;


import com.vladsch.flexmark.util.options.MutableDataSet;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class MarkDown2HtmlWrapper {
    public String markdown2Html( InputStream stream) throws IOException {
        DataHolder OPTIONS = PegdownOptionsAdapter.flexmarkOptions(true,
                Extensions.ALL);
        String htmlContent = IOUtils.toString(stream, StandardCharsets.UTF_8 );

        Parser parser = Parser.builder(OPTIONS).build();
        HtmlRenderer renderer = HtmlRenderer.builder(OPTIONS).build();

        Node document = parser.parse(htmlContent);
        return "<p>"+renderer.render(document)+"</p>";
    }

    public String markdown2html_demo(InputStream stream) throws IOException {
        MutableDataSet options = new MutableDataSet();

        // uncomment to set optional extensions
        options.set(Parser.EXTENSIONS, Arrays.asList( TablesExtension.create(), StrikethroughExtension.create()));

        // uncomment to convert soft-breaks to hard breaks
        options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");

        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        // You can re-use parser and renderer instances
//        System.out.println(IOUtils.toString(stream, StandardCharsets.UTF_8 ));
        Node document = parser.parse(IOUtils.toString(stream, StandardCharsets.UTF_8 ));
        return "<p>"+renderer.render(document)+"</p>";  // "<p>This is <em>Sparta</em></p>\n"

    }

}
