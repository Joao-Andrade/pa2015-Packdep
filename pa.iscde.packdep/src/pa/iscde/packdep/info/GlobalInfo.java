package pa.iscde.packdep.info;

public class GlobalInfo {
	
	int nPackages;
	int nDependencies;
	int nClasses;
	
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
