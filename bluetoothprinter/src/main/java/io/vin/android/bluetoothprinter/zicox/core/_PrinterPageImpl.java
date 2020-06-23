package io.vin.android.bluetoothprinter.zicox.core;

import android.graphics.Bitmap;

import java.io.UnsupportedEncodingException;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;


public class _PrinterPageImpl {
	private byte[] totalData=new byte[150*1024];
	private int    totalDataLen=0;
	private byte[] listData=new byte[150*1024];
	private int    listDataLen=0;
	private String begin;
	private int _pw;
	private int _ph;

	private String end;

	private String strINVERSE="";

	public String _str;
	public _PrinterPageImpl()
	{
	
	}
	public void Create(int width,int height)
	{

		listDataLen=0;
		_pw=width;
		_ph=height;
		//begin = String.format("! 0 200 200 %d 1\r\nPAGE-WIDTH %d \r\nGAP-SENSE\r\n", height,width);
	//	end = String.format("ZPROTATE\r\nPRINT\r\n").getBytes();
	}
	public void Clear()
	{
		listDataLen=0;
	}
	public void add(byte[] d)
	{
		System.arraycopy(d,0,listData,listDataLen,d.length);
		listDataLen+=d.length;
	}
	public void feed()
	{
	//	add("GAP-SENSE\r\n".getBytes());
	}
	public int getDataLen()
	{
		return totalDataLen;
	}

