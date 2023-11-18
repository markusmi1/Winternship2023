/**
 * Class that holds match information
 */
public class Match {
    private String matchId;
    private double aWinReturn;
    private double bWinReturn;
    private String result;
    public Match(String matchId, double aWinReturn, double bWinReturn, String winner){
        this.matchId = matchId;
        this.bWinReturn = bWinReturn;
        this.aWinReturn=aWinReturn;
        this.result =winner;
    }

    //Returns match winning teams return rate(when draw then return rate isn't considered anyway)
    public double winnerReturn(String winner){
        if (result.equals("A")){
            return aWinReturn;
        }else {
            return bWinReturn;
        }
    }
    public double getaWinReturn() {
        return aWinReturn;
    }
    public double getbWinReturn() {
        return bWinReturn;
    }
    public String getMatchId() {
        return matchId;
    }
    public String getResult() {
        return result;
    }
    public void setaWinReturn(double aWinReturn) {
        this.aWinReturn = aWinReturn;
    }
    public void setbWinReturn(double bWinReturn) {
        this.bWinReturn = bWinReturn;
    }
    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }
    public void setResult(String result) {
        this.result = result;
    }
}

