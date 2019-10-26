package org.the77TCollective.stocks.util.json;
import java.util.List;
/**
 *
 * @author paddy (Patrick-Emil Zörner)
 */
public class Results
{
    //protected List<Stats> stats;
    protected List<Quote> quote;
    /**
     *
     * @return List<Quote> quote
     */
    public List<Quote> getQuotes()
    {
            return quote;
    }
    /*public List<Stats> getStats()
    {
            return stats;
    }*/
    /**
     * @param quote
     */
    public void setQuotes(List<Quote> quote)
    {
            this.quote = quote;
    }

    /**
     * @param stats
     */
/*    public void setStats(List<Stats> stats)
    {
            this.stats = stats;
    }*/
}