import sys
from pathlib import Path

import numpy as np

from hoppers import (EMERALD, ENDER, IRON, NAMESPACE, PEARL, REDSTONE, WATER, XP, despeckle, put, ramp, save)

WHITE = (255, 255, 255)
FILL = (198, 198, 198)
SHADOW = (85, 85, 85)
BLACK = (0, 0, 0)
SLOT_DARK = (55, 55, 55)
SLOT_FILL = (139, 139, 139)
HINT = (104, 104, 104)
HOVER_LIGHT = (207, 208, 247)
HOVER_FILL = (119, 120, 160)
HOVER_DARK = (55, 56, 96)

PAPER = ramp((92, 84, 70), (150, 140, 120), (200, 192, 172), (232, 226, 206), (250, 248, 236))
INK = ramp((30, 30, 46), (56, 58, 80))
WAX_RED = ramp((70, 10, 12), (120, 22, 22), (170, 40, 34), (210, 76, 60), (232, 124, 104))
WAX_GREEN = EMERALD
STRING = ramp((150, 150, 146), (200, 200, 196))
LAMP_GLOW = ramp((116, 52, 20), (176, 98, 40), (226, 156, 76), (244, 204, 126), (252, 234, 186))
LAMP_DARK = ramp((50, 26, 16), (74, 40, 22), (98, 56, 30), (122, 72, 40))
LAMP_FRAME = ramp((40, 22, 14), (70, 42, 26))
LEATHER = ramp((60, 34, 18), (98, 60, 32), (140, 94, 56), (178, 132, 86), (210, 172, 122), (232, 208, 162))
BRASS = ramp((70, 44, 16), (118, 78, 30), (166, 116, 46), (204, 156, 68), (232, 194, 104), (248, 226, 156))
GLASS = ramp((20, 60, 70), (40, 110, 120), (80, 170, 176), (150, 220, 220), (220, 250, 246))
WOOD = ramp((52, 34, 20), (86, 58, 34), (122, 86, 52), (160, 118, 74), (190, 150, 98))
EMBER = ramp((60, 8, 6), (98, 14, 10), (136, 24, 16), (170, 36, 24))


def canvas():
    return np.zeros((256, 256, 4), np.uint8)


def px(img, x, y, c):
    img[y, x, :3] = c
    img[y, x, 3] = 255


def rect(img, x0, y0, x1, y1, c):
    img[y0:y1, x0:x1, :3] = c
    img[y0:y1, x0:x1, 3] = 255


def panel(img, w, h):
    rect(img, 3, 3, w - 3, h - 3, FILL)
    rect(img, 3, 0, w - 4, 1, BLACK)
    rect(img, 0, 3, 1, h - 4, BLACK)
    rect(img, w - 1, 4, w, h - 3, BLACK)
    rect(img, 4, h - 1, w - 3, h, BLACK)
    for x, y in ((2, 1), (1, 2), (w - 4, 1), (w - 3, 2), (w - 2, 3), (1, h - 4), (2, h - 3), (3, h - 2),
                 (w - 3, h - 3), (w - 2, h - 4)):
        px(img, x, y, BLACK)
    rect(img, 3, 1, w - 4, 3, WHITE)
    rect(img, 1, 3, 3, h - 4, WHITE)
    px(img, 2, 2, WHITE)
    px(img, 3, 3, WHITE)
    rect(img, w - 3, 4, w - 1, h - 3, SHADOW)
    rect(img, 4, h - 3, w - 3, h - 1, SHADOW)
    px(img, w - 4, h - 4, SHADOW)
    px(img, w - 3, h - 3, BLACK)
    px(img, w - 4, h - 3, SHADOW)
    px(img, w - 3, h - 4, SHADOW)
    px(img, w - 4, 2, FILL)
    px(img, w - 3, 3, FILL)
    px(img, 2, h - 4, FILL)
    px(img, 3, h - 3, FILL)


def well(img, x, y, w=18, h=18, fill=SLOT_FILL):
    rect(img, x, y, x + w, y + h, fill)
    rect(img, x, y, x + w - 1, y + 1, SLOT_DARK)
    rect(img, x, y, x + 1, y + h - 1, SLOT_DARK)
    rect(img, x + 1, y + h - 1, x + w, y + h, WHITE)
    rect(img, x + w - 1, y + 1, x + w, y + h, WHITE)
    px(img, x + w - 1, y, fill)
    px(img, x, y + h - 1, fill)


