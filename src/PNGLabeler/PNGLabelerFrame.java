package PNGLabeler;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.LayoutStyle;

public class PNGLabelerFrame extends JFrame implements ActionListener, KeyListener {
	
	public BufferedImage pngImage;
	
	private JScrollPane scroller;
	private PNGCanvas canvasGrid;
	
	// Zoom scale
	private Float scale = 1.0F;
	
	// Data Box Width, could delete this here I think.
	private int dataBoxWidth = 13;
	
	// Toolbar objects
	private JToggleButton labelBtn;
	private boolean newDataSelector = false;
	
	// Document Properties Panel Objects
	JTextField documentTypeField;
	JTextField newDataCategoryField;
	JTextField newDataLabelField;
	
	// Data Inspector Panel Objects
	JPanel dataInspectorPanel;
	JLabel dataImageIcon;
	JTextField dataCategoryField;
	JTextField dataLabelField;
	
	public static void main(String[] args) {
		new PNGLabelerFrame();
	}
	
	public PNGLabelerFrame() {
		this.canvasGrid = new PNGCanvas(this, this.dataBoxWidth);
		this.initUI();
		
		this.requestFocus();
	}
	
	private void initUI() {
		
		this.initScroller();
		this.initDocPropertiesPanel();
		this.initDataInspectorPanel();
		
		this.createMenuBar();
		this.createToolBar();
		
		this.addKeyListener(this);
		this.setSize(1200, 1000);
		this.setBackground(Color.gray);
		this.setTitle("GridPNG");
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setVisible(true);
		
		this.requestFocus();
	}
	
	private void initScroller() {

		JPanel grayPanel = new JPanel();
		grayPanel.setBackground(Color.gray);
		grayPanel.setLayout(new GridBagLayout());
		grayPanel.add(canvasGrid);
		
		this.scroller = new JScrollPane(grayPanel);
		this.scroller.setSize(480, 480);
		this.scroller.getVerticalScrollBar().setVisible(true);
		this.scroller.getHorizontalScrollBar().setVisible(true);
		
		this.getContentPane().add(BorderLayout.CENTER, scroller);
	}
	
	private void initDocPropertiesPanel() {
		
		JPanel propertiesPanel = new JPanel();
		propertiesPanel.setBackground(Color.lightGray);
		
		GroupLayout propertiesPanelLayout = new GroupLayout(propertiesPanel);
		propertiesPanel.setLayout(propertiesPanelLayout);
		
		propertiesPanelLayout.setAutoCreateGaps(true);
		propertiesPanelLayout.setAutoCreateContainerGaps(true);
		
		JLabel documentTypeLabel = new JLabel("Document Type");
		this.documentTypeField = new JTextField(20);
		this.documentTypeField.setMaximumSize(new Dimension(200, 25));
		this.documentTypeField.addActionListener(this);
		
		JLabel dataCategoryFieldLabel = new JLabel("New Data Category");
		this.newDataCategoryField = new JTextField(20);
		this.newDataCategoryField.setMaximumSize(new Dimension(200, 25));
		this.newDataCategoryField.addActionListener(this);
		

		JLabel dataLabelFieldLabel = new JLabel("New Data Label");
		this.newDataLabelField = new JTextField(20);
		this.newDataLabelField.setMaximumSize(new Dimension(200, 25));
		this.newDataLabelField.addActionListener(this);
		
//		JCheckBox check1 = new JCheckBox("CheckBox1");
//		JCheckBox check2 = new JCheckBox("CheckBox2");
		
		JSeparator jSep = new JSeparator();
		
		propertiesPanelLayout.setVerticalGroup(
				propertiesPanelLayout.createSequentialGroup()
				.addComponent(documentTypeLabel)
//				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 15, 15)
				.addComponent(this.documentTypeField)
//				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(jSep, GroupLayout.PREFERRED_SIZE,
			             GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
				.addGap(25)
				.addComponent(dataCategoryFieldLabel)
				.addComponent(this.newDataCategoryField)
				.addComponent(dataLabelFieldLabel)
				.addComponent(this.newDataLabelField));
//				.addGroup(propertiesPanelLayout.createParallelGroup()
//						.addComponent(check1)
//						.addComponent(check2)));
		propertiesPanelLayout.setHorizontalGroup(
				propertiesPanelLayout.createParallelGroup()
				.addComponent(documentTypeLabel)
				.addComponent(this.documentTypeField)
				.addComponent(jSep)
				.addComponent(dataCategoryFieldLabel)
				.addComponent(this.newDataCategoryField)
				.addComponent(dataLabelFieldLabel)
				.addComponent(this.newDataLabelField));
//				.addGroup(propertiesPanelLayout.createSequentialGroup()
//						.addComponent(check1)
//						.addComponent(check2)));
		
		
		this.getContentPane().add(BorderLayout.LINE_START, propertiesPanel);
	}
	
