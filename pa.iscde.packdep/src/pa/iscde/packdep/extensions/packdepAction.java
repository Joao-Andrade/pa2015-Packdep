package pa.iscde.packdep.extensions;

import java.util.List;

import pa.iscde.packdep.info.PackageInfo;

/**
 * 
 * Create and set up the action to perform with the nodes we select at the graph
 * it is possible to choose the action's name and determinate what the action does
 * 
 * Note:there's no name or execute predefined, so the use as to set this properties
 *
 */
public interface packdepAction {
	
	/**
	 * @return action's name.
	 */
	String getActionName();
	
	/**
	 * 
	 * @param selectedPackages, is a List of PackageInfo, that retain information of the selected packages
	 * execute is to do the action itself
	 */
	void execute(List<PackageInfo> selectedPackages);
	
	
	

}
