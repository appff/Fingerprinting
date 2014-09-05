package SignExtractor;

import java.util.Iterator;

import com.gaurav.tree.*;
public class TreeExam {
	@SuppressWarnings({ "unchecked", "deprecation" })
	public static void main(String args[]) throws Exception
	{
		Tree tr = new ArrayListTree<BFNode>();
		
		BFNode bfn = new BFNode();
		bfn.setKey("name");
		bfn.setValue("Kimjeongmin");
		bfn.setVtype("String");
		
		tr.add(bfn);
		
		bfn = new BFNode();
		bfn.setKey("deviceToken");
		bfn.setValue("$r4");
		bfn.setVtype("String");
		
		tr.add(bfn);
		
		bfn = new BFNode();
		bfn.setKey("platform");
		bfn.setValue("GCM");
		bfn.setVtype("String");
		
		tr.add(bfn);
		
		bfn = new BFNode();
		bfn.setKey("features");
		bfn.setValue("");
		bfn.setVtype("JSONArray");
		
		tr.add(bfn);
		
		BFNode cbfn = new BFNode();
		cbfn.setKey("name");
		cbfn.setValue("$r3");
		cbfn.setVtype("String");
		
		tr.add(bfn,cbfn);
		
		cbfn = new BFNode();
		cbfn.setKey("path");
		cbfn.setValue("$r3");
		cbfn.setVtype("String");

		tr.add(bfn,cbfn);
		
		for ( Iterator<BFNode> iter = tr.preOrderTraversal().iterator(); iter.hasNext(); )
		{
			bfn = iter.next();
			System.out.println(bfn.getKey() + ":" + bfn.getValue());
		}
	}
}
