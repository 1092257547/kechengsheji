package com.example.mentalhealth.Service;

import com.example.mentalhealth.entities.Permission;
import com.example.mentalhealth.mapper.PermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class PermissionService {
    @Autowired
    public PermissionMapper permissionMapper;

    public Permission queryPermissionById(Integer pId){
       return permissionMapper.queryPermissionById(pId);
    }

    public List<Permission> queryAllPermissions(){
        return permissionMapper.queryAllPermissions();
    }


}
