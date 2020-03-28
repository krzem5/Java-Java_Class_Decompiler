package com.krzem.java_class_decompiler;



import java.io.File;
import java.lang.Exception;
import java.nio.file.Files;



public class ClassFileReader{
	private byte[] data;
	private int off=0;



	public ClassFileReader(File f){
		try{
			this.data=Files.readAllBytes(f.toPath());
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}



	public byte[] read(int l){
		byte[] dt=new byte[l];
		System.arraycopy(this.data,this.off,dt,0,l);
		this.off+=l;
		return dt;
	}



	public byte readByte(){
		return this.data[this.off++];
	}



	public int readUByte(){
		return (this.data[this.off++]&0xff);
	}



	public int readUShort(){
		return ((this.data[this.off++]&0xff)<<8)|(this.data[this.off++]&0xff);
	}



	public int readInt(){
		return ((this.data[this.off++]&0xff)<<24)|((this.data[this.off++]&0xff)<<16)|((this.data[this.off++]&0xff)<<8)|(this.data[this.off++]&0xff);
	}



	public float readFloat(){
		return Float.intBitsToFloat(((this.data[this.off++]&0xff)<<24)|((this.data[this.off++]&0xff)<<16)|((this.data[this.off++]&0xff)<<8)|(this.data[this.off++]&0xff));
	}



	public long readLong(){
		return (((((this.data[this.off++]&0xff)<<24)|((this.data[this.off++]&0xff)<<16)|((this.data[this.off++]&0xff)<<8)|(this.data[this.off++]&0xff))&0xffffffffL)<<32)|((((this.data[this.off++]&0xff)<<24)|((this.data[this.off++]&0xff)<<16)|((this.data[this.off++]&0xff)<<8)|(this.data[this.off++]&0xff))&0xffffffffL);
	}



	public double readDouble(){
		return Double.longBitsToDouble((((((this.data[this.off++]&0xff)<<24)|((this.data[this.off++]&0xff)<<16)|((this.data[this.off++]&0xff)<<8)|(this.data[this.off++]&0xff))&0xffffffffL)<<32)|((((this.data[this.off++]&0xff)<<24)|((this.data[this.off++]&0xff)<<16)|((this.data[this.off++]&0xff)<<8)|(this.data[this.off++]&0xff))&0xffffffffL));
	}



	public String readString(){
		int len=this.readUShort();
		char[] ca=new char[len];
		int[] c=new int[3];
		int cao=0;
		int e=this.off+len;
		while (this.off<e){
			c[0]=this.readUByte();
			if (c[0]>>4<=7){
				ca[cao++]=(char)c[0];
			}
			else if (c[0]>>4==12||c[0]>>4==13){
				c[1]=(int)this.readByte();
				ca[cao++]=(char)(((c[0]&0x1f)<<6)|(c[1]&0x3f));
			}
			else if (c[0]>>4==14){
				c[1]=(int)this.readByte();
				c[2]=(int)this.readByte();
				ca[cao++]=(char)(((c[0]&0x0f)<<12)|((c[1]&0x3f)<<6)|(c[2]&0x3f));
			}
		}
		return new String(ca,0,cao);
	}
}