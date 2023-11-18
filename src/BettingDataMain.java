/**
 * Main class
 */

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class BettingDataMain {
    public static void main(String[] args) {
        //create a Casino so that we can change their balance
        Casino casino = new Casino();
        List<Player> players = new ArrayList<>();
        //List of Matches
        Set<Match> matches = new HashSet<>();
        readFromMatchData(matches);
        //used treemap for already sorted map by keys(playerId's)
        TreeMap<String, String> illegalPlayers = new TreeMap<>();
        //for legalPlayers Players class is implementing Comparable interface
        List<Player> legalPlayers = new ArrayList<>();
        readFromPlayerData(casino, players, illegalPlayers, matches);
        //every player who was not illegal is legal
        for (Player el : players) {
            if (!illegalPlayers.containsKey(el.getPlayerId()))
                legalPlayers.add(el);
        }
        writeToResults(legalPlayers, illegalPlayers, casino);
    }

    /**
     * Reads data from match_data.txt located in resources folder and saves it into Set of Matches
     * @return returns Set of Match elements
     */
    private static void readFromMatchData(Set<Match> matches){
        try (BufferedReader bf = new BufferedReader(new FileReader("resources/match_data.txt"))) {
            String line = bf.readLine();
            while (line != null) {
                String[] elements = line.trim().split(",");
                //elements[0] - matchId
                //elements[1] and [2] - are returnRate
                //elements[3] - matchResult
                Match match = new Match(elements[0], Double.parseDouble(elements[1]), Double.parseDouble(elements[2]), elements[3]);
                matches.add(match);
                line = bf.readLine();
            }
        } catch (IOException e) {
            System.out.println("Error with readFromMatchData "+e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads data from player_data.txt file located in resources folder
     * Saves illegal activities to corresponding map
     * Updates casino balance
     * Creates players, updates player info and saves players into List
     * @param casino Casino class element
     * @param players List of Players
     * @param illegalActivities saves illegal player activities
     */
    private static void readFromPlayerData(Casino casino, List<Player> players, Map<String, String> illegalActivities, Set<Match> matches) {
        try (BufferedReader bf = new BufferedReader(new FileReader("resources/player_data.txt"))) {
            String line = bf.readLine();
            while (line != null) {
                String[] elements = line.trim().split(",");
                String playerId = elements[0];
                Player player = new Player(0, playerId);
                //add every player to List of Players
                players.add(player);
                while (elements[0].equals(playerId) && !line.equals("")) {
                    String playerOperation = elements[1];
                    //elements[0] - player id
                    //elements[1] - player action
                    //elements[2] - match id
                    //elements[3] - bet size
                    //elements[4] - bet side

                    /*check that player hasn't already betted on that game yet or if player made an illegal operation
                    then there is no reason to check their other operations*/
                    if (!player.getBetGames().contains(elements[2]) && illegalActivities.get(player.getPlayerId())==null) {
                        switch (playerOperation) {
                            case ("DEPOSIT") -> player.deposit(Long.parseLong(elements[3]));
                            case ("WITHDRAW") -> {
                                if (player.getCoins() >= Long.parseLong(elements[3])) {
                                    player.withdraw(Long.parseLong(elements[3]));
                                } else {
                                    illegalActivities.putIfAbsent(elements[0], elements[1] + " null " + elements[3] + " null");
                                    //if player made an illegal operation then casino balance is restored
                                    casino.change(player.getWonWithBets());
                                }
                            }
                            case ("BET") -> {
                                //bet must be lower then balance
                                if (Integer.parseInt(elements[3]) <= player.getCoins()) {
                                    int betSize = Integer.parseInt(elements[3]);
                                    Match match = findMatch(matches, elements[2]);
                                    //match can't be null because if match doesn't exist then player shouldn't be able to bet on it
                                    assert match != null;
                                    //check if match winner equals players chosen team
                                    if (match.getResult().equals(elements[4])) {
                                        player.bet(betSize, true, match.winnerReturn(elements[4]));
                                        casino.change((int) -(betSize * match.winnerReturn(elements[4])));
                                        player.getBetGames().add(elements[2]);
                                    //if game ended in a draw then nothing needs to be changed
                                    } else if (match.getResult().equals("DRAW")) {
                                        player.getBetGames().add(elements[2]);
                                    } else {
                                        player.bet(betSize, false, 0);
                                        casino.change(betSize);
                                        player.getBetGames().add(elements[2]);
                                    }
                                } else {
                                    illegalActivities.putIfAbsent(elements[0], elements[1] + " " + elements[2] + " " + elements[3] + " " + elements[4]);
                                    //if player made an illegal operation then casino balance is restored
                                    casino.change(player.getWonWithBets());
                                }
                            }
                        }
                    }
                    line = bf.readLine();
                    if (line != null) {
                        elements = line.trim().split(",");
                    } else {
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error with readFromPlayerData");
            throw new RuntimeException(e);
        }
    }

    /**
     * Finds a match that has given matchId
     * @param matches set of all Matches
     * @param matchId given match id
     * @return returns Match that has the given matchId
     */
    private static Match findMatch(Set<Match> matches, String matchId){
        for(Match match : matches){
            if(match.getMatchId().equals(matchId))
                return match;
        }
        return null;
    }

    /**
     * Writes necessary data to results.txt which is located in src folder
     * @param legalPlayers List of legal Players
     * @param illegalPlayers Map that contains data about illegal players that needs to be inserted to results.txt
     * @param casino Casino class element
     */
    private static void writeToResults(List<Player> legalPlayers, Map<String, String> illegalPlayers, Casino casino) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("src/results.txt"), StandardCharsets.UTF_8))) {
            //sorts legalPlayers by compareTo method in Player class
            Collections.sort(legalPlayers);
            if(legalPlayers.size()==0) bw.write("\n");
            for (Player player:legalPlayers){
                bw.write(player.toString());
                bw.write("\n");
            }
            //necessary empty lines
            bw.write("\n");
            if(illegalPlayers.size()==0) bw.write("\n");
            for (String playerId : illegalPlayers.keySet()){
                bw.write(playerId + " " + illegalPlayers.get(playerId));
                bw.write("\n");
            }
            bw.write("\n");
            bw.write(Long.toString(casino.getBalance()));
        } catch (IOException e) {
            System.out.println("Error with writeToResults");
            throw new RuntimeException(e);
        }
    }
}