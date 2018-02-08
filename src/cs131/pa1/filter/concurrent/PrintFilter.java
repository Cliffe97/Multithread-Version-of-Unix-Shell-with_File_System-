package cs131.pa1.filter.concurrent;

public class PrintFilter extends ConcurrentFilter {
	public PrintFilter() {
		super();
	}
	
	public void process() {
		
		// To avoid the stop of while loop while the prev did not finish
		while(true) {
			if (prev.isDone() && input.isEmpty()) {
				done = true;
				break;
			}
			if (!input.isEmpty()) {
				try {
					String line = input.take();
					processLine(line);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public String processLine(String line) {
		System.out.println(line);
		return null;
	}
}
