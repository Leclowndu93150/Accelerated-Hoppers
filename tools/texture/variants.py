import numpy as np

from hoppers import (BLACK, BOX_PLAIN, BREEZE, LAMP, LEATHER, PATINA, COPPER, CRYING, DEEPSLATE, DIAMOND, EMERALD, ENDER, ENDSTONE,
                     EXPOSED, GOLD, IRON, ITEM_BASE, OBSIDIAN, OXIDIZED, PEARL, PURPUR, QUARTZ, REDSTONE, SCULK,
                     SCULK_VEIN, SHULKER, STONE, TUFF, VALVE, WALL_PLATE, WATER, WEATHERED, XP, Hopper, palette, put,
                     quadrant)

BOX_RIVETED = [
    '22222221',
    '45555543',
    '46555563',
    '44555543',
    '34444332',
    '22222110',
]

PATINA_WALL_LIGHT = [
    '................',
    '................',
    '......cd........',
    '.....cdc.....dc.',
    '......cc....cc..',
]

PATINA_WALL_HEAVY = [
    '................',
    '..........cd....',
    '..dc.....cdc..d.',
    '.cdcc...cdc..cdc',
    '.ccc.....cc..cc.',
]

PATINA_BOX_LIGHT = [
    '........',
    '........',
    '........',
    '........',
    '.cd.....',
    '.cc.....',
]

PATINA_BOX_HEAVY = [
    '........',
    '.....d..',
    '....dc..',
    '.cd.....',
    'ccd..dc.',
    '.cc..cc.',
]

REMNANT_WALL = [
    '...ee.......ed..',
    '...dd...........',
    '................',
    '................',
    '................',
]

STACK_WALL = [
    'GHHHHHHHHHHHHHHG',
    'FE566156651665EF',
    'ED544154451445DE',
    'DC333133331333CD',
    'CDDDDDDDDDDDDDDC',
]

STACK_BOX = [
    '11111110',
    '45555542',
    'FHGGGGHE',
    'DFEEEEFC',
    '34444321',
    'CDDDDDDB',
]

STACK_SPOUT = [
    'FGGGFE',
    'EFFEDC',
    'DEEDCB',
    'CDDCBA',
]

STACK_FLOOR = quadrant([
    '........',
    '........',
    '........',
    '........',
    '....DEEE',
    '....E...',
    '....E...',
    '....E...',
])

STACK_UNDER = [
    '................',
    '................',
    '................',
    '................',
    '................',
    '.....EEEEED.....',
    '.....E....C.....',
    '.....E....C.....',
    '.....E....C.....',
    '.....E....C.....',
    '.....DCCCCB.....',
    '................',
    '................',
    '................',
    '................',
    '................',
]

VOID_WALL = [
    '4566555555556654',
    '2431222332221342',
    '2243122222213422',
    '1224311221134221',
    '1111111111111111',
]

VOID_BOX = [
    '11111110',
    '23332dd1',
    '23322cd1',
    '22222c11',
    '12211c10',
    '11111000',
]

VOID_SPOUT = [
    '4443',
    '3332',
    '2221',
    '2110',
]

VOID_FLOOR = quadrant([
    '00000000',
    '00000000',
    '00JJJJJJ',
    '00JJKKKK',
    '00JKKKKK',
    '00JKKKaa',
    '00JKKabc',
    '00JKKacd',
])

QUARTZ_WALL = [
    '6777777777777776',
    'FE567776677765EF',
    'ED566654456665DE',
    'DC444443344444CD',
    '3222222222222223',
]

QUARTZ_BOX = [
    '22222221',
    'E566654C',
    'E566654C',
    'E555554C',
    'D444443B',
    '22222110',
]

QUARTZ_SPOUT = [
    'FGGE',
    '5654',
    '4543',
    '3432',
]

SPLITTER_FLOOR = quadrant([
    '........',
    '........',
    '........',
    '...33333',
    '...30005',
    '...30005',
    '...30005',
    '...3555F',
])

TUFF_WALL = [
    '6777766776677776',
    '4555345555435554',
    '4544344444434454',
    '3333233333323333',
    '2111111111111112',
]

VACUUM_BOX = [
    '22222221',
    '45555543',
    '3abbbba2',
    '45555543',
    '3abbbba2',
    '22222110',
]

VACUUM_FLOOR = quadrant([
    '11111111',
    '11111111',
    '11222222',
    '112abbbb',
    '112bbccc',
    '112bcdee',
    '112bcefg',
    '112bcegh',
])

