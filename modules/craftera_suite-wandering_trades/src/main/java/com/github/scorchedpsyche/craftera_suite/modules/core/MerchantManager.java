package com.github.scorchedpsyche.craftera_suite.modules.core;

import com.github.scorchedpsyche.craftera_suite.modules.CraftEraSuiteWanderingTrades;
import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.models.TradeEntryModel;
import com.github.scorchedpsyche.craftera_suite.modules.utils.ConsoleUtils;
import com.github.scorchedpsyche.craftera_suite.modules.utils.PlayerHeadUtils;
import com.github.scorchedpsyche.craftera_suite.modules.utils.natives.StringUtils;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.*;

public class MerchantManager
{
    private final List<MerchantRecipe> decorationHeads = new ArrayList<>();
    private final List<MerchantRecipe> items = new ArrayList<>();
    private final List<MerchantRecipe> playerHeads = new ArrayList<>();
    private final List<MerchantRecipe> playerHeadsWhitelisted = new ArrayList<>();

    private List<MerchantRecipe> trades = new ArrayList<>();

    public MerchantManager()
    {
        setup();
    }

    /***
     * Setup for the Merchant Manager class which preloads user files and creates static lists from them.
     */
    private void setup()
    {
        // Loops through trade files
        for (TradeEntryModel trade : CraftEraSuiteWanderingTrades.tradeListManager.Trades.offers)
        {
            // Check if recipe is valid
            if (isRecipeValid(trade))
            {
                // Checks type of item
                if (!trade.getMinecraftId().equalsIgnoreCase("player_head"))
                {
                    // Other Items
                    loadItemRecipe(trade);
                } else
                {
                    // Heads (Decoration and Player)
                    if (trade.getOwnerId() == null && trade.getTexture() != null)
                    {
                        // Decoration Heads
                        loadDecorationHeadRecipe(trade);
                    } else
                    {
                        // Player Head
//                        addPlayerHeadFromFile( trade );
//                        LoggerCore.Log( "Loaded player heads trades" );
                    }
                }
            }
        }
        ConsoleUtils.logSuccess(SuitePluginManager.WanderingTrades.Name.full,
                                "Loaded item trades");
        ConsoleUtils.logSuccess(SuitePluginManager.WanderingTrades.Name.full,
                                "Loaded decoration heads trades");

        // WHITELISTED Player's Heads synchronization
        if (CraftEraSuiteWanderingTrades.config.getBoolean("whitelist.enable_synchronization")) // TO DO
        {
            // Check if whitelist is empty
            if (Bukkit.hasWhitelist() && Bukkit.getWhitelistedPlayers().size() > 0)
            {
                // Check if config.yml
                if ( isConfigYmlMissingWhitelistConfig() )
                {
                    // Not empty
                    for (OfflinePlayer offlinePlayer : Bukkit.getWhitelistedPlayers())
                    {
                        loadWhitelistedPlayerHeadRecipe(offlinePlayer);
                    }
                    ConsoleUtils.logSuccess(SuitePluginManager.WanderingTrades.Name.full,
                                            "Loaded whitelisted player heads trades");
                }
            } else
            {
                // Empty whitelist
                ConsoleUtils.logError(SuitePluginManager.WanderingTrades.Name.full,
                                        "Whitelist synchronization is ON (check config.yml) but the whitelist is " +
                                       "empty or doesn't exists");
            }
        }
    }

    /***
     * Loads item recipe from the files.
     * @param trade Recipe to be loaded and processed
     */
    private void loadItemRecipe(TradeEntryModel trade)
    {
        // Checks if there are any typos or missing configs on trade files
        if (isRecipeValid(trade))
        {
            // Valid recipe
            Material material = Material.matchMaterial(trade.getMinecraftId());
            Material ingredient1 = Material.matchMaterial(trade.getPriceItem1());

            // Check if ingredient is valid
            if (trade.getPriceItem2() != null)
            {
                Material ingredient2 = Material.matchMaterial(trade.getPriceItem1());

                // Ingredient2 valid - add trade with both ingredients
                items.add(createRecipe(material, trade.getAmount(), trade.getUsesMax(), ingredient1,
                                       trade.getPrice1(), ingredient2, trade.getPrice2()));
            } else
            {
                // No second ingredient - add trade
                items.add(createRecipe(material, trade.getAmount(), trade.getUsesMax(), ingredient1,
                                       trade.getPrice1()));
            }
        }
    }

