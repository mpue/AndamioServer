package org.pmedv.servlets;

import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileUploadListener implements ProgressListener {

	private volatile long bytesRead = 0L;
	private volatile long contentLength = 0L;
	private volatile long item = 0L;
	private volatile int  percentComplete = 0;
	private volatile int  lastPercentComplete = 0;
	
	private static final Log log = LogFactory.getLog(FileUploadListener.class);

	public FileUploadListener() {
		super();
	}

	public void update(long aBytesRead, long aContentLength, int anItem) {
		bytesRead = aBytesRead;
		contentLength = aContentLength;
		item = anItem;
		percentComplete = (int) ((100 * bytesRead) / contentLength);
		
		if (percentComplete > lastPercentComplete) {
			lastPercentComplete = percentComplete;
			log.info(percentComplete + "% complete.");
		}

	}

	public long getBytesRead() {
		return bytesRead;
	}

	public long getContentLength() {
		return contentLength;
	}

	public long getItem() {
		return item;
	}

	public int getPercentComplete() {
		return percentComplete;
	}

}