package lib;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ImageEnc {
    String path;
    String sBoxPath;

    public ImageEnc(String p){
        this.path = p;
        this.sBoxPath = "src/res/sbox_08x08_20130110_011319_02.SBX";
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

    public int input(int y, int x, int width){
        return (y*width)+x;
    }

    public void encrypt(){
        ArrayList<Integer> sbox = new ArrayList<>();
        FileOp file = new FileOp();
        file.readSboxFile(sbox, sBoxPath);

        BufferedImage image = this.getImage();
        for(int y=0; y<image.getHeight(); y++){
            for(int x=0; x<image.getWidth(); x++){
                int pixel = image.getRGB(x, y);

                Color color = new Color(pixel);
                int r = color.getRed();
                int g = color.getGreen();
                int b = color.getBlue();

                int encryptedR = sbox.get(r);
                int encryptedG = sbox.get(g);
                int encryptedB = sbox.get(b);
                int encryptedPixel = (encryptedR<<16) | (encryptedG<<8) | encryptedB;
                image.setRGB(x, y, encryptedPixel);
            }
        }
        try {
            ImageIO.write(image, "jpg", new File("src/res/encrypted_image.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}