package pa.iscde.packdep.extensions;

import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.zest.core.widgets.ZestStyles;

import pa.iscde.packdep.info.GlobalInfo;
import pa.iscde.packdep.info.PackageInfo;
import pa.iscde.packdep.info.PackageSize;

/**
 * 
 * @author packdepCreators
 *
 *This interface is for the extensions that want to personalize the view.
 *This includes:
 *-color, highlight color, size, border color, highlight border color, 
 *text color and icon for each node (package).
 *-background color.
 *
 *the init function receives a GlobalInfo object that contains information regarding the workspace.
 *All the other functions receive information about a single package.
 *
 *When implementing this interface, all methods can return null. 
 *If a method returns null, a default value is used instead, so whoever implements this doesn't 
 *need to worry about methods that he/she is not interested.
 *
 *ATTENTION: the icon method needs special attention. If the user does not return a valid argument,
 *the view may not open and may raise an exception. If the user does not know what to return, 
 *it is recommended that returns null.
 */
public interface packdepStyle {

		/**
		 * 
		 * This is called first. Use this method to store the information about the 
		 * workspace and then use that information on the rest of the methods.
		 * 
		 * @param gInfo information about the workspace.
		 * It has the total number of the packages, classes and dependencies.
		 */
	    void init(GlobalInfo gInfo);
	    
	    /**
	     * 
	     * Get the color that the node (package) will have.<br>
	     * Example: return new Color(Display.getDefault(), 255, 255, 255);
	     * 
	     * @param info package information.
	     * It contains the packageElement, name, number of classes, number of dependencies, 
	     * number of lines and the packages that depends on.
	     * @return color of the package
	     */
	    Color getColor(PackageInfo info);
	    
	    /**
	     * 
	     * Get the color of the name of the package.<br>
	     * Example: return new Color(Display.getDefault(), 255, 255, 255);
	     * 
	     * @param info package information.
	     * It contains the packageElement, name, number of classes, number of dependencies, 
	     * number of lines and the packages that depends on.
	     * @return color of the name of the package
	     */
	    Color getTextColor(PackageInfo info);
	    
	    /**
	     * 
	     * Get the color that the node (package) will have when highlighted (e.g. selected).<br>
	     * Example: return new Color(Display.getDefault(), 255, 255, 255);
	     * 
	     * @param info package information.
	     * It contains the packageElement, name, number of classes, number of dependencies, 
	     * number of lines and the packages that depends on.
	     * @return color of the package when highlighted
	     */
	    Color getHighlightColor(PackageInfo info);
	    
	    /**
	     * 
	     * Get the border color of the nodes (packages).<br>
	     * Example: return new Color(Display.getDefault(), 255, 255, 255);
	     * 
	     * @param info package information.
	     * It contains the packageElement, name, number of classes, number of dependencies, 
	     * number of lines and the packages that depends on.
	     * @return border color
	     */
	    Color getBorderColor(PackageInfo info);
	    
	    /**
	     * 
	     * Get the border color of the nodes (packages) when highlighted (e.g. selected).<br>
	     * Example: return new Color(Display.getDefault(), 255, 255, 255);
	     * 
	     * @param info package information.
	     * It contains the packageElement, name, number of classes, number of dependencies, 
	     * number of lines and the packages that depends on.
	     * @return border color when highlighted.
	     */
	    Color getBorderHighlightColor(PackageInfo info);
	    
	    /**
	     * 
	     * Get node (package) size.<br>
	     * Example: return new PackageSize(100,50);
	     * 
	     * @param info package information.
	     * It contains the packageElement, name, number of classes, number of dependencies, 
	     * number of lines and the packages that depends on.
	     * @return size of the node (package).
	     */
	    PackageSize getSize(PackageInfo info);
	    
	    /**
	     * 
	     * Get text font that appears on the node (package).<br>
	     * Example: new PackageSize(100,50);
	     * 
	     * @param info package information.
	     * It contains the packageElement, name, number of classes, number of dependencies, 
	     * number of lines and the packages that depends on.
	     * @return node's text font.
	     */
	    Font getTextFont(PackageInfo info);
	    
	    
	    /**
	     * 
	     * Get background color.<br>
	     * Example: return new Color(Display.getDefault(), 255, 255, 255);
	     * 
	     * @param info package information.
	     * It contains the packageElement, name, number of classes, number of dependencies, 
	     * number of lines and the packages that depends on.
	     * @return background color.
	     */
	    Color getBackgroundColor(PackageInfo info);
	    
	    /**
	     * 
	     * Get icon shown on the node (package).<br>
	     * Be carefull! Returning a non valid image may raise an exception!
	     * 
	     * @param info package information.
	     * It contains the packageElement, name, number of classes, number of dependencies, 
	     * number of lines and the packages that depends on.
	     * @return image.
	     */
	    Image getIcon(PackageInfo info);
	
}
