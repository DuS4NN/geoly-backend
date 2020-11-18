/*
 * This file is generated by jOOQ.
 */
package com.geoly.app.jooq.tables;


import com.geoly.app.jooq.Geoly;
import com.geoly.app.jooq.Indexes;
import com.geoly.app.jooq.Keys;
import com.geoly.app.jooq.tables.records.QuestRecord;

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
public class Quest extends TableImpl<QuestRecord> {

    private static final long serialVersionUID = -357830654;

    /**
     * The reference instance of <code>geoly.QUEST</code>
     */
    public static final Quest QUEST = new Quest();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<QuestRecord> getRecordType() {
        return QuestRecord.class;
    }

    /**
     * The column <code>geoly.QUEST.ID</code>.
     */
    public final TableField<QuestRecord, Integer> ID = createField("ID", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>geoly.QUEST.ACTIVE</code>.
     */
    public final TableField<QuestRecord, Byte> ACTIVE = createField("ACTIVE", org.jooq.impl.SQLDataType.TINYINT, this, "");

    /**
     * The column <code>geoly.QUEST.CREATED_AT</code>.
     */
    public final TableField<QuestRecord, Timestamp> CREATED_AT = createField("CREATED_AT", org.jooq.impl.SQLDataType.TIMESTAMP.precision(6), this, "");

    /**
     * The column <code>geoly.QUEST.DAILY</code>.
     */
    public final TableField<QuestRecord, Byte> DAILY = createField("DAILY", org.jooq.impl.SQLDataType.TINYINT, this, "");

    /**
     * The column <code>geoly.QUEST.DESCRIPTION</code>.
     */
    public final TableField<QuestRecord, String> DESCRIPTION = createField("DESCRIPTION", org.jooq.impl.SQLDataType.VARCHAR(500).nullable(false), this, "");

    /**
     * The column <code>geoly.QUEST.DIFFICULTY</code>.
     */
    public final TableField<QuestRecord, Byte> DIFFICULTY = createField("DIFFICULTY", org.jooq.impl.SQLDataType.TINYINT, this, "");

    /**
     * The column <code>geoly.QUEST.NAME</code>.
     */
    public final TableField<QuestRecord, String> NAME = createField("NAME", org.jooq.impl.SQLDataType.VARCHAR(50).nullable(false), this, "");

    /**
     * The column <code>geoly.QUEST.PREMIUM</code>.
     */
    public final TableField<QuestRecord, Byte> PREMIUM = createField("PREMIUM", org.jooq.impl.SQLDataType.TINYINT, this, "");

    /**
     * The column <code>geoly.QUEST.PRIVATE_QUEST</code>.
     */
    public final TableField<QuestRecord, Byte> PRIVATE_QUEST = createField("PRIVATE_QUEST", org.jooq.impl.SQLDataType.TINYINT, this, "");

    /**
     * The column <code>geoly.QUEST.CATEGORY_ID</code>.
     */
    public final TableField<QuestRecord, Integer> CATEGORY_ID = createField("CATEGORY_ID", org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * The column <code>geoly.QUEST.USER_ID</code>.
     */
    public final TableField<QuestRecord, Integer> USER_ID = createField("USER_ID", org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * Create a <code>geoly.QUEST</code> table reference
     */
    public Quest() {
        this(DSL.name("QUEST"), null);
    }

    /**
     * Create an aliased <code>geoly.QUEST</code> table reference
     */
    public Quest(String alias) {
        this(DSL.name(alias), QUEST);
    }

    /**
     * Create an aliased <code>geoly.QUEST</code> table reference
     */
    public Quest(Name alias) {
        this(alias, QUEST);
    }

    private Quest(Name alias, Table<QuestRecord> aliased) {
        this(alias, aliased, null);
    }

    private Quest(Name alias, Table<QuestRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> Quest(Table<O> child, ForeignKey<O, QuestRecord> key) {
        super(child, key, QUEST);
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
        return Arrays.<Index>asList(Indexes.FK8O38CSDJ7HT1E8BLO3IMVWDGA_INDEX_4, Indexes.FK9GLM6AI8S4XIUJBSUCPGAB31D_INDEX_4, Indexes.PRIMARY_KEY_49);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<QuestRecord> getPrimaryKey() {
        return Keys.CONSTRAINT_49;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<QuestRecord>> getKeys() {
        return Arrays.<UniqueKey<QuestRecord>>asList(Keys.CONSTRAINT_49);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ForeignKey<QuestRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<QuestRecord, ?>>asList(Keys.FK8O38CSDJ7HT1E8BLO3IMVWDGA, Keys.FK9GLM6AI8S4XIUJBSUCPGAB31D);
    }

    public Category category() {
        return new Category(this, Keys.FK8O38CSDJ7HT1E8BLO3IMVWDGA);
    }

    public User user() {
        return new User(this, Keys.FK9GLM6AI8S4XIUJBSUCPGAB31D);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Quest as(String alias) {
        return new Quest(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Quest as(Name alias) {
        return new Quest(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Quest rename(String name) {
        return new Quest(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Quest rename(Name name) {
        return new Quest(name, null);
    }
}
