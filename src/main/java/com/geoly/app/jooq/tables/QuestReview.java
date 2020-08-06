/*
 * This file is generated by jOOQ.
 */
package com.geoly.app.jooq.tables;


import com.geoly.app.jooq.Geoly;
import com.geoly.app.jooq.Indexes;
import com.geoly.app.jooq.Keys;
import com.geoly.app.jooq.tables.records.QuestReviewRecord;

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
public class QuestReview extends TableImpl<QuestReviewRecord> {

    private static final long serialVersionUID = 1941897903;

    /**
     * The reference instance of <code>geoly.QUEST_REVIEW</code>
     */
    public static final QuestReview QUEST_REVIEW = new QuestReview();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<QuestReviewRecord> getRecordType() {
        return QuestReviewRecord.class;
    }

    /**
     * The column <code>geoly.QUEST_REVIEW.ID</code>.
     */
    public final TableField<QuestReviewRecord, Integer> ID = createField("ID", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>geoly.QUEST_REVIEW.CREATED_AT</code>.
     */
    public final TableField<QuestReviewRecord, Timestamp> CREATED_AT = createField("CREATED_AT", org.jooq.impl.SQLDataType.TIMESTAMP.precision(6), this, "");

    /**
     * The column <code>geoly.QUEST_REVIEW.REVIEW</code>.
     */
    public final TableField<QuestReviewRecord, Byte> REVIEW = createField("REVIEW", org.jooq.impl.SQLDataType.TINYINT, this, "");

    /**
     * The column <code>geoly.QUEST_REVIEW.REVIEW_TEXT</code>.
     */
    public final TableField<QuestReviewRecord, String> REVIEW_TEXT = createField("REVIEW_TEXT", org.jooq.impl.SQLDataType.VARCHAR(1000), this, "");

    /**
     * The column <code>geoly.QUEST_REVIEW.UPDATE_AT</code>.
     */
    public final TableField<QuestReviewRecord, Timestamp> UPDATE_AT = createField("UPDATE_AT", org.jooq.impl.SQLDataType.TIMESTAMP.precision(6), this, "");

    /**
     * The column <code>geoly.QUEST_REVIEW.QUEST_ID</code>.
     */
    public final TableField<QuestReviewRecord, Integer> QUEST_ID = createField("QUEST_ID", org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * The column <code>geoly.QUEST_REVIEW.USER_ID</code>.
     */
    public final TableField<QuestReviewRecord, Integer> USER_ID = createField("USER_ID", org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * Create a <code>geoly.QUEST_REVIEW</code> table reference
     */
    public QuestReview() {
        this(DSL.name("QUEST_REVIEW"), null);
    }

    /**
     * Create an aliased <code>geoly.QUEST_REVIEW</code> table reference
     */
    public QuestReview(String alias) {
        this(DSL.name(alias), QUEST_REVIEW);
    }

    /**
     * Create an aliased <code>geoly.QUEST_REVIEW</code> table reference
     */
    public QuestReview(Name alias) {
        this(alias, QUEST_REVIEW);
    }

    private QuestReview(Name alias, Table<QuestReviewRecord> aliased) {
        this(alias, aliased, null);
    }

    private QuestReview(Name alias, Table<QuestReviewRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> QuestReview(Table<O> child, ForeignKey<O, QuestReviewRecord> key) {
        super(child, key, QUEST_REVIEW);
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
        return Arrays.<Index>asList(Indexes.FK1HJWWRR4XDDJJ9C7IICVVM78J_INDEX_1, Indexes.FKQG1FBVCAVQ2LBPP8G8W8VTIAM_INDEX_1, Indexes.PRIMARY_KEY_105, Indexes.UKBO8AKQYL97OHGFN6T4O83Q6DR_INDEX_1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<QuestReviewRecord> getPrimaryKey() {
        return Keys.CONSTRAINT_105;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<QuestReviewRecord>> getKeys() {
        return Arrays.<UniqueKey<QuestReviewRecord>>asList(Keys.CONSTRAINT_105, Keys.UKBO8AKQYL97OHGFN6T4O83Q6DR);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ForeignKey<QuestReviewRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<QuestReviewRecord, ?>>asList(Keys.FK1HJWWRR4XDDJJ9C7IICVVM78J, Keys.FKQG1FBVCAVQ2LBPP8G8W8VTIAM);
    }

    public Quest quest() {
        return new Quest(this, Keys.FK1HJWWRR4XDDJJ9C7IICVVM78J);
    }

    public User user() {
        return new User(this, Keys.FKQG1FBVCAVQ2LBPP8G8W8VTIAM);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public QuestReview as(String alias) {
        return new QuestReview(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public QuestReview as(Name alias) {
        return new QuestReview(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public QuestReview rename(String name) {
        return new QuestReview(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public QuestReview rename(Name name) {
        return new QuestReview(name, null);
    }
}
