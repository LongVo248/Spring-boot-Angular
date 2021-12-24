package com.example.springbootangular.Repository;

import com.example.springbootangular.Model.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserPageRepository extends PagingAndSortingRepository<User,String>, JpaSpecificationExecutor<User>, CrudRepository<User, String> {
}
