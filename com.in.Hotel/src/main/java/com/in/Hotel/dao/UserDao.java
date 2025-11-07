package com.in.Hotel.dao;

import com.in.Hotel.POJO.User;
import com.in.Hotel.wrapper.UserWrapper;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import java.beans.Transient;
import java.util.List;

public interface UserDao extends JpaRepository<User, Long> {

User findByEmailId(@Param("email")String email);

List<UserWrapper> getAllUser();
List<String>getAllAdmin();

@Transactional
@Modifying
Integer updateStatus(@Param("status")String status,@Param("id")Long id);

User findByEmail(String email);
}

