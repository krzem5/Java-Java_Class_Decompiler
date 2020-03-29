// https://docs.oracle.com/javase/specs/jvms/se8/html/
package com.krzem.java_class_decompiler;



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.Exception;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class Decompiler{
	public static final List<String> LANG_IMPORT=Arrays.asList("java/lang/Exception","java/lang/Thread","java/lang/Runnable");
	private File f;
	private ClassFileReader cr;
	private String base;



	public Decompiler(String fp,String base){
		this.f=new File(fp);
		this.base=base;
		if (fp.endsWith(".jar")){
			this._jar();
		}
		else{
			this._decompile();
		}
	}



	private void _decompile(){
		this.cr=new ClassFileReader(this.f);
		if (this.cr.readInt()!=0xcafebabe){
			return;
		}
		int mnv=this.cr.readUShort();
		int mjv=this.cr.readUShort();
		int sz=this.cr.readUShort();
		Object[] cp=new Object[sz];
		int[] cpt=new int[sz];
		int idx=0;
		while (idx<sz-1){
			int t=this.cr.readUByte();
			cpt[idx+1]=t;
			switch (t){
				case 1:
					cp[idx+1]=this.cr.readString();
					break;
				case 3:
					cp[idx+1]=this.cr.readInt();
					break;
				case 4:
					cp[idx+1]=this.cr.readFloat();
					break;
				case 5:
					cp[idx+1]=this.cr.readLong();
					idx++;
					break;
				case 6:
					cp[idx+1]=this.cr.readDouble();
					idx++;
					break;
				case 7:
					cp[idx+1]=this.cr.readUShort();
					break;
				case 8:
					cp[idx+1]=this.cr.readUShort();
					break;
				case 9:
				case 10:
				case 11:
				case 18:
					cp[idx+1]=this.cr.readInt();
					break;
				case 12:
					cp[idx+1]=this.cr.readInt();
					break;
				case 15:
					cp[idx+1]=((int)this.cr.readByte()<<16)|this.cr.readUShort();
					break;
				case 16:
					cp[idx+1]=this.cr.readUShort();
					break;
			}
			System.out.printf("[%d > %d] => %s\n",idx+1,t,(cp[idx+1]==null?cp[idx]:cp[idx+1]).toString());
			idx++;
		}
		List<String> af_l=new ArrayList<String>();
		int cm=this.cr.readUShort();
		if (cm>=16384){
			cm%=16384;
			af_l.add("enum");
		}
		if (cm>=8192){
			cm%=8192;
			af_l.add("annotation");
		}
		if (cm>=4096){
			cm%=4096;
			af_l.add("synthetic");
		}
		if (cm>=1024){
			cm%=1024;
			af_l.add("abstract");
		}
		if (cm>=256){
			cm%=256;
			af_l.add("interface");
		}
		if (cm>=32){
			cm%=32;
			af_l.add("super");
		}
		if (cm>=16){
			cm%=16;
			af_l.add("final");
		}
		if (cm>=1){
			cm%=1;
			af_l.add("public");
		}
		af_l=this._reverse(af_l);
		int ths_idx=this.cr.readUShort();
		int sup_idx=this.cr.readUShort();
		sz=this.cr.readUShort();
		String[] il=new String[sz];
		for (int i=0;i<sz;i++){
			il[i]=(String)cp[(int)cp[this.cr.readUShort()]];
		}
		sz=this.cr.readUShort();
		Object[][] fl=new Object[sz][4];
		for (int i=0;i<sz;i++){
			List<String> ff_l=new ArrayList<String>();
			int fm=this.cr.readUShort();
			if (fm>=16384){
				fm%=16384;
				ff_l.add("enum");
			}
			if (fm>=4096){
				fm%=4096;
				ff_l.add("synthetic");
			}
			if (fm>=128){
				fm%=128;
				ff_l.add("transient");
			}
			if (fm>=64){
				fm%=64;
				ff_l.add("volatile");
			}
			if (fm>=16){
				fm%=16;
				ff_l.add("final");
			}
			if (fm>=8){
				fm%=8;
				ff_l.add("static");
			}
			if (fm>=4){
				fm%=4;
				ff_l.add("protected");
			}
			if (fm>=2){
				fm%=2;
				ff_l.add("private");
			}
			if (fm>=1){
				fm%=1;
				ff_l.add("public");
			}
			fl[i]=new Object[]{cp[this.cr.readUShort()],cp[this.cr.readUShort()],this._reverse(ff_l),this._attrib(cp)};
		}
		sz=this.cr.readUShort();
		Object[][] ml=new Object[sz][4];
		for (int i=0;i<sz;i++){
			List<String> mf_l=new ArrayList<String>();
			int mm=this.cr.readUShort();
			if (mm>=4096){
				mm%=4096;
				mf_l.add("synthetic");
			}
			if (mm>=2048){
				mm%=2048;
				mf_l.add("strict");
			}
			if (mm>=1024){
				mm%=1024;
				mf_l.add("abstract");
			}
			if (mm>=256){
				mm%=256;
				mf_l.add("native");
			}
			if (mm>=128){
				mm%=128;
				mf_l.add("varargs");
			}
			if (mm>=64){
				mm%=64;
				mf_l.add("bridge");
			}
			if (mm>=32){
				mm%=32;
				mf_l.add("synchronised");
			}
			if (mm>=16){
				mm%=16;
				mf_l.add("final");
			}
			if (mm>=8){
				mm%=8;
				mf_l.add("static");
			}
			if (mm>=4){
				mm%=4;
				mf_l.add("protected");
			}
			if (mm>=2){
				mm%=2;
				mf_l.add("private");
			}
			if (mm>=1){
				mm%=1;
				mf_l.add("public");
			}
			ml[i]=new Object[]{cp[this.cr.readUShort()],cp[this.cr.readUShort()],this._reverse(mf_l),this._attrib(cp)};
		}
		Map<String,Object> ca_l=this._attrib(cp);
		this._write(cp,cpt,af_l.toArray(new String[af_l.size()]),ths_idx,sup_idx,fl,ml,ca_l);
	}



	private Map<String,Object> _attrib(Object[] cp){
		int sz=this.cr.readUShort();
		Map<String,Object> o=new HashMap<String,Object>(sz);
		for (int i=0;i<sz;i++){
			String p=(String)cp[this.cr.readUShort()];
			int l=this.cr.readInt();
			switch (p){
				case "ConstantValue":
				case "SourceFile":
				case "Signature":
					o.put(p,cp[this.cr.readUShort()]);
					break;
				case "Code":
					int max_s=this.cr.readUShort();
					int max_l=this.cr.readUShort();
					int code_sz=this.cr.readInt();
					byte[] code=this.cr.read(code_sz);
					int ex_sz=this.cr.readUShort();
					Object[][] ex_l=new Object[ex_sz][4];
					for (int j=0;j<ex_sz;j++){
						int s=this.cr.readUShort();
						int e=this.cr.readUShort();
						int h=this.cr.readUShort();
						int t=this.cr.readUShort();
						ex_l[j]=new Object[]{s,e,h,(t==0?null:(String)cp[t])};
					}
					o.put("Code",new Object[]{max_s,max_l,code,ex_l,this._attrib(cp)});
					break;
				case "StackMapTable"://
					o.put(p,new String(this.cr.read(l)));
					break;
				case "Exceptions":
					int esz=this.cr.readUShort();
					Object[] el=new Object[esz];
					for (int j=0;j<esz;j++){
						el[j]=cp[(int)cp[this.cr.readUShort()]];
					}
					o.put(p,el);
					break;
				case "InnerClasses":
					int cn=this.cr.readUShort();
					Object[][] icl=new Object[cn][4];
					for (int j=0;j<cn;j++){
						icl[j]=new Object[]{cp[this.cr.readUShort()],cp[this.cr.readUShort()],cp[this.cr.readUShort()],null};
						List<String> icaf_l=new ArrayList<String>();
						int icafm=this.cr.readUShort();
						if (icafm>=16384){
							icafm%=16384;
							icaf_l.add("enum");
						}
						if (icafm>=8192){
							icafm%=8192;
							icaf_l.add("annotation");
						}
						if (icafm>=4096){
							icafm%=4096;
							icaf_l.add("synthetic");
						}
						if (icafm>=1024){
							icafm%=1024;
							icaf_l.add("abstract");
						}
						if (icafm>=512){
							icafm%=512;
							icaf_l.add("interface");
						}
						if (icafm>=16){
							icafm%=16;
							icaf_l.add("final");
						}
						if (icafm>=8){
							icafm%=8;
							icaf_l.add("static");
						}
						if (icafm>=4){
							icafm%=4;
							icaf_l.add("protected");
						}
						if (icafm>=2){
							icafm%=2;
							icaf_l.add("private");
						}
						if (icafm>=1){
							icafm%=1;
							icaf_l.add("public");
						}
						icl[j][3]=icaf_l.toArray(new String[icaf_l.size()]);
					}
					o.put("InnerClasses",icl);
					break;
				case "EnclosingMethod":
					o.put("EnclosingMethod",new Object[]{cp[this.cr.readUShort()],cp[this.cr.readUShort()]});
					break;
				case "Synthetic":
				case "Deprecated":
					break;
				case "SourceDebugExtension":
					o.put("SourceDebugExtension",new String(this.cr.read(l)));
					break;
				case "LineNumberTable":
					int tsz=this.cr.readUShort();
					Map<Integer,Integer> lnt=new HashMap<Integer,Integer>();
					for (int j=0;j<tsz;j++){
						lnt.put(this.cr.readUShort(),this.cr.readUShort());
					}
					o.put("LineNumberTable",lnt);
					break;
				case "LocalVariableTable":
					tsz=this.cr.readUShort();
					List<Object[]> lvt=new ArrayList<Object[]>();
					for (int j=0;j<tsz;j++){
						lvt.add(new Object[]{this.cr.readUShort(),this.cr.readUShort(),(String)cp[this.cr.readUShort()],(String)cp[this.cr.readUShort()],this.cr.readUShort()});
					}
					o.put("LocalVariableTable",lvt);
					break;
				case "LocalVariableTypeTable":
					tsz=this.cr.readUShort();
					List<Object[]> lvtt=new ArrayList<Object[]>();
					for (int j=0;j<tsz;j++){
						lvtt.add(new Object[]{this.cr.readUShort(),this.cr.readUShort(),(String)cp[this.cr.readUShort()],(String)cp[this.cr.readUShort()],this.cr.readUShort()});
					}
					o.put("LocalVariableTypeTable",lvtt);
					break;
				case "RuntimeVisibleAnnotations":
					int asz=this.cr.readUShort();
					Object[] rva=new Object[asz];
					for (int j=0;j<asz;j++){
						rva[j]=this._annotation(cp);
					}
					o.put("RuntimeVisibleAnnotations",rva);
					break;
				case "RuntimeInvisibleAnnotations":
					asz=this.cr.readUShort();
					Object[] ria=new Object[asz];
					for (int j=0;j<asz;j++){
						ria[j]=this._annotation(cp);
					}
					o.put("RuntimeInvisibleAnnotations",ria);
					break;
				case "RuntimeVisibleParameterAnnotations":
					int psz=this.cr.readUByte();
					Object[][] rvpa=new Object[psz][];
					for (int j=0;j<psz;j++){
						asz=this.cr.readUShort();
						rvpa[j]=new Object[asz];
						for (int k=0;k<asz;k++){
							rvpa[j][k]=this._annotation(cp);
						}
					}
					o.put("RuntimeVisibleParameterAnnotations",rvpa);
					break;
				case "RuntimeInvisibleParameterAnnotations":
					psz=this.cr.readUByte();
					Object[][] ripa=new Object[psz][];
					for (int j=0;j<psz;j++){
						asz=this.cr.readUShort();
						ripa[j]=new Object[asz];
						for (int k=0;k<asz;k++){
							ripa[j][k]=this._annotation(cp);
						}
					}
					o.put("RuntimeInvisibleParameterAnnotations",ripa);
					break;
				case "RuntimeVisibleTypeAnnotations"://
					o.put(p,new String(this.cr.read(l)));
					break;
				case "RuntimeInvisibleTypeAnnotations"://
					o.put(p,new String(this.cr.read(l)));
					break;
				case "AnnotationDefault":
					o.put("AnnotationDefault",this._keypair_value(cp));
					break;
				case "BootstrapMethods":
					int bsz=this.cr.readUShort();
					Object[][] bml=new Object[bsz][2];
					for (int j=0;j<bsz;j++){
						Object bm=cp[this.cr.readUShort()];
						Object[] ba_l=new Object[this.cr.readUShort()];
						for (int k=0;k<ba_l.length;k++){
							ba_l[k]=cp[(int)cp[this.cr.readUShort()]];
						}
						bml[j]=new Object[]{bm,ba_l};
					}
					o.put("BootstrapMethods",bml);
					break;
				case "MethodParameters":
					int pc=this.cr.readUByte();
					Object[][] mp=new Object[pc][2];
					for (int j=0;j<pc;j++){
						int ni=this.cr.readUShort();
						List<String> al=new ArrayList<String>();
						int afm=this.cr.readUShort();
						if (afm>=32768){
							afm%=32768;
							al.add("mandated");
						}
						if (afm>=4096){
							afm%=4096;
							al.add("synthetic");
						}
						if (afm>=16){
							afm%=16;
							al.add("final");
						}
						mp[j]=new Object[]{(ni==0?null:cp[ni]),al.toArray(new String[al.size()])};
					}
					o.put("MethodParameters",mp);
					break;
				default:
					o.put(p,new String(this.cr.read(l)));
					break;
			}
		}
		return o;
	}



	private Object _annotation(Object[] cp){
		String nm=(String)cp[this.cr.readUShort()];
		int sz=this.cr.readUShort();
		Map<String,Object> m=new HashMap<String,Object>();
		for (int i=0;i<sz;i++){
			String an=(String)cp[this.cr.readUShort()];
			m.put(an,this._keypair_value(cp));
		}
		return new Object[]{nm,m};
	}



	private Object _keypair_value(Object[] cp){
		char t=(char)this.cr.readUByte();
		switch (t){
			case 'B':
			case 'C':
			case 'D':
			case 'F':
			case 'I':
			case 'J':
			case 'S':
			case 'Z':
			case 's':
			case 'c':
				return cp[this.cr.readUShort()];
			case 'e':
				return new String[]{(String)cp[this.cr.readUShort()],(String)cp[this.cr.readUShort()]};
			case '@':
				return this._annotation(cp);
			case '[':
				int asz=this.cr.readUShort();
				Object[] a=new Object[asz];
				for (int j=0;j<asz;j++){
					a[j]=this._keypair_value(cp);
				}
				return a;
			default:
				return null;
		}
	}



	private void _write(Object[] cp,int[] cpt,String[] af_l,int ths_idx,int sup_idx,Object[] fl,Object[] ml,Map<String,Object> al){
		String _pkg=new File(this.base).toURI().relativize(this.f.toURI()).toString();
		String pkg=(_pkg.length()==0?null:this._base_class_path(_pkg.substring(0,_pkg.lastIndexOf("/")).replace("/",".")));
		String pkg_f=(_pkg.length()==0?null:this._base_path(_pkg.substring(0,_pkg.lastIndexOf("/"))));
		List<String> ll=new ArrayList<String>();
		List<String> il=new ArrayList<String>();
		Class c=new Class((String)cp[(int)cp[ths_idx]],(((String)cp[(int)cp[sup_idx]]).equals("java/lang/Object")?null:(String)cp[(int)cp[sup_idx]]),af_l,fl,ml,al,cp,cpt);
		c.write(ll,il,0);
		String is="";
		Collections.sort(il);
		for (int i=0;i<il.size();i++){
			if (this._base_path(il.get(i))!=null&&!this._base_path(il.get(i)).equals(pkg_f)&&((this._base_path(il.get(i)).equals("java/lang")&&LANG_IMPORT.contains(il.get(i)))||!this._base_path(il.get(i)).equals("java/lang"))){
				is+=String.format("\nimport %s;",il.get(i).replace("/","."));
			}
		}
		if (is.length()!=0){
			ll.add(0,String.format("%s\n\n\n",is.substring(1)));
		}
		if (pkg.length()!=0){
			ll.add(0,String.format("package %s;\n\n\n",pkg));
		}
		try{
			BufferedWriter fw=new BufferedWriter(new FileWriter(((String)al.get("SourceFile")).replace(".","2.")/*(String)al.get("SourceFile")*/));
			for (String l:ll){
				fw.write(l+"\n");
			}
			fw.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}



	private String _base_path(String p){
		String o="";
		for (int i=0;i<p.split("/").length-1;i++){
			o+="/"+p.split("/")[i];
		}
		return (o.length()==0?null:o.substring(1));
	}



	private String _base_class_path(String p){
		String o="";
		for (int i=0;i<p.split("\\.").length-1;i++){
			o+="."+p.split("\\.")[i];
		}
		return o.substring(1);
	}



	private List<String> _reverse(List<String> l){
		List<String> o=new ArrayList<String>();
		for (int i=l.size()-1;i>=0;i--){
			o.add(l.get(i));
		}
		return o;
	}



	private void _jar(){

	}
}






// int j=0;
// byte[] code=new byte[code_sz];
// while (j<code_sz){
// 	int op=this.cr.readUByte();
// 	System.out.printf("[%d > 0x%s (%s)]  ",j,(Integer.toHexString(op).length()==1?"0":"")+Integer.toHexString(op),(Integer.toString(op).length()==1?"  ":(Integer.toString(op).length()==2?" ":""))+Integer.toString(op));
// 	switch (op){
// 		case 0:
// 			System.out.printf("no action\n");
// 			break;
// 		case 1:
// 			System.out.printf("push null ref on stack\n");
// 			break;
// 		case 2:
// 			System.out.printf("load int -1 value on stack\n");
// 			break;
// 		case 3:
// 			System.out.printf("load int 0 value on stack\n");
// 			break;
// 		case 4:
// 			System.out.printf("load int 1 value on stack\n");
// 			break;
// 		case 5:
// 			System.out.printf("load int 2 value on stack\n");
// 			break;
// 		case 6:
// 			System.out.printf("load int 3 value on stack\n");
// 			break;
// 		case 7:
// 			System.out.printf("load int 4 value on stack\n");
// 			break;
// 		case 8:
// 			System.out.printf("load int 5 value on stack\n");
// 			break;
// 		case 9:
// 			System.out.printf("load long 0 value on stack\n");
// 			break;
// 		case 10:
// 			System.out.printf("load long 1 value on stack\n");
// 			break;
// 		case 11:
// 			System.out.printf("load float 0 value on stack\n");
// 			break;
// 		case 12:
// 			System.out.printf("load float 1 value on stack\n");
// 			break;
// 		case 13:
// 			System.out.printf("load float 2 value on stack\n");
// 			break;
// 		case 14:
// 			System.out.printf("load double 0 value on stack\n");
// 			break;
// 		case 15:
// 			System.out.printf("load double 1 value on stack\n");
// 			break;
// 		case 16:
// 			int v=this.cr.readByte();
// 			System.out.printf("load byte (%d) as int on stack\n",v);
// 			j++;
// 			break;
// 		case 17:
// 			v=this.cr.readUShort();
// 			System.out.printf("load short (%d) as int on stack\n",v);
// 			j+=2;
// 			break;
// 		case 18:
// 			int idx=this.cr.readUByte();
// 			System.out.printf("load cp item (%s) at idx (%d) on stack\n",cp[idx].toString(),idx);
// 			j++;
// 			break;
// 		case 19:
// 			idx=this.cr.readUShort();
// 			System.out.printf("load cp item (%s) at idx (%d) on stack (wide)\n",cp[idx].toString(),idx);
// 			j+=2;
// 			break;
// 		case 20:
// 			idx=this.cr.readUShort();
// 			System.out.printf("load cp item (%s) at idx (%d) {long/double/dynamic} on stack (wide)\n",cp[idx].toString(),idx);
// 			j+=2;
// 			break;
// 		case 21:
// 			idx=this.cr.readUByte();
// 			System.out.printf("load int from local var at idx (%d) on stack\n",idx);
// 			j++;
// 			break;
// 		case 22:
// 			idx=this.cr.readUByte();
// 			System.out.printf("load long from local var at idx (%d) on stack\n",idx);
// 			j++;
// 			break;
// 		case 23:
// 			idx=this.cr.readUByte();
// 			System.out.printf("load float from local var at idx (%d) on stack\n",idx);
// 			j++;
// 			break;
// 		case 24:
// 			idx=this.cr.readUByte();
// 			System.out.printf("load double from local var at idx (%d) on stack\n",idx);
// 			j++;
// 			break;
// 		case 25:
// 			idx=this.cr.readUByte();
// 			System.out.printf("load reference from local var at idx (%d) on stack\n",idx);
// 			j++;
// 			break;
// 		case 26:
// 			System.out.printf("load int from local var at 0 on stack\n");
// 			break;
// 		case 27:
// 			System.out.printf("load int from local var at 1 on stack\n");
// 			break;
// 		case 28:
// 			System.out.printf("load int from local var at 2 on stack\n");
// 			break;
// 		case 29:
// 			System.out.printf("load int from local var at 3 on stack\n");
// 			break;
// 		case 30:
// 			System.out.printf("load long from local var at 0 on stack\n");
// 			break;
// 		case 31:
// 			System.out.printf("load long from local var at 1 on stack\n");
// 			break;
// 		case 32:
// 			System.out.printf("load long from local var at 2 on stack\n");
// 			break;
// 		case 33:
// 			System.out.printf("load long from local var at 3 on stack\n");
// 			break;
// 		case 34:
// 			System.out.printf("load float from local var at 0 on stack\n");
// 			break;
// 		case 35:
// 			System.out.printf("load float from local var at 1 on stack\n");
// 			break;
// 		case 36:
// 			System.out.printf("load float from local var at 2 on stack\n");
// 			break;
// 		case 37:
// 			System.out.printf("load float from local var at 3 on stack\n");
// 			break;
// 		case 38:
// 			System.out.printf("load double from local var at 0 on stack\n");
// 			break;
// 		case 39:
// 			System.out.printf("load double from local var at 1 on stack\n");
// 			break;
// 		case 40:
// 			System.out.printf("load double from local var at 2 on stack\n");
// 			break;
// 		case 41:
// 			System.out.printf("load double from local var at 3 on stack\n");
// 			break;
// 		case 42:
// 			System.out.printf("load reference from local var at 0 on stack\n");
// 			break;
// 		case 43:
// 			System.out.printf("load reference from local var at 1 on stack\n");
// 			break;
// 		case 44:
// 			System.out.printf("load reference from local var at 2 on stack\n");
// 			break;
// 		case 45:
// 			System.out.printf("load reference from local var at 3 on stack\n");
// 			break;
// 		case 46:
// 			System.out.printf("load int from array on stack\n");
// 			break;
// 		case 47:
// 			System.out.printf("load long from array on stack\n");
// 			break;
// 		case 48:
// 			System.out.printf("load float from array on stack\n");
// 			break;
// 		case 49:
// 			System.out.printf("load double from array on stack\n");
// 			break;
// 		case 50:
// 			System.out.printf("load reference from array on stack\n");
// 			break;
// 		case 51:
// 			System.out.printf("load byte/boolean from array on stack\n");
// 			break;
// 		case 52:
// 			System.out.printf("load char from array on stack\n");
// 			break;
// 		case 53:
// 			System.out.printf("load short from array on stack\n");
// 			break;
// 		case 54:
// 			idx=this.cr.readUByte();
// 			System.out.printf("store int value into local var at idx (%d)\n",idx);
// 			j++;
// 			break;
// 		case 55:
// 			idx=this.cr.readUByte();
// 			System.out.printf("store long value into local var at idx (%d)\n",idx);
// 			j++;
// 			break;
// 		case 56:
// 			idx=this.cr.readUByte();
// 			System.out.printf("store float value into local var at idx (%d)\n",idx);
// 			j++;
// 			break;
// 		case 57:
// 			idx=this.cr.readUByte();
// 			System.out.printf("store double value into local var at idx (%d)\n",idx);
// 			j++;
// 			break;
// 		case 58:
// 			idx=this.cr.readUByte();
// 			System.out.printf("store reference value into local var at idx (%d)\n",idx);
// 			j++;
// 			break;
// 		case 59:
// 			System.out.printf("store int value into local var at 0\n");
// 			break;
// 		case 60:
// 			System.out.printf("store int value into local var at 1\n");
// 			break;
// 		case 61:
// 			System.out.printf("store int value into local var at 2\n");
// 			break;
// 		case 62:
// 			System.out.printf("store int value into local var at 3\n");
// 			break;
// 		case 63:
// 			System.out.printf("store long value into local var at 0\n");
// 			break;
// 		case 64:
// 			System.out.printf("store long value into local var at 1\n");
// 			break;
// 		case 65:
// 			System.out.printf("store long value into local var at 2\n");
// 			break;
// 		case 66:
// 			System.out.printf("store long value into local var at 3\n");
// 			break;
// 		case 67:
// 			System.out.printf("store float value into local var at 0\n");
// 			break;
// 		case 68:
// 			System.out.printf("store float value into local var at 1\n");
// 			break;
// 		case 69:
// 			System.out.printf("store float value into local var at 2\n");
// 			break;
// 		case 70:
// 			System.out.printf("store float value into local var at 3\n");
// 			break;
// 		case 71:
// 			System.out.printf("store double value into local var at 0\n");
// 			break;
// 		case 72:
// 			System.out.printf("store double value into local var at 1\n");
// 			break;
// 		case 73:
// 			System.out.printf("store double value into local var at 2\n");
// 			break;
// 		case 74:
// 			System.out.printf("store double value into local var at 3\n");
// 			break;
// 		case 75:
// 			System.out.printf("store reference value into local var at 0\n");
// 			break;
// 		case 76:
// 			System.out.printf("store reference value into local var at 1\n");
// 			break;
// 		case 77:
// 			System.out.printf("store reference value into local var at 2\n");
// 			break;
// 		case 78:
// 			System.out.printf("store reference value into local var at 3\n");
// 			break;
// 		case 79:
// 			System.out.printf("store int value into array\n");
// 			break;
// 		case 80:
// 			System.out.printf("store long value into array\n");
// 			break;
// 		case 81:
// 			System.out.printf("store float value into array\n");
// 			break;
// 		case 82:
// 			System.out.printf("store double value into array\n");
// 			break;
// 		case 83:
// 			System.out.printf("store reference value into array\n");
// 			break;
// 		case 84:
// 			System.out.printf("store byte/boolean value into array\n");
// 			break;
// 		case 85:
// 			System.out.printf("store char value into array\n");
// 			break;
// 		case 86:
// 			System.out.printf("store short value into array\n");
// 			break;
// 		case 87:
// 			System.out.printf("pop value of the top of stack\n");
// 			break;
// 		case 88:
// 			System.out.printf("pop 2 values of the top of stack\n");
// 			break;
// 		case 89:
// 			System.out.printf("duplicate value on top of stack\n");
// 			break;
// 		case 90:
// 			System.out.printf("duplicate value 2 from top of stack\n");
// 			break;
// 		case 91:
// 			System.out.printf("insert value from top of stack 2 (or 3) valus from top\n");
// 			break;
// 		case 92:
// 			System.out.printf("duplicate 2 top values\n");
// 			break;
// 		case 93:
// 			System.out.printf("duplicate 2 top values and insert 3 from top\n");
// 			break;
// 		case 94:
// 			System.out.printf("duplicate 2 top values and insert 4 from top\n");
// 			break;
// 		case 95:
// 			System.out.printf("swap 2 top values\n");
// 			break;
// 		case 96:
// 			System.out.printf("add 2 ints {a + b}\n");
// 			break;
// 		case 97:
// 			System.out.printf("add 2 longs {a + b}\n");
// 			break;
// 		case 98:
// 			System.out.printf("add 2 floats {a + b}\n");
// 			break;
// 		case 99:
// 			System.out.printf("add 2 doubles {a + b}\n");
// 			break;
// 		case 100:
// 			System.out.printf("subtract 2 ints {a - b}\n");
// 			break;
// 		case 101:
// 			System.out.printf("subtract 2 longs {a - b}\n");
// 			break;
// 		case 102:
// 			System.out.printf("subtract 2 floats {a - b}\n");
// 			break;
// 		case 103:
// 			System.out.printf("subtract 2 doubles {a - b}\n");
// 			break;
// 		case 104:
// 			System.out.printf("multiply 2 ints {a * b}\n");
// 			break;
// 		case 105:
// 			System.out.printf("multiply 2 longs {a * b}\n");
// 			break;
// 		case 106:
// 			System.out.printf("multiply 2 floats {a * b}\n");
// 			break;
// 		case 107:
// 			System.out.printf("multiply 2 doubles {a * b}\n");
// 			break;
// 		case 108:
// 			System.out.printf("divide 2 ints {a / b}\n");
// 			break;
// 		case 109:
// 			System.out.printf("divide 2 longs {a / b}\n");
// 			break;
// 		case 110:
// 			System.out.printf("divide 2 floats {a / b}\n");
// 			break;
// 		case 111:
// 			System.out.printf("divide 2 doubles {a / b}\n");
// 			break;
// 		case 112:
// 			System.out.printf("logical int reminder {a - (a / b) * b }\n");
// 			break;
// 		case 113:
// 			System.out.printf("logical long reminder {a - (a / b) * b }\n");
// 			break;
// 		case 114:
// 			System.out.printf("logical float reminder {a - (a / b) * b }\n");
// 			break;
// 		case 115:
// 			System.out.printf("logical double reminder {a - (a / b) * b }\n");
// 			break;
// 		case 116:
// 			System.out.printf("negate int {-v}\n");
// 			break;
// 		case 117:
// 			System.out.printf("negate long {-v}\n");
// 			break;
// 		case 118:
// 			System.out.printf("negate float {-v}\n");
// 			break;
// 		case 119:
// 			System.out.printf("negate double {-v}\n");
// 			break;
// 		case 120:
// 			System.out.printf("int bitwise shift left {a >> b}\n");
// 			break;
// 		case 121:
// 			System.out.printf("long bitwise shift left {a >> b}\n");
// 			break;
// 		case 122:
// 			System.out.printf("int arithmetic shift right {a << b}\n");
// 			break;
// 		case 123:
// 			System.out.printf("long arithmetic shift right {a << b}\n");
// 			break;
// 		case 124:
// 			System.out.printf("int logical shift right {a <<< b}\n");
// 			break;
// 		case 125:
// 			System.out.printf("long logical shift right {a <<< b}\n");
// 			break;
// 		case 126:
// 			System.out.printf("bitwise and of 2 ints\n");
// 			break;
// 		case 127:
// 			System.out.printf("bitwise and of 2 longs\n");
// 			break;
// 		case 128:
// 			System.out.printf("bitwise or of 2 ints\n");
// 			break;
// 		case 129:
// 			System.out.printf("bitwise or of 2 longs\n");
// 			break;
// 		case 130:
// 			System.out.printf("bitwise xor of 2 ints\n");
// 			break;
// 		case 131:
// 			System.out.printf("bitwise xor of 2 long\n");
// 			break;
// 		case 132:
// 			idx=this.cr.readUByte();
// 			int cn=this.cr.readByte();
// 			System.out.printf("increment local var at idx (%d) by const (%d)\n",idx,cn);
// 			j+=2;
// 			break;
// 		case 133:
// 			System.out.printf("convert int to long\n");
// 			break;
// 		case 134:
// 			System.out.printf("convert int to float\n");
// 			break;
// 		case 135:
// 			System.out.printf("convert int to double\n");
// 			break;
// 		case 136:
// 			System.out.printf("convert long to int\n");
// 			break;
// 		case 137:
// 			System.out.printf("convert long to float\n");
// 			break;
// 		case 138:
// 			System.out.printf("convert long to double\n");
// 			break;
// 		case 139:
// 			System.out.printf("convert float to int\n");
// 			break;
// 		case 140:
// 			System.out.printf("convert float to long\n");
// 			break;
// 		case 141:
// 			System.out.printf("convert float to double\n");
// 			break;
// 		case 142:
// 			System.out.printf("convert double to int\n");
// 			break;
// 		case 143:
// 			System.out.printf("convert double to long\n");
// 			break;
// 		case 144:
// 			System.out.printf("convert double to float\n");
// 			break;
// 		case 145:
// 			System.out.printf("convert int to byte\n");
// 			break;
// 		case 146:
// 			System.out.printf("convert int to char\n");
// 			break;
// 		case 147:
// 			System.out.printf("convert int to short\n");
// 			break;
// 		case 148:
// 			System.out.printf("if v1==v2 push 0, if v1>v2 push 1, else -1 (long)\n");
// 			break;
// 		case 149:
// 			System.out.printf("if v1==v2 push 0, if v1>v2 push 1, if v1<v2 push -1, else (v1 or v2 is NaN) push -1 (float)\n");
// 			break;
// 		case 150:
// 			System.out.printf("if v1==v2 push 0, if v1>v2 push 1, if v1<v2 push -1, else (v1 or v2 is NaN) push 1 (float)\n");
// 			break;
// 		case 151:
// 			System.out.printf("if v1==v2 push 0, if v1>v2 push 1, if v1<v2 push -1, else (v1 or v2 is NaN) push -1 (double)\n");
// 			break;
// 		case 152:
// 			System.out.printf("if v1==v2 push 0, if v1>v2 push 1, if v1<v2 push -1, else (v1 or v2 is NaN) push 1 (double)\n");
// 			break;
// 		case 153:
// 			idx=this.cr.readUShort();
// 			System.out.printf("if v == 0 goto instruction at %d\n",idx+j);
// 			j+=2;
// 			break;
// 		case 154:
// 			idx=this.cr.readUShort();
// 			System.out.printf("if v != 0 goto instruction at %d\n",idx+j);
// 			j+=2;
// 			break;
// 		case 155:
// 			idx=this.cr.readUShort();
// 			System.out.printf("if v < 0 goto instruction at %d\n",idx+j);
// 			j+=2;
// 			break;
// 		case 156:
// 			idx=this.cr.readUShort();
// 			System.out.printf("if v >= 0 goto instruction at %d\n",idx+j);
// 			j+=2;
// 			break;
// 		case 157:
// 			idx=this.cr.readUShort();
// 			System.out.printf("if v > 0 goto instruction at %d\n",idx+j);
// 			j+=2;
// 			break;
// 		case 158:
// 			idx=this.cr.readUShort();
// 			System.out.printf("if v <= 0 goto instruction at %d\n",idx+j);
// 			j+=2;
// 			break;
// 		case 159:
// 			idx=this.cr.readUShort();
// 			System.out.printf("if a == b goto instruction at %d\n",idx+j);
// 			j+=2;
// 			break;
// 		case 160:
// 			idx=this.cr.readUShort();
// 			System.out.printf("if a != b goto instruction at %d\n",idx+j);
// 			j+=2;
// 			break;
// 		case 161:
// 			idx=this.cr.readUShort();
// 			System.out.printf("if v1 < v2 goto instruction at %d\n",idx+j);
// 			j+=2;
// 			break;
// 		case 162:
// 			idx=this.cr.readUShort();
// 			System.out.printf("if v1 >= v2 goto instruction at %d\n",idx+j);
// 			j+=2;
// 			break;
// 		case 163:
// 			idx=this.cr.readUShort();
// 			System.out.printf("if v1 > v2 goto instruction at %d\n",idx+j);
// 			j+=2;
// 			break;
// 		case 164:
// 			idx=this.cr.readUShort();
// 			System.out.printf("if v1 <= v2 goto instruction at %d\n",idx+j);
// 			j+=2;
// 			break;
// 		case 165:
// 			idx=this.cr.readUShort();
// 			System.out.printf("if v1 == v2 goto instruction at %d\n",idx);
// 			j+=2;
// 			break;
// 		case 166:
// 			idx=this.cr.readUShort();
// 			System.out.printf("if v1 != v2 goto instruction at %d\n",idx+j);
// 			j+=2;
// 			break;
// 		case 167:
// 			idx=this.cr.readUShort();
// 			System.out.printf("goto instruction at %d\n",idx+j);
// 			j+=2;
// 			break;
// 		case 168:
// 			idx=this.cr.readUShort();
// 			System.out.printf("goto subroutine at %d and return value on stack\n",idx+j);
// 			j+=2;
// 			break;
// 		case 169:
// 			idx=this.cr.readUShort();
// 			System.out.printf("goto instruction at local var at idx (%d)\n",idx+j);
// 			j+=2;
// 			break;
// 		case 170:
// 			int pad=0;
// 			while ((j+pad+1)%4!=0){
// 				pad++;
// 				this.cr.readByte();
// 			}
// 			int df=this.cr.readInt()+j;
// 			int lw=this.cr.readInt();
// 			int hg=this.cr.readInt();
// 			int[] jt=new int[hg-lw+1];
// 			for (int k=0;k<hg-lw+1;k++){
// 				jt[k]=this.cr.readInt()+j;
// 			}
// 			System.out.printf("table switch with %d low, %d high, %d jump locations (%s) and default %d\n",lw,hg,jt.length,java.util.Arrays.toString(jt),df);
// 			j+=pad+12+4*jt.length;
// 			break;
// 		case 171:
// 			pad=0;
// 			while ((j+pad+1)%4!=0){
// 				pad++;
// 				this.cr.readByte();
// 			}
// 			df=this.cr.readInt()+j;
// 			int pn=this.cr.readInt();
// 			Map<Integer,Integer> t=new HashMap<Integer,Integer>();
// 			for (int k=0;k<pn;k++){
// 				t.put(this.cr.readInt(),this.cr.readInt()+j);
// 			}
// 			System.out.printf("lookup switch with %d keypairs (%s) and default %d\n",pn,t.toString(),df);
// 			j+=pad+8+8*pn;
// 			break;
// 		case 172:
// 			System.out.printf("return int from method\n");
// 			break;
// 		case 173:
// 			System.out.printf("return long from method\n");
// 			break;
// 		case 174:
// 			System.out.printf("return float from method\n");
// 			break;
// 		case 175:
// 			System.out.printf("return double from method\n");
// 			break;
// 		case 176:
// 			System.out.printf("return reference from method\n");
// 			break;
// 		case 177:
// 			System.out.printf("return void from method\n");
// 			break;
// 		case 178:
// 			idx=this.cr.readUShort();
// 			System.out.printf("get static filed in cp at idx (%d)\n",idx);
// 			j+=2;
// 			break;
// 		case 179:
// 			idx=this.cr.readUShort();
// 			System.out.printf("set static filed in cp at idx (%d) value\n",idx);
// 			j+=2;
// 			break;
// 		case 180:
// 			idx=this.cr.readUShort();
// 			System.out.printf("get filed in cp at idx (%d)\n",idx);
// 			j+=2;
// 			break;
// 		case 181:
// 			idx=this.cr.readUShort();
// 			System.out.printf("set filed in cp at idx (%d) value\n",idx);
// 			j+=2;
// 			break;
// 		case 182:
// 			idx=this.cr.readUShort();
// 			System.out.printf("invoke a virtual method in cp at idx (%d) and result on stack\n",idx);
// 			j+=2;
// 			break;
// 		case 183:
// 			idx=this.cr.readUShort();
// 			System.out.printf("invoke a special (instance) method in cp at idx (%d) and result on stack\n",idx);
// 			j+=2;
// 			break;
// 		case 184:
// 			idx=this.cr.readUShort();
// 			System.out.printf("invoke a static method in cp at idx (%d) and result on stack\n",idx);
// 			j+=2;
// 			break;
// 		case 185:
// 			idx=this.cr.readUShort();
// 			int an=this.cr.readUByte();
// 			this.cr.readUByte();
// 			System.out.printf("invoke an interface in cp (%s) at idx (%d) with %d args\n",cp[idx].toString(),idx,an);
// 			j+=4;
// 			break;
// 		case 186:
// 			idx=this.cr.readUShort();
// 			this.cr.readUShort();
// 			System.out.printf("invoke a dynamic method in cp at idx (%d)\n",idx);
// 			j+=4;
// 			break;
// 		case 187:
// 			idx=this.cr.readUShort();
// 			System.out.printf("create new object of type in cp (%s) at idx (%d)\n",cp[(int)cp[idx]].toString(),idx);
// 			j+=2;
// 			break;
// 		case 188:
// 			int tp=this.cr.readUByte();
// 			System.out.printf("create new array of primitive type (%d)\n",tp);
// 			j++;
// 			break;
// 		case 189:
// 			idx=this.cr.readUShort();
// 			System.out.printf("create new array of type in cp (%s) at idx (%d)\n",cp[(int)cp[idx]].toString(),idx);
// 			j+=2;
// 			break;
// 		case 191:
// 			System.out.printf("throw an exception and clear the stack\n");
// 			break;
// 		case 192:
// 			idx=this.cr.readUShort();
// 			System.out.printf("check if type of objectref can cast to cp (%s) at idx (%d)\n",cp[(int)cp[idx]].toString(),idx);
// 			j+=2;
// 			break;
// 		case 193:
// 			idx=this.cr.readUShort();
// 			System.out.printf("check if type of objectref is same as cp (%s) at idx (%d)\n",cp[(int)cp[idx]].toString(),idx);
// 			j+=2;
// 			break;
// 		case 194:
// 			System.out.printf("start of synchronised section\n");
// 			break;
// 		case 195:
// 			System.out.printf("end of synchronised section\n");
// 			break;
// 		case 196:
// 			int op2=this.cr.readUByte();
// 			switch (op2){
// 				case 15:
// 					idx=this.cr.readUShort();
// 					System.out.printf("load int from local var at idx (%d) on stack (wide)\n",idx);
// 					j+=2;
// 					break;
// 				case 16:
// 					idx=this.cr.readUShort();
// 					System.out.printf("load long from local var at idx (%d) on stack (wide)\n",idx);
// 					j+=2;
// 					break;
// 				case 17:
// 					idx=this.cr.readUShort();
// 					System.out.printf("load float from local var at idx (%d) on stack (wide)\n",idx);
// 					j+=2;
// 					break;
// 				case 18:
// 					idx=this.cr.readUShort();
// 					System.out.printf("load double from local var at idx (%d) on stack (wide)\n",idx);
// 					j+=2;
// 					break;
// 				case 19:
// 					idx=this.cr.readUShort();
// 					System.out.printf("load reference from local var at idx (%d) on stack (wide)\n",idx);
// 					j+=2;
// 					break;
// 				case 36:
// 					idx=this.cr.readUShort();
// 					System.out.printf("store int from stack into local var at idx (%d) (wide)\n",idx);
// 					j+=2;
// 					break;
// 				case 37:
// 					idx=this.cr.readUShort();
// 					System.out.printf("store long from stack into local var at idx (%d) (wide)\n",idx);
// 					j+=2;
// 					break;
// 				case 38:
// 					idx=this.cr.readUShort();
// 					System.out.printf("store float from stack into local var at idx (%d) (wide)\n",idx);
// 					j+=2;
// 					break;
// 				case 39:
// 					idx=this.cr.readUShort();
// 					System.out.printf("store double from stack into local var at idx (%d) (wide)\n",idx);
// 					j+=2;
// 					break;
// 				case 40:
// 					idx=this.cr.readUShort();
// 					System.out.printf("store reference from stack into local var at idx (%d) (wide)\n",idx);
// 					j+=2;
// 					break;
// 				case 84:
// 					idx=this.cr.readUShort();
// 					int cnt=this.cr.readUShort();
// 					System.out.printf("increment local var at idx (%d) by const (%d) (wide)\n",idx,cnt);
// 					j+=4;
// 					break;
// 				default:
// 					System.out.printf("UNKNOWN OPCODE (WIDE): %d (%s)\n",op2,Integer.toHexString(op2));
// 					break;
// 			}
// 			j++;
// 			break;
// 		case 197:
// 			idx=this.cr.readUShort();
// 			int dsz=this.cr.readUByte();
// 			System.out.printf("create multidimensional array of type (%s) at idx (%d) with %d dimensions\n",cp[(int)cp[idx]].toString(),idx,dsz);
// 			j+=3;
// 			break;
// 		case 198:
// 			idx=this.cr.readUShort();
// 			System.out.printf("if value null brack to instruction at idx (%d)\n",idx);
// 			j+=2;
// 			break;
// 		case 199:
// 			idx=this.cr.readUShort();
// 			System.out.printf("if value not null brack to instruction at idx (%d)\n",idx);
// 			j+=2;
// 			break;
// 		case 200:
// 			idx=this.cr.readInt();
// 			System.out.printf("go to another instruction at idx (%d)\n",idx);
// 			j+=4;
// 			break;
// 		case 201:
// 			idx=this.cr.readInt();
// 			System.out.printf("jump to subroutine at idx (%d) and return address on stack\n",idx);
// 			j+=4;
// 			break;
// 		case 202:
// 			System.out.printf("debugger\n");
// 			break;
// 		case 254:
// 			System.out.printf("impdep1\n");
// 			break;
// 		case 255:
// 			System.out.printf("impdep2\n");
// 			break;
// 		default:
// 			System.out.printf("UNKNOWN OPCODE: %d (0x%s)\n",op,Integer.toHexString(op));
// 			break;
// 	}
// 	j++;
// }
