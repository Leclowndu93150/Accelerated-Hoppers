import json
import sys
from pathlib import Path

import numpy as np
from PIL import Image

LUMA = np.array([0.3, 0.59, 0.11])
NAMESPACE = 'advanced_hoppers'
MAX_STEP = 18.5


def ramp(*stops, count=8):
    stops = np.asarray(stops, float)
    fine = np.linspace(0, len(stops) - 1, 400)
    low = np.floor(fine).astype(int).clip(0, len(stops) - 2)
    t = (fine - low)[:, None]
    colours = stops[low] * (1 - t) + stops[low + 1] * t
    lum = np.maximum.accumulate(colours @ LUMA)
    low = max(lum[0], lum[-1] - MAX_STEP * (count - 1))
    targets = np.linspace(low, lum[-1], count)
    picks = [int(np.searchsorted(lum, v).clip(0, len(lum) - 1)) for v in targets]
    return [tuple(int(round(c)) for c in colours[i]) for i in picks]


COPPER = ramp((70, 30, 22), (122, 56, 36), (168, 86, 58), (204, 116, 82), (232, 152, 114), (244, 186, 146))
EXPOSED = ramp((60, 44, 40), (104, 80, 70), (146, 114, 98), (176, 136, 116), (204, 166, 144), (226, 198, 176))
WEATHERED = ramp((30, 48, 42), (56, 86, 66), (84, 120, 88), (106, 150, 110), (134, 178, 138), (176, 210, 178))
OXIDIZED = ramp((20, 44, 46), (34, 80, 74), (52, 118, 104), (74, 152, 130), (98, 180, 152), (150, 214, 188))
DEEPSLATE = ramp((22, 22, 28), (38, 38, 46), (56, 56, 64), (76, 76, 84), (98, 98, 106), (124, 124, 130))
IRON = ramp((52, 52, 60), (92, 92, 98), (134, 134, 138), (174, 174, 176), (206, 206, 202), (232, 232, 224))
OBSIDIAN = ramp((8, 6, 14), (18, 12, 30), (30, 20, 48), (46, 32, 70), (66, 48, 96), (94, 72, 130))
CRYING = ramp((42, 6, 84), (78, 16, 140), (116, 44, 192), (156, 92, 228), (192, 140, 244), (218, 190, 252))
QUARTZ = ramp((112, 98, 96), (146, 134, 128), (180, 170, 162), (208, 200, 192), (230, 224, 216), (246, 244, 236))
GOLD = ramp((84, 50, 12), (150, 102, 22), (196, 146, 34), (226, 182, 52), (244, 212, 86), (250, 232, 136))
TUFF = ramp((32, 36, 32), (52, 58, 52), (74, 80, 74), (96, 100, 92), (120, 122, 112), (148, 148, 136))
BREEZE = ramp((40, 54, 98), (62, 84, 140), (90, 110, 172), (122, 132, 200), (162, 160, 220), (206, 204, 244))
STONE = ramp((48, 48, 52), (76, 76, 80), (106, 106, 108), (136, 136, 136), (164, 164, 162), (194, 194, 190))
REDSTONE = ramp((70, 6, 4), (112, 10, 6), (156, 20, 8), (198, 34, 14), (234, 66, 30), (252, 124, 86))
PURPUR = ramp((58, 34, 60), (88, 58, 90), (124, 88, 124), (158, 118, 156), (184, 146, 182), (210, 184, 208))
SHULKER = ramp((104, 80, 24), (146, 116, 40), (192, 162, 66), (230, 206, 110), (246, 232, 164))
ENDSTONE = ramp((92, 86, 54), (124, 118, 78), (162, 158, 106), (196, 196, 136), (222, 226, 160), (240, 244, 196))
ENDER = ramp((16, 44, 40), (28, 70, 60), (46, 102, 82), (72, 136, 100), (112, 170, 98), (164, 208, 120))
PEARL = ramp((8, 38, 34), (12, 60, 52), (20, 92, 78), (38, 128, 110), (70, 164, 146), (130, 206, 192))
SCULK = ramp((8, 12, 16), (12, 22, 28), (16, 34, 42), (22, 50, 60), (30, 70, 80), (46, 98, 106))
SCULK_VEIN = ramp((10, 52, 60), (14, 84, 94), (26, 124, 132), (60, 166, 166), (124, 210, 202))
XP = ramp((52, 70, 12), (82, 118, 20), (126, 174, 36), (174, 216, 62), (212, 240, 108), (240, 252, 170))
DIAMOND = ramp((14, 52, 62), (18, 86, 96), (32, 140, 146), (60, 194, 194), (120, 230, 220), (190, 250, 240))
EMERALD = ramp((12, 54, 32), (18, 84, 46), (28, 128, 66), (54, 176, 92), (110, 216, 130), (180, 246, 190))
VALVE = ramp((60, 14, 14), (96, 22, 20), (142, 36, 30), (184, 58, 44), (214, 96, 74), (232, 140, 116))
WATER = ramp((24, 40, 104), (30, 62, 150), (50, 102, 200), (92, 150, 230), (150, 200, 248))
LAMP = ramp((96, 44, 18), (166, 92, 40), (222, 150, 74), (240, 196, 120), (250, 226, 176))
PATINA = ramp((56, 66, 62), (84, 104, 94), (110, 132, 118), (138, 158, 142), (168, 186, 170))
LEATHER = ramp((52, 30, 16), (84, 52, 28), (120, 80, 46), (156, 112, 70), (188, 146, 98))
BLACK = ramp((4, 3, 8), (10, 7, 18), (18, 12, 32), (28, 18, 48))


