package com.grachro.wikipedia;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Map;

/**
 * Wikipediaで配布されているコンテンツxmlから<page></page>を抜き取ります。
 */
public class WikipediaXmlPagePikerWithDitionaryTsv {

    public static void main(String[] args) {
        String dictionaryTsv = args[1]; // WikipediaUtils.createTitleLineDictionary()の出力結果
        Map<String, Integer> titleLineDictionary = WikipediaUtils.getDictionaryMap(dictionaryTsv);

        String wikiPagesMetaXmlFilePath = args[0];
        String outPutDirPath = args[2];
        String[] titles = args[3].split(",");

        File outPutDir = new File(outPutDirPath);
        outPutDir.mkdirs();


        for(String title : titles) {
            String pageXml = WikipediaUtils.getPageXml(titleLineDictionary, wikiPagesMetaXmlFilePath, title);
            File xml = new File(outPutDir,title + ".xml");
            System.out.println(pageXml);

            try {
                FileUtils.writeStringToFile(xml, pageXml);
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

}