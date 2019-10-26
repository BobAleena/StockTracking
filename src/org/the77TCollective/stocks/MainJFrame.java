/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.the77TCollective.stocks;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.the77TCollective.stocks.util.image.WindowIcons;
import org.the77TCollective.stocks.util.json.*;
import org.the77TCollective.stocks.util.json.Query;
import org.the77TCollective.stocks.util.json.Results;
import org.the77TCollective.stocks.util.renderer.CurrentRatioColumnCellRenderer;
import org.the77TCollective.stocks.util.renderer.LFreeCashFlowColumnCellRenderer;
import org.the77TCollective.stocks.util.renderer.PEGColumnCellRenderer;
import org.the77TCollective.stocks.util.renderer.PERatioColumnCellRenderer;
import org.the77TCollective.stocks.util.renderer.PriceBookColumnCellRenderer;
import org.the77TCollective.stocks.util.renderer.ShortRatioColumnCellRenderer;
import org.the77TCollective.stocks.util.renderer.StatColumnCellRenderer;
/**
 *
 * @author BobDevelopment
 */
public class MainJFrame extends javax.swing.JFrame
{
    public static final long serialVersionUID = 1234567890L;
    private static final String stockmarkets = "Stock Analysis";
    private static StockManagerDialog stockManagerDialog;
    private static final String QUERY_YAHOOAPIS_COM = "query.yahooapis.com";
    protected HashSet<String> stocksymbols;
    /**
     * The column names for the table with the stock prices and other
     * information.
     */
    private final String[] COLUMN_NAMES = {"Symbol",
                                           "PEGRatio", 
                                           "ForwardPE",
                                           "CurrentRatio",
                                           "PriceBook",
                                           "LeveredFreeCashFlow",
                                           "ShortRatio",
                                           "Industry"};
    
   // private static final String outputFile = "current_prices.csv";  //where to put results
    private static final String currentWorkingDirectory = System.getProperty("user.dir") + "/";  //put in home
    private static Image image;
    private static Dimension preferredScrollableViewportSize = new Dimension(800, 350);
    /**
     * The x and y offset of the desktop displayed by the main frame
     * @see #MAIN_FRAME_DESKTOP
     */
    private static final int xOffset = 20, yOffset = 20;
    /**
     * Creates new form MAinJFrame
     */
    public MainJFrame()
    {
        super(stockmarkets);
        this.setTitle(stockmarkets);
        URL url;
        ArrayList<Image> imageList;
        url = getClass().getResource("Stockmarket.png");
        imageList = WindowIcons.createScaledIcons(url);
        if(!imageList.isEmpty()) {
            if(imageList.size() == WindowIcons.getResolutionSize()) {
                this.setIconImages(imageList);
            } else {
                image = Toolkit.getDefaultToolkit().getImage(url);
                this.setIconImage(image);
            }
        }
        initComponents();
        stocksymbols = getSymbols();
    }
    
