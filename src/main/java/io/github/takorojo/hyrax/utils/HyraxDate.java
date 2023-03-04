package io.github.takorojo.hyrax.utils;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

public class HyraxDate extends BigInteger {
    /**
     * Translates the decimal String representation of a BigInteger into a BigInteger.  The String representation consists
     * of an optional minus or plus sign followed by a sequence of one or more decimal digits.  The character-to-digit
     * mapping is provided by {@link Character#digit(char, int) Character.digit}.  The String may not contain any extraneous
     * characters (whitespace, for example).
     *
     * @param val decimal String representation of BigInteger.
     *
     * @throws NumberFormatException {@code val} is not a valid representation of a BigInteger.
     */
    public HyraxDate(@NotNull String val) {
        super(val);
    }

    public boolean isEarlierThan(BigInteger date) {
        return super.compareTo(date) < 0;
    }

    public boolean isLaterThan(BigInteger date) {
        return super.compareTo(date) > 0;
    }
}
