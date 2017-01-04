package com.blog.web.cache;

import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.blog.web.annotation.CacheHandle;
import com.blog.web.annotation.DelCacheHandle;
import com.blog.web.base.cache.CacheFinal;
import com.blog.web.base.cache.CacheTimerHandler;
import com.blog.web.cache.base.BaseCache;
import com.blog.web.entity.HttpEntity;
import com.blog.web.model.Shell;
import com.blog.web.util.HttpUtil;

@Service
public class ShellCache extends BaseCache {

	@CacheHandle(key=CacheFinal.SHELL_CACHE ,validTime=60)
	public  List<Shell> loadShells(){
		return (List<Shell>) baseService.load(Shell.class);
	}
	@DelCacheHandle(keys={CacheFinal.SHELL_IP_CACHE})
	public  void updateShell(Shell shell){
		baseService.update(shell);
	}
	@CacheHandle(key=CacheFinal.SHELL_IP_CACHE ,validTime=60*60*24)
	public  String getShellIp(String url){
		try {
			URI uri = new URI(url);
			String domain=uri.getHost();
			InetAddress address = InetAddress.getByName(domain);
			return address.getHostAddress().toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	@DelCacheHandle(keys={CacheFinal.SHELL_IP_CACHE})
	public void delShell(Shell shell){
		baseService.delete(shell);
	}
	@CacheHandle(key=CacheFinal.SHELL_HTML_CACHE ,validTime=3)
	public HttpEntity getShellHtml(String url,String postData,String cookie){
		HttpEntity entity=HttpUtil.Post(url,postData,"GBK",cookie);
		if(entity==null){
			return null;
		}
		return entity;
	}
	public static void main(String[] args) throws URISyntaxException {
		System.out.println(new ShellCache().getShellIp("http://blog.51duobei.com/"));
	}
}