def lighten(colour, amount):
    return tuple(int(round(c + (255 - c) * amount)) for c in colour)


def palette(body, trim=None, accent=None, glow=None, inside=None):
    keys = {}
    for i, c in enumerate(body):
        keys[str(i)] = c
    for ramp_, start in ((trim, 'A'), (accent, 'a'), (glow, 'p'), (inside, 'J')):
        if ramp_:
            for i, c in enumerate(ramp_):
                keys[chr(ord(start) + i)] = c
    return keys


def put(img, x, y, rows, keys):
    for dy, row in enumerate(rows):
        for dx, key in enumerate(row):
            if key in '. ':
                continue
            img[y + dy, x + dx, :3] = keys[key]
            img[y + dy, x + dx, 3] = 255


def check(rows, w, h, name):
    assert len(rows) == h, (name, len(rows))
    for r in rows:
        assert len(r) == w, (name, r, len(r))
    return rows


WALL_PLATE = [
    '6777777777777776',
    '5656666446666565',
    '5555444334445555',
    '4443333223333444',
    '3222222222222223',
]

RIM_EDGE = ['4555555555555554']

BOX_PLAIN = [
    '22222221',
    '45555543',
    '45666543',
    '45655443',
    '34444332',
    '22222110',
]

SPOUT_PLAIN = [
    '5554',
    '5443',
    '4332',
    '3221',
]

FILL = [
    '4444',
    '4344',
    '4443',
    '3444',
]


def quadrant(rows):
    q = np.array([list(r) for r in check(rows, 8, 8, 'quadrant')])
    assert (q == q.T).all(), rows
    top = np.concatenate([q, np.rot90(q, -1)], axis=1)
    bottom = np.concatenate([np.rot90(q, 1), np.rot90(q, 2)], axis=1)
    return [''.join(r) for r in np.concatenate([top, bottom])]


def dihedral(field):
    q = np.triu(field) + np.triu(field, 1).T
    top = np.concatenate([q, np.rot90(q, -1)], axis=1)
    bottom = np.concatenate([np.rot90(q, 1), np.rot90(q, 2)], axis=1)
    return np.concatenate([top, bottom])


def rim_top_levels(seams=()):
    lv = np.full((16, 16), 3, int)
    for y in range(16):
        for x in range(16):
            ring = min(x, y, 15 - x, 15 - y)
            if ring > 1:
                continue
            along = max(min(x, 15 - x), min(y, 15 - y))
            lv[y, x] = 4 if along in seams else 6 if ring == 0 else 5
    return lv


def underside_levels(spout=4):
    lv = np.zeros((16, 16), int)
    for y in range(16):
        for x in range(16):
            d = min(x, y, 15 - x, 15 - y)
            lit = x + y < 15
            if d == 0:
                v = 3 if lit else 1
            elif d == 1:
                v = 4 if lit else 2
            elif d == 2:
                v = 3
            elif d == 3:
                v = 2
            elif d == 4:
                v = 5 if lit else 3
            else:
                v = 4
            lv[y, x] = v
    lo, hi = 8 - spout // 2, 8 + spout // 2
    for y in range(lo, hi):
        for x in range(lo, hi):
            d = min(x - lo, y - lo, hi - 1 - x, hi - 1 - y)
            lit = (x - lo) + (y - lo) < spout - 1
            lv[y, x] = (3 if lit else 2) if d == 0 else 0
    return lv


