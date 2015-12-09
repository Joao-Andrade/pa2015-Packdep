package pa.iscde.packdep;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;

import org.eclipse.swt.graphics.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphItem;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;

import pa.iscde.packdep.extensions.packdepStyle;
import pa.iscde.packdep.info.GlobalInfo;
import pa.iscde.packdep.info.PackageInfo;
import pa.iscde.packdep.info.PackageSize;
import pt.iscte.pidesco.extensibility.PidescoView;
import pt.iscte.pidesco.javaeditor.service.JavaEditorListener;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;
import pt.iscte.pidesco.projectbrowser.model.PackageElement;
import pt.iscte.pidesco.projectbrowser.model.SourceElement;
import pt.iscte.pidesco.projectbrowser.service.ProjectBrowserListener;
import pt.iscte.pidesco.projectbrowser.service.ProjectBrowserServices;

//Show package Dependencies in a window.
public class View implements PidescoView {

	//services
	JavaEditorServices editorService;
	ProjectBrowserServices projectService;
	
	//all packages
	ArrayList<PackageElement> packages;
	
	//array with all Packages information
	ArrayList<PackageInfo> pInfo;
	//infos.add(packageInformation((PackageElement) e));
	
	//global information
	GlobalInfo gInfo;
	
	//variables to define global information
	int nPackages;
	int nDependencies;
	int nClasses;
	
	@Override
	public void createContents(Composite viewArea, Map<String, Image> imageMap) {
		
		packages = new ArrayList<PackageElement>();
		
		// services
		editorService = Activator.getEditorService();
		projectService = Activator.getProjectService();
		
		//get packages informations
		pInfo = getPackages();
		
		//set global info
		gInfo = new GlobalInfo(nPackages, nDependencies, nClasses);
		
		//print packages information
		//testPrintInformation();
		
		showGraph(viewArea, imageMap);
		
	}

	private ArrayList<PackageInfo> getPackages(){
		ArrayList<PackageInfo> infos = new ArrayList<PackageInfo>();
		
		// root element of workspace.
		PackageElement root_package = projectService.getRootPackage();

		//----get all packages
		
		// if workspace is not empty.
		if (root_package.hasChildren()) {
			// childs of root_package.
			SortedSet<SourceElement> pack_child = root_package.getChildren();

			// when searching for packages, the childs, that are packages and
			// are not yet searched, go here.
			// basically its an array with packages that are not searched yet.
			ArrayList<PackageElement> searchPackages = new ArrayList<PackageElement>();

			// check if a child is package
			//this gets only the packages that are children of the root_package.
			for (SourceElement element : pack_child) {
				if (element.isPackage()) {
					packages.add((PackageElement) element);
					searchPackages.add((PackageElement) element);
					//add to the list
				}
			}

			//search all packages that are inside packages
			boolean over = false;
			ArrayList<PackageElement> newFoundPackages = new ArrayList<PackageElement>();
			while (!over) {
				for (PackageElement p : searchPackages) {
					if (p.hasChildren()) {
						// childs of packages
						SortedSet<SourceElement> children = p.getChildren();
						// check if a child is package
						for (SourceElement e : children) {
							if (e.isPackage()) {
								newFoundPackages.add((PackageElement) e);
								//add to the list
									packages.add((PackageElement) e);
							}
						}
					}
				}
				// if there are new packages
				if (newFoundPackages.isEmpty()) {
					over = true;
				}
				// replace search array for the array with the new packages
				// found
				searchPackages = newFoundPackages;
				newFoundPackages = new ArrayList<PackageElement>();
			}
		}
		
		
		//----build PackageInfo array
		
		for (PackageElement element : packages) {
			nPackages+=1;
			infos.add(packageInformation(element));
		}
		
		return infos;
	}
	
	private PackageInfo packageInformation(PackageElement element){
		int nClass = 0;
		int nLines = 0;
		int nDependencies = 0;
		ArrayList<PackageElement> dependencies = new ArrayList<PackageElement>();
		
		//Check if the package contains classes
		if(element.hasChildren()){
			SortedSet<SourceElement> childElements = element.getChildren();
			//check if child is a class
			for(SourceElement e : childElements){
				if(e.isClass()){
					nClasses+=1;
					nClass +=1;
					
					//check for imports
					//Analyzes the file and checks the imports
					try {
						BufferedReader in = new BufferedReader(new FileReader(e.getFile()));
						String line;
						boolean stop = false;
						while ((line = in.readLine()) != null) {
							//counting lines
							nLines+=1;
							//if is still checking for imports.
							if(!stop){
								// if it is an import and not a word import somewhere on the code.
								if (line.indexOf("import") == 0) {
									// get only the import.
									//Removes the word import.
									String i = line.replace("import ", "");
									if(i.contains(".")){
										List<String> impo = Arrays.asList(i.split("\\."));
										i = impo.get(impo.size()-2);
										for(PackageElement p : packages){
											if(p.getName().equals(i)){
												if(!dependencies.contains(p)){
													this.nDependencies+=1;
													nDependencies+=1;
													dependencies.add(p);
												}
												break;
											}
										}
									}
								}
								// stop analyzing the code when the class of the file starts.
								// the imports occur before the class initialization. 
								else if (line.contains("class")) {
									stop = true;
								}
							}
						}
						//By counting only on the while, the number of lines would be 1 less than the real value.
						//So we compensate that by adding one line in the end.
						nLines+=1;
						in.close();
					}
					catch (IOException exception) {}
				}
			}
		}
		
		PackageInfo packInfo = new PackageInfo(element, nClass, nLines, nDependencies, dependencies);
		return packInfo;
	}
	
