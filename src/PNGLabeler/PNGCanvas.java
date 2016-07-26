package PNGLabeler;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class PNGCanvas extends JPanel implements MouseMotionListener, MouseListener {
	
	private PNGLabelerFrame labelerFrame;
	
	private LabeledPNG labeledPNG;
	private Float scale = 1.0F;
	private boolean newLabelButtonActive = false;
	private int dataBoxWidth;
	private String newLabelCategory;
	private String newLabelName;
	private LabeledData selectedImageData;
	
	
	public PNGCanvas(PNGLabelerFrame containingFrame, int dataBoxW) {
		
		this.labelerFrame = containingFrame;
		
		this.dataBoxWidth = dataBoxW;
		this.setPreferredSize(new Dimension(800, 800));
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
	}
	
	@Override
	public Dimension getPreferredSize() {
		if(this.labeledPNG != null){
			return new Dimension((int)(this.labeledPNG.getPNGImage().getWidth() * this.scale),
					(int) (this.labeledPNG.getPNGImage().getHeight() * this.scale));
		}	else {
			return new Dimension(800, 800);
		}
	}
	
	@Override
	public void paint(Graphics g) {

		if(this.labeledPNG == null) {
			g.setColor(Color.white);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			g.setColor(Color.red);
			g.fillRect(0, 0, 50, 50);
			g.fillRect(450, 450, 50, 50);
			g.fillRect(750, 750, 50, 50);
		} else {
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
			g2d.scale(this.scale, this.scale);
			g2d.drawImage(this.labeledPNG.getToViewPNGImage(), 0, 0, null);
		}
	}
	
	public LabeledPNG getLabeledPNG() {
		return this.labeledPNG;
	}
	
	public void setPNGImg(BufferedImage pngImg) {
		
		this.labeledPNG = new LabeledPNG(pngImg);
		
		this.revalidate();
		this.repaint();
	}
	
	public void setPNGImg(LabeledPNG newLabeledPNG) {
		this.labeledPNG = newLabeledPNG;
		
		this.revalidate();
		this.repaint();
	}
	
	public void setDocumentType(String docType) {
		if(this.labeledPNG != null) {
			this.labeledPNG.setDocumentType(docType);
		}
	}
	
	public void setNewCategory(String newCategory) {
		this.newLabelCategory = newCategory;
	}
	
	public void setNewLabelName(String newName) {
		this.newLabelName = newName;
	}
	
	public void setSelectedImageDataCategory(String newCategory) {
		if(this.selectedImageData != null) {
			this.selectedImageData.setLabelCategory(newCategory);
		}
	}
	
	public void setSelectedImageDataName(String newName) {
		if(this.selectedImageData != null){
			this.selectedImageData.setLabelName(newName);
		}
	}
	
	public void updateScale(Float newScale) {
		
		this.scale = newScale;
		
		this.revalidate();
		this.repaint();
	}
	
	public void newLabelButtonActivated() {
		this.newLabelButtonActive = true;
	}
	
	public void newLabelButtonDeactivated() {
		this.newLabelButtonActive = false;
		this.labeledPNG.eraseLastLabelBox();
		this.repaint();
	}
	
	public void deleteData() {
		if(this.selectedImageData != null){
			this.labeledPNG.deleteData(this.selectedImageData);
			this.repaint();
			this.selectedImageData = null;
		}
	}

	// Mouse Motion Listener
	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		if(this.labeledPNG != null && this.newLabelButtonActive) {
			this.labeledPNG.updateLabelBox((int)(arg0.getX() / this.scale), (int)(arg0.getY() / this.scale));
			this.repaint();
		}
	}
	
	// Mouse Event Listener
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		this.labelerFrame.subMouseEntered();
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		
		this.labelerFrame.subMouseClicked();

		int x = (int)(arg0.getX() / this.scale);
		int y = (int)(arg0.getY() / this.scale);
		
		boolean alreadyLabeled = false;
		
		ArrayList<LabeledData> data = this.labeledPNG.getLabeledData();
		
		for(LabeledData dat : data) {
			
			if(dat.getLabelImageCenter()[0] == x && dat.getLabelImageCenter()[1] == y) {
				alreadyLabeled = true;
				
				this.selectedImageData = dat;
				this.labelerFrame.setDataImageInfo(dat.getImageDataImageIcon(), 
						dat.getLabelCategory(), dat.getLabelName());
				
				this.newLabelButtonActive = false;
				this.newLabelButtonDeactivated();
				this.labeledPNG.updateLabelBox(x, y);
				this.repaint();
			}
		}
		
		if(this.newLabelButtonActive && !alreadyLabeled) {
			
			this.newLabelButtonActive = false;
			this.selectedImageData = this.labeledPNG.addLabel(x, y, 
					this.newLabelCategory, this.newLabelName);
			this.labelerFrame.setDataImageInfo(this.selectedImageData.getImageDataImageIcon(), 
					this.newLabelCategory, this.newLabelName);
			this.repaint();
		} else {
			
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
