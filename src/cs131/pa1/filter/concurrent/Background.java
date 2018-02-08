package cs131.pa1.filter.concurrent;

import java.util.List;

public class Background implements Runnable {
	
	protected boolean done = false;
	protected String command;
	protected String cmd;
    
	public Background(String command) {
		this.command = command.substring(0, command.length()-2);
		 cmd = command;
	}
	
	public void execute() {

		List<ConcurrentFilter> filterlist = ConcurrentCommandBuilder.createFiltersFromCommand(command);
		
		
		if(filterlist != null) {
			//run each filter process manually.
			
			// Put the all the filter into thread and run them individually and join them at last
			int k = 0;
			while(k < filterlist.size()) {
				Thread th = new Thread(filterlist.get(k));
				th.start();
				if (k == filterlist.size() - 1) {
					try {
						th.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
//					done = true;
					ConcurrentREPL.bgtask.remove(cmd);
//					System.out.println("here");
			    }
				k++;
			}
		}
	}
	
//	public boolean done() {
//		return done;
//	}

	@Override
	public void run() {
		execute();
	}
}
