jaWikipediaTinyParser
=====================
Wikipediaで配布されているXMLから、記事タイトルとカテゴリ名だけを抽出してMySQLにインサートするプログラムです。

Wikipediaの記事
http://ja.wikipedia.org/wiki/Wikipedia:%E3%83%87%E3%83%BC%E3%82%BF%E3%83%99%E3%83%BC%E3%82%B9%E3%83%80%E3%82%A6%E3%83%B3%E3%83%AD%E3%83%BC%E3%83%89

XMLの取得方法
wget http://dumps.wikimedia.org/jawiki/20140503/jawiki-20140503-pages-meta-current.xml.bz2
bunzip2 jawiki-20140503-pages-meta-current.xml.bz2

XMLから記事タイトルとカテゴリを一時ファイルに出力
ruby jawikiTinyParser.rb > jawiki-20140503-parsed.txt

一時ファイルをMySQLにインサート
JaWikiDbInsert.java






