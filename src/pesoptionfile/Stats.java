package pesoptionfile; /**
 * Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
 * Jad home page: http://www.geocities.com/kpdus/jad.html
 * Decompiler options: packimports(3)
 */

/**
 * Referenced classes of package editor:
 * Stat, pesoptionfile.OptionFile
 */
public class Stats {

    /**
     * Reads a player stat in option file.
     *
     * @param optionfile  Loaded option file.
     * @param playerIndex pesoptionfile.Player index in file.
     * @param playerStat  Stat to read.
     */
    public static int readValue(OptionFile optionfile, int playerIndex, PlayerStat playerStat) {
        int offset;
        // get player offset
        if (playerIndex >= OptionFilePlayer.firstEditIndex)
            offset = statsBeginOffsetEdited + (playerIndex - OptionFilePlayer.firstEditIndex) * OptionFilePlayer.size + playerStat.initialOffset;
        else
            offset = statsBeginOffset + playerIndex * OptionFilePlayer.size + playerStat.initialOffset;

        // get player stats in 2 bytes
        int k = Utils.UnsignedbyteToInt(optionfile.optionFileData[offset]) << 8
                | Utils.UnsignedbyteToInt(optionfile.optionFileData[offset - 1]);
        // shift stats to get the value
        k >>>= playerStat.shift;
        // apply mask
        k &= playerStat.mask;
        return k;
    }

    /**
     * Writes a value about a player in the option file.
     */
    public static void setValue(OptionFile optionfile, int playerIndex, PlayerStat playerStat, int newValue) {
        int offset;
        if (playerIndex >= OptionFilePlayer.firstEditIndex) {
            offset = statsBeginOffsetEdited + (playerIndex - OptionFilePlayer.firstEditIndex) * OptionFilePlayer.size + playerStat.initialOffset;
        } else {
            offset = statsBeginOffset + playerIndex * OptionFilePlayer.size + playerStat.initialOffset;
        }
        // read the existing bytes for not changing the other values that can be in these ones
        int oldValue = Utils.UnsignedbyteToInt(optionfile.optionFileData[offset]) << 8
                | Utils.UnsignedbyteToInt(optionfile.optionFileData[offset - 1]);
        // creating the save mask, which allows to save the other bits of the 2 bytes
        int saveMask = 0xffff & ~(playerStat.mask << playerStat.shift);
        // put 0s in the value to change
        oldValue &= saveMask;
        // placing new value in the 2 bytes
        newValue &= playerStat.mask;
        newValue <<= playerStat.shift;
        newValue = oldValue | newValue;
        // writing the 2 bytes with the new value and the saved bits
        optionfile.optionFileData[offset - 1] = Utils.integerToUnsignedByte(newValue & 0xff);
        optionfile.optionFileData[offset] = Utils.integerToUnsignedByte(newValue >>> 8);
    }

    public static void setValue(OptionFile optionfile, int i, PlayerStat playerStat, String stringValue) {
        int j = 0;
        try {
            if (playerStat.type == 0)
                j = Integer.parseInt(stringValue);
            if (playerStat.type == 1)
                j = Integer.parseInt(stringValue) - OptionFilePlayer.minHeight;
            if (playerStat.type == 2)
                j = Integer.parseInt(stringValue) - OptionFilePlayer.minAge;
            if (playerStat.type == 3) {
                for (int k = 0; k < nation.length; k++)
                    if (stringValue.equals(nation[k]))
                        j = k;

            }
            if (playerStat.type == 4) {
                for (int l = 0; l < preferredFootString.length; l++)
                    if (stringValue.equals(preferredFootString[l]))
                        j = l;

            }
            if (playerStat.type == 5)
                j = Integer.parseInt(stringValue) - 1;
            if (playerStat.type == 6) {
                for (int i1 = 0; i1 < injuryTendencyString.length; i1++)
                    if (stringValue.equals(injuryTendencyString[i1]))
                        j = i1;

            }
            if (playerStat.type == 7) {
                for (int j1 = 0; j1 < freeKickStyleString.length; j1++)
                    if (stringValue.equals(freeKickStyleString[j1]))
                        j = j1;

            }
            setValue(optionfile, i, playerStat, j);
        } catch (NumberFormatException _ex) {
        }
    }