    /***
     * Loads decoration head recipe from the files.
     * @param trade Recipe to be loaded and processed
     */
    private void loadDecorationHeadRecipe(TradeEntryModel trade)
    {
        ItemStack decorationHead = createDecorationHead(trade);

        // Checks if there are any typos or missing configs on trade files
        if ( isRecipeValid(trade) )
        {
            // Valid recipe
            Material material = Material.matchMaterial(trade.getMinecraftId());
            Material ingredient1 = Material.matchMaterial(trade.getPriceItem1());

            // Check if ingredient is valid
            if (trade.getPriceItem2() != null)
            {
                Material ingredient2 = Material.matchMaterial(trade.getPriceItem2());

                // Ingredient2 valid - add trade with both ingredients
                decorationHeads.add(createRecipe(decorationHead, trade.getUsesMax(), ingredient1, trade.getPrice1(),
                                                 ingredient2, trade.getPrice2()));
            } else
            {
                // No second ingredient - add trade
                decorationHeads.add(createRecipe(decorationHead, trade.getUsesMax(), ingredient1, trade.getPrice1()));
            }
        }
    }

    /***
     * Creates a decoration head Item Stack.
     * @param trade Trade entry from .json file
     * @return Item stack
     */
    private ItemStack createDecorationHead(TradeEntryModel trade)
    {
        ItemStack decorationHead = new ItemStack(Material.PLAYER_HEAD, trade.getAmount());

        SkullMeta decorationHeadMeta = (SkullMeta) decorationHead.getItemMeta();

        UUID randomUUID = UUID.randomUUID();

        GameProfile profile = new GameProfile(randomUUID, null);
        profile.getProperties().put("textures", new Property("textures", trade.getTexture()));

        Field profileField;

        try
        {
            assert decorationHeadMeta != null;
            decorationHeadMeta.setOwningPlayer( Bukkit.getOfflinePlayer( randomUUID ) ); // ScorchedPsyche UUID
            profileField = decorationHeadMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(decorationHeadMeta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e1)
        {
            e1.printStackTrace();
        }

        decorationHeadMeta.setDisplayName( trade.getName() );
        decorationHead.setItemMeta(decorationHeadMeta);

        return decorationHead;
    }

    private void loadPlayerHeadRecipe(TradeEntryModel trade)
    {
        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) playerHead.getItemMeta();
        assert meta != null;
        meta.setOwningPlayer( Bukkit.getPlayerExact(trade.getOwnerId()) );
        playerHead.setItemMeta(meta);

        MerchantRecipe recipe = new MerchantRecipe(
                playerHead,
                1
        );

        recipe.addIngredient(new ItemStack(
                Material.DIAMOND,
                1));

        playerHeads.add(recipe);
    }

