package cn.ohyeah.stb.key;

/**
 * ¼üÂë±í
 * @author maqian
 * @version 1.0
 */
public interface KeyCode {
	public static final int INVALID    	= 0;
	public static final int NUM0		= 1<<0;		// 0 Key
	public static final int NUM1     	= 1<<1; 	// 1 Key
	public static final int NUM2     	= 1<<2; 	// 2 Key
	public static final int NUM3     	= 1<<3; 	// 3 Key
	public static final int NUM4     	= 1<<4; 	// 4 Key
	public static final int NUM5     	= 1<<5; 	// 5 Key
	public static final int NUM6     	= 1<<6; 	// 6 Key
	public static final int NUM7     	= 1<<7; 	// 7 Key
	public static final int NUM8     	= 1<<8; 	// 8 Key
	public static final int NUM9     	= 1<<9; 	// 9 Key
	public static final int ASTERISK	= 1<<10;    // * Key
	public static final int POUND    	= 1<<11;    // # Key
 
	public static final int UP       	= 1<<12;	// UP Key
	public static final int DOWN     	= 1<<13;	// DOWN Key	
	public static final int LEFT     	= 1<<14; 	// LEFT Key
	public static final int RIGHT    	= 1<<15; 	// RIGHT Key
	public static final int CLEAR    	= 1<<16;   	// Clear Key 
	public static final int OK    		= 1<<17; 	// OK KEY
	public static final int BACK    	= 1<<18;   	// BACK KEY
	
	public static final int MENU			=1<<20;	//MENU KEY
	public static final int PAGE_UP			=1<<21;	//PAGE UP KEY
	public static final int PAGE_DOWN		=1<<22;	//PAGE DOWN KEY
	public static final int VOLUME_UP		=1<<23;	//VOLUME UP KEY
	public static final int VOLUME_DOWN		=1<<24;	//VOLUME DOWN KEY
	public static final int CHANNEL_UP		=1<<25;
	public static final int CHANNEL_DOWN	=1<<26;
	public static final int MUTE			=1<<27;	//MUTE KEY
	public static final int SWITCH			=1<<30; //SWITCH KEY
}
