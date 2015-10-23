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
package eu.scasefp7.eclipse.core.ui.preferences;

import java.net.URL;
import java.text.MessageFormat;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;

import eu.scasefp7.eclipse.core.ui.Activator;
import eu.scasefp7.eclipse.core.ui.SharedImages;

/**
 * @author emaorli
 *
 */
public class GlobalPreferencePage extends PreferencePage implements IWorkbenchPreferencePage, IWorkbenchPropertyPage {

    private static final String PAGE_ID = "eu.scasefp7.eclipse.core.ui.preferences.global";

    private final SharedImages images;

    // Stores owning element of properties
    private IAdaptable element;
    
    /**
     * 
     */
    public GlobalPreferencePage() {
    //    super(GRID);
        Activator plugin = Activator.getDefault();
        setPreferenceStore(plugin.getPreferenceStore());
        this.images = Activator.getImages();
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    public void init(IWorkbench workbench) {
        setDescription("Expand the tree to edit preferences for a specific feature.");
    }


    protected String getPageId() {
        return PAGE_ID;
    }

    @Override
    protected Control createContents(Composite parent)
    {
      noDefaultAndApplyButton();
      Composite composite = new Composite(parent, 0);
      GridDataFactory.fillDefaults().applyTo(composite);
      GridLayoutFactory.swtDefaults().margins(0, 5).applyTo(composite);
      
      Group group = new Group(composite, 0);
      group.setText("Useful links");
      GridDataFactory.fillDefaults().grab(true, false).applyTo(group);
      GridLayoutFactory.swtDefaults().numColumns(2).applyTo(group);
      
      addOpenBrowserAction(createLink(group, SharedImages.Images.VIEW_SCASE, "Visit the <a href=\"{0}\">Project Homepage</a>", 
        "http://www.scasefp7.eu/"));
      
      addOpenBrowserAction(createLink(group, SharedImages.Images.OBJ_GITHUB, "View the <a href=\"{0}\">Project source code</a>", 
        "https://github.com/s-case"));

      addOpenBrowserAction(createLink(group, SharedImages.Images.OBJ_FACEBOOK, "<a href=\"{0}\">Like us</a> on Facebook", 
        "http://bit.ly/SCasefb"));
      
      addOpenBrowserAction(createLink(group, SharedImages.Images.OBJ_LINKEDIN, "<a href=\"{0}\">Join us</a> on LinkedIn", 
        "http://bit.ly/SCasegrp"));
      
      addOpenBrowserAction(createLink(group, SharedImages.Images.OBJ_TWITTER, "<a href=\"{0}\">Follow us</a> on Twitter", 
        "http://www.twitter.com/scasefp7"));
      
      return parent;
    }
    
    private Link createLink(Composite content, SharedImages.Images icon, String urlLabel, String url) {
      Label label = new Label(content, 1);
      label.setImage(images.getImage(icon));
      
      Link link = new Link(content, 1);
      link.setText(MessageFormat.format(urlLabel, new Object[] { url }));
      return link;
    }
    
    /**
     * Tries to open an URL wit the web browser configured in the Eclipse preferences (General &gt; Web Browser). By
     * default, this will open a new editor to display the URL within the Eclipse IDE.
     * 
     * See org.eclipse.recommenders.rcp.utils.BrowserUtils#openInDefaultBrowser(String)
     * 
     * @param url to open
     */
    protected static void openInDefaultBrowser(String url) {
        try {
            IWebBrowser defaultBrowser = PlatformUI.getWorkbench().getBrowserSupport().createBrowser(null);
            defaultBrowser.openURL(new URL(url));
        } catch (Exception e) {
            // Ignore failure; this method is best effort.
        }
    }

    /**
     * Tries to open an URL with an external web browser. If one is configure in the Eclipse preferences (General &gt;
     * Web Browser) it will prefer that over the operating system's default browser. If either way to open an external
     * browser does not succeed, this method will this will open a new editor to display the URL within the Eclipse IDE.
     * 
     * See org.eclipse.recommenders.rcp.utils.BrowserUtils#openInExternalBrowser(String)
     * 
     * @param url to open
     */
    protected static void openInExternalBrowser(String url) {
        try {
            IWebBrowser externalBrowser = PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser();
            externalBrowser.openURL(new URL(url));
        } catch (Exception e) {
            if (!Program.launch(url)) {
                openInDefaultBrowser(url);
            }
        }
    }

    /**
     * See org.eclipse.recommenders.rcp.utils.BrowserUtils#openInExternalBrowser(URL).
     * 
     * @param url to open
     */
    protected static void openInExternalBrowser(URL url) {
      openInExternalBrowser(url.toExternalForm());
    }
    
    /**
     * Augments the supplied link to open it in a web browser when clicked on.
     * See org.eclipse.recommenders.rcp.utils.BrowserUtils#addOpenBrowserAction(Link).
     * 
     * @param link to add the handler to
     */
    protected static void addOpenBrowserAction(Link link) {
        link.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                openInExternalBrowser(event.text);
            }
        });
    }

    @Override
    public IAdaptable getElement() {
        return element;
    }

    @Override
    public void setElement(IAdaptable element) {
        this.element = element;
    }
}
