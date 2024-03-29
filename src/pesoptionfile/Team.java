package pesoptionfile;

import java.nio.charset.StandardCharsets;
import java.util.Vector;

public class Team extends OptionFileElement {

    public Team(OptionFile optionFile, int index) {
        super(optionFile, index);
    }

    @Override
    public String getName() {
        String name = "";
        int length = 0;

        // get name address
        int nameAddr = startNameAddr + (getIndex() - nationalLimit - 2) * size;

        if (getOptionFile().optionFileData[nameAddr] != 0) {
            // club name length computation
            for (int i = 0; i <= maxNameLen; i++)
                if (length == 0 && getOptionFile().optionFileData[nameAddr + i] == 0)
                    length = i;

            name = new String(getOptionFile().optionFileData, nameAddr, length, StandardCharsets.UTF_8);
        } else {
            name = "<" + getIndex() +">";
        }
        return name;
    }

    @Override
    protected void readChildren() {
        playersIndexes = readPlayersIndexes();
        players = readPlayers();
    }

     // (Maybe) get the player number in a team, or a way to obtain it
    private byte getSlot(int teamIndex, int playerIndex)
    {
        return getOptionFile().optionFileData[beginSlotAddress + slotSize * teamIndex + playerIndex];
    }

    private Vector<Integer> readPlayersIndexes() {
        Vector<Integer> playersIndexes = new Vector<>();
        int nbPlayers = getNbPlayers();
        int beginPlayerOffset = getBeginPlayerOffset();
        int teamNum = getTeamNum();

        for (int currPlayer = 0; currPlayer < nbPlayers; currPlayer++)
        {
            int playerAdr = beginPlayerOffset + currPlayer * 2;
            if (getIndex() >= 0 && getIndex() < nationalLimit
                    || getIndex() >= firstClubIndex && getIndex() < 213)
                playerAdr = beginPlayerOffset + getSlot(teamNum, currPlayer) * 2;

            int numPlayer = Utils.UnsignedbyteToInt(getOptionFile().optionFileData[playerAdr + 1]) << 8
                    | Utils.UnsignedbyteToInt(getOptionFile().optionFileData[playerAdr]);
            playersIndexes.add(numPlayer);
        }
        return playersIndexes;
    }

    private Vector<OptionFilePlayer> readPlayers() {
        Vector<OptionFilePlayer> players = new Vector<>();
        for (int playerIndex: playersIndexes) {
            OptionFilePlayer player = new OptionFilePlayer(getOptionFile(), playerIndex);
            players.add(player);
        }
        return players;
    }

    private int getNbPlayers() {

        if (getIndex() < nationalLimit)
            return nbPlayersNational;
        else if (getIndex() == nationalLimit)
            return 14;
        else
            return nbPlayersClub;
    }

    private int getTeamOffset() {
        if (getIndex() <= nationalLimit)
            return getIndex() * nbPlayersNational * 2;
        else
            return (getIndex() - firstClubIndex) * nbPlayersClub * 2;
    }

    private int getBeginPlayerOffset() {
        if (getIndex() <= nationalLimit)
            return nationalSquadAdr + getTeamOffset();
        else
            return clubSquadAdr + getTeamOffset();
    }

    private int getTeamNum() {
        if (getIndex() <= nationalLimit)
            return getIndex();
        else
            return getIndex() - 9;
    }

    public void writePlayersNames(Vector<String> newNames) {
        for (int nameNdx = 0; nameNdx < newNames.size() && nameNdx < players.size(); nameNdx++) {
            players.get(nameNdx).writeName(newNames.get(nameNdx));
        }
    }

    public Vector<Integer> getPlayersIndexes() {
        return playersIndexes;
    }

    private Vector<Integer> playersIndexes;

    public Vector<OptionFilePlayer> getPlayers() {
        return players;
    }

    private Vector<OptionFilePlayer> players;

    public static final int nbTeams = 140;
    public static final int startNameAddr = 0xb7770;
    public static final int size = 88;

    public static final int slotSize = 364;

    public static final int maxNameLen = 48;

    public final static int nationalSquadAdr = 0xa2334;

    public final static int clubSquadAdr = 0xa3052;

    public final static int nationalLimit = 71;

    public final static int nbPlayersNational = 23;

    public final static int nbPlayersClub = 32;

    public final static int beginSlotAddress = 0xa5558;

    public final static int atkAdr = 0xa55e7;

    public final static int defAdr = 0xa55f2;

    public final static int posAdr = 0xa55dc;

    public final static int nationalNumberAdr = 0xa0a24;

    public final static int clubNumberAdr = 0xa10b3;

    public final static int firstClubIndex = 73;
}
