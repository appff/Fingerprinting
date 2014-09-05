//package SignExtractor;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//
//import SignExtractor.JSONBuilder.MethodIds;
//import soot.Local;
//import soot.Unit;
//import soot.Value;
//import soot.jimple.InstanceInvokeExpr;
//import soot.util.Chain;
//
//
//public class XMLBuilder extends BufferExtractor {
//    
//    	int blanklevel = 0;
//    	String blank = " ";
//
//	public class MethodIds {
//		public static final int init = 0;
//		public static final int setOutput = 1;
//		public static final int valueOf = 2;
//		public static final int startDocument = 3;
//		public static final int setFeature = 4;
//		public static final int startTag = 5;
//		public static final int attribute = 6;
//		public static final int text = 7;
//		public static final int endTag = 8;
//		public static final int endDocument = 9;
//		public static final int toString = 10;
//	}
//	
//	public static void main (String args[]) throws Exception
//	{
//		XMLBuilder xb = new XMLBuilder();
//		xb.methodlist = Arrays.asList("<init>", "setOutput", "valueOf", "startDocument", "setFeature", "startTag", "attribute", "text", "endTag", "endDocument", "toString");
//		//xb.ExtractingSignature("/Users/appff/Documents/workspace/soot/sootOutput/test_XML.jimple");
//		xb.ExtractingSignature("/Users/appff/Documents/workspace/soot/sootOutput/chat.jimple");
//		
//		
//		EntryIndexer ei = xb.hmBuffer.get("$r1");
//		for ( BufferEntry be : ei.beList )
//		{
//		    System.out.print(be.arg1);
//		}
//	}
//	
//	
//	@Override
//	public String GenRegex(HashMap<String, EntryIndexer> hm, String TrackingReg) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//    @Override
//    public void ExtractingExpr(InstanceInvokeExpr iie,
//	    HashMap<String, EntryIndexer> hm, Chain<Local> lc, Value dst) {
//
//	/* Local variables */
//	String strMethod = iie.getMethodRef().name();
//	List<Value> args = new ArrayList<Value>();
//	String strDest = iie.getBase().toString();
//	EntryIndexer temparg = null;
//	EntryIndexer eiDst = null;
//	Value larg = null;
//	Value rarg = null;
//	BufferEntry newbe;
//	
//	// Check method
//	int chkMethod = methodlist.indexOf(strMethod);
//	
//	//System.out.println(strMethod);
//
//	args = iie.getArgs();
//	
//	/* Buffer Tracking */
//	switch (chkMethod) {
//	case MethodIds.init:
//	    	if (args.isEmpty())
//	    	    break;
//	    break;
//	case MethodIds.attribute:
//	    	larg = args.get(1);
//	    	rarg = args.get(2);
//	    	
//	    	eiDst = hm.get(strDest);
//	    	BufferEntry be = eiDst.beList.get(eiDst.currentidx-1);
//	    	
//	    	if ( be.arg1.lastIndexOf(">") != -1 )
//	    	{
//	    	    be.arg1 = be.arg1.substring(0, be.arg1.length()-8);
//	    	    be.arg1 += " " + larg.toString().replaceAll("\"", "") + "=" + rarg.toString() + ">\\n(\\s)*";
//	    	    //System.out.println(be.arg1);
//	    	}
//	    	
//	    	//System.out.println("Attribute : " + be.arg1.toString());
//	    	hm.put(strDest, eiDst);
//	    break;
//	case MethodIds.endDocument:
//	    break;
//	case MethodIds.endTag:
//	    rarg = args.get(1);
//
//	    eiDst = hm.get(strDest);
//	    newbe = new BufferEntry();
//	    newbe.arg1 = "";
//
//	    newbe.arg1 += "<\\/" + rarg.toString().replaceAll("\"", "") + ">(\\n)*(\\s)*";
//	    newbe.argtype1 = "String";
//	    newbe.betype = BeTypes.Const;
//
//	    eiDst.beList.add(newbe);
//	    eiDst.currentidx++;
//	    hm.put(strDest, eiDst);
//	    break;
//	case MethodIds.setFeature:
//	    break;
//	case MethodIds.setOutput:
//	    break;
//	case MethodIds.startDocument:
//	    	larg = args.get(0);
//	    	
//	    	eiDst = hm.get(strDest);
//	    	newbe = new BufferEntry();
//	    	
//	    	newbe.arg1 = "^(<\\?)xml version='1\\.0' encoding='" + larg.toString().replaceAll("\"", "") + "' standalone='yes' \\?>\\n";
//	    	newbe.argtype1 = larg.getType().toString();
//	    	newbe.betype = BeTypes.Const;
//	    	
//	    	eiDst.beList.add(newbe);
//	    	eiDst.currentidx++;
//	    	hm.put(strDest, eiDst);
//	    break;
//	case MethodIds.startTag:
//	    	rarg = args.get(1);
//	    	
//	    	eiDst = hm.get(strDest);
//	    	newbe = new BufferEntry();	    	
//	    	newbe.arg1 = "";
//	    	
//	    	newbe.arg1 += "<" + rarg.toString().replaceAll("\"", "") + ">\\n(\\s)*";
//	    	newbe.argtype1 = "String";
//	    	newbe.betype = BeTypes.Const;
//	    	
//	    	eiDst.beList.add(newbe);
//	    	eiDst.currentidx++;
//	    	hm.put(strDest, eiDst);
//	    break;
//	case MethodIds.text:
//	    	larg = args.get(0);
//	    	
//	    	eiDst = hm.get(strDest);
//	    	
//	    	//Last Entry \r\n must be remove
//	    	newbe = eiDst.beList.get(eiDst.currentidx-1);
//	    	
//	    	if ( newbe.arg1.lastIndexOf(">") != -1 )
//	    	{
//	    	    newbe.arg1 = newbe.arg1.substring(0, newbe.arg1.length()-7);
//	    	}
//	    	
//	    	newbe = new BufferEntry();
//	    	newbe.arg1 = "";
//	    	
//	    	newbe.arg1 += larg.toString().replaceAll("\"", "");
//	    	newbe.argtype1 = larg.getType().toString();
//	    	newbe.betype = BeTypes.Const;
//	    	
//	    	eiDst.beList.add(newbe);
//	    	eiDst.currentidx++;
//	    	hm.put(strDest, eiDst);
//	    break;
//	case MethodIds.toString:
//	    break;
//	case MethodIds.valueOf:
//	    break;
//	default:
//	    break;
//	}
//    }
//    private void addBlank(BufferEntry be)
//    {
//    	for ( int j=0 ; j < blanklevel; j++ )
//    	    be.arg1 += blank;
//    }
//}
