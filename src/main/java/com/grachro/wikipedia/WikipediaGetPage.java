package com.grachro.wikipedia;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 概要 Wikipediaで配布されているコンテンツxmlから、タイトルとカテゴリを抜出す簡易パーサです
 * 
 * Kotlin
 * start:Mon Mar 07 13:49:11 JST 2016
 * end  :Mon Mar 07 14:00:33 JST 2016
 * 11分22秒!
 * 
 * </pre>
 */
public class WikipediaGetPage extends DefaultHandler {

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {

		// wget
		// http://dumps.wikimedia.org/jawiki/20140503/jawiki-20140503-pages-meta-current.xml.bz2
		// bunzip2 jawiki-20140503-pages-meta-current.xml.bz2
		String wikiPagesMetaXmlFilePath = args[0];

		String searchPageTitle = args[1];

		WikipediaGetPage gp = new WikipediaGetPage(wikiPagesMetaXmlFilePath);

		Date start = new Date();
		Page page = gp.getPage(searchPageTitle);
		System.out.println(page.id);
		System.out.println(page.title);
		System.out.println(page.nameSpace);
		System.out.println(page.currentPageContents);
		Date end = new Date();
		System.out.println("start:" + start);
		System.out.println("end  :" + end);
			
	}

	private final String xml;

	private String findTitle;

	private String currentTitle = null;
	private String currentNameSpace = null;
	private String currentId = null;
	private StringBuilder currentPageContents = null;

	private Page page;

	private boolean findHit = false;

	private boolean titleTag = false;
	private boolean nsTag = false;
	private boolean revisionTag = false;
	private boolean idTag = false;
	private boolean textTag = false;

	public static class Page {
		private String id = "-1";
		private String title = "";
		private String nameSpace = "-1";
		private String currentPageContents = null;

		public String getId() {
			return id;
		}

		public String getTitle() {
			return title;
		}

		public String getNameSpace() {
			return nameSpace;
		}

		public String getCurrentPageContents() {
			return currentPageContents;
		}

	}

	private static class StopException extends SAXException {
		private static final long serialVersionUID = 1L;
	}

	public WikipediaGetPage(String inputXml) throws ParserConfigurationException, SAXException, IOException {
		this.xml = inputXml;
	}

	public Page getPage(String title) throws SAXException, IOException, ParserConfigurationException {

		this.findTitle = title;
		this.currentTitle = null;

		SAXParserFactory spfactory = SAXParserFactory.newInstance();
		SAXParser parser = spfactory.newSAXParser();

		try {
			parser.parse(new File(this.xml), this);
		} catch (StopException e) {
			System.out.println("強制停止");
		}
		return this.page;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) {

		if ("title".equals(qName)) {
			this.titleTag = true;
		} else if ("ns".equals(qName)) {
			this.nsTag = true;
		} else if ("id".equals(qName)) {
			this.idTag = true;
		} else if ("revision".equals(qName)) {
			this.revisionTag = true;
		} else if ("text".equals(qName)) {
			this.textTag = true;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if ("title".equals(qName)) {
			this.titleTag = false;
		} else if ("ns".equals(qName)) {
			this.nsTag = false;
		} else if ("id".equals(qName)) {
			this.idTag = false;
		} else if ("revision".equals(qName)) {
			this.revisionTag = false;
		} else if ("text".equals(qName)) {
			this.textTag = false;
		} else if ("page".equals(qName)) {
			if (this.currentTitle.equals(this.findTitle)) {
				this.page = new Page();
				this.page.id = this.currentId;
				this.page.title = this.currentTitle;
				this.page.nameSpace = this.currentNameSpace;
				this.page.currentPageContents = this.currentPageContents.toString();

				throw new StopException();
			}
			this.clearTagFlags();
		}

	}

	private void clearTagFlags() {
		this.titleTag = false;
		this.nsTag = false;
		this.revisionTag = false;
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

			if (this.currentTitle.equals(this.findTitle)) {
				this.findHit = true;
				this.currentPageContents = new StringBuilder();
			}

		} else if (this.nsTag) {
			this.currentNameSpace = text;
		} else if (!this.revisionTag && this.idTag) {
			this.currentId = text;
		} else if (this.textTag) {
			if (findHit) {
				this.currentPageContents.append(text);
			}
		}
	}

}