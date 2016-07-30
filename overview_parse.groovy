def f = new File("overview.txt")
f.eachLine {
try{
 def line = it.substring(3,it.size()-1)
 def line2 = line.split("'''（")
 println("${line2[0]}\t${line2[1]}")
}catch(e){}
}
