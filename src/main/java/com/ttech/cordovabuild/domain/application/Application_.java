package com.ttech.cordovabuild.domain.application;

import com.ttech.cordovabuild.domain.BuildInfo;
import com.ttech.cordovabuild.domain.user.User;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-08-01T23:27:22.232+0300")
@StaticMetamodel(Application.class)
public class Application_ {
	public static volatile SingularAttribute<Application, Long> id;
	public static volatile SingularAttribute<Application, String> name;
	public static volatile SingularAttribute<Application, Date> created;
	public static volatile ListAttribute<Application, BuildInfo> builds;
	public static volatile SingularAttribute<Application, User> owner;
	public static volatile SingularAttribute<Application, Boolean> deleted;
	public static volatile SingularAttribute<Application, String> sourceURI;
	public static volatile SingularAttribute<Application, ApplicationConfig> applicationConfig;
}
