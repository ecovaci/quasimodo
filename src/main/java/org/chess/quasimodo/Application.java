/*******************************************************************************
 * Quasimodo - a chess interface for playing and analyzing chess games.
 * Copyright (C) 2011 Eugen Covaci.
 * All rights reserved.
 *  
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 ******************************************************************************/
/**
 * Quasimodo - a chess interface for playing and analyzing chess games.
 * More information is available at <my.link>.
 * Copyright (C) 2011 Eugen Covaci.
 * All rights reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.chess.quasimodo;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chess.quasimodo.application.Constants;
import org.chess.quasimodo.application.QuasimodoContext;
import org.chess.quasimodo.config.Config;
import org.chess.quasimodo.database.OpeningDao;
import org.chess.quasimodo.database.PGNDatabaseDao;
import org.chess.quasimodo.domain.logic.Game;
import org.chess.quasimodo.engine.EngineRepository;
import org.chess.quasimodo.errors.FatalException;
import org.chess.quasimodo.gui.EnginePanel;
import org.chess.quasimodo.gui.MainFrame;
import org.chess.quasimodo.gui.PromotionDialog;
import org.chess.quasimodo.gui.model.PromotionDialogModel;
import org.chess.quasimodo.message.MessageHandler;
import org.chess.quasimodo.service.DatabaseService;
import org.chess.quasimodo.util.LocalIOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;


/**
 * The main class of the application.
 */

@Configuration
@ComponentScan(basePackages = "org.chess.quasimodo")
public class Application implements ApplicationContextAware {

	private static final Logger logger = LoggerFactory.getLogger(Application.class);
	
	@Autowired
	private MainFrame frame;
	
	@Autowired
	private EngineRepository engineRepository;
	
	@Autowired
	private MessageHandler messageHandler;
	
	@Autowired
	private DatabaseService databaseService;
	
	@Autowired
	private QuasimodoContext context;
	
	@Autowired
	private Config config;
	
	private AbstractApplicationContext springContext;

	@Bean
	EnginePanel engineView () {
		return new EnginePanel();
	}

	@Bean
	EnginePanel extraEngineView () {
		return new EnginePanel();
	}

	@Bean
	PromotionDialog whitePromotionDialog () {
		return new PromotionDialog(new PromotionDialogModel(true));
	}

	@Bean
	PromotionDialog blackPromotionDialog () {
		return new PromotionDialog(new PromotionDialogModel(false));
	}

	@Bean
	HikariDataSource openingDataSource () {
		HikariDataSource dataSource = new HikariDataSource();
		dataSource.setDriverClassName("org.h2.Driver");
		dataSource.setJdbcUrl("jdbc:h2:~/.quasimodo/database/eco");
		return dataSource;
	}

	@Bean
	HikariDataSource import_1DataSource () {
		HikariDataSource dataSource = new HikariDataSource();
		dataSource.setDriverClassName("org.h2.Driver");
		dataSource.setJdbcUrl("jdbc:h2:~/.quasimodo/database/import_1");
		return dataSource;
	}

	@Bean
	OpeningDao openingDao () {
		OpeningDao openingDao = new OpeningDao();
		openingDao.setDataSource(openingDataSource());
		return openingDao;
	}

	@Bean
	PGNDatabaseDao pgnDatabaseDao () {
		PGNDatabaseDao pgnDatabaseDao = new PGNDatabaseDao();
		pgnDatabaseDao.setDataSource(import_1DataSource());
		return pgnDatabaseDao;
	}

	private void setupGUI() throws FatalException {
    	FutureTask<Object> showFrameTask = new FutureTask<Object>(new Callable<Object>() {
			@Override
			public Object call() {
				frame.addWindowListener(new ExitListener());
				frame.pack();
				frame.setVisible(true);
				return null;
			}
		});
    	
    	SwingUtilities.invokeLater(showFrameTask);
    	try {
			showFrameTask.get();
		} catch (Exception e) {
			logger.error("", e);
			throw new FatalException("Execution error on building application's main frame", e);
		}
    }
    
