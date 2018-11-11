package com.example.define;

public class KIND {
	
	private int kind;
	
	public KIND(int k){
		kind = k;
	}
	public int toInt() {
		return kind;
	}
	


	public boolean isWave() {
		return (this.kind&0x1)==1;
	}
	public boolean isTyphoon() {
		return (this.kind&0x2)>>1==1;
	}
	public boolean isRain() {
		return (this.kind&0x4)>>2==1;
	}
	public boolean isRiver() {
		return (this.kind&0x8)>>3==1;
	}
	public boolean isSand() {
		return (this.kind&0x10)>>4==1;
	}
	public boolean isLifeline() {
		return (this.kind&0x20)>>5==1;
	}
	public boolean isAlart() {
		return (this.kind&0x40)>>6==1;
	}
	public int getKind() {
		return this.kind;
	}
	public KIND setAlart(boolean b) {
		if(b) {
			this.kind |= 0x40;
		}else {
			this.kind &= (0xff-0x40);
		}
		return this;
	}
	public KIND setSand(boolean b) {
		if(b) {
			this.kind |= 0x10;
		}else {
			this.kind &= (0xff-0x10);
		}
		return this;
	}
	public KIND setWave(boolean b) {
		if(b) {
			this.kind |= 0x01;
		}else {
			this.kind &= (0xff-0x01);
		}
		return this;
	}
	public KIND setLifeline(boolean b) {
		if(b) {
			this.kind |= 0x20;
		}else {
			this.kind &= (0xff-0x20);
		}
		return this;
	}
	public KIND setRiver(boolean b) {
		if(b) {
			this.kind |= 0x8;
		}else {
			this.kind &= (0xff-0x8);
		}
		return this;
	}
	public KIND setRain(boolean b) {
		if(b) {
			this.kind |= 0x4;
		}else {
			this.kind &= (0xff-0x4);
		}
		return this;
	}
	public KIND setTyphoon(boolean b) {
		if(b) {
			this.kind |= 0x2;
		}else {
			this.kind &= (0xff-0x2);
		}
		return this;
	}

	
}