	private void initDataInspectorPanel() {
		
		this.dataInspectorPanel = new JPanel();
		dataInspectorPanel.setBackground(Color.lightGray);
		
		GroupLayout inspectorLayout = new GroupLayout(dataInspectorPanel);
		dataInspectorPanel.setLayout(inspectorLayout);
		inspectorLayout.setAutoCreateGaps(true);
		inspectorLayout.setAutoCreateContainerGaps(true);
		
		JLabel dataImageLabel = new JLabel("Data Image");
//		BufferedImage dataImage = new BufferedImage(this.dataBoxWidth, this.dataBoxWidth, 0);
		ImageIcon dataImage = new ImageIcon("./resources/labelImage.png");
		this.dataImageIcon = new JLabel();
		dataImageIcon.setIcon(dataImage);
		
		JLabel dataCategoryFieldLabel = new JLabel("Data Category");
		this.dataCategoryField = new JTextField(20);
		this.dataCategoryField.setMaximumSize(new Dimension(200, 25));

		JLabel dataLabelFieldLabel = new JLabel("Data Label");
		this.dataLabelField = new JTextField(20);
		this.dataLabelField.setMaximumSize(new Dimension(200, 25));
		
		inspectorLayout.setVerticalGroup(
				inspectorLayout.createSequentialGroup()
				.addComponent(dataImageLabel)
				.addComponent(dataImageIcon)
				.addComponent(dataCategoryFieldLabel)
				.addComponent(this.dataCategoryField)
				.addComponent(dataLabelFieldLabel)
				.addComponent(this.dataLabelField));
//				.addGroup(propertiesPanelLayout.createParallelGroup()
//						.addComponent(check1)
//						.addComponent(check2)));
		inspectorLayout.setHorizontalGroup(
				inspectorLayout.createParallelGroup()
				.addComponent(dataImageLabel)
				.addComponent(dataImageIcon)
				.addComponent(dataCategoryFieldLabel)
				.addComponent(this.dataCategoryField)
				.addComponent(dataLabelFieldLabel)
				.addComponent(this.dataLabelField));
//				.addGroup(propertiesPanelLayout.createSequentialGroup()
//						.addComponent(check1)
//						.addComponent(check2)));
		
		
		this.getContentPane().add(BorderLayout.LINE_END, dataInspectorPanel);
	}
	
 	private void createMenuBar() {
		
		JMenuBar menubar = new JMenuBar();

        JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);

        JMenuItem openPNGMenuItem = new JMenuItem("Open PNG");
        openPNGMenuItem.setToolTipText("Open PNG");
        openPNGMenuItem.addActionListener(this);
        
        JMenuItem openLabeledPNGMenuItem = new JMenuItem("Open Labeled PNG");
        openLabeledPNGMenuItem.setToolTipText("Open Labeled PNG");
        openLabeledPNGMenuItem.addActionListener(this);
        
        JMenuItem saveMenuItem = new JMenuItem("Save Labeled PNG");
        saveMenuItem.setToolTipText("Save Labeled PNG");
        saveMenuItem.addActionListener(this);
        
        

        file.add(openPNGMenuItem);
        file.add(openLabeledPNGMenuItem);
        file.add(saveMenuItem);
        menubar.add(file);

