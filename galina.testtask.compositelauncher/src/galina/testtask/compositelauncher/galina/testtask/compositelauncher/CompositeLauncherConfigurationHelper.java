package galina.testtask.compositelauncher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;

public class CompositeLauncherConfigurationHelper {
	private final static Logger logger = Logger.getLogger(CompositeLauncherConfigurationHelper.class.toString());

	public final static String ATTRIBUTE_CHOSEN_CONFIGURATIONS = "CHOSEN_CONFIGURATIONS";
	public final static String ATTRIBUTE_EXECUTION_TYPE_PARALLEL = "PARALLEL";
	public final static String ATTRIBUTE_EXECUTION_TYPE_SEQUENTIAL = "SEQUENTIAL";
	public final static String ATTRIBUTE_CONFIGURATIONS_EXECUTION_TYPE = "CONFIGURATIONS_EXECUTION_TYPE";
	public final static String COMPOSITE_LAUNCH_TYPE_ID = "galina.testtask.compositelauncher";

	private static List<ILaunchConfiguration> getAvailableLauncherConfigurations() {
		try {
			return new ArrayList<ILaunchConfiguration>(
					Arrays.asList(DebugPlugin.getDefault().getLaunchManager().getLaunchConfigurations()));
		} catch (CoreException e) {
			logger.log(Level.WARNING, "Available configurations does not load " + e.getMessage());
		}
		return new ArrayList<ILaunchConfiguration>();
	}

	public static List<LaunchConfigurationItem> getAvailableLauncherConfigurations(String mode) {
		List<LaunchConfigurationItem> availableConfigurationsFiltered = new ArrayList<>();
		List<ILaunchConfiguration> availableConfigurations = getAvailableLauncherConfigurations();

		availableConfigurations.stream().filter(config -> {
			try {
				return config.supportsMode(mode);
			} catch (CoreException e) {
				logger.log(Level.WARNING,
						"Can not define mode supporting for " + config.getName() + " " + e.getMessage());
			}
			return false;
		}).forEach(config -> 
			availableConfigurationsFiltered.add(getConfigurationItem(config)));

		return availableConfigurationsFiltered;
	}

	public static Optional<LaunchConfigurationItem> getConfigurationByName(String name, String mode) {
		List<LaunchConfigurationItem> availableConfigurations = getAvailableLauncherConfigurations(mode);
		return availableConfigurations.stream()
				.filter(config -> config.getName().equals(name))
				.findFirst();
	}

	public static List<LaunchConfigurationItem> getChosenConfigurations(ILaunchConfiguration configuration, String mode) {
		List<LaunchConfigurationItem> chosenConfigurations = new ArrayList<LaunchConfigurationItem>();
		try {
			List<String> names = configuration.getAttribute(
					CompositeLauncherConfigurationHelper.ATTRIBUTE_CHOSEN_CONFIGURATIONS, new ArrayList<String>());
			names.stream()
				.forEach(name ->
					getConfigurationByName(name, mode).ifPresent(config ->
						chosenConfigurations.add(config)));
		} catch (CoreException e) {
			logger.log(Level.WARNING, "Chosen configurations does not load " + e.getMessage());
		} 
		return chosenConfigurations;
	}

	public static boolean isComposite(ILaunchConfiguration configuration) {
			try {
				return configuration.getType().getIdentifier().equals(COMPOSITE_LAUNCH_TYPE_ID);
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			return false;
	}



	public static List<ILaunchConfiguration> getCompositeConfigurations(ILaunchConfiguration configuration,
			String mode) {
		return getChosenConfigurations(configuration, mode).stream()
					.map(c -> c.getConfiguration())
					.filter(c -> isComposite(c))
					.collect(Collectors.toList());
	}

	public static String getConfigurationExecutionType(ILaunchConfiguration configuration) {
		try {
			return configuration.getAttribute(ATTRIBUTE_CONFIGURATIONS_EXECUTION_TYPE,
					ATTRIBUTE_EXECUTION_TYPE_SEQUENTIAL);
		} catch (CoreException e) {
			logger.log(Level.WARNING, "Can not define type correctly " + e.getMessage());
		}
		return ATTRIBUTE_EXECUTION_TYPE_SEQUENTIAL;
	}

	public static boolean isParallel(ILaunchConfiguration configuration) {
		return getConfigurationExecutionType(configuration).equals(ATTRIBUTE_EXECUTION_TYPE_PARALLEL);
	}

	public static List<LaunchConfigurationItem> getConfigurationItems(List<ILaunchConfiguration> launchConfViewerInput) {
		return launchConfViewerInput.stream()
				.map(config -> getConfigurationItem(config))
				.filter(config -> 
					config != null)
				.collect(Collectors.toList());
	}
	
	public static LaunchConfigurationItem getConfigurationItem(ILaunchConfiguration launchConfViewerInput) {
		try {
			return LaunchConfigurationItem.of(launchConfViewerInput, launchConfViewerInput.getName(),launchConfViewerInput.getType(), launchConfViewerInput.getModes());
		} catch (CoreException e) {
			logger.log(Level.WARNING, "Can not wrap configuration " + e.getMessage());
			return null;
		}	
	}

	public static boolean equals(ILaunchConfiguration config, ILaunchConfiguration anotherConfiguration) {
		return config.getName().equals(anotherConfiguration.getName());
	}
}

