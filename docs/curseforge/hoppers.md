<h1 style="text-align:center">Advanced Hoppers Mod</h1><p style="text-align:center">Take your item and fluid transfer to the next level with <strong>Advanced Hoppers</strong>, a mod designed to enhance automation in Minecraft. With new tiers of hoppers, a fluid-transfer flopper, and customizable configurations, you have more control than ever!</p><p style="text-align:center">Support is Available in my <a href="https://discord.gg/m4EHeRjfZ9">Discord</a>!</p>

***

## Hoppers

All numbers below are the default values and can be changed in the config. Speeds are in game ticks: 20 ticks is one second, and a vanilla hopper moves 1 item every 8 ticks.

### Tiered hoppers

*   ![Wooden Hopper](https://raw.githubusercontent.com/Leclowndu93150/Accelerated-Hoppers/main/versions/26.1.2/common/src/main/resources/assets/advanced_hoppers/textures/item/wooden_hopper.png) **Wooden Hopper**: a cheap early-game hopper. Moves 1 item every 20 ticks.
*   ![Iron Hopper](https://raw.githubusercontent.com/Leclowndu93150/Accelerated-Hoppers/main/versions/26.1.2/common/src/main/resources/assets/advanced_hoppers/textures/item/iron_hopper.png) **Iron Hopper**: moves 1 item every 6 ticks.
*   ![Golden Hopper](https://raw.githubusercontent.com/Leclowndu93150/Accelerated-Hoppers/main/versions/26.1.2/common/src/main/resources/assets/advanced_hoppers/textures/item/golden_hopper.png) **Golden Hopper**: moves 1 item every 4 ticks.
*   ![Diamond Hopper](https://raw.githubusercontent.com/Leclowndu93150/Accelerated-Hoppers/main/versions/26.1.2/common/src/main/resources/assets/advanced_hoppers/textures/item/diamond_hopper.png) **Diamond Hopper**: moves 1 item every 2 ticks.
*   ![Netherite Hopper](https://raw.githubusercontent.com/Leclowndu93150/Accelerated-Hoppers/main/versions/26.1.2/common/src/main/resources/assets/advanced_hoppers/textures/item/netherite_hopper.png) **Netherite Hopper**: moves 1 item every tick.

### Copper hoppers

*   ![Copper Hopper](https://raw.githubusercontent.com/Leclowndu93150/Accelerated-Hoppers/main/versions/26.1.2/common/src/main/resources/assets/advanced_hoppers/textures/item/copper_hopper.png) **Copper Hopper**: moves 1 item every 10 ticks. Like copper blocks, it slowly oxidizes over time and gets slower at each stage.
*   ![Exposed Copper Hopper](https://raw.githubusercontent.com/Leclowndu93150/Accelerated-Hoppers/main/versions/26.1.2/common/src/main/resources/assets/advanced_hoppers/textures/item/exposed_copper_hopper.png) **Exposed Copper Hopper**: moves 1 item every 12 ticks.
*   ![Weathered Copper Hopper](https://raw.githubusercontent.com/Leclowndu93150/Accelerated-Hoppers/main/versions/26.1.2/common/src/main/resources/assets/advanced_hoppers/textures/item/weathered_copper_hopper.png) **Weathered Copper Hopper**: moves 1 item every 14 ticks.
*   ![Oxidized Copper Hopper](https://raw.githubusercontent.com/Leclowndu93150/Accelerated-Hoppers/main/versions/26.1.2/common/src/main/resources/assets/advanced_hoppers/textures/item/oxidized_copper_hopper.png) **Oxidized Copper Hopper**: moves 1 item every 16 ticks.

Use honeycomb on a copper hopper to wax it and stop it from aging. Use an axe to remove the wax or scrape off one stage of oxidation. Items inside are kept when it changes stage.

### Special hoppers

*   ![Filtered Hopper](https://raw.githubusercontent.com/Leclowndu93150/Accelerated-Hoppers/main/versions/26.1.2/common/src/main/resources/assets/advanced_hoppers/textures/item/filtered_hopper.png) **Filtered Hopper**: moves 1 item every 2 ticks, but only the items you allow. It has 5 filter slots and three toggles: whitelist or blacklist, match any item that shares a tag with a listed item, and match exact data (enchantments, names and so on). With an empty filter it lets everything through.
*   ![Stack Hopper](https://raw.githubusercontent.com/Leclowndu93150/Accelerated-Hoppers/main/versions/26.1.2/common/src/main/resources/assets/advanced_hoppers/textures/item/stack_hopper.png) **Stack Hopper**: moves a whole stack, up to 64 items, every 8 ticks.
*   ![Void Hopper](https://raw.githubusercontent.com/Leclowndu93150/Accelerated-Hoppers/main/versions/26.1.2/common/src/main/resources/assets/advanced_hoppers/textures/item/void_hopper.png) **Void Hopper**: destroys items instead of storing them. This covers items dropped on it, items in a container above it, and items other hoppers push into it. Its 5 filter slots decide what gets destroyed: either only the listed items (everything when the filter is empty) or everything except the listed items.
*   ![Splitter Hopper](https://raw.githubusercontent.com/Leclowndu93150/Accelerated-Hoppers/main/versions/26.1.2/common/src/main/resources/assets/advanced_hoppers/textures/item/splitter_hopper.png) **Splitter Hopper**: hands items out in turn to every container around it, below and on all four sides. Moves 1 item every 4 ticks.
*   ![Vacuum Hopper](https://raw.githubusercontent.com/Leclowndu93150/Accelerated-Hoppers/main/versions/26.1.2/common/src/main/resources/assets/advanced_hoppers/textures/item/vacuum_hopper.png) **Vacuum Hopper**: pulls in dropped items from around it. The range is set in its screen, from 1 to 8 blocks.
*   ![Pulse Hopper](https://raw.githubusercontent.com/Leclowndu93150/Accelerated-Hoppers/main/versions/26.1.2/common/src/main/resources/assets/advanced_hoppers/textures/item/pulse_hopper.png) **Pulse Hopper**: collects items like a normal hopper but only sends them on when it receives a redstone pulse. The amount sent per pulse is set in its screen, from 1 to 64 items.
*   ![Upward Hopper](https://raw.githubusercontent.com/Leclowndu93150/Accelerated-Hoppers/main/versions/26.1.2/common/src/main/resources/assets/advanced_hoppers/textures/item/upward_hopper.png) **Upward Hopper**: works upside down. It pulls items from the container below it and pushes them up, or to the side it faces.
*   ![Linked Hopper](https://raw.githubusercontent.com/Leclowndu93150/Accelerated-Hoppers/main/versions/26.1.2/common/src/main/resources/assets/advanced_hoppers/textures/item/linked_hopper.png) **Linked Hopper**: sends items wirelessly to another Linked Hopper up to 64 blocks away in the same dimension. Use a Hopper Linker ![Hopper Linker](https://raw.githubusercontent.com/Leclowndu93150/Accelerated-Hoppers/main/versions/26.1.2/common/src/main/resources/assets/advanced_hoppers/textures/item/hopper_linker.png) on the sending hopper, then on the receiving one. Sneak and use the linker on a hopper to unlink it. Its screen shows where it sends items to and where it receives them from. When it is not linked it works like a normal hopper.
*   ![XP Hopper](https://raw.githubusercontent.com/Leclowndu93150/Accelerated-Hoppers/main/versions/26.1.2/common/src/main/resources/assets/advanced_hoppers/textures/item/xp_hopper.png) **XP Hopper**: collects experience orbs that land on it and stores the experience. Open it to take 1 level, 10 levels, or everything stored. A redstone signal stops it from collecting.

### Floppers (fluid hoppers)

Floppers move fluids instead of items. They drain source blocks and tanks above them and fill the tank they point into. You can also use a bucket on them to fill or empty them.

*   ![Flopper](https://raw.githubusercontent.com/Leclowndu93150/Accelerated-Hoppers/main/versions/26.1.2/common/src/main/resources/assets/advanced_hoppers/textures/item/flopper.png) **Flopper**: holds 8,000 mB and moves 50 mB per tick.
*   ![Iron Flopper](https://raw.githubusercontent.com/Leclowndu93150/Accelerated-Hoppers/main/versions/26.1.2/common/src/main/resources/assets/advanced_hoppers/textures/item/iron_flopper.png) **Iron Flopper**: holds 16,000 mB and moves 100 mB per tick.
*   ![Golden Flopper](https://raw.githubusercontent.com/Leclowndu93150/Accelerated-Hoppers/main/versions/26.1.2/common/src/main/resources/assets/advanced_hoppers/textures/item/golden_flopper.png) **Golden Flopper**: holds 32,000 mB and moves 250 mB per tick.
*   ![Diamond Flopper](https://raw.githubusercontent.com/Leclowndu93150/Accelerated-Hoppers/main/versions/26.1.2/common/src/main/resources/assets/advanced_hoppers/textures/item/diamond_flopper.png) **Diamond Flopper**: holds 64,000 mB and moves 1,000 mB per tick.
*   ![Filtered Flopper](https://raw.githubusercontent.com/Leclowndu93150/Accelerated-Hoppers/main/versions/26.1.2/common/src/main/resources/assets/advanced_hoppers/textures/item/filtered_flopper.png) **Filtered Flopper**: only accepts one fluid. Set the filter by putting a filled bucket or other fluid container in the filter slot of its screen, and click the slot with an empty hand to clear it. Holds 8,000 mB and moves 50 mB per tick.
*   ![Placing Flopper](https://raw.githubusercontent.com/Leclowndu93150/Accelerated-Hoppers/main/versions/26.1.2/common/src/main/resources/assets/advanced_hoppers/textures/item/placing_flopper.png) **Placing Flopper**: places its fluid into the world as a source block where its spout points, one every 8 ticks while it holds at least 1,000 mB. Holds 8,000 mB.

 

If you'd like to support me and the development of my mods, I recommend trying out BisectHosting. Use code "project8gbderam" to get 25% off your first month of a gaming server for new customers. They offer 24/7 support and lightning-fast response times, ensuring you're in great hands and providing exceptional assistance for all your gaming needs.

[https://www.bisecthosting.com/project8gbderam](https://www.curseforge.com/linkout?remoteUrl=https://www.bisecthosting.com/project8gbderam)

![](https://www.bisecthosting.com/partners/custom-banners/54bb107c-f9fc-4f32-8515-fb4e3d56c124.png)
