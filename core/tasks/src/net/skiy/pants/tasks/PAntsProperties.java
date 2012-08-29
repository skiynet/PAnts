package net.skiy.pants.tasks;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Property;

public class PAntsProperties {

	private static PAntsProperties pantsProperties;
	private Project project;
	
	public static PAntsProperties getInstance() {
		if (pantsProperties == null) {
			pantsProperties = new PAntsProperties();
		}
		return pantsProperties;
	}
	
	public void init(Project project) {
		this.project = project;
		try {
			loadDeployProperty();
		} catch (IOException e) {
			throw new BuildException("cannot load deploy properties");
		}
		setToProject();
	}
	
	private void loadDeployProperty() throws IOException {
		loadPropertyFile(new File(getServerTopDirectory() + File.separator + "deploy.properties"));
	}

	private void loadPropertyFile(File propertyfile) throws IOException {
		getProject().log(
				"loading: " + propertyfile.getAbsolutePath(),
				Project.MSG_VERBOSE);
		Property p = (Property) getProject().createTask("property");
		p.setFile(propertyfile);
		p.execute();
	}
	
	private void setToProject() {
		Method[] methods = PAntsProperties.class.getMethods();
		
		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			
			if (!Modifier.isPublic(method.getModifiers())) {
				continue;
			}
			
			if (!method.getName().equals("getInstance") && method.getName().startsWith("get")) {
				String convertedName = convert(method.getName().substring("get".length()));
				String key = getPAntsPropertyPrefix() + "." + convertedName;
				String value;
				try {
					Object ret = method.invoke(this, null);
					if (ret instanceof File) {
						value = ((File)ret).getAbsolutePath();
					} else if (ret instanceof String) {
						value = (String)ret;
					} else {
						continue;
					}
					
					project.setProperty(key, value);
				} catch (IllegalAccessException e) {
					throw new BuildException(e);
				} catch (IllegalArgumentException e) {
					throw new BuildException(e);
				} catch (InvocationTargetException e) {
					throw new BuildException(e);
				}
			}
		}
	}
	
	
	private String convert(String javaMethodName) {
		char[] chars = javaMethodName.toCharArray();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < chars.length; i++) {
			if (Character.isUpperCase(chars[i])) {
				if (i != 0) {
					sb.append(".");
				}
				sb.append(Character.toLowerCase(chars[i]));
			} else {
				sb.append(chars[i]);
			}
		}
		return sb.toString();
	}

	public String getPAntsPropertyPrefix() {
		return "pants";
	}

	
	public File getSiteFileDirectory() {
//		<property file="${root.dir}/projectsite/${site-file}"
//				prefix="${lserverprefix}" />
		return new File(getServerTopDirectory() + File.separator + "projectsite");
	}

	public File getProjectBaseDirectory() {
		if (getProject().getProperty("project.basedir") != null) {
			return new File(getProject().getProperty("project.basedir"));
		}
		return new File(getProject().getProperty("basedir"));
	}
	
	public String getProjectTopDirectoryName() {
//		return getServerTopDirectory().getName();
		return "cases";
	}

	public String getProjectBuildPropertyName() {
		return "conductor-build.properties";
	}

	public String getDistributeDirectoryName() {
		return getProjectBaseDirectory().getAbsolutePath() + File.separator + "dist";
	}
	
	private Project getProject() {
		return project;
	}

	public File getServerTopDirectory() {
		if (System.getenv("ANTCRAWLER_HOME") == null || System.getenv("ANTCRAWLER_HOME").equals("")) {
			return new File(".");
		}
		File top = new File(System.getenv("ANTCRAWLER_HOME"));
		if (!top.exists()) {
			throw new BuildException("ANTCRAWLER_HOME is invalid directory " + top.getAbsolutePath());
		}
		return top;
	}

	public String getProjectBuildFileName() {
		return "conductor-build.xml";
	}
	
	public File getSiteMappingFile() throws IOException {
//		<property file="${root.dir}/site-server.mapping" prefix="sitemapping."/>
		return new File(getServerTopDirectory().getCanonicalPath() + File.separator + getSiteMappingFileName());
	}
	
	public String getSiteMappingFileName() {
		return "site-server.mapping";
	}
	
	public String getAgentLibDir() {
		return "lib";
	}
	
	
	public String getServerAgentDir() {
		return getProject().getProperty(getPAntsPropertyPrefix()+"." + "server.agent.dir");
	}
	
	public String getServerAgentLibDirPath() throws IOException {
		return getServerTopDirectory().getCanonicalPath() + File.separator + getProject().getProperty(getPAntsPropertyPrefix()+"." + "server.agent.lib.dir");

	}
	
	public String getAgentTempDir() {
		return "tmp";
	}
	
	public String getServerAgentAntDirPath() throws IOException {
		return getServerTopDirectory().getCanonicalPath() + File.separator + getProject().getProperty(getPAntsPropertyPrefix()+"." + "server.agent.ant.dir");
	}
	
	public String getAgentAntDir() {
		return ".";
	}
	
	
	public String getModulesLibDir() {
		return "module";
	}

//	public String getPantsSiteprorpertyFilename() {
//		return "site.antcrawler.properties";
//	}
	
}
