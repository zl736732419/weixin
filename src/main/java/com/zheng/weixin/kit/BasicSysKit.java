package com.zheng.weixin.kit;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;

public class BasicSysKit {
	/**
	 * 把(22)(33)(55)取出形成List
	 * @param str
	 * @return
	 */
	public static List<Integer> braceStr2List(String str) {
		Pattern pattern = Pattern.compile("(\\d+)");
		Matcher m = pattern.matcher(str);
		List<Integer> list = new ArrayList<Integer>();
		while(m.find()) {
			list.add(Integer.parseInt(m.group()));
		}
		return list;
	}
	
	@SuppressWarnings("rawtypes")
	public static boolean isEmpty(Object obj) {
		if(obj==null) return true;
		if(obj instanceof String) {
			if("".equals(obj)) return true;
		}
		if(obj instanceof Collection<?>) {
			if(((Collection) obj).size()>0) return true;
		}
		return false;
	}
	
	public static <N extends Object>void mergeList(List<N> baseList,List<N> mergeList) {
		for(N o:mergeList) {
			if(!baseList.contains(o)) {
				baseList.add(o);
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static List<Class> listByClass(String p) {
		try {
			List<Class> clzs = new ArrayList<Class>();
			String packagePath = p.replace(".", "/");
			URL url = Thread.currentThread().getContextClassLoader().getResource(packagePath);
			File file = new File(url.getPath());
			if(!file.exists()) throw new RuntimeException("初始化的包名路径不正确");
			File []fs = file.listFiles(new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					if(pathname.getName().endsWith(".class")) return true;
					return false;
				}
			});
			for(File f:fs) {
				String cname = p+"."+FilenameUtils.getBaseName(f.getName());
				Class clz = Class.forName(cname);
				clzs.add(clz);
			}
			return clzs;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<Class> listByClassAnnotation(String p,Class an) {
		try {
			List<Class> clzs = new ArrayList<Class>();
			String packagePath = p.replace(".", "/");
			URL url = Thread.currentThread().getContextClassLoader().getResource(packagePath);
			File file = new File(url.getPath());
			if(!file.exists()) throw new RuntimeException("初始化的包名路径不正确");
			File []fs = file.listFiles(new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					if(pathname.getName().endsWith(".class")) return true;
					return false;
				}
			});
			for(File f:fs) {
				String cname = p+"."+FilenameUtils.getBaseName(f.getName());
				Class clz = Class.forName(cname);
				if(clz.isAnnotationPresent(an))
					clzs.add(clz);
			}
			return clzs;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 将字符串str进行sha1加密操作
	 *
	 * @author zhenglian
	 * @data 2015年12月27日 下午4:35:21
	 * @param str
	 * @return
	 */
	public static String sha1(String str) {
		StringBuilder builder = new StringBuilder();
		try {
			MessageDigest md = MessageDigest.getInstance("sha1");
			md.update(str.getBytes());
			byte[] bytes = md.digest();
			String s = null;
			for(byte b : bytes) {
				s = String.format("%02x", b);
				builder.append(s);
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return builder.toString();
	}
	
}
