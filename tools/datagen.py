import json
import shutil
import sys
from pathlib import Path

NS = 'advanced_hoppers'
ROOT = Path(__file__).resolve().parent.parent
ART = ROOT / 'tools' / 'texture' / 'out' / 'assets' / NS

COPPER = ['copper_hopper', 'exposed_copper_hopper', 'weathered_copper_hopper', 'oxidized_copper_hopper']
WAXED = ['waxed_' + n for n in COPPER]
STANDARD = COPPER + ['stack_hopper', 'void_hopper', 'vacuum_hopper', 'pulse_hopper', 'linked_hopper', 'xp_hopper',
                     'iron_flopper', 'golden_flopper', 'diamond_flopper', 'filtered_flopper', 'placing_flopper']
NEW_BLOCKS = STANDARD + WAXED + ['splitter_hopper', 'upward_hopper']

NAMES = {
    'copper_hopper': 'Copper Hopper', 'exposed_copper_hopper': 'Exposed Copper Hopper',
    'weathered_copper_hopper': 'Weathered Copper Hopper', 'oxidized_copper_hopper': 'Oxidized Copper Hopper',
    'waxed_copper_hopper': 'Waxed Copper Hopper', 'waxed_exposed_copper_hopper': 'Waxed Exposed Copper Hopper',
    'waxed_weathered_copper_hopper': 'Waxed Weathered Copper Hopper', 'waxed_oxidized_copper_hopper': 'Waxed Oxidized Copper Hopper',
    'stack_hopper': 'Stack Hopper', 'void_hopper': 'Void Hopper', 'splitter_hopper': 'Splitter Hopper',
    'vacuum_hopper': 'Vacuum Hopper', 'pulse_hopper': 'Pulse Hopper', 'upward_hopper': 'Upward Hopper',
    'linked_hopper': 'Linked Hopper', 'xp_hopper': 'XP Hopper', 'iron_flopper': 'Iron Flopper',
    'golden_flopper': 'Golden Flopper', 'diamond_flopper': 'Diamond Flopper', 'filtered_flopper': 'Filtered Flopper',
    'placing_flopper': 'Placing Flopper',
}

EXTRA_LANG = {
    'item.advanced_hoppers.hopper_linker': 'Hopper Linker',
    'tooltip.advanced_hoppers.xp_hopper': 'Collects experience orbs',
    'tooltip.advanced_hoppers.linker': 'Use on a Linked Hopper, then on the hopper it should send to',
    'tooltip.advanced_hoppers.linker_source': 'Sending from %s, %s, %s',
    'message.advanced_hoppers.source_selected': 'Selected the hopper at %s, %s, %s. Now use it on the destination.',
    'message.advanced_hoppers.linked': 'Linked to %s, %s, %s',
    'message.advanced_hoppers.unlinked': 'Hopper unlinked',
    'message.advanced_hoppers.link_failed': 'Could not link: both must be Linked Hoppers in range in the same dimension',
    'gui.advanced_hoppers.whitelist': 'Whitelist',
    'gui.advanced_hoppers.blacklist': 'Blacklist',
    'gui.advanced_hoppers.match_tags': 'Match item tags',
    'gui.advanced_hoppers.match_components': 'Match exact components',
    'gui.advanced_hoppers.void_whitelist': 'Void listed items (everything when empty)',
    'gui.advanced_hoppers.void_blacklist': 'Void everything except listed items',
    'gui.advanced_hoppers.range': 'Range: %s blocks',
    'gui.advanced_hoppers.per_pulse': 'Per pulse: %s',
    'gui.advanced_hoppers.sending_to': 'Sending items to',
    'gui.advanced_hoppers.receiving_from': 'Receiving items from',
    'gui.advanced_hoppers.coordinates': '%s, %s, %s',
    'gui.advanced_hoppers.to': 'To: %s, %s, %s',
    'gui.advanced_hoppers.from': 'From: %s, %s, %s',
    'gui.advanced_hoppers.not_linked': 'Not linked',
    'gui.advanced_hoppers.use_linker': 'Use a Hopper Linker',
    'gui.advanced_hoppers.take_one': 'Take 1',
    'gui.advanced_hoppers.take_ten': 'Take 10',
    'gui.advanced_hoppers.take_all': 'Take All',
    'gui.advanced_hoppers.empty': 'Empty',
    'gui.advanced_hoppers.millibuckets': '%s mB',
    'gui.advanced_hoppers.tank': '%s / %s mB',
}