    /***
     * Checks the whitelist section for the `config.yml` file.
     * @return True if whitelist section on `config.yml` is valid
     */
    private boolean isConfigYmlMissingWhitelistConfig()
    {
        boolean isValid = true;

        // Check if there's a whitelist section
        if( !CraftEraSuiteWanderingTrades.config.contains( "whitelist" ) )
        {
            ConsoleUtils.logError(SuitePluginManager.WanderingTrades.Name.full,
                                    "Missing `whitelist` section from `config.yml`");
            isValid = false;
        } else {
            // Whitelist section exists check others

            // Check whitelist config: enable_synchronization
            if( !CraftEraSuiteWanderingTrades.config.contains( "whitelist.enable_synchronization" ) &&
                    !CraftEraSuiteWanderingTrades.config.isBoolean( "whitelist.enable_synchronization" ) )
            {
                ConsoleUtils.logError(SuitePluginManager.WanderingTrades.Name.full,
                                      "Whitelist 'enable_synchronization' config either doesn't exists or it's not a boolean " +
                                       "(true/false). Check `config.yml`");
                isValid = false;
            }

            // Check whitelist config: number_of_player_head_offers
            if( !CraftEraSuiteWanderingTrades.config.contains( "whitelist.number_of_player_head_offers" ) &&
                    !CraftEraSuiteWanderingTrades.config.isInt( "whitelist.number_of_player_head_offers" ) )
            {
                ConsoleUtils.logError(SuitePluginManager.WanderingTrades.Name.full,
                                      "Whitelist 'number_of_player_head_offers' config either doesn't exists or it's " +
                                       "not an integer. Check `config.yml`");
                isValid = false;
            }

            // Check whitelist config: heads_rewarded_per_trade
            if( !CraftEraSuiteWanderingTrades.config.contains( "whitelist.heads_rewarded_per_trade" ) &&
                    !CraftEraSuiteWanderingTrades.config.isInt( "whitelist.heads_rewarded_per_trade" ) )
            {
                ConsoleUtils.logError(SuitePluginManager.WanderingTrades.Name.full,
                                      "Whitelist 'heads_rewarded_per_trade' config either doesn't exists or it's " +
                                       "not an integer. Check `config.yml`");
                isValid = false;
            }

            // Check whitelist config: maximum_number_of_trades
            if( !CraftEraSuiteWanderingTrades.config.contains( "whitelist.maximum_number_of_trades" ) &&
                    !CraftEraSuiteWanderingTrades.config.isInt( "whitelist.maximum_number_of_trades" ) )
            {
                ConsoleUtils.logError(SuitePluginManager.WanderingTrades.Name.full,
                                      "Whitelist 'maximum_number_of_trades' config either doesn't exists or it's " +
                                       "not an integer. Check `config.yml`");
                isValid = false;
            }

            // Check whitelist config: experience_rewarded_for_each_trade
            if( !CraftEraSuiteWanderingTrades.config.contains( "whitelist.experience_rewarded_for_each_trade" ) &&
                    !CraftEraSuiteWanderingTrades.config.isBoolean( "whitelist.experience_rewarded_for_each_trade" ) )
            {
                ConsoleUtils.logError(SuitePluginManager.WanderingTrades.Name.full,
                                      "Whitelist 'experience_rewarded_for_each_trade' config either doesn't exists or it's not a boolean " +
                                       "(true/false). Check `config.yml`");
                isValid = false;
            }

            // Check if there's a whitelist.price section
            if( !CraftEraSuiteWanderingTrades.config.contains( "whitelist.price" ) )
            {
                // whitelist.price config doesn't exists
                ConsoleUtils.logError(SuitePluginManager.WanderingTrades.Name.full,
                                      "Missing `whitelist.price` section from `config.yml`");
                isValid = false;
            } else
            {
                // whitelist.price section exists

                // Check if there's a whitelist.price.item1 section
                if (!CraftEraSuiteWanderingTrades.config.contains("whitelist.price.item1"))
                {
                    // whitelist.price.item1 config doesn't exists
                    ConsoleUtils.logError(SuitePluginManager.WanderingTrades.Name.full,
                                          "Missing `whitelist.price.item1` section from `config.yml`");
                    isValid = false;
                } else
                {
                    // whitelist.price.item1 section exists

                    // Check whitelist config: whitelist.price.item1.minecraft_id
                    if( !CraftEraSuiteWanderingTrades.config.contains( "whitelist.price.item1.minecraft_id" ) &&
                            !CraftEraSuiteWanderingTrades.config.isString( "whitelist.price.item1.minecraft_id" ) )
                    {
                        ConsoleUtils.logError(SuitePluginManager.WanderingTrades.Name.full,
                                              "Whitelist 'price.item1.minecraft_id' config either doesn't exists or it's " +
                                               "not a string. Check `config.yml`");
                        isValid = false;
                    } else {
                        // Valid whitelist.price.item1.minecraft_id

                        // Check if it's a valid material
                        if( Material.matchMaterial(
                                CraftEraSuiteWanderingTrades.config.getString( "whitelist.price.item1.minecraft_id" ) ) == null )
                        {
                            ConsoleUtils.logError(SuitePluginManager.WanderingTrades.Name.full,
                                                  "Whitelist 'price.item1.minecraft_id' (" +
                                            CraftEraSuiteWanderingTrades.config.getString( "whitelist.price.item1.minecraft_id" ) +
                                           ") is not a valid Minecraft ID. Check `config.yml`");
                            isValid = false;
                        }
                    }

                    // Check whitelist config: whitelist.price.item1.quantity
                    if( !CraftEraSuiteWanderingTrades.config.contains( "whitelist.price.item1.quantity" ) &&
                            !CraftEraSuiteWanderingTrades.config.isInt( "whitelist.price.item1.quantity" ) )
                    {
                        ConsoleUtils.logError(SuitePluginManager.WanderingTrades.Name.full,
                                              "Whitelist 'whitelist.price.item1.quantity' config either doesn't exists or it's " +
                                               "not an integer. Check `config.yml`");
                        isValid = false;
                    }
                }

                // Check if there's a whitelist.price.item2 section
                if ( CraftEraSuiteWanderingTrades.config.contains("whitelist.price.item2") )
                {
                    // whitelist.price.item2 section exists

                    // Check whitelist config: whitelist.price.item2.minecraft_id
                    if( !CraftEraSuiteWanderingTrades.config.contains( "whitelist.price.item2.minecraft_id" ) &&
                            !CraftEraSuiteWanderingTrades.config.isString( "whitelist.price.item2.minecraft_id" ) )
                    {
                        ConsoleUtils.logError(SuitePluginManager.WanderingTrades.Name.full,
                                              "Whitelist 'price.item2.minecraft_id' (" +
                                       CraftEraSuiteWanderingTrades.config.getString( "whitelist.price.item2.minecraft_id" ) +
                                       ") is not a valid Minecraft ID. Check `config.yml`");
                        isValid = false;
                    } else {
                        // Valid whitelist.price.item2.minecraft_id

                        // Check if it's a valid material
                        if( Material.matchMaterial(
                                CraftEraSuiteWanderingTrades.config.getString( "whitelist.price.item2.minecraft_id" ) ) == null )
                        {
                            ConsoleUtils.logError(SuitePluginManager.WanderingTrades.Name.full,
                                    "Whitelist 'price.item2.minecraft_id' is not a valid Minecraft ID. " +
                                                   "Check `config.yml`");
                            isValid = false;
                        }
                    }

                    // Check whitelist config: whitelist.price.item2.quantity
                    if( !CraftEraSuiteWanderingTrades.config.contains( "whitelist.whitelist.price.item2.quantity" ) &&
                            !CraftEraSuiteWanderingTrades.config.isInt( "whitelist.whitelist.price.item2.quantity" ) )
                    {
                        ConsoleUtils.logError(SuitePluginManager.WanderingTrades.Name.full,
                                    "Whitelist 'whitelist.price.item2.quantity' config either doesn't " +
                                               "exists or it's not an integer. Check `config.yml`");
                        isValid = false;
                    }
                }
            }
        }

        return isValid;
    }

