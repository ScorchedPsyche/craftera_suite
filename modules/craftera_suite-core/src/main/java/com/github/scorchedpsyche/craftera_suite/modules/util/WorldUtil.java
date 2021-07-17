package com.github.scorchedpsyche.craftera_suite.modules.util;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class WorldUtil {
    public class DayNightCycle
    {
        public static final long SUNRISE = 23992;
        public static final long VILLAGER_WORK_START = 2000;
        public static final long VILLAGER_WORK_END = 9000;

        public class WEATHER_CLEAR
        {
            public static final long BEDS_CAN_BE_USED_START = 12542;
            public static final long BEDS_CAN_BE_USED_END = 23460;
            public static final long MONSTERS_SPAWN_START = 13187;
            public static final long MONSTERS_SPAWN_END = 23031;
        }

        public class WEATHER_RAIN
        {
            public static final long BEDS_CAN_BE_USED_START = 12010;
            public static final long BEDS_CAN_BE_USED_END = 23992;
            public static final long MONSTERS_SPAWN_START = 12969;
            public static final long MONSTERS_SPAWN_END = 13187;
        }
    }

    public static boolean isWorldAtBedsCanBeUsedEndTime(World world)
    {
        if( world.isClearWeather() )
        {
            return world.getTime() == DayNightCycle.WEATHER_CLEAR.BEDS_CAN_BE_USED_END;
        }

        return world.getTime() == DayNightCycle.WEATHER_RAIN.BEDS_CAN_BE_USED_END;

//        long currentDay1TickAfterBedsCannotBeUsed;
//        long nextDay1TickAfterBedsCannotBeUsed;
//
//        if( world.isClearWeather() )
//        {
//            currentDay1TickAfterBedsCannotBeUsed = 24000 * Math.floorDiv(world.getFullTime(), 24000);
//            nextDay1TickAfterBedsCannotBeUsed =
//                DayNightCycle.WEATHER_CLEAR.BEDS_CAN_BE_USED_END + currentDay1TickAfterBedsCannotBeUsed +1;
//        } else {
//            currentDay1TickAfterBedsCannotBeUsed = 24000 * Math.floorDiv(world.getFullTime(), 24000);
//            nextDay1TickAfterBedsCannotBeUsed =
//                DayNightCycle.WEATHER_RAIN.BEDS_CAN_BE_USED_END + currentDay1TickAfterBedsCannotBeUsed +1;
//        }
//
//        return world.getFullTime() == nextDay1TickAfterBedsCannotBeUsed;
    }

    public static boolean skipNightUntilBedsCannotBeUsed(World world)
    {
        long currentDaySunrise = 24000 * Math.floorDiv(world.getFullTime(), 24000);
//        long currentDayTime = world.getFullTime() - currentDaySunrise;
        long nextDaySunrise;
        if( world.isClearWeather() )
        {
            nextDaySunrise = DayNightCycle.WEATHER_CLEAR.BEDS_CAN_BE_USED_END + currentDaySunrise;
        } else {
            nextDaySunrise = DayNightCycle.WEATHER_RAIN.BEDS_CAN_BE_USED_END + currentDaySunrise;
        }
//        ConsoleUtil.debugMessage("nextWorldFullTime: " + world.getFullTime() + 100);
//        ConsoleUtil.debugMessage("currentDaySunrise: " + currentDaySunrise);
//        ConsoleUtil.debugMessage("nextDaySunrise: " + nextDaySunrise);

//        ConsoleUtil.debugMessage("attemptTimeSkipIntoTheNight");
        // Check if skip step won't go over the start of the day
        if( world.getFullTime() + 100 < nextDaySunrise)
        {
//            ConsoleUtil.debugMessage("world.getFullTime() + 100: " + (world.getFullTime() + 100));
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

    public static boolean canBedsBeUsed(@NotNull World world)
    {
        if( world.isClearWeather() )
        {
            return world.getTime() >= DayNightCycle.WEATHER_CLEAR.BEDS_CAN_BE_USED_START
                    && world.getTime() < DayNightCycle.WEATHER_CLEAR.BEDS_CAN_BE_USED_END;
        }

        return world.getTime() >= DayNightCycle.WEATHER_RAIN.BEDS_CAN_BE_USED_START
                && world.getTime() < DayNightCycle.WEATHER_RAIN.BEDS_CAN_BE_USED_END;
    }

    public static boolean isThundering(@NotNull World world)
    {
        return world.isThundering();
    }

    public static boolean attemptToClearWeatherDependingOnChance(World world, int chance)
    {
        chance = MathUtil.limitBetween(chance, 0, 100);

//        ConsoleUtil.debugMessage("chance: " + chance);
        if ( chance == 100 )
        {
//            ConsoleUtil.debugMessage("weather CLEARED 1");
            world.setStorm(false);
            return true;
        } else if ( chance == 0 )
        {
//            ConsoleUtil.debugMessage("weather KEEP");
            return false;
        } else {
            if( new Random().nextInt(101) <= chance )
            {
//                ConsoleUtil.debugMessage("weather CLEARED 2");
                world.setStorm(false);
                return true;
            }
        }

//        ConsoleUtil.debugMessage("weather KEEP 2");
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