    public static String getString(OptionFile optionfile, int i, PlayerStat playerStat) {
        String s = "";
        int value = readValue(optionfile, i, playerStat);
        // normal stats, whose value is converted to a string
        if (playerStat.type == 0)
            s = String.valueOf(value);
        // height, minimum 148 cm
        if (playerStat.type == 1)
            s = String.valueOf(value + OptionFilePlayer.minHeight);
        // age, minimum 15 years
        if (playerStat.type == 2)
            s = String.valueOf(value + OptionFilePlayer.minAge);
        // nationality
        if (playerStat.type == 3)
            if (value >= 0 && value < nation.length)
                s = nation[value];
            else
                s = value + "?";
        // foot
        if (playerStat.type == 4) {
            s = "R";
            if (value == 1)
                s = "L";
        }
        // condition and styles
        if (playerStat.type == 5)
            s = String.valueOf(value + 1);
        // injury
        if (playerStat.type == 6) {
            s = "A";
            if (value == 1)
                s = "B";
            if (value == 0)
                s = "C";
        }
        if (playerStat.type == 7)
            s = freeKickStyleString[value];
        return s;
    }

    public static final PlayerStat gk;

    public static final PlayerStat cwp;

    public static final PlayerStat cbt;

    public static final PlayerStat sb;

    public static final PlayerStat dm;

    public static final PlayerStat wb;

    public static final PlayerStat cm;

    public static final PlayerStat sm;

    public static final PlayerStat am;

    public static final PlayerStat wg;

    public static final PlayerStat ss;

    public static final PlayerStat cf;

    public static final PlayerStat[] roles;

    public static final PlayerStat attack;

    public static final PlayerStat defence;

    public static final PlayerStat balance;

    public static final PlayerStat stamina;

    public static final PlayerStat speed;

    public static final PlayerStat accel;

    public static final PlayerStat response;

    public static final PlayerStat agility;

    public static final PlayerStat dribAcc;

    public static final PlayerStat dribSpe;

    public static final PlayerStat sPassAcc;

    public static final PlayerStat sPassSpe;

    public static final PlayerStat lPassAcc;

    public static final PlayerStat lPassSpe;

    public static final PlayerStat shotAcc;

    public static final PlayerStat shotPow;

    public static final PlayerStat shotTec;

    public static final PlayerStat fk;

    public static final PlayerStat curling;

    public static final PlayerStat heading;

    public static final PlayerStat jump;

    public static final PlayerStat tech;

    public static final PlayerStat aggress;

    public static final PlayerStat mental;

    public static final PlayerStat gkAbil;

    public static final PlayerStat team;

    public static final PlayerStat[] ability99;

    public static final PlayerStat[] playerDetails;

    public static final PlayerStat[] otherAbilities;

    public static final int statsBeginOffset = 0x912C;

    public static final int statsBeginOffsetEdited = 0x3800;

    public static final PlayerStat callName = new PlayerStat(0, 1, 0, 0xFFFF, "");

    public static final PlayerStat nameEdited = new PlayerStat(0, 3, 0, 1, "");

    public static final PlayerStat shirtEdited = new PlayerStat(0, 3, 1, 1, "");

    public static final PlayerStat callEdited = new PlayerStat(0, 3, 2, 1, "");

    public static final PlayerStat foot = new PlayerStat(4, 5, 0, 1, "Foot");

    public static final PlayerStat freekick = new PlayerStat(5, 5, 1, 0xF, "");

    public static final PlayerStat pkStyle = new PlayerStat(5, 5, 5, 7, "");

    public static final PlayerStat dribSty = new PlayerStat(5, 6, 0, 3, "");

    public static final PlayerStat dkSty = new PlayerStat(5, 6, 2, 3, "");

    public static final PlayerStat regPos = new PlayerStat(0, 6, 4, 0xF, "");

    public static final PlayerStat consistency = new PlayerStat(5, 33, 0, 7, "Consistency");

    public static final PlayerStat wrongFootFrequency = new PlayerStat(5, 33, 3, 7, "W Foot Freq");

    public static final PlayerStat injuryTendency = new PlayerStat(6, 33, 6, 3, "Injury T");

    public static final PlayerStat condition = new PlayerStat(5, 33, 8, 7, "Condition");

    public static final PlayerStat wrongFootAccuracy = new PlayerStat(5, 33, 11, 7, "W Foot Acc");

    public static final PlayerStat favSide = new PlayerStat(0, 33, 14, 3, "Fav side");

