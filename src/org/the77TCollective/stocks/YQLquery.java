package org.the77TCollective.stocks;

import org.the77TCollective.stocks.util.json.*;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import org.paddy.stockmarket.util.json.Diagnostics;
import org.paddy.stockmarket.util.json.Javascript;
//import org.the77TCollective.stocks.util.json.Query;
//import org.the77TCollective.stocks.util.json.QueryContainer;

/**
 *
 * @author paddy
 */
public class YQLquery 
{
    protected final static String requestURI = "http://query.yahooapis.com/v1/public/yql?q=";
    protected final static String GETparam = "&format=json" +
                                "&diagnostics=true" +
                                "&env=";
    /**
     * 
     * @param request String
     * @return Query
     */
    protected static Query yqlQueryResult(String request)
    {
        Query query = null;
        try
        {
            System.out.println(request);
            URL url = new URL(request);
            try
            {
                InputStreamReader isr;
                BufferedReader br;
                isr = new InputStreamReader(url.openStream());
                br = new BufferedReader(isr);
                String inputLine;
                String returnString;
                returnString = "";
                while ((inputLine = br.readLine()) != null)
                {
                        returnString += inputLine;
                }
                br.close();
                Gson gson = new Gson();
                JsonParser parser = new JsonParser();
                JsonElement jsonElement = parser.parse(returnString);
                
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                System.out.println(jsonObject);
                
                QueryContainer queryContainer;
                queryContainer = gson.fromJson(jsonElement, QueryContainer.class);
                
                query = queryContainer.getQuery();
                System.out.println(query.getCount());
            }
            catch(IOException ioe)
            {
                    System.err.println(ioe);
            }
        }
        catch(MalformedURLException mue)
        {
                System.err.println(mue);
        }
        return query;
    }
    /**
     * 
     * @param query Query
     * @return String
     */
    protected static String getDiagnostics(Query query)
    {
        Diagnostics diagnostics = query.getDiagnostics();
        String content = diagnostics.getJavascript().toString();
        System.err.println(content);
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(content);
        Javascript javascript;
        javascript = gson.fromJson(jsonElement, Javascript.class);
        content = javascript.getContent();
        return content;
    }
}