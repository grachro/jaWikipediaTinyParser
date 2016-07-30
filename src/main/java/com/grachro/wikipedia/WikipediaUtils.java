package com.grachro.wikipedia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class WikipediaUtils {

    public static void createTitleLineDictionary(String wikiPagesMetaXmlFilePath, String outputFilePath)
            throws IOException {

        OutputStream out = FileUtils.openOutputStream(new File(outputFilePath));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
        try {
            readXmlTitles(wikiPagesMetaXmlFilePath, bw);
        } finally {
            IOUtils.closeQuietly(bw);
            IOUtils.closeQuietly(out);
        }

    }

    private static void readXmlTitles(String wikiPagesMetaXmlFilePath, BufferedWriter bw) {

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

                    String title = line.trim();
                    title = title.substring(7, title.length() - 8);
                    String outLine = (lineNo - 1) + "\t" + title;
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

    public static Map<String, Integer> getDictionaryMap(String dictionaryFilePath) {
        Map<String, Integer> result = new HashMap<String, Integer>();

        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(dictionaryFilePath));

            String line;
            while ((line = in.readLine()) != null) {

                String[] args = line.split("\t");
                int lineNo = Integer.parseInt(args[0]);
                String title = args[1];

                result.put(title, lineNo);

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

        return result;
    }

    public static String getPageXml(Map<String, Integer> titleLineDictionary, String wikiPagesMetaXmlFilePath, String title) {

        int lineNo = titleLineDictionary.get(title);

        StringBuilder sb = new StringBuilder();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(wikiPagesMetaXmlFilePath));

            for (int i = 0; i < lineNo - 1; ++i) {
                in.readLine();
            }

            String line;
            while ((line = in.readLine()) != null) {

                sb.append(line).append("\n");
                if (line.contains("</page>")) {
                    break;
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

        return sb.toString();
    }
}