    /**
     *
     * @return HashSet<String> of stock symbols
     */
    @SuppressWarnings("unchecked")
    protected final HashSet<String> getSymbols() {
        HashSet<String> stockSymbols = null;
        boolean exists = (new File("Symbols")).exists();
        if (exists) {
            try {
                BufferedInputStream bufferedInputStream;
                bufferedInputStream = new BufferedInputStream(new FileInputStream("Symbols"));
                bufferedInputStream.mark(1);
                int empty = bufferedInputStream.read();
                if(empty == -1) {
                    System.err.println("File Symbols is empty!");
                } else {
                    bufferedInputStream.reset();
                    ObjectInputStream ois = new ObjectInputStream(bufferedInputStream);
                    stockSymbols = (HashSet<String>) ois.readObject();
                }
            } catch(FileNotFoundException fnfe) {
                System.err.println(fnfe);
            } catch(IOException | ClassNotFoundException e) {
                System.err.println(e);
            }
        }
        return stockSymbols;
    }
    /**
     *
     * @param stockSymbols
     */
    @SuppressWarnings("unchecked")
    public void setSymbols(HashSet<String> stockSymbols) {
        HashSet<String> stockSymbolsFile = null;
        boolean exists = (new File("Symbols")).exists();
        if (exists) {
            try {
                BufferedInputStream bufferedInputStream;
                bufferedInputStream = new BufferedInputStream(new FileInputStream("Symbols"));
                bufferedInputStream.mark(1);
                int empty = bufferedInputStream.read();
                if(empty == -1) {
                    System.err.println("File Symbols is empty!");
                } else {
                    bufferedInputStream.reset();
                    ObjectInputStream ois = new ObjectInputStream(bufferedInputStream);
                    stockSymbolsFile = (HashSet<String>) ois.readObject();
                }
            } catch(FileNotFoundException fnfe) {
                System.err.println(fnfe);
            } catch(IOException | ClassNotFoundException e) {
                System.err.println(e);
            }
        } 
        if(stockSymbolsFile != null && stockSymbols == null) {
            stockSymbols.addAll(stockSymbolsFile);
        } 
        if(stockSymbols.size()<100) {
            try {
                ObjectOutputStream oos;
                oos = new ObjectOutputStream(new FileOutputStream("Symbols"));
                oos.writeObject(stockSymbols);
                oos.close();
            } catch(FileNotFoundException fnfe) {
                System.err.println(fnfe);
            } catch(IOException ioe) {
                System.err.println(ioe);
            }
        }
    }
    /**
     * 
     * @param stocksymbols
     */
    public void setSymbolHash(HashSet<String> stocksymbols)
    {
        this.stocksymbols = stocksymbols;
    }
    /**
     *
     * @param symbols
     */
    private void readPrices(String symbols)
    {
        Query query = null;
        Date date = new Date();
        boolean yahooFinanceQuotesBlocked = false;
        JInternalFrame jInternalFrame=new JInternalFrame("Prices on: " + date.toString(),
                                                         true, //resizable
                                                         true, //closable
                                                         true, //maximizable
                                                         true);
        Object[][] rowData = new Object[stocksymbols.size()][8];
        JTable table = new JTable(rowData, COLUMN_NAMES);
        try {   
            String keyStatsString;
            keyStatsString = createQuery(symbols);
            query = YQLquery.yqlQueryResult(YQLquery.requestURI + keyStatsString);
        } catch(UnsupportedEncodingException uee) {
            System.err.println(uee);
        }
        System.out.println("This is the query: " + query.toString());
        Results results = query.getResults();
        System.out.println("This is the results " + results);
        if(results == null) {
            yahooFinanceQuotesBlocked = true;
            String diagnostics = "placeholder"; //YQLquery.getDiagnostics(query);
            JOptionPane.showMessageDialog(this,
                diagnostics,
                "YAHOO Errog",
                JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                PrintWriter printWriter;
//                printWriter = new PrintWriter(new FileWriter(currentWorkingDirectory + outputFile), true);
//                printWriter.write("Symbol,PEGRatio,ForwardPE, CurrentRatio, PriceBook, LeveredFreeCashFlow, ShortRatio %\r\n");
                List<Quote> allQuote = results.getQuotes();
                Iterator<Quote> allQuoteIterator = allQuote.iterator();
                int i=0;
                while (allQuoteIterator.hasNext()) {
                    String symbol;
                    //BaseStockStatContent pEGRatio, forwardPE,currentRatio,priceBook,leveredFreeCashFlow,shortRatio;
                    String pEGRatio, forwardPE,currentRatio,priceBook,leveredFreeCashFlow,shortRatio;

                    
                    Quote quote = allQuoteIterator.next();
                    System.out.println(quote.toString());
                    symbol = quote.getSymbol();
                    System.out.println(symbol);
                    rowData[i][0] = symbol;
                    setCellRenderers(table);
                    pEGRatio = quote.getPEGRatio();
                    rowData[i][1] = pEGRatio;//.getContent();
                    forwardPE = quote.getForwardPE();
                    rowData[i][2] = forwardPE;//.getContent();
                    currentRatio = quote.getCurrentRatio();
                    rowData[i][3] = currentRatio;//.getContent();
                    priceBook = quote.getPriceBook();
                    rowData[i][4] = priceBook;//.getContent();
                    leveredFreeCashFlow = quote.getLeveredFreeCashFlow();
                    rowData[i][5] = leveredFreeCashFlow;//.getContent();
                    shortRatio = quote.getShortRatio();
                    rowData[i][6] = shortRatio;//.getContent();
/*                    printWriter.write(rowData[i][0] + "," +
                                      rowData[i][1] + "," +
                                      rowData[i][2] + "," +
                                      rowData[i][3] + "," +
                                      rowData[i][4] + "," +
                                      rowData[i][5] + "\r\n");*/
                    System.out.println("Printed...");
                    i++;
                }
                System.out.println("All done");
                //printWriter.flush();
              //  printWriter.close();
            } catch (Exception ioe) {
                System.err.println(ioe);
            }
        }
        if(yahooFinanceQuotesBlocked) {
            jInternalFrame.dispose();
            jInternalFrame = null;
            table = null;
            rowData = null;
        } else {
            TableColumn column = null;
            JScrollPane scrollPane = new JScrollPane(table);
            table.setPreferredScrollableViewportSize(preferredScrollableViewportSize);
            jInternalFrame.getContentPane().add(scrollPane);
            jInternalFrame.setSize(jInternalFrame.getPreferredSize());
            int openFrameCount = jDesktopPane1.getAllFrames().length;
            jInternalFrame.setLocation(xOffset*openFrameCount, yOffset*openFrameCount);
            Component ComponentAdded;
            ComponentAdded = jDesktopPane1.add(jInternalFrame);
            jInternalFrame.setVisible(true);
        }
    }
    
    
    /**
     * 
     * 
     */
    private String createQuery(String symbols) throws UnsupportedEncodingException {
        String query;
        query = URLEncoder.encode("use " + 
                //"\"https://raw.githubusercontent.com/yql/yql-tables/4404b415f2c5a2353966f7d054c238e29ef1a292/yahoo/finance/yahoo.finance.keystats.xml\" " +
               // "\"https://raw.githubusercontent.com/yql/yql-tables/d352c5ec239e2aa3d3d1192ddfa9b6271eb3f42e/yahoo/finance/yahoo.finance.keystats.xml\" " +
               // "\"https://raw.githubusercontent.com/yql/yql-tables/e50ccc07980c7697be987e37a331af253b032a64/yahoo/finance/yahoo.finance.keystats.xml\" " +    
            //query = URLEncoder.encode("use " +
        //        "\"https://raw.githubusercontent.com/yql/yql-tables/yahoo/finance/yahoo.finance.keystats.xml\" " +
                "\"https://raw.githubusercontent.com/yql/yql-tables/yahoo/finance/yahoo.finance.quotes.xml\" " +
                " as keystatistics; ", "UTF-8");
        
        symbols = "\"aapl\",\"msft\"";
        
        query = query + URLEncoder.encode("select symbol, PEGRatio, ForwardPE, CurrentRatio, PriceBook, ShortRatio, LeveredFreeCashFlow " + //Symbol, Name, BidRealtime " +
                       // "from keystatistics " + //"from yahoo.finance.keystats " +
                        "from yahoo.finance.quotes " +
                        "where symbol in (" + 
                        symbols +")", "UTF-8");// +*/
                       // "| sort(field=\"symbol\", descending=\"false\")", "UTF-8");
        query = query + YQLquery.GETparam + URLEncoder.encode(/*"http://datatables.org/alltables.env"*/"store://datatables.org/alltableswithkeys", "UTF-8");
/*        String YQLqueryString = URLEncoder.encode("select * " +
                        "from yahoo.finance.quotes " +
                        "where symbol in (" + 
                        symbols +
                        ") | sort(field=\"Name\", descending=\"false\")", "UTF-8");
*/
           
        return query;
    }
    
