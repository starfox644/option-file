import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Vector;

public class Team extends OptionFileElement {

    public Team(int index) {
        super(index);
    }

    @Override
    protected String readName(OptionFile optionFile) {
        String s = "";
        int nameLength = 0;
        // get name address
        int clubAdr = startNameAddr + getIndex() * size;
        if (optionFile.optionFileData[clubAdr] != 0) {
            // club name length computation
            for (int i = 0; i <= maxNameLen; i++)
            {
                if (nameLength == 0 && optionFile.optionFileData[clubAdr + i] == 0)
                {
                    nameLength = i;
                }
            }
            try {
                s = new String(optionFile.optionFileData, clubAdr, nameLength, "UTF-8");
            } catch (UnsupportedEncodingException _ex) {
                throw new RuntimeException("UTF-8 not supported, impossible to continue.");
            }
        } else {
            s = (new StringBuilder("<")).append(String.valueOf(getIndex())).append(">")
                    .toString();
        }
        return s;
    }

    @Override
    protected void readChildren(OptionFile optionFile) {
        playersIndexes = readPlayersIndexes(optionFile);
    }

    /**
     * 		(Maybe) get the player number in a team, or a way to obtain it
     * @param optionfile
     * @param teamIndex
     * @param playerIndex	Player index in the team
     * @return
     */
    private static byte getSlot(OptionFile optionfile, int teamIndex, int playerIndex)
    {
        return optionfile.optionFileData[beginSlotAddress + size * teamIndex + playerIndex];
    }

    private Vector<Integer> readPlayersIndexes(OptionFile optionfile) {
        Vector<Integer> players = new Vector<>();
//        if (!flag && getIndex() > 63)
//            getIndex() += 9;
        // 219 = tous les joueurs
        if (getIndex() == 219) {
            // ajout de tous les joueurs dans un vecteur
            Vector<Player> vector = new Vector<Player>();
            for (int j = 1; j < 5000; j++)
                players.add(j);

            for (int k = 32768; k < 32952; k++)
                players.add(k);

            // tri des joueurs
//            Collections.sort(vector);
//            vector.trimToSize();
//            // placement des joueurs dans la liste
//            players = new Player[vector.size()];
//            players = vector.toArray(players);
        } else {
            byte nbPlayers;
            int beginPlayerOffset;
            int teamOffset;
            int teamNum;
            if (getIndex() < nationalLimit)
            {
                nbPlayers = nbPlayersNational;
                teamOffset = getIndex() * nbPlayersNational * 2;
                beginPlayerOffset = nationalSquadAdr + teamOffset;
                teamNum = getIndex();
            }
            else if (getIndex() == nationalLimit)
            {
                nbPlayers = 14;
                teamOffset = getIndex() * nbPlayersNational * 2;
                beginPlayerOffset = nationalSquadAdr + teamOffset;
                teamNum = getIndex();
            }
            else
            {
                nbPlayers = nbPlayersClub;
                teamOffset = (getIndex() - firstClubIndex) * nbPlayersClub * 2;
                beginPlayerOffset = clubSquadAdr + teamOffset;
                teamNum = getIndex() - 9;
            }

            for (int playerActu = 0; playerActu < nbPlayers; playerActu++)
            {
                int playerAdr = beginPlayerOffset + playerActu * 2;
                if (getIndex() >= 0 && getIndex() < nationalLimit
                        || getIndex() >= firstClubIndex && getIndex() < 213)
                {
                    playerAdr = beginPlayerOffset + getSlot(optionfile, teamNum, playerActu) * 2;
                }
                int numPlayer = Utils.UnsignedbyteToInt(optionfile.optionFileData[playerAdr + 1]) << 8
                        | Utils.UnsignedbyteToInt(optionfile.optionFileData[playerAdr]);
                players.add(numPlayer);
            }
        }
        return players;
    }

    public Vector<Integer> getPlayersIndexes() {
        return playersIndexes;
    }

    private Vector<Integer> playersIndexes;

    public static final int nbTeams = 140;
    public static final int startNameAddr = 0xb7770;
    public static final int size = 88;
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
