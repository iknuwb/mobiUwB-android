package com.example.mobiuwb.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

import android.util.Log;

public class IoManager {
	/**
	 * Jest to zmienna przechowująca aktualny plik XML do parsowania.
	 */
	public static final String PROPERTIES_XML_FILE_NAME = "properties.xml";

	public static boolean copyAssetsFile(
			InputStream assetsImputStream,
			String assetsFileName, 
			String destinationFolderPath) 
	{
		try 
		{
			File destinationFolderFile = new File(destinationFolderPath);
			if (destinationFolderFile.canWrite()) 
			{
				File destination = new File(
						destinationFolderPath, 
						assetsFileName);

				FileOutputStream destinationFileOutputStream = new FileOutputStream(
						destination);
				
				copyFile(assetsImputStream, destinationFileOutputStream);
				
				destinationFileOutputStream.close();
			}
			return true;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return false;
		}
	}
	
	private static void copyFile(InputStream in, OutputStream out) throws IOException 
	{
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) 
		{
			out.write(buffer, 0, read);
		}
	}
}
