package bot;

import utils.Sleeper;

public abstract class AsyncTask {

	public abstract boolean loop();
	
	public abstract long getEta();
	
	public abstract float getProgress();
	
	public abstract void doStart();
	
	public abstract void onEnd(boolean forced);
	
	private boolean forceStop;
	
	private Thread thread;
	
	public boolean running() {
		if(thread == null)
			return false;
		return thread.isAlive();
	}
	
	public void start() {
		if(thread != null) {
			System.err.println("TASK ALREDY RUNNING");
			return;
		}
		thread = new Thread() {
			
			@Override
			public void run() {
				doStart();
				while(!loop())
					if(forceStop)break;
				onEnd(forceStop);
			}
			
		};
		thread.start();
	}
	
	public void stop() {
		forceStop = true;
		while(running())
			Sleeper.sleep(10);
		thread = null;
	}
	
}