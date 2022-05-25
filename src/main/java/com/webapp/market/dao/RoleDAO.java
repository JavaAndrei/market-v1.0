package com.webapp.market.dao;

import com.webapp.market.dao.entities.Role;
import com.webapp.market.exceptions.UserDataFormatException;

import java.util.List;

public interface RoleDAO {

    //boolean insertRole(Role role) throws UserDataFormatException;
    Role findRole(Role role) throws UserDataFormatException;
    //boolean updateRole(Role role);
    //boolean deleteRole(Role role) throws UserDataFormatException;
    List<Role> selectRoleTO(Role role) throws UserDataFormatException;

}
