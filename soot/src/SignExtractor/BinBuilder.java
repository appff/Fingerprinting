//package SignExtractor;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//
//import SignExtractor.BufferExtractor.BeTypes;
//import soot.Local;
//import soot.Value;
//import soot.jimple.InstanceInvokeExpr;
//import soot.util.Chain;
//
//public class BinBuilder extends BufferExtractor {
//
//    public class MethodIds {
//	public static final int init = 0;
//	public static final int getBytes = 1;
//	public static final int write = 2;
//	public static final int toByteArray = 3;
//	public static final int send = 4;
//	public static final int getType = 5;
//	public static final int getSender = 6;
//	public static final int getMessage = 7;
//	public static final int getChatroom = 8;
//	public static final int getAddress = 9;
//	public static final int getSerializable = 10;
//	public static final int getPort = 11;
//    }
//
//    public static void main(String args[]) throws Exception {
//
//	BinBuilder bb = new BinBuilder();
//	bb.methodlist = Arrays.asList("<init>", "getBytes", "write",
//		"toByteArray", "send", "getType", "getSender", "getMessage",
//		"getChatroom", "getAddress", "getSerializable", "getPort");
//	bb.ExtractingSignature("/Users/appff/Documents/workspace/soot/sootOutput/chat.jimple");
//
////	 EntryIndexer ei = bb.hmBuffer.get("$r6");
////	 System.out.print("intermediate representation : ");
////	 for ( BufferEntry be : ei.beList )
////	 {
////	     System.out.print(be.arg1);
////	 }
////	System.out.println("");
//	 
//	System.out.print("REGEX representation : \n" + "((x[6-7]{1}[0-9A-F]{1})+(x7C){1}){2}(x[2-7]{1}[0-9A-F]{1})+(x7C){1}((x[6-7]{1}[0-9A-F]{1})+(x7C){1}){1}((x0A{1})+(x7C){1}){1}((x[0-9A-F]{2,})+(x7C){1}){1}");
//
//    }
//
//    @Override
//    public String GenRegex(HashMap<String, EntryIndexer> hm, String TrackingReg) {
//	// TODO Auto-generated method stub
//	return null;
//    }
//
//    @Override
//    public void ExtractingExpr(InstanceInvokeExpr iie,
//	    HashMap<String, EntryIndexer> hm, Chain<Local> lc, Value dst) {
//	// TODO Auto-generated method stub
//
//	/* Local variables */
//	String strMethod = iie.getMethodRef().name();
//	List<Value> args = new ArrayList<Value>();
//	String strDest = iie.getBase().toString();
//	EntryIndexer temparg = null;
//	EntryIndexer eiDst = null;
//	Value larg = null;
//	Value rarg = null;
//	BufferEntry be;
//
//	// Check method
//	int chkMethod = methodlist.indexOf(strMethod);
//	args = iie.getArgs();
//
//	/* Buffer Tracking */
//	switch (chkMethod) {
//	case MethodIds.getPort:
//	    eiDst = hm.get(dst.toString());
//	    
//	    be = new BufferEntry();
//
//	    be.arg1 = "*Port";
//	    be.argtype1 = iie.getMethodRef().returnType().toString();
//	    be.betype = BeTypes.Const;
//	    eiDst.beList.clear();
//	    eiDst.beList.add(be);
//	    
//	    //System.out.println(dst.toString());
//	    
//	    hm.put(dst.toString(), eiDst);
//	    break;
//	case MethodIds.getSerializable:
//	    larg = args.get(0);
//
//	    eiDst = hm.get(strDest);
//	    be = new BufferEntry();
//
//	    be.arg1 = larg.toString();
//	    be.argtype1 = larg.getType().toString();
//	    be.betype = BeTypes.Const;
//	    eiDst.beList.clear();
//	    eiDst.beList.add(be);
//	    hm.put(strDest, eiDst);
//	    break;
//	case MethodIds.init:
//	    break;
//	case MethodIds.getBytes:
//	    eiDst = hm.get(dst.toString());
//
//	    if (strDest.indexOf("$") != -1) {
//		if (hm.containsKey(strDest)) {
//		    temparg = hm.get(strDest);
//		}
//	    }
//	    
//	    //System.out.println("getBytes : " + strDest);
//	    
//	    if (!temparg.beList.isEmpty()) // const : variable
//	    {
//		be = temparg.beList.iterator().next();
//		
//		BufferEntry newbe = new BufferEntry();
//		
//		newbe.arg1 = be.arg1;
//		newbe.argtype1 = be.argtype1;
//		newbe.betype = BeTypes.ConstConst;
//		eiDst.beList.clear();
//		eiDst.beList.add(newbe);
//		hm.put(dst.toString(), eiDst);
//		
//		temparg.beList.clear();
//		newbe = new BufferEntry();
//		
//		newbe.arg1 = "|";
//		newbe.argtype1 = "String";
//		newbe.betype = BeTypes.ConstConst;
//		temparg.beList.clear();
//		temparg.beList.add(newbe);
//	    }
//	    break;
//	case MethodIds.send:
//	    break;
//	case MethodIds.toByteArray:
//	    break;
//	case MethodIds.write:
//
//	    if (dst.toString().indexOf("$") != -1) {
//		if (hm.containsKey(dst.toString())) {
//		    temparg = hm.get(dst.toString());
//		}
//	    }
//	    
//	    eiDst = hm.get(strDest);
//	    //System.out.println(dst.toString() + " " + strDest);
//
//	    if (!temparg.beList.isEmpty()) // const : variable
//	    {
////		be = temparg.beList.iterator().next();
////		BufferEntry newbe = new BufferEntry();
////
////		newbe.arg1 = be.arg1;
////		newbe.argtype1 = be.argtype1;
////		newbe.betype = BeTypes.ConstConst;
////
////		eiDst.beList.add(newbe);
////		eiDst.currentidx++;
//		
//		eiDst.beList.addAll(temparg.beList);
//		//System.out.println(dst.toString());
//		hm.put(strDest, eiDst);
//	    }
////
////	    System.out.println(" ");
////	    for (BufferEntry bes: eiDst.beList)
////	    {
////		System.out.print(bes.arg1 + "->");
////	    }
////	    System.out.println(" ");
//
//	    break;
//	case MethodIds.getType:
//	    eiDst = hm.get(dst.toString());
//
//	    be = new BufferEntry();
//
//	    be.arg1 = "*String";
//	    be.argtype1 = iie.getMethodRef().returnType().toString();
//	    be.betype = BeTypes.Const;
//	    eiDst.beList.clear();
//	    eiDst.beList.add(be);
//	    
//	    //System.out.println("get Type : " + dst.toString() + " " + strDest);
//	    
//	    hm.put(dst.toString(), eiDst);
//	    break;
//	case MethodIds.getSender:
//	    eiDst = hm.get(dst.toString());
//	    be = new BufferEntry();
//
//	    be.arg1 = "*String";
//	    be.argtype1 = iie.getMethodRef().returnType().toString();
//	    be.betype = BeTypes.Const;
//	    eiDst.beList.clear();
//	    eiDst.beList.add(be);
//	    
//	    //System.out.println("get sender : " + dst.toString() + " " + strDest);
//	    
//	    hm.put(dst.toString(), eiDst);
//	    break;
//	case MethodIds.getChatroom:
//	    eiDst = hm.get(dst.toString());
//	    be = new BufferEntry();
//
//	    be.arg1 = "*String";
//	    be.argtype1 = iie.getMethodRef().returnType().toString();
//	    be.betype = BeTypes.Const;
//	    eiDst.beList.clear();
//	    eiDst.beList.add(be);
//	    hm.put(dst.toString(), eiDst);
//	    break;
//	case MethodIds.getMessage:
//	    eiDst = hm.get(dst.toString());
//	    be = new BufferEntry();
//
//	    be.arg1 = "*String";
//	    be.argtype1 = iie.getMethodRef().returnType().toString();
//	    be.betype = BeTypes.Const;
//	    eiDst.beList.clear();
//	    eiDst.beList.add(be);
//	    hm.put(dst.toString(), eiDst);
//	    break;
//	case MethodIds.getAddress:
//	    eiDst = hm.get(dst.toString());
//	    be = new BufferEntry();
//
//	    be.arg1 = "*IPaddr";
//	    be.argtype1 = iie.getMethodRef().returnType().toString();
//	    be.betype = BeTypes.Const;
//	    eiDst.beList.clear();
//	    eiDst.beList.add(be);
//	    hm.put(dst.toString(), eiDst);
//	    break;
//	}
//
//    }
//
//}
