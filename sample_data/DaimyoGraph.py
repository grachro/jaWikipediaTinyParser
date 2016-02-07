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

pos = nx.spring_layout(G)


# find node near center (0.5,0.5)
dmin=1
ncenter=0
for n in pos:
    x,y=pos[n]
    d=(x-0.5)**2+(y-0.5)**2
    if d<dmin:
        ncenter=n
        dmin=d

# color by path length from node near center
p=nx.single_source_shortest_path_length(G,ncenter)

plt.figure(figsize=(8,8))
nx.draw_networkx_edges(G,pos,nodelist=[ncenter],alpha=0.4)
nx.draw_networkx_nodes(G,pos,nodelist=p.keys(),
                       node_size=80,
                       node_color=[float(v) for v in p.values()],
                       cmap=plt.cm.Reds_r)

nx.draw_networkx_labels(G,pos,labels,font_size=16)

plt.axis('off') #軸線非表示
plt.show()

