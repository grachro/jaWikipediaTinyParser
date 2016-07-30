package com.grachro.wikipedia;

import java.util.Map;

/**
 * Wikipediaで配布されているコンテンツxmlから<page></page>を抜き取ります。
 */
public class WikipediaXmlPagePikerWithDitionaryTsv {

    public static void main(String[] args) {
        String dictionaryTsv = args[1]; // WikipediaUtils.createTitleLineDictionary()の出力結果
        Map<String, Integer> titleLineDictionary = WikipediaUtils.getDictionaryMap(dictionaryTsv);

        String wikiPagesMetaXmlFilePath = args[0];
        String title = args[2];

        String pageXml = WikipediaUtils.getPageXml(titleLineDictionary, wikiPagesMetaXmlFilePath, title);
        System.out.println(pageXml);
    }

}