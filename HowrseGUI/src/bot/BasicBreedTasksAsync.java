package bot;

import java.util.Iterator;

import api.API;
import api.ApiException;
import api.Breed;
import api.horse.Horse;
import logging.Logger;
import utils.Sleeper;

public class BasicBreedTasksAsync extends AsyncTask{

	public BasicBreedTasksAsync(Breed breed, boolean drink, boolean stroke, boolean groom, boolean carrot, boolean mash, boolean suckle, boolean feed, boolean sleep, boolean centreMission, long timeout, Bot bot, Runnable onEnd) {
		horseAmmount = breed.horses.size();
		horses = breed.horses.values().iterator();
		this.timeout = timeout;
		taskAmmount = (drink ? 1 : 0) + (stroke ? 1 : 0) + (groom ? 1 : 0) + (carrot ? 1 : 0) + (mash ? 1 : 0) + (suckle ? 1 : 0) + (feed ? 1 : 0) + (sleep ? 1 : 0) + (centreMission ? 1 : 0);
		tasks = new boolean[] {drink, stroke, groom, carrot, mash, suckle, feed, sleep, centreMission};
		this.api = bot.account.api;
		this.onEnd = onEnd;
		logger = new Logger("[BREED][" + breed.name + "][" + bot.username + "]");
		curTask = -1;
		curHorse = horses.next();
	}
	
	public Logger logger;
	
	public long endTime;
	
	public int curTask;
	
	public Horse curHorse;
	
	public Iterator<Horse> horses;
	
	public int doneHorses;
	public int horseAmmount;
	public long timeout;
	public int taskAmmount;
	public API api;
	public Runnable onEnd;
	public boolean done;
	
	boolean[] tasks;
	
	public boolean doNextTask() {
		curTask ++;
		if(curTask >= tasks.length) {
			doneHorses ++;
			logger.infoln("Horse " + doneHorses + "/" + horseAmmount + " finished!");
			if(!horses.hasNext())
				return true;
			curHorse = horses.next();
			curTask = 0;
		}
		while(!tasks[curTask]) {
			if(curTask >= tasks.length) {
				doneHorses ++;
				logger.infoln("Horse " + doneHorses + "/" + horseAmmount + " finished!");
				if(!horses.hasNext())
					return true;
				curHorse = horses.next();
				curTask = -1;
			}
			curTask ++;
		}
		if (doTask(curTask))
			Sleeper.sleep(timeout);
		return false;
	}

	public boolean doTask(int id) {
		try {
			curHorse.updateHorse(api);
		if(id == 0) {
			if(!curHorse.tasks.drink.available)
				return false;
			
			if(curHorse.tasks.drink.performTask(curHorse, api).sucess) {
				logger.debugln("Horse " + curHorse.id + " drink sucess");
			} else {
				logger.warnln("Horse " + curHorse.id + " drink failed");
			}
			return true;
		} else if (id == 1) {
			if(!curHorse.tasks.stroke.available)
				return false;
			
			if(curHorse.tasks.stroke.performTask(curHorse, api).sucess) {
				logger.debugln("Horse " + curHorse.id + " stroke sucess");
			} else {
				logger.warnln("Horse " + curHorse.id + " stroke failed");
			}
			return true;
		} else if (id == 2) {
			if(!curHorse.tasks.groom.available)
				return false;
			
			if(curHorse.tasks.groom.performTask(curHorse, api).sucess) {
				logger.debugln("Horse " + curHorse.id + " groom sucess");
			} else {
				logger.warnln("Horse " + curHorse.id + " groom failed");
			}
			return true;
			
		} else if (id == 3) {
			if(!curHorse.tasks.carrot.available)
				return false;
			
			if(curHorse.tasks.carrot.performTask(curHorse, api).sucess) {
				logger.debugln("Horse " + curHorse.id + " carrot sucess");
			} else {
				logger.warnln("Horse " + curHorse.id + " carrot failed");
			}
			return true;
		} else if (id == 4) {
			if(!curHorse.tasks.mash.available)
				return false;
			
			if(curHorse.tasks.mash.performTask(curHorse, api).sucess) {
				logger.debugln("Horse " + curHorse.id + " mash sucess");
			} else {
				logger.warnln("Horse " + curHorse.id + " mash failed");
			}
			return true;
		} else if (id == 5) {
			if(!curHorse.tasks.suckle.available)
				return false;
			
			if(curHorse.tasks.suckle.performTask(curHorse, api).sucess) {
				logger.debugln("Horse " + curHorse.id + " suckle sucess");
			} else {
				logger.warnln("Horse " + curHorse.id + " suckle failed");
			}
			return true;
		} else if (id == 6) {
			if(!curHorse.tasks.feed.isSuggested())
				return false;
			
			if(curHorse.tasks.feed.performTask(curHorse, api).sucess) {
				logger.debugln("Horse " + curHorse.id + " feed sucess");
			} else {
				logger.warnln("Horse " + curHorse.id + " feed failed");
			}
			return true;
			
		} else if (id == 7) {
			if(!curHorse.tasks.sleep.available)
				return false;
			
			if(curHorse.tasks.sleep.performTask(curHorse, api).sucess) {
				logger.debugln("Horse " + curHorse.id + " sleep sucess");
			} else {
				logger.warnln("Horse " + curHorse.id + " sleep failed");
			}
			return true;
		} else if (id == 8) {
			if(!curHorse.tasks.centreMission.available)
				return false;
			
			if(curHorse.tasks.centreMission.performTask(curHorse, api).sucess) {
				logger.debugln("Horse " + curHorse.id + " centreMission sucess");
			} else {
				logger.warnln("Horse " + curHorse.id + " centreMission failed");
			}
			return true;
		} else {
			return false;
		}
		} catch (ApiException e) {
			logger.warnln("Couldn't do task " + id + " for horse " + curHorse.id);
			logger.warnEx(e);
			return true;
		}
	}
	
	@Override
	public boolean loop() {
		long s = System.currentTimeMillis();
		done = doNextTask();
		endTime += System.currentTimeMillis() - s - timeout;
		return done;
	}

	@Override
	public long getEta() {
		return endTime - System.currentTimeMillis();
	}

	@Override
	public float getProgress() {
		if(done)
			return 1;
		return (float)(doneHorses*taskAmmount + curTask) / (float)(horseAmmount * taskAmmount);
	}

	@Override
	public void doStart() {
		endTime = System.currentTimeMillis() + timeout * horseAmmount * taskAmmount;
	}

	@Override
	public void onEnd(boolean forced) {
		if(forced) {
			logger.infoln("Task was forced to end");
		} else {
			logger.infoln("Task ended");
		}
		
		onEnd.run();
	}

	
	
}