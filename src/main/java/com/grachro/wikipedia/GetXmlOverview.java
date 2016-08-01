package com.grachro.wikipedia;


import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.regex.Pattern;

public class GetXmlOverview {
    private final static Pattern PATTERN = Pattern.compile("'''.*?'''（.*?）");

   public static void main(String[] args) throws IOException {

       String wikiPagesMetaXmlFilePath = args[0];
       String outputFilePath = args[1];

       OutputStream out = FileUtils.openOutputStream(new File(outputFilePath));
       BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
       try {
           WikipediaUtils.getOverViews(wikiPagesMetaXmlFilePath,bw);
       } finally {
           IOUtils.closeQuietly(bw);
           IOUtils.closeQuietly(out);
       }


    }

}