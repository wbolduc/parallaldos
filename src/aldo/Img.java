/*
 * WIlliam Bolduc
 * 0851313
 * Img.java
 * The Img data struct, handles loading images and aldos from files and generating rotated versions of the contained Img
 */
package aldo;

import java.io.BufferedReader;
import java.io.FileReader;
import static java.lang.System.in;

/**
 *
 * @author William Bolduc
 */
public class Img {
	public String bareFileName;
	public int xSize;
	public int ySize;
	public int rotation = 0;
	public char[][] img;
	
	Img(int xSize, int ySize, char[][] img)
	{
		this.xSize = xSize;
		this.ySize = ySize;
		this.img = img;
	}
	
	Img(String dirPath, String bareFileName)
	{
		this.bareFileName = bareFileName;
		
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(dirPath + bareFileName));

			// get size of aldo
			String[] sizes = br.readLine().split("\\s+");
			ySize = Integer.parseInt(sizes[0]);
			xSize = Integer.parseInt(sizes[1]);

			img = new char[ySize][];

			//load aldo
			for (int i = 0; i < ySize; i++)
			{
				img[i] = br.readLine().toCharArray();
			}

			//Close the input stream
			in.close();
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
	
	public Img rotated()
	{
		char [][] newImg = new char[xSize][ySize];
		
		for(int y = 0; y < ySize; y++)
		{
			for(int x = 0; x < xSize; x++)
			{
				newImg[x][ySize - 1 - y] = img[y][x];
			}
		}
		
		Img r = new Img(ySize, xSize, newImg);
		r.rotation = rotation + 1;
		r.bareFileName = bareFileName;
		
		return r;
	}
	
	public void print()
	{
		System.out.println(xSize + "," + ySize);
		for(char[] c : img)
		{
			System.out.println(c);
		}
	}
}
