import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
public class ImageEncryptionWithMath {
    public static void main(String[] args) {
        try {
            String[] actions = {"Encrypt", "Decrypt"};
            int action = JOptionPane.showOptionDialog(null,
                    "Select operation:",
                    "Image Encryption Tool",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, actions, actions[0]);
            if (action == JOptionPane.CLOSED_OPTION) return;
            boolean isEncrypting = (action == 0);
            String[] operations = {"XOR", "Add", "Subtract", "Swap"};
            String operation = (String) JOptionPane.showInputDialog(null,
                    "Choose pixel manipulation method:",
                    "Operation",
                    JOptionPane.PLAIN_MESSAGE,
                    null, operations, operations[0]);
            if (operation == null) return;
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select an image to " + (isEncrypting ? "encrypt" : "decrypt"));
            int result = fileChooser.showOpenDialog(null);
            if (result != JFileChooser.APPROVE_OPTION) {
                JOptionPane.showMessageDialog(null, "No file selected. Exiting.");
                return;
            }
            File inputFile = fileChooser.getSelectedFile();
            BufferedImage image = ImageIO.read(inputFile);
            int width = image.getWidth();
            int height = image.getHeight();
            String keyInput = JOptionPane.showInputDialog("Enter numeric key (use same key for decrypting):");
            int key = Integer.parseInt(keyInput);
            if (operation.equals("Swap")) {
                Random rand = new Random(key);
                int totalSwaps = width * height / 10;
                if (isEncrypting) {
                    for (int i = 0; i < totalSwaps; i++) {
                        int x1 = rand.nextInt(width);
                        int y1 = rand.nextInt(height);
                        int x2 = rand.nextInt(width);
                        int y2 = rand.nextInt(height);
                        int temp = image.getRGB(x1, y1);
                        image.setRGB(x1, y1, image.getRGB(x2, y2));
                        image.setRGB(x2, y2, temp);
                    }
                } else {
                    int[] x1s = new int[totalSwaps];
                    int[] y1s = new int[totalSwaps];
                    int[] x2s = new int[totalSwaps];
                    int[] y2s = new int[totalSwaps];
                    for (int i = 0; i < totalSwaps; i++) {
                        x1s[i] = rand.nextInt(width);
                        y1s[i] = rand.nextInt(height);
                        x2s[i] = rand.nextInt(width);
                        y2s[i] = rand.nextInt(height);
                    }
                    for (int i = totalSwaps - 1; i >= 0; i--) {
                        int temp = image.getRGB(x1s[i], y1s[i]);
                        image.setRGB(x1s[i], y1s[i], image.getRGB(x2s[i], y2s[i]));
                        image.setRGB(x2s[i], y2s[i], temp);
                    }
                }
            } else {
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        int pixel = image.getRGB(x, y);
                        int a = (pixel >> 24) & 0xff;
                        int r = (pixel >> 16) & 0xff;
                        int g = (pixel >> 8) & 0xff;
                        int b = pixel & 0xff;
                        switch (operation) {
                            case "XOR":
                                r = r ^ key;
                                g = g ^ key;
                                b = b ^ key;
                                break;
                            case "Add":
                                if (isEncrypting) {
                                    r = (r + key) % 256;
                              g = (g + key) % 256;
                                    b = (b + key) % 256;
                                } else {
                                    r = (r - key + 256) % 256;
                                    g = (g - key + 256) % 256;
                                    b = (b - key + 256) % 256;
                                }
                                break;
                            case "Subtract":
                                if (isEncrypting) {
                                    r = (r - key + 256) % 256;
                                    g = (g - key + 256) % 256;
                                    b = (b - key + 256) % 256;
                                } else {
                                    r = (r + key) % 256;
                                    g = (g + key) % 256;
                                    b = (b + key) % 256;
                                }
                                break;
                        }

                        int newPixel = (a << 24) | (r << 16) | (g << 8) | b;
                        image.setRGB(x, y, newPixel);
                    }
                }
            }
            String folder = "C:\\Users\\bhava\\OneDrive\\Documents\\INTERNSHIP\\";
            String outputName = folder + (isEncrypting ? "encrypted_" : "decrypted_")
                    + operation.toLowerCase() + ".png";
            ImageIO.write(image, "png", new File(outputName));
            JOptionPane.showMessageDialog(null,
                    (isEncrypting ? "✅ Image Encrypted Successfully!" : "🔓 Image Decrypted Successfully!")
                            + "\nOperation: " + operation
                            + "\nSaved as:\n" + outputName);
            System.out.println("File saved: " + outputName);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "❌ Error reading or writing the image file.");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "❌ Invalid key entered. Please enter a number.");
        }
    }
}
