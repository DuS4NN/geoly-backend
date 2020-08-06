/*
 * This file is generated by jOOQ.
 */
package com.geoly.app.jooq.tables;


import com.geoly.app.jooq.Geoly;
import com.geoly.app.jooq.Indexes;
import com.geoly.app.jooq.Keys;
import com.geoly.app.jooq.tables.records.UserReportRecord;

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
public class UserReport extends TableImpl<UserReportRecord> {

    private static final long serialVersionUID = -1212470748;

    /**
     * The reference instance of <code>geoly.USER_REPORT</code>
     */
    public static final UserReport USER_REPORT = new UserReport();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<UserReportRecord> getRecordType() {
        return UserReportRecord.class;
    }

    /**
     * The column <code>geoly.USER_REPORT.ID</code>.
     */
    public final TableField<UserReportRecord, Integer> ID = createField("ID", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>geoly.USER_REPORT.CREATED_AT</code>.
     */
    public final TableField<UserReportRecord, Timestamp> CREATED_AT = createField("CREATED_AT", org.jooq.impl.SQLDataType.TIMESTAMP.precision(6), this, "");

    /**
     * The column <code>geoly.USER_REPORT.REASON</code>.
     */
    public final TableField<UserReportRecord, String> REASON = createField("REASON", org.jooq.impl.SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>geoly.USER_REPORT.COMPLAINANT</code>.
     */
    public final TableField<UserReportRecord, Integer> COMPLAINANT = createField("COMPLAINANT", org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * The column <code>geoly.USER_REPORT.REPORTED</code>.
     */
    public final TableField<UserReportRecord, Integer> REPORTED = createField("REPORTED", org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * Create a <code>geoly.USER_REPORT</code> table reference
     */
    public UserReport() {
        this(DSL.name("USER_REPORT"), null);
    }

    /**
     * Create an aliased <code>geoly.USER_REPORT</code> table reference
     */
    public UserReport(String alias) {
        this(DSL.name(alias), USER_REPORT);
    }

    /**
     * Create an aliased <code>geoly.USER_REPORT</code> table reference
     */
    public UserReport(Name alias) {
        this(alias, USER_REPORT);
    }

    private UserReport(Name alias, Table<UserReportRecord> aliased) {
        this(alias, aliased, null);
    }

    private UserReport(Name alias, Table<UserReportRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> UserReport(Table<O> child, ForeignKey<O, UserReportRecord> key) {
        super(child, key, USER_REPORT);
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
        return Arrays.<Index>asList(Indexes.FK2CM7CI55E41B6EAKUNXYDH3LG_INDEX_1, Indexes.FKO1LNULINQCBVABO3W4NI3K4D1_INDEX_1, Indexes.PRIMARY_KEY_11, Indexes.UKPT3UYBUWE0QPIJ09B9LHQV52S_INDEX_1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<UserReportRecord> getPrimaryKey() {
        return Keys.CONSTRAINT_11;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<UserReportRecord>> getKeys() {
        return Arrays.<UniqueKey<UserReportRecord>>asList(Keys.CONSTRAINT_11, Keys.UKPT3UYBUWE0QPIJ09B9LHQV52S);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ForeignKey<UserReportRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<UserReportRecord, ?>>asList(Keys.FK2CM7CI55E41B6EAKUNXYDH3LG, Keys.FKO1LNULINQCBVABO3W4NI3K4D1);
    }

    public User fk2cm7ci55e41b6eakunxydh3lg() {
        return new User(this, Keys.FK2CM7CI55E41B6EAKUNXYDH3LG);
    }

    public User fko1lnulinqcbvabo3w4ni3k4d1() {
        return new User(this, Keys.FKO1LNULINQCBVABO3W4NI3K4D1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserReport as(String alias) {
        return new UserReport(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserReport as(Name alias) {
        return new UserReport(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public UserReport rename(String name) {
        return new UserReport(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public UserReport rename(Name name) {
        return new UserReport(name, null);
    }
}
