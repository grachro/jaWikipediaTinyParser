package com.grachro.wikipedia;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

/**
 * Wikipediaで配布されているコンテンツxmlから<page></page>を抜き取ります。
 */
public class WikipediaXmlPagePiker {

	public static void main(String[] args) {

		// wget
		// http://dumps.wikimedia.org/jawiki/20140503/jawiki-20140503-pages-meta-current.xml.bz2
		// bunzip2 jawiki-20140503-pages-meta-current.xml.bz2
		String wikiPagesMetaXmlFilePath = args[0];

		Date start = new Date();

		StringBuilder sb = null;
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(wikiPagesMetaXmlFilePath));

			boolean hit = false;
			String line;
			while ((line = in.readLine()) != null) {
				if (line.contains("<title>" + args[1] + "</title>")) {
					hit = true;
					sb = new StringBuilder("<page>\n");
				}

				if (hit) {
					sb.append(line).append("\n");
					if (line.contains("</page>")) {
						hit = false;
						break;
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