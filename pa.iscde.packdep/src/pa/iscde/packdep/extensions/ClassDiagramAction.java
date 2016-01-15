package pa.iscde.packdep.extensions;

import java.util.Map;

import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphNode;

import pa.iscde.packdep.View;
import pt.iscde.classdiagram.extensibility.IClassDiagramAction;
import pt.iscde.classdiagram.model.TopLevelElement;
import pt.iscte.pidesco.projectbrowser.model.PackageElement;

public class ClassDiagramAction implements IClassDiagramAction {
	
	@Override
	public void run(TopLevelElement selectedElement) {
		if(selectedElement!=null){
			System.out.println(selectedElement.getName());
			selectedElement.setSelected(true);
			
			View.diagramAction(selectedElement.getName());
		}else{
			System.out.println("Nenhum elemento");
		}
	}



}
