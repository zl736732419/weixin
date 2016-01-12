package com.zheng.weixin.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.zheng.weixin.constant.WeixinConstant;
import com.zheng.weixin.domain.WGroup;
import com.zheng.weixin.kit.JsonKit;
import com.zheng.weixin.kit.MessageKit;
import com.zheng.weixin.kit.WeixinReqKit;
import com.zheng.weixin.service.IWGroupService;

@Service
public class WGroupServiceImpl implements IWGroupService {

	@Override
	public String createWGroup(WGroup group) {
		String url = WeixinConstant.GROUP_CREATE;
		url = MessageKit.replaceUrlAccessToken(url);
		Map<String, WGroup> map = new HashMap<>();
		map.put("group", group);
		String data = JsonKit.obj2Json(map);
		String result = WeixinReqKit.reqPost(url, data, "application/json");
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WGroup> findAllWGroups() {
		String url = WeixinConstant.GROUP_FIND_ALL;
		url = MessageKit.replaceUrlAccessToken(url);
		String result = WeixinReqKit.reqGet(url);
		Map<String, List<Map<String, Object>>> map = JsonKit.json2Obj(result, Map.class);
		List<Map<String, Object>> list = map.get("groups");
		List<WGroup> groups = new ArrayList<>();
		for(Map<String, Object> m : list) {
			WGroup group = new WGroup();
			group.setId(((Double)m.get("id")).intValue());
			group.setName((String)m.get("name"));
			group.setCount(((Double)m.get("count")).intValue());
			groups.add(group);
		}
		
		return groups;
	}

	@SuppressWarnings("unchecked")
	@Override
	public WGroup findUserWGroup(String openId) {
		List<WGroup> groups = findAllWGroups();
		String url = WeixinConstant.GROUP_FIND_USER_GROUP;
		url = MessageKit.replaceUrlAccessToken(url);
		Map<String, Object> map = new HashMap<>();
		map.put("openid", openId);
		String data = JsonKit.obj2Json(map);
		String result = WeixinReqKit.reqPost(url, data, "application/json");
		Map<String, Object> m = JsonKit.json2Obj(result, Map.class);
		Integer gid = (Integer) m.get("groupid");
		WGroup group = null;
		for(WGroup g : groups) {
			if(g.getId() == gid) {
				group = g;
				break;
			}
		}
		
		return group;
	}

	@Override
	public String updateWGroupName(Integer gid, String name) {
		String url = WeixinConstant.GROUP_UPDATE_NAME;
		url = MessageKit.replaceUrlAccessToken(url);
		Map<String, WGroup> map = new HashMap<>();
		WGroup group = new WGroup();
		group.setId(gid);
		group.setName(name);
		map.put("group", group);
		String data = JsonKit.obj2Json(map);
		String result = WeixinReqKit.reqPost(url, data, "application/json");
		
		return result;
	}

	@Override
	public String moveUserToWGroup(String openId, Integer gid) {
		String url = WeixinConstant.GROUP_MOVE_USER_TO_GROUP;
		url = MessageKit.replaceUrlAccessToken(url);
		Map<String, Object> map = new HashMap<>();
		map.put("openid", openId);
		map.put("to_groupid", gid);
		String data = JsonKit.obj2Json(map);
		String result = WeixinReqKit.reqPost(url, data, "application/json");
		return result;
	}

	@Override
	public String moveUsersToWGroup(List<String> openIds, Integer gid) {
		String url = WeixinConstant.GROUP_MOVE_USERS_TO_GROUP;
		url = MessageKit.replaceUrlAccessToken(url);
		Map<String, Object> map = new HashMap<>();
		map.put("openid_list", openIds);
		map.put("to_groupid", gid);
		String data = JsonKit.obj2Json(map);
		String result = WeixinReqKit.reqPost(url, data, "application/json");
		return result;
	}

	@Override
	public String deleteWGroup(Integer gid) {
		String url = WeixinConstant.GROUP_DELETE;
		url = MessageKit.replaceUrlAccessToken(url);
		Map<String, Object> map = new HashMap<>();
		WGroup group = new WGroup();
		group.setId(gid);
		map.put("group", group);
		String data = JsonKit.obj2Json(map);
		String result = WeixinReqKit.reqPost(url, data, "application/json");
		return result;
	}

}