	public byte[] GetData(int r,int gap)
	{
	/*	if (r==1)
		{
			// if(gap==0){begin = String.format("! 0 200 200 %d 1\r\nPAGE-WIDTH %d \r\n", _ph,_pw); end = String.format("ZPROTATE90\r\nPRINT\r\n");}
			// if(gap==1){begin = String.format("! 0 200 200 %d 1\r\nPAGE-WIDTH %d \r\nGAP-SENSE\r\n", _ph,_pw);end = String.format("ZPROTATE90\r\nFORM\r\nPRINT\r\n");}
		    // if(gap==2){begin = String.format("! 0 200 200 %d 1\r\nPAGE-WIDTH %d \r\nBAR-SENSE-LEFT\r\n", _ph,_pw);end = String.format("ZPROTATE90\r\nFORM\r\nPRINT\r\n");}
			// if(gap==3){begin = String.format("! 0 200 200 %d 1\r\nPAGE-WIDTH %d \r\nBAR-SENSE\r\n", _ph,_pw);end = String.format("ZPROTATE90\r\nFORM\r\nPRINT\r\n");}
			if(gap==0){begin = String.format("! 0 200 200 %d 1\r\nPAGE-WIDTH %d \r\nZPROTATE180\r\n", _ph,_pw); end = String.format("PRINT\r\n");}
			if(gap==1){begin = String.format("! 0 200 200 %d 1\r\nPAGE-WIDTH %d \r\nZPROTATE180\r\nGAP-SENSE\r\n", _ph,_pw);end = String.format("FORM\r\nPRINT\r\n");}
			if(gap==2){begin = String.format("! 0 200 200 %d 1\r\nPAGE-WIDTH %d \r\nZPROTATE180\r\nBAR-SENSE-LEFT\r\n", _ph,_pw);end = String.format("FORM\r\nPRINT\r\n");}
			if(gap==3){begin = String.format("! 0 200 200 %d 1\r\nPAGE-WIDTH %d \r\nZPROTATE180\r\nBAR-SENSE\r\n", _ph,_pw);end = String.format("FORM\r\nPRINT\r\n");}
		}
		else 	
		{
		//	if(INVERSE==true)
		//	{end = String.format(strINVERSE+"PRINT\r\n");
		//	INVERSE=false;
		//	}
		//	else
		//		end = String.format("PRINT\r\n");

			if(gap==0){begin = String.format("! 0 200 200 %d 1\r\nPAGE-WIDTH %d \r\n", _ph,_pw); end = String.format("PRINT\r\n");}
			if(gap==1){begin = String.format("! 0 200 200 %d 1\r\nPAGE-WIDTH %d \r\nGAP-SENSE\r\n", _ph,_pw);end = String.format("FORM\r\nPRINT\r\n");}
			if(gap==2){begin = String.format("! 0 200 200 %d 1\r\nPAGE-WIDTH %d \r\nBAR-SENSE-LEFT\r\n", _ph,_pw);end = String.format("FORM\r\nPRINT\r\n");}
			if(gap==3){begin = String.format("! 0 200 200 %d 1\r\nPAGE-WIDTH %d \r\nBAR-SENSE\r\n", _ph,_pw);end = String.format("FORM\r\nPRINT\r\n");}
		
		//	else   { begin = String.format("! 0 200 200 %d 1\r\nPAGE-WIDTH %d \r\nGAP-SENSE\r\n", _ph,_pw);  end = String.format("FORM\r\nPRINT\r\n");}
				
		}
		*/
		if (r==0)
		{
			if(gap==0){begin = String.format("! 0 200 200 %d 1\r\nPAGE-WIDTH %d \r\n", _ph,_pw); end = String.format("PRINT\r\n");}
			if(gap==1){begin = String.format("! 0 200 200 %d 1\r\nPAGE-WIDTH %d \r\nGAP-SENSE\r\n", _ph,_pw);end = String.format("FORM\r\nPRINT\r\n");}
		}
		if (r==90)
		{
			if(gap==0){begin = String.format("! 0 200 200 %d 1\r\nPAGE-WIDTH %d \r\nZPROTATE90\r\n", _ph,_pw); end = String.format("PRINT\r\n");}
			if(gap==1){begin = String.format("! 0 200 200 %d 1\r\nPAGE-WIDTH %d \r\nZPROTATE90\r\nGAP-SENSE\r\n", _ph,_pw);end = String.format("FORM\r\nPRINT\r\n");}
		}
		if (r==180)
		{
			if(gap==0){begin = String.format("! 0 200 200 %d 1\r\nPAGE-WIDTH %d \r\nZPROTATE180\r\n", _ph,_pw); end = String.format("PRINT\r\n");}
			if(gap==1){begin = String.format("! 0 200 200 %d 1\r\nPAGE-WIDTH %d \r\nZPROTATE180\r\nGAP-SENSE\r\n", _ph,_pw);end = String.format("FORM\r\nPRINT\r\n");}
		}
		if (r==270)
		{
			if(gap==0){begin = String.format("! 0 200 200 %d 1\r\nPAGE-WIDTH %d \r\nZPROTATE270\r\n", _ph,_pw); end = String.format("PRINT\r\n");}
			if(gap==1){begin = String.format("! 0 200 200 %d 1\r\nPAGE-WIDTH %d \r\nZPROTATE1270\r\nGAP-SENSE\r\n", _ph,_pw);end = String.format("FORM\r\nPRINT\r\n");}
		}

		int total_len=begin.length()+listDataLen+end.length();
		
		totalDataLen=total_len;
		//byte[] _data=new byte[total_len];
		
		int pos=0;
		System.arraycopy(begin.getBytes(),0,totalData,pos,begin.length());
		pos+=begin.length();
		System.arraycopy(listData,0,totalData,pos,listDataLen);
		pos+=listDataLen;
		System.arraycopy(end.getBytes(),0,totalData,pos,end.length());
		listDataLen=0;
	
		return totalData;
	}

	
	public void DrawText(int text_x, int text_y, String text,
			int fontSize, int rotate,int bold, boolean reverse, boolean underline,int location)
	{


		if(location==0)
		{
			add("LEFT\r\n".getBytes());
		}
		if(location==1)
		{
			add("CENTER\r\n".getBytes());
		}
		if(location==2)
		{
			add("RIGHT\r\n".getBytes());
		}
		int f_size=24;
		int f_height=24;
		if(underline){
			
			add("UNDERLINE ON\r\n".getBytes());
		}
		else
		{
			add("UNDERLINE OFF\r\n".getBytes());
		}
		
		String textScale="";
		if(fontSize==16){f_size=55;f_height=16;}
		if(fontSize==20){ f_size=20;f_height=20;}
		if(fontSize==24){ f_size=24;f_height=24;}
		if(fontSize==28){ f_size=28;f_height=28;}
		if(fontSize==32)
		{
			f_size=56;
			f_height=32;
		}
		if(fontSize==40) 
		{
			f_size=20; textScale = String.format("SETMAG %d %d\r\n", 2,2);
			add(textScale.getBytes());
			f_height=40;
		}

		if(fontSize==48) 
		{
			//f_size=8; 
			f_size=24;
			textScale = String.format("SETMAG %d %d\r\n", 2,2);
			add(textScale.getBytes());
			f_height=48;
		}
		if(fontSize==56) 
		{
			f_size=28; textScale = String.format("SETMAG %d %d\r\n",  2,2);

			add(textScale.getBytes());
			f_height=56;
		}
		if(fontSize==64) 
		{
			//f_size=8;
			f_size=56;
			textScale = String.format("SETMAG %d %d\r\n", 2,2);

			add(textScale.getBytes());
			f_height=64;
		}
	
		if(bold==1) 
		{
			add("SETBOLD 1\r\n".getBytes());
		}
		else 
		{
			add("SETBOLD 0\r\n".getBytes());
		}

		String cmd = "T";
		if(rotate==1)cmd="VT";
		if(rotate==2)cmd="T180";
		if(rotate==3)cmd="T270";
		
		String str = String.format("%s %s %s %d %d %s\r\n",cmd,f_size,0,text_x,text_y,text);
		byte[] byteStr=null;
		try {byteStr = str.getBytes("gbk");} catch (UnsupportedEncodingException e) {}
		if(byteStr!=null){add(byteStr);}
		add("SETMAG 0 0 \r\n".getBytes());
		if(reverse)
		{
			byte[] bytetext=null;
			try {bytetext = text.getBytes("gbk");} catch (UnsupportedEncodingException e) {return;}
			if(bytetext==null)return;

			int block_h=f_height;
			int block_w=f_height/2*bytetext.length;
			INVERSE(text_x,text_y,text_x+block_w,text_y,f_height);
		}

		add("LEFT\r\n".getBytes());
		
	

		if(true)return;
	}

	
	

