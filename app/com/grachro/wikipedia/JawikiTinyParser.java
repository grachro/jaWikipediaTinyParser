package com.grachro.wikipedia;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 概要 Wikipediaで配布されているコンテンツxmlから、タイトルとカテゴリを抜出す超簡易パーサです
 * 
 * 対象データ
 * wget http://dumps.wikimedia.org/jawiki/20140503/jawiki-20140503-pages-meta
 * -current.xml.bz2 bunzip2 jawiki-20140503-pages-meta-current.xml.bz2
 */
public class JawikiTinyParser extends DefaultHandler {

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {

		String filePath = args[0];

		SAXParserFactory spfactory = SAXParserFactory.newInstance();
		SAXParser parser = spfactory.newSAXParser();

		parser.parse(new File(filePath), new JawikiTinyParser());
	}

	private Pattern CATEGORY_PATTERN = Pattern.compile("\\[\\[Category:.+?\\]\\]");

	private boolean titleTag = false;
	private boolean nsTag = false;
	private boolean idTag = false;
	private boolean textTag = false;

	private String currentTitle = "";
	private String currentNameSpace = "-1";
	private String currentId = "-1";
	private StringBuilder currentPageContents = new StringBuilder();

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) {
		clearTagFlags();
		if ("title".equals(qName)) {
			this.titleTag = true;
		} else if ("ns".equals(qName)) {
			this.nsTag = true;
		} else if ("id".equals(qName)) {
			idTag = true;
		} else if ("text".equals(qName)) {
			this.currentPageContents = new StringBuilder();
			textTag = true;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (this.textTag) {
			String output = "id=" + this.currentId + ",namespace=" + this.currentNameSpace + ",title=" + this.currentTitle;
			System.out.println(output);

			String contents = this.currentPageContents.toString();
			LineIterator itr = IOUtils.lineIterator(new StringReader(contents));
			while (itr.hasNext()) {
				String line = itr.next();
				if (line.indexOf("[[Category") == -1) {
					continue;
				}
				Matcher matcher = CATEGORY_PATTERN.matcher("line");
				while (matcher.find()) {
					String category = matcher.group();
					System.out.println("¥t" + category);
				}

				if (line.startsWith("[[Category")) {
					System.out.println("¥t" + line);
				}
			}
		}
		clearTagFlags();
	}

	private void clearTagFlags() {
		this.titleTag = false;
		this.nsTag = false;
		this.idTag = false;
		this.textTag = false;
	}

	@Override
	public void characters(char[] ch, int offset, int length) throws SAXException {
		String text = new String(ch, offset, length);
		try {
			parse(text);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void parse(String text) throws IOException {
		if (this.titleTag) {
			this.currentTitle = text;
		} else if (this.nsTag) {
			this.currentNameSpace = text;
		} else if (this.idTag) {
			this.currentId = text;
		} else if (this.textTag) {
			this.currentPageContents.append(text);
		}
	}

}