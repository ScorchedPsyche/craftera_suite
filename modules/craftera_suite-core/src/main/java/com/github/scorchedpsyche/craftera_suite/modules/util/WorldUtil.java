package com.github.scorchedpsyche.craftera_suite.modules.util;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class WorldUtil {
    public class DayNightCycle
    {
        public static final long SUNRISE = 23992;
        public static final long BEDS_CAN_BE_USED_START = 12541;
        public static final long BEDS_CAN_BE_USED_END = 23459;
        public static final long VILLAGER_WORK_START = 2000;
        public static final long VILLAGER_WORK_END = 9000;

        public class WEATHER_CLEAR
        {
            public static final long MONSTERS_SPAWN_START = 13187;
            public static final long MONSTERS_SPAWN_END = 23031;
        }

        public class WEATHER_RAIN
        {
            public static final long MONSTERS_SPAWN_START = 12969;
            public static final long MONSTERS_SPAWN_END = 13187;
        }
    }

    public static boolean timeSkipIfNotSunrise(World world)
    {
        long currentDaySunrise = 24000 * Math.floorDiv(world.getFullTime(), 24000);
//        long currentDayTime = world.getFullTime() - currentDaySunrise;
        long nextDaySunrise = DayNightCycle.BEDS_CAN_BE_USED_END + currentDaySunrise;
//        ConsoleUtil.debugMessage("nextWorldFullTime: " + world.getFullTime() + 100);
//        ConsoleUtil.debugMessage("currentDaySunrise: " + currentDaySunrise);
//        ConsoleUtil.debugMessage("nextDaySunrise: " + nextDaySunrise);

//        ConsoleUtil.debugMessage("attemptTimeSkipIntoTheNight");
        // Check if skip step won't go over the start of the day
        if( world.getFullTime() < nextDaySunrise)
        {
//            ConsoleUtil.debugMessage("world.getTime() + 100");
            world.setFullTime( world.getFullTime() + 100 );
            return true;
        } else {
            // Will overflow the day
//            ConsoleUtil.debugMessage("SUNRISE!!!!" );
            world.setFullTime(nextDaySunrise);
            return false;
        }
    }

    public static boolean isThereAtLeastOnePlayerSleepingInWorld(World world)
    {
        for( Player player : world.getPlayers() )
        {
            if ( player.isSleeping() )
            {
                return true;
            }
        }

        return false;
    }

    public static boolean isItNightTime(@NotNull World world)
    {
        return world.getTime() >= DayNightCycle.BEDS_CAN_BE_USED_START
                && world.getTime() <= DayNightCycle.BEDS_CAN_BE_USED_END;
    }

    public static boolean isThundering(@NotNull World world)
    {
        return world.isThundering();
    }

    public static boolean attemptToClearWeatherDependingOnChance(World world, int chance)
    {
        chance = MathUtil.limitBetween(chance, 0, 100);

        // TODO: DEBUG
        if ( chance == 100 )
        {
//            ConsoleUtil.debugMessage("weather CLEARED");
            world.setStorm(false);
            return true;
        } else if ( chance == 0 )
        {
//            ConsoleUtil.debugMessage("weather KEEP");
            return false;
        } else {
            if( new Random().nextInt(101) <= chance )
            {
//                ConsoleUtil.debugMessage("weather CLEARED");
                world.setStorm(false);
                return true;
            }
        }

//        ConsoleUtil.debugMessage("weather KEEP");
        return false;
    }

    public static void wakeAllPlayers(World world)
    {
        for( Player player : world.getPlayers() )
        {
            if( player.isSleeping() )
            {
                player.wakeup(true);
            }
        }
    }
}
