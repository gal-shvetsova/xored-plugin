package galina.testtask.compositelauncher.ui;

import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.dialogs.PatternFilter;

public class LaunchConfigurationPatternFilter extends PatternFilter {

	@Override
	protected boolean isLeafMatch(Viewer viewer, Object element) {
        String labelText = ((LaunchConfigurationLabelProvider) ((StructuredViewer) viewer).getLabelProvider()).getColumnText(element, 0);
        if(labelText == null) {
            return false;
        }
        return wordMatches(labelText);  
	}

}
