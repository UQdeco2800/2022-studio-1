import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.MouseInputListener;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileInputStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeSet;
import java.util.SortedSet;
import java.awt.Graphics;
import java.lang.Math;

public class MapMaker extends JFrame {

        private final int width = 1500;
        private final int height = 1000;

        MapPanel mapPanel;
        JPanel tileSelectionPanel;

        private JButton currentlySelected = null;

        private HashMap<Image, Integer> imagePathIndexMap;

        private String[] textures = {
                        "../images/65x33_tiles/water.png",
                        "../images/65x33_tiles/sand.png",
                        "../images/65x33_tiles/shoreline.png",
                        "../images/65x33_tiles/shoreline.png",
                        "../images/65x33_tiles/shoreline.png",
                        "../images/65x33_tiles/shoreline.png",
                        "../images/65x33_tiles/shoreline.png",
                        "../images/65x33_tiles/shoreline.png",
                        "../images/65x33_tiles/shoreline.png",
                        "../images/65x33_tiles/shoreline.png"
        };

        private JButton makeTileButton(String imagePath) {

                ImageIcon initImage = new ImageIcon(imagePath);
                Image image = initImage.getImage();
                Image newImage = image.getScaledInstance(image.getWidth(null) * 2, image.getHeight(null) * 2,
                                java.awt.Image.SCALE_SMOOTH);
                ImageIcon imageIcon = new ImageIcon(newImage);
                JButton button = new JButton(imageIcon);

                button.setBorder(new LineBorder(Color.BLACK));
                button.setBorderPainted(false);
                button.setContentAreaFilled(false);
                button.setFocusPainted(false);
                button.setOpaque(false);

                return button;
        }

