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

        for (int i = 0; i < Team.nbTeams; i++) {
            Team team = new Team(i);
            team.read(optionFile);
            System.out.println(team.getIndex() + " " + team.getName() + " " + team.getPlayersIndexes());
        }

//        for(int i = 0; i < Player.totalPlayers; i++) {
//            Player player = new Player(i);
//            player.read(optionFile);
//            System.out.println(player.getName());
//        }
    }
}
