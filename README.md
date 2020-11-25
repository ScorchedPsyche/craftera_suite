
# CraftEra Suite  
Plugin suite which aims to enhance Minecraft's gameplay experience while maintaining Vanilla feeling.   
  
All plugins are being developed to use as little resources from the server as possible and Asynchronous code will be used everywhere possible.  
  
```diff
- NOTICE: this suite is in early stages of development; 
- NOTICE: usage in production at this moment is NOT recommended!
```

**Status reference:**

- ![#f03c15](https://via.placeholder.com/15/f03c15/000000?text=+) `Not yet implemented`
- ![#1589F0](https://via.placeholder.com/15/1589F0/000000?text=+) `Implemented`  
- ![#c5f015](https://via.placeholder.com/15/c5f015/000000?text=+) `Production ready`

## Modules (current/planned)  
Modules with a check-list are the ones that are either being worked on or that have some ideas planned. The ones without a check-list haven't yet been planned.  
  
Plugin names are a work-in-progress and might change in the future.  
  
### Achievements 
Achievements such as server-first and custom ones. 

![#f03c15](https://via.placeholder.com/15/f03c15/000000?text=+) `Vanilla player personal achievement accounting (0/80 advancements done);`  
![#f03c15](https://via.placeholder.com/15/f03c15/000000?text=+) `Vanilla server-first achievement accounting;`  
![#f03c15](https://via.placeholder.com/15/f03c15/000000?text=+) `Seasons module support: achievements resets on new Season and old ones are archived.`
  
### Baby Entities 
Prevents entities from becoming their adult variant.  
  
**Features:**  
![#1589F0](https://via.placeholder.com/15/1589F0/000000?text=+) `Passive mob support;`  
![#1589F0](https://via.placeholder.com/15/1589F0/000000?text=+) `Aggressive mob support (for those that have an infant variant);`  
![#1589F0](https://via.placeholder.com/15/1589F0/000000?text=+) `Custom Name Tag support: tool to transform entities into baby/adult;`  
![#1589F0](https://via.placeholder.com/15/1589F0/000000?text=+) `Infinite Name Tag: prevent name tag from being expended;`  
![#1589F0](https://via.placeholder.com/15/1589F0/000000?text=+) `Undo feature: convert back to their original state;`  
![#1589F0](https://via.placeholder.com/15/1589F0/000000?text=+) `Prevent exploit: usage on natural babies to accelerate growth;`  
![#1589F0](https://via.placeholder.com/15/1589F0/000000?text=+) `Prevent exploit: Scute drop on Turtle's growth;`  
![#1589F0](https://via.placeholder.com/15/1589F0/000000?text=+) `Prevent exploit: extra experience for converting aggressive mobs to their baby variant before killing.`
  
### Core 
Main library dependency needed for other plugins of this pack.  
  
**Features:**   
![#1589F0](https://via.placeholder.com/15/1589F0/000000?text=+) `Auto-restart if memory usage high (disabled by default): useful if server is experiencing memory leaks;`  
![#1589F0](https://via.placeholder.com/15/1589F0/000000?text=+) `Console interface;`  
![#1589F0](https://via.placeholder.com/15/1589F0/000000?text=+) `Player command interface;`  
![#1589F0](https://via.placeholder.com/15/1589F0/000000?text=+) `Folder utility class;`  
![#1589F0](https://via.placeholder.com/15/1589F0/000000?text=+) `String utility class.` 
  
### Commerce
Plugin that's mainly a helper for Commercial Districts for your server. Also synchronizes with a database so that you can attach your own tools to a web server or similar.
  
### Database Synchronization  
Plugin which acts as an interface to synchronize the suite data with a database. This data can be used for your website so that you can have website integration and synchronization with your server.  

**Features:**  
![#1589F0](https://via.placeholder.com/15/1589F0/000000?text=+) `Database Interface;`  
![#1589F0](https://via.placeholder.com/15/1589F0/000000?text=+) `SQLite integration;`  
![#f03c15](https://via.placeholder.com/15/f03c15/000000?text=+) `MySQL integration;`  
![#f03c15](https://via.placeholder.com/15/f03c15/000000?text=+) `Plugin Integration: Baby Entities;`  
![#f03c15](https://via.placeholder.com/15/f03c15/000000?text=+) `Plugin Integration: Core;`  
![#1589F0](https://via.placeholder.com/15/1589F0/000000?text=+) `Plugin Integration: HUD;`  
![#f03c15](https://via.placeholder.com/15/f03c15/000000?text=+) `Plugin Integration: Spectator Mode;`  
![#f03c15](https://via.placeholder.com/15/f03c15/000000?text=+) `Plugin Integration: Wandering Trades.`
 
### Discord Integration  
Connects the suite with Discord so that you can create more interactivity between your server and Discord.  
  
### Events  
Add random organic or recurring events ("missions") to increase Vanilla gameplay options.  
  
### HUD  
Player customizable in-game HUD integrated with all other plugins from this suite.  
  
**Features:**  
![#1589F0](https://via.placeholder.com/15/1589F0/000000?text=+) `Toggle on/off;`  
![#1589F0](https://via.placeholder.com/15/1589F0/000000?text=+) `Per player configuration;`  
![#1589F0](https://via.placeholder.com/15/1589F0/000000?text=+) `Per player display mode: compact or expanded;`  
![#1589F0](https://via.placeholder.com/15/1589F0/000000?text=+) `Player login auto-load of preferences;`  
![#1589F0](https://via.placeholder.com/15/1589F0/000000?text=+) `Coordinates;`  
![#1589F0](https://via.placeholder.com/15/1589F0/000000?text=+) `Nether Portal coordinates;`  
![#1589F0](https://via.placeholder.com/15/1589F0/000000?text=+) `Tool durability support: displays main/off hand durability so that the player doesn't have to open inventory;`  
![#1589F0](https://via.placeholder.com/15/1589F0/000000?text=+) `Tool durability colorization: below 100 health it's colored yellow and bellow 50 red;`  
![#1589F0](https://via.placeholder.com/15/1589F0/000000?text=+) `Player orientation (N/S/E/W/etc);`  
![#1589F0](https://via.placeholder.com/15/1589F0/000000?text=+) `Server time;`   
![#1589F0](https://via.placeholder.com/15/1589F0/000000?text=+) `World time;`  
![#1589F0](https://via.placeholder.com/15/1589F0/000000?text=+) `World time colorization: colors work hours, monster spawning and bed usage;`  
![#1589F0](https://via.placeholder.com/15/1589F0/000000?text=+) `Server TPS (Tick Per Second);`  
![#1589F0](https://via.placeholder.com/15/1589F0/000000?text=+) `Server TPS colorization: green when 20, yellow 19-15 and red below 15;`  
![#f03c15](https://via.placeholder.com/15/f03c15/000000?text=+) `Spectator plugin range;`  
![#f03c15](https://via.placeholder.com/15/f03c15/000000?text=+) `Commerce plugin integration.`
  
### Player Contribution  
Account for player contribution to the server so that rewards can be given.  
    
### Seasons  
Enables Season support for the CraftEra Suite. Useful when you reset your server often and play season-styled. 
  
**Features:**  
![#f03c15](https://via.placeholder.com/15/f03c15/000000?text=+) `Database integration: once enabled mold database tables to support seasons;`
 
### Spectator Mode  
Spectator mode with range limit configurable by admin or with automatic view range limit mode.  
  
**Features:**  
![#f03c15](https://via.placeholder.com/15/f03c15/000000?text=+) `Automatic range limit support (range = server's view distance);`  
![#f03c15](https://via.placeholder.com/15/f03c15/000000?text=+) `Manual range limit support;`  
![#f03c15](https://via.placeholder.com/15/f03c15/000000?text=+) `Player Armor Stand: to leave a "marker" for other to see;`  
![#f03c15](https://via.placeholder.com/15/f03c15/000000?text=+) `Recent movement prevention check: to avoid abuse if falling from deadly height;`  
![#f03c15](https://via.placeholder.com/15/f03c15/000000?text=+) `Recent damage prevention check: to avoid abuse if player is about to die if damaged recently;`  
![#f03c15](https://via.placeholder.com/15/f03c15/000000?text=+) `Waypoint ability: visual marker the player can make while in spectator mode. Useful for the player to have a heading of where they were looking at.`
  
### Streaming Support  
Enables interactivity between your member's audience and their streaming audience.  
  
### Wandering Trades  
Customizable Wandering Trader trades with both decoration and player heads support.  
  
**Features:**  
![#1589F0](https://via.placeholder.com/15/1589F0/000000?text=+) `Main config file;`  
![#1589F0](https://via.placeholder.com/15/1589F0/000000?text=+) `Whitelist support for member's player heads;`  
![#1589F0](https://via.placeholder.com/15/1589F0/000000?text=+) `Player Head trade support;`  
![#1589F0](https://via.placeholder.com/15/1589F0/000000?text=+) `Decoration Head trade support;`  
![#1589F0](https://via.placeholder.com/15/1589F0/000000?text=+) `Item trade support;`  
![#1589F0](https://via.placeholder.com/15/1589F0/000000?text=+) `Default values to reduce trade .json file clutter;`  
![#1589F0](https://via.placeholder.com/15/1589F0/000000?text=+) `Custom trade lists;`  
![#1589F0](https://via.placeholder.com/15/1589F0/000000?text=+) `Default trade lists to ship with plugin: for people who don't want to bother;`  
![#1589F0](https://via.placeholder.com/15/1589F0/000000?text=+) `Conversion script from .csv to .json files: useful for managing trade lists in Google Docs, for example.` 
  
  
### WorldGen  
100% new World Generation with much more interesting landscape and structures.  
  
**Features:**  
![#f03c15](https://via.placeholder.com/15/f03c15/000000?text=+) `Litematica structure support: loads litematica files and uses them on WorldGen.`