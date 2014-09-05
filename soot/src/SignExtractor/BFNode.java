package SignExtractor;


//The key of this node is Variable name.
public class BFNode {
	private String	Key;
	private String	Value;
	private String	Vtype;
	
	/*Getter & Setter*/
	public String getKey() {
		return Key;
	}
	public void setKey(String key) {
		Key = key;
	}
	public String getValue() {
		return Value;
	}
	public void setValue(String value) {
		Value = value;
	}
	public String getVtype() {
		return Vtype;
	}
	public void setVtype(String vtype) {
		Vtype = vtype;
	}
}
