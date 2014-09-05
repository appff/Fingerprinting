package SignExtractor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.gaurav.tree.NodeNotFoundException;
import com.gaurav.tree.Tree;

import soot.Local;
import soot.Unit;
import soot.Value;
import soot.jimple.InstanceInvokeExpr;
import soot.util.Chain;

public class JSONBuilder extends BufferExtractor {

	public class MethodIds {
		public static final int init = 0;
		public static final int accumulate = 1;
		public static final int setEntity = 2;
		public static final int setHeader = 3;
		public static final int toString = 4;
		public static final int append = 5;
		public static final int put = 6;
	}

	public static void main(String args[]) throws Exception {
		JSONBuilder jb = new JSONBuilder();
		jb.methodlist = Arrays.asList("<init>", "accumulate", "setEntity",
				"setHeader", "toString", "append", "put");
		jb.ExtractingSignature("D:\\Github\\soot\\systests\\programslices\\cnn_receive.jimple");
		// jb.printBuffer(jb.hmBuffer, "$r9");
	}

	@Override
	public String GenRegex(HashMap<String, SymbolEntry> SMtable,
			HashMap<String, Tree> BFTtable, String TrackingReg) {
		return "";
	}

	@Override
	public void ExtractingExpr(InstanceInvokeExpr iie,
			HashMap<String, SymbolEntry> SMtable,
			HashMap<String, Tree> BFTtable, Unit ut) throws NodeNotFoundException {

		String strMethod = iie.getMethodRef().name();
		List<Value> args = new ArrayList<Value>();
		String strDest = iie.getBase().toString();
		EntryIndexer temparg = null;
		EntryIndexer eiDst = null;
		Value larg = null;
		Value rarg = null;

		// Check method
		int chkMethod = methodlist.indexOf(strMethod);

		// loading args
		args = iie.getArgs();

		switch (chkMethod) {
		case MethodIds.init:
			initfuncHandler(iie, SMtable, BFTtable, ut);
			break;
		case MethodIds.append:
			appendfuncHandler(iie, SMtable, BFTtable, ut);
			break;
		case MethodIds.put:
			putfuncHandler(iie, SMtable, BFTtable, ut);
			break;
		case MethodIds.accumulate:
			break;
		case MethodIds.setEntity:
			break;
		case MethodIds.setHeader:
			break;
		case MethodIds.toString:
			break;
		default:
			break;
		}
	}
	
	public void setEntityfuncHandler(InstanceInvokeExpr iie,
			HashMap<String, SymbolEntry> SMtable,
			HashMap<String, Tree> BFTtable, Unit ut) {
		
	}
	
	public void StringEntityfuncHandler(InstanceInvokeExpr iie,
			HashMap<String, SymbolEntry> SMtable,
			HashMap<String, Tree> BFTtable, Unit ut) {
		if (iie.getMethodRef().toString() == "<org.apache.http.entity.StringEntity: void <init>(java.lang.String)>") {
			
		}
	}
	
