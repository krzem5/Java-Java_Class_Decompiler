package com.krzem.java_class_decompiler;



import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;



public class Main{
	public static void main(String[] args){
		new Main(args);
	}



	public Main(String[] args){
		List<String> dcl=new ArrayList<String>();
		String base=System.getProperty("user.dir")+"\\";
		for (int i=0;i<args.length;i++){
			if (args[i].equals("--base-dir")||args[i].equals("-bd")){
				base=Paths.get(base+args[i+1]).normalize().toFile().getAbsolutePath()+"\\";
				i++;
			}
			else{
				dcl.add(args[i]);
			}
		}
		for (String p:dcl){
			new Decompiler(p,base);
		}
	}
}