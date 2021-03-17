package edu.ucsd.idekerlab.webbymcsearch.query;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import edu.ucsd.idekerlab.webbymcsearch.DoNothingTask;
import edu.ucsd.idekerlab.webbymcsearch.util.ColumnUtil;
import edu.ucsd.idekerlab.webbymcsearch.util.Constants;
import edu.ucsd.idekerlab.webbymcsearch.util.DesktopUtil;
import edu.ucsd.idekerlab.webbymcsearch.util.ShowDialogUtil;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import javax.swing.JOptionPane;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.model.CyColumn;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTable;
import org.cytoscape.model.CyTableUtil;
import org.cytoscape.task.AbstractNodeViewTaskFactory;
import org.cytoscape.task.NetworkViewTaskFactory;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.View;
import org.cytoscape.work.TaskIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of {@link NetworkViewTaskFactory} and
 * {@link AbstractNodeViewTaskFactory} to send members of specified
 * node to iQuery
 *
 */
public class WebSearchTaskFactoryImpl extends AbstractNodeViewTaskFactory implements NetworkViewTaskFactory {

	public static final int MAX_RAW_DATA_LEN = 20;
	public static final int MAX_RAW_COL_LEN = 20;
	private final static Logger LOGGER = LoggerFactory.getLogger(WebSearchTaskFactoryImpl.class);
	private final CySwingApplication _swingApplication;
	private final ShowDialogUtil _dialogUtil;
	private DesktopUtil _deskTopUtil;
	private WebSearchDialog _webSearchDialog;
	private ColumnUtil _columnUtil;
 

	public WebSearchTaskFactoryImpl(CySwingApplication swingApplication,
			ShowDialogUtil dialogUtil, WebSearchDialog webSearchDialog) {
		_swingApplication = swingApplication;
		_dialogUtil = dialogUtil;
		_deskTopUtil = new DesktopUtil();
		_webSearchDialog = webSearchDialog;
		_columnUtil = new ColumnUtil();
	}
	
	protected void setAlternateDesktopUtil(DesktopUtil deskTopUtil){
		this._deskTopUtil = deskTopUtil;
	}
	
	/**
	 * Creates URL with genes appended to end in format: 
	 * http://search.ndexbio.org/?genes=GENE1%20GENE2%20GENE3
	 * @param networkView
	 * @return 
	 */
	private URI getQueryURI(final String data, WebQuery query){
		try {
			boolean replaceWhiteSpaceWithOr = false;			
			String queryString = _columnUtil.getQueryString(data, query.getReplaceWhiteSpaceWith());
			if (queryString == null){
				_dialogUtil.showMessageDialog(_swingApplication.getJFrame(),
						"No terms to query");
				return null;
			}
			return new URI(query.getUrlAsString() + queryString);
		} catch(UnsupportedEncodingException | URISyntaxException e){
			LOGGER.error("Unable to build query URL", e);
			_dialogUtil.showMessageDialog(_swingApplication.getJFrame(),
					"Unable to generate URL for query: " + e.getMessage());
			return null;
		}
	}
	
	private String getTruncatedColumnData(final String rawData){
		if (rawData.length() <= MAX_RAW_DATA_LEN){
			return rawData;
		}
		return rawData.substring(0, MAX_RAW_DATA_LEN - 3) + "...";
		
	}
	
	private HashMap<String, CyColumn> getColumns(CyNetworkView networkView){
		CyNetwork network = networkView.getModel();
		CyTable nodeTable = network.getDefaultNodeTable();
		HashMap<String, CyColumn> columnMap = new LinkedHashMap<>();
		CyNode selectedNode = getSelectedNode(network);
		for (CyColumn column : nodeTable.getColumns()) {
			
			if (column.getName().equals(CyNetwork.SUID)){
				continue;
			}
			
			String rawValue = _columnUtil.getColumnData(network, column, selectedNode);
			if (rawValue == null){
				continue;
			}
			columnMap.put("[" + _columnUtil.getTruncatedColumnName(column,
					MAX_RAW_COL_LEN) + "]  "
					+ getTruncatedColumnData(rawValue), column);
		}

		return columnMap;
	}
	
