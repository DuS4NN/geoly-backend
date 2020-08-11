/*
 * This file is generated by jOOQ.
 */
package com.geoly.app.jooq.tables;


import com.geoly.app.jooq.Geoly;
import com.geoly.app.jooq.Indexes;
import com.geoly.app.jooq.Keys;
import com.geoly.app.jooq.tables.records.UserPartyQuestRecord;

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
public class UserPartyQuest extends TableImpl<UserPartyQuestRecord> {

    private static final long serialVersionUID = -2072304582;

    /**
     * The reference instance of <code>geoly.USER_PARTY_QUEST</code>
     */
    public static final UserPartyQuest USER_PARTY_QUEST = new UserPartyQuest();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<UserPartyQuestRecord> getRecordType() {
        return UserPartyQuestRecord.class;
    }

    /**
     * The column <code>geoly.USER_PARTY_QUEST.ID</code>.
     */
    public final TableField<UserPartyQuestRecord, Integer> ID = createField("ID", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>geoly.USER_PARTY_QUEST.CREATED_AT</code>.
     */
    public final TableField<UserPartyQuestRecord, Timestamp> CREATED_AT = createField("CREATED_AT", org.jooq.impl.SQLDataType.TIMESTAMP.precision(6), this, "");

    /**
     * The column <code>geoly.USER_PARTY_QUEST.STATUS</code>.
     */
    public final TableField<UserPartyQuestRecord, String> STATUS = createField("STATUS", org.jooq.impl.SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>geoly.USER_PARTY_QUEST.UPDATED_AT</code>.
     */
    public final TableField<UserPartyQuestRecord, Timestamp> UPDATED_AT = createField("UPDATED_AT", org.jooq.impl.SQLDataType.TIMESTAMP.precision(6), this, "");

    /**
     * The column <code>geoly.USER_PARTY_QUEST.PARTY_QUEST_ID</code>.
     */
    public final TableField<UserPartyQuestRecord, Integer> PARTY_QUEST_ID = createField("PARTY_QUEST_ID", org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * The column <code>geoly.USER_PARTY_QUEST.STAGE_ID</code>.
     */
    public final TableField<UserPartyQuestRecord, Integer> STAGE_ID = createField("STAGE_ID", org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * The column <code>geoly.USER_PARTY_QUEST.USER_ID</code>.
     */
    public final TableField<UserPartyQuestRecord, Integer> USER_ID = createField("USER_ID", org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * Create a <code>geoly.USER_PARTY_QUEST</code> table reference
     */
    public UserPartyQuest() {
        this(DSL.name("USER_PARTY_QUEST"), null);
    }

    /**
     * Create an aliased <code>geoly.USER_PARTY_QUEST</code> table reference
     */
    public UserPartyQuest(String alias) {
        this(DSL.name(alias), USER_PARTY_QUEST);
    }

    /**
     * Create an aliased <code>geoly.USER_PARTY_QUEST</code> table reference
     */
    public UserPartyQuest(Name alias) {
        this(alias, USER_PARTY_QUEST);
    }

    private UserPartyQuest(Name alias, Table<UserPartyQuestRecord> aliased) {
        this(alias, aliased, null);
    }

    private UserPartyQuest(Name alias, Table<UserPartyQuestRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> UserPartyQuest(Table<O> child, ForeignKey<O, UserPartyQuestRecord> key) {
        super(child, key, USER_PARTY_QUEST);
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
        return Arrays.<Index>asList(Indexes.FK2SU24YK08TSVBUNLJQ37QS3H3_INDEX_5, Indexes.FKHV562R9D4GYY92EQSJGXTP3AN_INDEX_5, Indexes.FKQVJ70L8UEE5IOUNI0L5473MIJ_INDEX_5, Indexes.PRIMARY_KEY_55);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<UserPartyQuestRecord> getPrimaryKey() {
        return Keys.CONSTRAINT_55;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<UserPartyQuestRecord>> getKeys() {
        return Arrays.<UniqueKey<UserPartyQuestRecord>>asList(Keys.CONSTRAINT_55);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ForeignKey<UserPartyQuestRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<UserPartyQuestRecord, ?>>asList(Keys.FKHV562R9D4GYY92EQSJGXTP3AN, Keys.FK2SU24YK08TSVBUNLJQ37QS3H3, Keys.FKQVJ70L8UEE5IOUNI0L5473MIJ);
    }

    public Stage fkhv562r9d4gyy92eqsjgxtp3an() {
        return new Stage(this, Keys.FKHV562R9D4GYY92EQSJGXTP3AN);
    }

    public Stage fk2su24yk08tsvbunljq37qs3h3() {
        return new Stage(this, Keys.FK2SU24YK08TSVBUNLJQ37QS3H3);
    }

    public User user() {
        return new User(this, Keys.FKQVJ70L8UEE5IOUNI0L5473MIJ);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserPartyQuest as(String alias) {
        return new UserPartyQuest(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserPartyQuest as(Name alias) {
        return new UserPartyQuest(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public UserPartyQuest rename(String name) {
        return new UserPartyQuest(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public UserPartyQuest rename(Name name) {
        return new UserPartyQuest(name, null);
    }
}
