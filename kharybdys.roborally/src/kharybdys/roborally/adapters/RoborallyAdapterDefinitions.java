package kharybdys.roborally.adapters;

import java.io.InputStream;
import java.util.List;

import navajo.ExtensionDefinition;



public class RoborallyAdapterDefinitions implements ExtensionDefinition {

	private static final long serialVersionUID = -7321213989463678155L;

	@Override
	public InputStream getDefinitionAsStream() {
		return getClass().getClassLoader().getResourceAsStream("kharybdys/roborally/adapters/roborallyadapters.xml");
	}

	@Override
	public String getConnectorId() {
		return null;
	}

	@Override
	public List<String> getDependingProjectUrls() {
		return null;
	}

	public String getDeploymentDescriptor() {
		return null;
	}

	@Override
	public String getDescription() {
		return "The RoboRally Navajo Adapter library";
	}

	@Override
	public String getId() {
		return "RoboRallyAdapters";
	}

	@Override
	public String[] getIncludes() {
		return null;
	}

	public List<String> getLibraryJars() {
		return null;
	}

	public List<String> getMainJars() {
		return null;
	}

	@Override
	public String getProjectName() {
		return null;
	}

	@Override
	public List<String> getRequiredExtensions() {
		return null;
	}

	@Override
	public boolean isMainImplementation() {
		return false;
	}

	@Override
	public String requiresMainImplementation() {
		return null;
	}

	@Override
	public ClassLoader getExtensionClassloader() {
		return null;
	}

	@Override
	public void setExtensionClassloader(ClassLoader extClassloader) {
		
	}

}
