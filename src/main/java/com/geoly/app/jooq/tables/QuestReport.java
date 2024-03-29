/*
 * This file is generated by jOOQ.
 */
package com.geoly.app.jooq.tables;


import com.geoly.app.jooq.Geoly;
import com.geoly.app.jooq.Indexes;
import com.geoly.app.jooq.Keys;
import com.geoly.app.jooq.tables.records.QuestReportRecord;

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
public class QuestReport extends TableImpl<QuestReportRecord> {

    private static final long serialVersionUID = -985777914;

    /**
     * The reference instance of <code>geoly.QUEST_REPORT</code>
     */
    public static final QuestReport QUEST_REPORT = new QuestReport();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<QuestReportRecord> getRecordType() {
        return QuestReportRecord.class;
    }

    /**
     * The column <code>geoly.QUEST_REPORT.ID</code>.
     */
    public final TableField<QuestReportRecord, Integer> ID = createField("ID", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>geoly.QUEST_REPORT.CREATED_AT</code>.
     */
    public final TableField<QuestReportRecord, Timestamp> CREATED_AT = createField("CREATED_AT", org.jooq.impl.SQLDataType.TIMESTAMP.precision(6), this, "");

    /**
     * The column <code>geoly.QUEST_REPORT.REASON</code>.
     */
    public final TableField<QuestReportRecord, String> REASON = createField("REASON", org.jooq.impl.SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>geoly.QUEST_REPORT.SOLVED</code>.
     */
    public final TableField<QuestReportRecord, Byte> SOLVED = createField("SOLVED", org.jooq.impl.SQLDataType.TINYINT, this, "");

    /**
     * The column <code>geoly.QUEST_REPORT.QUEST_ID</code>.
     */
    public final TableField<QuestReportRecord, Integer> QUEST_ID = createField("QUEST_ID", org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * The column <code>geoly.QUEST_REPORT.USER_ID</code>.
     */
    public final TableField<QuestReportRecord, Integer> USER_ID = createField("USER_ID", org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * Create a <code>geoly.QUEST_REPORT</code> table reference
     */
    public QuestReport() {
        this(DSL.name("QUEST_REPORT"), null);
    }

    /**
     * Create an aliased <code>geoly.QUEST_REPORT</code> table reference
     */
    public QuestReport(String alias) {
        this(DSL.name(alias), QUEST_REPORT);
    }

    /**
     * Create an aliased <code>geoly.QUEST_REPORT</code> table reference
     */
    public QuestReport(Name alias) {
        this(alias, QUEST_REPORT);
    }

    private QuestReport(Name alias, Table<QuestReportRecord> aliased) {
        this(alias, aliased, null);
    }

    private QuestReport(Name alias, Table<QuestReportRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> QuestReport(Table<O> child, ForeignKey<O, QuestReportRecord> key) {
        super(child, key, QUEST_REPORT);
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
        return Arrays.<Index>asList(Indexes.FKLX1098F38PYASBE16HYXWOED4_INDEX_1, Indexes.FKONI920KBF1BS1JBC7SALC9K53_INDEX_1, Indexes.PRIMARY_KEY_10);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<QuestReportRecord> getPrimaryKey() {
        return Keys.CONSTRAINT_10;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<QuestReportRecord>> getKeys() {
        return Arrays.<UniqueKey<QuestReportRecord>>asList(Keys.CONSTRAINT_10);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ForeignKey<QuestReportRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<QuestReportRecord, ?>>asList(Keys.FKONI920KBF1BS1JBC7SALC9K53, Keys.FKLX1098F38PYASBE16HYXWOED4);
    }

    public Quest quest() {
        return new Quest(this, Keys.FKONI920KBF1BS1JBC7SALC9K53);
    }

    public User user() {
        return new User(this, Keys.FKLX1098F38PYASBE16HYXWOED4);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public QuestReport as(String alias) {
        return new QuestReport(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public QuestReport as(Name alias) {
        return new QuestReport(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public QuestReport rename(String name) {
        return new QuestReport(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public QuestReport rename(Name name) {
        return new QuestReport(name, null);
    }
}
