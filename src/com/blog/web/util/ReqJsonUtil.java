package com.blog.web.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

/**
 * 客户端请求处理类
 * @author WUJB
 *
 */
@Service("reqJsonUtil")
public class ReqJsonUtil {
	/**
	 * 获取客户端的json请求内容(此方法不作URLDecoder的UTF-8解码，用于登录使用)
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public  String getJsonContentNoDecode(HttpServletRequest request){
		// 读取请求内容
		InputStream i = null;
		StringBuilder sb = null;
		String str = null;
		try {
			i =request.getInputStream();
			int a = 0;
			byte[] bytes = new byte[2048];
			sb = new StringBuilder();
			while ((a = i.read(bytes)) != -1) {
				sb.append(new String(bytes,0,a,"utf-8"));
			}
			str = sb.toString();
		} catch (IOException e) {
			
		}
		return str;
	}
	
	/**
	 * 获取POST请求参数中数据
	 * 
	 * @param request
	 * @throws IOException
	 */
	public  String getPostContent(HttpServletRequest request){
		String content = null;
		try {
			content = IOUtils.toString(request.getInputStream(), request
					.getCharacterEncoding());
		} catch (Exception e) {
			
		}
		return content;
	}
	public  String readServletInputStream(HttpServletRequest req) {

		String content = null;
		try {
		ServletInputStream sis = req.getInputStream();
		byte[] b = new byte[1024];
		int count = -1;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while ((count = sis.read(b)) != -1) {
		baos.write(b, 0, count);
		}
		baos.flush();
		content = baos.toString("UTF-8");
		baos.close();
		sis.close();
		} catch (IOException e) {
		
		}
		return content;
		}

		public  void writeServletOutputStream(HttpServletResponse resp, String content) {

		try {
		ServletOutputStream  out=resp.getOutputStream();
		OutputStreamWriter sos = new OutputStreamWriter(out,"UTF-8");  
		sos.write(content);
		sos.close();
		} catch (IOException e) {
		
		}
		}
		/**
		 * 获取客户端的json请求内容
		 * @param request
		 * @return
		 * @throws UnsupportedEncodingException 
		 */
		public static String getJsonContent(HttpServletRequest request){
			InputStream i = null;
			StringBuilder sb = null;
			String str = null;
			try {
				i =request.getInputStream();
				int a = 0;
				byte[] bytes = new byte[2048];
				sb = new StringBuilder();
				while ((a = i.read(bytes)) != -1) {
					sb.append(new String(bytes,0,a,"utf-8"));
				}
				str = URLDecoder.decode(sb.toString(),"utf-8");
			} catch (IOException e) {
				
			}
			return str;//sb.toString();
		}
		/**
		 * 直接将客户端请求转换成对象
		 * @param request
		 * @param cla
		 * @return
		 */
		public static Object changeToObject(HttpServletRequest request, Class cla) {
			Object object = null;
			try {
				String jsonContent = getJsonContent(request);
				Gson gson = new Gson();
				object = gson.fromJson(jsonContent, cla);
			} catch (Exception e) {
				
			}
			return object;
		}
		
		/**
		 * json转换成对象
		 * @param jsonContent
		 * @param cla
		 * @return
		 */
		public static Object jsonToObject(String jsonContent, Class cla) {
			if(jsonContent==null)return null;
			Object object = null;
			try {
				Gson gson = new Gson();
				object = gson.fromJson(jsonContent, cla);
			} catch (Exception e) {
				
			}
			return object;
		}
		
		public static String objectToJson(Object obj){
			Gson gson = new Gson();
			return gson.toJson(obj);
		}
		/**
		 * json返回数据
		 * @param returnCode
		 * @param msg
		 * @return
		 * @throws IOException
		 * @throws JSONException 
		 */
		public static String jsonResult(int returnCode, String msg){
	       try {
			JSONObject json = new JSONObject();
			json.put("returnCode", returnCode);//响应码
			json.put("msg", msg);//描述
			return json.toString();
	       } catch (Exception e) {
	    	   
			   return null;
	       }
	    }
		 private static String SPACE = "   "; 
			public static String jsonFormat(String json)  
		    {  
		        StringBuffer result = new StringBuffer();  
		          
		        int length = json.length();  
		        int number = 0;  
		        char key = 0;  
		          
		        //遍历输入字符串。  
		        for (int i = 0; i < length; i++)  
		        {  
		            //1、获取当前字符。  
		            key = json.charAt(i);  
		              
		            //2、如果当前字符是前方括号、前花括号做如下处理：  
		            if((key == '[') || (key == '{') )  
		            {  
		                //（1）如果前面还有字符，并且字符为“：”，打印：换行和缩进字符字符串。  
		                if((i - 1 > 0) && (json.charAt(i - 1) == ':'))  
		                {  
		                    result.append('\n');  
		                    result.append(indent(number));  
		                }  
		                  
		                //（2）打印：当前字符。  
		                result.append(key);  
		                  
		                //（3）前方括号、前花括号，的后面必须换行。打印：换行。  
		                result.append('\n');  
		                  
		                //（4）每出现一次前方括号、前花括号；缩进次数增加一次。打印：新行缩进。  
		                number++;  
		                result.append(indent(number));  
		                  
		                //（5）进行下一次循环。  
		                continue;  
		            }  
		              
		            //3、如果当前字符是后方括号、后花括号做如下处理：  
		            if((key == ']') || (key == '}') )  
		            {  
		                //（1）后方括号、后花括号，的前面必须换行。打印：换行。  
		                result.append('\n');  
		                  
		                //（2）每出现一次后方括号、后花括号；缩进次数减少一次。打印：缩进。  
		                number--;  
		                result.append(indent(number));  
		                  
		                //（3）打印：当前字符。  
		                result.append(key);  
		                  
		                //（4）如果当前字符后面还有字符，并且字符不为“，”，打印：换行。  
		                if(((i + 1) < length) && (json.charAt(i + 1) != ','))  
		                {  
		                    result.append('\n');  
		                }  
		                  
		                //（5）继续下一次循环。  
		                continue;  
		            }  
		              
		            //4、如果当前字符是逗号。逗号后面换行，并缩进，不改变缩进次数。  
		            if((key == ','))  
		            {  
		                result.append(key);  
		                result.append('\n');  
		                result.append(indent(number));  
		                continue;  
		            }  
		              
		            //5、打印：当前字符。  
		            result.append(key);  
		        }  
		          
		        return result.toString();  
		    }  
		      
		    /** 
		     * 返回指定次数的缩进字符串。每一次缩进三个空格，即SPACE。 
		     *  
		     * @param number 缩进次数。 
		     * @return 指定缩进次数的字符串。 
		     */  
		    private static String indent(int number)  
		    {  
		        StringBuffer result = new StringBuffer();  
		        for(int i = 0; i < number; i++)  
		        {  
		            result.append(SPACE);  
		        }  
		        return result.toString();  
		    }  
}