def player_inventory(img, top):
    for row in range(3):
        for col in range(9):
            well(img, 7 + col * 18, top + row * 18)
    for col in range(9):
        well(img, 7 + col * 18, top + 58)


def button(img, x, y, size, light, fill, dark):
    rect(img, x, y, x + size, y + size, fill)
    rect(img, x, y, x + size - 1, y + 1, light)
    rect(img, x, y, x + 1, y + size - 1, light)
    rect(img, x + 1, y + size - 1, x + size, y + size, dark)
    rect(img, x + size - 1, y + 1, x + size, y + size, dark)


def toggle_buttons(img, x, y, size=18):
    button(img, x, y, size, WHITE, SLOT_FILL, SLOT_DARK)
    button(img, x + size, y, size, HOVER_LIGHT, HOVER_FILL, HOVER_DARK)
    button(img, x + size * 2, y, size, SLOT_DARK, SLOT_FILL, WHITE)


PAPER_SLIP = [
    '................',
    '...233333332....',
    '...3777777762...',
    '...3700000762...',
    '...3777777652...',
    '...3700007652...',
    '...3777777652...',
    '...3700000552...',
    '...3777776552...',
    '...3700076552...',
    '...37777abba....',
    '...3666abdcb....',
    '...3555bdecb....',
    '...2222acca.....',
    '........ab......',
    '................',
]

LAMP_LIT = [
    '................',
    '.1222222222222..',
    '.1rsttsr1sttsr2.',
    '.1sttuut1ttuut2.',
    '.1ttuvvu1tuvvu2.',
    '.1suvvuu1suvvu2.',
    '.1rsuuss1rsuus2.',
    '.11111222222222.',
    '.1rsttsr1sttsr2.',
    '.1sttuut1ttuut2.',
    '.1ttuvvu1tuvvu2.',
    '.1suvvuu1suvvu2.',
    '.1rsuuss1rsuus2.',
    '.1rrsssr1rsssr2.',
    '..222222222222..',
    '................',
]

LAMP_UNLIT = [
    '................',
    '.1222222222222..',
    '.1abbbba1bbbba2.',
    '.1bccddb1ccddb2.',
    '.1bcddcb1cddcb2.',
    '.1bcdccb1cdccb2.',
    '.1abccba1bccba2.',
    '.11111222222222.',
    '.1abbbba1bbbba2.',
    '.1bccddb1ccddb2.',
    '.1bcddcb1cddcb2.',
    '.1bcdccb1cdccb2.',
    '.1abccba1bccba2.',
    '.1aabbba1abbba2.',
    '..222222222222..',
    '................',
]


def distance_inside(mask):
    h, w = mask.shape
    pts = [(x, y) for y in range(-1, h + 1) for x in range(-1, w + 1)
           if not (0 <= x < w and 0 <= y < h) or not mask[y, x]]
    pts = np.array(pts, float)
    dist = np.zeros((h, w))
    for y in range(h):
        for x in range(w):
            if mask[y, x]:
                dist[y, x] = np.hypot(pts[:, 0] - x, pts[:, 1] - y).min()
    return dist


def emboss(mask, ramp_, radius=2.5):
    light = np.array([-0.55, -0.7, 0.9])
    light /= np.linalg.norm(light)
    dist = distance_inside(mask)
    height = np.sqrt(np.clip(dist / radius, 0, 1))
    gy, gx = np.gradient(height)
    out = np.zeros(mask.shape + (4,), np.uint8)
    top = len(ramp_) - 1
    for y in range(mask.shape[0]):
        for x in range(mask.shape[1]):
            if not mask[y, x]:
                continue
            n = np.array([-gx[y, x] * 2.2, -gy[y, x] * 2.2, 1.0])
            level = 1 + max(0.0, float(n / np.linalg.norm(n) @ light)) * (top - 1)
            if dist[y, x] <= 1.0:
                level = 1 if gx[y, x] * light[0] + gy[y, x] * light[1] < 0 else 0
            out[y, x, :3] = ramp_[int(np.clip(round(level), 0, top))]
            out[y, x, 3] = 255
    return out


