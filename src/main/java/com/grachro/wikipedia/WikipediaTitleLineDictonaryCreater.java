package com.grachro.wikipedia;

import java.io.IOException;

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