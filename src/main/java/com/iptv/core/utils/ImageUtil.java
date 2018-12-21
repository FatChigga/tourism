package com.iptv.core.utils;

import java.awt.Image;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ImageUtil
{

    public static void zoomImage(String sourcePath, String destPath, int w, int h)
 {

        double wr = 0.0D;
        double hr = 0.0D;

        File srcFile = new File(sourcePath);

        File destFile = new File(destPath);

        try
 {

            BufferedImage bufImg = ImageIO.read(srcFile);

            Image Itemp = bufImg.getScaledInstance(w, h, 4);

            wr = w * 1.0D / bufImg.getWidth();

            hr = h * 1.0D / bufImg.getHeight();

            AffineTransformOp ato = new AffineTransformOp(java.awt.geom.AffineTransform.getScaleInstance(wr, hr), null);

            Itemp = ato.filter(bufImg, null);

            ImageIO.write((BufferedImage) Itemp, destPath.substring(destPath.lastIndexOf(".") + 1), destFile);

        } catch (Exception ex) {

            ex.printStackTrace();

        }

    }

    public static void main(String[] args) {

        String inputPath = "/Users/mac/Downloads/logo256x256.png";

        String outputPath = "/Users/mac/Downloads/icons/";

        zoomImage(inputPath, outputPath + "icon29.png", 29, 29);

        zoomImage(inputPath, outputPath + "icon40.png", 40, 40);

        zoomImage(inputPath, outputPath + "icon50.png", 50, 50);
/* 39 */
        zoomImage(inputPath, outputPath + "icon57.png", 57, 57);
/* 40 */
        zoomImage(inputPath, outputPath + "icon58.png", 58, 58);
/* 41 */
        zoomImage(inputPath, outputPath + "icon72.png", 72, 72);
/* 42 */
        zoomImage(inputPath, outputPath + "icon76.png", 76, 76);
/* 43 */
        zoomImage(inputPath, outputPath + "icon80.png", 80, 80);
/* 44 */
        zoomImage(inputPath, outputPath + "icon87.png", 87, 87);
/* 45 */
        zoomImage(inputPath, outputPath + "icon100.png", 100, 100);
/* 46 */
        zoomImage(inputPath, outputPath + "icon114.png", 114, 114);
/* 47 */
        zoomImage(inputPath, outputPath + "icon120.png", 120, 120);
/* 48 */
        zoomImage(inputPath, outputPath + "icon144.png", 144, 144);
/* 49 */
        zoomImage(inputPath, outputPath + "icon152.png", 152, 152);
/* 50 */
        zoomImage(inputPath, outputPath + "icon180.png", 180, 180);
/*    */
    }
/*    */
}