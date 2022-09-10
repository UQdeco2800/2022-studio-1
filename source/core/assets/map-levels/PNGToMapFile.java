import javax.imageio.ImageIO;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.awt.image.BufferedImage;

public class PNGToMapFile {

        public static void main(String[] args) {
                for (int i = 0; i < args.length; i++) {
                        createMapFile(args[i], i);
                }
        }

        /**
         * Reads from input PNG to create map tiles values based on RGB values
         * 
         * @param fileName name of input PNG
         * @param levelNum arg number
         */
        public static void createMapFile(String fileName, int levelNum) {
                BufferedImage mapImg = null;
                FileWriter writer = null;

                try { // open the img for reading and file for writing
                        mapImg = ImageIO.read(new File(fileName));
                        writer = new FileWriter("level" + levelNum + ".txt");

                        int width = mapImg.getWidth(); // Image
                        int height = mapImg.getHeight();// Bounds

                        for (int x = 0; x < width; x++) {
                                for (int y = 0; y < height; y++) {
                                        int color = mapImg.getRGB(x, y);

                                        // Extract bits from RGB value and convert them to masks
                                        int b = (color & 0xff) > 0 ? 1 << 2 : 0;
                                        int g = ((color & 0xff00) >> 8) > 0 ? 1 << 1 : 0;
                                        int r = ((color & 0xff0000) >> 16) > 0 ? 1 : 0;

                                        int tileByte = r | g | b; // wow what a nice line

                                        writer.write(Integer.toString(tileByte));

                                }

                                writer.write(String.format("%n"));
                        }

                        writer.close();

                } catch (Exception e) {
                        e.printStackTrace();
                        return;
                }
        }

}
