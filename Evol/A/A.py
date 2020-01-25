import re


def modify(pol):
    pol = re.sub(r'([^+-])(x)', r'\1*x', pol)
    pol = re.sub(r'([^+-])(y)', r'\1*y', pol)
    return re.sub("\\^", "**", pol)


def f(x, y):
    return eval(pol, {"x": x, "y": y})


def loc(x, y, op):
    val = dict[(x, y)]
    if x - 1 < -10:
        p1 = True
    else:
        p1 = op(val, dict[(x - 1, y)])
    if x + 1 > 10:
        p2 = True
    else:
        p2 = op(val, dict[(x + 1, y)])
    if y - 1 < -10:
        p3 = True
    else:
        p3 = op(val, dict[(x, y - 1)])
    if y + 1 > 10:
        p4 = True
    else:
        p4 = op(f(x, y), dict[(x, y + 1)])
    return p1 & p2 & p3 & p4


def platF(x, y):
    val = f(x, y)
    if x - 1 < -10:
        p1 = False
    else:
        p1 = val == dict[(x - 1, y)]
    if x + 1 > 10:
        p2 = False
    else:
        p2 = val == dict[(x + 1, y)]
    if y - 1 < -10:
        p3 = False
    else:
        p3 = val == dict[(x, y - 1)]
    if y + 1 > 10:
        p4 = False
    else:
        p4 = val == dict[(x, y + 1)]
    return p1 | p2 | p3 | p4


pol = modify(open("unimulti.in", "r").read())
mx = 0
mn = 0
plat = False
dict = {}
for x in range(-10, 11):
    for y in range(-10, 11):
        dict[(x, y)] = f(x, y)
for x in range(-10, 11):
    for y in range(-10, 11):
        mx += 1 if loc(x, y, lambda a, b: a > b) else 0
        mn += 1 if loc(x, y, lambda a, b: a < b) else 0
        plat |= platF(x, y)

out = open("unimulti.out", "w")
out.writelines(["Multiple local maxima: " + ("Yes" if mx > 1 else "No") + "\n",
                "Multiple local minima: " + ("Yes" if mn > 1 else "No") + "\n",
                "Plateaus: " + ("Yes" if plat else "No") + "\n"])
out.close()
