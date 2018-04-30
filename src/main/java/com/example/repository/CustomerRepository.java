package com.example.repository;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Customer;

@Repository
@Transactional
public class CustomerRepository {
    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    SimpleJdbcInsert insert;

    @PostConstruct
    public void init() {
        insert = new SimpleJdbcInsert((JdbcTemplate) jdbcTemplate.getJdbcTemplate())
                .withTableName("customers")
                .usingGeneratedKeyColumns("id");
    }

    private static final RowMapper<Customer> customerRowmapper = (rs, i) ->
        {
            Integer id = rs.getInt("id");
            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");
            return new Customer(id, firstName, lastName);
        };

    public List<Customer> findAll() {
        List<Customer> customers = jdbcTemplate.query(
                "SELECT id, first_name, last_name FROM customers ORDER BY id",
                customerRowmapper);
        return customers;
    }

    public Customer findOne(Integer id) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
        return jdbcTemplate.queryForObject(
                "SELECT id, first_name, last_name FROM customers WHERE id = :id",
                param,
                customerRowmapper);
    }

    public Page<Customer> findAll(Pageable pageable) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    public Customer save(Customer customer) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(customer);
            if(customer.getId() == null) {
                Number key = insert.executeAndReturnKey(param);
                customer.setId(key.intValue());
            } else {
                jdbcTemplate.update("UPDATE cutomers SET first_name=:firstName, last_name=:lastName WHERE id=:id", param);
            }
            return customer;
    }

    public void delete(Integer id) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
        jdbcTemplate.update("DELETE FROM customers WHERE id=:id", param);
    }

}
