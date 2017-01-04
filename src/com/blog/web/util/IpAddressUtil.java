package com.blog.web.util;

import java.text.MessageFormat;

import com.blog.web.entity.HttpEntity;
import com.blog.web.entity.IpAddressEntity;

public class IpAddressUtil {
	public static IpAddressEntity.AddressInfo getAddress(String ip){
		String url="http://ip.taobao.com/service/getIpInfo.php?ip={0}";
		url=MessageFormat.format(url, ip);
		HttpEntity entity=HttpUtil.Get(url);
		IpAddressEntity address=(IpAddressEntity) ReqJsonUtil.jsonToObject(entity.getHtml(), IpAddressEntity.class);
		if(StringUtils.isNullOrEmpty(address.getData())){
			return null;
		}
		return address.getData();
	}
	public static void main(String[] args) {
	}
}
