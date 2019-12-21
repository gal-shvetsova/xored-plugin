package galina.testtask.compositelauncher;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.debug.core.model.IProcess;

public class CompositeLauncherDelegate implements ILaunchConfigurationDelegate {
	private final Logger logger = Logger.getLogger(CompositeLauncherDelegate.class.toString());

	@Override
	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor)
			throws CoreException {
		List<ILaunchConfiguration> launchConfigurationsList = CompositeLauncherConfigurationHelper
				.getChosenConfigurations(configuration, mode).stream()
					.map(c -> c.getConfiguration())
					.collect(Collectors.toList());

		if (CompositeLauncherConfigurationHelper.isParallel(configuration)) {
			launchParallel(configuration, mode, launch, monitor);
		} else {
			launchSequential(configuration, launchConfigurationsList, mode, monitor);
		}

		monitor.done();

	}

	private void launchParallel(ILaunchConfiguration configuration, String mode, ILaunch launch,
			IProgressMonitor monitor) {
		CompositeLauncherConfigurationHelper.getChosenConfigurations(configuration, mode).stream().forEach(config -> {
			System.out.println("working");
			if (!monitor.isCanceled()) {
				try {
					ILaunch configurationLaunch = config.getConfiguration().launch(mode, monitor);

					for (IDebugTarget debugTarget : configurationLaunch.getDebugTargets()) {
						launch.addDebugTarget(debugTarget);
					}
					for (IProcess process : configurationLaunch.getProcesses()) {
						launch.addProcess(process);
					}
				} catch (CoreException e) {
					logger.log(Level.WARNING, "Can not launch " + config.getName() + " " + e.getMessage());
				}
			}
		});
	}

	private void launchSequential(ILaunchConfiguration configuration, List<ILaunchConfiguration> configurationsList,
			String mode, IProgressMonitor monitor) throws CoreException {

		ILaunch previousLaunch = configurationsList.get(0).launch(mode, monitor);
		if (configurationsList.isEmpty()) {
			return;
		}
		configurationsList.remove(0);
		
		for (ILaunchConfiguration config : configurationsList) {
			if (previousLaunch != null) {
				monitor.subTask(config.getName());
				while (!monitor.isCanceled() && !previousLaunch.isTerminated()) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						break;
					}
				}
				previousLaunch = config.launch(mode, monitor);
			}
		}
	}
}
