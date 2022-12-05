package edu.ucsd.idekerlab.webbymcsearch.query;

import edu.ucsd.idekerlab.webbymcsearch.util.IconJLabelDialogFactory;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.cytoscape.model.CyColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author churas
 */
@SuppressWarnings("serial")
public class WebSearchDialog extends JPanel {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(WebSearchDialog.class);
	
	private final static String REMEMBER_TEXT = "Remember selections";
	private final static String REMEMBER_TOOLTIP = "Not implemented";
	private final static String REMEMBER_MESSAGE = "Remember selections has not been implemented";
	
	//private final static String REMEMBER_TOOLTIP = "If set, remembers selections";
	//private final static String REMEMBER_MESSAGE = "If set, remembers selected column and <br/>"
	//		+ "services to query. ";   
	private final static String COMBO_TEXT = "Choose data";
	private final static String COMBO_TOOLTIP = "Select attribute to use for web query";
	private final static String COMBO_MESSAGE = "Select attribute to use for web query<br/><br/>"
			+ "This dropdown menu displays a list of node attributes for the selected node<br/>"
			+ "that are of type <b>String</b> or <b>List of String</b><br/><br/>"
			+ "The entries are in the format of <b>[NAME]  VALUE</b> where:<br/>"
			+ "<ul>"
			+ "<li><b>NAME</b> is the name of the attribute</li>"
			+ "<li><b>VALUE</b> is the data for that attribute</li>"
			+ "</ul>"
			+ "<b>NAME / VALUE</b> entries exceeding <b>"
			+ Integer.toString(WebSearchTaskFactoryImpl.MAX_RAW_COL_LEN)
			+ " / " + Integer.toString(WebSearchTaskFactoryImpl.MAX_RAW_DATA_LEN)
			+ "</b> characters<br/>"
			+ "are truncated and have the characters <b>'...'</b> appended<br/>";

	      
	
	private boolean _guiLoaded;
	private HashMap<JCheckBox, WebQuery> _checkBoxes;
	
	private JComboBox _comboBox;
	private HashMap<String, CyColumn> _columns;
	private IconJLabelDialogFactory _iconFactory;
	private String _lastSelectedColumn;
	private Set<WebQuery> _queries;
	
	public WebSearchDialog(IconJLabelDialogFactory iconFactory,
			Set<WebQuery> queries){
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		_iconFactory = iconFactory;
		_guiLoaded = false;
		_queries = queries;
		_checkBoxes = new LinkedHashMap<>();
	}
	
	/**
	* Creates the GUI
	* @return true upon success otherwise false
	*/
    public boolean createGUI(HashMap<String, CyColumn> columns) {
		if (_guiLoaded == false){
			this.add(getPanel());
			_guiLoaded = true;
		}
		return updateComboBox(columns);
    }
	
	public CyColumn getSelectedColumn(){
		
		return _columns.get(_comboBox.getSelectedItem());
	}
	
	/**
	* Gets value of "Remember selection" checkbox
	* @return value of checkbox or {@code false} if checkbox is null
	*/
	public boolean rememberChoice(){
		return true;
	}
	
	public Set<WebQuery> getSelectedQueries(){
		Set<WebQuery> qSet = new HashSet<>();
		for (JCheckBox checkBox : this._checkBoxes.keySet()){
			if (checkBox.isSelected()){
				qSet.add(_checkBoxes.get(checkBox));
			}
		}
		return qSet;
	}
	
	private boolean updateComboBox(HashMap<String, CyColumn> columns){
		String selItem = (String)_comboBox.getSelectedItem();
		if (selItem != null){
			_lastSelectedColumn = selItem.substring(0, selItem.indexOf("]  ")+1);
		}
		
		// if remember is disabled check all the boxes which
		// is default behavior
		/*
		if (_rememberCheckBox.isSelected() == false){
			for (JCheckBox checkBox : this._checkBoxes.keySet()){
				checkBox.setSelected(true);
			}
		}
		*/
		_comboBox.removeAllItems();
		_columns = columns;
		for (String col : columns.keySet()){
			_comboBox.addItem(col);
		
			if (_lastSelectedColumn != null){
				if (col.startsWith(_lastSelectedColumn)){
					_comboBox.setSelectedItem(col);
				}
			}
		}
		return true;
	}
	