        private JPanel setUpTileSelectionPanel() {
                tileSelectionPanel = new JPanel(new GridLayout(13, 1));
                tileSelectionPanel.setSize(width / 5, height);
                tileSelectionPanel.setVisible(true);

                imagePathIndexMap = new HashMap<>();

                for (int i = 0; i < textures.length; i++) {
                        JButton button = makeTileButton(textures[i]);
                        imagePathIndexMap.put(((ImageIcon) button.getIcon()).getImage(), Integer.valueOf(i));
                        tileSelectionPanel.add(button);
                        button.addActionListener(new ActionListener() {

                                public void actionPerformed(ActionEvent evt) {

                                        if (currentlySelected != null) {
                                                currentlySelected.setBorderPainted(false);
                                        }
                                        button.setBorderPainted(true);
                                        currentlySelected = button;
                                }
                        });
                }

                JButton eraser = new JButton("Eraser");
                eraser.addActionListener(new ActionListener() {

                        public void actionPerformed(ActionEvent evt) {
                                if (currentlySelected != null) {
                                        currentlySelected.setBorderPainted(false);
                                }
                                currentlySelected = null;
                        }
                });

                JButton saveButton = new JButton("Save Map");
                saveButton.addActionListener(new ActionListener() {

                        public void actionPerformed(ActionEvent evt) {
                                mapPanel.writeToFile();
                        }

                });

                JButton loadButton = new JButton("Load Map");
                loadButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent evt) {
                                mapPanel.readFromFile();
                        }
                });

                tileSelectionPanel.add(eraser);
                tileSelectionPanel.add(saveButton);
                tileSelectionPanel.add(loadButton);

                return tileSelectionPanel;
        }

        private void setUpContent(Container container) {

                JSplitPane splitPane = new JSplitPane();

                JPanel tileSelectionPanel = setUpTileSelectionPanel();
                mapPanel = new MapPanel();

                splitPane.setSize(width, height);
                splitPane.setDividerSize(0);
                splitPane.setDividerLocation(width - (width / 5));
                splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
                splitPane.setLeftComponent((JPanel) mapPanel);
                splitPane.setRightComponent(tileSelectionPanel);
                splitPane.setVisible(true);

                container.add(splitPane);

        }

        public MapMaker() {

                setSize(width, height);
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                setUpContent(this.getContentPane());

                setVisible(true);

        }

        public static void main(String args[]) {
                JFrame frame = new MapMaker();
                frame.setTitle("Map Maker");
                frame.setLocation(400, 200);
        }

        class MapPanel extends JPanel implements MouseListener {

                private HashMap<Coordinate, Image> imagePositionMap;

                private double transformationMatrix[][];
                private double invTransformationMatrix[][];
                private double scalingMatrix[][];

                public MapPanel() {
                        super();
                        setSize(width - (width / 5), height);
                        setVisible(true);
                        setBackground(Color.BLUE);
                        addMouseListener(this);

                        imagePositionMap = new HashMap<>();

                        double firstRow[] = { Math.cos(Math.toRadians(-45)), 0 - Math.sin(Math.toRadians(-45)) };
                        double secondRow[] = { Math.sin(Math.toRadians(-45)), Math.cos(Math.toRadians(-45)) };

                        transformationMatrix = new double[][] { firstRow, secondRow };

                        double determinant = (transformationMatrix[0][0] * transformationMatrix[1][1])
                                        - (transformationMatrix[1][0] * transformationMatrix[0][1]);

                        invTransformationMatrix = new double[][] {
                                        new double[] { transformationMatrix[1][1] / determinant,
                                                        -(transformationMatrix[0][1] / determinant) },
                                        new double[] { -(transformationMatrix[1][0] / determinant),
                                                        transformationMatrix[0][0] / determinant }
                        };

                        scalingMatrix = new double[][] {
                                        new double[] { 1, 0 },
                                        new double[] { 0, 1.967 }
                        };
                }

                @Override
                protected void paintComponent(Graphics g) {
                        super.paintComponent(g);

                        for (Entry<Coordinate, Image> set : imagePositionMap.entrySet()) {

                                int tileX = set.getKey().x;
                                int tileY = set.getKey().y;

                                // System.out.print("Tile Position: " + set.getKey().toString() + ". ");

                                int x = (int) (tileX + tileY) * (130 / 2);
                                int y = (int) (getHeight() / 2) - ((tileY - tileX) * (66 / 2));

                                // System.out.println("Place at: (" + x + ", " + y + ")");
                                // x *= 130;
                                // y *= 130;

                                // double rotatedVector[] = {

                                // (transformationMatrix[0][0] * x) + (transformationMatrix[0][1] * y),
                                // (transformationMatrix[1][0] * x) + (transformationMatrix[1][1] * y)
                                // };

                                // x = rotatedVector[0];
                                // y = (getHeight() / 2) - (rotatedVector[1] / scalingMatrix[1][1]);

                                // System.out.println("x: " + x + " y: " + y);

                                g.drawImage(set.getValue(), (int) x, (int) y, null);
                        }
                }

                @Override
                public void mouseClicked(MouseEvent e) {

                        double posVector[] = { (double) e.getX(), (double) (getHeight() / 2) -
                                        e.getY() };

                        double unScaledVector[] = {
                                        (scalingMatrix[0][0] * posVector[0]) + (scalingMatrix[0][1] * posVector[1]),
                                        (scalingMatrix[1][0] * posVector[0]) + (scalingMatrix[1][1] * posVector[1])
                        };

                        // System.out.println(transformationMatrix[0][0] + "," +
                        // transformationMatrix[0][1]);
                        // System.out.println(transformationMatrix[1][0] + "," +
                        // transformationMatrix[1][1]);
                        System.out.println("Unscaled Vector: " + unScaledVector[0] + " " +
                                        unScaledVector[1]);

                        double unRotatedVector[] = {

                                        (invTransformationMatrix[0][0] * unScaledVector[0])
                                                        + (invTransformationMatrix[0][1] * unScaledVector[1]),
                                        (invTransformationMatrix[1][0] * unScaledVector[0])
                                                        + (invTransformationMatrix[1][1] * unScaledVector[1])
                        };

                        System.out.println("UnrotatedVector: " + unRotatedVector[0] + " " +
                                        unRotatedVector[1]);

                        int x = (int) Math.floor(unRotatedVector[0] / 95);
                        int y = (int) Math.floor(unRotatedVector[1] / 95);

                        System.out.println(x);
                        System.out.println(y);

                        if (x < 0 || y < 0)
                                return;

                        if (currentlySelected != null) {
                                Image tile = ((ImageIcon) currentlySelected.getIcon()).getImage();
                                imagePositionMap.put(new Coordinate(x, y), tile);

                        } else { // Eraser is selected
                                imagePositionMap.remove(new Coordinate(x, y));
                        }
                        repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                public void writeToFile() {

                        if (imagePositionMap.size() == 0) {
                                JOptionPane.showMessageDialog(null, "No tiles have been placed!");
                                return;
                        }

                        File mapFile = null;

                        boolean pathVerified = false;
                        while (!pathVerified) {
                                try {
                                        String fileName = JOptionPane.showInputDialog("Enter a file name");
                                        mapFile = new File(fileName);
                                        if (mapFile.createNewFile()) {
                                                pathVerified = true;
                                        } else {
                                                JOptionPane.showMessageDialog(null,
                                                                "File already exists.");
                                        }
                                } catch (IOException e) {
                                        e.printStackTrace();
                                        return;
                                }
                        }

                        SortedSet<Coordinate> sortedKeys = new TreeSet<>(imagePositionMap.keySet());

                        try {
                                FileWriter writer = new FileWriter(mapFile, true);

                                int currentY = sortedKeys.first().y;

                                for (Coordinate key : sortedKeys) {

                                        if (key.y > currentY) {
                                                writer.write("\n");
                                        }

                                        Image tileImage = imagePositionMap.get(key);
                                        Integer textureIndex = imagePathIndexMap.get(tileImage);

                                        // 0: water
                                        // 1: sand
                                        // 2:
                                        // 3:
                                        // 4:
                                        // 5:
                                        // 6:
                                        // 7:
                                        // 8:
                                        // 9:

                                        writer.write(textureIndex.toString());
                                }
                                writer.close();
                        } catch (IOException e) {
                                e.printStackTrace();
                                return;
                        }

                }

                public void readFromFile() {

                        HashMap<Coordinate, Image> loadedMap = new HashMap<>();

                        File mapFile = null;
                        do {
                                String fileName = JOptionPane.showInputDialog("Enter a file name");
                                mapFile = new File(fileName);
                                System.out.println(mapFile.exists());
                        } while (!mapFile.exists());
                        try (FileInputStream stream = new FileInputStream(mapFile)) {
                                int byteRead;
                                int currentY = 0;
                                int currentX = 0;
                                while ((byteRead = stream.read()) != -1) {

                                        char tileIndex = (char) byteRead;

                                        if ((tileIndex) == '\n') {
                                                currentY++;
                                                currentX = 0;
                                                continue;
                                        }

                                        ImageIcon initImage = new ImageIcon(
                                                        textures[Integer.parseInt(String.valueOf(tileIndex))]);
                                        Image image = initImage.getImage();
                                        Image newImage = image.getScaledInstance(image.getWidth(null) * 2,
                                                        image.getHeight(null) * 2,
                                                        java.awt.Image.SCALE_SMOOTH);

                                        loadedMap.put(new Coordinate(currentX, currentY), newImage);
                                        currentX++;
                                }

                                stream.close();

                        } catch (IOException e) {
                                e.printStackTrace();
                        }

                        if (loadedMap != null) {

                                if (loadedMap.size() > 0) {
                                        imagePositionMap.clear();
                                        imagePositionMap.putAll(loadedMap);
                                        repaint();
                                }
                        }
                }
        }

        class Coordinate implements Comparable<Coordinate> {

                public int x;
                public int y;

                public Coordinate(int x, int y) {
                        this.x = x;
                        this.y = y;
                }

                @Override
                public boolean equals(Object obj) {
                        if (this == obj)
                                return true;
                        if (!(obj instanceof Coordinate))
                                return false;

                        Coordinate coord = (Coordinate) obj;

                        if (coord.x == x && coord.y == y) {
                                return true;
                        }
                        return false;
                }

                @Override
                public int hashCode() {
                        return Integer.parseInt(Integer.toString(y) + Integer.toString(x));
                }

                /*
                 * The compare function compares functions first by its y value, then by its
                 * x value
                 */
                @Override
                public int compareTo(Coordinate coord) {

                        if (coord.y != this.y)
                                return this.y - coord.y;

                        return this.x - coord.x;

                }

                @Override
                public String toString() {
                        return "(" + Integer.toString(x) + ", " + Integer.toString(y) + ")";
                }

        }

}
