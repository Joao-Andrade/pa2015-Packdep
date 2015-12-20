package pa.iscde.packdep;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphNode;

import pa.iscde.packdep.info.PackageStyleInfo;
import pt.iscte.pidesco.extensibility.PidescoTool;

//menu that appears on the upper right corner of the view.
public class StyleSelector implements PidescoTool {

	static Menu menu;
	static Graph graph;
	//all styles. From the extensions.
	static ArrayList<ArrayList<PackageStyleInfo>> styles;
	//Graph Nodes
	static ArrayList<GraphNode> nodes;
	
	//initiate variables
	public static void init(Graph g, ArrayList<GraphNode> n, ArrayList<ArrayList<PackageStyleInfo>> s){
		graph = g;
		nodes = n;
		styles = s;
		
		//build menu
		menu = new Menu(graph);
	    
	    Menu menuStyles = new Menu(menu);
	    for(int i = 0; i< styles.size(); i++){
	    	final int numberStyle = i;
	    	MenuItem style1 = new MenuItem(menu, SWT.CASCADE);
		    style1.setText(styles.get(i).get(0).getName());

		    //o acontece quando clicas no item 1
		    style1.addSelectionListener(new SelectionAdapter() {
		      public void widgetSelected(SelectionEvent e) {
		        changeStyle(numberStyle);
		      }
		    });
	    }
	    
	    graph.setMenu(menu);
	}
	
	//change style
	private static void changeStyle(int style){
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
				graph.setBackground(p.getBackgroundColor());
				i++;
			}
		}
	}
	
	//show menu to change style.
	@Override
	public void run(boolean activate) {
	    menu.setVisible(true);
	}

}