def floor_levels(throat=4):
    lv = np.ones((16, 16), int)
    hole = throat / 2
    for y in range(16):
        for x in range(16):
            radius = max(abs(x - 7.5), abs(y - 7.5))
            if radius < hole - 1:
                lv[y, x] = 0
            elif radius < hole:
                lv[y, x] = 1
            elif radius < hole + 1:
                lv[y, x] = 4
            elif radius < 5:
                lv[y, x] = 3
            elif radius < 6:
                lv[y, x] = 2
    return lv


def levels_to(img, x, y, lv, ramp_):
    h, w = lv.shape
    for dy in range(h):
        for dx in range(w):
            img[y + dy, x + dx, :3] = ramp_[int(np.clip(lv[dy, dx], 0, len(ramp_) - 1))]
            img[y + dy, x + dx, 3] = 255


ITEM_BASE = [
    '................',
    '................',
    '....aaaaaaaa....',
    '..bahhhhhhhhad..',
    '.behhkkkkkkhhef.',
    '.bbeekkkkkheebf.',
    '..bbdgggggedbf..',
    '...babbbbbbdf...',
    '....baeeaddf....',
    '....begggadf....',
    '.....bggeaf.....',
    '.....beggdf.....',
    '......bgaf......',
    '......bedf......',
    '.......bf.......',
    '................',
]

ITEM_RANKS = {'f': 0, 'b': 2, 'd': 3, 'a': 5, 'e': 7, 'g': 8}


class Hopper:
    def __init__(self, name, body, trim=None, accent=None, glow=None, inside=None,
                 wall=WALL_PLATE, rim=RIM_EDGE, box=BOX_PLAIN, spout=SPOUT_PLAIN, fill=FILL,
                 seams=(), floor=None, floor_ramp=None, underside=None, item=ITEM_BASE, item_overlay=None,
                 wide_spout=False, model='hopper', rim_ramp=None, underside_ramp=None, floor_overlay=None,
                 rim_overlay=None, underside_overlay=None, front=None, grain=0.0, grain_cell=2.0, seed=0,
                 item_rim_ramp=None, hole=None):
        self.name = name
        self.item_rim_ramp = item_rim_ramp
        self.hole = hole
        self.front = front
        self.grain = grain
        self.grain_cell = grain_cell
        self.seed = seed
        self.inside = inside or [tuple(int(v * 0.62) for v in c) for c in body]
        self.keys = palette(body, trim, accent, glow, self.inside)
        self.item_keys = self.keys
        self.body = body
        self.rim_ramp = rim_ramp or body
        self.underside_ramp = underside_ramp or body
        self.floor_overlay = floor_overlay
        self.rim_overlay = rim_overlay
        self.underside_overlay = underside_overlay
        self.wall, self.rim, self.box, self.spout, self.fill = wall, rim, box, spout, fill
        self.seams = seams
        self.floor = floor
        self.floor_ramp = floor_ramp or body
        self.underside = underside
        self.item = item
        self.item_overlay = item_overlay
        self.wide_spout = wide_spout
        self.model = model

    def atlas(self):
        img = np.zeros((32, 32, 4), np.uint8)
        for y in range(6, 16, 4):
            for x in range(0, 16, 4):
                put(img, x, y, self.fill, self.keys)
        put(img, 0, 0, check(self.wall, 16, 5, self.name + ' wall'), self.keys)
        put(img, 0, 5, check(self.rim, 16, 1, self.name + ' rim'), self.keys)
        put(img, 4, 6, check(self.box, 8, 6, self.name + ' box'), self.keys)
        if self.wide_spout:
            put(img, 5, 12, check(self.spout, 6, 4, self.name + ' spout'), self.keys)
        else:
            put(img, 6, 12, check(self.spout, 4, 4, self.name + ' spout'), self.keys)
        levels_to(img, 16, 0, rim_top_levels(self.seams), self.rim_ramp)
        if self.rim_overlay:
            put(img, 16, 0, check(self.rim_overlay, 16, 16, self.name + ' rim overlay'), self.keys)
        if self.underside:
            put(img, 0, 16, check(self.underside, 16, 16, self.name + ' underside'), self.keys)
        else:
            levels_to(img, 0, 16, underside_levels(6 if self.wide_spout else 4), self.underside_ramp)
        if self.underside_overlay:
            put(img, 0, 16, check(self.underside_overlay, 16, 16, self.name + ' underside overlay'), self.keys)
        if self.floor:
            put(img, 16, 16, check(self.floor, 16, 16, self.name + ' floor'), self.keys)
        else:
            levels_to(img, 16, 16, floor_levels(6 if self.wide_spout else 4), self.floor_ramp)
        if self.floor_overlay:
            put(img, 16, 16, check(self.floor_overlay, 16, 16, self.name + ' floor overlay'), self.keys)
        if self.front:
            put(img, 18, 2, check(self.front, 8, 6, self.name + ' front'), self.keys)
        if self.grain:
            img = apply_grain(img, self.body, self.grain, self.grain_cell, self.seed)
        return img

    def item_icon(self):
        img = np.zeros((16, 16, 4), np.uint8)
        rows = check(self.item, 16, 16, self.name + ' item')
        hole_ramp, wall, deep = self.hole or (self.inside, 3, 1)
        for y, row in enumerate(rows):
            for x, key in enumerate(row):
                if key in ITEM_RANKS:
                    ramp_ = self.item_rim_ramp if self.item_rim_ramp and y <= 6 else self.body
                    rank = ITEM_RANKS[key]
                    colour = lighten(ramp_[-1], 0.55) if rank >= len(ramp_) else ramp_[rank]
                elif key == 'h':
                    colour = hole_ramp[wall]
                elif key == 'k':
                    colour = hole_ramp[deep]
                else:
                    continue
                img[y, x, :3] = colour
                img[y, x, 3] = 255
        if self.item_overlay:
            put(img, 0, 0, check(self.item_overlay, 16, 16, self.name + ' item overlay'), self.item_keys)
        return img


