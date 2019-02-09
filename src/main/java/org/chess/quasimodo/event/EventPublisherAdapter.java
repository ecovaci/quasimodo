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
package org.chess.quasimodo.event;

import org.chess.quasimodo.domain.logic.Form;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;


@Component ("eventPublisher")
public class EventPublisherAdapter implements
		ApplicationEventPublisherAware {
	
	private ApplicationEventPublisher applicationEventPublisher;
	
	/**
	 * It calls: {@link org.springframework.context.ApplicationEventPublisherAware#publishEvent(ApplicationEvent)} 
	 */
	public void publishEvent (ApplicationEvent event) {
    	this.applicationEventPublisher.publishEvent(event);
    }

	@Override
	public void setApplicationEventPublisher(
			ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}

	public void publishCommandEvent (Object source, Form<?> form, CommandEvent.Command type) {
    	this.applicationEventPublisher.publishEvent(new CommandEvent(source, form, type));
    }
	
	public void publishCommandEvent (Object source, CommandEvent.Command type) {
    	this.applicationEventPublisher.publishEvent(new CommandEvent(source, type));
    }
	
}
