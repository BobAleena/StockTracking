package org.the77TCollective.stocks.util.json;
import java.util.List;
/**
 *
 * @author paddy (Patrick-Emil Zörner)
 */
public class Results
{
    protected List<Stats> stats;
    //protected List<Quote> quote;
    /**
     *
     * @return List<Quote> quote
     */
  
    public List<Stats> getStats()
    {
            return stats;
    }
    /**
     * @param stats
     */
    public void setQuotes(List<Stats> stats)
    {
            this.stats = stats;
    }
}