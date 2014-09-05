package SignExtractor;

//The key of this node is Variable Name.
public class SymbolEntry {
	private String 	Type;
	private String 	Value;
	private int		Length;
	
	/*Getter & Setter*/
	public String getType() {
		return Type;
	}
	public void setType(String type) {
		Type = type;
	}
	public String getValue() {
		return Value;
	}
	public void setValue(String value) {
		Value = value;
	}
	public int getLength() {
		return Length;
	}
	public void setLength(int length) {
		Length = length;
	}
}