STONE_WALL = [
    '6777777777777776',
    '5666666666666665',
    '3444444444444443',
    '5555555555555555',
    '3333333333333333',
]

PULSE_FRONT = [
    '2BCCCCB1',
    '4CsttsC3',
    '4CrsstC3',
    '4CqrrsC3',
    '3BCCCCB2',
    '22222110',
]

PULSE_FLOOR_OVERLAY = quadrant([
    '........',
    '........',
    '........',
    '.....cde',
    '....c...',
    '...c....',
    '...d....',
    '...e....',
])

PULSE_RIM_OVERLAY = [
    '..cdeeedcccdeedc',
    '................',
    '................',
    '................',
    '................',
    '................',
    '................',
    '................',
    '................',
    '................',
    '................',
    '................',
    '................',
    '................',
    '................',
    '................',
]

PURPUR_WALL = [
    '6777777777777776',
    '5666665335666665',
    '4555554224555554',
    '3444443113444443',
    '2111111111111112',
]

UPWARD_BOX = [
    '22222221',
    '45555543',
    '455cd543',
    '45cdd443',
    '344dc332',
    '22222110',
]

ENDSTONE_WALL = [
    '6777777777777776',
    '5666654444566665',
    '5554441221444555',
    '4443331221333444',
    '2111111111111112',
]

LINKED_FRONT = [
    '22222221',
    '45bccb43',
    '4bdfadb3',
    '4cegadc3',
    '45bdcb43',
    '22222110',
]

LINKED_FLOOR = quadrant([
    '11111111',
    '11111111',
    '11JJJJJJ',
    '11JJKKKK',
    '11JKLLMM',
    '11JKLMNN',
    '11JKMNOO',
    '11JKMNOP',
])

SCULK_WALL = [
    '5666666666666665',
    '3443344444433443',
    '2bc2233333322cb2',
    '1ab1222222221ba1',
    '0111111111111110',
]

XP_FRONT = [
    '11111110',
    '2bccccb1',
    '2cqrsrc1',
    '2crsusc1',
    '2bccccb1',
    '11111000',
]

XP_BOX = [
    '11111110',
    '23332221',
    '2abc2221',
    '22abc221',
    '12222110',
    '11111000',
]

XP_FLOOR = quadrant([
    '00000000',
    '00000000',
    '00JJJJJJ',
    '00JKppqq',
    '00Jpqqrr',
    '00Jpqrss',
    '00Jqrstt',
    '00Jqrstu',
])

FLOPPER_WALL = [
    '6777777777777776',
    '3ee22ee22ee22ee3',
    '5565555555555655',
    '4454444444444544',
    '3222222222222223',
]

SIGHT_FRONT = [
    '22222221',
    '4ABBBBA3',
    '4BtttsB3',
    '4BrqqrB3',
    '3ABBBBA2',
    '22222110',
]

DRAIN_FLOOR = quadrant([
    '11111111',
    '11111111',
    '11222222',
    '11233333',
    '11234444',
    '11234500',
    '11234055',
    '11234050',
])

STRAINER_FLOOR = quadrant([
    '11111111',
    '11111111',
    '11111111',
    '111bcccc',
    '111cJJ2J',
    '111cJJ2J',
    '111c2222',
    '111cJJ2J',
])

STRAINER_FRONT = [
    '22222221',
    '4ceeeeb3',
    '4d2222b3',
    '4deeeeb3',
    '3cbbbba2',
    '22222110',
]

VALVE_FRONT = [
    '22222221',
    '45dffd43',
    '4dc55cd3',
    '4fEGGEf3',
    '4dc44cd2',
    '22dccd10',
]

NOZZLE_SPOUT = [
    'FGGE',
    'DEDC',
    'CDCB',
    'BCBA',
]


PATINA_ITEM_LIGHT = [
    '................',
    '................',
    '................',
    '................',
    '................',
    '................',
    '................',
    '................',
    '.........cc.....',
    '................',
    '................',
    '......dc........',
    '................',
    '................',
    '................',
    '................',
]

REMNANT_ITEM = [
    '................',
    '................',
    '.....ee.........',
    '................',
    '................',
    '................',
    '................',
    '...........dd...',
    '................',
    '................',
    '................',
    '................',
    '................',
    '................',
    '................',
    '................',
]

STACK_ITEM_OVERLAY = [
    '................',
    '................',
    '................',
    '................',
    '................',
    '................',
    '................',
    '................',
    '................',
    '................',
    '.....DEEEEC.....',
    '................',
    '................',
    '................',
    '................',
    '................',
]

