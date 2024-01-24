package pesoptionfile;

import java.nio.charset.StandardCharsets;
import java.util.Vector;

public class League extends OptionFileElement {

    public League(OptionFile optionFile, int index) {
        super(optionFile, index);
        teams = new Vector<>();
    }

    public String getName() {
        // league name address
        int nameAdr = startAdr + getIndex() * fieldLen;
        int len = 0;
        String name;
        byte tempLen;
        if(getIndex() < 0 || getIndex() >= nbLeagues)
        {
            throw new IllegalArgumentException("Illegal league index : " + getIndex() + ", must not exceed " + (nbLeagues-1));
        }
        if (getOptionFile().optionFileData[nameAdr] != 0) {
            // name length computation, the name ends with the value 0
            tempLen = 0;
            while(tempLen < maxLen && getOptionFile().optionFileData[nameAdr + tempLen] != 0)
            {
                tempLen++;
            }
            len = tempLen;
            name = new String(getOptionFile().optionFileData, nameAdr, len, StandardCharsets.UTF_8);
        }
        else if (getIndex() >= beginCupIndex)
        {
            name = def[getIndex() - beginCupIndex];
            if (getIndex() < 27)
                name += " Cup";
        }
        else
        {
            name = "<" + getIndex() + ">";
        }
        return name;
    }

    @Override
    protected void readChildren() {
        teams.clear();
        if (getIndex() >= leagueStartIndexes.length)
            return;

        int start = leagueStartIndexes[getIndex()];
        int nb = leagueTeamNumbers[getIndex()];
        for (int teamIndex = start; teamIndex < start + nb ; teamIndex++) {
            Team team = new Team(getOptionFile(), teamIndex + 53);
            teams.add(team);
        }
    }

    public Vector<Team> getTeams() {
        return teams;
    }

    Vector<Team> teams;

    public static final byte nbLeagues = 29;

    /**	Leagues names begin offset. */
    public static final int startAdr = 11859;

    /** Maximal number of characters for a league name. */
    public static final byte maxLen = 61;

    /** Size of a league entry. */
    public static final byte fieldLen = 84;

    /** Index from which the leagues are cups. */
    public static final int beginCupIndex = 11;

    public static final String[] def = { "ISS", "England", "French", "German",
            "Italian", "Netherlands", "Spanish", "International", "European",
            "African", "American", "Asia-Oceanian", "Konami", "D2", "D1",
            "European Masters", "European Championship",
            "Product Placement Cup" };

    public static int[] leagueStartIndexes = {
            0,
            20,
            40,
            60,
            78,
            98,
            117,
            122
    };

    public static int[] leagueTeamNumbers = {
            20,
            20,
            20,
            18,
            20,
            19,
            5,
            18
    };
}