GRAIN_REGIONS = [(0, 0, 16, 5), (0, 6, 4, 16), (12, 6, 16, 16), (4, 6, 12, 12), (16, 0, 32, 16), (0, 16, 16, 32)]


def value_noise(w, h, cell, seed):
    rng = np.random.default_rng(seed)
    gw, gh = int(np.ceil(w / cell)) + 2, int(np.ceil(h / cell)) + 2
    lattice = rng.random((gh, gw))
    yy, xx = np.mgrid[0:h, 0:w]
    fx, fy = xx / cell, yy / cell
    x0, y0 = fx.astype(int), fy.astype(int)
    tx, ty = fx - x0, fy - y0
    tx, ty = tx * tx * (3 - 2 * tx), ty * ty * (3 - 2 * ty)
    a = lattice[y0, x0] * (1 - tx) + lattice[y0, x0 + 1] * tx
    b = lattice[y0 + 1, x0] * (1 - tx) + lattice[y0 + 1, x0 + 1] * tx
    return a * (1 - ty) + b * ty


def apply_grain(img, body, strength, cell, seed):
    lookup = {tuple(c): i for i, c in enumerate(body)}
    noise = value_noise(img.shape[1], img.shape[0], cell, seed + 7)
    noise[0:16, 16:32] = dihedral(noise[0:8, 16:24])
    noise[0:5, 8:16] = noise[0:5, 0:8][:, ::-1]
    out = img.copy()
    for x0, y0, x1, y1 in GRAIN_REGIONS:
        for y in range(y0, y1):
            for x in range(x0, x1):
                if x1 == 32 and 18 <= x < 26 and 2 <= y < 8:
                    continue
                level = lookup.get(tuple(int(v) for v in img[y, x, :3]))
                if level is None or level in (0, len(body) - 1):
                    continue
                n = noise[y, x] - 0.5
                if n > 0.5 - strength / 2:
                    level += 1
                elif n < -0.5 + strength / 2:
                    level -= 1
                out[y, x, :3] = body[level]
    return out


def difference(a, b):
    delta = a.astype(float) - b.astype(float)
    return max(abs(delta @ LUMA), np.abs(delta).max() * 0.6)


