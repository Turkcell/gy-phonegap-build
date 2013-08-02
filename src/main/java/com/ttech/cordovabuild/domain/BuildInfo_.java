package com.ttech.cordovabuild.domain;

import com.ttech.cordovabuild.domain.BuildInfo.Status;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-08-01T22:16:46.620+0300")
@StaticMetamodel(BuildInfo.class)
public class BuildInfo_ {
	public static volatile SingularAttribute<BuildInfo, Long> id;
	public static volatile SingularAttribute<BuildInfo, Date> startDate;
	public static volatile SingularAttribute<BuildInfo, Date> finishDate;
	public static volatile SingularAttribute<BuildInfo, String> sourceURI;
	public static volatile SingularAttribute<BuildInfo, Status> status;
	public static volatile ListAttribute<BuildInfo, BuilTarget> buildTargets;
}
