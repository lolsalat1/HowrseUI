package utils;

public class Sleeper {

	public static void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch(Exception e) {
			// NOT GONNA HAPPEN
		}
	}
	
}
