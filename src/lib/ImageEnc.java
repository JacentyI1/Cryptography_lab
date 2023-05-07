package lib;

import org.w3c.dom.css.RGBColor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class ImageEnc {
    String path;
    String sBoxPath;
    ArrayList<Integer> sBox;
    ArrayList<Integer> inverseSBox;
    public ImageEnc(String p){
        this.path = p;
        this.sBoxPath = "src/res/sbox_08x08_20130110_011319_02.SBX";
        ArrayList<Integer> sbox = new ArrayList<>();
        FileOp file = new FileOp();
        file.readSboxFile(sbox, sBoxPath);
        this.sBox = sbox;
        this.inverseSBox = this.createInverseSBox();
    }

    public ArrayList<Integer> createInverseSBox() {
        ArrayList<Integer> inverseSBox = new ArrayList<>(256);
        for (int i = 0; i < 256; i++) inverseSBox.add(0);
        for (int i = 0; i < this.sBox.size(); i++) {
            inverseSBox.set(this.sBox.get(i), i);
        }
        return inverseSBox;
    }

    public BufferedImage getImage() {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(this.path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
    public BufferedImage getImage(String p) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(p));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public int input(int y, int x, int width){
        return (y*width)+x;
    }

    public void simpleEncrypt(String file_postfix){
        BufferedImage image = this.getImage();
        for(int y=0; y<image.getHeight(); y++){
            for(int x=0; x<image.getWidth(); x++){
                int pixel = image.getRGB(x, y);
                Color color = new Color(pixel);
                int r = color.getRed();
                int g = color.getGreen();
                int b = color.getBlue();
                int encryptedR = this.sBox.get(r);
                int encryptedG = this.sBox.get(g);
                int encryptedB = this.sBox.get(b);
                int encryptedPixel = (encryptedR<<16) | (encryptedG<<8) | encryptedB;
                image.setRGB(x, y, encryptedPixel);
//                Collections.rotate(this.sBox, 1);
            }
        }
        try {
            ImageIO.write(image, "jpg", new File("src/res/encrypted_image"+file_postfix+".jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void simpleDecript(String file_postfix){
        BufferedImage image = this.getImage("src/res/encrypted_image"+file_postfix+".jpg");
        for(int y=0; y<image.getHeight(); y++){
            for(int x=0; x<image.getWidth(); x++){
                int pixel = image.getRGB(x, y);
                Color color = new Color(pixel);
                int r = color.getRed();
                int g = color.getGreen();
                int b = color.getBlue();
                int decryptedR = this.inverseSBox.get(r);
                int decryptedG = this.inverseSBox.get(g);
                int decryptedB = this.inverseSBox.get(b);
                int decryptedPixel = (decryptedR<<16) | (decryptedG<<8) | decryptedB;
                image.setRGB(x, y, decryptedPixel);
//                Collections.rotate(this.sBox,1);
            }
        }
        try {
            ImageIO.write(image, "jpg", new File("src/res/decrypted_image"+file_postfix+".jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void rotateEnc(String file_postfix){
        BufferedImage image = this.getImage();
        for(int y=0; y<image.getHeight(); y++){
            for(int x=0; x<image.getWidth(); x++){
                int pixel = image.getRGB(x, y);
                Color color = new Color(pixel);
                int r = color.getRed();
                int g = color.getGreen();
                int b = color.getBlue();
                int encryptedR = this.sBox.get(r);
                int encryptedG = this.sBox.get(g);
                int encryptedB = this.sBox.get(b);
                int encryptedPixel = (encryptedR<<16) | (encryptedG<<8) | encryptedB;
                image.setRGB(x, y, encryptedPixel);
                Collections.rotate(this.sBox, 1);
            }
        }
        try {
            ImageIO.write(image, "jpg", new File("src/res/encrypted_image"+file_postfix+".jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void previousStateEnc(String file_postfix){
        BufferedImage image = this.getImage();
        int first = image.getRGB(0,0);
        Color color1 = new Color(first);
        int r1 = color1.getRed();
        int g1 = color1.getGreen();
        int b1 = color1.getBlue();
        int encryptedR = this.sBox.get(r1);
        int encryptedG = this.sBox.get(g1);
        int encryptedB = this.sBox.get(b1);
        int encryptedPixel = (encryptedR<<16) | (encryptedG<<8) | encryptedB;
        image.setRGB(0, 0, encryptedPixel);
        for(int y=0; y<image.getHeight(); y++){
            for(int x=1; x<image.getWidth(); x++){
                int pixel = image.getRGB(x-1, y);
                Color color = new Color(pixel);
                int r = color.getRed();
                int g = color.getGreen();
                int b = color.getBlue();
                encryptedR = this.sBox.get(r);
                encryptedG = this.sBox.get(g);
                encryptedB = this.sBox.get(b);
                encryptedPixel = (encryptedR<<16) | (encryptedG<<8) | encryptedB;
                image.setRGB(x, y, encryptedPixel);
//                Collections.rotate(this.sBox, 1);
            }
        }
        try {
            ImageIO.write(image, "jpg", new File("src/res/encrypted_image"+file_postfix+".jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void previousStateEncXOR(String file_postfix){
        BufferedImage image = this.getImage();
        int first = image.getRGB(0,0);
        Color color1 = new Color(first);
        int r1 = color1.getRed();
        int g1 = color1.getGreen();
        int b1 = color1.getBlue();
        int encryptedR = this.sBox.get(r1);
        int encryptedG = this.sBox.get(g1);
        int encryptedB = this.sBox.get(b1);
        int encryptedPixel = (encryptedR<<16) | (encryptedG<<8) | encryptedB;
        image.setRGB(0, 0, encryptedPixel);
        for(int y=0; y<image.getHeight(); y++){
            for(int x=1; x<image.getWidth(); x++){
                int pixel = image.getRGB(x, y) ^ image.getRGB(x-1, y);
                Color color = new Color(pixel);
                int r = color.getRed();
                int g = color.getGreen();
                int b = color.getBlue();
                encryptedR = this.sBox.get(r);
                encryptedG = this.sBox.get(g);
                encryptedB = this.sBox.get(b);
                encryptedPixel = (encryptedR<<16) | (encryptedG<<8) | encryptedB;
                image.setRGB(x, y, encryptedPixel);
//                Collections.rotate(this.sBox, 1);
            }
        }
        try {
            ImageIO.write(image, "jpg", new File("src/res/encrypted_image"+file_postfix+".jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
