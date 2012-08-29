package net.skiy.pants.tasks;


import java.io.File;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.PropertyHelper;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Property;
import org.apache.tools.ant.types.FileList;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.ResourceCollection;



public class GatherReportTask extends Task {
	
	private Path buildpath;
    private boolean verbose = false;
	
    private Path getBuildpath() {
        if (buildpath == null) {
            buildpath = new Path(getProject());
        }
        return buildpath;
    }
    public void add(ResourceCollection rc) {
    	getBuildpath().add(rc);
    }
    public void addFileset(FileSet set) {
        add(set);
    }
    public void addFilelist(FileList list) {
        add(list);
    }
	public void execute() {
		
		//fileListの各ファイルを取得
		//各ファイルのテキストをstate変数に入れる
		
        if (buildpath == null) {
            throw new BuildException("No buildpath specified");
        }

        final String[] filenames = buildpath.list();
        final int count = filenames.length;
        if (count < 1) {
            log("No sub-builds to iterate on", Project.MSG_WARN);
            return;
        }
        
        for (int i = 0; i < count; ++i) {
            File file = null;
            try {
                file = new File(filenames[i]);
                if (file.isDirectory()) {
                    continue;
                }
                addProperty(file);
            } finally {
            	
            }
        }
	}
	
	private void addProperty(File file) {
		String prefix = file.getName();
		System.out.println("Read property file: " + prefix);
		Property prop = new Property();
		prop.setProject(getProject());
		prop.setFile(file);
		prop.setPrefix(prefix);
		prop.execute();
	}
	
	//標準出力に成形されたレポートを表示する
	private void show() {
		String regex = "(.*)\\.status";
		Pattern p = Pattern.compile(regex);
		Hashtable<String, String> table = new Hashtable<String, String>();
		Set set = getProject().getProperties().entrySet();
		for (Iterator iterator = set.iterator(); iterator.hasNext();) {
			Property prop = (Property) iterator.next();
			Matcher m1 = p.matcher(prop.getName());
			if (m1.find()) {
				String name = m1.group(1);
				table.put(name, prop.getValue());
			}
		}
		
		showtable(table);
	}
	
	private void showtable(Hashtable table) {
		for (Iterator iterator = table.entrySet().iterator(); iterator.hasNext();) {
			Property prop = (Property) iterator.next();
			System.out.println(prop.getName() + ": " + prop.getValue());
		}
	}
}
