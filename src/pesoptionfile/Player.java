package pesoptionfile;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Player extends OptionFileElement {
    public Player(int index) {
        super(index);
        stats = new HashMap<>();
        for (PlayerStat playerStat: Stats.ability99) {
            stats.put(playerStat, 0);
        }
    }

    @Override
    protected String readName(OptionFile optionFile) {
        String name;
        if(getIndex() == 0)
            name = "<empty>";
            // bounds check
        else if(getIndex() < 0 || getIndex() >= totalPlayers && getIndex() < firstEditIndex || getIndex() > lastEditIndex)
            name = "<ERROR>";

        else
        {
            // players begin offset
            int begin = getPlayersBegin();
            int offset = getPlayerNameOffset();

            name = internalReadName(optionFile, begin, offset);
            if (name.isEmpty())
                name = handleEmptyName(name);
        }
        return name;
    }

    static String shortName(String name) {
        String[] parts = name.split(" ");
        if (parts.length > 1) {
            return parts[parts.length - 1];
        }
        return name;
    }

    public void writeName(OptionFile optionFile, String newName) {
        if (newName.length() > nameMaxLength)
            newName = shortName(newName);

        // arguments check : empty player not allowed, maximal size for name
        if(getIndex() != 0 && newName.length() <= nameMaxLength)
        {
            byte finalName[] = new byte[nameNbBytes];
            byte gotName[];

            // convert String in option file encoding for names
            gotName = newName.getBytes(StandardCharsets.UTF_16LE);

            // copy name got from the string into the final name array
            if (gotName.length <= nameMaxBytes)
                System.arraycopy(gotName, 0, finalName, 0, gotName.length);
            else
                System.arraycopy(gotName, 0, finalName, 0, nameMaxBytes);

            int beginAdr;
            int playerOffset;
            if(getIndex() >= firstEditIndex)
            {
                beginAdr = beginOffsetEdited;
                playerOffset = (getIndex() - firstEditIndex) * size;
            }
            else
            {
                beginAdr = beginOffset;
                playerOffset = getIndex() * size;
            }
            // copy name in option file
            System.arraycopy(finalName, 0, optionFile.optionFileData, beginAdr + playerOffset, nameNbBytes);
            // removes call name of the player
            Stats.setValue(optionFile, getIndex(), Stats.callName, 52685);
            // indicates that the name was modified
            Stats.setValue(optionFile, getIndex(), Stats.nameEdited, 1);
            Stats.setValue(optionFile, getIndex(), Stats.callEdited, 1);
//            name = newName;
        }
        readName(optionFile);
    }

    private int getPlayersBegin() {
        if(getIndex() >= firstEditIndex)
            return beginOffsetEdited;
        else
            return beginOffset;
    }

    private int getPlayerNameOffset() {
        if(getIndex() >= firstEditIndex)
            return (getIndex() - firstEditIndex) * size;
        else
            return getIndex() * size;
    }

    private String internalReadName(OptionFile optionFile, int begin, int nameOffset) {
        byte[] chars = new byte[nameNbBytes];
        int playerOffset = begin + nameOffset;
        // copies name characters (16 characters, 32 bytes in UTF16)
        System.arraycopy(optionFile.optionFileData, playerOffset, chars, 0, nameNbBytes);
        boolean end = false;
        // name length computation, by searching for the first null character (2 '00' in UTF-16)
        int length = 0;
        for(int i = 0; !end && i < chars.length - 1; i += 2)
            if(chars[i] == 0 && chars[i + 1] == 0)
            {
                end = true;
                length = i;
            }

        // UTF-16 name creation
        return new String(chars, 0, length, StandardCharsets.UTF_16LE);
    }

    private String handleEmptyName(String name) {
        // edited players, without name by default
        if(getIndex() >= firstEditIndex)
            name = "<Edited " + (getIndex() - firstEditIndex) + ">";
        else
            if (getIndex() >= firstUnusedIndex)
                // unused player without name
                name = "<Unused " + getIndex() + ">";
            else
                // normal player without name, this should not happen
                name = "<L " + getIndex() + ">";
        return name;
    }

    protected void readChildren(OptionFile optionFile) {
        readStats(optionFile);
    }

    public void readStats(OptionFile optionFile) {
        for (PlayerStat playerStat: Stats.ability99) {
            stats.replace(playerStat, playerStat.readValue(optionFile, getIndex()));
        }
        nationalityCode = Stats.nationality.readValue(optionFile, getIndex());
        regPosCode = Stats.regPos.readValue(optionFile, getIndex());

        roles = new Vector<>();
        for (int roleNdx = 0; roleNdx < Stats.roles.length; roleNdx++) {
            if (Stats.roles[roleNdx].readValue(optionFile, getIndex()) != 0)
                roles.add(roleNdx);
        }
    }

    public Map<PlayerStat, Integer> stats;

    public int nationalityCode;

    public int regPosCode;

    public Vector<Integer> roles;

    public static final int minAge = 15;

    public static final int minHeight = 148;

    public static final int size = 0x7C;

    public static final int beginOffset =  0x90FC;

    public static final int shirtNameOffset = 0x911C;

    public static final int beginOffsetEdited =  0x37D0;

    public static final int shirtNameOffsetEdited = 0x37F0;

    public static final int firstUnusedIndex =  4784;

    public static final int totalPlayers =  5000;

    public static final int firstEditIndex =  32768;

    public static final int totalPlayersEdited =  184;

    public static final int lastEditIndex = firstEditIndex + totalPlayersEdited - 1;

    /** aximum number of characters for a name */
    public static final int nameMaxLength = 15;

    /** maximum number of bytes for a name */
    public static final int nameMaxBytes = 30;

    /** total number of characters for a name */
    public static final int nameTotalLength = 16;

    /** total number of bytes for a name */
    public static final int nameNbBytes = 32;

    public static final int shirtNameTotalLength = 16;

    public static final int shirtNameMaxLength = 15;
}
