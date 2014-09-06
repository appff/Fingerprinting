package SignExtractor;

import java.util.HashMap;
import java.util.List;

public class EquvNode {
	private String Parent;
	private HashMap<String, List<BFNode>> BFlist;
	
	public HashMap<String, List<BFNode>> getBFlist() {
		return BFlist;
	}
	public void setBFlist(HashMap<String, List<BFNode>> bFlist) {
		BFlist = bFlist;
	}
	public String getParent() {
		return Parent;
	}
	public void setParent(String parent) {
		Parent = parent;
	}
	
}
