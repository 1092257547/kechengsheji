package com.example.mentalhealth.Service;

import com.example.mentalhealth.entities.Role;
import com.example.mentalhealth.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    @Autowired
    public RoleMapper roleMapper;

    public List<Role> queryRoleByIds(Integer rId){
        return roleMapper.queryRoleByIds(rId);
    }


}
