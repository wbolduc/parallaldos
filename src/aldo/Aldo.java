/*
 * WIlliam Bolduc
 * 0851313
 * Aldo.java
 * contains the main loop, handles file loading and work allocation
 */
package aldo;

import java.io.File;
import static java.lang.System.in;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author William Bolduc
 */
public class Aldo {
	static int cores;

	static String imgDir;
	static String aldoDir;
	
	static int aldoCount;

	static Img [] aldoList;
	//static ArrayList<Img> imgList = new ArrayList<Img>();

	
	
	public static void main(String[] args) throws InterruptedException {
		long time = System.currentTimeMillis();

		//check params
		try
		{
			aldoDir = args[0];
			if(aldoDir.charAt(aldoDir.length() - 1) != '\\')
			{
			    aldoDir = aldoDir + "\\";
			}
			
			imgDir = args[1];
			if(imgDir.charAt(imgDir.length() - 1) != '\\')
			{
			    imgDir = imgDir + "\\";
			}			
			cores = Integer.parseInt(args[2]);
		} catch(NumberFormatException e)
		{
			System.out.println("No core parameter given");
			return;
		}
		catch (IndexOutOfBoundsException e)
		{
			System.out.println("Missing some folder names");
			return;
		}
		
		//check folder contents (kinda)
		if (new File(aldoDir).list().length == 0)
		{
			System.out.println("No aldos in folder");
			return;
		}
		
		File targets = new File(imgDir);
		String[] targetList = targets.list();
		int targetCount = targetList.length;
		
		if (targetCount == 0)
		{
			System.out.println("No target in folder");
			return;			
		}
		
		//Print config
		//System.out.println("Images: " + imgDir);
		//System.out.println("Aldos : " + aldoDir);
		//System.out.println("Cores : " + cores);
		
		//load Aldos
		loadAldos(aldoDir);
		
		//make pool;
		ExecutorService pool = Executors.newFixedThreadPool(cores);
		
		//main loop, split to double buffer
		Flag newFlag;
		Flag currentFlag = new Flag();
		Img currentImg = new Img(imgDir, targetList[0]);
		
		for (Img a : aldoList)
		{
			pool.submit(new Searcher(currentImg, a, currentFlag));
		}

		for(int i = 1; i < targetCount; i++)
		{
			currentImg = new Img(imgDir, targetList[i]);
			newFlag = new Flag();
			
			currentFlag.waitForSolution();
			currentFlag = newFlag;
			
			for (Img a : aldoList)
			{
				pool.submit(new Searcher(currentImg, a, currentFlag));
			}	
		}
		currentFlag.waitForSolution();
		
		pool.shutdown();
		pool.awaitTermination( 1 , TimeUnit.DAYS);
		
		System.out.println(System.currentTimeMillis() - time);
		System.out.println(cores);
	}
	
	public static void loadAldos(String dir)
	{
		int i = 0;
		File imgFld = new File(dir);
		String[] aldoFileList = imgFld.list();
		
		aldoCount = aldoFileList.length * 4;		
		aldoList = new Img[aldoCount];
		
		for (String s : aldoFileList)
		{
			try
			{
				//System.out.println(dir + s);
				
				aldoList[i++] = new Img(dir, s);
				in.close();
				
				aldoList[i] = aldoList[i-1].rotated();
				i++;
				aldoList[i] = aldoList[i-1].rotated();
				i++;
				aldoList[i] = aldoList[i-1].rotated();
				i++;
			}
			catch (Exception e)
			{
				//Catch exception if any
				System.err.println("Error: " + e.getMessage());
			}
			finally
			{
				try
				{
					in.close();
				}
				catch (Exception e)
				{
					System.err.println("Error: " + e.getMessage());
				}
			}
		}
	}
}
