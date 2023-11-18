/**
 * Class that holds player information and changes that using class methods
 */

import java.util.HashSet;
import java.util.Set;

public class Player implements Comparable<Player>{
    private long coins;
    private String playerId;
    private Set<String> betGames;
    private int betsWon;
    private int wonWithBets;
    public Player(long money, String playerId){
        this.coins = money;
        this.playerId = playerId;
        this.betsWon=0;
        this.betGames =new HashSet<>();
        //deposited is for situation when player makes an illegal operation
        this.wonWithBets=0;
    }

    //If player makes a Bet operation then this function is called
    public void bet(int stake, boolean winner, double returnRate){
        if(winner){
            this.coins += (int) (stake*returnRate);
            this.wonWithBets+=(int) (stake*returnRate);
            betsWon+=1;
        }else {
            this.coins = coins - stake;
            this.wonWithBets=wonWithBets-stake;
        }
    }
    //If player makes a Deposit operation then this function is called
    public void deposit(long a){
        this.coins = coins+a;
    }
    //If player makes a Withdraw operation then this function is called
    public void withdraw(long a){
        this.coins = coins-a;
    }
    //Calculates players bet win rate
    public double winRate(){
        return (double)(100*betsWon/ betGames.size())/100;
    }
    public int getWonWithBets() {
        return wonWithBets;
    }
    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }
    public void setCoins(long coins) {
        this.coins = coins;
    }
    public String getPlayerId() {
        return playerId;
    }
    public long getCoins() {
        return coins;
    }

    public Set<String> getBetGames() {
        return betGames;
    }
    @Override
    public String toString() {
        return playerId+" "+ coins +" "+ winRate();
    }
    @Override
    public int compareTo(Player o) {
        return CharSequence.compare(getPlayerId(), o.getPlayerId());
    }
}