    /***
     * Add to merchant trade list Player Head from whitelisted player.
     * @param offlinePlayer The owner of the Player Head
     */
    private void loadWhitelistedPlayerHeadRecipe(OfflinePlayer offlinePlayer)
    {
        ItemStack playerHead = PlayerHeadUtils.playerHeadItemStackFromOfflinePlayer(
                CraftEraSuiteWanderingTrades.config.getInt( "whitelist.price.item1.quantity" ), offlinePlayer);

        Material ingredient1 = Material.matchMaterial( Objects.requireNonNull(
                        CraftEraSuiteWanderingTrades.config.getString("whitelist.price.item1.minecraft_id")));

        if( !CraftEraSuiteWanderingTrades.config.contains( "whitelist.price.item2" ) )
        {
            playerHeadsWhitelisted.add( createRecipe(
                    playerHead,
                    CraftEraSuiteWanderingTrades.config.getInt( "whitelist.maximum_number_of_trades" ),
                    ingredient1,
                    CraftEraSuiteWanderingTrades.config.getInt( "whitelist.price.item1.quantity" )
                ) );
        } else {
            Material ingredient2 =
                    Material.matchMaterial( Objects.requireNonNull(
                            CraftEraSuiteWanderingTrades.config.getString("whitelist.price.item2.minecraft_id")));

            playerHeadsWhitelisted.add( createRecipe(
                    playerHead,
                    CraftEraSuiteWanderingTrades.config.getInt( "whitelist.maximum_number_of_trades" ),
                    ingredient1,
                    CraftEraSuiteWanderingTrades.config.getInt( "whitelist.price.item1.quantity" ),
                    ingredient2,
                    CraftEraSuiteWanderingTrades.config.getInt( "whitelist.price.item2.quantity" )
                                                    ) );
        }
    }

    /***
     * Removes Wandering Trader default trades.
     * @param merchant The Wandering Trader to remove the default trade from
     */
    public static void removeDefaultTrades(  WanderingTrader merchant )
    {
        merchant.setRecipes( new ArrayList<>() );
    }

