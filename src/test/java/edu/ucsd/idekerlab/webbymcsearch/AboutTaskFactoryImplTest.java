package edu.ucsd.idekerlab.webbymcsearch2;

import edu.ucsd.idekerlab.webbymcsearch.AboutTaskFactoryImpl;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import edu.ucsd.idekerlab.webbymcsearch.util.JEditorPaneFactory;
import edu.ucsd.idekerlab.webbymcsearch.util.Constants;
import edu.ucsd.idekerlab.webbymcsearch.util.ShowDialogUtil;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.model.CyNetwork;
import static org.mockito.Mockito.*;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 *
 * @author churas
 */
public class AboutTaskFactoryImplTest {
	
	@Test
	public void testCreateTaskIterator(){
		CySwingApplication mockApp = mock(CySwingApplication.class);
		when(mockApp.getJFrame()).thenReturn(null);
		JEditorPaneFactory mockPaneFac = mock(JEditorPaneFactory.class);
		JEditorPane mockEditorPane = mock(JEditorPane.class);
		ShowDialogUtil mockDialog = mock(ShowDialogUtil.class);
		when(mockPaneFac.getDescriptionFrame(anyString())).thenReturn(mockEditorPane);
		
		AboutTaskFactoryImpl tFac = new AboutTaskFactoryImpl(mockApp, mockPaneFac,
															mockDialog);
		tFac.createTaskIterator(null);
		verify(mockDialog).showMessageDialog(eq(null), eq(mockEditorPane),
				eq(Constants.APP_NAME), eq(JOptionPane.INFORMATION_MESSAGE), any(ImageIcon.class));
	}
	
	@Test
	public void testIsReady(){
		AboutTaskFactoryImpl tFac = new AboutTaskFactoryImpl(null, null,
															null);
		assertTrue(tFac.isReady(null));
		CyNetwork mockNet = mock(CyNetwork.class);
		assertTrue(tFac.isReady(mockNet));
	}
	
}
