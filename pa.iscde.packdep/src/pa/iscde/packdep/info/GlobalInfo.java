package pa.iscde.packdep.info;

//global workspace information
public class GlobalInfo {
	
	//number of packages
	int nPackages;
	//number of dependencies
	int nDependencies;
	//number of classes
	int nClasses;
	
	//constructor.
	public GlobalInfo(int nPackages, int nDependencies, int nClasses) {
		this.nPackages = nPackages;
		this.nDependencies = nDependencies;
		this.nClasses = nClasses;
	}

	public int getnPackages() {
		return nPackages;
	}

	public int getnDependencies() {
		return nDependencies;
	}

	public int getnClasses() {
		return nClasses;
	}

}