def despeckle(img, rects, threshold=20.0, passes=3):
    out = img.copy()
    for _ in range(passes):
        changed = False
        for x0, y0, x1, y1 in rects:
            for y in range(y0, y1):
                for x in range(x0, x1):
                    if out[y, x, 3] == 0:
                        continue
                    here = out[y, x, :3]
                    neighbours = [(x + dx, y + dy) for dx, dy in ((1, 0), (-1, 0), (0, 1), (0, -1))
                                  if x0 <= x + dx < x1 and y0 <= y + dy < y1 and out[y + dy, x + dx, 3] > 0]
                    if not neighbours:
                        continue
                    if any((out[ny, nx, :3] == here).all() for nx, ny in neighbours):
                        continue
                    if any(x0 <= x + dx < x1 and y0 <= y + dy < y1 and out[y + dy, x + dx, 3] > 0
                           and (out[y + dy, x + dx, :3] == here).all()
                           for dx, dy in ((1, 1), (-1, 1), (1, -1), (-1, -1))):
                        continue
                    best = min(neighbours, key=lambda n: difference(out[n[1], n[0], :3], here))
                    if difference(out[best[1], best[0], :3], here) > threshold:
                        out[y, x, :3] = out[best[1], best[0], :3]
                        changed = True
        if not changed:
            break
    return out


def uv_rects(model):
    rects = set()
    for element in model['elements']:
        for face in element['faces'].values():
            u0, v0, u1, v1 = face['uv']
            x0, x1 = sorted((round(u0 * 2), round(u1 * 2)))
            y0, y1 = sorted((round(v0 * 2), round(v1 * 2)))
            if x1 > x0 and y1 > y0:
                rects.add((x0, y0, x1, y1))
    return sorted(rects)


def hopper_elements(spout_lo=6, spout_hi=10, side=False, splitter=False, front=False):
    w = '#0'
    s0, s1 = spout_lo / 2, spout_hi / 2
    elements = [
        {'from': [0, 10, 0], 'to': [16, 11, 16], 'faces': {
            'north': {'uv': [0, 2.5, 8, 3], 'texture': w}, 'east': {'uv': [0, 2.5, 8, 3], 'texture': w},
            'south': {'uv': [0, 2.5, 8, 3], 'texture': w}, 'west': {'uv': [0, 2.5, 8, 3], 'texture': w},
            'up': {'uv': [8, 8, 16, 16], 'texture': w}, 'down': {'uv': [0, 8, 8, 16], 'texture': w}}},
        {'from': [0, 11, 0], 'to': [2, 16, 16], 'faces': {
            'north': {'uv': [7, 0, 8, 2.5], 'texture': w}, 'east': {'uv': [0, 0, 8, 2.5], 'texture': w},
            'south': {'uv': [0, 0, 1, 2.5], 'texture': w}, 'west': {'uv': [0, 0, 8, 2.5], 'texture': w},
            'up': {'uv': [8, 0, 9, 8], 'texture': w}}},
        {'from': [14, 11, 0], 'to': [16, 16, 16], 'faces': {
            'north': {'uv': [0, 0, 1, 2.5], 'texture': w}, 'east': {'uv': [0, 0, 8, 2.5], 'texture': w},
            'south': {'uv': [7, 0, 8, 2.5], 'texture': w}, 'west': {'uv': [0, 0, 8, 2.5], 'texture': w},
            'up': {'uv': [15, 0, 16, 8], 'texture': w}}},
        {'from': [2, 11, 0], 'to': [14, 16, 2], 'faces': {
            'north': {'uv': [1, 0, 7, 2.5], 'texture': w}, 'south': {'uv': [1, 0, 7, 2.5], 'texture': w},
            'up': {'uv': [9, 0, 15, 1], 'texture': w}}},
        {'from': [2, 11, 14], 'to': [14, 16, 16], 'faces': {
            'north': {'uv': [1, 0, 7, 2.5], 'texture': w}, 'south': {'uv': [1, 0, 7, 2.5], 'texture': w},
            'up': {'uv': [9, 7, 15, 8], 'texture': w}}},
        {'from': [4, 4, 4], 'to': [12, 10, 12], 'faces': {
            'north': {'uv': [2, 3, 6, 6], 'texture': w}, 'east': {'uv': [2, 3, 6, 6], 'texture': w},
            'south': {'uv': [9, 1, 13, 4] if front else [2, 3, 6, 6], 'texture': w}, 'west': {'uv': [2, 3, 6, 6], 'texture': w},
            'down': {'uv': [2, 10, 6, 14], 'texture': w}}},
    ]
    side_uv = [s0, 6, s1, 8]
    end_uv = [s0, 8 + s0, s1, 8 + s1]
    if not side or splitter:
        elements.append({'from': [spout_lo, 0, spout_lo], 'to': [spout_hi, 4, spout_hi], 'faces': {
            'north': {'uv': side_uv, 'texture': w}, 'east': {'uv': side_uv, 'texture': w},
            'south': {'uv': side_uv, 'texture': w}, 'west': {'uv': side_uv, 'texture': w},
            'down': {'uv': end_uv, 'texture': w}}})
    if side and not splitter:
        elements.append({'from': [spout_lo, 4, 0], 'to': [spout_hi, 8, 4], 'faces': {
            'north': {'uv': end_uv, 'texture': w},
            'east': {'uv': side_uv, 'texture': w, 'rotation': 270}, 'west': {'uv': side_uv, 'texture': w, 'rotation': 90},
            'up': {'uv': side_uv, 'texture': w, 'rotation': 180}, 'down': {'uv': side_uv, 'texture': w}}})
    if splitter:
        for frm, to, end, sides in (
                ([6, 4, 0], [10, 8, 4], 'north', ('east', 'west', 'up', 'down')),
                ([6, 4, 12], [10, 8, 16], 'south', ('east', 'west', 'up', 'down')),
                ([0, 4, 6], [4, 8, 10], 'west', ('north', 'south', 'up', 'down')),
                ([12, 4, 6], [16, 8, 10], 'east', ('north', 'south', 'up', 'down'))):
            faces = {end: {'uv': end_uv, 'texture': w}}
            for f in sides:
                rot = {('north', 'up'): 180, ('south', 'up'): 0, ('west', 'up'): 90, ('east', 'up'): 270,
                       ('north', 'east'): 270, ('north', 'west'): 90, ('south', 'east'): 90, ('south', 'west'): 270,
                       ('west', 'north'): 270, ('west', 'south'): 90, ('east', 'north'): 90, ('east', 'south'): 270,
                       ('north', 'down'): 0, ('south', 'down'): 180, ('west', 'down'): 270, ('east', 'down'): 90}[(end, f)]
                faces[f] = {'uv': side_uv, 'texture': w, 'rotation': rot}
            elements.append({'from': frm, 'to': to, 'faces': faces})
    return elements


