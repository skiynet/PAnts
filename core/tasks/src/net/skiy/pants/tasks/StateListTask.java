package net.skiy.pants.tasks;

import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.Task;

public class StateListTask extends Task {

	private List<StateType> stateList = new ArrayList<StateType>();
	public List<StateType> getStateList() {
		return stateList;
	}

	private String name;
	
	private String exectarget;
	
	public String getExectarget() {
		return exectarget;
	}

	public void setExectarget(String exectarget) {
		this.exectarget = exectarget;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addState(StateType state) {
		stateList.add(state);
	}
	
	public void execute() {
		for (StateType state : stateList) {
			System.out.println(state.getModule());
		}
	}
}
