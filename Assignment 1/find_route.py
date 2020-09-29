import sys
from itertools import repeat
#command line arguments is inititalized to different variables
p=sys.argv[1]
q=sys.argv[2]
r=sys.argv[3]
s=sys.argv[4]
#if p is inf, then we tell the program to execute informed search to find the nearest route 
if p=='inf':
  file= sys.argv[5]
  node_exp=-2
else:
  file= ''
  node_exp=0
#All the required variables are 
frm_city, dest_city, distance, frm_city_h, dis_h = [], [], [], [], []
fringe, fringed, vis_node = [], [], []
fringedic, parentdistancedic = {}, {}
distance1, route = list(), list()
parentchilddic = {}
f = r
t = s
d = 0

global dist_h
dist_h=0
inp_file = open(q, 'r')

#The tree is stored in form of an array
for i in inp_file:
    linestriped = i.rstrip()
    splitedline = linestriped.split(" ")
    if splitedline[0] == '' or splitedline[0] == 'END':
        break
    frm_city.append(splitedline[0])
    dest_city.append(splitedline[1])
    distance.append(splitedline[2])

if file:
    inp_file1 = open(file, 'r')
    for i in inp_file1:
        linestriped = i.rstrip()
        splitedline = linestriped.split(" ")
        if splitedline[0] == '' or splitedline[0] == 'END':
            break
        frm_city_h.append(splitedline[0])
        dis_h.append(splitedline[1])
else:
    dis_h = list(repeat(0,len(frm_city)))

#Function which retrieves the heuristic values
def heuristicdis(f):
    if len(frm_city_h) >1:
        for i in range(0,len(frm_city_h)):
            if frm_city_h[i]==f:
                dish=dis_h[i]
        return dish
    else:
        return 0


#Function which does backtracking of the route
def bck_trck(t, f):
    while t != f:
        if t not in parentchilddic:
            print('Distance : Infinty')
            break
        temp = parentchilddic[t]

        for i in range(len(frm_city)):
            if frm_city[i] == t:
                if dest_city[i] == temp:
                    distance1.append(int(distance[i]))
            elif dest_city[i] == t:
                if frm_city[i] == temp:
                    distance1.append(int(distance[i]))
        route.append(t)
        route.append(temp)
        t = temp

#Function which checks whether the goal state is reached or not 
#and if not gives the next node to expand 
def check_fringe(fringedics, t, d):
    if len(fringedics) < 1 or t == fringedics[0][0]:

        return 0
    else:
        f1 = fringedics[0][0]
        d = fringedics[0][1]
        del fringedic[f1]
        fringeaddition(f1, t, d)

#Function which calculates the distance from the parent node 
def dist_parent(f, d):
    if f in parentchilddic:
        parent = parentchilddic[f]
        if not parent:
            return 0
        else:
            if parentdistancedic[parent] is not 0:
                parentdistancedic[f] = d
                return d
            else:
                d = int(parentdistancedic[parent]) + int(d)
                parentdistancedic[f] = d
                return d
    else:
        parentdistancedic[f] = d
        return 0
#Function which expands the node and adds to the fringe 
def fringeaddition(f, t, d):
    parentdistance = dist_parent(f, d)
    global node_exp
    node_exp += 1
    for i in range(len(frm_city)):
        if frm_city[i] == f and dest_city[i] not in vis_node and dest_city[i] not in fringedic:
            dish_h=heuristicdis(dest_city[i])
            fringedic[dest_city[i]] = str(int(distance[i]) + int(parentdistance)+ int(dist_h))
            parentchilddic[dest_city[i]] = frm_city[i]
        elif dest_city[i] == f and frm_city[i] not in vis_node and frm_city[i] not in fringedic:
            dish_h=heuristicdis(frm_city[i])
            fringedic[frm_city[i]] = str(int(distance[i]) + int(parentdistance) + int(dist_h))
            parentchilddic[frm_city[i]] = dest_city[i]
    fringedics = sorted(fringedic.iteritems(), key=lambda (k, v): (v, k))
    vis_node.append(f)
    set(vis_node)
    state = check_fringe(fringedics, t, d)
    if state == 0:
        return fringedics

#Function which prints the route 
def routeprint():
    print 'Distance is  ' + str(total_dist) + 'Km'
    if route > 1:
        for i in range(len(route) - 2):
            first = route.pop()
            fdis = distance1.pop()
            second = route.pop()
            print str(first) + ' to ' + str(second) + ', ' + str(fdis) + 'Km'
            if not route:
                break
    else:
        print 'route :'


fringedics = fringeaddition(f, t, d)
bck_trck(t, f)
total_dist = sum(distance1)
routeprint()
print 'node expression : '+str(node_exp)