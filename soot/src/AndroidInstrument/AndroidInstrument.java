package AndroidInstrument;


import java.util.Iterator;
import java.util.Map;

import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.PackManager;
import soot.PatchingChain;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.Transform;
import soot.Unit;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.options.Options;


public class AndroidInstrument {
     public static void main(String[] args)
     {

          //prefer Android APK files// -src-prec apk
          Options.v().set_src_prec(Options.src_prec_apk);
         
          //output as APK, too//-f J
          Options.v().set_output_format(Options.output_format_jimple);
         
        // resolve the PrintStream and System soot-classes
          Scene.v().addBasicClass("java.io.PrintStream",SootClass.SIGNATURES);
        Scene.v().addBasicClass("java.lang.System",SootClass.SIGNATURES);

        PackManager.v().getPack("jtp").add(new Transform("jtp.myInstrumenter", new BodyTransformer() {

               @Override
               protected void internalTransform(final Body b, String phaseName, @SuppressWarnings("rawtypes") Map options) {
                    final PatchingChain<Unit> units = b.getUnits();
                   
                    //important to use snapshotIterator here
                    for(Iterator<Unit> iter = units.snapshotIterator(); iter.hasNext();) {
                         final Unit u = iter.next();
                         u.apply(new AbstractStmtSwitch() {
                             
                              public void caseInvokeStmt(InvokeStmt stmt) {
//                                   InvokeExpr invokeExpr = stmt.getInvokeExpr();
//                                  
//                                   //check this expr for each statement
//                                   if(invokeExpr.getMethod().getName().equals("onDraw")) {
//
//                                        Local tmpRef = addTmpRef(b);
//                                        Local tmpString = addTmpString(b);
//                                       
//                                          // insert "tmpRef = java.lang.System.out;"
//                                      units.insertBefore(Jimple.v().newAssignStmt(
//                                                    tmpRef, Jimple.v().newStaticFieldRef(
//                                                    Scene.v().getField("<java.lang.System: java.io.PrintStream out>").makeRef())), u);
//
//                                     
//                                     
//                                      //this causes Soot to insert a System.out.println("HELLO") just before
//                                      //the method invocation but only if the target of this invocation is an onDraw method.
//                                     
//                                     
//                                      // insert "tmpLong = 'HELLO';"
//                                      units.insertBefore(Jimple.v().newAssignStmt(tmpString,
//                                                    StringConstant.v("HELLO")), u);
//                                     
//                                      // insert "tmpRef.println(tmpString);"
//                                      SootMethod toCall = Scene.v().getSootClass("java.io.PrintStream").getMethod("void println(java.lang.String)");                   
//                                      units.insertBefore(Jimple.v().newInvokeStmt(
//                                                    Jimple.v().newVirtualInvokeExpr(tmpRef, toCall.makeRef(), tmpString)), u);
//                                     
//                                      //check that we did not mess up the Jimple
//                                      b.validate();
//                                   }
                              }
                             
                         });
                    }
               }


          }));
         
          soot.Main.main(args);
     }
    
    private static Local addTmpRef(Body body)
    {
        Local tmpRef = Jimple.v().newLocal("tmpRef", RefType.v("java.io.PrintStream"));
        body.getLocals().add(tmpRef);
        return tmpRef;
    }
   
    private static Local addTmpString(Body body)
    {
        Local tmpString = Jimple.v().newLocal("tmpString", RefType.v("java.lang.String"));
        body.getLocals().add(tmpString);
        return tmpString;
    }
}
