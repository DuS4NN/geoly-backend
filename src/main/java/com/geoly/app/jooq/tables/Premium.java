/*
 * This file is generated by jOOQ.
 */
package com.geoly.app.jooq.tables;


import com.geoly.app.jooq.Geoly;
import com.geoly.app.jooq.Indexes;
import com.geoly.app.jooq.Keys;
import com.geoly.app.jooq.tables.records.PremiumRecord;

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
public class Premium extends TableImpl<PremiumRecord> {

    private static final long serialVersionUID = 667231831;

    /**
     * The reference instance of <code>geoly.PREMIUM</code>
     */
    public static final Premium PREMIUM = new Premium();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PremiumRecord> getRecordType() {
        return PremiumRecord.class;
    }

    /**
     * The column <code>geoly.PREMIUM.ID</code>.
     */
    public final TableField<PremiumRecord, Integer> ID = createField("ID", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>geoly.PREMIUM.END_AT</code>.
     */
    public final TableField<PremiumRecord, Timestamp> END_AT = createField("END_AT", org.jooq.impl.SQLDataType.TIMESTAMP.precision(6), this, "");

    /**
     * The column <code>geoly.PREMIUM.START_AT</code>.
     */
    public final TableField<PremiumRecord, Timestamp> START_AT = createField("START_AT", org.jooq.impl.SQLDataType.TIMESTAMP.precision(6), this, "");

    /**
     * The column <code>geoly.PREMIUM.TRANSACTION_ID</code>.
     */
    public final TableField<PremiumRecord, Integer> TRANSACTION_ID = createField("TRANSACTION_ID", org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * The column <code>geoly.PREMIUM.USER_ID</code>.
     */
    public final TableField<PremiumRecord, Integer> USER_ID = createField("USER_ID", org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * Create a <code>geoly.PREMIUM</code> table reference
     */
    public Premium() {
        this(DSL.name("PREMIUM"), null);
    }

    /**
     * Create an aliased <code>geoly.PREMIUM</code> table reference
     */
    public Premium(String alias) {
        this(DSL.name(alias), PREMIUM);
    }

    /**
     * Create an aliased <code>geoly.PREMIUM</code> table reference
     */
    public Premium(Name alias) {
        this(alias, PREMIUM);
    }

    private Premium(Name alias, Table<PremiumRecord> aliased) {
        this(alias, aliased, null);
    }

    private Premium(Name alias, Table<PremiumRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> Premium(Table<O> child, ForeignKey<O, PremiumRecord> key) {
        super(child, key, PREMIUM);
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
        return Arrays.<Index>asList(Indexes.FK55RHAW7HB4IB9USGJD0RVYUBB_INDEX_1, Indexes.FKOTOO6BVI30QYEL0RVHS8OH9WO_INDEX_1, Indexes.PRIMARY_KEY_1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<PremiumRecord> getPrimaryKey() {
        return Keys.CONSTRAINT_1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<PremiumRecord>> getKeys() {
        return Arrays.<UniqueKey<PremiumRecord>>asList(Keys.CONSTRAINT_1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ForeignKey<PremiumRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<PremiumRecord, ?>>asList(Keys.FKOTOO6BVI30QYEL0RVHS8OH9WO, Keys.FK55RHAW7HB4IB9USGJD0RVYUBB);
    }

    public Transaction transaction() {
        return new Transaction(this, Keys.FKOTOO6BVI30QYEL0RVHS8OH9WO);
    }

    public User user() {
        return new User(this, Keys.FK55RHAW7HB4IB9USGJD0RVYUBB);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Premium as(String alias) {
        return new Premium(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Premium as(Name alias) {
        return new Premium(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Premium rename(String name) {
        return new Premium(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Premium rename(Name name) {
        return new Premium(name, null);
    }
}