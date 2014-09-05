package SignExtractor;

import java.util.ArrayList;
import java.util.List;

import soot.Type;

public class EntryIndexer {
	
	public List<BufferEntry> beList;
	public List<Integer> cstIdxes;
	public List<Integer> cstcstIdxes;
	public List<Integer> csvarIdxes;
	public List<Integer> varcsIdxes;
	public int currentidx;
	public String 	LastType;
	public int		Bemaxlevel = 1;



	public EntryIndexer() {
		super();

		beList = new ArrayList<BufferEntry>();
		cstIdxes = new ArrayList<Integer>();
		csvarIdxes = new ArrayList<Integer>();
		varcsIdxes = new ArrayList<Integer>();
		cstcstIdxes = new ArrayList<Integer>();
		currentidx = 0;
		LastType = "";
	}
	
	
}
