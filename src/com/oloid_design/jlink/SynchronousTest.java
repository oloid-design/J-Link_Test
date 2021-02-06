package com.oloid_design.jlink;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcCommand.DefaultUICommandActionListener;
import com.ptc.pfc.pfcGlobal.pfcGlobal;
import com.ptc.pfc.pfcSession.Session;

public class SynchronousTest extends DefaultUICommandActionListener {

	public SynchronousTest() {
	}
	
	@Override
	public void OnCommand() throws jxthrowable {
		Session session;
		JFrame jFrame;
		
		// Aktuelle Creo Parametrics Sitzung holen.
		session = pfcGlobal.GetProESession();
		
		//
		// do something with the session
		//
		
		
		// Der JFrame ist nur dafür da, dass das Nachrichtenfester auch sicher im Vordergrung erscheint.
		jFrame = new JFrame();
		jFrame.setAlwaysOnTop(true);
		
		if (testEnvironment()) {
			// Info über den erfolgreichen Durchlauf des Programmes
			JOptionPane.showMessageDialog(jFrame, "Synchronous Connection successfull. http://www.oloid-design.com");			
		} else {
			// Info über den fehlgeschlagenen Durchlauf des Programmes
			JOptionPane.showMessageDialog(jFrame, "Synchronous Connection NOT successfull. http://www.oloid-design.com");
		}
		
	}
	
	
	// Test J-Link environment
	public boolean testEnvironment() {
		JFrame jFrame;
		StringBuilder sb;
		Process p;
		String pLine, creoLoadpoint="", datecode="", architecture="";
		int nCreoSessions;
		Pattern pattern;
		Matcher m;
		boolean returnValue, pcmeFound;
		
		
		sb = new StringBuilder();
		nCreoSessions = 0;
		returnValue = true;
		
		
		// Der JFrame ist nur dafür da, dass das Nachrichtenfester auch sicher im Vordergrung erscheint.
		jFrame = new JFrame();
		jFrame.setAlwaysOnTop(true);
		
		
		// Find running xtop.exe
		pattern = Pattern.compile("^xtop.exe.*\"(.*)\\\\Common Files\\\\(.*)\\\\(.*)\\\\obj\\\\xtop.exe\".*");
		
		try {
			p = Runtime.getRuntime().exec("wmic process");
			
			BufferedReader in = new BufferedReader( new InputStreamReader(p.getInputStream()) );
			
			while ((pLine = in.readLine()) != null) {
				m = pattern.matcher(pLine);
				if (m.matches()) {
					nCreoSessions++;
					
					creoLoadpoint = m.group(1);
					datecode = m.group(2);
					architecture = m.group(3);
					
					sb.append("Creo loadpoint: " + creoLoadpoint + "\n");
					sb.append("Weekly version: " + datecode + "\n");
					sb.append("Found: " + architecture + "\n");
				}
			}
			
			in.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		// Number of creo sessions found
		sb.append("# creo sessions: " + nCreoSessions + " (should be 1)\n");
		if (nCreoSessions == 0)
			returnValue = false;

		
		// Test java version
		sb.append("Java version: " + System.getProperty("java.runtime.version") + " " + System.getProperty("sun.arch.data.model") + "-bit\n");

		
		// J-Link API installed
		
		File jLinkAPI = new File(creoLoadpoint+"\\Common Files\\"+datecode+"\\text\\java\\pfc.jar");
		if (jLinkAPI.exists()) {
			if (jLinkAPI.canRead()) {
				sb.append("Found and readable: " + jLinkAPI.getAbsolutePath() + "\n");
			} else {
				sb.append("Found but NOT readable: " + jLinkAPI.getAbsolutePath() + "\n");
				returnValue = false;
			}
		} else {
			sb.append("NOT found: " + jLinkAPI.getAbsolutePath() + "\n");
			returnValue = false;
		}
		
		
		// Test PRO_COMM_MSG_EXE
		sb.append("PRO_COMM_MSG_EXE=" + System.getenv("PRO_COMM_MSG_EXE") + "\n");
		
		
		// Find running pro_comm_msg.exe
		pattern = Pattern.compile("^pro_comm_msg.exe.*\"(.*)\\\\pro_comm_msg.exe\".*");
		
		pcmeFound = false;
		try {
			p = Runtime.getRuntime().exec("wmic process");
			
			BufferedReader in = new BufferedReader( new InputStreamReader(p.getInputStream()) );
			
			while ((pLine = in.readLine()) != null) {
				m = pattern.matcher(pLine);
				if (m.matches()) {
					sb.append("pro_comm_msg.exe is running: " + m.group(1) + "\n");
					pcmeFound = true;
				}
			}
			
			in.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (!pcmeFound)
			returnValue=false;
		
		
		// Text als Fenstermeldung ausgeben
		JOptionPane.showMessageDialog(jFrame, sb.toString());
		
		return returnValue;
		
		
	}
		
}

