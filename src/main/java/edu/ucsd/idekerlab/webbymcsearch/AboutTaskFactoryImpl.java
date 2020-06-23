package edu.ucsd.idekerlab.webbymcsearch;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Properties;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import edu.ucsd.idekerlab.webbymcsearch.util.JEditorPaneFactory;
import edu.ucsd.idekerlab.webbymcsearch.util.Constants;
import edu.ucsd.idekerlab.webbymcsearch.util.ShowDialogUtil;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.task.NetworkTaskFactory;
import org.cytoscape.work.TaskIterator;

/**
 * A Dummy task that just displays a dialog describing the Community Detection
 * Application
 * @author churas
 */
public class AboutTaskFactoryImpl implements NetworkTaskFactory {

    private CySwingApplication _swingApplication;
    private JEditorPaneFactory _editorPaneFactory;
	private ShowDialogUtil _dialogUtil;
    private JEditorPane _editorPane;
    private ImageIcon _aboutIcon;
    
    public AboutTaskFactoryImpl(CySwingApplication swingApplication,
	    JEditorPaneFactory editorPaneFactory, ShowDialogUtil dialogUtil){
		_swingApplication = swingApplication;
		_editorPaneFactory = editorPaneFactory;
		_dialogUtil = dialogUtil;
    }
    
    private void createAboutEditorPaneIfNeeded(){
	if (_editorPane != null){
	    return;
	}
	
	loadImageIcon();
	
	StringBuilder sb = new StringBuilder();
	String version = "Unknown";
	Properties properties = new Properties();
	try {
	    properties.load(getClass().getClassLoader().getResourceAsStream(Constants.PROP_NAME + ".props"));
	    version = properties.getProperty(Constants.PROJECT_VERSION_PROP, version);
	} catch(IOException | NullPointerException ioex){
	    
	}
	
	sb.append("Webby McSearch (");
	sb.append(version);
	sb.append(") lets ");
	sb.append("one perform web queries<br/>");
	sb.append("on attributes for a selected node<br/>");
	sb.append("using Google, PubMed or other common search engines.<br/><br/>");
    sb.append("This is accomplished by sending the queries to the default web browser.<br/><br/>");
	sb.append("To use open a network and select a <b>single</b> node in the view, then right click to <br/>");
	sb.append("open the context menu and select <b>Apps > Webby McSearch > Web Search</b><br/><br/>");

	sb.append("<b>NOTE:</b> This service is experimental. The interface is subject to change.<br/><br/>");
	sb.append("");
	
	_editorPane = _editorPaneFactory.getDescriptionFrame(sb.toString());
    }

    private void loadImageIcon(){
	    try {
		File imgFile = File.createTempFile("about_icon", "png");
	        InputStream imgStream = getClass().getClassLoader().getResourceAsStream("about_icon.png");
		Files.copy(imgStream, imgFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		_aboutIcon = new ImageIcon(new ImageIcon(imgFile.getPath()).getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
	    }
	    catch (IOException ex){
		    
	    }
	}
    
    /**
     * Brings up an about dialog and then calls a dummy task 
     * that does nothing
     * @param network
     * @return TaskIterator with a dummy task
     */
    @Override
    public TaskIterator createTaskIterator(CyNetwork network) {
		createAboutEditorPaneIfNeeded();
		_dialogUtil.showMessageDialog(_swingApplication.getJFrame(),
			_editorPane, Constants.APP_NAME,
			JOptionPane.INFORMATION_MESSAGE, _aboutIcon);
		return new TaskIterator(new DoNothingTask());
    }

    /**
     * This is always ready
     * @param network
     * @return true
     */
    @Override
    public boolean isReady(CyNetwork network) {
	return true;
    }
    
}
