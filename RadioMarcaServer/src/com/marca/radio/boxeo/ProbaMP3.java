package com.marca.radio.boxeo;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

public class ProbaMP3 {

	public static void main(String[] args) throws Exception {
		//System.out.println("HOla1");
		
		//String ruta = "C:\\Program Files (x86)\\Android\\android-sdk\\samples\\android-19\\legacy\\ApiDemos\\res\\raw\\test_cbr.mp3";
		//String ruta = "C:\\ProgramData\\TechSmith\\Camtasia Studio 8\\Sample_Projects\\Getting Started\\getting-started-project.mp3";
		String ruta = "C:\\Users\\Vale\\Desktop\\pgmmarca2.mp3";
		
		//C:\Users\Vale\Desktop
		
		InputStream is = new FileInputStream(ruta);
		
		BufferedInputStream bs = new BufferedInputStream(is);
		
		
		int byteleido;
		int conta = 1;
		
		while (((byteleido=bs.read())!=-1)&&(conta<5))
		{
			System.out.println(conta + " " +byteleido);
			conta++;
		}
		
		is.close();
		

		System.out.println("------------------------------------------");
		
		BufferedReader br = new BufferedReader(new FileReader(ruta));
		
		conta = 0;
		while (((byteleido=br.read())!=-1)&&(conta<100))
		{
			System.out.println(conta + " " +(char)byteleido);
			conta++;
		}
		
		br.close();
	}

}
