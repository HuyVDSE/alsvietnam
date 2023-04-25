package com.alsvietnam.repository;

import com.alsvietnam.entities.User;
import com.alsvietnam.models.search.ParameterSearchUser;
import com.alsvietnam.models.wrapper.ListWrapper;

/**
 * Duc_Huy
 * Date: 9/6/2022
 * Time: 11:09 PM
 */
public interface UserRepositoryCustom {

    ListWrapper<User> searchUsers(ParameterSearchUser parameterSearchUser);

}