def mask_from(rows):
    return np.array([[c != '.' for c in r] for r in rows])


TAG_MASK = [
    '................',
    '................',
    '................',
    '................',
    '................',
    '.....######.....',
    '....########....',
    '....########....',
    '....########....',
    '....########....',
    '....########....',
    '....########....',
    '....########....',
    '....########....',
    '.....######.....',
    '................',
]

TAG_DETAIL = [
    '................',
    '.......st.......',
    '......s..t......',
    '......s..t......',
    '.......st.......',
    '.......FE.......',
    '......FAAD......',
    '.......DC.......',
    '................',
    '.....0000.......',
    '................',
    '.....00000......',
    '................',
    '................',
    '................',
    '................',
]

SPYGLASS_MASK = [
    '................',
    '..........####..',
    '.........######.',
    '........#######.',
    '.......#######..',
    '......#######...',
    '.....######.....',
    '....######......',
    '...#####........',
    '..#####.........',
    '..####..........',
    '.####...........',
    '.###............',
    '..#.............',
    '................',
    '................',
]

SPYGLASS_DETAIL = [
    '................',
    '..........1111..',
    '.........1cdd1..',
    '.........cddb1..',
    '.......1.1bb1...',
    '......1.........',
    '.....1..........',
    '................',
    '...1............',
    '..1.............',
    '.mm.............',
    '.mnm............',
    '.mm.............',
    '................',
    '................',
    '................',
]

PEARL_MASK = [
    '................',
    '................',
    '.....######.....',
    '....########....',
    '...##########...',
    '..############..',
    '..############..',
    '..############..',
    '..############..',
    '..############..',
    '..############..',
    '...##########...',
    '....########....',
    '.....######.....',
    '................',
    '................',
]

PEARL_DETAIL = [
    '................',
    '................',
    '................',
    '.....ee.........',
    '....effe........',
    '....eff.........',
    '................',
    '..........cc....',
    '.........cc.....',
    '.....ccc........',
    '................',
    '................',
    '................',
    '................',
    '................',
    '................',
]


def embossed_icon(img, x, y, mask_rows, ramp_, detail_rows=None, detail_keys=None, radius=2.5):
    sprite = emboss(mask_from(mask_rows), ramp_, radius)
    if detail_rows:
        put(sprite, 0, 0, detail_rows, detail_keys)
    sprite = despeckle(sprite, [(0, 0, 16, 16)])
    m = sprite[..., 3] > 0
    img[y:y + 16, x:x + 16][m] = sprite[m]


EYE = [
    '................',
    '................',
    '.....122221.....',
    '....12344321....',
    '...1245665421...',
    '..124566a65421..',
    '..12566aa66521..',
    '..12567aa76521..',
    '..12456aa65421..',
    '..124566a65421..',
    '...1245665421...',
    '....12344321....',
    '.....122221.....',
    '................',
    '................',
    '................',
]

BUCKET_HINT = [
    '................',
    '................',
    '................',
    '...##########...',
    '...#........#...',
    '...##########...',
    '....#......#....',
    '....#......#....',
    '....#......#....',
    '.....#....#.....',
    '.....#....#.....',
    '.....######.....',
    '................',
    '................',
    '................',
    '................',
]

ARROW = [
    '..............#.......',
    '..............##......',
    '..............###.....',
    '..............####....',
    '..............#####...',
    '..............######..',
    '#####################.',
    '######################',
    '#####################.',
    '..............######..',
    '..............#####...',
    '..............####....',
    '..............###.....',
    '..............##......',
    '..............#.......',
]


def icon(img, x, y, rows, keys):
    sprite = np.zeros((16, 16, 4), np.uint8)
    put(sprite, 0, 0, [r.replace(' ', '.') for r in rows], keys)
    sprite = despeckle(sprite, [(0, 0, 16, 16)])
    mask = sprite[..., 3] > 0
    img[y:y + 16, x:x + 16][mask] = sprite[mask]


def mono(img, x, y, rows, colour):
    for dy, row in enumerate(rows):
        for dx, k in enumerate(row):
            if k == '#':
                px(img, x + dx, y + dy, colour)


def keys(ramp_, extra=None, extra_start='a', ink=None):
    k = {str(i + 1): c for i, c in enumerate(ramp_)}
    if extra:
        for i, c in enumerate(extra):
            k[chr(ord(extra_start) + i)] = c
    if ink:
        k['0'] = ink
    return k


