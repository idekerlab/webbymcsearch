package edu.ucsd.idekerlab.webbymcsearch.query;

import edu.ucsd.idekerlab.webbymcsearch.util.ColumnUtil;
import edu.ucsd.idekerlab.webbymcsearch.util.CyNetworkUtil;
import java.util.Set;
import org.cytoscape.model.CyColumn;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

/**
 *
 * @author churas
 */
public class AddQueryColumnsTask extends AbstractTask {

	private CyNetwork _network;
	private CyColumn _selectedColumn;
	private Set<WebQuery> _selectedQueries;
	private boolean _canceled;
	private CyNetworkUtil _cyNetworkUtil;
	private ColumnUtil _columnUtil;
	public AddQueryColumnsTask(CyNetwork network, final CyColumn selectedColumn,
			        Set<WebQuery> selectedQueries){
		_network = network;
		_selectedColumn = selectedColumn;
		_selectedQueries = selectedQueries;
		_canceled = false;
		_cyNetworkUtil = new CyNetworkUtil();
		_columnUtil = new ColumnUtil();
	}
	
	@Override
	public void run(TaskMonitor tm) throws Exception {
		tm.setTitle("Creating query column(s)");
		tm.setProgress(0.0);
		if (_canceled){
			return;
		}
		double numColsToAdd = _selectedQueries.size();
		for (WebQuery selectedQuery : _selectedQueries){
			String newColName = "(" + _selectedColumn.getName() + ") "
							+ selectedQuery.getColumnName()
							+ " query";
			_cyNetworkUtil.createTableColumn(_network.getDefaultNodeTable(),
					newColName, String.class, false, "");
			
			for (CyNode node : _network.getNodeList()){
				String rawValue = _columnUtil.getColumnData(_network, _selectedColumn, node);
				if (rawValue == null){
					continue;
				}
				//replace vertical bar and commas with space in raw value
				String qStr = _columnUtil.getQueryString(rawValue.replace(",", " ").replace("|", " "),
						selectedQuery.getReplaceWhiteSpaceWith());
				if (qStr == null){
					continue;
				}
				_network.getRow(node).set(CyNetworkUtil.WEBBY_MCSEARCH_NAMESPACE,
                                                   newColName, selectedQuery.getUrlAsString() + qStr);
						//newColName , "<a target=\"_blank\" href=\"" + selectedQuery.getUrlAsString() + qStr + "\">Run " + selectedQuery.getColumnName() + " Query via browser</a>");
			}
		}
		tm.setProgress(100.0);
		
	}
	
	@Override
	public void cancel() {
		_canceled = true;
		super.cancel();
	}
}
