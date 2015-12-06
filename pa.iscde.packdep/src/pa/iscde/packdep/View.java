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
import pt.iscte.pidesco.extensibility.PidescoView;
import pt.iscte.pidesco.javaeditor.service.JavaEditorListener;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;
import pt.iscte.pidesco.projectbrowser.model.PackageElement;
import pt.iscte.pidesco.projectbrowser.model.SourceElement;
import pt.iscte.pidesco.projectbrowser.service.ProjectBrowserListener;
import pt.iscte.pidesco.projectbrowser.service.ProjectBrowserServices;

//Show package Dependencies in a window.
public class View implements PidescoView {

	//extension point layout parameters
	int packageSizeHeight;
	int packageSizeWidth;
	Color packageBackgroundColor;
	Color packageForegroundColor;
	Font packageFont;
	int highlightedSizeHeight;
	int highlightedSizeWidth;
	Color highlightedBackgroundColor;
	Color highlightedForegroundColor;
	Font highlightedFont;
	Image icon;
	Color backgroundColor;
	int connectionStyle;
	
	
	//graph
	Graph g;
	
	//package selected
	GraphNode packSelected;
	
	//all packages
	ArrayList<PackageElement> packages;
	
	//----Viewer----
	
	
	@Override
	public void createContents(Composite viewArea, Map<String, Image> imageMap) {

		// services
		JavaEditorServices editorService = Activator.getEditorService();
		ProjectBrowserServices projectService = Activator.getProjectService();
		
		getLayoutExtensionInfo(imageMap);
		
		// all packages
		packages = getAllPackages(projectService);
		
		//dependencies
		Multimap<PackageElement, PackageElement> dependencies = getDependencies(packages);
		
		//show graph of packages
		showPackDep(viewArea, imageMap, packages, dependencies);
		
		
		//possivel forma de extensao do deepsearch
		searched("package1");
	}
	
