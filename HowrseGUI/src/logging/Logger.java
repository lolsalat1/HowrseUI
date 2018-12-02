package logging;

public class Logger {

	public Logger(String name) {
		this.name = name;
	}
	
	public String name;
	public int printlevel = 0;
	
	public void infoln(Object text) {
		if(printlevel <= 1) {
			System.out.println(name + "[INFO]: " + text);
		}
	}
	
	public void warnln(Object text) {
		if(printlevel <= 2) {
			System.out.println(name + "[WARN]: " + text);
		}
	}
	
	public void debugln(Object text) {
		if(printlevel <= 0) {
			System.out.println(name + "[DEBUG]: " + text);
		}
	}
	
	public void errorln(Object text) {
		if(printlevel <= 3) {
			System.err.println(name + "[ERROR]: " + text);
		}
	}
	
	public void errorEx(Exception e) {
		if(printlevel <= 3) {
			e.printStackTrace();
		}
	}
	
	public void warnEx(Exception e) {
		if(printlevel <= 2) {
			e.printStackTrace();
		}
	}
	
}