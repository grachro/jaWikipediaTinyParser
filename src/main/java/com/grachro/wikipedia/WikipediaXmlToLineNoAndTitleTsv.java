package com.grachro.wikipedia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 * Wikipediaで配布されているjawiki-xxx-pages-articles.xmlから、タイトル行(<title>.*</title>
 * )とその行数を取得します。
 * 
 * main arg0 = jawiki-xxx-pages-articles.xml の ファイルパス arg1 =
 * main arg1 = 出力するtsvのファイルのパス。([行数\tタイトル行]形式)
 */
public class WikipediaXmlToLineNoAndTitleTsv {

    public static void main(String[] args) throws IOException {
        String wikiPagesMetaXmlFilePath = args[0];
        String outputFilePath = args[1];

        OutputStream out = FileUtils.openOutputStream(new File(outputFilePath));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
        try {
            read(wikiPagesMetaXmlFilePath, bw);
        } finally {
            IOUtils.closeQuietly(bw);
            IOUtils.closeQuietly(out);
        }

    }

    private static void read(String wikiPagesMetaXmlFilePath,BufferedWriter bw) {
 

        Date start = new Date();

        StringBuilder sb = null;
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(wikiPagesMetaXmlFilePath));
            int lineNo = 0;
            String line;
            while ((line = in.readLine()) != null) {
                lineNo++;
                if (line.matches(".*<title>.*</title>.*")) {
                    String outLine = (lineNo - 1) + "\t" + line;
                    System.out.println(outLine);
                    bw.write(outLine);
                    bw.newLine();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {

                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Date end = new Date();
        System.out.println(sb);
        System.out.println("start:" + start);
        System.out.println("end  :" + end);
        System.out.println("finish");
    }

}