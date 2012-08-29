package net.skiy.pants.tasks.agentproperty;

import java.io.File;

import net.skiy.pants.tasks.PAntsProperties;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.optional.EchoProperties;
import org.apache.tools.ant.types.PropertySet;
import org.apache.tools.ant.types.PropertySet.PropertyRef;

public class GenerateProperties extends Task {

	private String server;
	
	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public void execute() {
		addBaseDirProperty();
		PAntsProperties pantsProp = PAntsProperties.getInstance();
		pantsProp.init(getProject());
		
		EchoProperties p = (EchoProperties)getProject().createTask("echoproperties");
		
		PropertySet ps1 = (PropertySet)getProject().createDataType("propertyset");
		PropertyRef ref1 = new PropertySet.PropertyRef();
		ref1.setPrefix(server + ".");
		ps1.addPropertyref(ref1);
		ps1.setMapper("glob", server + ".*", "*");
		
		PropertySet ps2 = (PropertySet)getProject().createDataType("propertyset");
		PropertyRef ref2 = new PropertySet.PropertyRef();
		ref2.setPrefix("all.");
		ps2.addPropertyref(ref2);
		ps2.setMapper("glob", "all.*", "*");
		
		p.addPropertyset(ps1);
		p.addPropertyset(ps2);
		
		File destDir = new File(pantsProp.getDistributeDirectoryName());
		if (!destDir.exists()) {
			if (!destDir.mkdir()) {
				throw new BuildException("Cannot create directory " + destDir.getAbsolutePath());
			}
		}
		File destfile = new File(pantsProp.getDistributeDirectoryName() + File.separator + server + ".properties");
		p.setDestfile(destfile);
		p.execute();
//		<echoproperties destfile="@{todir}/@{serverprefix}.properties">
//		<!-- all will be overrided by server -->
//		<propertyset>
//			<propertyref prefix="@{serverprefix}." />
//			<mapper type="glob" from="@{serverprefix}.*" to="*" />
//		</propertyset>
//		<propertyset>
//			<propertyref prefix="all." />
//			<mapper type="glob" from="all.*" to="*" />
//		</propertyset>
//	</echoproperties>
	}

	private void addBaseDirProperty() {
		// TODO Auto-generated method stub
		
	}
}