RECIPES = {
    'copper_hopper': (['C C', 'CHC', ' C '], {'C': 'minecraft:copper_ingot', 'H': 'advanced_hoppers:wooden_hopper'}),
    'stack_hopper': (['I I', 'IHI', ' I '], {'I': 'minecraft:iron_block', 'H': 'minecraft:hopper'}),
    'void_hopper': (['O O', 'OHO', ' E '], {'O': 'minecraft:obsidian', 'H': 'minecraft:hopper', 'E': 'minecraft:ender_pearl'}),
    'splitter_hopper': (['Q Q', 'QHQ', ' G '], {'Q': 'minecraft:quartz_block', 'H': 'minecraft:hopper', 'G': 'minecraft:gold_ingot'}),
    'vacuum_hopper': (['T T', 'THT', ' B '], {'T': 'minecraft:tuff_bricks', 'H': 'advanced_hoppers:iron_hopper', 'B': 'minecraft:breeze_rod'}),
    'pulse_hopper': (['S S', 'SHS', ' L '], {'S': 'minecraft:smooth_stone', 'H': 'minecraft:hopper', 'L': 'minecraft:redstone_lamp'}),
    'upward_hopper': (['P P', 'PHP', ' S '], {'P': 'minecraft:purpur_block', 'H': 'minecraft:hopper', 'S': 'minecraft:shulker_shell'}),
    'linked_hopper': (['E E', 'EHE', ' Y '], {'E': 'minecraft:end_stone_bricks', 'H': 'minecraft:hopper', 'Y': 'minecraft:ender_eye'}),
    'hopper_linker': ([' P ', 'C C', ' C '], {'P': 'minecraft:ender_pearl', 'C': 'minecraft:copper_ingot'}),
    'xp_hopper': (['S S', 'SHS', ' C '], {'S': 'minecraft:sculk', 'H': 'minecraft:hopper', 'C': 'minecraft:sculk_catalyst'}),
    'iron_flopper': (['I I', 'IFI', ' I '], {'I': 'minecraft:iron_ingot', 'F': 'advanced_hoppers:flopper'}),
    'golden_flopper': (['G G', 'GFG', ' G '], {'G': 'minecraft:gold_ingot', 'F': 'advanced_hoppers:iron_flopper'}),
    'diamond_flopper': (['D D', 'DFD', ' D '], {'D': 'minecraft:diamond', 'F': 'advanced_hoppers:golden_flopper'}),
    'filtered_flopper': (['WAW', 'WFW', ' W '], {'W': 'minecraft:emerald', 'A': 'minecraft:comparator', 'F': 'advanced_hoppers:flopper'}),
    'placing_flopper': (['F', 'D'], {'F': 'advanced_hoppers:flopper', 'D': 'minecraft:dispenser'}),
}


def write(path, data):
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(data, indent=2) + '\n', encoding='utf-8')


def ingredient(item, modern):
    if modern:
        return item
    return {'tag': item[1:]} if item.startswith('#') else {'item': item}


def blockstate(name, model):
    if name == 'splitter_hopper':
        return {'variants': {f'facing={f}': {'model': f'{NS}:block/{model}'} for f in ('down', 'east', 'north', 'south', 'west')}}
    if name == 'upward_hopper':
        side = {'north': 180, 'east': 270, 'south': 0, 'west': 90}
        variants = {'facing=down': {'model': f'{NS}:block/{model}', 'x': 180}}
        for facing, y in side.items():
            entry = {'model': f'{NS}:block/{model}_side', 'x': 180}
            if y:
                entry['y'] = y
            variants[f'facing={facing}'] = entry
        return {'variants': variants}
    variants = {'facing=down': {'model': f'{NS}:block/{model}'}}
    for facing, y in (('east', 90), ('north', 0), ('south', 180), ('west', 270)):
        entry = {'model': f'{NS}:block/{model}_side'}
        if y:
            entry['y'] = y
        variants[f'facing={facing}'] = entry
    return {'variants': variants}


