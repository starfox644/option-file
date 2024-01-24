import pesoptionfile.*;

import java.io.File;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        File file = new File("data\\KONAMI-WIN32PES6OPT_original");
//        File file = new File("data\\decrypted");
//        File file = new File("data\\decrypted_from_server");
//        File outFile = new File("data\\decrypted");

        OptionFile optionFile = new OptionFile();
        optionFile.read(file);

//        pesoptionfile.League league = new pesoptionfile.League(1);
//        league.read(optionFile);
//        System.out.println(league.getIndex() + " " + league.getName());
//
//        for (pesoptionfile.Team team: league.getTeams()) {
//            System.out.println("\t" + team.getIndex() + " " + team.getName());
//        }

//        for (int i = 0; i < pesoptionfile.League.nbLeagues; i++) {
//            pesoptionfile.League league = new pesoptionfile.League(i);
//            league.read(optionFile);
//            System.out.println(league.getIndex() + " " + league.getName());

//            for (pesoptionfile.Team team: league.getTeams()) {
//                System.out.println("\t" + team.getIndex() + " " + team.getName());
//            }
//        }

//        for (int i = pesoptionfile.Team.nationalLimit + 2; i < pesoptionfile.Team.nationalLimit + pesoptionfile.Team.nbTeams; i++) {
//            pesoptionfile.Team team = new pesoptionfile.Team(i);
//            team.read(optionFile);
//            System.out.println(team.getIndex() + " " + team.getName() + " " + team.getPlayersIndexes());

//            for (int p: team.getPlayersIndexes()) {
//                pesoptionfile.Player player = new pesoptionfile.Player(p);
//                player.read(optionFile);
//                System.out.println(player.getName());
//            }
//            System.out.println();
//        }

//         PSG
//        Team team = new Team(105);

        // Arsenal
        Team team = new Team(optionFile, 73);
        System.out.println(team.getIndex() + " " + team.getName() + " " + team.getPlayersIndexes());

        PlayerData newPlayer = new PlayerData();
        newPlayer.setName("Victor");
        newPlayer.setAge(22);

        OptionFilePlayer playerToWrite = team.getPlayers().get(1);
        playerToWrite.writeDataFrom(newPlayer);

        for (OptionFilePlayer player: team.getPlayers()) {
            System.out.println(player.getIndex() + " " + player.getName() + " " + player.getStatValue(Stats.age));
        }

//        String[] newNames = {"MBappé", "Hakimi", "TOTO", "titi"};
//        team.writePlayersNames(optionFile, newNames);
//        Team newTeam = new Team(105);
//        team.read(optionFile);
//
//        System.out.println("New names");
//        for (Player player: team.getPlayers()) {
//            System.out.println(player.getIndex() + " " + player.getName());
//        }

//        Player frau = new Player(2138);
//        frau.read(optionFile);
//        frau.writeName(optionFile, "MBappé");

//        for (int p: team.getPlayersIndexes()) {
//            Player player = new Player(p);
//            player.read(optionFile);
//            System.out.println( player.getIndex() + " " + player.getName() + " " + player.stats);
//        }
//
//        File newFile = new File("data\\new");
//        optionFile.encryptedWrite(newFile);
//
//        pesoptionfile.Team team = new pesoptionfile.Team(pesoptionfile.Team.nationalLimit + 2);
//        team.read(optionFile);
//        System.out.println(team.getName()+ " " + team.getPlayersIndexes());
//
//        for (int p: team.getPlayersIndexes()) {
//            pesoptionfile.Player player = new pesoptionfile.Player(p);
//            player.read(optionFile);
//            System.out.println(player.getName());
//        }

//        for(int i = 0; i < pesoptionfile.Player.totalPlayers; i++) {
//            pesoptionfile.Player player = new pesoptionfile.Player(i);
//            player.read(optionFile);
//            System.out.println(player.getName());
//        }
    }
}
