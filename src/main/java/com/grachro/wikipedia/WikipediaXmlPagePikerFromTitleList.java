package com.grachro.wikipedia;

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Wikipediaで配布されているコンテンツxmlから<page></page>を抜き取ります。
 */
public class WikipediaXmlPagePikerFromTitleList {

	public static void main(String[] args) throws IOException {

		// wget
		// http://dumps.wikimedia.org/jawiki/20140503/jawiki-20140503-pages-meta-current.xml.bz2
		// bunzip2 jawiki-20140503-pages-meta-current.xml.bz2
		String wikiPagesMetaXmlFilePath = args[0];

		String outPutDirPath = args[1];
		File outPutDir = new File(outPutDirPath);
		outPutDir.mkdirs();

		String titleFile = args[2];


		Set<String> titles = new HashSet<>(FileUtils.readLines(new File(titleFile)));

		Date start = new Date();

		StringBuilder sb = null;
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(wikiPagesMetaXmlFilePath));

			boolean hit = false;
			String line;
			String title = null;
			while ((line = in.readLine()) != null) {
				if (line.matches(".*<title>.*</title>.*")) {
					title = line.trim();
					title = title.substring(7, title.length() - 8);
					if (!titles.contains(title)) {
						continue;
					}

					hit = true;
					sb = new StringBuilder("<page>\n");
				}

				if (hit) {
					sb.append(line).append("\n");
					if (line.contains("</page>")) {

						File xml = new File(outPutDir,title + ".xml");
						System.out.println(title + " write start");
						try {
							FileUtils.writeStringToFile(xml, sb.toString());
						}catch(Exception e) {
							e.printStackTrace();
						}
						System.out.println(title + " write end");


						hit = false;
					}
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