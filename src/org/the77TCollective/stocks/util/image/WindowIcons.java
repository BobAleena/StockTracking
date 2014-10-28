/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.the77TCollective.stocks.util.image;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
/**
 *
 * @author niabot
 * @author paddy (Patrick-Emil Zörner)
 */
public class WindowIcons
{
    private static final int[] RESOLUTIONS = { 16, 24, 32, 48, 64, 96, 128, 256 };
    private URL url;
    /**
     *
     * @param url
     */
    public WindowIcons(URL url)
    {
        this.url =url;
        List<Image> imageList = getWindowIcons(url);
    }
    /**
     *
     * @param url URL
     * @return static ArrayList<Image>
     */
    public static ArrayList<Image> createScaledIcons(URL url)
    {
        ArrayList<Image> arrayList = new ArrayList<>();
        try
        {
            Image icon = ImageIO.read(url);
            for (int res : RESOLUTIONS)
            {
                    arrayList.add(createScaledImage(icon, res, res));
            }
        }
        catch(IOException ioe)
        {
            System.err.println(ioe);
        }
        return arrayList;
    }
    /**
     *
     * @param image
     * @param width
     * @param height
     * @return BufferedImage scaledImage
     */
    private static BufferedImage createScaledImage(Image image, int width, int height)
    {
        BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = scaledImage.createGraphics();
        g2d.setComposite(AlphaComposite.Src);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawImage(image, 0, 0, width, height, null);
        g2d.dispose();
        return scaledImage;
    }
    /**
     * In GUI-Klasse kopieren und "imageIcons" des Frames mit Custom Code initialisieren:
     * @return List<Image> list
     */
    private List<Image> getWindowIcons(URL url)
    {
        System.out.println(url);
        List<Image> list = WindowIcons.createScaledIcons(url);
        return list;
    }
    /**
     *
     * @return int size
     */
    public static int getResolutionSize()
    {
        int size = RESOLUTIONS.length;
        return size;
    }
}
