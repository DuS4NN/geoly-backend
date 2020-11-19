/*
 * This file is generated by jOOQ.
 */
package com.geoly.app.jooq.tables;


import com.geoly.app.jooq.Geoly;
import com.geoly.app.jooq.Indexes;
import com.geoly.app.jooq.Keys;
import com.geoly.app.jooq.tables.records.TokenRecord;

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
public class Token extends TableImpl<TokenRecord> {

    private static final long serialVersionUID = 1850007509;

    /**
     * The reference instance of <code>geoly.TOKEN</code>
     */
    public static final Token TOKEN = new Token();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<TokenRecord> getRecordType() {
        return TokenRecord.class;
    }

    /**
     * The column <code>geoly.TOKEN.ID</code>.
     */
    public final TableField<TokenRecord, Integer> ID = createField("ID", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>geoly.TOKEN.ACTION</code>.
     */
    public final TableField<TokenRecord, String> ACTION = createField("ACTION", org.jooq.impl.SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>geoly.TOKEN.CREATED_AT</code>.
     */
    public final TableField<TokenRecord, Timestamp> CREATED_AT = createField("CREATED_AT", org.jooq.impl.SQLDataType.TIMESTAMP.precision(6), this, "");

    /**
     * The column <code>geoly.TOKEN.TOKEN</code>.
     */
    public final TableField<TokenRecord, String> TOKEN_ = createField("TOKEN", org.jooq.impl.SQLDataType.VARCHAR(100), this, "");

    /**
     * The column <code>geoly.TOKEN.USER_ID</code>.
     */
    public final TableField<TokenRecord, Integer> USER_ID = createField("USER_ID", org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * Create a <code>geoly.TOKEN</code> table reference
     */
    public Token() {
        this(DSL.name("TOKEN"), null);
    }

    /**
     * Create an aliased <code>geoly.TOKEN</code> table reference
     */
    public Token(String alias) {
        this(DSL.name(alias), TOKEN);
    }

    /**
     * Create an aliased <code>geoly.TOKEN</code> table reference
     */
    public Token(Name alias) {
        this(alias, TOKEN);
    }

    private Token(Name alias, Table<TokenRecord> aliased) {
        this(alias, aliased, null);
    }

    private Token(Name alias, Table<TokenRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> Token(Table<O> child, ForeignKey<O, TokenRecord> key) {
        super(child, key, TOKEN);
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
        return Arrays.<Index>asList(Indexes.FKCR55UO5IEX6L1HS2XQP94G2SS_INDEX_4, Indexes.PRIMARY_KEY_4C);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<TokenRecord> getPrimaryKey() {
        return Keys.CONSTRAINT_4C;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<TokenRecord>> getKeys() {
        return Arrays.<UniqueKey<TokenRecord>>asList(Keys.CONSTRAINT_4C);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ForeignKey<TokenRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<TokenRecord, ?>>asList(Keys.FKCR55UO5IEX6L1HS2XQP94G2SS);
    }

    public User user() {
        return new User(this, Keys.FKCR55UO5IEX6L1HS2XQP94G2SS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Token as(String alias) {
        return new Token(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Token as(Name alias) {
        return new Token(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Token rename(String name) {
        return new Token(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Token rename(Name name) {
        return new Token(name, null);
    }
}
