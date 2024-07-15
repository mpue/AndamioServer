package org.pmedv.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.beans.objects.DirectoryObject;


public class ZipUtils {
	
    protected static final Log log = LogFactory.getLog(ZipUtils.class);
    
    private static final byte[] buffer = new byte[0xFFFF];
	
    public static void createZipArchive(String zipFilename, String sourceDir, String destDir, ArrayList<DirectoryObject> fileList) {

        byte[] buffer = new byte[18024];

        // Specify zip file name

        try {

            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(destDir + zipFilename));

            // Set the compression ratio

            out.setLevel(Deflater.BEST_COMPRESSION);

            for (Iterator<DirectoryObject> fileIterator = fileList.iterator(); fileIterator.hasNext();) {
            	
                DirectoryObject dirObj = fileIterator.next();

                FileInputStream in = new FileInputStream(sourceDir + dirObj.getName());

                // Add ZIP entry to output stream.
                out.putNextEntry(new ZipEntry(dirObj.getName()));

                // Transfer bytes from the current file to the ZIP file
                //out.write(buffer, 0, in.read(buffer));

                int len;

                while ((len = in.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }

                // Close the current entry
                out.closeEntry();

                // Close the current file input stream
                in.close();

            }

            out.close();

        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    /**
     * @param filename the filename to extract
     * @param destination the destination to write to
     */
    public static void extractZipFile(String filename, String destination) throws Exception {

        byte[] buf = new byte[1024];
        ZipInputStream zipinputstream = null;
        ZipEntry zipentry;
        zipinputstream = new ZipInputStream(
                new FileInputStream(filename));

        log.debug("Extracting zipFile :" + filename);

        zipentry = zipinputstream.getNextEntry();

        while (zipentry != null) {

            //for each entry to be extracted

            String entryName = zipentry.getName();
            log.debug("found zip entryname " + entryName);
            int n;
            FileOutputStream fileoutputstream;
            File newFile = new File(entryName);
            String directory = newFile.getParent();

            if (directory == null) {
                if (newFile.isDirectory()) {
                    break;
                }
            }

            fileoutputstream = new FileOutputStream(destination + entryName);

            while ((n = zipinputstream.read(buf, 0, 1024)) > -1) {
                fileoutputstream.write(buf, 0, n);
            }

            fileoutputstream.close();
            zipinputstream.closeEntry();
            zipentry = zipinputstream.getNextEntry();

        }//while

        zipinputstream.close();
    }

    /**
     * @param filename the filename to extract
     * @param destination the destination to write to
     */
    public static void extractZipArchive(String filename, String destination)
            throws Exception {

        try {
            ZipFile zipFile = new ZipFile(filename);
            Enumeration<? extends ZipEntry> zipEntryEnum = zipFile.entries();

            while (zipEntryEnum.hasMoreElements()) {
                ZipEntry zipEntry = zipEntryEnum.nextElement();
                log.debug("Found zip entry :" + zipEntry.getName() + ".");
                extractEntry(zipFile, zipEntry, destination);
                log.debug("unpacked");
            }

            zipFile.close();
        } catch (FileNotFoundException e) {
            log.debug("ZipFile not found :" + filename);
        } catch (IOException e) {
            log.fatal("unzip/IO error!");
        }
    }

    private static void extractEntry(ZipFile zf, ZipEntry entry, String destDir) throws IOException {
        File file = new File(destDir, entry.getName());

        if (entry.isDirectory()) {
            file.mkdirs();
        } else {
            new File(file.getParent()).mkdirs();

            InputStream is = null;
            OutputStream os = null;

            try {
                is = zf.getInputStream(entry);
                os = new FileOutputStream(file);

                for (int len; (len = is.read(buffer)) != -1;) {
                    os.write(buffer, 0, len);
                }
            } finally {
                os.close();
                is.close();
            }
        }
    }
	
	public static void zipDir(String destination, String source) throws Exception {
		
		File dirObj = new File(source);
		
		if (dirObj.list().length < 1)
			return;
		
		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(destination));
		
		log.info("Creating : " + destination);
		addDir(dirObj, zos,dirObj.getAbsolutePath());
		zos.close();
	}

	public static void addDir(File dirObj, ZipOutputStream out, String baseDir) throws IOException {
		
		File[] files = dirObj.listFiles();
		
		byte[] tmpBuf = new byte[1024];

		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				addDir(files[i], out, baseDir);
				continue;
			}
		
			FileInputStream in = new FileInputStream(files[i].getAbsolutePath());
			log.info("adding "+files[i].getAbsolutePath().substring(baseDir.length()+1));			
			out.putNextEntry(new ZipEntry(files[i].getAbsolutePath().substring(baseDir.length()+1)));
			
			int len;
			
			while ((len = in.read(tmpBuf)) > 0) {
				out.write(tmpBuf, 0, len);
			}
			
			out.closeEntry();
			in.close();
		}
		
	}	

}