def loot_table(name):
    return {
        'type': 'minecraft:block',
        'pools': [{
            'rolls': 1,
            'entries': [{
                'type': 'minecraft:item',
                'functions': [{'function': 'minecraft:copy_components', 'include': ['minecraft:custom_name'], 'source': 'block_entity'}],
                'name': f'{NS}:{name}',
            }],
            'conditions': [{'condition': 'minecraft:survives_explosion'}],
        }],
        'random_sequence': f'{NS}:blocks/{name}',
    }


def generate(resources, modern):
    assets = resources / 'assets' / NS
    data = resources / 'data' / NS
    for sub in ('textures/block', 'textures/item', 'textures/gui/container', 'models/block', 'models/item'):
        for src in (ART / sub).glob('*'):
            dst = assets / sub / src.name
            dst.parent.mkdir(parents=True, exist_ok=True)
            shutil.copyfile(src, dst)
    if modern:
        for src in (ART / 'items').glob('*.json'):
            shutil.copyfile(src, assets / 'items' / src.name)

    for name in NEW_BLOCKS:
        model = name[len('waxed_'):] if name.startswith('waxed_') else name
        write(assets / 'blockstates' / f'{name}.json', blockstate(name, model))
        if name.startswith('waxed_'):
            write(assets / 'models' / 'item' / f'{name}.json', {'parent': f'{NS}:item/{model}'})
            if modern:
                write(assets / 'items' / f'{name}.json', {'model': {'type': 'minecraft:model', 'model': f'{NS}:item/{model}'}})
        write(data / 'loot_table' / 'blocks' / f'{name}.json', loot_table(name))
    write(assets / 'blockstates' / 'flopper.json', blockstate('flopper', 'flopper'))

    lang_path = assets / 'lang' / 'en_us.json'
    lang = json.loads(lang_path.read_text(encoding='utf-8'))
    for name, title in NAMES.items():
        lang[f'block.{NS}.{name}'] = title
    lang.pop('gui.advanced_hoppers.linked_to', None)
    lang.update(EXTRA_LANG)
    write(lang_path, lang)

    for name, (pattern, keys) in RECIPES.items():
        write(data / 'recipe' / f'{name}.json', {
            'type': 'minecraft:crafting_shaped',
            'category': 'redstone',
            'pattern': pattern,
            'key': {k: ingredient(v, modern) for k, v in keys.items()},
            'result': {'id': f'{NS}:{name}'} if modern else {'id': f'{NS}:{name}', 'count': 1},
        })
    for plain, waxed in zip(COPPER, WAXED):
        write(data / 'recipe' / f'{waxed}_from_honeycomb.json', {
            'type': 'minecraft:crafting_shapeless',
            'category': 'redstone',
            'ingredients': [ingredient(f'{NS}:{plain}', modern), ingredient('minecraft:honeycomb', modern)],
            'result': {'id': f'{NS}:{waxed}'} if modern else {'id': f'{NS}:{waxed}', 'count': 1},
        })

    pickaxe_path = resources / 'data' / 'minecraft' / 'tags' / 'block' / 'mineable' / 'pickaxe.json'
    pickaxe = json.loads(pickaxe_path.read_text(encoding='utf-8'))
    for name in NEW_BLOCKS:
        if f'{NS}:{name}' not in pickaxe['values']:
            pickaxe['values'].append(f'{NS}:{name}')
    write(pickaxe_path, pickaxe)


def copper_data_maps(version):
    data_maps = ROOT / 'versions' / version / 'neoforge' / 'src' / 'main' / 'resources' / 'data' / 'neoforge' / 'data_maps' / 'block'
    write(data_maps / 'oxidizables.json', {'values': {
        f'{NS}:{COPPER[i]}': {'next_oxidation_stage': f'{NS}:{COPPER[i + 1]}'} for i in range(len(COPPER) - 1)}})
    write(data_maps / 'waxables.json', {'values': {
        f'{NS}:{plain}': {'waxed': f'{NS}:{waxed}'} for plain, waxed in zip(COPPER, WAXED)}})


if __name__ == '__main__':
    for version in sys.argv[1:]:
        generate(ROOT / 'versions' / version / 'common' / 'src' / 'main' / 'resources', not version.startswith('1.21.1'))
        copper_data_maps(version)