def block_model(hopper, side=False):
    texture = '%s:block/%s' % (NAMESPACE, hopper.name)
    lo, hi = (5, 11) if hopper.wide_spout else (6, 10)
    return {
        'ambientocclusion': False,
        'textures': {'0': texture, 'particle': texture},
        'elements': hopper_elements(lo, hi, side, hopper.model == 'splitter', hopper.front is not None),
    }


def save(img, path):
    path.parent.mkdir(parents=True, exist_ok=True)
    Image.fromarray(np.asarray(img, np.uint8)).save(path)


def write_json(data, path):
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(data, indent=2) + '\n')


def generate(out, hoppers, extra_items):
    assets = out / 'assets' / NAMESPACE
    for h in hoppers:
        rects = uv_rects(block_model(h)) + uv_rects(block_model(h, side=True))
        save(despeckle(h.atlas(), sorted(set(rects))), assets / 'textures' / 'block' / (h.name + '.png'))
        save(despeckle(h.item_icon(), [(0, 0, 16, 16)]), assets / 'textures' / 'item' / (h.name + '.png'))
        write_json(block_model(h), assets / 'models' / 'block' / (h.name + '.json'))
        if h.model != 'splitter':
            write_json(block_model(h, side=True), assets / 'models' / 'block' / (h.name + '_side.json'))
        write_json({'parent': 'minecraft:item/generated', 'textures': {'layer0': '%s:item/%s' % (NAMESPACE, h.name)}},
                   assets / 'models' / 'item' / (h.name + '.json'))
        write_json({'model': {'type': 'minecraft:model', 'model': '%s:item/%s' % (NAMESPACE, h.name)}},
                   assets / 'items' / (h.name + '.json'))
    for name, img in extra_items.items():
        save(despeckle(img, [(0, 0, 16, 16)]), assets / 'textures' / 'item' / (name + '.png'))
        write_json({'parent': 'minecraft:item/generated', 'textures': {'layer0': '%s:item/%s' % (NAMESPACE, name)}},
                   assets / 'models' / 'item' / (name + '.json'))
        write_json({'model': {'type': 'minecraft:model', 'model': '%s:item/%s' % (NAMESPACE, name)}},
                   assets / 'items' / (name + '.json'))


if __name__ == '__main__':
    from variants import HOPPERS, EXTRA_ITEMS
    generate(Path(sys.argv[1]) if len(sys.argv) > 1 else Path(__file__).parent / 'out', HOPPERS, EXTRA_ITEMS())
