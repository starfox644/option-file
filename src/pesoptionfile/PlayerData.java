package pesoptionfile;

import java.util.HashMap;
import java.util.Map;

public class PlayerData implements Player {

    public PlayerData() {
        name = "";
        age = 0;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getStatValue(PlayerStat stat) {
        if (stat == Stats.age)
            return age;
        else
            return 0;
    }

    public void setStatValue(PlayerStat stat, int value) {
        if (stat == Stats.age)
            age = value;
    }

    Map<PlayerStat, Integer> getStatsValues() {
        Map<PlayerStat, Integer> values = new HashMap<>();
        PlayerStat[] availableStats = {Stats.age};
        for (PlayerStat stat: availableStats)
            values.put(stat, getStatValue(stat));
        return values;
    }

    public void setAge(int realAge) {
        age = realAge - OptionFilePlayer.minAge;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;
    private int age;
}
