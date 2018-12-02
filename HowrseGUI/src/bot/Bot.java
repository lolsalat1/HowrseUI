package bot;

import java.util.HashMap;

import api.API;
import api.API.SERVER_COUNTRY;
import api.Account;
import api.ApiException;
import api.Breed;
import api.request.requests.DefaultResponse;
import logging.Logger;
import utils.Return;

public class Bot {

	public Bot(String username, String password, SERVER_COUNTRY locale) {
		this.username = username;
		this.password = password;
		account = new Account();
		account.api = new API(locale);
		logger = new Logger("[Bot_" + username + "]");
	}
	
	public Account account;
	
	public Logger logger;
	public String username, password;
	public boolean loggedIn;
	public boolean forceStop;
	
	public Return<BasicBreedTasksAsync> basicBreedTasks(int id, boolean drink, boolean stroke, boolean groom, boolean carrot, boolean mash, boolean suckle, boolean feed, boolean sleep, boolean centreMission, long timeout, Runnable onEnd) {
		if(!loggedIn)
			if(!login()) 
				return new Return<BasicBreedTasksAsync>(null, false);
		Return<Breed> res = getBreed(id, true);
		if(!res.sucess)
			return new Return<BasicBreedTasksAsync>(null, false);
		BasicBreedTasksAsync task = new BasicBreedTasksAsync(res.data, drink, stroke, groom, carrot, mash, suckle, feed, sleep, centreMission, timeout, this, onEnd);
		return new Return<BasicBreedTasksAsync>(task, true);
	}
	
	public Return<Breed> getBreed(int id, boolean update) {
			Breed breed;
			
			if(account.breeds.containsKey(id)) {
				logger.debugln("Getting cached breed " + id + " for " + username);
				breed = account.breeds.get(id);
			} else {
				logger.debugln("No cached breed " + id + " for " + username);
				getBreeds();
				if(!account.breeds.containsKey(id)) {
					logger.warnln("Breed " + id + " does not exist for " + username);
					return new Return<Breed>(null, false);
				} else {
					breed = account.breeds.get(id);
				}
			}
			boolean sucess = breed != null;
			if(sucess) {
				logger.debugln("Got breed " + id + " for " + username + " sucessfully");
				if(update) {
					try {
						breed.updateHorses(account.api);
						logger.debugln("Updated breed " + id + " for " + username + " sucessfully");
					} catch(ApiException e) {
						logger.warnln("Couldn't update breed " + id + " for " + username);
						logger.warnEx(e);
					}
				}
			}
				
			else
				logger.warnln("Breed " + id + " for " + username + " returned null");
			return new Return<Breed>(breed, sucess);
	}
	
	public Return<HashMap<Integer, Breed>> getBreeds(){
		try {
			HashMap<Integer, Breed> breeds = account.api.getBreeds();
			
			boolean sucess = !breeds.isEmpty();
			if(sucess) {
				account.breeds = breeds;
				logger.debugln("Got breeds sucessfully for " + username);
			} else
				logger.warnln("Couldn't get breeds for " + username);
			return new Return<HashMap<Integer, Breed>>(breeds, sucess);
		} catch(ApiException e) {
			logger.warnln("Couldn't get breeds for " + username);
			logger.warnEx(e);
			return new Return<HashMap<Integer, Breed>>(null, false);
		}
	}
	
	public boolean login() {
		try {
			DefaultResponse response = account.api.doLogin(username, password);
			if(response.sucess) {
				logger.infoln("Logged in as " + username);
				logger.debugln("sessionprod: " + account.api.sessionprod);
				return true;
			} else {
				logger.errorln("Couldn't log in as " + username);
				logger.debugln(response);
				return false;
			}
		} catch(ApiException e) {
			logger.errorln("Couldn't log in as " + username);
			logger.errorEx(e);
			return false;
		}
	}

	public void logout() {
		try {
			DefaultResponse response = account.logout();
			if(!response.sucess) {
				logger.warnln("Couldn't log out");
				return;
			}
			logger.infoln("Logged out sucessfully");
			loggedIn = false;
		} catch(ApiException e) {
			logger.warnln("Logout failed");
			logger.warnEx(e);
		}
		
		
	}
	
}