def paper_icon(img, x, y, seal):
    k = keys(PAPER, seal, 'a', INK[1])
    k['7'] = PAPER[4]
    k['6'] = PAPER[3]
    k['5'] = PAPER[2]
    icon(img, x, y, PAPER_SLIP, k)


def tank_ticks(img, x, y, h=52):
    for i in range(0, h, 8):
        rect(img, x, y + i, x + (4 if i % 16 == 0 else 2), y + i + 1, SHADOW)


def xp_bar(img, x, y, w):
    well(img, x, y, w + 2, 9, (139, 139, 139))
    fill = np.zeros((7, w, 4), np.uint8)
    rows = [XP[5], XP[4], XP[3], XP[3], XP[2], XP[2], XP[1]]
    for r, c in enumerate(rows):
        fill[r, :, :3] = c
        fill[r, :, 3] = 255
    for col in range(w):
        if (col + 3) % 7 in (0, 1):
            fill[2, col, :3] = XP[4]
        if (col + 5) % 9 in (0, 1, 2):
            fill[4, col, :3] = XP[3]
    return fill


FLAT_PAPER = [
    '................',
    '...oooooooooo...',
    '...offffffffo...',
    '...offllllffo...',
    '...offffffffo...',
    '...ofllllllfo...',
    '...offffffffo...',
    '...offlllllfo...',
    '...offffffffo...',
    '...ofllllffoo...',
    '...offffffSSS...',
    '...offfffSssSS..',
    '...offfffSssSS..',
    '...ooooooSSSS...',
    '...........S....',
    '................',
]

FLAT_TAG = [
    '................',
    '.......gg.......',
    '......g..g......',
    '......g..g......',
    '.......gg.......',
    '.....oooooo.....',
    '....offhhffo....',
    '....offhhffo....',
    '....offffffo....',
    '....ollllffo....',
    '....offffffo....',
    '....olllllfo....',
    '....offffffo....',
    '....offffffo....',
    '.....oooooo.....',
    '................',
]

FLAT_SPYGLASS = [
    '................',
    '..........oooo..',
    '.........oLLLLo.',
    '........ofLLLLo.',
    '.......offoLLo..',
    '......offfo.o...',
    '.....offfo......',
    '....offfo.......',
    '...offfo........',
    '..ooffo.........',
    '.oddoo..........',
    '.oddo...........',
    '.ooo............',
    '................',
    '................',
    '................',
]

FLAT_LAMP = [
    '................',
    '.ooooooooooooooo',
    '.offffffoffffffo',
    '.offhhffoffhhffo',
    '.offhhffoffhhffo',
    '.offffffoffffffo',
    '.offffffoffffffo',
    '.ooooooooooooooo',
    '.offffffoffffffo',
    '.offhhffoffhhffo',
    '.offhhffoffhhffo',
    '.offffffoffffffo',
    '.offffffoffffffo',
    '.ooooooooooooooo',
    '................',
    '................',
]

FLAT_EYE = [
    '................',
    '................',
    '.....oooooo.....',
    '....offffffo....',
    '...offffffffo...',
    '..offffppffffo..',
    '..offffppffffo..',
    '..offffppffffo..',
    '..offffppffffo..',
    '..offffppffffo..',
    '...offffffffo...',
    '....offffffo....',
    '.....oooooo.....',
    '................',
    '................',
    '................',
]

FLAT_PEARL = [
    '................',
    '................',
    '.....oooooo.....',
    '....offffffo....',
    '...ohhffffffo...',
    '..ofhhffffffo...',
    '..offfffffffo...',
    '..offfffffffo...',
    '..offfffffffo...',
    '..offfffffffo...',
    '...offffffffo...',
    '....offffffo....',
    '.....oooooo.....',
    '................',
    '................',
    '................',
]


def flat(img, x, y, rows, colours):
    for dy, row in enumerate(rows):
        for dx, k in enumerate(row):
            if k in colours:
                px(img, x + dx, y + dy, colours[k])


