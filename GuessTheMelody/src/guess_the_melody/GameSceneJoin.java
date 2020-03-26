package guess_the_melody;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import guess_the_melody.GameSceneHost.ServerThread;
import guess_the_melody.User.gameState;

public class GameSceneJoin extends JFrame{

	Socket socket;
	DataInputStream dis;
	DataOutputStream dos;

	JPanel namePanel = new JPanel();
	JPanel textPanel = new JPanel();
	JPanel buttonPanel = new JPanel();
	JPanel keyPanelTop = new JPanel();
	JPanel keyPanelBottom = new JPanel();
	
	ImageIcon keyTop = new ImageIcon(Main.class.getResource("/Images/PianoBtn/Piano.png"));
	ImageIcon keyImg = new ImageIcon(Main.class.getResource("/Images/PianoBtn/Btn_basic.png"));
	ImageIcon[] keyPressed = { new ImageIcon(Main.class.getResource("/Images/PianoBtn/Btn_do.png")),
			new ImageIcon(Main.class.getResource("/Images/PianoBtn/Btn_re.png")), 
			new ImageIcon(Main.class.getResource("/Images/PianoBtn/Btn_mi.png")), 
			new ImageIcon(Main.class.getResource("/Images/PianoBtn/Btn_fa.png")), 
			new ImageIcon(Main.class.getResource("/Images/PianoBtn/Btn_so.png")), 
			new ImageIcon(Main.class.getResource("/Images/PianoBtn/Btn_la.png")), 
			new ImageIcon(Main.class.getResource("/Images/PianoBtn/Btn_si.png")), 
			new ImageIcon(Main.class.getResource("/Images/PianoBtn/Btn_do2.png"))};

	JLabel nameLabel;
	JLabel scoreLabel = new JLabel();
	JLabel topImg = new JLabel(keyTop);
	JLabel backImg = new JLabel();
	JLabel endImg = new JLabel();
	JLabel nameBackImg = new JLabel();
	JLabel textAreaImg = new JLabel();
	JLabel currentTime = new JLabel();
	JLabel scoreImg = new JLabel();
	JTextArea ta = new JTextArea();
	JButton[] keys = new JButton[8];
	JButton endKey = new JButton(new ImageIcon(Main.class.getResource("/Images/Song.png")));
	
	User me;
	TimeCounter timeCounter;
	String opponentName;
	
	public GameSceneJoin(String userName) {

		setTitle("방 찾기");
		setSize(640, 720);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);

		me = new User(userName, true);

