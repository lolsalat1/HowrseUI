package bot;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JOptionPane;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import api.Breed;
import api.API.SERVER_COUNTRY;
import utils.Return;
import utils.Sleeper;


public class MainWindow {

	protected Shell shell;
	boolean breed = true,drink = true,stroke = true,groom = true,carrot = true,mash = true,suckle = true,feed = true,sleep = true,mission = true;
	private Text Console;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MainWindow window = new MainWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	

	/**
	 * Create contents of the window.
	 */
	private void createContents() {
		shell = new Shell();
		shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		shell.setSize(500, 400);
		shell.setText("SWT Application");
		shell.setLayout(new FormLayout());
		
		SERVER_COUNTRY locale = SERVER_COUNTRY.DE;
		String username = JOptionPane.showInputDialog("Username");
		String password = JOptionPane.showInputDialog("Password");
		Bot bot = new Bot(username, password, locale);
		
		Group grpActions = new Group(shell, SWT.NONE);
		grpActions.setText("Actions");
		grpActions.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		grpActions.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		grpActions.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		grpActions.setLayout(new GridLayout(5, false));
		FormData fd_grpActions = new FormData();
		fd_grpActions.left = new FormAttachment(0, 203);
		fd_grpActions.right = new FormAttachment(100, -10);
		fd_grpActions.top = new FormAttachment(0, 10);
		fd_grpActions.bottom = new FormAttachment(0, 152);
		grpActions.setLayoutData(fd_grpActions);
		
		Console = new Text(shell, SWT.BORDER | SWT.READ_ONLY | SWT.V_SCROLL | SWT.MULTI);
		FormData fd_Console = new FormData();
		fd_Console.left = new FormAttachment(grpActions, 0, SWT.LEFT);
		fd_Console.right = new FormAttachment(100, -10);
		Console.setLayoutData(fd_Console);

		
		Button Feed = new Button(grpActions, SWT.CHECK);
		Feed.setSelection(true);
		Feed.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				feed = !feed;
				Console.append("\nTest");
			}
		});
		Feed.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		Feed.setText("Feed");
		new Label(grpActions, SWT.NONE);
		
		Button Drink = new Button(grpActions, SWT.CHECK);
		Drink.setSelection(true);
		Drink.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				drink = !drink;
			}
		});
		Drink.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		Drink.setText("Drink");
		new Label(grpActions, SWT.NONE);
		
		Button Stroke = new Button(grpActions, SWT.CHECK);
		Stroke.setSelection(true);
		Stroke.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				stroke = !stroke;
			}
		});
		Stroke.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		Stroke.setText("Stroke");
		
		
		//STATIC
		Label lblNewLabel_0 = new Label(grpActions, SWT.NONE);
		lblNewLabel_0.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		lblNewLabel_0.setImage(SWTResourceManager.getImage(MainWindow.class, "/PNGs/feed.png"));
		new Label(grpActions, SWT.NONE);
		
		Label lblNewLabel_1 = new Label(grpActions, SWT.NONE);
		lblNewLabel_1.setImage(SWTResourceManager.getImage(MainWindow.class, "/PNGs/drink.png"));
		lblNewLabel_1.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		new Label(grpActions, SWT.NONE);
		
		
		Group grpBreeds = new Group(shell, SWT.NONE);
		grpBreeds.setText("Breeds");
		grpBreeds.setLayout(new GridLayout(1, false));
		
		FormData fd_grpBreeds = new FormData();
		fd_grpBreeds.top = new FormAttachment(0, 10);
		fd_grpBreeds.bottom = new FormAttachment(100, -10);
		fd_grpBreeds.right = new FormAttachment(0, 180);
		fd_grpBreeds.left = new FormAttachment(0, 10);
		grpBreeds.setLayoutData(fd_grpBreeds);
		
		Button Defaults = new Button(grpBreeds, SWT.RADIO);
		Defaults.setText("Defaults");
		//END STATIC
		
		bot.account.api.requests.setTimeout(200);
		bot.logger.printlevel = 1;	
		
		Return<HashMap<Integer,Breed>> b = bot.getBreeds();		
		if(!b.sucess) {
			Console.append("\nERROR!");
			System.exit(-1);
		}		
		
		

		Iterator<Breed> iterator = b.data.values().iterator();
		HashMap<String, java.awt.Button> buttons = new HashMap<String, Button>();
		
		while(iterator.hasNext()) {
			Breed breed = iterator.next();
			
			String name = breed.name;
			
			Button btn = new Button(grpBreeds, SWT.RADIO);
			btn.setText(name);
			
			buttons.put(name, btn);
			
			//	TODO - Für jeden Breed wird ein Button erstellt:
			//	Button [HIER DER BREED NAME] = new Button(grpBreeds, SWT.RADIO); <-- Dass geht nicht so wirklich so
			//	Defaults.setText("[BREED NAME]");
		}
		
		// um ein button mit name <name> zu bekommen: Button whatever = buttons.get(<name>);
		
		
		
		
		Button btnStartBot = new Button(shell, SWT.NONE);
		fd_Console.top = new FormAttachment(0, 221);
		btnStartBot.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Console.setText("Starting bot... ");					
							
				/*TODO
				Breed[] breeds = new Breed[b.data.size()];
				
				Iterator<Breed> br = b.data.values().iterator();
				
				for(int i = 0; i < b.data.size(); i++) {
					breeds[i] = br.next();
				};
				Breed s = (Breed) JOptionPane.showInputDialog(null, "Choose breed", "breed selector", JOptionPane.PLAIN_MESSAGE, null, breeds, "default");
				END TODO*/
				
				
				//Breed breed, boolean drink, boolean stroke, boolean groom, boolean carrot, boolean mash, boolean suckle, boolean feed, boolean sleep, boolean centreMission, long timeout, Bot bot, Runnable onEnd
				Return<BasicBreedTasksAsync> ret = bot.basicBreedTasks(0, drink, stroke, groom, carrot, mash, suckle, feed, sleep, mission, 500, new Runnable() {

				@Override
					public void run() {
						JOptionPane.showMessageDialog(null, "FINISHED!", "Bot", JOptionPane.PLAIN_MESSAGE);
						
						

						bot.logout();
					}
					
				});
				
				if(!ret.sucess) {
					Console.append("ERROR");
					System.exit(-1);
				}
				
				ret.data.start();
				
				//TODO
				while(ret.data.running()) {
					Sleeper.sleep(5000);
					if(ret.data.getProgress() == 1)
						break;
					Console.append("progress: " + (ret.data.getProgress() * 100) + "%");
					Console.append("ETA: " + (ret.data.getEta() / 1000) + "sec.");
				}
				
			}
		});
		FormData fd_btnStartBot = new FormData();
		fd_btnStartBot.left = new FormAttachment(grpActions, 0, SWT.LEFT);
		btnStartBot.setLayoutData(fd_btnStartBot);
		btnStartBot.setText("Start Bot");
		
		Button btnStopBot = new Button(shell, SWT.NONE);
		fd_btnStartBot.top = new FormAttachment(btnStopBot, 0, SWT.TOP);
		btnStopBot.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Console.append("Stopping bot...");
				bot.logout();
			}
		});
		FormData fd_btnStopBot = new FormData();
		fd_btnStopBot.right = new FormAttachment(grpActions, 0, SWT.RIGHT);
		btnStopBot.setLayoutData(fd_btnStopBot);
		btnStopBot.setText("Stop Bot");
			
		
		ProgressBar progressBar = new ProgressBar(shell, SWT.NONE);
		fd_Console.bottom = new FormAttachment(progressBar, -6);
		fd_btnStopBot.bottom = new FormAttachment(progressBar, -119);
		
		FormData fd_progressBar = new FormData();
		fd_progressBar.left = new FormAttachment(grpBreeds, 23);
		
		
		
		
		
		
		//STATIC
		Label lblNewLabel_2 = new Label(grpActions, SWT.NONE);
		lblNewLabel_2.setImage(SWTResourceManager.getImage(MainWindow.class, "/PNGs/stroke.png"));
		lblNewLabel_2.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		
		Button Groom = new Button(grpActions, SWT.CHECK);
		Groom.setSelection(true);
		Groom.setSelection(true);
		Groom.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				groom = !groom;
			}
		});
		Groom.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		Groom.setText("Groom");
		new Label(grpActions, SWT.NONE);
		
		Button Treat = new Button(grpActions, SWT.CHECK);
		Treat.setSelection(true);
		Treat.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				carrot = !carrot;
			}
		});
		Treat.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		Treat.setText("Treat");
		new Label(grpActions, SWT.NONE);
		
		Button Mash = new Button(grpActions, SWT.CHECK);
		Mash.setSelection(true);
		Mash.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				mash = !mash;
			}
		});
		Mash.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		Mash.setText("Mash");
		
		Label lblNewLabel_3 = new Label(grpActions, SWT.NONE);
		lblNewLabel_3.setImage(SWTResourceManager.getImage(MainWindow.class, "/PNGs/groom.png"));
		lblNewLabel_3.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		new Label(grpActions, SWT.NONE);
		
		Label lblNewLabel_4 = new Label(grpActions, SWT.NONE);
		lblNewLabel_4.setImage(SWTResourceManager.getImage(MainWindow.class, "/PNGs/carotte.png"));
		lblNewLabel_4.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		new Label(grpActions, SWT.NONE);
		
		Label lblNewLabel_5 = new Label(grpActions, SWT.NONE);
		lblNewLabel_5.setImage(SWTResourceManager.getImage(MainWindow.class, "/PNGs/mash.png"));
		lblNewLabel_5.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		
		Button btnSleep = new Button(grpActions, SWT.CHECK);
		btnSleep.setSelection(true);
		btnSleep.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				sleep = !sleep;
			}
		});
		btnSleep.setText("Sleep");
		new Label(grpActions, SWT.NONE);
		
		Button btnMission = new Button(grpActions, SWT.CHECK);
		btnMission.setSelection(true);
		btnMission.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				mission = !mission;
			}
		});
		btnMission.setText("Mission");
		new Label(grpActions, SWT.NONE);
		new Label(grpActions, SWT.NONE);

	}
}
