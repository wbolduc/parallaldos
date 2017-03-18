/*
 * WIlliam Bolduc
 * 0851313
 * Flag.java
 * Just a flag to indicate when a waldo is found, also contains a wait notify routine for Aldo.java to use
 */
package aldo;

/**
 *
 * @author William Bolduc
 */
public class Flag {
	public volatile boolean Found = false;
	
	public synchronized void waitForSolution()
	{
		while (!Found)
		{
			try
			{
				wait();
			}
			catch (InterruptedException e){}
		}
	}
	
	public synchronized void notifyWaiters()
	{
		this.notifyAll();
	}
	
}
