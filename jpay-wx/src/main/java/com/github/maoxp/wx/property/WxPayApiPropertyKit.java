package com.github.maoxp.wx.property;

import cn.hutool.core.util.StrUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * <p>微信支付常用配置 Kit</p>
 *
 * @author Javen
 */
public class WxPayApiPropertyKit {

	private static final ThreadLocal<String> TL = new ThreadLocal<>();

	private static final Map<String, WxPayApiProperty> CFG_MAP = new ConcurrentHashMap<>();
	private static final String DEFAULT_CFG_KEY = "_default_key_";

	/**
	 * 添加微信支付配置，每个appId只需添加一次，相同appId将被覆盖
	 *
	 * @param wxPayApiConfig 微信支付配置
	 * @return {WxPayApiProperty} 微信支付配置
	 */
	public static WxPayApiProperty putApiConfig(WxPayApiProperty wxPayApiConfig) {
		return putApiConfig(wxPayApiConfig.getAppId(), wxPayApiConfig);
	}

	public static WxPayApiProperty putApiConfig(String key, WxPayApiProperty wxPayApiConfig) {
		if (CFG_MAP.size() == 0) {
			CFG_MAP.put(DEFAULT_CFG_KEY, wxPayApiConfig);
		}
		return CFG_MAP.put(key, wxPayApiConfig);
	}

	public static WxPayApiProperty setThreadLocalWxPayApiProperty(WxPayApiProperty wxPayApiConfig) {
		return setThreadLocalWxPayApiProperty(wxPayApiConfig.getAppId(), wxPayApiConfig);
	}

	public static WxPayApiProperty setThreadLocalWxPayApiProperty(String key, WxPayApiProperty wxPayApiConfig) {
		if (StrUtil.isEmpty(key)) {
			key = wxPayApiConfig.getAppId();
		}
		setThreadLocalAppId(key);
		return putApiConfig(key, wxPayApiConfig);
	}

	public static WxPayApiProperty removeApiConfig(WxPayApiProperty wxPayApiConfig) {
		return removeApiConfig(wxPayApiConfig.getAppId());
	}

	public static WxPayApiProperty removeApiConfig(String appId) {
		return CFG_MAP.remove(appId);
	}

	public static void setThreadLocalAppId(String appId) {
		if (StrUtil.isEmpty(appId)) {
			appId = CFG_MAP.get(DEFAULT_CFG_KEY).getAppId();
		}
		TL.set(appId);
	}

	public static void removeThreadLocalAppId() {
		TL.remove();
	}

	public static String getAppId() {
		String appId = TL.get();
		if (StrUtil.isEmpty(appId)) {
			appId = CFG_MAP.get(DEFAULT_CFG_KEY).getAppId();
		}
		return appId;
	}

	public static WxPayApiProperty getWxPayApiProperty() {
		String appId = getAppId();
		return getApiConfig(appId);
	}

	public static WxPayApiProperty getApiConfig(String appId) {
		WxPayApiProperty cfg = CFG_MAP.get(appId);
		if (cfg == null) {
			throw new IllegalStateException("需事先调用 WxPayApiPropertyKit.putApiConfig(wxPayApiConfig) 将 appId 对应的 WxPayApiProperty 对象存入，才可以使用 WxPayApiPropertyKit.getWxPayApiProperty() 的系列方法");
		}
		return cfg;
	}
}