    /**
     * 
     * @param table 
     */
    private void setCellRenderers(JTable table) {
        TableColumnModel model = table.getColumnModel();
        model.getColumn(0).setCellRenderer(new StatColumnCellRenderer());
        model.getColumn(1).setCellRenderer(new PEGColumnCellRenderer());
        model.getColumn(2).setCellRenderer(new PERatioColumnCellRenderer());
        model.getColumn(3).setCellRenderer(new CurrentRatioColumnCellRenderer());
        model.getColumn(4).setCellRenderer(new PriceBookColumnCellRenderer());
        model.getColumn(5).setCellRenderer(new LFreeCashFlowColumnCellRenderer());
        model.getColumn(6).setCellRenderer(new ShortRatioColumnCellRenderer());
        
    }
    /**
     * @return boolean isUpAndReachable
     */
    protected boolean isUpAndReachable()
    {
        return true;
       /* boolean isUpAndReachable = false;
        if(org.the77TCollective.stocks.util.net.Network.isAInterfaceUp())
        {
            if(org.the77TCollective.stocks.util.net.Network.isReachable("www.cnn.com"))//QUERY_YAHOOAPIS_COM + "/v1/public/yql?"))
            {
                isUpAndReachable = true;
            }
            else
            {
                JOptionPane.showMessageDialog(this,
                        "Are you connected to the internet?\nMaybe your DNS-server is down?",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
        else
        {
            JOptionPane.showMessageDialog(this,
                "There seems no internet interface up other than loopback.\nCheck there is a connection.",
                "Warning",
                JOptionPane.WARNING_MESSAGE);
        }
        return isUpAndReachable;*/
    }
    /**
     * 
     * @return symbols String 
     */
    protected String getSymbolQueryString()
    {
        Iterator<String> iterator = stocksymbols.iterator();
        String symbols;
        symbols = "";
        while (iterator.hasNext())
        {
            symbols += "\"" + iterator.next() + "\"";
            if(iterator.hasNext())
            {
                symbols += ",";
            }
        }
        //symbols = "\"TRPL4.SA\"";
        return symbols;
    }
    /**
     * 
     * @param width int
     * @param height int
     */
    public void setScrollableViewportSize(int width, int height)
    {
        MainJFrame.preferredScrollableViewportSize = new Dimension(width, height);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenuFile = new javax.swing.JMenu();
        jMenuItemNew = new javax.swing.JMenuItem();
        jMenuItemOpen = new javax.swing.JMenuItem();
        jMenuItemSave = new javax.swing.JMenuItem();
        jMenuEdit = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuHelp = new javax.swing.JMenu();
        JMenuItemAbout = new javax.swing.JMenuItem();

        jMenuItem1.setText("jMenuItem1");

        jMenuItem2.setText("jMenuItem2");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jDesktopPane1.setLayout(null);

        jMenuFile.setMnemonic(KeyEvent.VK_F);
        jMenuFile.setText("File");

        jMenuItemNew.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemNew.setMnemonic(KeyEvent.VK_N);
        jMenuItemNew.setText("New");
        jMenuItemNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemNewActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemNew);