    /***
     * Checks if the recipe from the trade list is valid.
     * @param trade Recipe from the trade list
     * @return True if recipe is valid.
     */
    private boolean isRecipeValid( TradeEntryModel trade )
    {
        boolean isValid = true;
        Material material;

        // Check if material was provided
        if( trade.getMinecraftId() == null )
        {
            // Material wasn't provided
            isValid = false;
            ConsoleUtils.logError(SuitePluginManager.WanderingTrades.Name.full,
                                    "Missing 'minecraft_id' for a trade offer" );
        } else {
            material = Material.matchMaterial( trade.getMinecraftId() );

            // Check if material is valid
            if(material == null)
            {
                // Material invalid
                isValid = false;
                ConsoleUtils.logError(SuitePluginManager.WanderingTrades.Name.full,
                                    "Invalid 'minecraft_id': " + trade.getMinecraftId() + ". Item was not added" );
            }

            // Check if decoration head either texture or owner
            if( trade.getMinecraftId().equalsIgnoreCase("player_head") &&
                    (   StringUtils.isNullOrEmpty(trade.getOwnerId()) &&
                        StringUtils.isNullOrEmpty(trade.getTexture()) ) )
            {
                // Missing both
                isValid = false;
                ConsoleUtils.logError(SuitePluginManager.WanderingTrades.Name.full,
                                    "On: " + trade.getMinecraftId() + ". You are 'getOwnerId' and 'getTexture'. " +
                                        "Item was not added" );
            }
        }

        // Check if ingredient 1 was provided
        if( trade.getPriceItem1() == null )
        {
            // Ingredient 1 wasn't provided
            isValid = false;
            ConsoleUtils.logError(SuitePluginManager.WanderingTrades.Name.full,
                                    "Missing 'price_item1' for: " + trade.getMinecraftId() );
        } else {
            material = Material.matchMaterial( trade.getPriceItem1() );

            // Check if Ingredient 1 is valid
            if(material == null)
            {
                // Ingredient 1 invalid
                isValid = false;
                ConsoleUtils.logError(SuitePluginManager.WanderingTrades.Name.full,
                                    "Invalid 'price_item1' for: " + trade.getPriceItem1() + ". Item was not added" );
            }
        }

        // Check if Ingredient 2 or it's price are missing when one is provided
        if( (trade.getPrice2() == null && trade.getPriceItem2() != null) || (trade.getPrice2() != null && trade.getPriceItem2() == null) )
        {
            // One is missing
            isValid = false;
            ConsoleUtils.logError(SuitePluginManager.WanderingTrades.Name.full,
                                  "On: " + trade.getMinecraftId() + ". You are missing either 'price_item2' or " +
                                          "'price2'. " +
                                    "Item was not added" );
        }

        return isValid;
    }

    /***
     * Creates a recipe with only 1 ingredient.
     * @param itemStack Head to create the offer with
     * @param maxUses Maximum number of different transactions for this trade for ingredient 1
     * @param ingredient1 Item the player must give to the trader
     * @param amountIngredient1 How many items must the player give to the trader for ingredient 2
     * @return Returns a 1 ingredient recipe.
     */
    private MerchantRecipe createRecipe( ItemStack itemStack, int maxUses, Material ingredient1,
                                         int amountIngredient1 )
    {
        return createRecipe(itemStack, maxUses, ingredient1, amountIngredient1, Material.AIR, 1);
    }

    /***
     * Creates a recipe with 2 ingredients.
     * @param itemStack Head to create the offer with
     * @param maxUses Maximum number of different transactions for this trade for ingredient 1
     * @param ingredient1 Item the player must give to the trader
     * @param amountIngredient1 How many items must the player give to the trader for ingredient 2
     * @param ingredient2 Item the player must give to the trader
     * @param amountIngredient2 How many items must the player give to the trader for ingredient 2
     * @return Returns a 2 ingredient recipe.
     */
    private MerchantRecipe createRecipe( ItemStack itemStack, int maxUses, Material ingredient1, int amountIngredient1,
                                         Material ingredient2, int amountIngredient2 )
    {
        MerchantRecipe recipe = new MerchantRecipe( itemStack, maxUses );

        recipe.addIngredient( new ItemStack( ingredient1, amountIngredient1 ) );
        recipe.addIngredient( new ItemStack( ingredient2, amountIngredient2 ) );

        return recipe;
    }

