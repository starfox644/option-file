package pesoptionfile;

/**
 * Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
 * Jad home page: http://www.geocities.com/kpdus/jad.html
 * Decompiler options: packimports(3)
 */

public class PlayerStat {
    /**
     * A stat allows to read a given value about a player in the option file.
     * <br>
     * It reads the value with this algorithm:
     * <br>
     * k = playerDataBeginning + offset (on 2 bytes)
     * <br>
     * k >>>= shift
     * <br>
     * k &= mask
     *
     * @param type   Stat type
     * @param initialOffset Offset of the stat relative to the beginning of the player data
     * @param shift  Right shift to apply to the value at the offset location
     * @param mask   Mask to apply after the shift
     * @param name   Stat name
     */
    public PlayerStat(int type, int initialOffset, int shift, int mask, String name) {
        this.type = type;
        this.initialOffset = initialOffset;
        this.shift = shift;
        this.mask = mask;
        this.name = name;
    }

    /**
     * Stat type.
     * 0 : physical and attributes
     * 1 : height
     * 2 : age
     * 3 : nationality
     * 4 : foot
     * 5 : condition, game type
     * 6 : injury
     */
    public int type;

    public int initialOffset;

    public int shift;

    public int mask;

    public String name;

    public static int getPlayerStatsOffset(int playerIndex) {
        if(playerIndex >= OptionFilePlayer.firstEditIndex)
            return Stats.statsBeginOffsetEdited + (playerIndex - OptionFilePlayer.firstEditIndex) * OptionFilePlayer.size;
        else
            return Stats.statsBeginOffset + playerIndex * OptionFilePlayer.size;
    }

    public int readValue(OptionFile optionfile, int playerIndex)
    {
        int offset = getPlayerStatsOffset(playerIndex) + initialOffset;

        // get player stats in 2 bytes
        int k = Utils.UnsignedbyteToInt(optionfile.optionFileData[offset]) << 8
                | Utils.UnsignedbyteToInt(optionfile.optionFileData[offset - 1]);

        // shift stats to get the value
        k >>>= shift;

        // apply mask
        k &= mask;

        return k;
    }

    public void writeValue(OptionFile optionfile, int playerIndex, int newValue) {
        int offset = getPlayerStatsOffset(playerIndex) + initialOffset;
        // read the existing bytes for not changing the other values that can be in these ones
        int oldValue = Utils.UnsignedbyteToInt(optionfile.optionFileData[offset]) << 8
                | Utils.UnsignedbyteToInt(optionfile.optionFileData[offset - 1]);
        // creating the save mask, which allows to save the other bits of the 2 bytes
        int saveMask = 0xffff & ~(mask << shift);
        // put 0s in the value to change
        oldValue &= saveMask;
        // placing new value in the 2 bytes
        newValue &= mask;
        newValue <<= shift;
        newValue = oldValue | newValue;
        // writing the 2 bytes with the new value and the saved bits
        optionfile.optionFileData[offset - 1] = Utils.integerToUnsignedByte(newValue & 0xff);
        optionfile.optionFileData[offset] = Utils.integerToUnsignedByte(newValue >>> 8);
    }

    public String toString() {
        return name;
    }

}