	private CyNode getSelectedNode(CyNetwork network){
		List<CyNode> selectedNodes = CyTableUtil.getSelectedNodes(network);
		return selectedNodes.get(0);
	}

	@Override
	public TaskIterator createTaskIterator(CyNetworkView networkView) {
		HashMap<String, CyColumn> colData = this.getColumns(networkView);
		
		_webSearchDialog.createGUI(colData);
		Object[] options = {Constants.QUERY, Constants.CANCEL};
		int res = _dialogUtil.showOptionDialog(_swingApplication.getJFrame(),
											   this._webSearchDialog,
														"Parent Network Chooser",
														JOptionPane.YES_NO_OPTION,
														JOptionPane.PLAIN_MESSAGE, 
														null, 
														options,
														options[0]);

		if (res != 0){
			return new TaskIterator(new DoNothingTask());
		}
                boolean openWebBrowserRes = true;
		CyNetwork network = networkView.getModel();
                StringBuilder sb = new StringBuilder();
                for (CyNode selectedNode : CyTableUtil.getSelectedNodes(network)){
                    String data = _columnUtil.getColumnData(network, _webSearchDialog.getSelectedColumn(), selectedNode);
                    if (data != null && data.length() > 0){
                        if (sb.length() > 0){
                            sb.append(" ");
                        }
                        sb.append(data);
                    }
                }
		String combinedData = sb.toString();
                for (WebQuery query : _webSearchDialog.getSelectedQueries()){
                        URI queryURI = getQueryURI(combinedData, query);
                        if (queryURI == null){
                                continue;
                        }
                        openWebBrowserRes = runQueryOnWebBrowser(queryURI);
                        if (openWebBrowserRes == false){
                            break;
                        }
                }
		return new TaskIterator(new DoNothingTask());
	}

	private boolean runQueryOnWebBrowser(URI theQueryURI){
		URL theQueryURL = null;
		try {
			theQueryURL = theQueryURI.toURL();
			LOGGER.debug("Opening " + theQueryURL + " in default browser");
			_deskTopUtil.getDesktop().browse(theQueryURI);
		} catch (Exception e) {
			LOGGER.info("Unable to open default browser window to pass terms to iQuery", e);
			_dialogUtil.showMessageDialog(_swingApplication.getJFrame(),
					"Default browser window could not be opened. Please copy/paste this link to your browser: "
						+ (theQueryURL == null ? "NA" : theQueryURL));
			return false;
		}
		return true;
	}
	
	/**
	 * Lets caller know if this task can be invoked via 
	 * {@link #createTaskIterator(org.cytoscape.view.model.CyNetworkView) }
	 * @param networkView
	 * @return true if one and only one node is selected
	 */
	@Override
	public boolean isReady(CyNetworkView networkView) {
		if (networkView != null && networkView.getModel() != null) {
                        int numSelectedNodes = CyTableUtil.getSelectedNodes(networkView.getModel()).size();
			if (numSelectedNodes >= 1) {
				return true;
			}
		}
		return false;
	}

	@Override
	public TaskIterator createTaskIterator(View<CyNode> nodeView, CyNetworkView networkView) {
		return this.createTaskIterator(networkView);
	}

	/**
	 * Just calls {@link #isReady(org.cytoscape.view.model.CyNetworkView) } ignoring
	 * {@code nodeView}
	 * @param nodeView This is ignored
	 * @param networkView
	 * @return See {@link #isReady(org.cytoscape.view.model.CyNetworkView) } for 
	 *         return information
	 */
	@Override
	public boolean isReady(View<CyNode> nodeView, CyNetworkView networkView) {
		return this.isReady(networkView);
	}
}
