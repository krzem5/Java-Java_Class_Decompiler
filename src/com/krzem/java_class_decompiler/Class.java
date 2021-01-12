package com.krzem.java_class_decompiler;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;



public class Class{
	public static final List<String> VALID_CLASS_MODIF=Arrays.asList("public","final","abstract");
	public static final List<String> VALID_FIELD_MODIF=Arrays.asList("public","private","protected","static","final","volatile","transient");
	public static final List<String> VALID_METHOD_MODIF=Arrays.asList("public","private","protected","static","final","volatile","transient");
	private String nm;
	private List<String> ifl;
	private String sc;
	private List<Object[]> fl;
	private List<Object[]> ml;
	private String[] af_l;
	private List<String> il=new ArrayList<String>();



	public Class(String nm,String sc,String[] af_l,Object[] fl,Object[] ml,Map<String,Object> al,Object[] cp,int[] cpt){
		this.nm=nm;
		this.ifl=new ArrayList<String>();
		this.sc=sc;
		this.fl=this._fields(fl);
		this.ml=this._methods(ml);
		this.af_l=af_l;
		System.out.println(al.get("InnerClasses"));
	}



	public void add_interface(String _if){
		this.ifl.add(_if);
	}



	public void write(List<String> ll,List<String> il,int i){
		String afs="";
		for (String af:this.af_l){
			if (VALID_CLASS_MODIF.contains(af)){
				afs+=" "+af;
			}
		}
		String ifs="";
		for (String _if:this.ifl){
			ifs+=" "+this._class_name(_if);
			this.il.add(_if);
		}
		if (this.sc!=null){
			this.il.add(this.sc);
		}
		ll.add(String.format("%s%s class %s%s%s{",this._indent(i),(afs.length()==0?"":afs.substring(1)),this.nm,(this.sc.length()==0?"":" extends "+this._class_name(this.sc)),(ifs.length()==0?"":" implements "+ifs.substring(1))));
		for (Object[] f:this.fl){
			afs="";
			for (String af:(List<String>)((Object[])f)[2]){
				if (VALID_FIELD_MODIF.contains(af)){
					afs+=" "+af;
				}
			}
			ll.add(String.format("%s%s %s %s;",this._indent(i+1),(afs.length()==0?"":afs.substring(1)),this._decompile_type((String)((Object[])f)[1]),(String)((Object[])f)[0]));
		}
		if (this.fl.size()>0){
			ll.add("\n\n");
		}
		for (Object[] m:this.ml){
			if (((String)((Object[])m)[0]).equals("<clinit>")){
				continue;
			}
			afs="";
			boolean va=false;
			for (String af:(List<String>)((Object[])m)[2]){
				if (VALID_METHOD_MODIF.contains(af)){
					afs+=" "+af;
				}
				if (af.equals("varargs")){
					va=true;
				}
			}
			if (((String)((Object[])m)[0]).equals("<init>")){
				ll.add(String.format("%s%s %s(%s){\n",this._indent(i+1),(afs.length()==0?"":afs.substring(1)),this.nm,this._decompile_method_params((String)((Object[])m)[1],va)));
			}
			else{
				ll.add(String.format("%s%s %s %s(%s){\n",this._indent(i+1),(afs.length()==0?"":afs.substring(1)),this._decompile_method_return((String)((Object[])m)[1]),(String)((Object[])m)[0],this._decompile_method_params((String)((Object[])m)[1],va)));
			}
			ll.add(String.format("%s}\n\n\n",this._indent(i+1)));
		}
		il.addAll(this.il);
	}



	private String _indent(int i){
		String o="";
		for (int j=0;j<i;j++){
			o+="\t";
		}
		return o;
	}



	private String _class_name(String o){
		return o.split("/")[o.split("/").length-1];
	}



	private List<Object[]> _fields(Object[] _fl){
		List<Object[]> fl=new ArrayList<Object[]>();
		for (Object f:_fl){
			fl.add((Object[])f);
		}
		return fl;
	}



	private List<Object[]> _methods(Object[] _ml){
		List<Object[]> ml=new ArrayList<Object[]>();
		for (Object m:_ml){
			ml.add((Object[])m);
		}
		return ml;
	}



	private String _decompile_type(String t){
		String a="";
		int i=0;
		while (t.charAt(i)=='['){
			a+="[]";
			i++;
		}
		switch (t.charAt(i)){
			case 'B':
				return "byte"+a;
			case 'C':
				return "char"+a;
			case 'D':
				return "double"+a;
			case 'F':
				return "float"+a;
			case 'I':
				return "int"+a;
			case 'J':
				return "long"+a;
			case 'L':
				this.il.add(t.substring(i+1,t.length()-1));
				return this._class_name(t.substring(i+1,t.length()-1))+a;
			case 'S':
				return "short"+a;
			case 'Z':
				return "boolean"+a;
			case 'V':
				return "void";
		}
		return null;
	}



	private String _decompile_method_return(String t){
		return this._decompile_type(t.substring(t.lastIndexOf(")")+1));
	}



	private String _decompile_method_params(String t,boolean va){
		String o="";
		t=t.substring(1,t.lastIndexOf(")"));
		int i=0;
		while (i<t.length()){
			String a="";
			while (t.charAt(i)=='['){
				a+="[]";
				i++;
			}
			switch (t.charAt(i)){
				case 'B':
					o+=",byte"+a;
					break;
				case 'C':
					o+=",char"+a;
					break;
				case 'D':
					o+=",double"+a;
					break;
				case 'F':
					o+=",float"+a;
					break;
				case 'I':
					o+=",int"+a;
					break;
				case 'J':
					o+=",long"+a;
					break;
				case 'L':
					String or=t.substring(i+1);
					or=or.substring(0,or.indexOf(";"));
					this.il.add(or);
					o+=","+this._class_name(or)+a;
					i+=or.length();
					break;
				case 'S':
					o+=",short"+a;
					break;
				case 'Z':
					o+=",boolean"+a;
					break;
			}
			i++;
		}
		return (o.length()==0?"":(va==true?o.substring(1,o.length()-2)+"...":o.substring(1)));
	}
}