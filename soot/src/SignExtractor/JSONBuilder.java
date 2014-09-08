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

	public class Vtypes {
		public static final int vint = 0;
		public static final int vstring = 1;
		public static final int vjsonarray = 2;
		public static final int vconst = 3;
	}

	public static void main(String args[]) throws Exception {
		JSONBuilder jb = new JSONBuilder();
		jb.methodlist = Arrays.asList("<init>", "accumulate", "setEntity",
				"setHeader", "toString", "append", "put");
		jb.TrackingReg = "$r10";
		jb.ExtractingSignature("D:\\Github\\soot\\systests\\programslices\\cnn_receive.jimple");
		// jb.printBuffer(jb.hmBuffer, "$r9");
	}

	@Override
	public void GetEquivalenttb(HashMap<String, Tree> BFTtable,
			HashMap<String, EquvNode> EQtable, String TrackingReg) throws NodeNotFoundException {
		Tree BTTree = BFTtable.get(TrackingReg);

		for (Iterator<BFNode> iter = BTTree.preOrderTraversal().iterator(); iter
				.hasNext();) {
			BFNode bfn = iter.next();
			if (bfn.getKey() == null)
				continue;
			// printTree(BTTree, "R10");
			@SuppressWarnings("unchecked")
			BFNode prt = (BFNode) BTTree.parent(bfn);

			if (prt.getKey() == null) {
				SetEqtable("root", EQtable, bfn);
			}
			else{
				SetEqtable(prt.getKey(), EQtable, bfn);
			}
		}
		
		GenRegx(EQtable, "root");
	}
	
	private void GenRegx(HashMap<String, EquvNode> EQtable,String key)
	{
		EquvNode en = EQtable.get(key);
		HashMap<String, ArrayList<BFNode>> ENtable = en.getENtable();
		ArrayList<BFNode> al = null;
		int i = 1;
		
		System.out.print("{");
		
		al = ENtable.get("String");
		System.out.print("((");
		for ( BFNode bfn : al ) {
			System.out.print("(" + bfn.getKey() + ")");
			if ( al.size() > i )
				System.out.print("|");
			else {
				System.out.print(")*");
				System.out.print(":");
			}
			i++;
		}
		System.out.print("\"[a-z0-9A-Z]*\",*)*");
		
		i = 1;
		al = ENtable.get("Const");
		for ( BFNode bfn : al ) {
			System.out.print(bfn.getKey()+":"+bfn.getValue());
			if ( al.size() > i )
				System.out.print(",");
			i++;
		}
		
		
		al = ENtable.get("JSONArray");
		
		if ( al.size() > 0 )
			System.out.print(",");
		
		for ( BFNode bfn : al ) {
			System.out.print(bfn.getKey()+":");
			GenRegx(EQtable, bfn.getKey());
		}
		
		System.out.print("}");
	}
	
	private void SetEqtable(String key, HashMap<String, EquvNode> EQtable, BFNode bfn) {
		EquvNode en = null;
		en = getEquvNode(key, EQtable);
		HashMap<String, ArrayList<BFNode>> ENtable = null;
		ENtable = en.getENtable();
		// System.out.println("Root!");
		switch (checkVtype(bfn)) {
		case Vtypes.vstring:
			ENtable.get("String").add(bfn);
//			System.out.println("String key : " + bfn.getKey());
			break;
		case Vtypes.vconst:
			ENtable.get("Const").add(bfn);
//			System.out.println("Const key : " + bfn.getKey());
			break;
		case Vtypes.vint:
			ENtable.get("int").add(bfn);
//			System.out.println("int key : " + bfn.getKey());
			break;
		case Vtypes.vjsonarray:
			ENtable.get("JSONArray").add(bfn);
//			System.out.println("JSONArray key : " + bfn.getKey());
			break;
		default:
//			System.out.println("Error!");
			break;
		}
	}
	
	//traversal Equivalent table
	private void printEqtable(HashMap<String, EquvNode> EQtable,String key) {
		EquvNode en = EQtable.get(key);
		HashMap<String, ArrayList<BFNode>> ENtable = en.getENtable();
		ArrayList<BFNode> al = null;
		
		System.out.println("key : " + key);
		al = ENtable.get("String");
		for ( BFNode bfn : al ) {
			System.out.println("String OR : " + bfn.getKey());
		}
		
		al = ENtable.get("Const");
		for ( BFNode bfn : al ) {
			System.out.println("Const OR : " + bfn.getKey());
		}
		
		al = ENtable.get("JSONArray");
		for ( BFNode bfn : al ) {
			System.out.println("JSONArray OR : " + bfn.getKey());
			printEqtable(EQtable, bfn.getKey());
		}
		
	}
	
	private EquvNode getEquvNode(String key, HashMap<String, EquvNode> EQtable) {
		if (EQtable.containsKey(key)) {
			EquvNode qn = EQtable.get(key);
			return qn;
		} else {
			EquvNode qn = new EquvNode();
			EQtable.put(key, qn);
			return qn;
		}
	}

	private int checkVtype(BFNode bfn) {

		if (bfn.getVtype().indexOf("String") != -1) {
			if (bfn.getValue() == null)
				return Vtypes.vstring;
			else if ( isconst(bfn.getValue()) )
				return Vtypes.vconst;
		} else if (bfn.getVtype().indexOf("int") != -1)
			return Vtypes.vint;
		else if (bfn.getVtype().indexOf("JSONArray") != -1)
			return Vtypes.vjsonarray;

		return -1;
	}

	@Override
	public String GenRegex(HashMap<String, SymbolEntry> SMtable,
			HashMap<String, Tree> BFTtable, String TrackingReg) {
		return "";
	}

	@Override
	public void ExtractingExpr(InstanceInvokeExpr iie,
			HashMap<String, SymbolEntry> SMtable,
			HashMap<String, Tree> BFTtable, Unit ut)
			throws NodeNotFoundException {

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
	public void printTree(Tree tr, String Treename) {
		System.out.println("===============================");
		System.out.println("Tree " + Treename + " PreorderTraversal");
		for (Iterator<BFNode> iter = tr.preOrderTraversal().iterator(); iter
				.hasNext();) {
			BFNode bfn = iter.next();
			System.out.print(bfn.getKey() + ":" + bfn.getValue() + "\tVtype : "
					+ bfn.getVtype());
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
			HashMap<String, Tree> BFTtable, Unit ut)
			throws NodeNotFoundException {
		if (iie.getMethodRef().toString() == "<org.json.JSONObject: org.json.JSONObject put(java.lang.String,java.lang.Object)>") {
			if (isconst(iie.getArg(1).toString())) {
				Tree BTTree = BFTtable.get(iie.getBase().toString());
				if (BTTree != null) {
					BFNode bfn = new BFNode();

					bfn.setKey(iie.getArg(0).toString());
					bfn.setValue(iie.getArg(1).toString());
					bfn.setVtype(iie.getArg(1).getType().toString());
					BTTree.add(bfn);
					// printTree(BTTree,iie.getBase().toString());
				}
			} else {
				Tree BTTree = BFTtable.get(iie.getBase().toString());
				if (BTTree != null) {
					SymbolEntry se = SMtable.get(iie.getArg(1).toString());
					if (se != null) {
						if (iie.getArg(1).getType().toString()
								.indexOf("JSONArray") == -1) {
							BFNode bfn = new BFNode();
							bfn.setKey(iie.getArg(0).toString());
							bfn.setValue(se.getValue());
							bfn.setVtype(se.getType());
							BTTree.add(bfn);
							// printTree(BTTree,iie.getBase().toString());
						} else {
							BFNode parent = new BFNode();
							parent.setKey(iie.getArg(0).toString());
							parent.setValue(null);
							parent.setVtype(iie.getArg(1).getType().toString());
							BTTree.add(parent);

							// Copy tree
							Tree SrcTree = BFTtable.get(iie.getArg(1)
									.toString());
							if (SrcTree != null) {
								// must tree traversal! parent change
								copytree(BTTree, SrcTree, parent);
								// printTree(BTTree,iie.getBase().toString());
							}
						}
					}
				}
			}
		} else if (iie.getMethodRef().toString() == "<org.json.JSONArray: org.json.JSONArray put(java.lang.Object)>") {
			if (iie.getArg(0).getType().toString().indexOf("JSONObject") != -1) {
				Tree BTTree = BFTtable.get(iie.getBase().toString());
				if (BTTree != null) {
					Tree SrcTree = BFTtable.get(iie.getArg(0).toString());
					if (SrcTree != null) {
						copytree(BTTree, SrcTree, (BFNode) BTTree.root());
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
			// System.out.println("Init DefaultHttpClient");
		} else if (iie.getMethodRef().toString() == "<org.json.JSONObject: void <init>()>") {
			// System.out.println("Init JSONObject");
		} else if (iie.getMethodRef().toString() == "<java.lang.StringBuilder: void <init>()>") {
			// System.out.println("Init StringBuilder");
		} else if (iie.getMethodRef().toString() == "<org.apache.http.client.methods.HttpPost: void <init>(java.lang.String)>") {
			// System.out.println("Init HttpPost(String)");
		} else if (iie.getMethodRef().toString() == "<org.json.JSONArray: void <init>()>") {
			// System.out.println("Init JSONArray");
		} else if (iie.getMethodRef().toString() == "<org.apache.http.entity.StringEntity: void <init>(java.lang.String)>") {
			// System.out.println("Init StringEntity(String)");
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

			for (Iterator<BFNode> iter = DstTree.children(parent).iterator(); iter
					.hasNext();) {
				BFNode bfn = iter.next();
				if (bfn.getKey() == null && bfn.getVtype() == null) {
					DstTree.remove(bfn);
				}
			}

		} catch (NodeNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// printTree(DstTree, "combined tree");
	}
}
