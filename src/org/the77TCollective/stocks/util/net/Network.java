/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.the77TCollective.stocks.util.net;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author paddy (Patrick-Emil Zörner)
 */
public class Network
{
    private static final String OS = System.getProperty("os.name").toLowerCase();
    /**
     *
     * @return boolean isInterfaceUp
     */
    public static boolean isAInterfaceUp()
    {
        boolean isInterfaceUp = false;
        Enumeration<NetworkInterface> interfaces;
        try
        {
            interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements())
            {
                NetworkInterface interf = interfaces.nextElement();
                if(interf.isLoopback())
                {
                    isInterfaceUp = false;
                }
                else
                {
                    isInterfaceUp = interf.isUp();
                    if (isInterfaceUp)
                    {
                        return isInterfaceUp;
                    }
                    else
                    {
                        System.err.println(interf.getName() + " is down.");
                    }
                }
            }
        }
        catch (SocketException ex)
        {
            Logger.getLogger(Network.class.getName()).log(Level.SEVERE, null, ex);
        }
        return isInterfaceUp;
    }
    /**
     *
     * @param host
     * @return boolean reachable
     */
    public static boolean isReachable(String host)
    {
        if(host.equals("") || 
                host.equals("127.0.0.1") || 
                host.equals("localhost"))
        {
            System.err.println("Host can not be an empty String, 127.0.0.1 or localhost!");
            return false;
        }
        boolean reachable;
        reachable = false;
        int timeout = 100;
        try
        {
            InetAddress[] inetAddresses = InetAddress.getAllByName(host);
            for(InetAddress inetAddress : inetAddresses)
            {
                if(!inetAddress.getHostAddress().equals("127.0.0.1"))
                {
                    reachable = inetAddress.isReachable(timeout);
                    if(reachable)
                    {
                        System.out.println("Host: " + inetAddress.getHostAddress() + " is reachable.");
                        return reachable;
                    }
                }
            }
            /*
             * A Hackish Workaround
             * java InetAddress.isReachable only permits port 7
             * which in most cases is closed by firewall
             * This will return 0 if the host is reachable.
             * Otherwise, you will get "2" as a return value.
             */
            Process process = null;
            if(OS.indexOf("os x") >= 0 ||
                    OS.indexOf("nux") >= 0)
            {
                process = java.lang.Runtime.getRuntime().exec("ping -c 1 " + host);
            }
            else if(OS.indexOf("win") >= 0)
            {
                process = java.lang.Runtime.getRuntime().exec("ping -n 1 " + host);
            }
            else
            {
                System.err.println("Unknown system or not yet supported.");
            }
            try
            {
                int returnVal = process.waitFor();
                if(returnVal == 0)
                {
                    reachable = true;
                    return reachable;
                }
                else
                {
                    System.err.println(host + " is not pingable!\nProcess exited with: " + returnVal);
                }
            }
            catch(InterruptedException ie)
            {
                System.err.println(ie);
            }
        }
        catch(IOException ioe)
        {
            System.err.println(ioe);
        }
        return reachable;
    }
    /**
     * Possible Values are
     * win for Windows
     * os x for Mac
     * nix and nux for Unix and Linux
     * sunos for Solaris
     * @param identifier String
     * @return true when the identifier is found as substring
     */
    public static boolean isOs(String identifier)
    {
            return (OS.indexOf(identifier) >= 0);
    }
}
