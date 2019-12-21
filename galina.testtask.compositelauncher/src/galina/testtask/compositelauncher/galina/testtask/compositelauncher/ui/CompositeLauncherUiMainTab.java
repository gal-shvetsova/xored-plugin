package galina.testtask.compositelauncher.ui;

import java.util.List;
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
import org.eclipse.ui.dialogs.PatternFilter;

import galina.testtask.compositelauncher.CompositeLauncherConfigurationHelper;
import galina.testtask.compositelauncher.LaunchConfigurationItem;

import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.custom.StyledText;

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
		scrolledComposite.setMinWidth(710);
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
		boolean superValid = super.isValid(launchConfiguration);
		return superValid;
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
		groupChosen.setLayout(new GridLayout(5, false));
		FormData fd_groupChosen = new FormData();
		fd_groupChosen.top = new FormAttachment(groupAvailable, 0, SWT.TOP);
		fd_groupChosen.bottom = new FormAttachment(100, -36);
		fd_groupChosen.right = new FormAttachment(100, -13);
		groupChosen.setLayoutData(fd_groupChosen);
		groupChosen.setText(CompositeLauncherUiStandartElements.CHOSEN_GROUP_TEXT);

		Button toChosen = new Button(mainGroup, SWT.NONE);
		fd_groupChosen.left = new FormAttachment(50);
		FormData fd_toChosen = new FormData();
		fd_toChosen.top = new FormAttachment(0, 36);
		fd_toChosen.left = new FormAttachment(groupAvailable, 6);
		fd_toChosen.right = new FormAttachment(groupChosen, -6);
		toChosen.setLayoutData(fd_toChosen);
		toChosen.setText(CompositeLauncherUiStandartElements.TO_CHOSEN_BUTTON_TEXT);

		Button toAvailable = new Button(mainGroup, SWT.NONE);
		fd_toChosen.bottom = new FormAttachment(toAvailable, -6);
		fd_groupAvailable.right = new FormAttachment(32);
		FormData fd_toAvailable = new FormData();
		fd_toAvailable.bottom = new FormAttachment(0, 86);
		fd_toAvailable.right = new FormAttachment(groupChosen, -6);
		fd_toAvailable.left = new FormAttachment(groupAvailable, 6);
		fd_toAvailable.top = new FormAttachment(0, 64);
		toAvailable.setLayoutData(fd_toAvailable);
		toAvailable.setText(CompositeLauncherUiStandartElements.TO_AVAILABLE_BUTTON_TEXT);

		Button allToChosen = new Button(mainGroup, SWT.NONE);
		FormData fd_allToChosen = new FormData();
		fd_allToChosen.bottom = new FormAttachment(toAvailable, 32, SWT.BOTTOM);
		fd_allToChosen.right = new FormAttachment(groupChosen, -6);
		fd_allToChosen.top = new FormAttachment(toAvailable, 10);
		fd_allToChosen.left = new FormAttachment(groupAvailable, 6);
		allToChosen.setLayoutData(fd_allToChosen);
		allToChosen.setText(CompositeLauncherUiStandartElements.ALL_TO_CHOSEN_BUTTON_TEXT);

		Button allToAvailable = new Button(mainGroup, SWT.NONE);
		allToAvailable.setText(CompositeLauncherUiStandartElements.ALL_TO_AVAILABLE_BUTTON_TEXT);
		FormData fd_allToAvailable = new FormData();
		fd_allToAvailable.bottom = new FormAttachment(allToChosen, 28, SWT.BOTTOM);
		fd_allToAvailable.top = new FormAttachment(allToChosen, 6);
		fd_allToAvailable.right = new FormAttachment(groupChosen, -6);
		fd_allToAvailable.left = new FormAttachment(groupAvailable, 6);
		allToAvailable.setLayoutData(fd_allToAvailable);
		FormData fd_allToAvailable1 = new FormData();
		fd_allToAvailable1.right = new FormAttachment(groupChosen, -6);

		FormData fd_chosenList = new FormData();
		fd_chosenList.left = new FormAttachment(0);
		fd_chosenList.bottom = new FormAttachment(100, -7);
		fd_chosenList.top = new FormAttachment(0, 1);

		Button btnUp = new Button(groupChosen, SWT.NONE);
		fd_chosenList.right = new FormAttachment(100, -201);
		FormData fd_btnUp = new FormData();
		fd_btnUp.top = new FormAttachment(0, 30);
	//	btnUp.setLayoutData(fd_btnUp);
		btnUp.setText(CompositeLauncherUiStandartElements.UP_BUTTON_TEXT);
		new Label(groupChosen, SWT.NONE);
		new Label(groupChosen, SWT.NONE);

		Button btnDown = new Button(groupChosen, SWT.NONE);
		FormData fd_btnDown = new FormData();
		fd_btnDown.top = new FormAttachment(btnUp, 6);
		//btnDown.setLayoutData(fd_btnDown);
		btnDown.setText(CompositeLauncherUiStandartElements.DOWN_BUTTON_TEXT);

		btnParallel = new Button(groupChosen, SWT.RADIO);
		FormData fd_btnParallel = new FormData();
		//btnParallel.setLayoutData(fd_btnParallel);
		btnParallel.setText(CompositeLauncherUiStandartElements.PARALLEL_RADIO_BUTTON_TEXT);
		FormData fd_btnSequential = new FormData();
		fd_btnSequential.left = new FormAttachment(btnDown, 52);
		fd_btnSequential.top = new FormAttachment(btnDown, 5, SWT.TOP);

		fd_allToAvailable1.bottom = new FormAttachment(allToChosen, 28, SWT.BOTTOM);
		fd_allToAvailable1.top = new FormAttachment(allToChosen, 6);
		fd_allToAvailable1.left = new FormAttachment(groupAvailable, 6);
		FormData fd_launchConfViewerTree = new FormData();
		fd_launchConfViewerTree.bottom = new FormAttachment(100, -7);
		fd_launchConfViewerTree.top = new FormAttachment(0, 32);
		fd_launchConfViewerTree.right = new FormAttachment(100, -7);
		fd_launchConfViewerTree.left = new FormAttachment(0, 6);
		// tree.setLayoutData(fd_launchConfViewerTree);

		FilteredTree availableConfigurationsFilteredTree = new FilteredTree(groupAvailable,
				SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI,
				new LaunchConfigurationPatternFilter(), 
				true,
				true);

		availableConigurationsViewer = availableConfigurationsFilteredTree.getViewer();
		availableConigurationsViewer.setContentProvider(new LaunchConfigurationContentProvider());
		availableConigurationsViewer.setLabelProvider(new LaunchConfigurationLabelProvider());
		availableConigurationsViewer.setComparator(new LaunchConnfigurationViewerComparator());
		availableLaunchConfViewerTree = availableConigurationsViewer.getTree();
		availableLaunchConfViewerTree.setHeaderVisible(false);
		availableLaunchConfViewerTree.setFont(groupAvailable.getFont());
				new Label(groupChosen, SWT.NONE);
				
				chosenConigurationsViewer = new TreeViewer(groupChosen, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI);//chosenConfigurationsFilteredTree.getViewer();
				chosenConigurationsViewer.setContentProvider(new ChosenConfigurationContentProvider());
				chosenConigurationsViewer.setLabelProvider(new LaunchConfigurationLabelProvider());
				chosenLaunchConfViewerTree = chosenConigurationsViewer.getTree();
				GridData gd_chosenLaunchConfViewerTree = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
				gd_chosenLaunchConfViewerTree.heightHint = 304;
				chosenLaunchConfViewerTree.setLayoutData(gd_chosenLaunchConfViewerTree);
				fd_btnUp.left = new FormAttachment(chosenLaunchConfViewerTree, 10);
				fd_btnDown.left = new FormAttachment(chosenLaunchConfViewerTree, 6);
				
					//	chosenLaunchConfViewerTree.setLayoutData(fd_chosenLaunchConfViewerTree);
						chosenLaunchConfViewerTree.setHeaderVisible(false);
						chosenLaunchConfViewerTree.setFont(groupChosen.getFont());
						
								TreeColumn chosenNameColumn = new TreeColumn(chosenLaunchConfViewerTree, SWT.CENTER);
								chosenNameColumn.setText(" ");
								chosenNameColumn.setWidth(384);
								chosenNameColumn.setResizable(false);
								chosenNameColumn.setText(" ");
		
				btnSequential = new Button(groupChosen, SWT.RADIO);
				fd_btnParallel.bottom = new FormAttachment(btnSequential, -6);
				fd_btnParallel.left = new FormAttachment(btnSequential, 0, SWT.LEFT);
				//	btnSequential.setLayoutData(fd_btnSequential);
					btnSequential.setText(CompositeLauncherUiStandartElements.SEQUENTIAL_RADIO_BUTTON_TEXT);

		FilteredTree chosenConfigurationsFilteredTree = new FilteredTree(groupChosen,
				SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI,
				new LaunchConfigurationPatternFilter(), 
				true,
				true);
		FormData fd_chosenLaunchConfViewerTree = new FormData();
		fd_chosenLaunchConfViewerTree.bottom = new FormAttachment(100, -7);
		fd_chosenLaunchConfViewerTree.top = new FormAttachment(0, 30);
		fd_chosenLaunchConfViewerTree.left = new FormAttachment(0, 10);
		fd_chosenLaunchConfViewerTree.right = new FormAttachment(0, 346);

		
		TreeColumn nameColumn = new TreeColumn(availableLaunchConfViewerTree, SWT.CENTER);
		nameColumn.setWidth(384);
		nameColumn.setResizable(false);
		initButtons(toChosen, toAvailable, allToChosen, allToAvailable, btnUp, btnDown);
		new Label(groupChosen, SWT.NONE);
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

		btnParallel.addSelectionListener(adapter);
		btnSequential.addSelectionListener(adapter);

	}
}
