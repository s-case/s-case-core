/**
 * Copyright 2015 S-CASE Consortium
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.scasefp7.eclipse.core.ui;

/**
 * Convenience collection of constants to reference various IDs.
 * 
 * @author emaorli
 *
 */
public final class ScaseUiConstants {
	
	/** Used to store constants, cannot be instantiated. */
	private ScaseUiConstants() {};
	
	/** Category for views. */
	public static final String VIEW_CATEGORY = "eu.scasefp7.eclipse.category"; //$NON-NLS-1$
	
	/** S-CASE Perspective ID. */
	public static final String PERSPECTIVE = "eu.scasefp7.eclipse.core.ui.ScasePerspective"; //$NON-NLS-1$
	
	/** New S-CASE project wizard ID. */
	public static final String NEW_PROJECT_WIZARD = "eu.scasefp7.eclipse.core.ui.newProjectWizard"; //$NON-NLS-1$
	
	/** S-CASE Dashboard view ID. */
	public static final String DASHBOARD_VIEW = "eu.scasefp7.eclipse.core.ui.views.Dashboard"; //$NON-NLS-1$

	/** Extension point used to contribute dashboard items. */
	public static final String DASHBOARD_EXTENSION = "eu.scasefp7.eclipse.core.ui.dashboardItem"; //$NON-NLS-1$
	
	/** Link ontologies command ID. */
	public static final String COMMAND_EXPORTONTOLOGY = "eu.scasefp7.eclipse.core.commands.linkOntologies"; //$NON-NLS-1$
	
	
	/* These are external, included here for convenience */
	
	/** UML recognizer wizard ID. */
	public static final String UML_RECOGNIZER_NEWWIZARDID = "eu.scasefp7.eclipse.umlrec.importWizard"; //$NON-NLS-1$

	/** Requirements editor wizard ID.*/
	public static final String REQUIREMENTS_EDITOR_NEWWIZARDID = "eu.scasefp7.eclipse.reqeditor.wizards.CreateRqsWizard"; //$NON-NLS-1$
	
	/** Requirements editor ontology export command. */
	public static final String REQUIREMENTS_EDITOR_COMMAND_EXPORTONTOLOGY = "eu.scasefp7.eclipse.reqeditor.commands.exportToOntology"; //$NON-NLS-1$

	/** Storyboards editor wizard ID. */
	public static final String STORYBOARD_EDITOR_NEWWIZARDID = "eu.scasefp7.eclipse.storyboards.diagram.part.StoryboardsCreationWizardID"; //$NON-NLS-1$
	
	/** Storyboard editor ontology export command. */
	public static final String STORYBOARD_EDITOR_COMMAND_EXPORTONTOLOGY = "eu.scasefp7.eclipse.storyboards.commands.exportAllToOntology"; //$NON-NLS-1$

	/** Property for project domain. */ //TODO-move to prefs
    public static final String PROP_PROJECT_DOMAIN = "eu.scasefp7.eclipse.core.projectDomain"; //$NON-NLS-1$
    
    /** Default project domain (unset). */
    public static final int PROP_PROJECT_DOMAIN_DEFAULT = -1;
    
    /* Links */
    /**
     * URL for the project homepage (http://www.scasefp7.eu/).
     */
    public static final String PROJECT_HOMEPAGE = "http://www.scasefp7.eu/";

    /**
     * URL for the project github (https://github.com/s-case).
     */
    public static final String PROJECT_GITHUB = "https://github.com/s-case";

    /**
     * URL for the project Facebook page.
     */
    public static final String PROJECT_FACEBOOK = "http://bit.ly/SCasefb";

    /**
     * URL for the project LinkedIn page.
     */
    public static final String PROJECT_LINKEDIN = "http://bit.ly/SCasegrp";

    /**
     * URL for the project Twitter.
     */
    public static final String PROJECT_TWITTER = "http://www.twitter.com/scasefp7";
}
