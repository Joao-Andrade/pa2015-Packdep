package pa.iscde.packdep.extensions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphItem;
import org.eclipse.zest.core.widgets.GraphNode;

import extensionpoints.ISearchEvent;
import extensionpoints.ISearchEventListener;
import extensionpoints.Item;
import pa.iscde.packdep.Activator;
import pa.iscde.packdep.info.PackageInfo;
import pt.iscte.pidesco.projectbrowser.model.PackageElement;

public class DeepSearchService {

	public DeepSearchService(final Graph g, final Map<PackageElement, GraphNode> p) {
		final ISearchEvent event = Activator.getSearchService();
		event.addListener(new ISearchEventListener() {
			@Override
			public void searchEvent(String text_Search, String text_SearchInCombo, String specificText_SearchInCombo,
					String text_SearchForCombo, Collection<String> buttonsSelected_SearchForCombo) {
					String pack;
					if(!text_Search.isEmpty()){
						if((text_SearchInCombo.isEmpty() || text_SearchInCombo.equals("Package"))
								&& (text_SearchForCombo.isEmpty() || text_SearchForCombo.equals("Package"))){
							if(!specificText_SearchInCombo.isEmpty()){
								String[] split = specificText_SearchInCombo.split("\\.");
								pack = split[split.length-1];
							}
							else{
								pack = text_Search;
							}
							
							
							ArrayList<GraphNode> nodes = new ArrayList<GraphNode>();
							for(PackageElement i : p.keySet()){
								if(i.getName().equalsIgnoreCase(pack) || i.getName().contains(pack)){
									nodes.add(p.get(i));
								}
							}
							GraphItem[] selection = new GraphItem[nodes.size()];
							for(int i = 0; i < nodes.size(); i++){
								selection[i] = (GraphItem)nodes.get(i);
							}
							g.setSelection(selection);
						}
						else{
							g.setSelection(null);
						}
					}
					else{
						g.setSelection(null);
					}
				
			}
		});
	}
	
}
