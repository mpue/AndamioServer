package org.pmedv.comparator;

import java.io.Serializable;
import java.util.Comparator;

import org.pmedv.pojos.UserComment;

public class CommentComparator implements Comparator<UserComment>, Serializable{

	@Override
	public int compare(UserComment o1, UserComment o2) {

		if (o1.getCreated().after(o2.getCreated()))
			return 1;
		else if (o2.getCreated().after(o1.getCreated()))
			return -1;
		
		return 0;
	}

}
