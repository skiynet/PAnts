package net.skiy.pants.tasks.agentproperty;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import net.skiy.pants.tasks.PAntsProperties;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Property;
import org.apache.tools.ant.types.PropertySet.PropertyRef;
import org.apache.tools.ant.util.FileUtils;

public class LoadInstallProperties extends Task {

	public void execute() {
		getProject().log("####### LoadInstallProperties #######", Project.MSG_VERBOSE);
		
		PAntsProperties pantsProp = PAntsProperties.getInstance();
		pantsProp.init(getProject());

		File file;
		try {
			file = pantsProp.getSiteMappingFile();

			getProject().log("Loading " + file.getAbsolutePath(),
					Project.MSG_VERBOSE);
			if (file.exists()) {
				FileInputStream fis = null;
				try {
					BufferedReader br = new BufferedReader(new FileReader(file));
					String line = br.readLine();
					while (line != null) {
						line = line.trim();
						if ("".equals(line)) {
							line = br.readLine();
							continue;
						}
						if (line.startsWith("#")) {
							line = br.readLine();
							continue;
						}
						String[] words = line.split("=");
						if (words.length < 2) {
							line = br.readLine();
							continue;
						}
						String server = words[0];
						String installfile = words[1];
						loadInstallProperty(server, installfile);
//						loadPAntInstallProperty(server);
						loadPAntsAgentPropery(server);
						line = br.readLine();
					}

				} finally {
					FileUtils.close(fis);
				}
			} else {
				getProject().log(
						"Unable to find property file: "
								+ file.getAbsolutePath(), Project.MSG_VERBOSE);
			}
		} catch (IOException e) {
			throw new BuildException(e, getLocation());
		}
	}

	private void loadPAntsAgentPropery(String server) {
		String pantprefix = PAntsProperties.getInstance().getPAntsPropertyPrefix();
		Hashtable table = getProject().getProperties();
		for(Object key : table.keySet()) {
			String skey = (String)key;
			if (skey.startsWith(pantprefix + ".agent")) {
				getProject().setProperty(server + "." + skey, (String)table.get(key));
			}
		}
		
		addBaseDirToProperty(server);
	}

	private void addBaseDirToProperty(String server) {
		String casename = getProject().getProperty("ant.project.name");
		getProject().setProperty(server + "." + "basedir", casename + "/" + server);
	}

	private void loadInstallProperty(String server, String installfile)
			throws IOException {
		File dir = PAntsProperties.getInstance().getSiteFileDirectory();
		File absoluteInstallFile = new File(dir.getAbsoluteFile()
				+ File.separator + installfile);
		getProject().log("Loading for " + server + " " + absoluteInstallFile.getAbsolutePath(), Project.MSG_VERBOSE);
		if (!absoluteInstallFile.exists()) {
			throw new IOException("install property: "
					+ absoluteInstallFile.getAbsolutePath()
					+ " does not exist.");
		}
		Property p = (Property) getProject().createTask("property");
		p.setFile(absoluteInstallFile);
		p.setPrefix(server);
		p.execute();
	}
	
//	private void loadPAntInstallProperty(String server) throws IOException {
//		loadInstallProperty(server, PAntsProperties.getInstance().getPantsSiteprorpertyFilename());
//	}

}
