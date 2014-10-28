package org.the77TCollective.stocks.util.json;

/**
 *
 * @author paddy
 */
public class Javascript
{
    protected String name;
    protected String verb;
    protected String content;
    /*
    protected int execution-time;
    protected int instructions-used;
    protected String table-name;
    */
    /**
     *
     * @return String
     */
    public String getName()
    {
            return name;
    }
    /**
     *
     * @return String
     */
    public String getVerb()
    {
            return verb;
    }
    /**
     *
     * @return String
     */
    public String getContent()
    {
            return content;
    }
    /**
     *
     * @param name
     */
    public void setName(String name)
    {
            this.name = name;
    }
    /**
     *
     * @param verb
     */
    public void setVerb(String verb)
    {
            this.verb = verb;
    }
    /**
     *
     * @param content
     */
    public void setContent(String content)
    {
            this.content = content;
    }
}