        this.setJMenuBar(menubar);
	}
	
	private void createToolBar() {
		
		int toolbarButtonWidth = 30;
		
		JToolBar toolbar = new JToolBar();
		
        ImageIcon icon = new ImageIcon("./resources/labelImage.png");
        icon = new ImageIcon(icon.getImage().getScaledInstance(toolbarButtonWidth, 
        		toolbarButtonWidth, java.awt.Image.SCALE_SMOOTH));
        
		this.labelBtn = new JToggleButton(icon);
		this.labelBtn.setFocusable(false);
        toolbar.add(labelBtn);
        
        this.labelBtn.addActionListener(this);

        this.add("North", toolbar);
	}
	
	private void openPNG() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("./resources"));
        int returnFileVal = fileChooser.showOpenDialog(this.canvasGrid);
        if (returnFileVal == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			
			if(file.getName().endsWith(".png")) {
				try {
					System.out.println("File name: " + file.getAbsolutePath());
			    	this.pngImage = ImageIO.read(file);
			    	System.out.println("Image Height: " + pngImage.getHeight());
			    	System.out.println("Image Width: " + pngImage.getWidth());
			    	this.loadPNGToView();
				} catch (IOException e) {
					System.out.println("can't load PNG");
					}
			} else {
				System.out.println("File must be a PNG");
			}
		}
	}
	
	private void openLabeledPNG() {
		JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("./resources"));
		int returnFileVal = fileChooser.showOpenDialog(this.canvasGrid);
		if(returnFileVal == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			try {
				FileInputStream fIn = new FileInputStream(file.getAbsolutePath());
				ObjectInputStream oIn = new ObjectInputStream(fIn);
				LabeledPNG newLabeledPNG = (LabeledPNG) oIn.readObject();
				this.canvasGrid.setPNGImg(newLabeledPNG);
				this.documentTypeField.setText(this.canvasGrid.getLabeledPNG().getDocumentType());
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	private void saveLabeledPNG() {
		JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("./resources"));
		int returnFileVal = fileChooser.showSaveDialog(this.canvasGrid);
		if(returnFileVal == JFileChooser.APPROVE_OPTION) {
			File saveFile = fileChooser.getSelectedFile();
			try {
		    	LabeledPNG toSave = this.canvasGrid.getLabeledPNG();
			    FileOutputStream fout = new FileOutputStream(saveFile.getAbsolutePath());
			    ObjectOutputStream oos = new ObjectOutputStream(fout);
			    oos.writeObject(toSave);
			    
		    } catch(Exception ex) {
				   ex.printStackTrace();
		    }
		}
	}
	
	private void loadPNGToView() {
		this.canvasGrid.setPNGImg(this.pngImage);
		this.canvasGrid.repaint();
	}

	public void subMouseClicked(){
		
		this.requestFocus();
		
		if(this.labelBtn.isSelected()) {
			this.labelBtn.setSelected(false);
		}
	}
	
	public void subMouseEntered() {
		this.requestFocus();
		
		this.canvasGrid.setDocumentType(this.documentTypeField.getText());
		
		this.canvasGrid.setNewCategory(this.newDataCategoryField.getText());
		this.canvasGrid.setNewLabelName(this.newDataLabelField.getText());
		
		this.canvasGrid.setSelectedImageDataCategory(this.dataCategoryField.getText());
		this.canvasGrid.setSelectedImageDataName(this.dataLabelField.getText());
	}
	
	public void setDataImageInfo(ImageIcon newDataImage, String dataCategory, String dataLabel) {
		ImageIcon scalledDataImage = new ImageIcon(
				newDataImage.getImage().getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH));
		this.dataImageIcon.setIcon(scalledDataImage);
		this.dataCategoryField.setText(dataCategory);
		this.dataLabelField.setText(dataLabel);
	}
	
	// Action Listener
	@Override
	public void actionPerformed(ActionEvent event) {
//		System.out.println("Action Command: " + event.getActionCommand());
		if (event.getActionCommand().equals("Open PNG")) {
			this.openPNG();
		} else if(event.getActionCommand().equals("Save Labeled PNG")) {
			this.saveLabeledPNG();
		} else if(event.getActionCommand().equals("Open Labeled PNG")) {
			this.openLabeledPNG();
		} else if(event.getSource().equals(this.labelBtn)) {
        	this.canvasGrid.newLabelButtonActivated();
		} else if(event.getSource().equals(this.documentTypeField)) {
			this.canvasGrid.setDocumentType(this.documentTypeField.getText());
		} else if(event.getSource().equals(this.newDataCategoryField)) {
			this.canvasGrid.setNewCategory(this.newDataCategoryField.getText());
		} else if(event.getSource().equals(this.newDataLabelField)) {
			this.canvasGrid.setNewLabelName(this.newDataLabelField.getText());
		} else if(event.getSource().equals(this.dataCategoryField)) {
			this.canvasGrid.setSelectedImageDataCategory(this.dataCategoryField.getText());
		} else if(event.getSource().equals(this.dataLabelField)) {
			this.canvasGrid.setSelectedImageDataName(this.dataLabelField.getText());
		}
		
	}

	// Key Listener
	@Override
	public void keyPressed(KeyEvent event) {
//		System.out.println("Key pressed: " + event.getKeyCode());
		
		
		if(event.getKeyChar() == '=') {											// Zoom In
			this.scale = this.scale * 1.1F;
			this.canvasGrid.updateScale(this.scale);
			
			Point viewLoc = this.scroller.getViewport().getViewPosition();
			int viewWidth = this.scroller.getViewport().getWidth();
			int viewHeight = this.scroller.getViewport().getHeight();
			
			int newX = (int)(1.1 * viewLoc.x + (viewWidth / 2) * 0.1);
			int newY = (int)(1.1 * viewLoc.y + (viewHeight / 2) * 0.1);
			Point newLoc = new Point(newX, newY);
			
			this.scroller.getViewport().setViewPosition(newLoc);
			
		} else if(event.getKeyChar() == '-') {								// Zoom Out
			this.scale = this.scale / 1.1F;
			this.canvasGrid.updateScale(this.scale);
			
			Point viewLoc = this.scroller.getViewport().getViewPosition();
			int viewWidth = this.scroller.getViewport().getWidth();
			int viewHeight = this.scroller.getViewport().getHeight();
			
			int newX = (int)(viewLoc.x / 1.1 - (viewWidth / 2) * 0.091);
			int newY = (int)(viewLoc.y / 1.1 - (viewHeight / 2) * 0.091);
			Point newLoc = new Point(newX, newY);
			
			this.scroller.getViewport().setViewPosition(newLoc);
			
		} else if(event.getKeyCode() == KeyEvent.VK_ESCAPE) {
			this.labelBtn.setSelected(false);
			this.canvasGrid.newLabelButtonDeactivated();
		} else if(event.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			this.canvasGrid.deleteData();
		}
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	
	
}
