package com.grachro.wikipedia;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;

/**
 * DB作成
 * <pre>
 * mysql -u [USER] --password=[PASSWORD]
 * create database [DB_NAME];
 * use wikipedia_mini;
 * create table page (id int(10),namespace int(2),title varchar(255));
 * create table page_category (id int(10),namespace int(2),title varchar(255),category varchar(255),category_footer varchar(255));
 * </pre>
 */
public class WikipediaInsertMysql {

	private static final String INSERT_PAGE_SQL = "insert into page (id,namespace,title) value (?,?,?)";
	private static final String INSERT_PAGE_CATEGORY_SQL = "insert into page_category (id,namespace,title,category,category_footer) value (?,?,?,?,?)";

	private static int id = -1;
	private static int namespace = -1;
	private static String title = null;
	private static int totalLine = 0;

	public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {

		// jawikiTinyParser.rb で生成したファイル
		String jawikiTinyParserText = args[0];

		// MySQL
		String dbUrl = args[1];// jdbc:mysql://[HOST]/[DB_NAME]
		String dbUsr = args[2];// [USER]
		String dbPassword = args[3];// [PASSWORD]

		String filePath = jawikiTinyParserText;
		InputStream in = FileUtils.openInputStream(new File(filePath));
		LineIterator itr = IOUtils.lineIterator(in, "UTF-8");

		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection conn = DriverManager.getConnection(dbUrl + "?" + "user=" + dbUsr + "&password=" + dbPassword);
		conn.setAutoCommit(false);

		List<String> errList = new ArrayList<String>();

		try {
			int counter = 0;

			PreparedStatement pageStmt = conn.prepareStatement(INSERT_PAGE_SQL);
			PreparedStatement pageCategoryStmt = conn.prepareStatement(INSERT_PAGE_CATEGORY_SQL);
			try {
				while (itr.hasNext()) {
					String line = itr.next();

					System.out.println(totalLine + ":" + line);
					try {
						insert(line, pageStmt, pageCategoryStmt);
					} catch (Exception e) {
						e.printStackTrace();
						errList.add(totalLine + ":" + line);
					}

					counter++;
					if (counter > 100) {
						conn.commit();
						counter = 0;
					}

					totalLine++;
				}

				conn.commit();

				System.out.println("#########################");
				System.out.println("#########################");
				System.out.println("errList.size()=" + errList.size());
				for (String err : errList) {
					System.out.println(err);
				}
				System.out.println("#########################");
				System.out.println("#########################");

			} finally {
				DbUtils.closeQuietly(pageStmt);
				DbUtils.closeQuietly(pageCategoryStmt);

			}

		} finally {
			DbUtils.closeQuietly(conn);
		}

		System.out.println("finish.");
	}

	private static void insert(String line, PreparedStatement pageStmt, PreparedStatement pageCategoryStmt) throws SQLException {
		if (line.startsWith("id=")) {
			String[] lineArray = line.split(",");
			id = Integer.parseInt(lineArray[0].substring(3));
			namespace = Integer.parseInt(lineArray[1].substring(10));
			title = lineArray[2].substring(6);

			pageStmt.setInt(1, id);
			pageStmt.setInt(2, namespace);
			pageStmt.setString(3, title);
			pageStmt.execute();

		} else if (line.startsWith("\t[[Category:")) {
			int preLen = "\t[[".length();
			String category = line.substring(preLen, line.length() - 2);

			String[] categoryArray = category.split("\\|");

			pageCategoryStmt.setInt(1, id);
			pageCategoryStmt.setInt(2, namespace);
			pageCategoryStmt.setString(3, title);
			if (categoryArray.length == 1) {
				pageCategoryStmt.setString(4, categoryArray[0]);
				pageCategoryStmt.setString(5, null);
			} else {
				pageCategoryStmt.setString(4, categoryArray[0]);

				String after = category.substring(categoryArray[0].length());
				after = cut(after, 255);

				pageCategoryStmt.setString(5, after);
			}
			pageCategoryStmt.execute();

		}
	}

	private static String cut(String ori, int length) {
		if (ori.length() > length) {
			return ori.substring(0, length);
		}
		return ori;

	}
}
