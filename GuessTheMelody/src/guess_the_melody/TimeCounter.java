package guess_the_melody;

import java.util.Timer;
import java.util.TimerTask;

public class TimeCounter {

	static int interval = 3;
	static Timer timer;
	static int delay = 1000;
	static int period = 1000;
	
	TimeCounter instance;
	
	public TimeCounter() {
		instance = this;
		timer = new Timer();
	}
	
	public static void Start() {
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				SetInterval();
			}
		}, delay, period);
	}
	
	private static int SetInterval() {
		if(interval == 0) {
			Reset();
		}
		return --interval;
	}
	private static void Reset() {
		interval = 3;
	}
}
