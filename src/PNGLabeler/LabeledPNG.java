package PNGLabeler;

import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class LabeledPNG implements Serializable{

	private transient BufferedImage PNGImage;
	private transient BufferedImage labeledPNGImage;
	private transient BufferedImage toViewPNGImage;
	
	private ArrayList<LabeledData> labeledData;
	private int newLabelBoxWidth = 15;
	private int[] lastLabelBoxLocation = {-1, -1};
	
	private String documentType;
	
	public LabeledPNG(BufferedImage image) {
		this.PNGImage = image;
		this.labeledPNGImage = new BufferedImage(this.PNGImage.getWidth(), this.PNGImage.getHeight(), 2);
		this.labeledPNGImage.setData(this.PNGImage.getData());
		this.toViewPNGImage = new BufferedImage(this.PNGImage.getWidth(), this.PNGImage.getHeight(), 2);
		this.toViewPNGImage.setData(this.PNGImage.getData());
		this.labeledData = new ArrayList<LabeledData>();
	}
	
	public BufferedImage getPNGImage() {
		return this.PNGImage;
	}
	
	public BufferedImage getLabeledPNGImage() {
		return this.labeledPNGImage;
	}
	
	public BufferedImage getToViewPNGImage() {
		return this.toViewPNGImage;
	}
	
	public void updateLabeledPNGImage(BufferedImage updated) {
		this.labeledPNGImage = new BufferedImage(updated.getWidth(), updated.getHeight(), 2);
		this.labeledPNGImage.setData(updated.getData());
	}
	
	public ArrayList<LabeledData> getLabeledData() {
		return this.labeledData;
	}
	
	public void setDocumentType(String docType) {
		this.documentType = docType;
	}
	
	public String getDocumentType() {
		return this.documentType;
	}
	
	public LabeledData addLabel(int centerX, int centerY, String category, String name) {
		
		int subImageX = (int)(centerX - this.newLabelBoxWidth / 2);
		int subImageY = (int)(centerY - this.newLabelBoxWidth / 2);
		
		BufferedImage image = this.PNGImage.getSubimage(subImageX, subImageY, this.newLabelBoxWidth, this.newLabelBoxWidth);
		
		int[] center = {centerX, centerY};
		
		LabeledData dataToAdd = new LabeledData(this.newLabelBoxWidth, center, category, name, image);
		this.labeledData.add(dataToAdd);
		
		this.eraseLastLabelBox();

		Color pixelColor = new Color(255, 0, 0);
		this.toViewPNGImage.setRGB(centerX, centerY, pixelColor.getRGB());
		
		this.updateLabeledPNGImage(this.toViewPNGImage);
		
		this.resetLastLabelBoxLocation();
		
		return dataToAdd;
	}
	
	public void resetLastLabelBoxLocation() {
		this.lastLabelBoxLocation[0] = -1;
		this.lastLabelBoxLocation[1] = -1;
	}
	
	public void drawLabelBox(int x, int y) {
		
		int leftX = x - this.newLabelBoxWidth / 2;
		int topY = y - this.newLabelBoxWidth / 2;
		
		int[] rgbArray = new int[this.newLabelBoxWidth];
		
		for(int i = 0; i < this.newLabelBoxWidth; i++) {
			int RGB = this.PNGImage.getRGB(leftX + i, topY);
			Color pixelColor = new Color(255, 255 - (RGB>>8)&255, 255 - RGB&8);
			rgbArray[i] = pixelColor.getRGB();
		}
		
		this.toViewPNGImage.setRGB(leftX, topY, this.newLabelBoxWidth, 1, rgbArray, 0, 1);
		
		for(int i = 0; i < this.newLabelBoxWidth; i++) {
			int RGB = this.PNGImage.getRGB(leftX, topY + i);
			Color pixelColor = new Color(255, 255 - (RGB>>8)&255, 255 - RGB&8);
			rgbArray[i] = pixelColor.getRGB();
		}
		
		this.toViewPNGImage.setRGB(leftX, topY, 1, this.newLabelBoxWidth, rgbArray, 0, 1);
		
		for(int i = 0; i < this.newLabelBoxWidth; i++) {
			int RGB = this.PNGImage.getRGB(leftX + i, topY + this.newLabelBoxWidth - 1);
			Color pixelColor = new Color(255, 255 - (RGB>>8)&255, 255 - RGB&8);
			rgbArray[i] = pixelColor.getRGB();
		}
		
		this.toViewPNGImage.setRGB(leftX, topY + this.newLabelBoxWidth - 1, this.newLabelBoxWidth, 1, rgbArray, 0, 1);
		
		for(int i = 0; i < this.newLabelBoxWidth; i++) {
			int RGB = this.PNGImage.getRGB(leftX + this.newLabelBoxWidth - 1, topY + i);
			Color pixelColor = new Color(255, 255 - (RGB>>8)&255, 255 - RGB&8);
			rgbArray[i] = pixelColor.getRGB();
		}
		
		this.toViewPNGImage.setRGB(leftX + this.newLabelBoxWidth - 1, topY, 1, this.newLabelBoxWidth, rgbArray, 0, 1);
		
//		int RGB = this.PNGImage.getRGB(x, y);
//		Color pixelColor = new Color(255, 255 - (RGB>>8)&255, 255 - RGB&8);
		Color pixelColor = new Color(255, 0, 0);
		this.toViewPNGImage.setRGB(x, y, pixelColor.getRGB());
	}
	
	public void eraseLastLabelBox() {
		
		if(this.lastLabelBoxLocation[0] == -1 && this.lastLabelBoxLocation[1] == -1) {
			return;
		}
		
		int[] rgbArray = new int[this.newLabelBoxWidth];
		
		int x = this.lastLabelBoxLocation[0] - this.newLabelBoxWidth / 2;
		int y = this.lastLabelBoxLocation[1] - this.newLabelBoxWidth / 2;
		
		for(int i = 0; i < this.newLabelBoxWidth; i++) {
			int RGB = this.labeledPNGImage.getRGB(x + i, y);
			rgbArray[i] = RGB;
		}
		
		this.toViewPNGImage.setRGB(x, y, this.newLabelBoxWidth, 1, rgbArray, 0, 1);

		for(int i = 0; i < this.newLabelBoxWidth; i++) {
			int RGB = this.labeledPNGImage.getRGB(x, y + i);
			rgbArray[i] = RGB;
		}
		
		this.toViewPNGImage.setRGB(x, y, 1, this.newLabelBoxWidth, rgbArray, 0, 1);

		for(int i = 0; i < this.newLabelBoxWidth; i++) {
			int RGB = this.labeledPNGImage.getRGB(x + i, y + this.newLabelBoxWidth - 1);
			rgbArray[i] = RGB;
		}
		
		this.toViewPNGImage.setRGB(x, y + this.newLabelBoxWidth - 1, this.newLabelBoxWidth, 1, rgbArray, 0, 1);

		for(int i = 0; i < this.newLabelBoxWidth; i++) {
			int RGB = this.labeledPNGImage.getRGB(x + this.newLabelBoxWidth - 1, y + i);
			rgbArray[i] = RGB;
		}
		
		this.toViewPNGImage.setRGB(x + this.newLabelBoxWidth - 1, y, 1, this.newLabelBoxWidth, rgbArray, 0, 1);
		
		int RGB = this.labeledPNGImage.getRGB(this.lastLabelBoxLocation[0], this.lastLabelBoxLocation[1]);
		this.toViewPNGImage.setRGB(this.lastLabelBoxLocation[0], this.lastLabelBoxLocation[1], RGB);
	}
	
	public void updateLabelBox(int x, int y) {
		if(this.lastLabelBoxLocation[0] != -1 && this.lastLabelBoxLocation[1] != -1) {
			this.eraseLastLabelBox();
		}
		
		if(x > this.newLabelBoxWidth / 2 && 
				x < (this.PNGImage.getWidth() - this.newLabelBoxWidth / 2) && 
				y > this.newLabelBoxWidth / 2 && 
				y < (this.PNGImage.getHeight() - this.newLabelBoxWidth / 2)) {
			this.drawLabelBox(x, y);
			this.lastLabelBoxLocation[0] = x;
			this.lastLabelBoxLocation[1] = y;
		}
	}

	public void deleteData(LabeledData toDelete) {
		
		int x = toDelete.getLabelImageCenter()[0];
		int y = toDelete.getLabelImageCenter()[1];
		
		int RGB = this.PNGImage.getRGB(x, y);
		
		this.eraseLastLabelBox();
		this.toViewPNGImage.setRGB(x, y, RGB);
		this.updateLabeledPNGImage(this.toViewPNGImage);
		
		this.labeledData.remove(toDelete);
	}
	
	
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
		ImageIO.write(this.PNGImage, "png", out);
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		
		this.PNGImage = ImageIO.read(in);
		this.labeledPNGImage = new BufferedImage(this.PNGImage.getWidth(), this.PNGImage.getHeight(), this.PNGImage.getType());
		this.labeledPNGImage.setData(this.PNGImage.getData());
		
		for(LabeledData dat : this.labeledData) {
			int x = dat.getLabelImageCenter()[0];
			int y = dat.getLabelImageCenter()[1];
			this.labeledPNGImage.setRGB(x, y, new Color(255, 0, 0).getRGB());
		}
		
		this.toViewPNGImage = new BufferedImage(this.labeledPNGImage.getWidth(), this.labeledPNGImage.getHeight(), this.labeledPNGImage.getType());
		this.toViewPNGImage.setData(this.labeledPNGImage.getData());
	}
}


