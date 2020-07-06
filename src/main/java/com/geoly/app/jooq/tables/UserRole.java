/*
 * This file is generated by jOOQ.
 */
package com.geoly.app.jooq.tables;


import com.geoly.app.jooq.Geoly;
import com.geoly.app.jooq.Indexes;
import com.geoly.app.jooq.Keys;
import com.geoly.app.jooq.tables.records.UserRoleRecord;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.5"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class UserRole extends TableImpl<UserRoleRecord> {

    private static final long serialVersionUID = -698632129;

    /**
     * The reference instance of <code>geoly.USER_ROLE</code>
     */
    public static final UserRole USER_ROLE = new UserRole();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<UserRoleRecord> getRecordType() {
        return UserRoleRecord.class;
    }

    /**
     * The column <code>geoly.USER_ROLE.ID</code>.
     */
    public final TableField<UserRoleRecord, Integer> ID = createField("ID", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>geoly.USER_ROLE.CREATED_AT</code>.
     */
    public final TableField<UserRoleRecord, Timestamp> CREATED_AT = createField("CREATED_AT", org.jooq.impl.SQLDataType.TIMESTAMP.precision(6), this, "");

    /**
     * The column <code>geoly.USER_ROLE.ROLE_ID</code>.
     */
    public final TableField<UserRoleRecord, Integer> ROLE_ID = createField("ROLE_ID", org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * The column <code>geoly.USER_ROLE.USER_ID</code>.
     */
    public final TableField<UserRoleRecord, Integer> USER_ID = createField("USER_ID", org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * Create a <code>geoly.USER_ROLE</code> table reference
     */
    public UserRole() {
        this(DSL.name("USER_ROLE"), null);
    }

    /**
     * Create an aliased <code>geoly.USER_ROLE</code> table reference
     */
    public UserRole(String alias) {
        this(DSL.name(alias), USER_ROLE);
    }

    /**
     * Create an aliased <code>geoly.USER_ROLE</code> table reference
     */
    public UserRole(Name alias) {
        this(alias, USER_ROLE);
    }

    private UserRole(Name alias, Table<UserRoleRecord> aliased) {
        this(alias, aliased, null);
    }

    private UserRole(Name alias, Table<UserRoleRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> UserRole(Table<O> child, ForeignKey<O, UserRoleRecord> key) {
        super(child, key, USER_ROLE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return Geoly.GEOLY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.FK859N2JVI8IVHUI0RL0ESWS6O_INDEX_B, Indexes.FKA68196081FVOVJHKEK5M97N3Y_INDEX_B, Indexes.PRIMARY_KEY_B);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<UserRoleRecord> getPrimaryKey() {
        return Keys.CONSTRAINT_B;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<UserRoleRecord>> getKeys() {
        return Arrays.<UniqueKey<UserRoleRecord>>asList(Keys.CONSTRAINT_B);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ForeignKey<UserRoleRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<UserRoleRecord, ?>>asList(Keys.FKA68196081FVOVJHKEK5M97N3Y, Keys.FK859N2JVI8IVHUI0RL0ESWS6O);
    }

    public Role role() {
        return new Role(this, Keys.FKA68196081FVOVJHKEK5M97N3Y);
    }

    public User user() {
        return new User(this, Keys.FK859N2JVI8IVHUI0RL0ESWS6O);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserRole as(String alias) {
        return new UserRole(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserRole as(Name alias) {
        return new UserRole(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public UserRole rename(String name) {
        return new UserRole(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public UserRole rename(Name name) {
        return new UserRole(name, null);
    }
}
