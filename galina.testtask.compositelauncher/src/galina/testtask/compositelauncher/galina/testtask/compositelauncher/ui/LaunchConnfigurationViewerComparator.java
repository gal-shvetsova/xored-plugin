package galina.testtask.compositelauncher.ui;

import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

import galina.testtask.compositelauncher.LaunchConfigurationItem;

public class LaunchConnfigurationViewerComparator extends ViewerComparator {
	@Override
    public int compare(Viewer viewer, Object o1, Object o2) {
        if (o1 instanceof LaunchConfigurationItem && o2 instanceof LaunchConfigurationItem) {
        	LaunchConfigurationItem item = (LaunchConfigurationItem) o1;
        	LaunchConfigurationItem anotherItem = (LaunchConfigurationItem) o2;
            return !item.getType().equals(anotherItem.getType()) ? 
            		item.getType().getName().compareTo(anotherItem.getType().getName()) :
            		item.getConfiguration().getName().compareTo(anotherItem.getConfiguration().getName());
        } else if (o1 instanceof ILaunchConfigurationType && o2 instanceof ILaunchConfigurationType) {
        	ILaunchConfigurationType type = (ILaunchConfigurationType) o1;
        	ILaunchConfigurationType anotherType = (ILaunchConfigurationType) o2;
        	return type.getName().compareTo(anotherType.getName());
        }
        return super.compare(viewer, o1, o2);
    }
}
