package cs131.pa1.filter.concurrent;

public class WcFilter extends ConcurrentFilter {
	private int linecount;
	private int wordcount;
	private int charcount;
	
	public WcFilter() {
		super();
	}
	
	public void process() {
		
		while (true) {
			if (prev.isDone() && input.isEmpty()) {
				try {
					output.put(processLine(null));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				done = true;
				break;
			}
			if (!input.isEmpty()) {
				String line = "";
				try {
					line = input.take();
					processLine(line);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
		}
	}
	
	public String processLine(String line) {
		
		//prints current result if ever passed a null
		if(line == null) {
			return linecount + " " + wordcount + " " + charcount;
		}
		
		if (prev.isDone() && input.isEmpty()) {
			String[] wct = line.split(" ");
			wordcount += wct.length;
			String[] cct = line.split("|");
			charcount += cct.length;
			return ++linecount + " " + wordcount + " " + charcount;
		} else {
			linecount++;
			String[] wct = line.split(" ");
			wordcount += wct.length;
			String[] cct = line.split("|");
			charcount += cct.length;
			return null;
		}
	}
}