	public void DrawLine(int x0,int y0,int x1,int y1,int width)
	{

		String str = String.format("LINE %d %d %d %d %d\r\n", x0,y0,x1,y1,width);
		   
        add(str.getBytes());	

	
		if(true)return;

	}

	
	public void Drawbox(int x0,int y0,int x1,int y1,int width)
	{
		String str = String.format("BOX %d %d %d %d %d\r\n", x0,y0,x1,y1,width);
        add(str.getBytes());	

			
 
	}
	
	

	public void INVERSE(int x0,int y0,int x1,int y1,int width)
	{
		 strINVERSE = String.format("INVERSE-LINE %d %d %d %d %d\r\n", x0,y0,x1,y1,width);
        add(strINVERSE.getBytes());	

	}

	

	
	
	public void DrawBitmap(Bitmap bmp, int x, int y, boolean rotate)
	{
		int w=bmp.getWidth();
		int h=bmp.getHeight();
		int byteCountW=(w+7)/8;
		int[] bmpData=new int[w*h];

		bmp.copyPixelsToBuffer(IntBuffer.wrap(bmpData));
		
		int y0=0;
		while(y0<h)
		{
			int hh=h-y0;
			if(hh>128)hh=128;
			byte[] outData=new byte[byteCountW*hh];
			for(int yy=y0;yy<y0+hh;yy++)
			{
				for(int xx=0;xx<w;xx++)
				{
					int c=bmpData[yy*w+xx];
					int r=((c>>16)&0xFF);
					int g=((c>>8)&0xFF);
					int b=((c>>0)&0xFF);
					int gray=(r*30+g*59+b*11+50)/100;
					if(gray<0x80)
					{
						outData[byteCountW*(yy-y0)+(xx/8)]|=0x80>>(xx%8);
					}
				}
			}

			String cmd = "CG";
		    String strCmdHeader = String.format("%s %d %d %d %d \n",cmd,byteCountW,hh,x,y0+y);
		    add(strCmdHeader.getBytes());	
		    add(outData);
		   // y0+=hh;
		    y0=hh+y0;
		}

		     
	}
	
	


	
	
	
	public void DrawBarcode1D(String type, int x, int y, String text, int width, int height, int rotate, int location)
	{
		if(location==0)
		{
			add("LEFT\r\n".getBytes());
		}
		if(location==1)
		{
			add("CENTER\r\n".getBytes());
		}
		if(location==2)
		{
			add("RIGHT\r\n".getBytes());
		}
		
		
		String cmd = "BARCODE";
        if(rotate==1){
        	cmd="VBARCODE";}
        String str = String.format("%s %s %d 1 %d %d %d %s\r\n",cmd,type, width,height,x,y,text);
  
        add(str.getBytes());	
    	add("LEFT\r\n".getBytes());
	
	}	
	

	

	
	