	//get data from the plug in that uses this extension point
	private void getLayoutExtensionInfo(Map<String, Image> imageMap){
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IConfigurationElement[] extensions = reg.getConfigurationElementsFor("pa.iscde.packdep.packdepStyleCreator");
		for (int i = 0; i < extensions.length; i++) {
			IConfigurationElement element = extensions[i];
			try {
				//get parameters
				packdepStyle layoutExtension = (packdepStyle) element.createExecutableExtension("class");
				packageSizeHeight = layoutExtension.packageSizeHeight();
				packageSizeWidth = layoutExtension.packageSizeWidth();
				packageBackgroundColor = layoutExtension.packageBackgroundColor();
				packageForegroundColor = layoutExtension.packageForegroundColor();
				packageFont = layoutExtension.packageFont();
				highlightedSizeHeight = layoutExtension.highlightedSizeHeight();
				highlightedSizeWidth = layoutExtension.highlightedSizeWidth();
				highlightedBackgroundColor = layoutExtension.highlightedBackgroundColor();
				highlightedForegroundColor = layoutExtension.highlightedForegroundColor();
				highlightedFont = layoutExtension.highlightedFont();
				icon =layoutExtension.icon();
				backgroundColor = layoutExtension.backgroundColor();
				connectionStyle = layoutExtension.connectionStyle();
				
				//verify parameters
				//if some is not well defined, the default value is applied.
				if(packageSizeHeight <= 0){
					packageSizeHeight = 50;
				}
				if(packageSizeWidth <= 0){
					packageSizeWidth = 100;
				}
				if(packageBackgroundColor == null){
					packageBackgroundColor = new Color(Display.getDefault(), 255, 134, 59);
				}
				if(packageForegroundColor == null){
					packageForegroundColor = new Color(Display.getDefault(), 255, 255, 255);
				}
				if(packageFont == null){
					packageFont = new Font(Display.getDefault(), "style", 10, 3);
				}
				if(highlightedSizeHeight <= 0){
					highlightedSizeHeight = 50;
				}
				if(highlightedSizeWidth <= 0){
					highlightedSizeWidth = 100;
				}
				if(highlightedBackgroundColor == null){
					highlightedBackgroundColor = new Color(Display.getDefault(), 255, 11, 0);
				}
				if(highlightedForegroundColor == null){
					highlightedForegroundColor = new Color(Display.getDefault(), 255, 255, 255);
				}
				if(highlightedFont == null){
					highlightedFont = new Font(Display.getDefault(), "style", 10, 3);
				}
				if(icon == null){
					icon = imageMap.get("icon.png");
				}
				if(backgroundColor == null){
					backgroundColor = new Color(Display.getDefault(), 255, 255, 255);
				}
				if(connectionStyle < 0){
					connectionStyle = ZestStyles.CONNECTIONS_DIRECTED;
				}
			} catch (Exception e) {
				System.err.println("nao deu.");
			}
		}
	}
	
	
	//----Functions----

	
	// get all packages on the workspace
	private ArrayList<PackageElement> getAllPackages(ProjectBrowserServices projectService) {
		// all packages.
		ArrayList<PackageElement> packages = new ArrayList<PackageElement>();

		// root element of workspace.
		PackageElement root_package = projectService.getRootPackage();

		// if workspace is not empty.
		if (root_package.hasChildren()) {
			// childs of root_package.
			SortedSet<SourceElement> pack_child = root_package.getChildren();

			// when searching for packages, the childs, that are packages and
			// are not yet searched, go here.
			ArrayList<PackageElement> searchPackages = new ArrayList<PackageElement>();

			// check if a child is package
			//this gets only the packages that are children of the root_package.
			for (SourceElement element : pack_child) {
				if (element.isPackage()) {
					searchPackages.add((PackageElement) element);
					packages.add((PackageElement) element);
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
		return packages;
	}

	//get package dependencies
	private Multimap<PackageElement, PackageElement> getDependencies(ArrayList<PackageElement> packages) {
		//Guava multimap
		//to associate each package with the packages that depends on.
		Multimap<PackageElement,PackageElement> map = ArrayListMultimap.create();
		
		//check files in each package to check the imports.
		for (PackageElement p : packages) {
			//if package contains children
			if(p.hasChildren()){
				SortedSet<SourceElement> c = p.getChildren();
				//check if child is a class
				for(SourceElement e : c){
					if(e.isClass()){
						//get imports of the class
						ArrayList<PackageElement> imports = getImports(e, packages);
						for(PackageElement pk : imports){
							map.put(p, pk);
						}
					}
				}
			}
		}
			
		return map;
	}
	
	private ArrayList<PackageElement> getImports(SourceElement e, ArrayList<PackageElement> packages){
		ArrayList<PackageElement> imports = new ArrayList<PackageElement>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(e.getFile()));
			String line;
			boolean stop = false;
			ArrayList<String> dependencies = new ArrayList<String>();
			while ((line = in.readLine()) != null && !stop) {
				// if it is an import and not a word import somewhere on the code.
				if (line.indexOf("import") == 0) {
					// get only the import.
					//Removes the word import.
					dependencies.add(line.replace("import ", ""));
				}
				// stop analysing the code when the class of the file starts.
				// the imports occur before the class initialization. 
				else if (line.contains("class")) {
					stop = true;
				}
			}
			for (String string : dependencies) {
				String dependencie;
				//if import is not to a class that is on same package
				if(string.contains(".")){
					List<String> impo = Arrays.asList(string.split("\\."));
					dependencie = impo.get(impo.size()-2);
					//compare with packages names of the workspace
					for(PackageElement p : packages){
						if(p.getName().equals(dependencie) && !imports.contains(p)){
							imports.add(p);
						}
					}
				}
			}
		}
		catch (IOException exception) {}
		return imports;
		
	}
	
	//show graphically the package dependencies.
	private void showPackDep(Composite viewArea, Map<String, Image> imageMap, ArrayList<PackageElement> packages, Multimap<PackageElement, PackageElement> map){
		//layout
		viewArea.setLayout(new FillLayout());
		//set Graph
		g = new Graph(viewArea, SWT.NONE);
		g.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				//set node selected style properties.
				try{
					GraphNode selected = (GraphNode)((Graph) e.widget).getSelection().get(0);
					newPackSelected(selected);
				}
				catch(IndexOutOfBoundsException ioobe){
					newPackSelected(null);
				}
				
			}
		});
		g.setConnectionStyle(connectionStyle);
		g.setBackground(backgroundColor);
		//packages.
		//Map each GraphNode to the respective package as long as they have a connection in the dependencies
		Map<PackageElement, GraphNode> graph = new HashMap<PackageElement, GraphNode>();
		for (PackageElement packageElement : packages) {
			if(map.containsKey(packageElement) || map.containsValue(packageElement)){
				GraphNode n1 = new GraphNode(g, SWT.NONE, packageElement.getName(), icon);
				//set style Properties.
				n1.setSize(packageSizeWidth, packageSizeHeight);
				n1.setBackgroundColor(packageBackgroundColor);
				n1.setForegroundColor(packageForegroundColor);
				n1.setHighlightColor(highlightedBackgroundColor);
				n1.setFont(packageFont);
				graph.put(packageElement, n1);
			}
		}
		
		//Dependencies
		for (PackageElement key : map.keySet()) {
		     Collection<PackageElement> values = map.get(key);
		     for (PackageElement p: values){
		    	 new GraphConnection(g, SWT.NONE, graph.get(key), graph.get(p));
		     }
		   }
		
		
		g.setLayoutAlgorithm(new SpringLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
	}
	
	//Node selected style properties.
	private void newPackSelected(GraphNode selected){
		//undo previous selected node style properties.
		if(packSelected != null){
			packSelected.setSize(packageSizeWidth, packageSizeHeight);
			packSelected.setForegroundColor(packageForegroundColor);
			packSelected.setFont(packageFont);
		}
		if(selected != null){
			//apply highlighted properties to the selected node.
			selected.setSize(highlightedSizeWidth, highlightedSizeHeight);
			selected.setForegroundColor(highlightedForegroundColor);
			selected.setFont(highlightedFont);
			packSelected = selected;
		}
	}
	
	
	
	
	//--Extensions--
	
	//DeepSearch
	//supostamente deve dar o texto do resultado. Ou um array dos resultados....
	public void searched(String word){
		for (PackageElement p : packages) {
			if(p.getName().equals(word)){
				selectPackage(p);
				return;
			}
			else if(p.hasChildren()){
				SortedSet<SourceElement> c = p.getChildren();
				//check if child is a class
				for(SourceElement e : c){
					if(e.isClass()){
						//remove ".java"
						String name = (String)e.getName().subSequence(0, e.getName().length()-5);
						if(name.equals(word)){
							selectPackage(p);
							return;
						}
					}
				}
			}
		}
		//if searched word is not a package or class
		selectPackage(null);
	}
	
	private void selectPackage(PackageElement p){
		//if searched word is not a package or class
		if(p == null){
			g.setSelection(new GraphItem[]{});
			return;
		}
		//else select the currespondent node
		List<GraphNode> nodes = (List<GraphNode>)g.getNodes();
		for (GraphNode n : nodes) {
			if(p.getName().equals(n.getText())){
				g.setSelection(new GraphItem[]{(GraphItem)n});
				newPackSelected(n);
			}
		}
	}
}