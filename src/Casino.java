/**
 * Class used for casino balance
 */
public class Casino {
    private long balance;
    public Casino(){
        this.balance = 0;
    }

    //Used when player loses money to casino or wins money from casino
    public void change(int a){
        this.balance += a;
    }
    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Casino: " + "balance=" + balance;
    }
}
