package com.oloid_design.jlink;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TestJLinkAPI {

	public TestJLinkAPI() {
	}
	
	public static void main(String[] args) {
		
		if (testEnvironment()) {
			// Info über den erfolgreichen Durchlauf des Programmes
		} else {
			// Info über den fehlgeschlagenen Durchlauf des Programmes
		}
		
	}
	
	
	// Test J-Link environment
	public static boolean testEnvironment() {
		StringBuilder sb;
		Process p;
		String pLine, creoLoadpoint="", datecode="", architecture="";
		int nCreoSessions;
		Pattern pattern;
		Matcher m;
		boolean returnValue, pcmeFound, xtopFound;
		
		
		sb = new StringBuilder();
		nCreoSessions = 0;
		returnValue = true;
		
		
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
					
					System.out.println("Creo loadpoint: " + creoLoadpoint);
					System.out.println("Weekly version: " + datecode);
					System.out.println("Found: " + architecture);

				}
			}
			
			in.close();
		} catch (IOException e) {
			System.err.println(e);
		}

		if (creoLoadpoint.isEmpty()) {
			returnValue=false;
			System.err.println("xtop.exe (Creo) is NOT running!");
		}
		
		
		// Number of creo sessions found
		sb.append("# creo sessions: " + nCreoSessions + " (should be 1)\n");
		if (nCreoSessions == 0)
			returnValue = false;

		
		// Test java version
		System.out.println("Java version: " + System.getProperty("java.runtime.version") + " " + System.getProperty("sun.arch.data.model") + "-bit");

		
		// J-Link API installed
		if (!creoLoadpoint.isEmpty()) {
			File jLinkAPI = new File(creoLoadpoint+"\\Common Files\\"+datecode+"\\text\\java\\pfc.jar");
			if (jLinkAPI.exists()) {
				if (jLinkAPI.canRead()) {
					System.out.println("Found and readable: " + jLinkAPI.getAbsolutePath());
				} else {
					System.out.println("Found but NOT readable: " + jLinkAPI.getAbsolutePath());
					returnValue = false;
				}
			} else {
				System.out.println("NOT found: " + jLinkAPI.getAbsolutePath());
				returnValue = false;
			}
		}
		
		
		// Test PRO_COMM_MSG_EXE
		System.out.println("PRO_COMM_MSG_EXE=" + System.getenv("PRO_COMM_MSG_EXE"));
		
		
		// Find running pro_comm_msg.exe
		pattern = Pattern.compile("^pro_comm_msg.exe.*\"(.*)\\\\pro_comm_msg.exe\".*");
		
		pcmeFound = false;
		try {
			p = Runtime.getRuntime().exec("wmic process");
			
			BufferedReader in = new BufferedReader( new InputStreamReader(p.getInputStream()) );
			
			while ((pLine = in.readLine()) != null) {
				m = pattern.matcher(pLine);
				if (m.matches()) {
					System.out.println("pro_comm_msg.exe is running: " + m.group(1));
					pcmeFound = true;
				}
			}
			
			in.close();
		} catch (IOException e) {
			System.err.println(e);
		}
		
		if (!pcmeFound) {
			System.err.println("pro_comm_msg.exe is NOT running!");
			returnValue=false;
		}
		
		return returnValue;
		
	}
		
}

