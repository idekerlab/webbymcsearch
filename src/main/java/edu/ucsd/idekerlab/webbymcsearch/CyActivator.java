package edu.ucsd.idekerlab.webbymcsearch;

import edu.ucsd.idekerlab.webbymcsearch.query.WebSearchDialog;
import static org.cytoscape.application.swing.ActionEnableSupport.ENABLE_FOR_SELECTED_NODES;
import static org.cytoscape.work.ServiceProperties.ENABLE_FOR;
import static org.cytoscape.work.ServiceProperties.IN_CONTEXT_MENU;
import static org.cytoscape.work.ServiceProperties.IN_MENU_BAR;
import static org.cytoscape.work.ServiceProperties.MENU_GRAVITY;
import static org.cytoscape.work.ServiceProperties.PREFERRED_MENU;
import static org.cytoscape.work.ServiceProperties.TITLE;

import java.util.Properties;

import edu.ucsd.idekerlab.webbymcsearch.util.JEditorPaneFactoryImpl;
import edu.ucsd.idekerlab.webbymcsearch.query.WebSearchTaskFactoryImpl;
import edu.ucsd.idekerlab.webbymcsearch.util.Constants;
import edu.ucsd.idekerlab.webbymcsearch.util.IconJLabelDialogFactory;
import edu.ucsd.idekerlab.webbymcsearch.util.ImageIconHolderFactory;
import edu.ucsd.idekerlab.webbymcsearch.util.ShowDialogUtil;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.service.util.AbstractCyActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CyActivator extends AbstractCyActivator {

	private final static Logger LOGGER = LoggerFactory.getLogger(CyActivator.class);
	public CyActivator() {
		super();
	}

	
	@Override
	public void start(BundleContext bc) throws Exception {

		final CySwingApplication swingApplication = getService(bc, CySwingApplication.class);
		
		// sets up the PropertiesHelper and links it to properties that a user can
		// view and edit in Edit => Preferences menu
	
		
		ShowDialogUtil dialogUtil = new ShowDialogUtil();
		ImageIconHolderFactory iconHolderFactory = new ImageIconHolderFactory();
		JEditorPaneFactoryImpl editorPaneFac = new JEditorPaneFactoryImpl();
		IconJLabelDialogFactory iconJLabelFactory = new IconJLabelDialogFactory(dialogUtil,
				iconHolderFactory, editorPaneFac);

		// add About under Apps => Webby McSearch menu
		Properties aboutProps = new Properties();
		aboutProps.setProperty(MENU_GRAVITY, "2.0");
		aboutProps.setProperty(PREFERRED_MENU, Constants.TOP_MENU);
		aboutProps.setProperty(TITLE, "About");
		registerAllServices(bc, new AboutTaskFactoryImpl(swingApplication, editorPaneFac, dialogUtil), aboutProps);
			
		Properties iQueryCMenuProps = new Properties();
		iQueryCMenuProps.setProperty(PREFERRED_MENU, Constants.CONTEXT_MENU);
		iQueryCMenuProps.setProperty(ENABLE_FOR, ENABLE_FOR_SELECTED_NODES);
		iQueryCMenuProps.setProperty(TITLE, "Web Search");
		iQueryCMenuProps.put(IN_MENU_BAR, false);
		iQueryCMenuProps.put(IN_CONTEXT_MENU, true);
		WebSearchDialog webSearchDialog = new WebSearchDialog(iconJLabelFactory);
		WebSearchTaskFactoryImpl iQueryFac = new WebSearchTaskFactoryImpl(swingApplication, dialogUtil, webSearchDialog);
		registerAllServices(bc, iQueryFac, iQueryCMenuProps);
		
	}

}
