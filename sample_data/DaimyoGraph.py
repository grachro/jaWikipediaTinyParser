import networkx as nx
import matplotlib.pyplot as plt

G=nx.Graph()


f = open('daimyo_daimyo2.tsv.relation')
line = f.readline()
while line:
	cols = line.rstrip("\n").split(',')
	G.add_edge(cols[0],cols[1])
	line = f.readline()
f.close

labels = {}
f2 = open('daimyo_daimyo2.tsv.list')
line2 = f2.readline()
while line2:
	cols = line2.rstrip("\n").split(',')
	labels[cols[0]] = cols[1]
	line2 = f2.readline()
f2.close

pos = nx.spring_layout(G,iterations=100)


nx.draw_networkx_edges(G,pos,width=1.0,alpha=0.4)
nx.draw_networkx_labels(G,pos,labels,font_size=16,font_family='Osaka')

plt.axis('off') #軸線非表示
plt.show()

