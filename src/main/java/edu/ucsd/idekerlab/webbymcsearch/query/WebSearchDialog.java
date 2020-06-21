package edu.ucsd.idekerlab.webbymcsearch.query;

import edu.ucsd.idekerlab.webbymcsearch.util.IconJLabelDialogFactory;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
	private final static String REMEMBER_MESSAGE = "Remember selections has not been enabled";
	
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
	private JCheckBox _rememberCheckBox;
	private JCheckBox _googleCheckBox;
	private JCheckBox _pubmedCheckBox;
	private JCheckBox _pubmedCentralCheckBox;
	private JCheckBox _iQueryCheckBox;
	private JComboBox _comboBox;
	private HashMap<String, String> _columns;
	private IconJLabelDialogFactory _iconFactory;
	private String _lastSelectedColumn;
	
	public WebSearchDialog(IconJLabelDialogFactory iconFactory){
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		_iconFactory = iconFactory;
		_guiLoaded = false;
	}
	
	/**
	* Creates the GUI
	* @return true upon success otherwise false
	*/
    public boolean createGUI(HashMap<String, String> columns) {
		if (_guiLoaded == false){
			this.add(getPanel());
			_guiLoaded = true;
		}
		return updateComboBox(columns);
    }
	
	public String getSelectedColumn(){
		
		return _columns.get((String)_comboBox.getSelectedItem());
	}
	
	/**
	* Gets value of "Remember selection" checkbox
	* @return value of checkbox or {@code false} if checkbox is null
	*/
	public boolean rememberChoice(){
		if (_rememberCheckBox == null){
			return false;
		}
		return _rememberCheckBox.isSelected();
	}
	
	public Set<String> getSelectedQueries(){
		Set<String> qSet = new HashSet<>();
		if (_pubmedCheckBox != null && _pubmedCheckBox.isSelected()){
			qSet.add("https://pubmed.ncbi.nlm.nih.gov/?term=");
		}
		if (_googleCheckBox != null && _googleCheckBox.isSelected()){
			qSet.add("https://www.google.com/search?q=");
		}
		if (this._iQueryCheckBox != null && _iQueryCheckBox.isSelected()){
			qSet.add("http://search.ndexbio.org/?genes=");
		}
		if (this._pubmedCentralCheckBox != null && _pubmedCentralCheckBox.isSelected()){
			qSet.add("https://www.ncbi.nlm.nih.gov/pmc/?term=");
		}
		return qSet;
	}
	
	private boolean updateComboBox(HashMap<String, String> columns){
		String selItem = (String)_comboBox.getSelectedItem();
		if (selItem != null){
			_lastSelectedColumn = selItem.substring(0, selItem.indexOf("]  ")+1);
		}
		
		// if remember is disabled check all the boxes which
		// is default behavior
		if (_rememberCheckBox.isSelected() == false){
			_pubmedCheckBox.setSelected(true);
			_googleCheckBox.setSelected(true);
		    _iQueryCheckBox.setSelected(true);
			_pubmedCentralCheckBox.setSelected(true);
		}
		_comboBox.removeAllItems();
		_columns = columns;
		for (String col : columns.keySet()){
			_comboBox.addItem(col);
		
			if (_rememberCheckBox.isSelected() && _lastSelectedColumn != null){
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
		
		basePanel.add(getRememberSelectionPanel(), rememberConstraints);
		
		return basePanel;
	}
	
	private JPanel getWebQueryPanel(){
		JPanel queryPanel = new JPanel();
		queryPanel.setName("queryPanel");
		queryPanel.setLayout(new GridBagLayout());
		queryPanel.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createTitledBorder("Web Query"),
					BorderFactory.createEmptyBorder(5,5,5,5)));
		
		GridBagConstraints googleConstraints = new GridBagConstraints();
		googleConstraints.gridy = 0;
		googleConstraints.gridx = 0;
		googleConstraints.anchor = GridBagConstraints.WEST;
		googleConstraints.fill = GridBagConstraints.NONE;
		googleConstraints.insets = new Insets(0, 0, 0, 0);
		_googleCheckBox = new JCheckBox("Google Search");
		_googleCheckBox.setName("googleCheckBox");
		_googleCheckBox.setSelected(true);
		_googleCheckBox.setEnabled(true);
		_googleCheckBox.setToolTipText("Run Google Search");
		queryPanel.add(_googleCheckBox, googleConstraints);

		GridBagConstraints pubmedConstraints = new GridBagConstraints();
		pubmedConstraints.gridy = 1;
		pubmedConstraints.gridx = 0;
		pubmedConstraints.anchor = GridBagConstraints.WEST;
		pubmedConstraints.fill = GridBagConstraints.NONE;
		pubmedConstraints.insets = new Insets(0, 0, 0, 0);
		_pubmedCheckBox = new JCheckBox("PubMed Search");
		_pubmedCheckBox.setName("pubmedCheckBox");
		_pubmedCheckBox.setSelected(true);
		_pubmedCheckBox.setEnabled(true);
		_pubmedCheckBox.setToolTipText("Run PubMed Search");
		queryPanel.add(_pubmedCheckBox, pubmedConstraints);	
		
		GridBagConstraints pubmedCentralConstraints = new GridBagConstraints();
		pubmedCentralConstraints.gridy = 2;
		pubmedCentralConstraints.gridx = 0;
		pubmedCentralConstraints.anchor = GridBagConstraints.WEST;
		pubmedCentralConstraints.fill = GridBagConstraints.NONE;
		pubmedCentralConstraints.insets = new Insets(0, 0, 0, 0);
		_pubmedCentralCheckBox = new JCheckBox("PubMed Central Search");
		_pubmedCentralCheckBox.setName("pubmedCentralCheckBox");
		_pubmedCentralCheckBox.setSelected(true);
		_pubmedCentralCheckBox.setEnabled(true);
		_pubmedCentralCheckBox.setToolTipText("Run PubMed Central Search");
		queryPanel.add(_pubmedCentralCheckBox, pubmedCentralConstraints);	
		
		GridBagConstraints iQueryConstraints = new GridBagConstraints();
		iQueryConstraints.gridy = 3;
		iQueryConstraints.gridx = 0;
		iQueryConstraints.anchor = GridBagConstraints.WEST;
		iQueryConstraints.fill = GridBagConstraints.NONE;
		iQueryConstraints.insets = new Insets(0, 0, 0, 0);
		_iQueryCheckBox = new JCheckBox("iQuery Search");
		_iQueryCheckBox.setName("iQueryCheckBox");
		_iQueryCheckBox.setSelected(true);
		_iQueryCheckBox.setEnabled(true);
		_iQueryCheckBox.setToolTipText("Run iQuery Search");
		queryPanel.add(_iQueryCheckBox, iQueryConstraints);	

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
	
	private JPanel getRememberSelectionPanel(){
		JPanel rPanel = new JPanel();
		rPanel.setLayout(new GridBagLayout());
		GridBagConstraints rememberConstraints = new GridBagConstraints();
		rememberConstraints.gridy = 0;
		rememberConstraints.gridx = 0;
		rememberConstraints.anchor = GridBagConstraints.LINE_END;
		rememberConstraints.insets = new Insets(0, 0, 0, 0);
		_rememberCheckBox = new JCheckBox(REMEMBER_TEXT);
		_rememberCheckBox.setName("rememberCheckBox");
		_rememberCheckBox.setSelected(true);
		_rememberCheckBox.setEnabled(true);
		_rememberCheckBox.setToolTipText(REMEMBER_TOOLTIP);
		rPanel.add(_rememberCheckBox, rememberConstraints);
		GridBagConstraints rememberIcon = new GridBagConstraints();
		rememberIcon.gridy = 0;
		rememberIcon.gridx = 1;
		rememberIcon.anchor = GridBagConstraints.LINE_START;
		rememberIcon.insets = new Insets(0, 0, 0, 5);
		rPanel.add(getRememberInfoIcon(), rememberIcon);
		return rPanel;
	}
	
	/**
	* Gets Chooser dropdown info icon
	* @return 
	*/
	private JLabel getRememberInfoIcon(){
		JLabel iconLabel = _iconFactory.getJLabelIcon(this,"info_icon", "png",
					WebSearchDialog.REMEMBER_TEXT + " checkbox", 
					REMEMBER_MESSAGE, 20, 40);
		iconLabel.setName("rememberIcon");
		iconLabel.setEnabled(true);
		iconLabel.setToolTipText("Click here for more information about " +
					WebSearchDialog.REMEMBER_TEXT);
		return iconLabel;
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
