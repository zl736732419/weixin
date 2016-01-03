package com.zheng.weixin.kit;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.zheng.weixin.constant.WeixinConstant;
import com.zheng.weixin.ctx.WeixinContext;
import com.zheng.weixin.json.ErrorResp;
import com.zheng.weixin.json.WeixinMedia;

public class MediaKit {

	/**
	 * 上传临时素材到微信服务器
	 * 
	 * @author zhenglian
	 * @data 2016年1月3日 上午10:17:35
	 * @param path
	 * @param type
	 * @return
	 */
	public static WeixinMedia uploadMedia(String path, String type) {
		String url = WeixinConstant.UPLOAD_MEDIA
				.replaceAll("ACCESS_TOKEN", WeixinContext.getAccessToken())
				.replaceAll("TYPE", type);
		File file = new File(path);
		if(!file.exists()) {
			return null;
		}
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
		//这里构造post file请求参数
		FileBody fb = new FileBody(file);
		HttpEntity entity = MultipartEntityBuilder.create().addPart("media", fb).build();
		post.setEntity(entity);
		WeixinMedia media = null;
		String json = null;
		CloseableHttpResponse resp = null;
		try {
			resp = client.execute(post);
			int sc = resp.getStatusLine().getStatusCode();
			if(sc >= 200 && sc < 300) {
				json = EntityUtils.toString(resp.getEntity());
				media = JsonKit.json2Obj(json, WeixinMedia.class);
			}
		} catch (ParseException | IOException e) {
			e.printStackTrace();
			if(e instanceof ParseException) {
				ErrorResp errmsg = JsonKit.json2Obj(json, ErrorResp.class);
				System.out.println(errmsg.getErrcode() + "," + errmsg.getErrmsg());
			}
		}finally {
			if(resp != null) {
				try {
					resp.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(client != null) {
				try {
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return media;
	}
	
	/**
	 * 根据mediaId获取微信服务器上的临时素材信息
	 *
	 * @author zhenglian
	 * @data 2016年1月3日 上午10:48:07
	 * @param mediaId
	 */
	public static void getMedia(String mediaId, File file) {
		String url = WeixinConstant.DOWNLOAD_MEDIA
				.replaceAll("ACCESS_TOKEN", WeixinContext.getAccessToken())
				.replaceAll("MEDIA_ID", mediaId);
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet get = new HttpGet(url);
		CloseableHttpResponse resp = null;
		try {
			resp = client.execute(get);
			int sc = resp.getStatusLine().getStatusCode();
			if(sc >= 200 && sc < 300) {
				InputStream input = resp.getEntity().getContent();
				FileUtils.copyInputStreamToFile(input, file);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(resp != null) {
				try {
					resp.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(client != null) {
				try {
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