		keyPanelBottom.setBounds(0, 585, 640, 100);
		keyPanelBottom.setLayout(new GridLayout(1, 8));
		for(int i = 0; i < 8; i++) {
			final int num = i;
			keys[i] = new JButton(keyImg);
			keys[i].setBorderPainted(false);
			keys[i].setContentAreaFilled(false);
			keys[i].setFocusPainted(false);
			keys[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					if(me.state == gameState.attack) {
						Music music = new Music(Integer.toString(num));
						music.start();
						keys[num].setIcon(keyPressed[num]);
						SendMessage(Integer.toString(num));
					} else if(me.state == gameState.defend) {
						Music music = new Music(Integer.toString(num));
						music.start();
						keys[num].setIcon(keyPressed[num]);
						me.myMelody.add(Integer.toString(num));
					}
				}
				public void mouseReleased(MouseEvent e) {
					keys[num].setIcon(keyImg);
				}
			});
			keyPanelBottom.add(keys[num]);
		}
		
		

		int v = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;

		endKey.setBounds(260, 340, 120, 70);
		endKey.setBorderPainted(false);
		endKey.setContentAreaFilled(false);
		endKey.setFocusPainted(false);
		endKey.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				endKey.setIcon(new ImageIcon(Main.class.getResource("/Images/SongEntered.png")));
			}
			public void mousePressed(MouseEvent e) {
				endKey.setIcon(new ImageIcon(Main.class.getResource("/Images/SongPressed.png")));
			}
			public void mouseReleased(MouseEvent e) {
				endKey.setIcon(new ImageIcon(Main.class.getResource("/Images/Song.png")));
				if(me.state == gameState.attack) {
					SendMessage("attackend");
					me.state = gameState.wait;
					ta.append(opponentName + " 님이 연주중입니다.\n");
				}
				if(me.state == gameState.defend) {
					if(checkMelody()) {
						ta.append("방어성공!" + "\n");
						SendMessage("defendend");
						me.successNum++;
						me.state = gameState.attack;
						me.recvMelody.clear();
						me.myMelody.clear();
						scoreLabel.setText(Integer.toString(me.successNum));
						ta.append(me.name + " 님의 차례입니다.\n");
					} else {
						ta.append("방어실패!\n게임에서 패배하였습니다.");
						me.state = gameState.lose;
						SendMessage("win");
						endKey.setVisible(false);
						endImg.setVisible(true);
					}
				}
			}
			public void mouseExited(MouseEvent e) {
				endKey.setIcon(new ImageIcon(Main.class.getResource("/Images/Song.png")));
			}
		});

		
		nameLabel = new JLabel(me.name);
		nameLabel.setForeground(SystemColor.text);
		nameLabel.setBounds(423, 13, 197, 70);
		nameLabel.setFont(new Font("굴림", Font.BOLD, 24));
		nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		scoreLabel.setFont(new Font("굴림", Font.BOLD, 18));
		scoreLabel.setText(Integer.toString(me.successNum));
		scoreLabel.setBounds(50, 100, 134, 60);
		scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
		topImg.setBounds(0, 485, 640, 100);
		nameBackImg.setIcon(new ImageIcon(GameSceneHost.class.getResource("/Images/Box_No/Namebox.png")));
		nameBackImg.setBounds(423, 13, 197, 70);
		textAreaImg.setIcon(new ImageIcon(GameSceneHost.class.getResource("/Images/Box_No/Systembox.png")));
		textAreaImg.setBounds(45, 150, 550, 114);
		endImg.setLocation(270, 325);
		endImg.setSize(100, 100);
		endImg.setIcon(new ImageIcon(Main.class.getResource("/Images/Box_No/end.png")));
		endImg.setVisible(false);
		scoreImg.setIcon(new ImageIcon(GameSceneJoin.class.getResource("/Images/Box_No/Scorebox.png")));
		scoreImg.setBounds(50, 100, 134, 74);
		
		ta.setEditable(false);
		ta.setOpaque(false);
		JScrollPane jScrollPane = new JScrollPane(ta, v, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPane.setLocation(50, 199);
		jScrollPane.setSize(540, 60);
		jScrollPane.setOpaque(false);
		jScrollPane.setFocusable(false);

		getContentPane().add(nameLabel);
		getContentPane().add(nameBackImg);
		getContentPane().add(scoreLabel);
		getContentPane().add(scoreImg);
		getContentPane().add(jScrollPane);
		getContentPane().add(textAreaImg);
		getContentPane().add(endImg);
		getContentPane().add(endKey);
		getContentPane().add(keyPanelBottom);
		getContentPane().add(topImg);
		backImg.setBounds(0, 0, 634, 685);
		backImg.setIcon(new ImageIcon(GameSceneHost.class.getResource("/Images/Gameback.png")));
		getContentPane().add(backImg);

		ClientThread clientThread = new ClientThread();
		clientThread.setDaemon(true);
		clientThread.start();
		
		setVisible(true);
	}
	
	class ClientThread extends Thread {
		@Override
		public void run() {
			try {
				socket = new Socket("127.0.0.1", 10001);
				
				dis = new DataInputStream(socket.getInputStream());
				dos = new DataOutputStream(socket.getOutputStream());
				opponentName = dis.readUTF();
				SendMessage(me.name);
				
				ta.append(opponentName + "님의 방에 접속하였습니다!\n");
				setTitle(opponentName.toString() + "님의 방");
				me.state = gameState.wait;
				ta.append(opponentName + " 님의 차례입니다.\n");

				while (true) {
					String msg = dis.readUTF();
					if(msg.equals("attackend")) {
						me.state = gameState.defend;
						ta.append(opponentName + " 님의 연주가 끝났습니다. 똑같이 연주하세요!\n");
					} else if(msg.equals("defendend")) {
						me.state = gameState.wait;
						ta.append(opponentName + " 님의 차례입니다.\n");
					} else if(msg.equals("win")) {
						me.state = gameState.win;
						ta.append("게임에서 승리하셨습니다!\n");
						endKey.setVisible(false);
						endImg.setVisible(true);
						break;
					} else {
						Music music = new Music(msg);
						music.start();
						me.recvMelody.add(msg);
					}
					ta.setCaretPosition(ta.getText().length());
				}

			} catch (UnknownHostException e) {
				ta.append("서버주소가 이상합니다.");
				ta.setCaretPosition(ta.getText().length());
			} catch (IOException e) {
				ta.append("서버와 연결이 끊어졌습니다.");
				ta.setCaretPosition(ta.getText().length());
			}

		}
	}
	
	void SendMessage(String msg) {
		String temp = ta.getText();
		ta.append(msg);
		ta.setCaretPosition(ta.getText().length());
		ta.setText(temp);
		Thread t = new Thread() {
			@Override
			public void run() {
				try {
					dos.writeUTF(msg);
					dos.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		t.start();
	}

	boolean checkMelody() {
		if(me.recvMelody.size() != me.myMelody.size()) {
			return false;
		} else {
			for(int i = 0; i < me.recvMelody.size(); i++) {
				if(!me.recvMelody.get(i).equals(me.myMelody.get(i))) {
					return false;
				}
			}
			return true;
		}
	}
}