	private void showGraph(Composite viewArea, Map<String, Image> imageMap){
		//layout
		viewArea.setLayout(new FillLayout());
		
		//List of graphs
		//Each extension has a different graph
		//set Graph
		Graph g = new Graph(viewArea, SWT.NONE);
		g.setConnectionStyle(ZestStyles.CONNECTIONS_DIRECTED);
		
		Map<PackageElement, GraphNode> graphNodes = new HashMap<PackageElement, GraphNode>();
		
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IConfigurationElement[] extensions = reg.getConfigurationElementsFor("pa.iscde.packdep.packdepStyleCreator");
		//if there are no extensions
		if(extensions.length == 0){
			for(PackageInfo p : pInfo){
				GraphNode n = new GraphNode(g, SWT.NONE, p.getName(), imageMap.get("p2.png"));
				n.setBackgroundColor(new Color(Display.getDefault(), 255, 134, 59));
				n.setForegroundColor(new Color(Display.getDefault(), 255, 255, 255));
				n.setHighlightColor(new Color(Display.getDefault(), 255, 11, 0));
				n.setFont(new Font(Display.getDefault(), "style", 8, 3));
				n.setBorderColor(new Color(Display.getDefault(), 0, 0, 0));
				n.setBorderHighlightColor(new Color(Display.getDefault(), 0, 0, 0));
				g.setBackground(new Color(Display.getDefault(), 255, 255, 255));
				graphNodes.put(p.getElement(), n);
			}
		}
		//if there's extensions
		else{
			for (int i = 0; i < extensions.length; i++) {
				IConfigurationElement element = extensions[i];
				try {
					//get parameters
					packdepStyle e = (packdepStyle) element.createExecutableExtension("class");
					for(PackageInfo p : pInfo){
						e.init(gInfo);
						Image icon = e.getIcon(p);
						Color packageColor = e.getColor(p);
						Color letterColor = e.getTextColor(p);
						Color highlightColor = e.getHighlightColor(p);
						Color borderColor = e.getBorderColor(p);
						Color borderHighlightColor = e.getBorderHighlightColor(p);
						Color backgroundColor = e.getBackgroundColor(p);
						Font letterFont = e.getTextFont(p);
						PackageSize size = e.getSize(p);
						//check if extensions has everything completed or if has methods that return null.
						//if null, a default value is used.
						GraphNode n;
						if(icon == null){
							n = new GraphNode(g, SWT.NONE, p.getName(), imageMap.get("p2.png"));
						}
						else{
							n = new GraphNode(g, SWT.NONE, p.getName(), e.getIcon(p));
						}
						if(packageColor != null){
							n.setBackgroundColor(packageColor);
						}
						else{
							n.setBackgroundColor(new Color(Display.getDefault(), 255, 134, 59));
						}
						if(letterColor != null){
							n.setForegroundColor(letterColor);
						}
						else{
							n.setForegroundColor(new Color(Display.getDefault(), 255, 255, 255));
						}
						if(highlightColor != null){
							n.setHighlightColor(highlightColor);
						}
						else{
							n.setHighlightColor(new Color(Display.getDefault(), 255, 11, 0));
						}
						if(letterFont != null){
							n.setFont(letterFont);
						}
						else{
							n.setFont(new Font(Display.getDefault(), "style", 8, 3));
						}
						if(borderColor != null){
							n.setBorderColor(borderColor);
						}
						else{
							n.setBorderColor(new Color(Display.getDefault(), 0, 0, 0));
						}
						if(borderHighlightColor != null){
							n.setBorderHighlightColor(borderHighlightColor);
						}
						else{
							n.setBorderHighlightColor(new Color(Display.getDefault(), 0, 0, 0));
						}
						if(backgroundColor != null){
							g.setBackground(backgroundColor);
						}
						else{
							g.setBackground(new Color(Display.getDefault(), 255, 255, 255));
						}
						//size does not have a default size because it adjusts without a parameter.
						if(size != null){
							n.setSize(size.getWidth(), size.getHeight());
						}
						
						graphNodes.put(p.getElement(), n);
					}
				}
				catch(Exception e){}
			}
		}
		
		//connections. Dependencies.
		for(PackageInfo packageInfo: pInfo){
			ArrayList<PackageElement> dependencies = packageInfo.getDependencies();
			if(!dependencies.isEmpty()){
				for(PackageElement e: dependencies){
					new GraphConnection(g, SWT.NONE, graphNodes.get(packageInfo.getElement()), graphNodes.get(e));
				}
			}
		}
		
		/*for (PackageElement key : map.keySet()) {
		     Collection<PackageElement> values = map.get(key);
		     for (PackageElement p: values){
		    	 new GraphConnection(g, SWT.NONE, graph.get(key), graph.get(p));
		     }
		   }*/
		
		g.setLayoutAlgorithm(new SpringLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
	}
	

	
	private void testPrintInformation() {
		System.out.println("Packages information:");
		for (PackageInfo packageInfo : pInfo) {
			System.out.println("name: "+packageInfo.getName());
			System.out.println("number of classes: "+packageInfo.getnClass());
			System.out.println("number of lines: "+packageInfo.getnLines());
			System.out.println("number of dependencies: "+packageInfo.getnDependencies());
			System.out.println("Dependencias:");
			ArrayList<PackageElement> d = packageInfo.getDependencies();
			for (PackageElement packageElement : d) {
				System.out.println(packageElement.getName());
			}
			System.out.println("----");
		}
		System.out.println("\nGlobal information:");
		System.out.println("number of classes: "+gInfo.getnClasses());
		System.out.println("number of packages: "+gInfo.getnPackages());
		System.out.println("number of Dependencies: "+gInfo.getnDependencies());
	}
}