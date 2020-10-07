
# CraftEra Suite  
Plugin suite which aims to enhance Minecraft's gameplay experience while maintaining Vanilla feeling.   
  
All plugins are being developed to use as little resources from the server as possible and Asynchronous code will be used everywhere possible.  
  
## Modules (current/planned)  
Modules with a check-list are the ones that are either being worked on or that have some ideas planned. The ones without a check-list haven't yet been planned.  
  
Plugin names are a work-in-progress and might change in the future.  
  
### Achievements 
Custom achievements such as server-first accomplishments.  
  
### Baby Entities 
Prevents entities from becoming their adult variant.  
  
Features:  
 - [x] **Passive mob support;**  
 - [x] **Aggressive mob support (for those that have an infant variant);**
 - [x] **Custom Name Tag support: tool to transform entities into baby/adult;**  
 - [x] **Infinite Name Tag: prevent name tag from being expended;**  
 - [x] **Undo feature: convert back to their original state;**  
 - [x] **Prevent exploit: usage on natural babies to accelerate growth;**  
 - [x] **Prevent exploit: Scute drop on Turtle's growth;**
 - [x] **Prevent exploit: extra experience for converting aggressive mobs to their baby variant before killing.**
  
### Core 
Main library dependency needed for other plugins of this pack.  
  
Features:  
 - [x] **Console interface;**  
 - [x] **Player command interface;**  
 - [x] **Folder utility class;**  
 - [x] **String utility class.**  
  
### Commerce
Plugin that's mainly a helper for Commercial Districts for your server. Also synchronizes with a database so that you can attach your own tools to a web server or similar.
  
### Database Synchronization  
Plugin which acts as an interface to synchronize the suite data with a database. This data can be used for your website so that you can have website integration and synchronization with your server.  
  
Features:  
 - [x] **Database Interface;**  
 - [x] **SQLite integration;**
 - [ ] MySQL integration;
 - [ ] Plugin Integration: Baby Entities;
 - [ ] Plugin Integration: Core;
 - [x] **Plugin Integration: HUD;**
 - [ ] Plugin Integration: Spectator Mode;
 - [ ] Plugin Integration: Wandering Trades.
 
### Discord Integration  
Connects the suite with Discord so that you can create more interactivity between your server and Discord.  
  
### Events  
Add random organic or recurring events ("missions") to increase Vanilla gameplay options.  
  
### HUD  
Player customizable in-game HUD integrated with all other plugins from this suite.  
  
Features:  
 - [x] **Toggle on/off;**
 - [x] **Per player configuration;**
 - [x] **Player login auto-load of preferences;**
 - [x] **Coordinates;**  
 - [ ] Nether Portal coordinates;  
 - [ ] Tool durability support: displays main/off hand durability so that the player doesn't have to open inventory;  
 - [ ] Player orientation (N/S/E/W/etc);  
 - [ ] Server time;   
 - [ ] World time;  
 - [ ] World time with work hours' colorization; 
 - [ ] Server TPS (Tick Per Second);
 - [ ] Spectator plugin range;  
 - [ ] Commerce plugin integration.  
  
### Player Contribution  
Account for player contribution to the server so that rewards can be given.  
  
### Spectator Mode  
Spectator mode with range limit configurable by admin or with automatic view range limit mode.  
  
Features:  
 - [ ] Automatic range limit support (range = server's view distance);  
 - [ ] Manual range limit support;  
 - [ ] Player Armor Stand: to leave a "marker" for other to see;  
 - [ ] Recent movement prevention check: to avoid abuse if falling from deadly height;  
 - [ ] Recent damage prevention check: to avoid abuse if player is about to die if damaged recently;  
 - [ ] Waypoint ability: visual marker the player can make while in spectator mode. Useful for the player to have a heading of where they were looking at.  
  
### Streaming Support  
Enables interactivity between your member's audience and their streaming audience.  
  
### Wandering Trades  
Customizable Wandering Trader trades with both decoration and player heads support.  
  
Features:  
 - [x] **Main config file;**  
 - [x] **Whitelist support for member's player heads;**  
 - [x] **Player Head trade support;**  
 - [x] **Decoration Head trade support;**  
 - [x] **Item trade support;**  
 - [x] **Default values to reduce trade .json file clutter;**  
 - [x] **Custom trade lists;**  
 - [x] **Default trade lists to ship with plugin: for people who don't want to bother;**
 - [x] **Conversion script from .csv to .json files: useful for managing trade lists in Google Docs, for example.** 
  
  
### WorldGen  
100% new World Generation with much more interesting landscape and structures.  
  
Features:  
 - [ ] Litematica structure support: loads litematica files and uses them on WorldGen.