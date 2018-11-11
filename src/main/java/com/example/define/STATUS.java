package com.example.define;

public enum STATUS {
	init(1),
	normal(5),
	hello(10),
	namae(11),
	namae_kakunin(19),
	jis1(20),
	jis1_miss(24),
	jis2(25),
	jis2_multi(26),
	jis2_miss(27),
	jis_kakunin(29),
	jouhou1(50),
	jouhou2(51),
	jouhou3(52),
	jouhou_kakunin(59),
	modori1(60),
	touroku(61),
	touroku_full(62),
	touroku_kanryou(65),
	pull(201),
	train0000(500),
	edit(79),
	jouhou1edit(80),
	jouhou2edit(81),
	jouhou3edit(82),
	edit_kakunin(83),
	edit_kanryou(85),
	touroku_kanriyou(100), menu(999), more(300);
		
	private final int id;

    private STATUS(final int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
    
    public static STATUS getSTATUS(int id) {
    	for(STATUS s:values()){
    		if(s.getId() == id) return s;
    	}
    	return STATUS.normal;
    	
    }
}
