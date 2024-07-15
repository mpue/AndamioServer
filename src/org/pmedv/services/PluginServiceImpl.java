package org.pmedv.services;

import java.util.ArrayList;

import org.pmedv.plugins.IPlugin;
import org.pmedv.plugins.ParamDescriptor;
import org.pmedv.plugins.PluginHelper;

public class PluginServiceImpl implements PluginService {

	@Override
	public ArrayList<ParamDescriptor> getParamDescriptors(String pluginName) {

		PluginHelper.initIPluginClasses(this.getClass().getClassLoader());
		return PluginHelper.getParamDescriptors(pluginName, this.getClass().getClassLoader());

	}

	@Override
	public ArrayList<String> getPluginList() {

		ArrayList<String> pluginNames = new ArrayList<String>();

		PluginHelper.initIPluginClasses(this.getClass().getClassLoader());

		ArrayList<IPlugin> plugins = PluginHelper.getPluginList();

		for (IPlugin plugin : plugins) {
			pluginNames.add(plugin.getName());

		}

		return pluginNames;
	}

}
