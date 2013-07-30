
package com.ttech.cordovabuild.domain.user;

import java.util.List;

/**
 *
 * @author capacman
 */
public interface UserRepository {

    User findUserByID(Long id);

    User findUserByUserName(String username);

    User findUserByEmail(String email);

    Role saveRole(Role role);

    User saveOrUpdateUser(User user);

    List<Role> getActiveRoles();
}
