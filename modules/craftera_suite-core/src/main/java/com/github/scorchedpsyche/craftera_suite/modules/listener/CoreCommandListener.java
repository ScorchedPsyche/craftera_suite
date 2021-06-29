//package com.github.scorchedpsyche.craftera_suite.modules.listener;
//
//import com.github.scorchedpsyche.craftera_suite.modules.events.modules.hud.HudCommandEvent;
//import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
//import com.github.scorchedpsyche.craftera_suite.modules.util.ConsoleUtil;
//import com.github.scorchedpsyche.craftera_suite.modules.util.MessageUtil;
//import com.github.scorchedpsyche.craftera_suite.modules.util.PlayerUtil;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.Listener;
//
//public class CoreCommandListener implements Listener
//{
//
//    @EventHandler
//    public void onCoreCommandEvent(HudCommandEvent event)
//    {
//        String[] args = event.getArgs();
//
//        // Check if any arguments exists. If this check fails something went very wrong
//        if( args != null && args.length > 0 )
//        {
//            switch( args[0].toLowerCase() )
//            {
//                case "item": // /ces hud config
//                    if( args.length > 1 )
//                    {
//                        switch( args[1] )
//                        {
//                            case "remove":
//                                if( args.length > 2  )
//                                {
//                                    switch( args[2] )
//                                    {
//                                    }
//                                } else {
//                                    // TODO: display help
//                                }
//                                break;
//
//                            case "display_mode":
//                                if( args.length > 2  )
//                                {
//                                } else {
//                                    // TODO: display help
//                                }
//                                break;
//
//                            default:
//                                // TODO: display help
//                                break;
//                        }
//                    } else {
//                        // /ces hud config HELP
//                        PlayerUtil.sendMessageWithPluginPrefix(
//                                event.getPlayer(), SuitePluginManager.Hud.Name.full, hudConfigCommandHelpMessage());
//                    }
//                    break;
//
//                case "toggle": // /ces hud toggle
//                    break;
//
//                default: // /ces hud HELP
//                    PlayerUtil.sendMessageWithPluginPrefix(
//                            event.getPlayer(), SuitePluginManager.Hud.Name.full, hudHelpMessage() );
//                    break;
//            }
//        } else {
//            // Code shouldn't have gotten here as the CustomCommandExecutor sends at least "toggle". Log the error
//            ConsoleUtil.logError("onHudCommandEvent received null or empty args. Report this to the developer");
//        }
//    }
//
//    /**
//     * '/ces hud' command full help page.
//     * @return Formatted string with full HUD help instructions
//     */
//    private String hudHelpMessage()
//    {
//        return  "Commands usage and description:" +
//                MessageUtil.newLine() +
//                MessageUtil.newLine() +
//                MessageUtil.formattedCommandWithDescription(
//                    "/ces hud",
//                    "enables/disables the HUD;\n") +
//                MessageUtil.formattedCommandWithDescription(
//                        "/ces hud help",
//                        "this help page.") +
//                MessageUtil.newLine() +
//                MessageUtil.newLine() +
//                hudConfigCommandHelpMessage()+
//                MessageUtil.newLine() +
//                MessageUtil.newLine() +
//                hudToggleCommandHelpMessage();
//    }
//
//    /**
//     * '/ces hud config' command full help page.
//     * @return Formatted string with full HUD Config help instructions
//     */
//    private String hudConfigCommandHelpMessage()
//    {
//        return  "\nSubcommands for " + MessageUtil.formattedCommand("/ces hud config ...") + ":" +
//                MessageUtil.newLine() +
//                MessageUtil.newLine() +
//
//                MessageUtil.formattedCommandWithDescription(
//                        "... colorize coordinates",
//                        "enables color for coordinates;\n") +
//                MessageUtil.formattedCommandWithDescription(
//                        "... colorize nether_portal_coordinates",
//                        "enables color for Nether Portal coordinates;\n") +
//                MessageUtil.formattedCommandWithDescription(
//                        "... colorize player_orientation",
//                        "enables color for player orientation;\n") +
//                MessageUtil.formattedCommandWithDescription(
//                        "... colorize server_tps",
//                        "20 = green, 19-15 = yellow and below 15 = red;\n") +
//                MessageUtil.formattedCommandWithDescription(
//                        "... colorize tool_durability",
//                        "below 50 = yellow and below 25 = red;\n") +
//                MessageUtil.formattedCommandWithDescription(
//                        "... colorize world_time",
//                        "green = villager work hours, yellow = bed can be used and red = light level " +
//                                "allows monster spawning;") +
//                MessageUtil.newLine() +
//                MessageUtil.newLine() +
//
//                MessageUtil.formattedCommandWithDescription(
//                        "... display_mode compact",
//                        "shows as little text as possible;\n") +
//                MessageUtil.formattedCommandWithDescription(
//                        "... display_mode extended",
//                        "shows a lot of text.");
//    }
//
//
//    /**
//     * '/ces hud toggle' command full help page.
//     * @return Formatted string with full HUD Toggle help instructions
//     */
//    private String hudToggleCommandHelpMessage()
//    {
//        return  "\nSubcommands for " + MessageUtil.formattedCommand("/ces hud toggle ...") + ":" +
//                MessageUtil.newLine() +
//                MessageUtil.newLine() +
//
//                MessageUtil.formattedCommandWithDescription(
//                        "...",
//                        "enables/disables the HUD;\n") +
//                MessageUtil.formattedCommandWithDescription(
//                        "... coordinates",
//                        "enables/disables player coordinates;\n") +
//                MessageUtil.formattedCommandWithDescription(
//                        "... nether_portal_coordinates",
//                        "enables/disables Nether Portal coordinates on the opposing dimension;\n") +
//                MessageUtil.formattedCommandWithDescription(
//                        "... player_orientation",
//                        "enables/disables player orientation (N/S/E/W,etc);\n") +
//                MessageUtil.formattedCommandWithDescription(
//                        "... server_time",
//                        "enables/disables server_time;\n") +
//                MessageUtil.formattedCommandWithDescription(
//                        "... server_tps",
//                        "enables/disables server Ticks Per Second (performance);\n") +
//                MessageUtil.formattedCommandWithDescription(
//                        "... tool_durability",
//                        "enables/disables main/off hand tool durability;\n") +
//                MessageUtil.formattedCommandWithDescription(
//                        "... world_time",
//                        "enables/disables world time in ticks.");
//    }
//}