PAPER_FLAT = {'o': (96, 96, 92), 'f': (232, 230, 222), 'l': (150, 150, 146)}
WHITELIST_COLOURS = PAPER_FLAT | {'S': (34, 104, 46), 's': (72, 168, 84)}
BLACKLIST_COLOURS = PAPER_FLAT | {'S': (110, 24, 24), 's': (196, 56, 50)}
TAG_COLOURS = {'o': (104, 72, 42), 'f': (204, 162, 108), 'l': (150, 112, 70), 'h': (60, 40, 24), 'g': (176, 176, 172)}
SPYGLASS_COLOURS = {'o': (112, 74, 26), 'f': (214, 162, 70), 'L': (126, 204, 210), 'd': (96, 62, 36)}
LAMP_LIT_COLOURS = {'o': (76, 42, 22), 'f': (236, 160, 72), 'h': (250, 214, 140)}
LAMP_UNLIT_COLOURS = {'o': (56, 30, 18), 'f': (112, 64, 34), 'h': (134, 80, 44)}
EYE_COLOURS = {'o': (28, 74, 58), 'f': (104, 172, 96), 'p': (18, 38, 30)}
PEARL_COLOURS = {'o': (12, 64, 54), 'f': (40, 132, 112), 'h': (124, 210, 190)}


def filtered_hopper():
    img = canvas()
    panel(img, 176, 133)
    for i in range(5):
        well(img, 11 + i * 18, 19)
    player_inventory(img, 50)
    toggle_buttons(img, 176, 0)
    flat(img, 176, 18, FLAT_PAPER, WHITELIST_COLOURS)
    flat(img, 192, 18, FLAT_PAPER, BLACKLIST_COLOURS)
    flat(img, 208, 18, FLAT_TAG, TAG_COLOURS)
    flat(img, 224, 18, FLAT_SPYGLASS, SPYGLASS_COLOURS)
    return img


def void_hopper():
    img = canvas()
    panel(img, 176, 133)
    for i in range(5):
        well(img, 31 + i * 18, 19)
    player_inventory(img, 50)
    toggle_buttons(img, 176, 0)
    flat(img, 176, 18, FLAT_PAPER, WHITELIST_COLOURS)
    flat(img, 192, 18, FLAT_PAPER, BLACKLIST_COLOURS)
    return img


def slider_hopper(indicator=None):
    img = canvas()
    panel(img, 176, 158)
    for i in range(5):
        well(img, 43 + i * 18, 19)
    player_inventory(img, 75)
    if indicator == 'lamp':
        flat(img, 176, 0, FLAT_LAMP, LAMP_LIT_COLOURS)
        flat(img, 192, 0, FLAT_LAMP, LAMP_UNLIT_COLOURS)
    return img


def linked_hopper():
    img = canvas()
    panel(img, 176, 158)
    for i in range(5):
        well(img, 43 + i * 18, 19)
    well(img, 25, 41)
    player_inventory(img, 75)
    flat(img, 176, 0, FLAT_EYE, EYE_COLOURS)
    flat(img, 192, 0, FLAT_PEARL, PEARL_COLOURS)
    return img


def xp_hopper():
    img = canvas()
    panel(img, 176, 80)
    img[100:107, 0:160] = xp_bar(img, 7, 30, 160)
    return img


def filtered_flopper():
    img = canvas()
    panel(img, 176, 166)
    well(img, 51, 34)
    mono(img, 52, 35, BUCKET_HINT, HINT)
    mono(img, 74, 35, ARROW, SLOT_FILL)
    well(img, 103, 16, 18, 54)
    tank_ticks(img, 104, 18)
    player_inventory(img, 83)
    tank_ticks(img, 176, 0)
    return img


def generate(out):
    gui = out / 'assets' / NAMESPACE / 'textures' / 'gui' / 'container'
    save(filtered_hopper(), gui / 'filtered_hopper.png')
    save(void_hopper(), gui / 'void_hopper.png')
    save(slider_hopper(), gui / 'vacuum_hopper.png')
    save(slider_hopper('lamp'), gui / 'pulse_hopper.png')
    save(linked_hopper(), gui / 'linked_hopper.png')
    save(xp_hopper(), gui / 'xp_hopper.png')
    save(filtered_flopper(), gui / 'filtered_flopper.png')


if __name__ == '__main__':
    generate(Path(sys.argv[1]) if len(sys.argv) > 1 else Path(__file__).parent / 'out')
