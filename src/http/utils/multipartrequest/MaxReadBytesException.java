package http.utils.multipartrequest;
import java.io.IOException;

public class MaxReadBytesException extends IOException 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	long maxReadBytes;
	long contentLength;
	
	public MaxReadBytesException(long contentLength, long maxReadBytes)
	{
		super("Content length exceeded ("+contentLength+" > "+maxReadBytes+")");
		this.maxReadBytes = maxReadBytes;
		this.contentLength = contentLength;
	}

	public long getContentLength()
	{
		return this.contentLength;
	}
	
	public long getMaxReadBytes()
	{
		return this.maxReadBytes;
	}
}