	public void DrawBarcodeQRcode(int x, int y, String text, int unitWidth, String level, boolean rotate)
	{
		
		String cmd = "BARCODE";
        if(rotate)cmd="VBARCODE";
        String str = String.format("%s QR %d %d M 2 U %d\r\n",cmd,x,y,unitWidth);
        add(str.getBytes());
        add(String.format("%sA,", level).getBytes());
        byte[] buf = null;
        try { buf = text.getBytes("UTF-8");} catch (UnsupportedEncodingException e) {}
        if(buf!=null){add(buf);
    }
        add("\r\nENDQR\r\n".getBytes());
	
	}
	
	

	public void _DATAMARIX_CODE(int x, int y, int lel, String data)
	{
		String str = String.format("B DATAMATRIX %d %d H %d \r\n",x,y,lel);
		 add(str.getBytes());
	       byte[] buf = null;
	        try { buf = data.getBytes("UTF-8");} catch (UnsupportedEncodingException e) {}
	        if(buf!=null){add(buf);
	    }
	        add("\r\nENDDATAMATRIX\r\n".getBytes());  
	}
	
	public void DrawBitmap1(Bitmap bmp, int x, int y, boolean rotate)
	{
		if(bmp==null)return;
		Bitmap tagetBmp = bmp;
		int byteWidth = (tagetBmp.getWidth()+7)/8;
		byte[] bmpBuf = new byte[byteWidth*tagetBmp.getHeight()];
		
		short[] buf = new short[tagetBmp.getWidth()*tagetBmp.getHeight()];
		ShortBuffer shortBuffer = ShortBuffer.wrap(buf);
		tagetBmp.copyPixelsToBuffer(shortBuffer);
		for(int xx=0;xx<tagetBmp.getWidth();xx++)
        {
        	for(int yy=0;yy<tagetBmp.getHeight();yy++)
        	{
        		if(buf[yy*tagetBmp.getWidth()+xx]==0)bmpBuf[byteWidth*yy+xx/8]|= (0x80>>(xx%8));
        	}
        }

        String cmd = "CG";
        if(rotate)cmd="VCG";
        //String strCmdHeader = String.format("%s %d %d %d %d \n",cmd,byteWidth,tagetBmp.getHeight(),x+left,y+top);
        String strCmdHeader = String.format("%s %d %d %d %d \n",cmd,byteWidth,tagetBmp.getHeight(),x,y);

        
		add(strCmdHeader.getBytes());
        add(bmpBuf);	
		add("\r\n".getBytes());
	}
	
	public void PageFree()
	{
		Clear();
	}

	

}
