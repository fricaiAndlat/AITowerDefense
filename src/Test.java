import de.diavololoop.gui.GraphicUserInterface;
import de.diavololoop.gui.GuiImage;
import de.diavololoop.gui.GuiMenu.GuiMenu;
import de.diavololoop.gui.GuiMenu.ImageNode;
import de.diavololoop.gui.fxgui.FXGui;
import de.diavololoop.util.linked.Linked;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Chloroplast on 30.05.2017.
 */
public class Test {

    private static GuiImage testImage;

    public static void main(String[] args) throws InterruptedException, IOException {
        testLinked();

        FXGui gui = FXGui.create();

        testImage = createTestImage();

        gui.addDrawTask(Test::draw);


        GuiMenu menu = new GuiMenu(gui, 0, 0.5, 2, 1);
        menu.getRoot().setChild(new ImageNode(  testImage  ));


        Thread.sleep(4000);
        gui.setSize(100, 100);

        Thread.sleep(4000);
        gui.resizeToFull();





    }


    private static GuiImage createTestImage() throws IOException {

        BufferedImage image = new BufferedImage(400, 400, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g  = image.createGraphics();

        g.setColor(Color.GREEN.darker());
        g.fillRect(0, 0, 400, 400);

        g.setColor(Color.BLACK);
        g.fillRect(133, 0, 133, 400);
        g.fillRect(0, 200-20, 400, 40);

        g.setColor(Color.GREEN.darker());
        g.fillOval(150, 150, 100, 100);

        g.setColor(Color.WHITE);
        g.setFont(new Font(g.getFont().getFontName(), Font.BOLD, 150));
        g.drawString("Test", 10, 200);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "png", out);

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        return GuiImage.create(in);

    }

    static double angle;
    private static void draw(GraphicUserInterface handle) {

        handle.clear();

        handle.setColor(0, 0, 1);
        handle.fillRect(-0.9, -0.9, 0.1, 0.1);

        handle.fillOval(-0.7, -0.9, 0.1,0.1);

        handle.setColor(1, 0, 0, 0.5);
        handle.fillOval(-0.7 + 0.04, -0.9 + 0.05, 0.1,0.1);

        handle.setColor(0, 1, 0, 0.5);
        handle.fillOval(-0.7 - 0.04, -0.9 + 0.05, 0.1,0.1);

        handle.drawImage(-0.5, -0.9, 0.1, 0.1, testImage);

        angle += 0.3;

        handle.drawImage(-0.3, -0.9, 0.1, 0.1, angle, testImage);

    }



    private static void testLinked() {

        Linked<String> actual = new Linked<String>();

        actual.add("a");
        actual.add("b");
        actual.add("c");
        actual.add("d");

        System.out.println((actual.size() == 4) + "\tlength should be 4 is "+actual.size());

        System.out.println(actual.remove("c") + "\tremoveobject");

        Object[] arr = actual.toArray();

        System.out.println(Arrays.toString(arr));

        actual.forEach(string -> System.out.println(string));


    }
}
