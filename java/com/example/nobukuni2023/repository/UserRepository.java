package com.example.nobukuni2023.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nobukuni2023.entity.User;
import com.example.nobukuni2023.form.UserEditForm;
public interface UserRepository extends JpaRepository<User , Integer>{
	public User findByEmail(String email);
	public void save(String subId);
	public void save(UserEditForm userEgitForm);
}
