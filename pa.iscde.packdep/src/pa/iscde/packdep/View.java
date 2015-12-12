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
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
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
import pa.iscde.packdep.info.PackageStyleInfo;
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
	
	//all styles. From the extensions.
	ArrayList<ArrayList<PackageStyleInfo>> styles;
	
	//Graph
	Graph g;
	
	//Graph Nodes
	ArrayList<GraphNode> nodes;
	
	//array with all Packages information
	ArrayList<PackageInfo> pInfo;
	
	//global information
	GlobalInfo gInfo;
	
	//variables to define global information
	int nPackages;
	int nDependencies;
	int nClasses;
	
	@Override
	public void createContents(Composite viewArea, Map<String, Image> imageMap) {
		
		packages = new ArrayList<PackageElement>();
		styles = new ArrayList<ArrayList<PackageStyleInfo>>();
		
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
		g = new Graph(viewArea, SWT.NONE);
		g.setConnectionStyle(ZestStyles.CONNECTIONS_DIRECTED);
		
		Map<PackageElement, GraphNode> graphNodes = new HashMap<PackageElement, GraphNode>();
		nodes = new ArrayList<GraphNode>();
		//Default Style
		ArrayList<PackageStyleInfo> styleArray = new ArrayList<PackageStyleInfo>();
		for(PackageInfo p : pInfo){
			Image icon = imageMap.get("p2.png");
			Color packageColor = new Color(Display.getDefault(), 255, 134, 59);
			Color letterColor = new Color(Display.getDefault(), 255, 255, 255);
			Color highlightColor = new Color(Display.getDefault(), 255, 11, 0);
			Color borderColor = new Color(Display.getDefault(), 0, 0, 0);
			Color borderHighlightColor = new Color(Display.getDefault(), 0, 0, 0);
			Color backgroundColor = new Color(Display.getDefault(), 255, 255, 255);
			Font letterFont = new Font(Display.getDefault(), "style", 8, 3);
			GraphNode n = new GraphNode(g, SWT.NONE, p.getName(), icon);
			n.setBackgroundColor(packageColor);
			n.setForegroundColor(letterColor);
			n.setHighlightColor(highlightColor);
			n.setFont(letterFont);
			n.setBorderColor(borderColor);
			n.setBorderHighlightColor(borderHighlightColor);
			g.setBackground(backgroundColor);
			System.out.println("Name " + p.getName());
			PackageSize size = new PackageSize(n.getSize().width, n.getSize().height);
			n.setSize(size.getWidth(), size.getHeight());
			nodes.add(n);
			graphNodes.put(p.getElement(), n);
			PackageStyleInfo style1 = new PackageStyleInfo("default style", packageColor, letterColor, highlightColor, borderColor, borderHighlightColor, size, letterFont, backgroundColor, icon);
			styleArray.add(style1);
		}
		styles.add(styleArray);
		
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IConfigurationElement[] extensions = reg.getConfigurationElementsFor("pa.iscde.packdep.packdepStyleCreator");
		//if there's extensions
		for (int i = 0; i < extensions.length; i++) {
			//new style
			styleArray = new ArrayList<PackageStyleInfo>();
			IConfigurationElement element = extensions[i];
			try {
				//get parameters
				packdepStyle e = (packdepStyle) element.createExecutableExtension("class");
				int number = 0;
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
					if(icon == null){
						icon = imageMap.get("p2.png");
					}
					if(packageColor == null){
						packageColor = new Color(Display.getDefault(), 255, 134, 59);
					}
					if(letterColor == null){
						letterColor = new Color(Display.getDefault(), 255, 255, 255);
					}
					if(highlightColor == null){
						highlightColor = new Color(Display.getDefault(), 255, 11, 0);
					}
					if(letterFont == null){
						letterFont = new Font(Display.getDefault(), "style", 8, 3);
					}
					if(borderColor == null){
						borderColor = new Color(Display.getDefault(), 0, 0, 0);
					}
					if(borderHighlightColor == null){
						borderHighlightColor = new Color(Display.getDefault(), 0, 0, 0);
					}
					if(backgroundColor == null){
						backgroundColor = new Color(Display.getDefault(), 255, 255, 255);
					}
					if(size == null){
						size = styles.get(0).get(number).getSize();
					}
					number = number + 1;
					PackageStyleInfo style1 = new PackageStyleInfo(element.getAttribute("name"), packageColor, letterColor, highlightColor, borderColor, borderHighlightColor, size, letterFont, backgroundColor, icon);
					styleArray.add(style1);
				}
			}
			catch(Exception e){}
			styles.add(styleArray);
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
		
		g.setLayoutAlgorithm(new SpringLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
		
		popUpExample();
	}
	
	
	private void popUpExample(){
		
		//Os comentarios disto estao em portugues. Depois apaga isto.
		//Ou altera como quiseres desde que os comentarios
		//fiquem todos iguais. Em ingles. Ou entao mudasse tudo para portugues.

		//pode ser final porque a variavel não vai mudar.
		final Menu menu = new Menu(g);
		//item 1
	    MenuItem subMenuStyles = new MenuItem(menu, SWT.CASCADE);
	    subMenuStyles.setText("Styles");

	    //o acontece quando clicas no item 1
	    subMenuStyles.addSelectionListener(new SelectionAdapter() {
	      public void widgetSelected(SelectionEvent e) {
	        System.out.println("Styles");
	      }
	    });
	    
	    Menu menuStyles = new Menu(menu);
	    for(int i = 0; i< styles.size(); i++){
	    	final int numberStyle = i;
	    	MenuItem style1 = new MenuItem(menuStyles, SWT.CASCADE);
		    style1.setText(styles.get(i).get(0).getName());

		    //o acontece quando clicas no item 1
		    style1.addSelectionListener(new SelectionAdapter() {
		      public void widgetSelected(SelectionEvent e) {
		        changeStyle(numberStyle);
		      }
		    });
	    }
	    
	    subMenuStyles.setMenu(menuStyles);
	    
	    
	    
	    
	    
	    //colocar menu no grafico
	    g.setMenu(menu);
	    
	    //botao direito chama o menu
	    g.addMouseListener(new MouseListener(){
	    	@Override
	    	public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
				//botao direito
				if(e.button == 2){
					menu.setVisible(true);
				}
			}

			@Override
			public void mouseUp(MouseEvent e) {
			}
	    });
	}

	private void changeStyle(int style){
		int i = 0;
		if(!nodes.isEmpty()){
			for(GraphNode n : nodes){
				PackageStyleInfo p = styles.get(style).get(i);
				n.setBackgroundColor(p.getColor());
				n.setForegroundColor(p.getTextColor());
				n.setHighlightColor(p.getHighlightColor());
				n.setBorderColor(p.getBorderColor());
				n.setBorderHighlightColor(p.getBorderHighlightColor());
				n.setFont(p.getTextFont());
				n.setImage(p.getIcon());
				n.setSize(p.getSize().getWidth(), p.getSize().getHeight());
				g.setBackground(p.getBackgroundColor());
				i++;
			}
		}
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