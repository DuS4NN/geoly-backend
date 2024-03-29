/*
 * This file is generated by jOOQ.
 */
package com.geoly.app.jooq.tables;


import com.geoly.app.jooq.Geoly;
import com.geoly.app.jooq.Indexes;
import com.geoly.app.jooq.Keys;
import com.geoly.app.jooq.tables.records.PartyQuestRecord;

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
public class PartyQuest extends TableImpl<PartyQuestRecord> {

    private static final long serialVersionUID = -42296533;

    /**
     * The reference instance of <code>geoly.PARTY_QUEST</code>
     */
    public static final PartyQuest PARTY_QUEST = new PartyQuest();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PartyQuestRecord> getRecordType() {
        return PartyQuestRecord.class;
    }

    /**
     * The column <code>geoly.PARTY_QUEST.ID</code>.
     */
    public final TableField<PartyQuestRecord, Integer> ID = createField("ID", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>geoly.PARTY_QUEST.ACTIVE</code>.
     */
    public final TableField<PartyQuestRecord, Byte> ACTIVE = createField("ACTIVE", org.jooq.impl.SQLDataType.TINYINT, this, "");

    /**
     * The column <code>geoly.PARTY_QUEST.PARTY_ID</code>.
     */
    public final TableField<PartyQuestRecord, Integer> PARTY_ID = createField("PARTY_ID", org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * The column <code>geoly.PARTY_QUEST.QUEST_ID</code>.
     */
    public final TableField<PartyQuestRecord, Integer> QUEST_ID = createField("QUEST_ID", org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * Create a <code>geoly.PARTY_QUEST</code> table reference
     */
    public PartyQuest() {
        this(DSL.name("PARTY_QUEST"), null);
    }

    /**
     * Create an aliased <code>geoly.PARTY_QUEST</code> table reference
     */
    public PartyQuest(String alias) {
        this(DSL.name(alias), PARTY_QUEST);
    }

    /**
     * Create an aliased <code>geoly.PARTY_QUEST</code> table reference
     */
    public PartyQuest(Name alias) {
        this(alias, PARTY_QUEST);
    }

    private PartyQuest(Name alias, Table<PartyQuestRecord> aliased) {
        this(alias, aliased, null);
    }

    private PartyQuest(Name alias, Table<PartyQuestRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> PartyQuest(Table<O> child, ForeignKey<O, PartyQuestRecord> key) {
        super(child, key, PARTY_QUEST);
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
        return Arrays.<Index>asList(Indexes.FK4BFMT3TPKS38SN4GW97VYHEM3_INDEX_2, Indexes.FKHFLFH9KMNL4BFFJFQH57IGR76_INDEX_2, Indexes.PRIMARY_KEY_2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<PartyQuestRecord> getPrimaryKey() {
        return Keys.CONSTRAINT_2;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<PartyQuestRecord>> getKeys() {
        return Arrays.<UniqueKey<PartyQuestRecord>>asList(Keys.CONSTRAINT_2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ForeignKey<PartyQuestRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<PartyQuestRecord, ?>>asList(Keys.FK4BFMT3TPKS38SN4GW97VYHEM3, Keys.FKHFLFH9KMNL4BFFJFQH57IGR76);
    }

    public Party party() {
        return new Party(this, Keys.FK4BFMT3TPKS38SN4GW97VYHEM3);
    }

    public Quest quest() {
        return new Quest(this, Keys.FKHFLFH9KMNL4BFFJFQH57IGR76);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PartyQuest as(String alias) {
        return new PartyQuest(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PartyQuest as(Name alias) {
        return new PartyQuest(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public PartyQuest rename(String name) {
        return new PartyQuest(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public PartyQuest rename(Name name) {
        return new PartyQuest(name, null);
    }
}
