package com.subway.action;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class Temp {

	public static void main(String[] args) throws IOException{
		
		StringBuffer buffer = new StringBuffer();
		FileReader fr = new FileReader(new File("data.txt"));
		fr.read();
		
		
		
		/*
		FileInputStream fis = new FileInputStream(new File("data.txt"));
		
		
		byte[] b = new byte[1024];
		int len = fis.read(b);
		fis.
		while(len != -1){
			buffer.append(new String(b,0,len));
			len = fis.read(b);
		}
		buffer.append(new String(b));
		
		System.out.println(buffer);
		
		FileOutputStream fos = new FileOutputStream("data2.txt");
		fos.write(buffer.toString().getBytes());
		*/
	}
	
}
