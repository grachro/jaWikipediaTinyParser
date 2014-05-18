require 'rexml/parsers/baseparser'
require 'rexml/parsers/streamparser'
require 'rexml/streamlistener'

#############################################################################################
#概要
#Wikipediaで配布されているコンテンツxmlから、タイトルとカテゴリを抜出す超簡易パーサです
#
#使い方
#wget http://dumps.wikimedia.org/jawiki/20140503/jawiki-20140503-pages-meta-current.xml.bz2
#bunzip2 jawiki-20140503-pages-meta-current.xml.bz2
#ruby jawikiTinyParser.rb > jawiki-20140503-parsed.txt
#
#############################################################################################


class Listener
  include REXML::StreamListener
  def initialize
    @currentTitle=""
    @currentNameSpace=-1
    @currentId=-1
  end

  def text(text)
    if(@titleTag) then
      @currentTitle = text
    elsif(@nsTag)then
      @currentNameSpace = text	
    elsif(@idTag)then
      @currentId = text	
    elsif(@textTag) then

      puts "id=" + @currentId + ",namespace=" + @currentNameSpace + ",title=" + @currentTitle

      text.lines { |line|
        if line.index("[[Category") == 0 then
          puts "\t" + line
        end
      }
    end
  end

  def tag_start(tagName, attrs)

    clear_tag_flags()

    if tagName == 'title' then
      @titleTag = true
    elsif tagName == 'ns' then
      @nsTag = true	
    elsif tagName == 'id' then
      @idTag = true	
    elsif tagName == 'text' then
      @textTag = true	
    else
      clear_tag_flags()		
    end
  end

  def tag_end(tagName)
    clear_tag_flags()
  end

  def clear_tag_flags()
    @titleTag = false
    @nsTag = false	
    @idTag = false	
    @textTag = false
  end

end

puts('parse::start')


listener = Listener.new
open('jawiki-20140503-pages-meta-current.xml') {|f|
  REXML::Parsers::StreamParser.new(f, listener).parse
}

listener.events

puts 'parse::end'