    public static final PlayerStat abilityEdited = new PlayerStat(0, 40, 4, 1, "");

    public static final PlayerStat height = new PlayerStat(1, 41, 0, 0x3F, "Height");

    public static final PlayerStat weight = new PlayerStat(0, 41, 8, 0x7F, "Weight");

    public static final PlayerStat skin = new PlayerStat(0, 41, 6, 3, "");

    public static final PlayerStat hair = new PlayerStat(0, 45, 0, 0x7FF, "");

    public static final PlayerStat face = new PlayerStat(0, 53, 5, 0x1FF, "");

    public static final PlayerStat faceType = new PlayerStat(0, 55, 0, 3, "");

    // Add 15 (the min age) to obtain the real age
    public static final PlayerStat age = new PlayerStat(2, 65, 9, 0x1F, "Age");

    public static final PlayerStat nationality = new PlayerStat(3, 65, 0, 0x7F, "Nationality");

    static {
        gk = new PlayerStat(0, 7, 7, 1, "GK");
        cwp = new PlayerStat(0, 7, 15, 1, "CWP");
        cbt = new PlayerStat(0, 9, 7, 1, "CBT");
        sb = new PlayerStat(0, 9, 15, 1, "SB");
        dm = new PlayerStat(0, 11, 7, 1, "DM");
        wb = new PlayerStat(0, 11, 15, 1, "WB");
        cm = new PlayerStat(0, 13, 7, 1, "CM");
        sm = new PlayerStat(0, 13, 15, 1, "SM");
        am = new PlayerStat(0, 15, 7, 1, "AM");
        wg = new PlayerStat(0, 15, 15, 1, "WG");
        ss = new PlayerStat(0, 17, 7, 1, "SS");
        cf = new PlayerStat(0, 17, 15, 1, "CF");
        roles = (new PlayerStat[]{
                gk, new PlayerStat(0, 7, 15, 1, "?"), cwp, cbt, sb, dm, wb, cm, sm, am,
                wg, ss, cf
        });
        attack = new PlayerStat(0, 7, 0, 0x7F, "Attack");
        defence = new PlayerStat(0, 8, 0, 0x7F, "Defense");
        balance = new PlayerStat(0, 9, 0, 0x7F, "Balance");
        stamina = new PlayerStat(0, 10, 0, 0x7F, "Stamina");
        speed = new PlayerStat(0, 11, 0, 0x7F, "Speed");
        accel = new PlayerStat(0, 12, 0, 0x7F, "Accel");
        response = new PlayerStat(0, 13, 0, 0x7F, "Response");
        agility = new PlayerStat(0, 14, 0, 0x7F, "Agility");
        dribAcc = new PlayerStat(0, 15, 0, 0x7F, "Drib Acc");
        dribSpe = new PlayerStat(0, 16, 0, 0x7F, "Drib Spe");
        sPassAcc = new PlayerStat(0, 17, 0, 0x7F, "S Pass Acc");
        sPassSpe = new PlayerStat(0, 18, 0, 0x7F, "S Pass Spe");
        lPassAcc = new PlayerStat(0, 19, 0, 0x7F, "L Pass Acc");
        lPassSpe = new PlayerStat(0, 20, 0, 0x7F, "L Pass Spe");
        shotAcc = new PlayerStat(0, 21, 0, 0x7F, "Shot Acc");
        shotPow = new PlayerStat(0, 22, 0, 0x7F, "Shot Power");
        shotTec = new PlayerStat(0, 23, 0, 0x7F, "Shot Tech");
        fk = new PlayerStat(0, 24, 0, 0x7F, "FK Acc");
        curling = new PlayerStat(0, 25, 0, 0x7F, "Swerve");
        heading = new PlayerStat(0, 26, 0, 0x7F, "Heading");
        jump = new PlayerStat(0, 27, 0, 0x7F, "Jump");
        tech = new PlayerStat(0, 29, 0, 0x7F, "Tech");
        aggress = new PlayerStat(0, 30, 0, 0x7F, "Aggression");
        mental = new PlayerStat(0, 31, 0, 0x7F, "Mentality");
        gkAbil = new PlayerStat(0, 32, 0, 0x7F, "GK");
        team = new PlayerStat(0, 28, 0, 0x7F, "pesoptionfile.Team Work");
        ability99 = (new PlayerStat[]{
                attack, defence, balance, stamina, speed, accel, response, agility, dribAcc, dribSpe,
                sPassAcc, sPassSpe, lPassAcc, lPassSpe, shotAcc, shotPow, shotTec, fk, curling, heading,
                jump, tech, aggress, mental, gkAbil, team
        });
        playerDetails = (new PlayerStat[]{
                age, weight, height, nationality, skin, hair, face, faceType, foot
        });
        otherAbilities = (new PlayerStat[]{consistency});
    }