VOID_ITEM_OVERLAY = [
    '................',
    '................',
    '................',
    '................',
    '................',
    '................',
    '................',
    '................',
    '........d.......',
    '........d.......',
    '........c.......',
    '................',
    '................',
    '................',
    '................',
    '................',
]

SPLITTER_ITEM_OVERLAY = [
    '................',
    '................',
    '................',
    '................',
    '................',
    '................',
    '................',
    '................',
    '................',
    '................',
    '................',
    '................',
    '......EGFD......',
    '......DFEC......',
    '.......DC.......',
    '................',
]

PULSE_ITEM_OVERLAY = [
    '................',
    '................',
    '................',
    '................',
    '................',
    '................',
    '................',
    '................',
    '................',
    '.......ss.......',
    '.......rr.......',
    '................',
    '................',
    '................',
    '................',
    '................',
]

LINKED_ITEM_OVERLAY = [
    '................',
    '................',
    '................',
    '................',
    '................',
    '................',
    '................',
    '................',
    '................',
    '.......ff.......',
    '.......dd.......',
    '................',
    '................',
    '................',
    '................',
    '................',
]

FLOPPER_ITEM_OVERLAY = [
    '................',
    '................',
    '................',
    '................',
    '................',
    '................',
    '................',
    '.....ee..ee.....',
    '................',
    '.......ss.......',
    '.......rr.......',
    '................',
    '................',
    '................',
    '................',
    '................',
]

STRAINER_ITEM_OVERLAY = [
    '................',
    '................',
    '................',
    '................',
    '................',
    '................',
    '................',
    '....cddddc......',
    '................',
    '................',
    '................',
    '................',
    '................',
    '................',
    '................',
    '................',
]

VALVE_ITEM_OVERLAY = [
    '................',
    '................',
    '................',
    '................',
    '................',
    '................',
    '................',
    '................',
    '................',
    '.......ff.......',
    '.......dd.......',
    '................',
    '................',
    '......DEDC......',
    '.......DC.......',
    '................',
]

UPWARD_ITEM = list(reversed(ITEM_BASE))


def copper_stage(name, body, accent=None, wall_overlay=None, box_overlay=None, item_overlay=None):
    wall = [list(r) for r in WALL_PLATE]
    box = [list(r) for r in BOX_RIVETED]
    for grid, overlay in ((wall, wall_overlay), (box, box_overlay)):
        if overlay:
            for y, row in enumerate(overlay):
                for x, k in enumerate(row):
                    if k != '.':
                        grid[y][x] = k
    return Hopper(name, body, accent=accent, wall=[''.join(r) for r in wall], box=[''.join(r) for r in box],
                  seams=(5,), item_overlay=item_overlay, grain=0.3, grain_cell=2.0, seed=1)


def merge(base, overlay):
    rows = [list(r) for r in base]
    for y, row in enumerate(overlay):
        for x, k in enumerate(row):
            if k != '.':
                rows[y][x] = k
    return [''.join(r) for r in rows]


