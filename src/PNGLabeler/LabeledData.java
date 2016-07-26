package PNGLabeler;

import java.awt.image.BufferedImage;
import java.io.Serializable;

import javax.swing.ImageIcon;

public class LabeledData implements Serializable {
//public class LabeledData {
	private int labelImageWidth;
	private int[] labelImageCenter = new int[2];
	private String labelCategory;
	private String labelName;
	private int[][][] imageData;
	private ImageIcon imageDataImageIcon;
	
	public LabeledData(int imageWidth, int[] imageCenter, String category, String name, BufferedImage image){
		this.labelImageWidth = imageWidth;
		this.labelImageCenter = imageCenter;
		this.labelCategory = category;
		this.labelName = name;
		this.imageData = new int[imageWidth][imageWidth][3];
		this.imageDataImageIcon = new ImageIcon(image);
		
		for(int x = 0; x < imageWidth; x++) {
			for(int y = 0; y < imageWidth; y++) {
				int RGB = image.getRGB(x, y);
				
				int red= (RGB>>16)&255;
				int green= (RGB>>8)&255;
				int blue= (RGB)&255;
				
				imageData[x][y][0] = red;
				imageData[x][y][1] = green;
				imageData[x][y][2] = blue;
			}
		}
	}
	
	public int[] getLabelImageCenter() {
		return this.labelImageCenter;
	}
	
	public String getLabelCategory() {
		return this.labelCategory;
	}
	
	public void setLabelCategory(String newCategory) {
		this.labelCategory = newCategory;
	}
	
	public String getLabelName() {
		return this.labelName;
	}
	
	public void setLabelName(String newName) {
		this.labelName = newName;
	}
	
	public ImageIcon getImageDataImageIcon() {
		return this.imageDataImageIcon;
	}
}
