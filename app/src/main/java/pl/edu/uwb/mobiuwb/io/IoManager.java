package pl.edu.uwb.mobiuwb.io;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

public class IoManager
{
    /**
     * Jest to zmienna przechowująca aktualny plik XML do parsowania.
     */
    public static final String PROPERTIES_XML_FILE_NAME = "properties.xml";
    public static final String CONFIGURATION_XML_FILE_NAME = "config.xml";

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

    public static void downloadFile(
            String fileLink,
            String destinationFileName,
            String destinationFolderPath)
            throws IOException
    {
        File file = new File(destinationFolderPath, destinationFileName);
        downloadFile(fileLink, file.getAbsolutePath());
    }


    public static void downloadFile(
            String fileLink,
            String destinationFileFullPath)
            throws IOException
    {
        URL u = new URL(fileLink);
        InputStream is = u.openStream();
        DataInputStream dataInputStream = new DataInputStream(is);

        File destination = new File(destinationFileFullPath);

        FileOutputStream destinationFileOutputStream = new FileOutputStream(
                destination);
        copyFile(dataInputStream, destinationFileOutputStream);
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

    public static boolean checkIfFileExists(String fullFilePath)
    {
        File file = new File(fullFilePath);
        return file.exists();
    }

    public static String getFileContent(
            String fullFilePath)
            throws IOException
    {
        FileInputStream fileInputStream = getFileInputStream(fullFilePath);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null)
        {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }

    public static FileInputStream getFileInputStream(
            String fullFilePath)
            throws FileNotFoundException
    {
        File file = new File(fullFilePath);
        return new FileInputStream(file);
    }
}
