package pa.iscde.packdep.info;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import pt.iscte.pidesco.projectbrowser.model.PackageElement;

public class PackageInfo {

	private PackageElement p;
	private String name;
	private int nClass;
	private int nLines;
	private int nDependencies;
	private ArrayList<PackageElement> dependencies;
	
	public PackageInfo(PackageElement p, int nClass, int nLines, int nDependencies, ArrayList<PackageElement> dependencies){
		this.p = p;
		this.name = p.getName();
		this.nClass = nClass;
		this.nLines = nLines;
		this.nDependencies = nDependencies;
		this.dependencies = dependencies;
	}
	
	public PackageElement getElement() {
		return p;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the nClass
	 */
	public int getnClass() {
		return nClass;
	}

	/**
	 * @return the nLines
	 */
	public int getnLines() {
		return nLines;
	}

	/**
	 * @return the nDependencies
	 */
	public int getnDependencies() {
		return nDependencies;
	}

	/**
	 * @return the dependencies
	 */
	public ArrayList<PackageElement> getDependencies() {
		return dependencies;
	}
}
