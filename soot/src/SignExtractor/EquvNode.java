package SignExtractor;

import java.util.List;

public class EquvNode {
	private String Parent;
	private String Vtype;
	private List<BFNode> BFlist;
	
	public String getParent() {
		return Parent;
	}
	public void setParent(String parent) {
		Parent = parent;
	}
	public String getVtype() {
		return Vtype;
	}
	public void setVtype(String vtype) {
		Vtype = vtype;
	}
	public List<BFNode> getBFlist() {
		return BFlist;
	}
	public void setBFlist(List<BFNode> bFlist) {
		BFlist = bFlist;
	}
}
