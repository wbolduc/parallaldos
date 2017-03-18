/*
 * WIlliam Bolduc
 * 0851313
 * Searcher.java
 * The template matcher and also the runnable work class handed to the thread pool
 */
package aldo;

/**
 *
 * @author William Bolduc
 */
public class Searcher implements Runnable{
	
	Img targetImg;
	Img aldo;
	Flag flag;
	
	Searcher(Img targetImg, Img aldo, Flag flag)
	{
		this.targetImg = targetImg;
		this.aldo = aldo;
		this.flag = flag;
	}
	
	
	
	@Override
	public void run()
	{
		//System.out.println("Searching");
		int rightBound = targetImg.xSize - aldo.xSize;
		int bottomBound = targetImg.ySize - aldo.ySize;
		for (int y = 0; y <= bottomBound; y++)
		{
			if(flag.Found == true)
			{
				return;
			}
			for(int x = 0; x <= rightBound; x++)
			{
				if (scanForAldo(y, x, y + aldo.ySize, x + aldo.xSize))
				{
					flag.Found = true;
					flag.notifyWaiters();
					
					//fix output by rotation
					x++;
					y++;
					if (aldo.rotation == 1 || aldo.rotation == 2)
					{
						x+=aldo.xSize - 1;
					}
					if (aldo.rotation == 2 || aldo.rotation == 3)
					{
						y+=aldo.ySize - 1;
					}
					
					System.out.println("$" + aldo.bareFileName + " " + targetImg.bareFileName + " (" + y + "," + x + "," + (aldo.rotation * 90) + ")");
					return;
				}
			}
		}
	}
	
	private boolean scanForAldo(int yStart, int xStart, int bottomBound, int rightBound)
	{
		for(int y = yStart; y < bottomBound; y++)
		{
			for(int x = xStart; x < rightBound; x++)
			{
				if (targetImg.img[y][x] != aldo.img[y - yStart][x - xStart])
				{
					return false;
				}
			}
		}
		return true;
	}
	
}
