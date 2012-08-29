package net.skiy.pants.tasks.agentproperty;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

import net.skiy.pants.tasks.PAntsProperties;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.ImportTask;
import org.apache.tools.ant.taskdefs.Property;

public class LoadProjectProperties extends Task {

	public void execute() {
		PAntsProperties pantsProp = PAntsProperties.getInstance();
		pantsProp.init(getProject());
		
		File projectbasedir = pantsProp.getProjectBaseDirectory();
		File nowdir = projectbasedir;
		
		String buildpropertiesname = pantsProp.getProjectBuildPropertyName();
		String buildfilename = pantsProp.getProjectBuildFileName();
		try {
			getProject().log("recurse import untill directory " + pantsProp.getProjectTopDirectoryName() , Project.MSG_VERBOSE);
			while (!nowdir.getName().equals(
					pantsProp.getProjectTopDirectoryName())) {
				File nowdirbuildproperties = new File(nowdir.getAbsolutePath()
						+ File.separator + buildpropertiesname);
				File nowdirbuildxml = new File(nowdir.getAbsolutePath()
						+ File.separator + buildfilename);

				if (nowdirbuildproperties.exists()) {
					loadPropertyFile(nowdirbuildproperties);
					getProject().log(
							"loaded: " + nowdirbuildproperties.getAbsolutePath(),
							Project.MSG_VERBOSE);
				} else {
					getProject().log(
							"nowdirbuildproperties does not exist: "
									+ nowdirbuildproperties.getAbsolutePath(),
							Project.MSG_WARN);
				}

				if (nowdir != projectbasedir && nowdirbuildxml.exists()) {
					importantfile(nowdirbuildxml, projectbasedir);
					getProject().log(
							"imported: " + nowdirbuildxml.getAbsolutePath(),
							Project.MSG_VERBOSE);
				} else {
					getProject().log(
							"nowdirbuildxml does not exist: "
									+ nowdirbuildxml.getAbsolutePath(),
							Project.MSG_WARN);
				}

				nowdir = nowdir.getParentFile();
			}
		} catch (IOException e) {
			throw new BuildException(e);
		}

	}

	private void importantfile(File buildxml, File basedirectory) throws IOException {
		ImportTask t = (ImportTask) getProject().createTask("import");
		getProject().log(
				"getOwningTarget().getLocation().getFileName(): " + getOwningTarget().getLocation().getFileName(),
				Project.MSG_DEBUG);
		getProject().log(
				"getLocation().getFileName(): " + getLocation().getFileName(),
				Project.MSG_DEBUG);
		getProject().log(
				"getLocation().getFileName(): " + getLocation().getFileName(),
				Project.MSG_DEBUG);
		
		t.setOwningTarget(getOwningTarget());
		t.setLocation(getOwningTarget().getLocation());
		t.setFile("file:" + buildxml.getCanonicalPath());//need file: or Malformed Exception occur for windows path
		t.execute();
	}

	private void loadPropertyFile(File propertyfile) throws IOException {
		getProject().log(
				"loading: " + propertyfile.getAbsolutePath(),
				Project.MSG_VERBOSE);
		Property p = (Property) getProject().createTask("property");
		p.setFile(propertyfile);
		p.execute();
	}
}
