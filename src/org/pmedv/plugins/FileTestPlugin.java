package org.pmedv.plugins;



public class FileTestPlugin extends AbstractPlugin implements IPlugin {

	private static int calls = 0;
	
	@Override
	public String getContent() {

		calls++;
		
		return "calls :" +calls;
		
//		File f = new File("/home/pueski/devel/j2ee_workspace/andamio/src/MessageResources.properties");
//
//		byte[] content;
//		
//		try {
//			content = FileUtils.readFileToByteArray(f);
//			ByteArrayOutputStream bos = new ByteArrayOutputStream();
//			bos.write(content);
//
//			response.setContentType("text/plain");
//			response.setHeader("Content-Disposition","attachment; filename=test.txt");			
//			response.setContentLength(bos.size());
//
//			bos.writeTo(response.getOutputStream());
//			response.getOutputStream().flush();
//			
//		}
//		catch (IOException e1) {
//			e1.printStackTrace();
//		}
//		
//		return null;
		
	}

}
