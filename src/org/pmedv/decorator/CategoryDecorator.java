package org.pmedv.decorator;

import java.util.Iterator;

import org.pmedv.pojos.forums.Category;

public class CategoryDecorator extends CustomizableTableDecorator {
	
	public String getNumthreads() {
		Category category = (Category)getCurrentRowObject();
		
		return Integer.toString(category.getThreads().size());
		
	}

	public String getNumpostings() {
		Category category = (Category)getCurrentRowObject();
		
		int numpostings = 0;
		
		for (Iterator threadIterator=category.getThreads().iterator();threadIterator.hasNext();) {
			org.pmedv.pojos.forums.Thread currentThread = (org.pmedv.pojos.forums.Thread)threadIterator.next();
			
			numpostings += currentThread.getPostings().size();
			
		}
		
		return Integer.toString(numpostings);
		
	}

	
}