    /***
     * Creates a recipe with only 1 ingredient.
     * @param material Material sold
     * @param amount Quantity given to the player
     * @param maxUses Maximum number of different transactions for this trade for ingredient 1
     * @param ingredient1 Item the player must give to the trader
     * @param amountIngredient1 How many items must the player give to the trader for ingredient 2
     * @return Returns a 1 ingredient recipe.
     */
    private MerchantRecipe createRecipe( Material material, int amount, int maxUses, Material ingredient1,
                                         int amountIngredient1 )
    {
        return createRecipe(material, amount, maxUses, ingredient1, amountIngredient1, Material.AIR, 1);
    }

    /***
     *Creates a recipe with 2 ingredients.
     * @param material Material sold
     * @param amount Quantity given to the player
     * @param maxUses Maximum number of different transactions for this trade
     * @param ingredient1 Item 1 the player must give to the trader
     * @param amountIngredient1 How many items must the player give to the trader for ingredient 1
     * @param ingredient2 Item 2 the player must give to the trader
     * @param amountIngredient2 How many items must the player give to the trader for ingredient 2
     * @return Returns a 1 ingredient recipe.
     */
    private MerchantRecipe createRecipe( Material material, int amount, int maxUses, Material ingredient1,
                                         int amountIngredient1, Material ingredient2, int amountIngredient2 )
    {
        MerchantRecipe recipe = new MerchantRecipe( new ItemStack( material, amount), maxUses );

        recipe.addIngredient( new ItemStack( ingredient1, amountIngredient1 ) );
        recipe.addIngredient( new ItemStack( ingredient2, amountIngredient2 ) );

        return recipe;
    }

    /***
     * Sets the trades for the Wandering Trader.
     * @param merchant Target Wandering Trader to have his offers configured
     */
    public void setMerchantTrades(WanderingTrader merchant)
    {
        // Empties trade list to avoid duplicated
        trades = new ArrayList<>();

        if ( !playerHeadsWhitelisted.isEmpty() ) { addWhitelistedPlayersHeadsToOffers(); }
        if ( !items.isEmpty() ) { addItemsToOffers(); }
        if ( !playerHeads.isEmpty() ) { trades.addAll(playerHeads); }
        if ( !decorationHeads.isEmpty() ) { addDecorationHeadsToOffers(); }

        merchant.setRecipes(trades);
    }

    /***
     * Adds whitelisted player's head to Wandering Trader's offers.
     */
    private void addWhitelistedPlayersHeadsToOffers()
    {
        if( !playerHeadsWhitelisted.isEmpty() )
        {
            Collections.shuffle( playerHeadsWhitelisted );

            int nbrOfPlayers = playerHeadsWhitelisted.size();

            if( nbrOfPlayers > CraftEraSuiteWanderingTrades.config.getInt("whitelist.number_of_player_head_offers") )
            {
                nbrOfPlayers = CraftEraSuiteWanderingTrades.config.getInt("whitelist.number_of_player_head_offers");
            }

            for (int i = 0; i < nbrOfPlayers; i++)
            {
                trades.add( playerHeadsWhitelisted.get(i) );
            }
        }
    }

    /***
     * Adds items to Wandering Trader's offers.
     */
    private void addItemsToOffers()
    {
        if( !items.isEmpty() )
        {
            Collections.shuffle( items );

            int nbrOfTradesToAdd = items.size();

            if( nbrOfTradesToAdd > CraftEraSuiteWanderingTrades.config.getInt("maximum_unique_trade_offers.items") )
            {
                nbrOfTradesToAdd = CraftEraSuiteWanderingTrades.config.getInt("maximum_unique_trade_offers.items");
            }

            for (int i = 0; i < nbrOfTradesToAdd; i++)
            {
                trades.add( items.get(i) );
            }
        }
    }

    /***
     * Adds decoration heads to Wandering Trader's offers.
     */
    private void addDecorationHeadsToOffers()
    {
        if( !decorationHeads.isEmpty() )
        {
            Collections.shuffle( decorationHeads );

            int nbrOfTradesToAdd = decorationHeads.size();

            if( nbrOfTradesToAdd > CraftEraSuiteWanderingTrades.config.getInt("maximum_unique_trade_offers.decoration_heads") )
            {
                nbrOfTradesToAdd = CraftEraSuiteWanderingTrades.config.getInt("maximum_unique_trade_offers.decoration_heads");
            }

            for (int i = 0; i < nbrOfTradesToAdd; i++)
            {
                trades.add( decorationHeads.get(i) );
            }
        }
    }
}