	@SuppressWarnings("unchecked")
	public void printTree(Tree tr, String Treename)
	{
		System.out.println("===============================");
		System.out.println("Tree " + Treename +" PreorderTraversal");
		for ( Iterator<BFNode> iter = tr.preOrderTraversal().iterator(); iter.hasNext(); )
		{
			BFNode bfn = iter.next();
			System.out.print(bfn.getKey() + ":" + bfn.getValue() + "\tVtype : " + bfn.getVtype());
			try {
				System.out.print("\nThis : " + bfn);
				System.out.println("\nParent : " + tr.parent(bfn));
			} catch (NodeNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Tree depth : " + tr.depth());
	}
	
	@SuppressWarnings("unchecked")
	public void putfuncHandler(InstanceInvokeExpr iie,
			HashMap<String, SymbolEntry> SMtable,
			HashMap<String, Tree> BFTtable, Unit ut) throws NodeNotFoundException {
		if (iie.getMethodRef().toString() == "<org.json.JSONObject: org.json.JSONObject put(java.lang.String,java.lang.Object)>") {
			if ( isconst(iie.getArg(1).toString()) ) {
				Tree BFTree = BFTtable.get(iie.getBase().toString());
				if (BFTree != null) {
					BFNode bfn = new BFNode();

					bfn.setKey(iie.getArg(0).toString());
					bfn.setValue(iie.getArg(1).toString());
					bfn.setVtype(iie.getArg(1).getType().toString());
					BFTree.add(bfn);
//					printTree(BFTree,iie.getBase().toString());
				}
			} else {
				Tree BFTree = BFTtable.get(iie.getBase().toString());
				if ( BFTree != null ) {
					SymbolEntry se = SMtable.get(iie.getArg(1).toString());
					if ( se != null ) {
						if ( iie.getArg(1).getType().toString().indexOf("JSONArray") == -1 ) {
							BFNode bfn = new BFNode();
							bfn.setKey(iie.getArg(0).toString());
							bfn.setValue(se.getValue());
							bfn.setVtype(se.getType());
							BFTree.add(bfn);
//							printTree(BFTree,iie.getBase().toString());
						} else {
							BFNode parent = new BFNode();
							parent.setKey(iie.getArg(0).toString());
							parent.setValue(null);
							parent.setVtype(iie.getArg(1).getType().toString());
							BFTree.add(parent);
							
							//Copy tree
							Tree SrcTree = BFTtable.get(iie.getArg(1).toString());
							if ( SrcTree != null ) {
								//must tree traversal! parent change
								copytree(BFTree, SrcTree, parent);
//								printTree(BFTree,iie.getBase().toString());
							}
						}
					}
				}
			}
		} else if (iie.getMethodRef().toString() == "<org.json.JSONArray: org.json.JSONArray put(java.lang.Object)>") {
			if ( iie.getArg(0).getType().toString().indexOf("JSONObject") != -1 ) {
				Tree BFTree = BFTtable.get(iie.getBase().toString());
				if ( BFTree != null ) {
					Tree SrcTree = BFTtable.get(iie.getArg(0).toString());
					if ( SrcTree != null ) {
						copytree(BFTree, SrcTree, (BFNode)BFTree.root());
					}
				}
			}
		}
	}

	public void appendfuncHandler(InstanceInvokeExpr iie,
			HashMap<String, SymbolEntry> SMtable,
			HashMap<String, Tree> BFTtable, Unit ut) {
		if (iie.getMethodRef().toString() == "<java.lang.StringBuilder: java.lang.StringBuilder append(java.lang.String)>") {
		}
	}

	public void initfuncHandler(InstanceInvokeExpr iie,
			HashMap<String, SymbolEntry> SMtable,
			HashMap<String, Tree> BFTtable, Unit ut) {
		if (iie.getMethodRef().toString() == "<org.apache.http.impl.client.DefaultHttpClient: void <init>()>") {
//			System.out.println("Init DefaultHttpClient");
		} else if (iie.getMethodRef().toString() == "<org.json.JSONObject: void <init>()>") {
//			System.out.println("Init JSONObject");
		} else if (iie.getMethodRef().toString() == "<java.lang.StringBuilder: void <init>()>") {
//			System.out.println("Init StringBuilder");
		} else if (iie.getMethodRef().toString() == "<org.apache.http.client.methods.HttpPost: void <init>(java.lang.String)>") {
//			System.out.println("Init HttpPost(String)");
		} else if (iie.getMethodRef().toString() == "<org.json.JSONArray: void <init>()>") {
//			System.out.println("Init JSONArray");
		} else if (iie.getMethodRef().toString() == "<org.apache.http.entity.StringEntity: void <init>(java.lang.String)>") {
//			System.out.println("Init StringEntity(String)");
		}
	}
	
	public boolean isconst(String arg) {
		if (arg.indexOf("$") != -1)
			return false;
		else
			return true;
	}

	@SuppressWarnings("unchecked")
	public void copytree(Tree DstTree, Tree SrcTree, BFNode parent) {
		try {
			DstTree.addAll(parent, SrcTree);
			
			for ( Iterator<BFNode> iter = DstTree.children(parent).iterator(); iter.hasNext(); )
			{
				BFNode bfn = iter.next();
				if ( bfn.getKey() == null && bfn.getVtype() == null )
				{
					DstTree.remove(bfn);
				}
			}
			
		} catch (NodeNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		printTree(DstTree, "combined tree");
	}
}
