package com.sewage.springboot.controller;

import com.alibaba.fastjson.JSONObject;
import com.sewage.springboot.service.UserService;
import com.sewage.springboot.util.CommonUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
	@Autowired
	private UserService userService;

	/**
	 * 查询用户列表
	 */
	@RequiresPermissions("admin")
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public JSONObject listUser(HttpServletRequest request) {
		return userService.listUser(CommonUtil.request2Json(request));
	}

	/**
	 * 新增用户(由管理员分配)
	 */
	@RequiresPermissions("admin")
	@PostMapping("/addUser")
	public JSONObject addUser(@RequestBody JSONObject userJson) {
		String pwd = userJson.getString("password");
		// 加盐 存入数据库
		String salt = new SecureRandomNumberGenerator().nextBytes().toHex();
		// 对加入用户的密码进行md5加密
		String ans = new Md5Hash(pwd, salt, 2).toString();
		userJson.put("password", ans);
		userJson.put("salt", salt);
		return userService.addUser(userJson);
	}

	@RequiresPermissions("admin")
	@PostMapping("/updateUser")
	public JSONObject updateUser(@RequestBody JSONObject requestJson) {
		//CommonUtil.hasAllRequired(requestJson, "roleId, deleteStatus, userId");
		return userService.updateUser(requestJson);
	}

	/**
	 * 角色列表
	 */
//	@RequiresPermissions("role:list")
	@GetMapping("/listRole")
	public JSONObject listRole() {
		//System.out.println(userService.listRole());
		return userService.listRole();
	}

	/**
	 * 查询所有权限, 给角色分配权限时调用
	 */
//	@RequiresPermissions("role:list")
	@GetMapping("/listAllPermission")
	public JSONObject listAllPermission() {
		//System.out.println(userService.listAllPermission());
		return userService.listAllPermission();
	}

	/**
	 * 新增角色
	 */
//	@RequiresPermissions("role:add")
	@PostMapping("/addRole")
	public JSONObject addRole(@RequestBody JSONObject requestJson) {
		//CommonUtil.hasAllRequired(requestJson, "roleName,permissions");
		return userService.addRole(requestJson);
	}

	/**
	 * 修改角色
	 */
//	@RequiresPermissions("role:update")
	@PostMapping("/updateRole")
	public JSONObject updateRole(@RequestBody JSONObject requestJson) {
		//CommonUtil.hasAllRequired(requestJson, "roleId,roleName,permissions");
		return userService.updateRole(requestJson);
	}

	/**
	 * 删除角色
	 */
//	@RequiresPermissions("role:delete")
	@PostMapping("/deleteRole")
	public JSONObject deleteRole(@RequestBody JSONObject requestJson) {
		//CommonUtil.hasAllRequired(requestJson, "roleId");
		return userService.deleteRole(requestJson);
	}
}
