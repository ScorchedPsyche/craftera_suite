
# CraftEra Suite
Plugin suite which aims to enhance Minecraft's gameplay experience while maintaining Vanilla feeling. 

All plugins are being developed to use as little resources from the server as possible and Asynchronous code will be used everywhere possible.

## Modules (current/planned)

### Achievements 
Custom achievements such as server-first accomplishments.

### Baby Entities 
Prevents entities from becoming their adult variant.

Features:
 - [x] Passive mob support;
 - [x] Prevent Scute drop on Turtle's growth: prevent abuse;
 - [ ] Agressive mob support (for those that have an infant variant);

### Core 
Main library dependency needed for other plugins of this pack.

Features:
 - [x] Console interface;
 - [x] Player command interface;
 - [x] Folder utility class;
 - [x] String utility class.

### Database Synchronization
Plugin which acts as an interface to synchronize the suite data with a database. This data can be used for your website so that you can have website integration and synchronization with your server.

### Discord Integration
Connects the suite with Discord so that you can create more interactivity between your server and Discord.

### Events
Add random organic or recurring events ("missions") to increase Vanilla gameplay options.

### HUD
Player customizable in-game HUD integrated with all other plugins from this suite.

Features:
 - [x] Coordinates;
 - [ ] Nether Portal coordinates;
 - [ ] Player orientation (N/S/E/W/etc);
 - [ ] Server time;
 - [ ] Server time with work hours colorization;
 - [ ] World Time;
 - [ ] Spectator plugin range;
 - [ ] Commerce plugin integration.

### Player Contribution
Account for player contribution for the server so that rewards can be awarded.

### Spectator Mode
Spectator mode with range limit configurable by admin.

Features:
 - [ ] Player Armor Stand: to leave a "marker" for other to see;
 - [ ] Configurable range limit support;
 - [ ] Recent movement prevention check: to avoid abuse if falling from deadly height;
 - [ ] Recent damage prevention check: to avoid abuse if player is about to die if damaged recently;

### Streaming Support
Enables interactivity between your member's audience and their streaming audience.

### Wandering Trades
Customizable Wandering Trader trades with both decoration and player heads support.

Features:
 - [x] Main config file;
 - [x] Whitelist support for member's player heads;
 - [x] Player Head trade support;
 - [x] Decoration Head trade support;
 - [x] Item trade support;
 - [x] Default values to reduce trade .json file clutter;
 - [x] Custom trade lists;
 - [x] Default trade lists to ship with plugin: for people who don't want to bother;
 - [x] Conversion script from .csv to .json files: useful for managing trade lists in Google Docs, for example.


### WorldGen
100% new World Generation with a much more interesting landscape and structures.

Features:
 - [ ] Litematica structure support: loads litematica files and uses them on WorldGen.

