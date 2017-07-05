package de.diavololoop.game;

import de.diavololoop.gui.Animation;
import de.diavololoop.gui.GuiImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by gast2 on 20.06.17.
 */
public class Resource {

    private final static double NUMBER_OF_ELEMENTS = 13 - 0.1;

    private static Resource INSTANCE;

    public static Resource current(){
        return INSTANCE;
    }

    private double progress;

    private HashMap<String, GuiImage> images = new HashMap<String, GuiImage>();
    private HashMap<String, Animation> animations = new HashMap<String, Animation>();
    private HashMap<String, GuiImage[]> mimages = new HashMap<String, GuiImage[]>();


    public Resource() {
        INSTANCE = this;
    }

    public void init(){

        progress = 0;
        images.put("monster1", GuiImage.create(Resource.class.getResourceAsStream("../../../material/monster1.png")));
        progress = 1 / NUMBER_OF_ELEMENTS;
        images.put("monster2", GuiImage.create(Resource.class.getResourceAsStream("../../../material/monster2.png")));
        progress = 2 / NUMBER_OF_ELEMENTS;
        images.put("tile", GuiImage.create(Resource.class.getResourceAsStream("../../../material/tile.png")));
        progress = 3 / NUMBER_OF_ELEMENTS;
        images.put("spawn", GuiImage.create(Resource.class.getResourceAsStream("../../../material/spawn.png")));
        progress = 4 / NUMBER_OF_ELEMENTS;
        images.put("base", GuiImage.create(Resource.class.getResourceAsStream("../../../material/base.png")));
        progress = 5 / NUMBER_OF_ELEMENTS;
        images.put("turret", GuiImage.create(Resource.class.getResourceAsStream("../../../material/turret.png")));
        progress = 6 / NUMBER_OF_ELEMENTS;
        images.put("trash", GuiImage.create(Resource.class.getResourceAsStream("../../../material/trash.png")));
        progress = 7 / NUMBER_OF_ELEMENTS;
        images.put("gunturret", GuiImage.create(Resource.class.getResourceAsStream("../../../material/gunturret.png")));
        progress = 8 / NUMBER_OF_ELEMENTS;
        images.put("flashturret", GuiImage.create(Resource.class.getResourceAsStream("../../../material/flashturret.png")));
        progress = 9 / NUMBER_OF_ELEMENTS;

        animations.put("lasershot", loadAnimation("../../../material/lasershot.png", 1, 10, false, 0.2));
        progress = 10 / NUMBER_OF_ELEMENTS;
        animations.put("gunshot", loadAnimation("../../../material/gunshot.png", 1, 10, false, 0.1));
        progress = 11 / NUMBER_OF_ELEMENTS;
        animations.put("flashshot", loadAnimation("../../../material/flashshot.png", 1, 10, true, 1));
        progress = 12 / NUMBER_OF_ELEMENTS;

        mimages.put("laserturret", loadMultiImage("../../../material/laserturret.png", 4, 4));
        progress = 13 / NUMBER_OF_ELEMENTS;

    }

    public GuiImage image(String key){
        return images.get(key);
    }

    public Animation animation(String key) {
        return animations.get(key);
    }

    public GuiImage[] mimage(String key){
        return mimages.get(key);
    }

    public double progress(){
        return progress;
    }

    private GuiImage[] loadMultiImage(String path, int nx, int ny){
        try {


            BufferedImage image = ImageIO.read(Resource.class.getResourceAsStream(path));

            int dwidth  = image.getWidth()  / nx;
            int dheight = image.getHeight() / ny;

            int cc = 0;

            GuiImage[] result = new GuiImage[nx * ny];

            for(int j = 0; j < ny; ++j){
                for(int i = 0; i < nx; ++i){

                    BufferedImage tile = image.getSubimage(i*dwidth, j*dheight, dwidth, dheight);

                    GuiImage im = toGuiImage(  tile  );

                    result[cc++] = im;

                }
            }

            return result;

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        return null;
    }

    private Animation loadAnimation(String path, int nx, int ny, boolean loop, double duration){


        try {


            BufferedImage image = ImageIO.read(Resource.class.getResourceAsStream(path));
            Animation animation = new Animation(nx*ny, loop, duration);

            int dwidth  = image.getWidth()  / nx;
            int dheight = image.getHeight() / ny;

            for(int j = 0; j < ny; ++j){
                for(int i = 0; i < nx; ++i){

                    BufferedImage tile = image.getSubimage(i*dwidth, j*dheight, dwidth, dheight);

                    GuiImage im = toGuiImage(  tile  );

                    animation.addTick(im);

                }
            }

            return animation;

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        return null;
    }

    private GuiImage toGuiImage(BufferedImage image) {

        try {

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(image, "png", out);

            ByteArrayInputStream in = new ByteArrayInputStream(  out.toByteArray()  );
            return GuiImage.create(in);

        } catch (IOException e) {
            //should never happen
            e.printStackTrace();
            System.exit(-1);
        }

        return null;

    }



}
