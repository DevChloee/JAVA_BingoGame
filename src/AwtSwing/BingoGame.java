package AwtSwing;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class BingoGame {
	static JPanel panelNorth; // Top View
	static JPanel panelCenter; // Game View
	static JLabel labelMessage;
	static JButton[] buttons = new JButton[16];
	static String[] images = {
		"fruit01.png", "fruit02.png", "fruit03.png", "fruit04.png",
		"fruit05.png", "fruit06.png", "fruit07.png", "fruit08.png",
		"fruit01.png", "fruit02.png", "fruit03.png", "fruit04.png",
		"fruit05.png", "fruit06.png", "fruit07.png", "fruit08.png",
	};
	
	static int openCount = 0; //Opened Card Count: 0, 1, 2 
	static int buttonIndexSave1 = 0; //First Opened Card index : 0~15
	static int buttonIndexSave2 = 0; //Second Opened Card index " 0~15
	static Timer timer; //To give a time after make card turned
	static int tryCount = 0; // Number of trying
	static int successCount = 0; // Number of Bingo Count : 0~2
	//When same cards matched to each other, It will be counted as 1
	

	static class MyFrame extends JFrame implements ActionListener{
		public MyFrame(String title) {
			super(title);
			this.setLayout(new BorderLayout());
			this.setSize(400, 500);
			this.setVisible(true);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			initUI(this); // Screen UI Set.
			mixCard(); // Mix Fruit Card
			
			this.pack(); // Pack Empty Space.
		}
		
		/* Because of interface 'ActionListner',
		 * Implementing abstract method*/
		@Override
		public void actionPerformed(ActionEvent e){
			
			if(openCount == 2) {
				return;
			}
			
			// if button clicked, this sentence will be displayed in console window
			System.out.println("Button Clicked!");
			
			JButton btn = (JButton)e.getSource();
			// if button clicked, this will display index of the button
			int index = getButtonIndex( btn );
			System.out.println("Button Index:" + index);
			
			//change image when button clicked
			btn.setIcon(changeImage(images[index]));
			
			openCount++;
			if(openCount == 1) { //First Card?
				buttonIndexSave1 = index;
				
			} 
			else if(openCount == 2) { //Second Card?
				buttonIndexSave2 = index;
				tryCount++;
				labelMessage.setText("Find Same Fruit!" + "Try " + tryCount);
				
				//Judge Logic
				boolean isBingo = checkCard(buttonIndexSave1, buttonIndexSave2);
				if (isBingo == true) {
					openCount = 0;
					successCount++;
					/*If success count is 8, It means that all card matched with
					counterparts. So, It means Game over*/
					if(successCount == 8) {
						labelMessage.setText("Game Over " + "Try " +tryCount);
					}
				} else {
					// backToQuestion method which makes card back by turing it.
					backToQuestion();
					
				}
			}
		}
		
		/**
		 * This method will make card returned and it needs some time 
		 */
		public void backToQuestion() {
			// Timer 1Second
			timer = new Timer(1000, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("Timer.");
					
					openCount = 0;
					buttons[buttonIndexSave1].setIcon(changeImage("question.png"));
					buttons[buttonIndexSave2].setIcon(changeImage("question.png"));
					timer.stop();
				}
			});
			timer.start();
		}
		
		/**
		 * This method will check whether first card is same with second one or not
		 * @param index1 
		 * @param index2
		 * @return
		 */
		public boolean checkCard(int index1, int index2) {
			if(index1 == index2) {
				return false;
				
			}
			if(images[index1].equals(images[index2])) {
				return true;
			} else {
				return false;
			}
		}
		
		public int getButtonIndex(JButton btn) {
			int index = 0;
			for (int i=0; i<16; i++) {
				if(buttons[i] == btn) { // Same instance?
					index = i;
				}
			}
			return index;
		}
	}
	
	/**
	 * This method mix card randomly
	 */
	static void mixCard() {
		// use random class to use random function
		Random rand = new Random();
		for(int i=0; i<1000; i++) {
			int random = rand.nextInt(15) + 1; // 1~15
			//swap 
			String temp = images[0];
			images[0] = images[random];
			images[random] = temp;
		}
	}
	
	/**
	 * This method will set initial window when program executed
	 * @param myFrame
	 */
	static void initUI(MyFrame myFrame) {
		panelNorth = new JPanel();
		panelNorth.setPreferredSize(new Dimension(400,100));
		panelNorth.setBackground(Color.BLUE);
		labelMessage = new JLabel("Find Same Fruit!" + "Try 0");
		labelMessage.setPreferredSize(new Dimension(400, 100));
		labelMessage.setForeground(Color.WHITE);
		labelMessage.setFont(new Font("Monaco", Font.BOLD, 20));
		labelMessage.setHorizontalAlignment(JLabel.CENTER);
		panelNorth.add(labelMessage);
		myFrame.add("North", panelNorth);
		
		panelCenter = new JPanel();
		panelCenter.setLayout(new GridLayout(4,4));
		panelCenter.setPreferredSize(new Dimension(400,400));
		for(int i=0; i<16; i++) {
			buttons[i] = new JButton();
			buttons[i].setPreferredSize(new Dimension(100,100));
			buttons[i].setIcon( changeImage("question.png"));
			// put event to button
			buttons[i].addActionListener(myFrame);
			panelCenter.add(buttons[i]);
		}
		myFrame.add("Center", panelCenter);
		
	}
	
	/**
	 * This method will change image scale and return ImageIcon type
	 * @param filename
	 * @return icon_new
	 */
	static ImageIcon changeImage(String filename) {
		ImageIcon icon = new ImageIcon("./img/" + filename);
		Image originImage = icon.getImage();
		Image changedImage = originImage.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
		ImageIcon icon_new = new ImageIcon(changedImage);
		return icon_new;
	}
	
	public static void main(String[] args) {
		new MyFrame("Bingo Game");
		
	}

}
