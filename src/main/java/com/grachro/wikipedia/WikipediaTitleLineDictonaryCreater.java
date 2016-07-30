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
 * main arg0 = jawiki-xxx-pages-articles.xml の ファイルパス arg1 = main arg1 =
 * 出力するtsvのファイルのパス。([行数\tタイトル行]形式)
 */
public class WikipediaTitleLineDictonaryCreater {

    public static void main(String[] args) throws IOException {
        String wikiPagesMetaXmlFilePath = args[0];
        String outputFilePath = args[1];

        WikipediaUtils.createTitleLineDictionary(wikiPagesMetaXmlFilePath, outputFilePath);

    }

}