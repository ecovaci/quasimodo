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
package org.chess.quasimodo.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.chess.quasimodo.annotation.Action;
import org.chess.quasimodo.errors.FatalException;
import org.chess.quasimodo.event.CommandEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.util.ErrorHandler;


@Component
public class ApplicationController implements ApplicationListener<CommandEvent>, ApplicationContextAware {
	private Logger logger = LoggerFactory.getLogger(ApplicationController.class);
	
	private ApplicationContext applicationContext;
	
	@Autowired
	private ErrorHandler errorHandler;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	/**
	 * Handle <code>CommandEvent</code> event.<br>
	 * It scans classpath searching for Spring beans annotated with {@link org.springframework.stereotype.Controller} and calls
	 * all the {@link org.chess.quasimodo.annotation.Action} annotated methods.<br>
	 * <b>IMPORTANT:</b> If any of the method's call fails, no exception is thrown!<br>
	 * Depending on exception's type, the aplication might exit.
	 */
	public final void onApplicationEvent(CommandEvent event) {
		String commandName = event.getCommand().getName();
		outer:
		for (Object bean:applicationContext.getBeansWithAnnotation(Controller.class).values()) {
			for (Method method : bean.getClass().getMethods()) {
				if (method.getName().equals(commandName) 
						|| (method.getAnnotation(Action.class) != null 
								&& method.getAnnotation(Action.class).value().equals(event.getCommand()))) {
					//first, prepare arguments
					Class<?>[] paramTypes = method.getParameterTypes();
					Object[] args = new Object[paramTypes.length];
					for (int index = 0;index < paramTypes.length;index++) {
						if (paramTypes[index].isAssignableFrom(CommandEvent.class)) {
							args[index] = event;
							break;
						} 
					}
					//now invoke method
					try {
						MethodUtils.invokeMethod(bean, method.getName(), args);
					} catch (InvocationTargetException e) {
						logger.error("Target exception on calling method [" + method.getName() + "]", e);
						errorHandler.handleError(e.getCause());
					} catch (Exception e) {
						logger.error("Error on calling method [" + method.getName() + "]", e);
						errorHandler.handleError(new FatalException(e));
					} 
					break outer;
				}
			}
		}
	}
}
