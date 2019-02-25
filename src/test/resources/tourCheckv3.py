import sys
from math import sqrt

def parse_tsp_file(filename):
    '''
    Reads a .tsp file and returns a list of coordinates.
    '''

    coords = lambda s: (float(s.split(' ')[1]), float(s.split(' ')[2]))
    return [ coords(l.strip()) for l in open(filename) if l.strip()[0].isdigit() ]


def parse_solution_file(filename):
    '''
    Reads a .opt.tour file and returns a tour.
    '''

    return [ int(l.strip())-1 for l in open(filename, 'r') if l.strip()[0].isdigit() ]


def tour_length(tour, data):
    '''
    Returns the total length of the given tour.
    '''

    dist = lambda p1, p2: round(sqrt(sum( (x1-x2)**2 for x1, x2 in zip(p1, p2) )))
    return sum( dist(data[tour[i]], data[tour[i+1]]) for i in range(len(tour)-1) ) + dist(data[tour[len(tour)-1]], data[tour[0]])


def main():

    if len(sys.argv) < 4:
        print('Usage: tourCheckv3.py <.tsp file> <.opt.tour file> <distance>')
        return

    try:
        cities = parse_tsp_file(sys.argv[1])
    except:
        print(sys.argv[1], 'could not be opened.')
        return

    try:
        tour = parse_solution_file(sys.argv[2])
    except:
        print(sys.argv[2], 'could not be opened.')
        return

    try:
        claimed = int(sys.argv[3])
    except:
        print(sys.argv[3], 'is not a number.')
        return

    # Check all cities have been visited
    if not all((i in tour for i in range(len(cities)))):
        print('Incomplete tour, not all cities have been visited')
        return

    # Check claimed distance
    computed = tour_length(tour, cities)
    if claimed == computed:
        print('Tour is correct')
    else:
        print('Claimed distance is wrong, computed: {}, claimed: {}'.format(computed, claimed))


if __name__ == '__main__':
    main()

