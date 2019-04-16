import sys
import numpy as np
import pylab as pl
from matplotlib import collections  as mc
from matplotlib.pyplot import cm

def plotFile(path):
    file = open(path, 'r').read()

    blocks = file.split('\n\n')

    lines = list()
    for block in blocks:
        sub = block.split('\n')

        if len(sub) == 1 and sub[0] is "":
            continue

        coords = list()

        for c in sub:
            cxy = c.split(' ')
            if len(cxy) is 2:
                x = cxy[0]
                y = cxy[1]
                coords.append((x,y))
        lines.append(coords)

    lc = mc.LineCollection(lines, linewidths=2)
    fig, ax = pl.subplots()
    ax.add_collection(lc)
    ax.autoscale()
    ax.margins(0.1)
    pl.savefig(path + '.png')

plotFile(sys.argv[1])