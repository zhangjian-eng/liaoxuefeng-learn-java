package com.itranswarp.learnjava.dao;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public abstract class AbstractDao<T> extends JdbcDaoSupport {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private String table;
	private Class<T> entityClass;
	private RowMapper<T> rowMapper;

	public AbstractDao() {
		this.entityClass = getParameterizedType();
		this.table = this.entityClass.getSimpleName().toLowerCase() + "s";
		this.rowMapper = new BeanPropertyRowMapper<>(entityClass);
	}

	@PostConstruct
	public void init() {
		super.setJdbcTemplate(jdbcTemplate);
	}

	public T getById(long id) {
		return getJdbcTemplate().queryForObject("SELECT * FROM " + table + " WHERE id = ?", this.rowMapper, id);
	}

	public List<T> getAll(int pageIndex) {
		int limit = 100;
		int offset = limit * (pageIndex - 1);
		return getJdbcTemplate().query("SELECT * FROM " + table + " LIMIT ? OFFSET ?", new Object[] { limit, offset },
				this.rowMapper);
	}

	public void deleteById(long id) {
		getJdbcTemplate().update("DELETE FROM " + table + " WHERE id = ?", id);
	}

	public RowMapper<T> getRowMapper() {
		return this.rowMapper;
	}

	@SuppressWarnings("unchecked")
	private Class<T> getParameterizedType() {
		Type type = getClass().getGenericSuperclass();
		if (!(type instanceof ParameterizedType)) {
			throw new IllegalArgumentException("Class " + getClass().getName() + " does not have parameterized type.");
		}
		ParameterizedType pt = (ParameterizedType) type;
		Type[] types = pt.getActualTypeArguments();
		if (types.length != 1) {
			throw new IllegalArgumentException(
					"Class " + getClass().getName() + " has more than 1 parameterized types.");
		}
		Type r = types[0];
		if (!(r instanceof Class<?>)) {
			throw new IllegalArgumentException(
					"Class " + getClass().getName() + " does not have parameterized type of class.");
		}
		return (Class<T>) r;
	}
}
