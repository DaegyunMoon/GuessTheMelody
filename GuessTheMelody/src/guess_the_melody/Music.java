package guess_the_melody;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import javax.swing.JOptionPane;

import javazoom.jl.player.Player;

public class Music extends Thread {
	
	File file;
	FileInputStream fileInputStream;
	BufferedInputStream bufferedInputStream;
	Player player;
	
	public Music(String s) {
		try {
			file = new File(Main.class.getResource("/Sounds/key" + s + ".mp3").toURI());
			fileInputStream = new FileInputStream(file);
			bufferedInputStream = new BufferedInputStream(fileInputStream);
			player = new Player(bufferedInputStream);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "사운드를 재생할 수 없습니다!", "음악재생 오류", JOptionPane.WARNING_MESSAGE);
		}
	}
	@Override
	public void run() {
		try {
			player.play();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "사운드를 재생할 수 없습니다!", "음악재생 오류", JOptionPane.WARNING_MESSAGE);
		}
	}
}