        jMenuItemOpen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemOpen.setMnemonic(KeyEvent.VK_O);
        jMenuItemOpen.setText("Open");
        jMenuItemOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemOpenActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemOpen);

        jMenuItemSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemSave.setText("Save");
        jMenuItemSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSaveActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemSave);

        jMenuBar1.add(jMenuFile);

        jMenuEdit.setMnemonic(KeyEvent.VK_E);
        jMenuEdit.setText("Edit");

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem3.setMnemonic(KeyEvent.VK_S);
        jMenuItem3.setText("Stocks");
        jMenuItem3.setToolTipText("Add or delete stocks");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemEditStocksActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuItem3);

        jMenuBar1.add(jMenuEdit);

        jMenuHelp.setMnemonic(KeyEvent.VK_H);
        jMenuHelp.setText("Help");

        JMenuItemAbout.setMnemonic(KeyEvent.VK_A);
        JMenuItemAbout.setText("About");
        jMenuHelp.add(JMenuItemAbout);

        jMenuBar1.add(jMenuHelp);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>                        
    private void jMenuItemNewActionPerformed(java.awt.event.ActionEvent evt) {                                             
        if(stocksymbols != null)
        {
            if(isUpAndReachable())
            {
                String symbols;
                symbols = getSymbolQueryString();
                readPrices(symbols);
            }
        }
        else
        {
            System.err.println("No stock symbols loaded.");
            JOptionPane.showMessageDialog(this,
                "No stock symbols loaded.\nTry saving at least on symbol.",
                "Warning",
                JOptionPane.WARNING_MESSAGE);
        }
    }                                            
    private void jMenuItemOpenActionPerformed(java.awt.event.ActionEvent evt) {                                              
        // TODO add your handling code here:
    }                                             
    private void jMenuItemSaveActionPerformed(java.awt.event.ActionEvent evt) {                                              
        if(stocksymbols == null || stocksymbols.isEmpty())
        {
            String[] symbolsArray = {"FTE.PA","MSFT","EURUSD=X","EURGBP=X","YHOO"};
            stocksymbols = new HashSet<>(Arrays.asList(symbolsArray));
        }
        setSymbols(stocksymbols);
    }                                             

    private void jMenuItemEditStocksActionPerformed(java.awt.event.ActionEvent evt) {                                                    
        int lx, ly, width, height, shrink;
        shrink = 50;
        if(stockManagerDialog == null)
        {
            stockManagerDialog = new StockManagerDialog(this, true);
        }
        /* Calculate location and dimansion of the frame */
        lx = (int)getLocationOnScreen().getX()+shrink/2;
        ly = (int)getLocationOnScreen().getY()+shrink/2;
        Point managerLocation = new Point(lx, ly);
        width = (int)getSize().getWidth() - shrink;
        height = (int)getSize().getHeight() - shrink;
        Dimension stockManagerSize = new Dimension(width, height);
        /* Now set location and dimension */
        stockManagerDialog.setLocation(managerLocation);
        stockManagerDialog.setSize(stockManagerSize);
        stockManagerDialog.setIconImage(image);
        stockManagerDialog.setVisible(true);
    }                                                   

    // Variables declaration - do not modify                     
    private javax.swing.JMenuItem JMenuItemAbout;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenu jMenuEdit;
    private javax.swing.JMenu jMenuFile;
    private javax.swing.JMenu jMenuHelp;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItemNew;
    private javax.swing.JMenuItem jMenuItemOpen;
    private javax.swing.JMenuItem jMenuItemSave;
    // End of variables declaration                   
}
