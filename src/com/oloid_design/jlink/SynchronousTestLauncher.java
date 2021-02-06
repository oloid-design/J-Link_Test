package com.oloid_design.jlink;
 
import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcCommand.UICommand;
import com.ptc.pfc.pfcGlobal.pfcGlobal;
import com.ptc.pfc.pfcSession.Session;
 

public class SynchronousTestLauncher {
	public static String MSG_FILE = "msg_fks83h6k.txt";
 
	// Start Methode wie in der Datei portk.dat eingetrage (start)
	public static void start() throws jxthrowable {
		Session session = pfcGlobal.GetProESession();
		
    	// Neuer Befehl bei Creo Parametrics registrieren		
		UICommand command = session.UICreateCommand("Programmname", new SynchronousTest());
		
    	// Bild für die Menüleisten definieren (SynchronousTest.png 16x16 und SynchronousTest_large.png 32x32)
		command.SetIcon("SynchronousTest.png");
    	
		// Bildbeschriftung aus der Nachrichtendatei holen (Schlüsselwort SynchronousTest.label)
		command.Designate(MSG_FILE, "SynchronousTest.label", null, null);
	    
	}
 
	
	// Stop Methode wie in der Datei portk.dat eingetrage (stop)
	public static void stop() {
	}
	
}

