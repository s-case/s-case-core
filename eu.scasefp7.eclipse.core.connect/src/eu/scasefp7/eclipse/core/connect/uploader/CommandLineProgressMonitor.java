package eu.scasefp7.eclipse.core.connect.uploader;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * A command line progress monitor used to track the progress of a task. This class has functions similar to the Eclipse
 * {@link IProgressMonitor}.
 * 
 * @author themis
 */
public class CommandLineProgressMonitor {

	/**
	 * The total number of units of work.
	 */
	private int total;

	/**
	 * The completed number of units of work.
	 */
	private int completed;

	/**
	 * The message that will be shown when showing the progress.
	 */
	private String message;

	/**
	 * Initializes this progress monitor.
	 */
	public CommandLineProgressMonitor() {
		message = "";
		total = 0;
		completed = 0;
	}

	/**
	 * Starts a new task and provides the message shown and the total number of units of work.
	 * 
	 * @param message the message that will be shown when showing the progress.
	 * @param total the total number of units of work.
	 */
	public void beginTask(String message, int total) {
		this.total = total;
		this.message = message;
		printProgress();
	}

	/**
	 * Sets the work as completed and clears all variables.
	 */
	public void done() {
		message = "";
		total = 0;
		completed = 0;
	}

	private void printProgress() {
		System.out.print(message + " [");
		for (int i = 0; i < completed; i++)
			System.out.print("=");
		for (int i = 0; i < total - completed; i++)
			System.out.print(" ");
		System.out.println("]");
	}

	/**
	 * Adds a number of completed units of work to the monitored task.
	 *
	 * @param workUnit a number of completed units of work.
	 */
	public void worked(int workUnit) {
		completed += workUnit;
		printProgress();
	}
}
