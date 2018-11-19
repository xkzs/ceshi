package com.blb.action;



import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blb.entity.Terminal;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value="selectController",tags={"用户查询接口"})
@RestController
@RequestMapping(value = "/hello1")
public class UserController {

	@ApiOperation(value="获取用户详细信息", notes="根据url的id来获取用户详细信息",produces = "application/json")
	// ApiResponses 增加返回结果的描述
	@ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long")
	@RequestMapping(value = "/{id}",method=RequestMethod.GET)
	public Terminal test1(@PathVariable Long id){
		Terminal t = new Terminal();
		t.setLabel("dscav");
		t.setName("cdsafvares");
		t.setId(id);
		return t;
	}
	
}
