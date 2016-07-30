package com.grachro.wikipedia;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class WriteWikipediaXmlFromTitleList {

    public static void main(String[] args) throws IOException {

        System.out.println("dictionaryTsv load start");

        String dictionaryTsv = args[1]; // WikipediaUtils.createTitleLineDictionary()の出力結果
        Map<String, Integer> titleLineDictionary = WikipediaUtils.getDictionaryMap(dictionaryTsv);
        System.out.println("dictionaryTsv load end");

        String wikiPagesMetaXmlFilePath = args[0];
        String outPutDirPath = args[2];
        String titleFile = args[3];

        List<String> titles = FileUtils.readLines(new File(titleFile));



        File outPutDir = new File(outPutDirPath);
        outPutDir.mkdirs();


        for(String title : titles) {
            File xml = new File(outPutDir,title + ".xml");
            if (xml.exists()) {
                System.out.println(title + " exists");
                continue;
            }


            System.out.println(title + " load start");
            String pageXml = WikipediaUtils.getPageXml(titleLineDictionary, wikiPagesMetaXmlFilePath, title);
            System.out.println(title + " load end");

            System.out.println(pageXml);

            System.out.println(title + " write start");
            try {
                FileUtils.writeStringToFile(xml, pageXml);
            }catch(Exception e) {
                e.printStackTrace();
            }
            System.out.println(title + " write end");
        }
    }

}