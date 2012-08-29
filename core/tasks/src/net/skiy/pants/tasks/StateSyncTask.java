package net.skiy.pants.tasks;


import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Ant;
import org.apache.tools.ant.types.Reference;

import net.skiy.pants.tasks.antserver.client.ClientTask;

public class StateSyncTask extends Task {
	
	private Reference stateListRef;
	private Reference taskExecuteRef;
	
	private String remotetarget;
	

	public String getRemotetarget() {
		return remotetarget;
	}

	public void setRemotetarget(String remotetarget) {
		this.remotetarget = remotetarget;
	}

	public void setStateListRef(Reference r) {
		stateListRef = r;
    }
	
	public void setTaskExecuteRef(Reference r) {
		taskExecuteRef = r;
    }
	
	public void execute() {
		StateListTask stateListTask = (StateListTask)stateListRef.getReferencedObject();
		String targetname = stateListTask.getExectarget() == null ? "remote-execute" : stateListTask.getExectarget();
		
		for (StateType stateType : stateListTask.getStateList()) {
			try {

			    Ant ant = new Ant();
			    Project project = getProject();
//				    Project project = new Project();
//				    project.init();
			    project.setProperty("module", stateType.getModule());
			    project.setProperty("serverprefix", stateType.getServerprefix());
			    project.setProperty("build", stateType.getBuild());
			    project.setProperty("remotetarget", stateType.getTarget() == null ? remotetarget : stateType.getTarget());
			    ant.setProject(project);
			    ant.setDir(getProject().getBaseDir());
			    ant.setAntfile(getProject().getProperty("ant.file"));
			    ant.setTarget(targetname);
			    ant.execute();
				
			} catch (BuildException e) {
				System.out.println("caught failure but continuing:" + e.getMessage());
			}
		}
		

	}
}
