package pa.iscde.packdep.info;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import pt.iscte.pidesco.projectbrowser.model.PackageElement;

//package information.
public class PackageInfo {

	//Package element.
	private PackageElement p;
	//name of the package.
	private String name;
	//number of classes inside the package.
	private int nClass;
	//number of lines inside the package.
	private int nLines;
	//number of dependencies.
	private int nDependencies;
	//package elements that this package depends on
	private ArrayList<PackageElement> dependencies;
	
	//constructor.
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
	public List<PackageElement> getDependencies() {
		return Collections.unmodifiableList(dependencies);
	}
}
