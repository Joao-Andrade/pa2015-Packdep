package pa.iscde.packdep.info;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import pt.iscte.pidesco.projectbrowser.model.PackageElement;

//
/**
 * 
 * package information.
 * Contains:
 * -the package element;
 * -the name of the package;
 * -number of classes on the package;
 * -number of code lines on the package;
 * -number of dependencies on the package;
 * -List of dependencies. The dependencies are PackageElements.
 *
 */
public class PackageInfo {


	/**
	 * Package element.
	 */
	private PackageElement packageElement;

	/**
	 * name of the package.
	 */
	private String name;

	/**
	 * number of classes inside the package.
	 */
	private int numberOfClasses;

	/**
	 * number of lines inside the package.
	 */
	private int numberOfLines;

	/**
	 * number of dependencies of the package.
	 */
	private int numberOfDependencies;

	/**
	 * package elements that this package depends on.
	 */
	private ArrayList<PackageElement> dependencies;
	
	//constructor.
	public PackageInfo(PackageElement packageelement, int numberOfClass, int numberOfLines, int numberOfDependencies, ArrayList<PackageElement> dependencies){
		this.packageElement = packageelement;
		this.name = packageelement.getName();
		this.numberOfClasses = numberOfClass;
		this.numberOfLines = numberOfLines;
		this.numberOfDependencies = numberOfDependencies;
		this.dependencies = dependencies;
	}
	
	/**
	 * Package element.
	 */
	public PackageElement getElement() {
		return packageElement;
	}

	/**
	 * name of the package.
	 */
	public String getName() {
		return name;
	}

	/**
	 * number of classes inside the package.
	 */
	public int getNumberOfClass() {
		return numberOfClasses;
	}

	/**
	 * number of lines inside the package.
	 */
	public int getNumberOfLines() {
		return numberOfLines;
	}

	/**
	 * number of dependencies of the package.
	 */
	public int getNumberOfDependencies() {
		return numberOfDependencies;
	}

	/**
	 * package elements that this package depends on.
	 */
	public List<PackageElement> getDependencies() {
		return Collections.unmodifiableList(dependencies);
	}
}
