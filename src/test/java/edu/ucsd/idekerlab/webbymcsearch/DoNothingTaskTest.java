package edu.ucsd.idekerlab.webbymcsearch2;

import edu.ucsd.idekerlab.webbymcsearch.DoNothingTask;
import org.cytoscape.work.TaskMonitor;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Test;

/**
 *
 * @author churas
 */
public class DoNothingTaskTest {
	
	@Test
	public void testRun(){
		DoNothingTask task = new DoNothingTask();
		TaskMonitor mockMonitor = mock(TaskMonitor.class);
		try {
			task.run(mockMonitor);
		} catch(Exception ex){
			fail("Unexpected Exception: " + ex.getMessage());
		}
	}
	
}
