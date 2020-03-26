package guess_the_melody;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.InputStream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class MainScene extends JFrame{
	
	JLabel titleLabel;
	JLabel nameLabel = new JLabel("이름 ");
	JTextField nameText = new JTextField(10);

	ImageIcon titleImg = new ImageIcon(Main.class.getResource("/Images/Title.png"));
	ImageIcon createImg = new ImageIcon(Main.class.getResource("/Images/createButton.png"));
	ImageIcon createEnteredImg = new ImageIcon(Main.class.getResource("/Images/createButtonEntered.png"));
	ImageIcon joinImg = new ImageIcon(Main.class.getResource("/Images/joinButton.png"));
	ImageIcon joinEnteredImg = new ImageIcon(Main.class.getResource("/Images/joinButtonEntered.png"));
	
	JButton createButton = new JButton(createImg);
	JButton joinButton = new JButton(joinImg);
	
	public MainScene() {
		setTitle("옆집의 음대생");
		setBackground(null);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		createButton.setBounds(132, 461, 182, 78);
		createButton.setBorderPainted(false);
		createButton.setContentAreaFilled(false);
		createButton.setFocusPainted(false);
		createButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				createButton.setIcon(createEnteredImg);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				createButton.setIcon(createImg);
			}
			@Override
			public void mousePressed(MouseEvent e) {
				if(nameText.getText().trim().length() == 0) {
					JOptionPane.showMessageDialog(null, "이름을 입력하세요!", "방 만들기", JOptionPane.WARNING_MESSAGE);
				} else {
					String userName = nameText.getText();
					new GameSceneHost(userName);
				}
			}
		});
		joinButton.setBounds(319, 461, 182, 78);
		joinButton.setBorderPainted(false);
		joinButton.setContentAreaFilled(false);
		joinButton.setFocusPainted(false);
		joinButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				joinButton.setIcon(joinEnteredImg);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				joinButton.setIcon(joinImg);
			}
			@Override
			public void mousePressed(MouseEvent e) {
				if(nameText.getText().trim().length() == 0) {
					JOptionPane.showMessageDialog(null, "이름을 입력하세요!", "방 참여", JOptionPane.WARNING_MESSAGE);
				} else {
					String userName = nameText.getText();
					new GameSceneJoin(userName);
				}
			}
		});
		
		titleLabel = new JLabel(titleImg, SwingConstants.CENTER);
		titleLabel.setBounds(0, 0, 634, 228);
		
		JLabel backImg = new JLabel();
		backImg.setIcon(new ImageIcon(MainScene.class.getResource("/Images/Mainback.png")));
		backImg.setBounds(0, 0, 634, 684);
		
		nameLabel.setForeground(Color.WHITE);
		nameLabel.setBounds(240, 250, 160, 40);
		nameLabel.setFont(new Font("굴림", Font.BOLD, 24));
		nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(createButton);
		getContentPane().add(joinButton);
		getContentPane().add(nameLabel);
		
		
		JLabel nameBackImg = new JLabel();
		nameBackImg.setIcon(new ImageIcon(MainScene.class.getResource("/Images/Box_No/Scanbox.png")));
		nameBackImg.setBounds(140, 275, 360, 108);
		nameText.setHorizontalAlignment(SwingConstants.CENTER);
		nameText.setForeground(Color.WHITE);
		nameText.setBounds(160, 295, 320, 80);
		nameText.setOpaque(false);
		nameText.setFont(new Font("굴림", Font.BOLD, 36));
		nameText.setBorder(null);
		nameBackImg.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(nameText);
		getContentPane().add(nameBackImg);
		getContentPane().add(titleLabel);
		getContentPane().add(backImg);

		setSize(640, 720);
		setVisible(true);
	}

	public void paintComponent(Graphics g) {
		Image backImg = new ImageIcon(Main.class.getResource("/Images/Mainback.png")).getImage();
		g.drawImage(backImg, 0, 0, this);
	}
}
