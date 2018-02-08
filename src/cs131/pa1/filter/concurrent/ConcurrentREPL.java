package cs131.pa1.filter.concurrent;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.*;

import cs131.pa1.filter.Message;

public class ConcurrentREPL {

	static String currentWorkingDirectory;
	static ExecutorService bg = Executors.newCachedThreadPool();
	static Queue<String> bgtask = new LinkedList<String>();

	
	public static void main(String[] args){
		currentWorkingDirectory = System.getProperty("user.dir");
		Scanner s = new Scanner(System.in);
		System.out.print(Message.WELCOME);
		String command;
		while(true) {
			
			//obtaining the command from the user
			System.out.print(Message.NEWCOMMAND);
			command = s.nextLine();
			if(command.equals("exit")) {
				break;
			} else if (command.equals("repl_jobs")) {
				Repl_jobs();
			} else if(!command.trim().equals("")) {	
				
				// Background jobs and add the job to the queue
				if(command.endsWith("&")) {
					Background task = new Background(command);
					bgtask.add(task.cmd);
					Thread t = new Thread(task);
					bg.submit(t);
					
				} else {
				
					//building the filters list from the command
					List<ConcurrentFilter> filterlist = ConcurrentCommandBuilder.createFiltersFromCommand(command);
					
					//checking to see if construction was successful. If not, prompt user for another command
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
						    }
							k++;
						}	
					}
				}
			}
		}
		s.close();
		System.out.print(Message.GOODBYE);
	}

	public static void Repl_jobs() {
		
		// To print the running thread
		for (int i = 0; i < bgtask.size(); i++) {
			String k = bgtask.poll();
			System.out.println("\t" +(i + 1) + ". " + k);
			bgtask.add(k);
		}
	}
}