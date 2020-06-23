package edu.ucsd.idekerlab.webbymcsearch.query;

import edu.ucsd.idekerlab.webbymcsearch.DoNothingTask;
import edu.ucsd.idekerlab.webbymcsearch.util.Constants;
import edu.ucsd.idekerlab.webbymcsearch.util.DesktopUtil;
import edu.ucsd.idekerlab.webbymcsearch.util.ShowDialogUtil;
import java.util.HashMap;
import javax.swing.JOptionPane;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.model.CyColumn;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.task.NetworkTaskFactory;
import org.cytoscape.work.TaskIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Adds column with link to query data from another
 * column on Google, Pubmed, iQuery, or other websites
 * @author churas
 */
public class WebSearchAddColTaskFactoryImpl implements NetworkTaskFactory {

	private final static Logger LOGGER = LoggerFactory.getLogger(WebSearchAddColTaskFactoryImpl.class);
	private final CySwingApplication _swingApplication;
	private final ShowDialogUtil _dialogUtil;
	private DesktopUtil _deskTopUtil;
	private WebSearchDialog _webSearchDialog;
	private NodeQueryColumnFactory _nodeQueryColFac; 
 

	public WebSearchAddColTaskFactoryImpl(CySwingApplication swingApplication,
			ShowDialogUtil dialogUtil, WebSearchDialog webSearchDialog) {
		_swingApplication = swingApplication;
		_dialogUtil = dialogUtil;
		_deskTopUtil = new DesktopUtil();
		_webSearchDialog = webSearchDialog;
		_nodeQueryColFac = new NodeQueryColumnFactory();
	}
	
	@Override
	public TaskIterator createTaskIterator(CyNetwork cn) {
		Object[] options = {Constants.ADD_COLUMN, Constants.CANCEL};
		
		HashMap<String, CyColumn> colMap = _nodeQueryColFac.getColumns(cn);
		_webSearchDialog.createGUI(colMap);
		int res = _dialogUtil.showOptionDialog(_swingApplication.getJFrame(),
											   this._webSearchDialog,
														"Add Query Column(s)",
														JOptionPane.YES_NO_OPTION,
														JOptionPane.PLAIN_MESSAGE, 
														null, 
														options,
														options[0]);

		if (res == 0){
			return new TaskIterator(new AddQueryColumnsTask(cn,
					_webSearchDialog.getSelectedColumn(),
			        _webSearchDialog.getSelectedQueries()));
		}
		return new TaskIterator(new DoNothingTask());
	}

	@Override
	public boolean isReady(CyNetwork cn) {
		return true;
	}
	
	
}
