/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.the77TCollective.stocks.util.json;

/**
 *
 * @author BobDevelopment
 */
public class BaseStockStatContent {
   
    protected String term;

    protected String content;
    
    public String getTerm()
    {
            return term;
    }
    /**
     * @param term
     */
    public void setTerm (String term)
    {
            this.term = term;
    }
    
    public String getContent() {
        return content;
    }
     /**
     * @param content
     */
    public void setContent (String content) {
        this.content = content;
    }
}