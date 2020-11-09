package com.madmax.campaign.repository;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.madmax.campaign.models.Connection;

public interface ConnectionRepository extends JpaRepository<Connection, Long> {

	@Query("from Connection as c where c.user.userid =:userid")
	public Page<Connection> findConnectionsByUser(@Param("userid")long userid,Pageable pageable);
}
