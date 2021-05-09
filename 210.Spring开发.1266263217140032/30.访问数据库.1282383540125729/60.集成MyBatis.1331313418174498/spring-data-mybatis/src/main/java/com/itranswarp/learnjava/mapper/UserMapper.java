package com.itranswarp.learnjava.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.itranswarp.learnjava.entity.User;

public interface UserMapper {

	@Select("SELECT * FROM users WHERE id = #{id}")
	User getById(@Param("id") long id);

	@Select("SELECT * FROM users WHERE email = #{email}")
	User getByEmail(@Param("email") String email);

	@Select("SELECT * FROM users LIMIT #{offset}, #{maxResults}")
	List<User> getAll(@Param("offset") int offset, @Param("maxResults") int maxResults);

	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	@Insert("INSERT INTO users (email, password, name, createdAt) VALUES (#{user.email}, #{user.password}, #{user.name}, #{user.createdAt})")
	void insert(@Param("user") User user);

	@Update("UPDATE users SET name = #{user.name}, createdAt = #{user.createdAt} WHERE id = #{user.id}")
	void update(@Param("user") User user);

	@Delete("DELETE FROM users WHERE id = #{id}")
	void deleteById(@Param("id") long id);
}
