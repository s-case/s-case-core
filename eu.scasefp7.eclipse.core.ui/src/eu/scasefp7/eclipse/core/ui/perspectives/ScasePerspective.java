package eu.scasefp7.eclipse.core.ui.perspectives;

import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.progress.IProgressConstants;




//import org.eclipse.ui.console.IConsoleConstants;
import eu.scasefp7.eclipse.core.ui.ScaseUiConstants;

// import org.eclipse.ui.texteditor.templates.TemplatesView;

//import org.eclipse.debug.ui.IDebugUIConstants;

// import org.eclipse.search.ui.NewSearchUI;

//import org.eclipse.jdt.ui.JavaUI;
//import org.eclipse.ui.console.IConsoleConstants;
//import org.eclipse.jdt.ui.JavaUI;

/**
 * Based on org.eclipse.jdt.internal.ui.JavaPerspectiveFactory, 
 * https://git.eclipse.org/c/jdt/eclipse.jdt.ui.git/tree/org.eclipse.jdt.ui/ui/org/eclipse/jdt/internal/ui/JavaPerspectiveFactory.java
 * copyrighted by:
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
public class ScasePerspective implements IPerspectiveFactory {

	/**
	 * Construct the perspective object.
	 */
	public ScasePerspective() {
		super();
	}

	public void createInitialLayout(IPageLayout layout) {
	    
		String editorArea = layout.getEditorArea();

		IFolderLayout folder = layout.createFolder(
				"left", IPageLayout.LEFT, (float) 0.25, editorArea); //$NON-NLS-1$
		folder.addView("org.eclipse.jdt.ui.PackageExplorer"); //$NON-NLS-1$
		folder.addView(IPageLayout.ID_PROJECT_EXPLORER);

		IFolderLayout outputfolder = layout.createFolder(
				"bottom", IPageLayout.BOTTOM, (float) 0.75, editorArea); //$NON-NLS-1$
		
		outputfolder.addView(IPageLayout.ID_PROBLEM_VIEW);
		outputfolder.addPlaceholder("org.eclipse.search.ui.views.SearchView"); //$NON-NLS-1$
		outputfolder.addPlaceholder("org.eclipse.ui.console.ConsoleView"); //$NON-NLS-1$
		outputfolder.addPlaceholder(IPageLayout.ID_PROP_SHEET); //$NON-NLS-1$
		
		outputfolder.addView(ScaseUiConstants.DASHBOARD_VIEW); //$NON-NLS-1$
		outputfolder.addView("eu.scasefp7.eclipse.servicecomposition.views.ServiceCompositionView");

		IFolderLayout outlineFolder = layout.createFolder(
				"right", IPageLayout.RIGHT, (float) 0.75, editorArea); //$NON-NLS-1$
		outlineFolder.addView(IPageLayout.ID_OUTLINE);

//		outlineFolder.addPlaceholder(TemplatesView.ID);

		layout.addActionSet("org.eclipse.debug.ui.launchActionSet"); //$NON-NLS-1$
//		layout.addActionSet(JavaUI.ID_ACTION_SET);
//		layout.addActionSet(JavaUI.ID_ELEMENT_CREATION_ACTION_SET);
		layout.addActionSet("org.eclipse.search.searchActionSet"); //$NON-NLS-1$
		layout.addActionSet(IPageLayout.ID_NAVIGATE_ACTION_SET);

		// views - java
/*		layout.addShowViewShortcut(JavaUI.ID_PACKAGES);
		layout.addShowViewShortcut(JavaUI.ID_TYPE_HIERARCHY);
		layout.addShowViewShortcut(JavaUI.ID_SOURCE_VIEW);
		layout.addShowViewShortcut(JavaUI.ID_JAVADOC_VIEW);
*/
		// views - search
        layout.addShowViewShortcut("org.eclipse.search.ui.views.SearchView"); //$NON-NLS-1$
      
		// views - debugging
		layout.addShowViewShortcut("org.eclipse.ui.console.ConsoleView"); //$NON-NLS-1$

		// views - standard workbench
		layout.addShowViewShortcut(IPageLayout.ID_OUTLINE); //$NON-NLS-1$
		layout.addShowViewShortcut(IPageLayout.ID_PROBLEM_VIEW); //$NON-NLS-1$
//		layout.addShowViewShortcut(JavaPlugin.ID_RES_NAV);
		layout.addShowViewShortcut(IPageLayout.ID_TASK_LIST); //$NON-NLS-1$
		layout.addShowViewShortcut(IProgressConstants.PROGRESS_VIEW_ID); //$NON-NLS-1$
		layout.addShowViewShortcut(IPageLayout.ID_PROJECT_EXPLORER); //$NON-NLS-N$
//		layout.addShowViewShortcut(TemplatesView.ID);
		layout.addShowViewShortcut("org.eclipse.pde.runtime.LogView"); //$NON-NLS-1$

		// new actions - project creation wizard
        layout.addNewWizardShortcut(ScaseUiConstants.NEW_PROJECT_WIZARD);//$NON-NLS-1$
        layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.JavaProjectWizard"); //$NON-NLS-1$
        //----
        layout.addNewWizardShortcut(ScaseUiConstants.REQUIREMENTS_EDITOR_NEWWIZARDID);//$NON-NLS-1$
        layout.addNewWizardShortcut(ScaseUiConstants.STORYBOARD_EDITOR_NEWWIZARDID);//$NON-NLS-1$
		layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.folder");//$NON-NLS-1$
		layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.file");//$NON-NLS-1$
		layout.addNewWizardShortcut("org.eclipse.ui.editors.wizards.UntitledTextFileWizard");//$NON-NLS-1$
        
		// 'Window' > 'Open Perspective' contributions
        layout.addPerspectiveShortcut("eu.scasefp7.eclipse.core.ui.ScasePerspective");//$NON-NLS-1$
        layout.addPerspectiveShortcut("org.eclipse.jdt.ui.JavaPerspective");//$NON-NLS-1$

	}
}
