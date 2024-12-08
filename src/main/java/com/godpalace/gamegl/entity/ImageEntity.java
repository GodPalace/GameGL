package com.godpalace.gamegl.entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ImageEntity extends Entity {
    protected Image image;

    public ImageEntity(Image image, int id) {
        this(image, "ImageEntity", id, 0, 0,
                image.getWidth(null), image.getHeight(null));
    }

    public ImageEntity(Image image, String name, int id, int x, int y, int width, int height) {
        super(name, id, x, y, width, height);

        this.image = image;
    }

    public ImageEntity(URL url, String name, int id, int x, int y, int width, int height) {
        super(name, id, x, y, width, height);

        try {
            this.image = ImageIO.read(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ImageEntity(String path, String name, int id, int x, int y, int width, int height) {
        super(name, id, x, y, width, height);

        try {
            this.image = ImageIO.read(new File(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ImageEntity(InputStream in, String name, int id, int x, int y, int width, int height) {
        super(name, id, x, y, width, height);

        try {
            this.image = ImageIO.read(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Image getImage() {
        return image;
    }

    @Override
    public void update(Graphics g) {
        g.drawImage(image, x, y, width, height, null);
    }
}
