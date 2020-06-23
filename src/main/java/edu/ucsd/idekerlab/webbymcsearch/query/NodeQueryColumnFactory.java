package edu.ucsd.idekerlab.webbymcsearch.query;

import edu.ucsd.idekerlab.webbymcsearch.util.ColumnUtil;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import org.cytoscape.model.CyColumn;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyTable;

/**
 *
 * @author churas
 */
public class NodeQueryColumnFactory {
	
	public static final int MAX_RAW_COL_LEN = 20;

	private ColumnUtil _columnUtil;
	public NodeQueryColumnFactory(){
		_columnUtil = new ColumnUtil();
	}
	
	public HashMap<String, CyColumn> getColumns(CyNetwork network){
		CyTable nodeTable = network.getDefaultNodeTable();
		HashMap<String, CyColumn> columnMap = new LinkedHashMap<>();
		for (CyColumn column : nodeTable.getColumns()) {
			
			if (column.getName().equals(CyNetwork.SUID)){
				continue;
			}
			if (column.getType() == List.class ||
					column.getType() == String.class){
				columnMap.put("[" + _columnUtil.getTruncatedColumnName(column,
					MAX_RAW_COL_LEN) + "]  ", column);
			}
		}
		return columnMap;
	}
}