HOPPERS = [
    copper_stage('copper_hopper', COPPER),
    copper_stage('exposed_copper_hopper', EXPOSED, PATINA, PATINA_WALL_LIGHT, PATINA_BOX_LIGHT, PATINA_ITEM_LIGHT),
    copper_stage('weathered_copper_hopper', WEATHERED, EXPOSED, REMNANT_WALL, None, REMNANT_ITEM),
    copper_stage('oxidized_copper_hopper', OXIDIZED, OXIDIZED, None, None, None),
    Hopper('stack_hopper', DEEPSLATE, trim=IRON, wall=STACK_WALL, rim=['DEEEEEEEEEEEEEED'], box=STACK_BOX,
           spout=STACK_SPOUT, wide_spout=True, rim_ramp=IRON, floor_overlay=STACK_FLOOR, underside_overlay=STACK_UNDER,
           item_overlay=STACK_ITEM_OVERLAY, item_rim_ramp=IRON, grain=0.25, grain_cell=1.5, seed=2),
    Hopper('void_hopper', OBSIDIAN, accent=CRYING, inside=BLACK, wall=VOID_WALL, rim=['3444444444444443'], box=VOID_BOX,
           spout=VOID_SPOUT, floor=VOID_FLOOR, item_overlay=VOID_ITEM_OVERLAY, hole=(CRYING, 0, 2)),
    Hopper('splitter_hopper', QUARTZ, trim=GOLD, wall=QUARTZ_WALL, rim=['4555555555555554'], box=QUARTZ_BOX,
           spout=QUARTZ_SPOUT, floor_overlay=SPLITTER_FLOOR, item_overlay=SPLITTER_ITEM_OVERLAY,
           model='splitter', grain=0.15, grain_cell=3.0, seed=3),
    Hopper('vacuum_hopper', TUFF, accent=BREEZE, wall=TUFF_WALL, box=VACUUM_BOX, floor=VACUUM_FLOOR,
           hole=(BREEZE, 2, 4), grain=0.35, grain_cell=1.5, seed=4),
    Hopper('pulse_hopper', STONE, trim=STONE, accent=REDSTONE, glow=LAMP, wall=STONE_WALL, box=BOX_RIVETED,
           front=PULSE_FRONT, floor_overlay=PULSE_FLOOR_OVERLAY, item_overlay=PULSE_ITEM_OVERLAY,
           grain=0.25, grain_cell=2.0, seed=5),
    Hopper('upward_hopper', PURPUR, accent=SHULKER, wall=PURPUR_WALL, box=BOX_RIVETED, item=UPWARD_ITEM,
           grain=0.2, grain_cell=2.0, seed=6),
    Hopper('linked_hopper', ENDSTONE, accent=ENDER, inside=PEARL, wall=ENDSTONE_WALL, box=BOX_RIVETED,
           front=LINKED_FRONT, floor=LINKED_FLOOR, item_overlay=LINKED_ITEM_OVERLAY, hole=(PEARL, 2, 4), grain=0.3, grain_cell=1.5, seed=7),
    Hopper('xp_hopper', SCULK, accent=SCULK_VEIN, glow=XP, inside=SCULK, wall=SCULK_WALL, box=XP_BOX, front=XP_FRONT,
           spout=VOID_SPOUT, floor=XP_FLOOR, hole=(XP, 2, 4)),
    Hopper('iron_flopper', IRON, accent=COPPER, trim=IRON, glow=WATER, wall=FLOPPER_WALL, box=BOX_RIVETED,
           front=SIGHT_FRONT, floor=DRAIN_FLOOR, item_overlay=FLOPPER_ITEM_OVERLAY, grain=0.25, grain_cell=2.0, seed=8),
    Hopper('golden_flopper', GOLD, accent=COPPER, trim=IRON, glow=WATER, wall=FLOPPER_WALL, box=BOX_RIVETED,
           front=SIGHT_FRONT, floor=DRAIN_FLOOR, item_overlay=FLOPPER_ITEM_OVERLAY, grain=0.25, grain_cell=2.0, seed=9),
    Hopper('diamond_flopper', DIAMOND, accent=COPPER, trim=IRON, glow=WATER, wall=FLOPPER_WALL, box=BOX_RIVETED,
           front=SIGHT_FRONT, floor=DRAIN_FLOOR, item_overlay=FLOPPER_ITEM_OVERLAY, grain=0.25, grain_cell=2.0, seed=10),
    Hopper('filtered_flopper', COPPER, accent=EMERALD, wall=WALL_PLATE, box=BOX_RIVETED, front=STRAINER_FRONT,
           floor=STRAINER_FLOOR, item_overlay=STRAINER_ITEM_OVERLAY, seams=(5,), grain=0.3, grain_cell=2.0, seed=11),
    Hopper('placing_flopper', COPPER, accent=VALVE, trim=IRON, wall=WALL_PLATE, box=BOX_RIVETED, front=VALVE_FRONT,
           spout=NOZZLE_SPOUT, floor=DRAIN_FLOOR, item_overlay=VALVE_ITEM_OVERLAY, seams=(5,), grain=0.3,
           grain_cell=2.0, seed=12),
]


LINKER = [
    '................',
    '....43....43....',
    '...53.JKKJ.53...',
    '...53JKMQKJ53...',
    '...53KMQPNK53...',
    '...53KNPOMK53...',
    '...53JLNMLJ53...',
    '...53.JKKJ.53...',
    '...54......32...',
    '....54444443....',
    '.....533332.....',
    '.......52.......',
    '......cddc......',
    '.......cb.......',
    '......4532......',
    '.......21.......',
]


def EXTRA_ITEMS():
    img = np.zeros((16, 16, 4), np.uint8)
    put(img, 0, 0, LINKER, palette(COPPER, accent=LEATHER, inside=PEARL))
    return {'hopper_linker': img}
