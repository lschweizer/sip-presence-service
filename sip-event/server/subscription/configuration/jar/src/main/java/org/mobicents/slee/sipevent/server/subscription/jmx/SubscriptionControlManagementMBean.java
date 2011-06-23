/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.slee.sipevent.server.subscription.jmx;

/**
 * JMX Configuration of the SIP Event Subscription Control.
 * 
 * @author martins
 * 
 */
public interface SubscriptionControlManagementMBean {

	public static final String MBEAN_NAME = "org.mobicents.sippresence:name=SipEventSubscriptionControl";

	/**
	 * Retrieves default subscription time in seconds.
	 * 
	 * @return
	 */
	public int getDefaultExpires();

	/**
	 * Defines default subscription time in seconds.
	 * 
	 * @param defaultExpires
	 */
	public void setDefaultExpires(int defaultExpires);

	/**
	 * Retrieves maximum subscription time in seconds.
	 * 
	 * @return
	 */
	public int getMaxExpires();

	/**
	 * Defines maximum subscription time in seconds.
	 * 
	 * @param maxExpires
	 */
	public void setMaxExpires(int maxExpires);

	/**
	 * Retrieves minimum subscription time in seconds.
	 * 
	 * @return
	 */
	public int getMinExpires();

	/**
	 * Defines minimum subscription time in seconds.
	 * 
	 * @param maxExpires
	 */
	public void setMinExpires(int minExpires);

	/**
	 * Retrieves default expires value, in seconds, to keep a subscription in
	 * waiting state.
	 * 
	 * @return
	 */
	public int getDefaultWaitingExpires();

	/**
	 * Defines default expires value, in seconds, to keep a subscription in
	 * waiting state.
	 * 
	 * @param defaultWaitingExpires
	 */
	public void setDefaultWaitingExpires(int defaultWaitingExpires);

	/**
	 * Retrieves Max-Forwards header value for generated NOTIFY requests.
	 * 
	 * @return
	 */
	public int getMaxForwards();

	/**
	 * Defines Max-Forwards header value for generated NOTIFY requests.
	 * 
	 * @param maxForwards
	 */
	public void setMaxForwards(int maxForwards);

	/**
	 * Retrieves the display name used in contact header's addresses.
	 * 
	 * @return
	 */
	public String getContactAddressDisplayName();

	/**
	 * Defines the display name used in contact header's addresses.
	 * 
	 * @param contactAddressDisplayName
	 */
	public void setContactAddressDisplayName(String contactAddressDisplayName);

	/**
	 * Indicates if event list support is on, that is, this server acts as
	 * Resource List Server too
	 * 
	 * @return
	 */
	public boolean getEventListSupportOn();

	/**
	 * Turns on/off event list support, that is, if this server acts as Resource
	 * List Server too
	 * 
	 * @param eventListSupportOn
	 */
	//public void setEventListSupportOn(boolean eventListSupportOn);

	/**
	 * Retrieves the TerminationIOI parameter of PChargingVector header, to be
	 * used on SUBSCRIBE responses and NOTIFY requests in a IMS environment.
	 * 
	 * @return
	 */
	public String getPChargingVectorHeaderTerminatingIOI();

	/**
	 * Defines the TerminationIOI parameter of PChargingVector header, to be
	 * used on SUBSCRIBE responses and NOTIFY requests in a IMS environment.
	 * 
	 * @param chargingVectorHeaderTerminatingIOI
	 */
	public void setPChargingVectorHeaderTerminatingIOI(
			String chargingVectorHeaderTerminatingIOI);
}
