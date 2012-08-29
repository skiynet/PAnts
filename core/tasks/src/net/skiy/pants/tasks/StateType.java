package net.skiy.pants.tasks;

import org.apache.tools.ant.types.DataType;

public class StateType extends DataType {

	private String serverprefix;

	private String build;

	private String module;
	
	private String target;

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getModule() {
		return module;
	}

	public String getServerprefix() {
		return serverprefix;
	}

	public String getBuild() {
		return build;
	}

	public void setModule(String module) {
		this.module = module;
	}
	public void setServerprefix(String serverprefix) {
		this.serverprefix = serverprefix;
	}
	public void setBuild(String build) {
		this.build = build;
	}

}
