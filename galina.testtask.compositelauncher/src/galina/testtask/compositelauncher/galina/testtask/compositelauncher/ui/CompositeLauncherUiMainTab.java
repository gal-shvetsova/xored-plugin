package galina.testtask.compositelauncher.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.dialogs.FilteredTree;
import galina.testtask.compositelauncher.CompositeLauncherConfigurationHelper;
import galina.testtask.compositelauncher.CompositeLauncherValidator;
import galina.testtask.compositelauncher.LaunchConfigurationItem;

import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.jface.viewers.TreeViewer;

public class CompositeLauncherUiMainTab extends AbstractLaunchConfigurationTab {
	public final static Logger logger = Logger.getLogger(CompositeLauncherUiMainTab.class.toString());
	private List<LaunchConfigurationItem> availableConfigurations;
	private TreeViewer availableConigurationsViewer;
	private Tree availableLaunchConfViewerTree;
	private List<LaunchConfigurationItem> chosenConfigurations;
	private TreeViewer chosenConigurationsViewer;
	private Tree chosenLaunchConfViewerTree;
	private Button btnParallel;
	private Button btnSequential;
	private String currentType = CompositeLauncherConfigurationHelper.ATTRIBUTE_EXECUTION_TYPE_SEQUENTIAL;
	private String mode;

