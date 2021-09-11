package utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ImageHandler {

	public static BufferedImage toBufferedImage(Image image) {
		if (image instanceof BufferedImage)
			return (BufferedImage) image;

		// Create a buffered image with transparency
		BufferedImage bImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
				BufferedImage.TYPE_INT_ARGB);

		// Draw the image on to the buffered image
		Graphics2D bGr = bImage.createGraphics();
		bGr.drawImage(image, 0, 0, null);
		bGr.dispose();

		return bImage;
	}

	// crop an image from an image
	public static Image cropImage(Image image, int width, int height) {
		try {
			BufferedImage croppedImage = toBufferedImage(image).getSubimage((image.getWidth(null) - width) / 2,
					(image.getHeight(null) - height) / 2, width, height);
			return croppedImage;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// crop an image from a file
	public static Image cropImage(File file, int width, int height) {
		try {
			Image image = ImageIO.read(file);
			BufferedImage croppedImage = ImageIO.read(file).getSubimage((image.getWidth(null) - width) / 2,
					(image.getHeight(null) - height) / 2, width, height);
			return croppedImage;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// construct ImageIcon from file directory
	public static ImageIcon imageMaker(String fileDir, int width, int height) {
		Image image;
		try {
			image = ImageIO.read(new File(fileDir)).getScaledInstance(width, height, Image.SCALE_SMOOTH);
			ImageIcon icon = new ImageIcon(image);
			return icon;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// construct ImageIcon from file
	public static ImageIcon imageMaker(File file, int width, int height) {
		Image image = null;
		try {
			image = ImageIO.read(file).getScaledInstance(width, height, Image.SCALE_SMOOTH);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ImageIcon icon = new ImageIcon(image);
		return icon;
	}

	// construct ImageIcon from image
	public static ImageIcon imageMaker(Image image) {
		ImageIcon icon = new ImageIcon(image);
		return icon;
	}

	// get image file by JFileChooser
	public static File getImageFile(String dialogTitle) {
		final JFileChooser fileChooser = new JFileChooser();

		fileChooser.setDialogTitle(dialogTitle);

		File selectedFile = null;

		fileChooser.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter jpg = new FileNameExtensionFilter("JPG", "jpg");
		FileNameExtensionFilter jpeg = new FileNameExtensionFilter("JPEG", "jpeg");
		FileNameExtensionFilter png = new FileNameExtensionFilter("PNG", "png");
		fileChooser.addChoosableFileFilter(jpg);
		fileChooser.addChoosableFileFilter(jpeg);
		fileChooser.addChoosableFileFilter(png);

		int returnValue = fileChooser.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			selectedFile = fileChooser.getSelectedFile();
		}

		return selectedFile;
	}

	// save file to a folder in project resource
	public static void saveFile(File image, String folderName) {
		BufferedImage bi = null;
		try {
			String fileName = image.getName();
			String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
			bi = ImageIO.read(image);
			ImageIO.write(bi, extension, new File(folderName + "/" + fileName));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// convert an string dir of image into a file
	public static File convertToFile(String dir) {
		File image = new File(dir);
		return image;
	}

	// check if a image is in a folder or not
	public static boolean isExistedInFolder(String imageName, String folderName) {
		for (String fileName : listFilesForFolder(new File(folderName))) {
			if (imageName.equals(fileName)) {
				return true;
			}
		}
		return false;
	}

	public static ArrayList<String> listFilesForFolder(final File folder) {
		ArrayList<String> fileNames = new ArrayList<>();
		for (final File file : folder.listFiles()) {
			if (file.isDirectory()) {
				listFilesForFolder(file);
			} else {
				fileNames.add(file.getName());
			}
		}
		return fileNames;
	}
}
