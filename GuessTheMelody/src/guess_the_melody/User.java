package guess_the_melody;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class User {
	enum gameState { wait, attack, defend, win, lose };
	gameState state;
	String name;
	int successNum;
	boolean isHost;
	ArrayList<String> recvMelody;
	ArrayList<String> myMelody;
	
	User(String name, boolean isHost) {
		this.name = name;
		this.successNum = 0;
		this.isHost = isHost;
		this.state = gameState.wait;
		recvMelody = new ArrayList<String>();
		myMelody = new ArrayList<String>();
	}
}
