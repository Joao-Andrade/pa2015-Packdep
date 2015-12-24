package pa.iscde.packdep.info;

/**
 * 
 * global workspace information
 *
 */
public class GlobalInfo {
	
	/**
	 * number of packages on the workspace
	 */
	int numberOfPackages;
	/**
	 * number of dependencies on the workspace
	 */
	int numberOfDependencies;
	/**
	 * number of classes on the workspace
	 */
	int numberOfClasses;
	
	//constructor.
	public GlobalInfo(int numberOfPackages, int numberOfDependencies, int numbeOfClasses) {
		this.numberOfPackages = numberOfPackages;
		this.numberOfDependencies = numberOfDependencies;
		this.numberOfClasses = numbeOfClasses;
	}

	/**
	 * number of packages on the workspace
	 */
	public int getNumberOfPackages() {
		return numberOfPackages;
	}

	/**
	 * number of dependencies on the workspace
	 */
	public int getNumberOfDependencies() {
		return numberOfDependencies;
	}

	/**
	 * number of classes on the workspace
	 */
	public int getNumberOfClasses() {
		return numberOfClasses;
	}

}
