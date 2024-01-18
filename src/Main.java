import java.io.File;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        File file = new File("data\\KONAMI-WIN32PES6OPT");
        File outFile = new File("data\\decrypted");
        OptionFile optionFile = new OptionFile();
        optionFile.read(file);
        optionFile.decryptedWrite(outFile);

//        for (int i = 0; i < League.nbLeagues; i++) {
//            League league = new League(i);
//            league.read(optionFile);
//            System.out.println(league.getIndex() + " " + league.getName());
//
//            for (Team team: league.getTeams()) {
//                System.out.println("\t" + team.getIndex() + " " + team.getName());
//            }
//        }

//        for (int i = Team.nationalLimit + 2; i < Team.nationalLimit + Team.nbTeams; i++) {
//            Team team = new Team(i);
//            team.read(optionFile);
//            System.out.println(team.getIndex() + " " + team.getName() + " " + team.getPlayersIndexes());

//            for (int p: team.getPlayersIndexes()) {
//                Player player = new Player(p);
//                player.read(optionFile);
//                System.out.println(player.getName());
//            }
//            System.out.println();
//        }

        // PSG
        Team team = new Team(105);
        team.read(optionFile);
        System.out.println(team.getIndex() + " " + team.getName() + " " + team.getPlayersIndexes());

        for (int p: team.getPlayersIndexes()) {
            Player player = new Player(p);
            player.read(optionFile);
//            int shotAcc = Stats.readValue(optionFile, player.getIndex(), Stats.shotAcc);
//            int shotAcc = Stats.shotAcc.readValue(optionFile, player.getIndex());
            System.out.println(player.getName() + " " + player.stats);
        }
//
//        Team team = new Team(Team.nationalLimit + 2);
//        team.read(optionFile);
//        System.out.println(team.getName()+ " " + team.getPlayersIndexes());
//
//        for (int p: team.getPlayersIndexes()) {
//            Player player = new Player(p);
//            player.read(optionFile);
//            System.out.println(player.getName());
//        }

//        for(int i = 0; i < Player.totalPlayers; i++) {
//            Player player = new Player(i);
//            player.read(optionFile);
//            System.out.println(player.getName());
//        }
    }
}
