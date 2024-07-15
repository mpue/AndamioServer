/**

	Weberknecht AndamioManager - Open Source Content Management
	Written and maintained by Matthias Pueski 
	
	Copyright (c) 2009 Matthias Pueski
	
	This program is free software; you can redistribute it and/or
	modify it under the terms of the GNU General Public License
	as published by the Free Software Foundation; either version 2
	of the License, or (at your option) any later version.
	
	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU General Public License
	along with this program; if not, write to the Free Software
	Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

*/
package org.pmedv.services;

import java.util.ArrayList;

import org.pmedv.beans.objects.ConfigBean;
import org.pmedv.beans.objects.DirectoryObject;
import org.pmedv.beans.objects.StatusInformer;
import org.pmedv.context.AppContext;
import org.pmedv.core.util.IProgressMonitor;
import org.pmedv.util.FileHelper;
import org.springframework.context.ApplicationContext;

public class FileServiceImpl implements FileService {
	
	private final ApplicationContext ctx = AppContext.getApplicationContext();
	
	@Override
	public ArrayList<String> getFileURLsForContent() {

		ConfigService cs = (ConfigService)ctx.getBean("configService");		
		ConfigBean config = cs.getConfig();
		
		String contentDir = config.getBasepath()+config.getContentpath();		
		
		return FileHelper.getFileURLsForDirectory(contentDir, config.getHostname()+"/"+config.getLocalPath()+config.getContentpath());
		
	}

	@Override
	public ArrayList<DirectoryObject> getFiles(DirectoryObject arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean copyFiles(ArrayList<DirectoryObject> files, String targetPath) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean copyFiles(ArrayList<DirectoryObject> files, String targetPath, IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void createDirectory(String path) throws IllegalArgumentException {
	}

	@Override
	public void deleteFiles(ArrayList<DirectoryObject> files, ConfirmationHandler handler) {
	}

	@Override
	public String getCurrentFilename() {
		// TODO Auto-generated method stub
		return null;
	}

	public void updateStatus(StatusInformer si, DirectoryObject o) {
		
	}
	
}