    public static final PlayerStat[] abilitySpecial = {
            new PlayerStat(0, 19, 15, 1, "Centre"),
            new PlayerStat(0, 19, 7, 1, "Penalties"),
            new PlayerStat(0, 21, 7, 1, "Dribbling"),
            new PlayerStat(0, 21, 15, 1, "Tactical Drib"),
            new PlayerStat(0, 23, 7, 1, "Positioning"),
            new PlayerStat(0, 23, 15, 1, "Reaction"),
            new PlayerStat(0, 25, 7, 1, "Playmaking"),
            new PlayerStat(0, 25, 15, 1, "Passing"),
            new PlayerStat(0, 27, 7, 1, "Scoring"),
            new PlayerStat(0, 27, 15, 1, "1-1 Scoring"),
            new PlayerStat(0, 29, 7, 1, "Post"),
            new PlayerStat(0, 29, 15, 1, "Line Position"),
            new PlayerStat(0, 31, 7, 1, "Mid shooting"),
            new PlayerStat(0, 31, 15, 1, "Side"),
            new PlayerStat(0, 35, 0, 1, "1-T Pass"),
            new PlayerStat(0, 35, 1, 1, "Outside"),
            new PlayerStat(0, 35, 2, 1, "Marking"),
            new PlayerStat(0, 35, 3, 1, "Sliding"),
            new PlayerStat(0, 35, 4, 1, "Cover"),
            new PlayerStat(0, 35, 5, 1, "D-L Control"),
            new PlayerStat(0, 35, 6, 1, "Penalty GK"),
            new PlayerStat(0, 35, 7, 1, "1-on-1 GK"),
            new PlayerStat(0, 37, 7, 1, "Long Throw")
    };

    public static final String[] nation = {
            "Austria", "Belgium", "Bulgaria", "Croatia", "Czech Republic", "Denmark", "England", "Finland", "France", "Germany",
            "Greece", "Hungary", "Ireland", "Italy", "Latvia", "Netherlands", "Northern Ireland", "Norway", "Poland", "Portugal",
            "Romania", "Russia", "Scotland", "Serbia and Montenegro", "Slovakia", "Slovenia", "Spain", "Sweden", "Switzerland", "Turkey",
            "Ukraine", "Wales", "Angola", "Cameroon", "Cote d'Ivoire", "Ghana", "Nigeria", "South Africa", "Togo", "Tunisia",
            "Costa Rica", "Mexico", "Trinidad and Tobago", "USA", "Argentina", "Brazil", "Chile", "Colombia", "Ecuador", "Paraguay",
            "Peru", "Uruguay", "Iran", "Japan", "Saudi Arabia", "South Korea", "Australia", "Bosnia and Herzegovina", "Estonia", "Israel",
            "Honduras", "Jamaica", "Panama", "Bolivia", "Venezuela", "China", "Uzbekistan", "Albania", "Cyprus", "Iceland",
            "Macedonia", "Armenia", "Belarus", "Georgia", "Liechtenstein", "Lithuania", "Algeria", "Benin", "Burkina Faso", "Cape Verde",
            "Congo", "DR Congo", "Egypt", "Equatorial Guinea", "Gabon", "Gambia", "Guinea", "Guinea-Bissau", "Kenya", "Liberia",
            "Libya", "Mali", "Morocco", "Mozambique", "Senegal", "Sierra Leone", "Zambia", "Zimbabwe", "Canada", "Grenada",
            "Guadeloupe", "Martinique", "Netherlands Antilles", "Oman", "New Zealand", "Free Nationality"
    };

    public static final String[] preferredFootString = {
            "R", "L"
    };

    public static final String[] injuryTendencyString = {
            "C", "B", "A"
    };

    public static final String[] freeKickStyleString = {
            "A", "B", "C", "D", "E", "F", "G", "H"
    };

    public static final String[] mod18 = {
            "1", "2", "3", "4", "5", "6", "7", "8"
    };
}
