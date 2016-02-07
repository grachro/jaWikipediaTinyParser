def map = [:]
def maxIndex = 0

def toIndex = {name ->
	if (map[name] == null) {
		map[name] = maxIndex
		maxIndex++
	}	
	return map[name]
}

def inputFile = args[0]

def relationFile = new File(inputFile + ".relation")
relationFile.delete()

new File(inputFile).splitEachLine("\t"){line ->
	def from = line[0]
	def to = line[1]
	def output = "${toIndex(from)},${toIndex(to)}\n"
	relationFile.append(output)
}


def listFile = new File(inputFile + ".list")
listFile.delete()
def indexMap = new TreeMap()
map.each{k,v ->
	indexMap[v] = k
}

indexMap.each{k,v ->
	//v index
	//k title
	def output = "${k},${v}\n"
	listFile.append(output)
} 