	public CompositeLauncherUiMainTab(String mode) {
		this.availableConfigurations = CompositeLauncherConfigurationHelper.getAvailableLauncherConfigurations(mode);
		this.mode = mode;
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	public void createControl(Composite parent) {
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setMinHeight(300);
		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		GridLayout layout = new GridLayout();
		scrolledComposite.setLayout(layout);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		init(scrolledComposite);
		setControl(scrolledComposite);
	}

	@Override
	public String getName() {
		return CompositeLauncherUiStandartElements.CONFIGURATIONS_TAB_TEXT;
	}

	@Override

	public void initializeFrom(ILaunchConfiguration configuration) {
		chosenConfigurations = CompositeLauncherConfigurationHelper.getChosenConfigurations(configuration, mode);
		setAvailableConfigurationsInput();
		setChosenConfigurationsInput();
		initExecutionType();
	}

	private void setAvailableConfigurationsInput() {
		if (availableConigurationsViewer != null && availableConfigurations != null) {
			availableConigurationsViewer.setInput(availableConfigurations);
		}
	}

	private void setChosenConfigurationsInput() {
		if (chosenConigurationsViewer != null && chosenConfigurations != null) {
			chosenConigurationsViewer.setInput(chosenConfigurations);
		}
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		java.util.List<String> names = chosenConfigurations.stream().map(config -> config.getName())
				.collect(Collectors.toList());
		configuration.setAttribute(CompositeLauncherConfigurationHelper.ATTRIBUTE_CHOSEN_CONFIGURATIONS, names);
		String type = btnParallel.getSelection()
				? CompositeLauncherConfigurationHelper.ATTRIBUTE_EXECUTION_TYPE_PARALLEL
				: CompositeLauncherConfigurationHelper.ATTRIBUTE_EXECUTION_TYPE_SEQUENTIAL;

		configuration.setAttribute(CompositeLauncherConfigurationHelper.ATTRIBUTE_CONFIGURATIONS_EXECUTION_TYPE, type);
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		currentType = CompositeLauncherConfigurationHelper.ATTRIBUTE_EXECUTION_TYPE_SEQUENTIAL;
		configuration.setAttribute(CompositeLauncherConfigurationHelper.ATTRIBUTE_CHOSEN_CONFIGURATIONS,
				new java.util.ArrayList<String>());
		configuration.setAttribute(CompositeLauncherConfigurationHelper.ATTRIBUTE_CONFIGURATIONS_EXECUTION_TYPE,
				currentType);
	}

	@Override
	public boolean isValid(ILaunchConfiguration launchConfiguration) {
		return super.isValid(launchConfiguration)
				&& CompositeLauncherValidator.validateConfiguration(launchConfiguration, mode)
				&& !chosenConfigurations.isEmpty();

	}

	private void init(ScrolledComposite composite) {

		Group mainGroup = new Group(composite, SWT.PUSH | SWT.FILL);
		mainGroup.setLayout(new FormLayout());
		GridData gd_mainGroup = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_mainGroup.heightHint = 267;
		gd_mainGroup.widthHint = 448;
		mainGroup.setLayoutData(gd_mainGroup);
		mainGroup.setText(CompositeLauncherUiStandartElements.MAIN_GROUP_TEXT);
		composite.setContent(mainGroup);
		composite.setMinSize(800, 210);

		Group groupAvailable = new Group(mainGroup, SWT.NONE);
		groupAvailable.setLayout(new GridLayout(1, false));
		FormData fd_groupAvailable = new FormData();
		fd_groupAvailable.left = new FormAttachment(0, 13);
		fd_groupAvailable.top = new FormAttachment(0, 20);
		fd_groupAvailable.bottom = new FormAttachment(100, -36);
		groupAvailable.setLayoutData(fd_groupAvailable);
		groupAvailable.setText(CompositeLauncherUiStandartElements.AVAILABLE_GROUP_TEXT);

		Group groupChosen = new Group(mainGroup, SWT.NONE);
		groupChosen.setLayout(new FormLayout());
		FormData fd_groupChosen = new FormData();
		fd_groupChosen.bottom = new FormAttachment(100, -31);
		fd_groupChosen.top = new FormAttachment(0, 25);
		fd_groupChosen.right = new FormAttachment(100, -79);
		groupChosen.setLayoutData(fd_groupChosen);
		groupChosen.setText(CompositeLauncherUiStandartElements.CHOSEN_GROUP_TEXT);

		Button toChosen = new Button(mainGroup, SWT.NONE);
		fd_groupChosen.left = new FormAttachment(50);
		FormData fd_toChosen = new FormData();
		fd_toChosen.bottom = new FormAttachment(0, 58);
		fd_toChosen.left = new FormAttachment(groupAvailable, 31);
		fd_toChosen.right = new FormAttachment(groupChosen, -24);
		fd_toChosen.top = new FormAttachment(0, 36);
		toChosen.setLayoutData(fd_toChosen);
		toChosen.setText(CompositeLauncherUiStandartElements.TO_CHOSEN_BUTTON_TEXT);

		Button toAvailable = new Button(mainGroup, SWT.NONE);
		fd_groupAvailable.right = new FormAttachment(32);
		FormData fd_toAvailable = new FormData();
		fd_toAvailable.bottom = new FormAttachment(toChosen, 28, SWT.BOTTOM);
		fd_toAvailable.top = new FormAttachment(toChosen, 6);
		fd_toAvailable.left = new FormAttachment(groupAvailable, 31);
		fd_toAvailable.right = new FormAttachment(groupChosen, -24);
		toAvailable.setLayoutData(fd_toAvailable);
		toAvailable.setText(CompositeLauncherUiStandartElements.TO_AVAILABLE_BUTTON_TEXT);

		Button allToChosen = new Button(mainGroup, SWT.NONE);
		FormData fd_allToChosen = new FormData();
		fd_allToChosen.bottom = new FormAttachment(0, 118);
		fd_allToChosen.left = new FormAttachment(groupAvailable, 31);
		fd_allToChosen.right = new FormAttachment(groupChosen, -24);
		fd_allToChosen.top = new FormAttachment(0, 96);
		allToChosen.setLayoutData(fd_allToChosen);
		allToChosen.setText(CompositeLauncherUiStandartElements.ALL_TO_CHOSEN_BUTTON_TEXT);

		Button allToAvailable = new Button(mainGroup, SWT.NONE);
		allToAvailable.setText(CompositeLauncherUiStandartElements.ALL_TO_AVAILABLE_BUTTON_TEXT);
		FormData fd_allToAvailable = new FormData();
		fd_allToAvailable.bottom = new FormAttachment(allToChosen, 28, SWT.BOTTOM);
		fd_allToAvailable.top = new FormAttachment(allToChosen, 6);
		fd_allToAvailable.left = new FormAttachment(groupAvailable, 31);
		fd_allToAvailable.right = new FormAttachment(groupChosen, -24);
		allToAvailable.setLayoutData(fd_allToAvailable);

		FormData fd_chosenList = new FormData();
		fd_chosenList.left = new FormAttachment(0);
		fd_chosenList.bottom = new FormAttachment(100, -7);
		fd_chosenList.top = new FormAttachment(0, 1);
		fd_chosenList.right = new FormAttachment(100, -201);

		Composite compositeChosen = new Composite(groupChosen, SWT.NONE);
		FormData fd_compositeChosen = new FormData();
		fd_compositeChosen.left = new FormAttachment(0);
		fd_compositeChosen.bottom = new FormAttachment(100, -1);
		fd_compositeChosen.top = new FormAttachment(0);
		compositeChosen.setLayoutData(fd_compositeChosen);
		compositeChosen.setLayout(new GridLayout(1, true));

		FilteredTree chosenConfigurationsFilteredTree = new FilteredTree(compositeChosen,
				SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI, new LaunchConfigurationPatternFilter(), true,
				true);

		chosenConigurationsViewer = chosenConfigurationsFilteredTree.getViewer();
		chosenConigurationsViewer.setContentProvider(new ChosenConfigurationContentProvider());
		chosenConigurationsViewer.setLabelProvider(new LaunchConfigurationLabelProvider());
		chosenLaunchConfViewerTree = chosenConigurationsViewer.getTree();

		GridData gd_chosenLaunchConfViewerTree = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_chosenLaunchConfViewerTree.heightHint = 330;
		chosenLaunchConfViewerTree.setLayoutData(gd_chosenLaunchConfViewerTree);
		chosenLaunchConfViewerTree.setHeaderVisible(false);
		chosenLaunchConfViewerTree.setFont(groupChosen.getFont());

		TreeColumn chosenNameColumn = new TreeColumn(chosenLaunchConfViewerTree, SWT.CENTER);
		chosenNameColumn.setWidth(384);
		chosenNameColumn.setResizable(false);
		Button btnUp = new Button(groupChosen, SWT.NONE);
		fd_compositeChosen.right = new FormAttachment(btnUp, -6);
		FormData fd_btnUp = new FormData();
		fd_btnUp.left = new FormAttachment(100, -119);
		fd_btnUp.right = new FormAttachment(100, -72);
		fd_btnUp.top = new FormAttachment(0, 31);
		btnUp.setLayoutData(fd_btnUp);

		btnUp.setText(CompositeLauncherUiStandartElements.UP_BUTTON_TEXT);

		FormData fd_btnParallel = new FormData();
		FormData fd_btnSequential = new FormData();
		FormData fd_launchConfViewerTree = new FormData();
		fd_launchConfViewerTree.bottom = new FormAttachment(100, -7);
		fd_launchConfViewerTree.top = new FormAttachment(0, 32);
		fd_launchConfViewerTree.right = new FormAttachment(100, -7);
		fd_launchConfViewerTree.left = new FormAttachment(0, 6);

		FilteredTree availableConfigurationsFilteredTree = new FilteredTree(groupAvailable,
				SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI, new LaunchConfigurationPatternFilter(), true,
				true);

		availableConigurationsViewer = availableConfigurationsFilteredTree.getViewer();
		availableConigurationsViewer.setContentProvider(new LaunchConfigurationContentProvider());
		availableConigurationsViewer.setLabelProvider(new LaunchConfigurationLabelProvider());
		availableConigurationsViewer.setComparator(new LaunchConnfigurationViewerComparator());
		availableLaunchConfViewerTree = availableConigurationsViewer.getTree();
		availableLaunchConfViewerTree.setHeaderVisible(false);
		availableLaunchConfViewerTree.setFont(groupAvailable.getFont());

		Button btnDown = new Button(groupChosen, SWT.NONE);
		FormData fd_btnDown1 = new FormData();
		fd_btnDown1.left = new FormAttachment(100, -119);
		fd_btnDown1.right = new FormAttachment(100, -57);
		fd_btnDown1.top = new FormAttachment(btnUp, 6);
		btnDown.setLayoutData(fd_btnDown1);
		btnDown.setText(CompositeLauncherUiStandartElements.DOWN_BUTTON_TEXT);
		fd_btnSequential.left = new FormAttachment(btnDown, 52);
		fd_btnSequential.top = new FormAttachment(btnDown, 5, SWT.TOP);

		btnSequential = new Button(groupChosen, SWT.RADIO);
		FormData fd_btnSequential1 = new FormData();
		fd_btnSequential1.left = new FormAttachment(100, -119);
		fd_btnSequential1.right = new FormAttachment(100, -41);
		btnSequential.setLayoutData(fd_btnSequential1);
		fd_btnParallel.bottom = new FormAttachment(btnSequential, -6);
		fd_btnParallel.left = new FormAttachment(btnSequential, 0, SWT.LEFT);
		btnSequential.setText(CompositeLauncherUiStandartElements.SEQUENTIAL_RADIO_BUTTON_TEXT);

		btnParallel = new Button(groupChosen, SWT.RADIO);
		fd_btnSequential1.top = new FormAttachment(btnParallel, 6);
		FormData fd_btnParallel1 = new FormData();
		fd_btnParallel1.left = new FormAttachment(100, -119);
		fd_btnParallel1.right = new FormAttachment(100, -58);
		fd_btnParallel1.top = new FormAttachment(0, 97);
		btnParallel.setLayoutData(fd_btnParallel1);
		btnParallel.setText(CompositeLauncherUiStandartElements.PARALLEL_RADIO_BUTTON_TEXT);

		FormData fd_chosenLaunchConfViewerTree = new FormData();
		fd_chosenLaunchConfViewerTree.bottom = new FormAttachment(100, -7);
		fd_chosenLaunchConfViewerTree.top = new FormAttachment(0, 30);
		fd_chosenLaunchConfViewerTree.left = new FormAttachment(0, 10);
		fd_chosenLaunchConfViewerTree.right = new FormAttachment(0, 346);

		TreeColumn nameColumn = new TreeColumn(availableLaunchConfViewerTree, SWT.CENTER);
		nameColumn.setWidth(384);
		nameColumn.setResizable(false);
		initButtons(toChosen, toAvailable, allToChosen, allToAvailable, btnUp, btnDown);
	}

	private void initExecutionType() {
		if (currentType.equals(CompositeLauncherConfigurationHelper.ATTRIBUTE_EXECUTION_TYPE_SEQUENTIAL)) {
			btnSequential.setSelection(true);
			btnParallel.setSelection(false);
		} else {
			btnParallel.setSelection(true);
			btnSequential.setSelection(false);
		}
	}

	private void initButtons(Button toChosen, Button toAvailable, Button allToChosen, Button allToAvailable, Button up,
			Button down) {
		toChosen.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				System.out.print(availableLaunchConfViewerTree.getSelectionCount());
				if (availableLaunchConfViewerTree.getSelectionCount() > 0) {
					TreeItem[] chosenConfigurationsItems = availableLaunchConfViewerTree.getSelection();
					System.out.println(chosenConfigurationsItems);
					for (TreeItem item : chosenConfigurationsItems) {
						CompositeLauncherConfigurationHelper.getConfigurationByName(item.getText(), mode)
								.ifPresent(config -> chosenConfigurations.add(config));
					}
					setChosenConfigurationsInput();
					updateLaunchConfigurationDialog();
				}
			}
		});
		toAvailable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				if (chosenLaunchConfViewerTree.getSelectionCount() > 0) {
					TreeItem[] chosenConfigurationsItems = chosenLaunchConfViewerTree.getSelection();
					for (TreeItem item : chosenConfigurationsItems) {
						CompositeLauncherConfigurationHelper.getConfigurationByName(item.getText(), mode)
								.ifPresent(config -> chosenConfigurations.remove(config));
					}
					setChosenConfigurationsInput();
					updateLaunchConfigurationDialog();
				}
			}
		});

		allToChosen.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				for (TreeItem item : availableLaunchConfViewerTree.getItems()) {
					for (TreeItem child : item.getItems()) {
						CompositeLauncherConfigurationHelper.getConfigurationByName(child.getText(), mode)
								.ifPresent(config -> chosenConfigurations.add(config));
					}
				}
				setChosenConfigurationsInput();
				updateLaunchConfigurationDialog();
			}
		});

		allToAvailable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				chosenConfigurations.clear();
				setChosenConfigurationsInput();
				updateLaunchConfigurationDialog();
			}
		});

		SelectionAdapter adapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button source = (Button) e.getSource();

				if (source.equals(btnSequential)) {
					currentType = CompositeLauncherConfigurationHelper.ATTRIBUTE_EXECUTION_TYPE_SEQUENTIAL;
				} else {
					currentType = CompositeLauncherConfigurationHelper.ATTRIBUTE_EXECUTION_TYPE_PARALLEL;
				}
				updateLaunchConfigurationDialog();
			}
		};

		down.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				Set<Integer> selectedIndexes = Arrays.stream(chosenLaunchConfViewerTree.getSelection())
						.map(item -> chosenLaunchConfViewerTree.indexOf(item))
						.collect(Collectors.toSet());
				for (int currentIndex = chosenConfigurations.size() - 1; currentIndex > 0; --currentIndex) {
					if (!selectedIndexes.contains(currentIndex)) {
						if (selectedIndexes.contains(currentIndex - 1)) {
							int notSelectedIndex = currentIndex - 1;
							while (selectedIndexes.contains(notSelectedIndex)) {
								--notSelectedIndex;
							}
							notSelectedIndex++;
							chosenConfigurations.add(notSelectedIndex, chosenConfigurations.remove(currentIndex));
							currentIndex = notSelectedIndex + 1;
						}
					}
				}
				setChosenConfigurationsInput();
				updateLaunchConfigurationDialog();
			}
		});
		
		up.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				Set<Integer> selectedIndexes = Arrays.stream(chosenLaunchConfViewerTree.getSelection())
						.map(item -> chosenLaunchConfViewerTree.indexOf(item))
						.collect(Collectors.toSet());
				for (int currentIndex = 0; currentIndex < chosenConfigurations.size() - 1; ++currentIndex) {
					if (!selectedIndexes.contains(currentIndex)) {
						if (selectedIndexes.contains(currentIndex + 1)) {
							int notSelectedIndex = currentIndex + 1;
							while (selectedIndexes.contains(notSelectedIndex)) {
								++notSelectedIndex;
							}
							notSelectedIndex--;
							chosenConfigurations.add(notSelectedIndex, chosenConfigurations.remove(currentIndex));
							currentIndex = notSelectedIndex - 1;
						}
					}
				}
				setChosenConfigurationsInput();
				updateLaunchConfigurationDialog();
			}
		});

		btnParallel.addSelectionListener(adapter);
		btnSequential.addSelectionListener(adapter);

	}
}