	private JPanel getPanel(){
		JPanel basePanel = new JPanel();
		basePanel.setLayout(new GridBagLayout());
		
		GridBagConstraints chooserConstraints = new GridBagConstraints();
		chooserConstraints.gridy = 0;
		chooserConstraints.gridx = 0;
		chooserConstraints.insets = new Insets(0,0,0,0);
		basePanel.add(getDataChooserPanel(), chooserConstraints);
		
		GridBagConstraints queryConstraints = new GridBagConstraints();
		queryConstraints.gridy = 1;
		queryConstraints.gridx = 0;
		queryConstraints.fill = GridBagConstraints.HORIZONTAL;
		queryConstraints.insets = new Insets(0,0,0,0);
		basePanel.add(getWebQueryPanel(), queryConstraints);
		
		GridBagConstraints rememberConstraints = new GridBagConstraints();
		rememberConstraints.gridy = 2;
		rememberConstraints.gridx = 0;
		rememberConstraints.anchor = GridBagConstraints.LINE_END;
		rememberConstraints.insets = new Insets(0, 0, 0, 0);
		
		//basePanel.add(getRememberSelectionPanel(), rememberConstraints);
		
		return basePanel;
	}
	
	private JPanel getWebQueryPanel(){
		JPanel queryPanel = new JPanel();
		queryPanel.setName("queryPanel");
		queryPanel.setLayout(new GridBagLayout());
		queryPanel.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createTitledBorder("Web Query"),
					BorderFactory.createEmptyBorder(5,5,5,5)));
		
		int yCoord = 0;
		for (WebQuery wq : this._queries){
			GridBagConstraints checkBoxConstraints = new GridBagConstraints();
			checkBoxConstraints.gridy = yCoord;
			checkBoxConstraints.gridx = 0;
			checkBoxConstraints.anchor = GridBagConstraints.WEST;
			checkBoxConstraints.fill = GridBagConstraints.NONE;
			checkBoxConstraints.insets = new Insets(0, 0, 0, 0);
			JCheckBox checkBox = new JCheckBox(wq.getGuiVisibleName());
			checkBox.setName(wq.getName());
			checkBox.setSelected(true);
			checkBox.setEnabled(true);
			checkBox.setToolTipText("Run " + wq.getGuiVisibleName());
			_checkBoxes.put(checkBox, wq);
			queryPanel.add(checkBox, checkBoxConstraints);
			yCoord++;
		}
		return queryPanel;
	}
	
	private JPanel getDataChooserPanel(){
		JPanel chooserPanel = new JPanel();
		chooserPanel.setName("chooserPanel");
		chooserPanel.setLayout(new GridBagLayout());
		chooserPanel.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createTitledBorder("Data Chooser"),
					BorderFactory.createEmptyBorder(5,5,5,5)));

		GridBagConstraints labelConstraints = new GridBagConstraints();
		labelConstraints.gridy = 0;
		labelConstraints.gridx = 0;
		labelConstraints.anchor = GridBagConstraints.LINE_START;
		labelConstraints.fill = GridBagConstraints.NONE;
		labelConstraints.insets = new Insets(0, 5, 5, 0);
		chooserPanel.add(new JLabel(COMBO_TEXT + ": "), labelConstraints);

		GridBagConstraints comboConstraints = new GridBagConstraints();
		comboConstraints.gridy = 0;
		comboConstraints.gridx = 1;
		comboConstraints.insets = new Insets(0, 0, 5, 0);

		_comboBox = new JComboBox();
		_comboBox.setName("comboBox");
		_comboBox.setToolTipText(COMBO_TOOLTIP);
		chooserPanel.add(_comboBox, comboConstraints);
		
		GridBagConstraints comboIcon = new GridBagConstraints();
		comboIcon.gridy = 0;
		comboIcon.gridx = 2;
		comboIcon.insets = new Insets(0, 0, 5, 0);
		chooserPanel.add(this.getComboInfoIcon(), comboIcon);

		return chooserPanel;
	}
	
	/**
	* Gets Chooser dropdown info icon
	* @return 
	*/
	private JLabel getComboInfoIcon(){
		JLabel iconLabel = _iconFactory.getJLabelIcon(this,"info_icon", "png",
					WebSearchDialog.COMBO_TEXT + " combobox", 
					WebSearchDialog.COMBO_MESSAGE, 20, 40);
		iconLabel.setName("comboIcon");
		iconLabel.setToolTipText("Click here for more information about " +
					WebSearchDialog.COMBO_TEXT);
		return iconLabel;
	}
}
