package SignExtractor;

/* BufferExtractor Abstract class */
import soot.jimple.*;
import soot.jimple.parser.*;
import soot.util.Chain;

import java.io.*;
import java.util.*;

import soot.*;

import com.gaurav.tree.*;

public abstract class BufferExtractor {

	public String TrackingReg;
	public List<String> methodlist;
	// CreateHashMap
	public HashMap<String, SymbolEntry> SMtable = new HashMap<String, SymbolEntry>();
	@SuppressWarnings("rawtypes")
	public HashMap<String, Tree> BFTtable = new HashMap<String, Tree>();
	public HashMap<String, EquvNode> EQtable = new HashMap<String, EquvNode>();

	@SuppressWarnings("rawtypes")
	abstract public String GenRegex(HashMap<String, SymbolEntry> SMtable,
			HashMap<String, Tree> BFTtable, String TrackingReg);

	@SuppressWarnings("rawtypes")
	abstract public void ExtractingExpr(InstanceInvokeExpr iie,
			HashMap<String, SymbolEntry> SMtable,
			HashMap<String, Tree> BFTtable, Unit ut)
			throws NodeNotFoundException;
	
	abstract public void GetEquivalenttb(HashMap<String, Tree> BFTtable, HashMap<String, EquvNode> EQtable, String TrackingReg) throws NodeNotFoundException;

	@SuppressWarnings({ "unused", "unchecked" })
	public void ExtractingSignature(String filepath) throws java.lang.Exception {
		// InputStream is = new
		// FileInputStream("/Users/appff/Documents/workspace/soot/sootOutput/com.example.android.snake.TileView.jimple");
		// InputStream is = new
		// FileInputStream("/Users/appff/Documents/workspace/soot/sootOutput/test.jimple");
		InputStream is = new FileInputStream(filepath);
		JimpleAST jast = new JimpleAST(is);
		SootClass sc = jast.createSootClass();
		SootMethod sm = sc.getMethods().iterator().next();
		Body body = jast.getBody(sm);
		Chain<Local> locals = body.getLocals();
		Value retArg = null;

		// Init Symboltable
		for (Local l : locals) {
			SymbolEntry se = new SymbolEntry();
			se.setType(l.getType().toString());
			SMtable.put(l.toString(), se);

			if (l.getType().toString().indexOf("JSON") != -1) {
				BFTtable.put(l.toString(), new ArrayListTree<BFNode>());
				BFTtable.get(l.toString()).add(new BFNode());
			}
		}
		for (Unit ut : body.getUnits()) {
			List<ValueBox> lvb = ut.getUseBoxes();

			if (ut instanceof AssignStmt) {
				AssignStmt as = (AssignStmt) ut;
				// System.out.println("Assign:  " + as);

				if (as.containsInvokeExpr()) {
					if (as.getInvokeExpr() instanceof SpecialInvokeExpr) {
						SpecialInvokeExpr sie = (SpecialInvokeExpr) as
								.getInvokeExpr();
						// System.out.println("SpecialInvokeExpr : " + sie);
						ExtractingExpr(sie, SMtable, BFTtable, ut);
					} else if (as.getInvokeExpr() instanceof VirtualInvokeExpr) {
						VirtualInvokeExpr vie = (VirtualInvokeExpr) as
								.getInvokeExpr();
						// System.out.println("VirtualInvokeExpr : " + vie);
						ExtractingExpr(vie, SMtable, BFTtable, ut);
					}
				} else {
					Tree ropTree = BFTtable.get(as.getRightOp().toString());

					if (ropTree != null) {
						Tree lopTree = BFTtable.get(as.getLeftOp().toString());

						BFTtable.put(as.getLeftOp().toString(), ropTree);
						// System.out.println("Assign tree : " +
						// as.getRightOp().toString());
					}
				}
			} else if (ut instanceof InvokeStmt) {
				InvokeStmt ivs = (InvokeStmt) ut;
				if (ivs.getInvokeExpr() instanceof SpecialInvokeExpr) {
					SpecialInvokeExpr sie = (SpecialInvokeExpr) ivs
							.getInvokeExpr();
					// System.out.println("SpecialInvokeExpr in the InvokeStmt : "
					// + sie);
					ExtractingExpr(sie, SMtable, BFTtable, ut);
				} else if (ivs.getInvokeExpr() instanceof VirtualInvokeExpr) {
					VirtualInvokeExpr vie = (VirtualInvokeExpr) ivs
							.getInvokeExpr();
					// System.out.println("VirtualInvokeExpr in the InvokeStmt : "
					// + vie);
					ExtractingExpr(vie, SMtable, BFTtable, ut);
				}
			}
		}
		
		GetEquivalenttb(BFTtable, EQtable, TrackingReg);
	}
}
