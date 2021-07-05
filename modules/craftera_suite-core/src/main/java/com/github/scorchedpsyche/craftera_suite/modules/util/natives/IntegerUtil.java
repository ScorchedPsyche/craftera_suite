package com.github.scorchedpsyche.craftera_suite.modules.util.natives;

import java.util.Scanner;

public class IntegerUtil {
    public static boolean isInteger(String s, int radix) {
        Scanner sc = new Scanner(s.trim());
        if(!sc.hasNextInt(radix)) return false;
        // we know it starts with a valid int, now make sure
        // there's nothing left!
        sc.nextInt(radix);
        return !sc.hasNext();
    }
}