	private static void checkAppHomeDirectory () throws  FatalException {
		File appHomeDir = LocalIOUtils.getAppHomeDirectory();
		if (appHomeDir.exists()) {
			if (appHomeDir.isDirectory()) {
				logger.info("Application's home directory already exist, no need to create it");
			} else {
				throw new FatalException("TODO");
			}
		} else {
			logger.info("Application's home directory does not exist, let's fix this");
			logger.info("Create a new  application's home directory ...");
			if (appHomeDir.mkdir()) {
				logger.info("Application home directory created");
			} else {
				throw new FatalException ("Cannot create application's home directory");
			}
			logger.info("Copy the configuration files to application's home directory");
			try {
				FileUtils.copyURLToFile( 
						Thread.currentThread().getContextClassLoader()
						      .getResource("system/defaults/design.properties"), 
						      new File( LocalIOUtils.getAppHomeDirectory().getAbsolutePath() 
				    			+ File.separator + "design.properties"));
				logger.info("Design file copied.");
				FileUtils.copyURLToFile( 
						Thread.currentThread().getContextClassLoader()
						      .getResource("system/defaults/config.properties"), 
						      new File( LocalIOUtils.getAppHomeDirectory().getAbsolutePath() 
				    			+ File.separator + "config.properties"));
				logger.info("Config file copied.");
			} catch (IOException e) {
				throw new FatalException ("Cannot copy configutation files into application's home directory", e);
			}
		}
	}
	
	/**
	 * Check to see if the ECO database file exist. <br>
	 * If true does nothing, else the ECO database is created.
	 */
	private void checkECODatabase () {
		if (config.getEcoFile().exists()) {
			logger.info("Eco database already exist, no need to create it");
		} else {
			logger.info("No Eco database found, let's create it");
			databaseService.generateECO();
		}
	}
	
	private class ExitListener extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
        	int option = messageHandler.showConfirm(frame, "Do you want to leave the application?");
        	if (option == JOptionPane.OK_OPTION) {
        		 exit();
        	}
        }
    }

    public void exit () {
		//fire game.end event
    	//disconect from fics (if connected)
		//stop engines
    	//etc
    	if (context.existCurrentGame()) {
    		context.getCurrentGame().stop(Game.Status.UNDECIDED);
    	}
    	engineRepository.closeEngines();
    	springContext.close();
    	logger.info("Application stopped at: " + new Date());
    }   
    
    public void launch (String[] args) {
    	try {
			setupGUI();
		} catch (Throwable e) {
			logger.trace("", e);
			messageHandler.showErrorFullTrace("Fatal error on launching the application's GUI!", e);
			System.exit(Constants.ERR_CODE_STARTUP_GUI);
		}
    }
    
    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
    	logger.info("Application started at: " + new Date());
    	
    	//check requirements (application home directory and design/config files)
    	try {
    		checkAppHomeDirectory();
		} catch (Throwable e) {
            MessageHandler.showErrorTrace("Fatal error when starting application", e);			
			logger.error("Fatal error when starting application", e);
			System.exit(Constants.ERR_CODE_STARTUP_APP);
		}
    	
		//bootstrap Spring framework
		AbstractApplicationContext applicationContext = null;
		try {
			applicationContext = new AnnotationConfigApplicationContext(Application.class);
		} catch (Throwable e) {
			logger.error("Fatal error when starting Spring framework's container", e);
			MessageHandler.showErrorTrace("Fatal error when starting Spring framework's container", e);			
			System.exit(Constants.ERR_CODE_STARTUP_SPRING);
			
		}
		
		//get the application instance
    	Application application = (Application)applicationContext.getBean(Application.class);
    	
    	try {
			application.checkECODatabase();
		} catch (Exception e) {
			MessageHandler.showErrorTrace("Fatal error when checking ECO database", e);			
			logger.error("Fatal error when starting Spring framework's container", e);
			System.exit(Constants.ERR_CODE_ECO);
		}
    	
    	//create and show the main frame
    	application.launch(args);
    }

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.springContext = (AbstractApplicationContext)applicationContext;
	}
}
