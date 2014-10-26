package cser.ast.view;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;

import cser.compare.view.CompareUtils;
import cser.core.CSeRClone;
import cser.core.CSeRDBController;
import cser.core.CSeRException;






/**
 * Insert the type's description here.
 * @see ViewPart
 */
public class CSeRCloneView extends ViewPart {
	public static TreeViewer treeViewer;
	
	protected CSeRLabelProvider labelProvider;
	
	protected Action openAction;
	protected Action trackAction;
	protected Action deleteAction;
	private ISelectionProvider selectionProvider = new MarkerSelectionProviderAdapter();
	protected Action copyPasteAction;
	protected Action showGroupAction;
	public static IFile file =null;

	
	/**
	 * The constructor.
	 */
	public CSeRCloneView() {
	}

	/*
	 * @see IWorkbenchPart#createPartControl(Composite)
	 */
	public void createPartControl(Composite parent) {
		/* Create a grid layout object so the text and treeviewer
		 * are layed out the way I want. */
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		layout.verticalSpacing = 2;
		layout.marginWidth = 0;
		layout.marginHeight = 2;
		parent.setLayout(layout);
		
		/* Create a "label" to display information in. I'm
		 * using a text field instead of a lable so you can
		 * copy-paste out of it. */
		// layout the text field above the treeviewer
		GridData layoutData = new GridData();
		layoutData.grabExcessHorizontalSpace = true;
		layoutData.horizontalAlignment = GridData.FILL;
		
		// Create the tree viewer as a child of the composite parent
		treeViewer = new TreeViewer(parent);
		treeViewer.setContentProvider(new CSeRContentProvider());
		labelProvider = new CSeRLabelProvider();
		treeViewer.setLabelProvider(labelProvider);
		
		treeViewer.setUseHashlookup(true);
		
		// layout the tree viewer below the text field
		layoutData = new GridData();
		layoutData.grabExcessHorizontalSpace = true;
		layoutData.grabExcessVerticalSpace = true;
		layoutData.horizontalAlignment = GridData.FILL;
		layoutData.verticalAlignment = GridData.FILL;
		treeViewer.getControl().setLayoutData(layoutData);
		

	 
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection)treeViewer.getSelection();

				
				if(!selection.isEmpty() && selection.getFirstElement() instanceof CSeRClone ){
					openAction.setEnabled(true);
				}
				else {
					openAction.setEnabled(false);

				}
			

		
			}
		
		});

		treeViewer.addDoubleClickListener(new IDoubleClickListener() {


			private void viewerSelectionChanged(IStructuredSelection selection) {

                openAction.run();
			}

			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event
						.getSelection();
				viewerSelectionChanged(selection);

			}
		});

		// create the actions before the input is set on the viewer but after
		// the
		// sorter and filter are set so the actions will be enabled correctly.
		// createActions();

		treeViewer.setInput(CompareUtils.getClones());
		createActions();

		treeViewer.expandAll();
		MenuManager mgr = initContextMenu();
		Menu menu = mgr.createContextMenu(treeViewer.getControl());
		treeViewer.getControl().setMenu(menu);
		getSite().registerContextMenu(mgr, selectionProvider);

		getSite().setSelectionProvider(selectionProvider);

		IActionBars actionBars = getViewSite().getActionBars();
		 initMenu(actionBars.getMenuManager());
		initToolBar(actionBars.getToolBarManager());
	}
	private void initMenu(IMenuManager menuManager) {
		menuManager.add(trackAction);
		menuManager.add(deleteAction);
		menuManager.add(openAction);
		menuManager.add(copyPasteAction);
		menuManager.add(showGroupAction);
	}

	private void initToolBar(IToolBarManager toolBarManager) {
		// TODO Auto-generated method stub
		toolBarManager.add(openAction);
		
		toolBarManager.add(trackAction);
		toolBarManager.add(deleteAction);
		toolBarManager.add(copyPasteAction);
		toolBarManager.add(showGroupAction);
	}

	protected MenuManager initContextMenu() {
		MenuManager mgr = new MenuManager();
		mgr.setRemoveAllWhenShown(true);
		mgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager mgr) {

				treeViewer.cancelEditing();
		
				initMenu(mgr);
			}

		});
		return mgr;
	}



	private void createActions() {
		openAction = new OpenAction(treeViewer);
		
		trackAction =  new TrackAction();
		
		copyPasteAction = new  CopyPasteAction();
		deleteAction = new DeleteAcion();
		showGroupAction = new ShowGroupsAction();
	}


	public static void refresh(String[] strings){
		
		
	
     treeViewer.refresh();
   
    
	
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}
	
	}
