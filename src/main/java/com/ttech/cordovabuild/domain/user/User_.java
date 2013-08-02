package com.ttech.cordovabuild.domain.user;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-08-01T22:16:46.622+0300")
@StaticMetamodel(User.class)
public class User_ {
	public static volatile SingularAttribute<User, Long> id;
	public static volatile SingularAttribute<User, String> name;
	public static volatile SingularAttribute<User, String> surname;
	public static volatile SingularAttribute<User, String> email;
	public static volatile SingularAttribute<User, String> username;
	public static volatile SetAttribute<User, Role> roles;
	public static volatile SingularAttribute<User, String> password;
	public static volatile SingularAttribute<User, Boolean> accountNonExpired;
	public static volatile SingularAttribute<User, Boolean> enabled;
	public static volatile SingularAttribute<User, Boolean> credentialsNonExpired;
	public static volatile SingularAttribute<User, Boolean> accountNonLocked;
}
