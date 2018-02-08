package cs131.pa1.filter.concurrent;
//import java.util.LinkedList;
//import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import cs131.pa1.filter.Filter;


public abstract class ConcurrentFilter extends Filter implements Runnable{
	
	
//    protected boolean flag;
    protected boolean done = false;
	protected LinkedBlockingQueue<String> input;
	protected LinkedBlockingQueue<String> output;
	
	@Override
	public void run() {
		process();
		done = true;
	}
	
	@Override
	public void setPrevFilter(Filter prevFilter) {
		prevFilter.setNextFilter(this);
	}
	
	@Override
	public void setNextFilter(Filter nextFilter) {
		if (nextFilter instanceof ConcurrentFilter){
			ConcurrentFilter sequentialNext = (ConcurrentFilter) nextFilter;
			this.next = sequentialNext;
			sequentialNext.prev = this;
			if (this.output == null){
				this.output = new LinkedBlockingQueue<String>();
			}
			sequentialNext.input = this.output;
		} else {
			throw new RuntimeException("Should not attempt to link dissimilar filter types.");
		}
	}
	
	public void process(){		
		
		// Run while loop until the input will not have any further input and prev is done
        while (true) {
			if (prev.isDone() && input.isEmpty()) {
				done = true;
				break;
			}
			if (!input.isEmpty()) {
				String line = "";
				try {
					line = input.take();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				try {
					String processedLine = processLine(line);
					if (processedLine != null){
						output.put(processedLine);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}		
	    }	
	}
	
	// return whether the thread finishes or not
	@Override
	public boolean isDone() {
		return done ;
	}
	
	protected abstract String processLine(String line);
	
}
