/*
 * This file is generated by jOOQ.
 */
package com.geoly.app.jooq.tables.records;


import com.geoly.app.jooq.tables.Quest;

import java.sql.Timestamp;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record11;
import org.jooq.Row11;
import org.jooq.impl.UpdatableRecordImpl;


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
public class QuestRecord extends UpdatableRecordImpl<QuestRecord> implements Record11<Integer, Byte, Timestamp, Byte, String, Byte, String, Byte, Byte, Integer, Integer> {

    private static final long serialVersionUID = -1984169721;

    /**
     * Setter for <code>geoly.QUEST.ID</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>geoly.QUEST.ID</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>geoly.QUEST.ACTIVE</code>.
     */
    public void setActive(Byte value) {
        set(1, value);
    }

    /**
     * Getter for <code>geoly.QUEST.ACTIVE</code>.
     */
    public Byte getActive() {
        return (Byte) get(1);
    }

    /**
     * Setter for <code>geoly.QUEST.CREATED_AT</code>.
     */
    public void setCreatedAt(Timestamp value) {
        set(2, value);
    }

    /**
     * Getter for <code>geoly.QUEST.CREATED_AT</code>.
     */
    public Timestamp getCreatedAt() {
        return (Timestamp) get(2);
    }

    /**
     * Setter for <code>geoly.QUEST.DAILY</code>.
     */
    public void setDaily(Byte value) {
        set(3, value);
    }

    /**
     * Getter for <code>geoly.QUEST.DAILY</code>.
     */
    public Byte getDaily() {
        return (Byte) get(3);
    }

    /**
     * Setter for <code>geoly.QUEST.DESCRIPTION</code>.
     */
    public void setDescription(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>geoly.QUEST.DESCRIPTION</code>.
     */
    public String getDescription() {
        return (String) get(4);
    }

    /**
     * Setter for <code>geoly.QUEST.DIFFICULTY</code>.
     */
    public void setDifficulty(Byte value) {
        set(5, value);
    }

    /**
     * Getter for <code>geoly.QUEST.DIFFICULTY</code>.
     */
    public Byte getDifficulty() {
        return (Byte) get(5);
    }

    /**
     * Setter for <code>geoly.QUEST.NAME</code>.
     */
    public void setName(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>geoly.QUEST.NAME</code>.
     */
    public String getName() {
        return (String) get(6);
    }

    /**
     * Setter for <code>geoly.QUEST.PREMIUM</code>.
     */
    public void setPremium(Byte value) {
        set(7, value);
    }

    /**
     * Getter for <code>geoly.QUEST.PREMIUM</code>.
     */
    public Byte getPremium() {
        return (Byte) get(7);
    }

    /**
     * Setter for <code>geoly.QUEST.PRIVATE_QUEST</code>.
     */
    public void setPrivateQuest(Byte value) {
        set(8, value);
    }

    /**
     * Getter for <code>geoly.QUEST.PRIVATE_QUEST</code>.
     */
    public Byte getPrivateQuest() {
        return (Byte) get(8);
    }

    /**
     * Setter for <code>geoly.QUEST.CATEGORY_ID</code>.
     */
    public void setCategoryId(Integer value) {
        set(9, value);
    }

    /**
     * Getter for <code>geoly.QUEST.CATEGORY_ID</code>.
     */
    public Integer getCategoryId() {
        return (Integer) get(9);
    }

    /**
     * Setter for <code>geoly.QUEST.USER_ID</code>.
     */
    public void setUserId(Integer value) {
        set(10, value);
    }

    /**
     * Getter for <code>geoly.QUEST.USER_ID</code>.
     */
    public Integer getUserId() {
        return (Integer) get(10);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record11 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row11<Integer, Byte, Timestamp, Byte, String, Byte, String, Byte, Byte, Integer, Integer> fieldsRow() {
        return (Row11) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row11<Integer, Byte, Timestamp, Byte, String, Byte, String, Byte, Byte, Integer, Integer> valuesRow() {
        return (Row11) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field1() {
        return Quest.QUEST.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Byte> field2() {
        return Quest.QUEST.ACTIVE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field3() {
        return Quest.QUEST.CREATED_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Byte> field4() {
        return Quest.QUEST.DAILY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field5() {
        return Quest.QUEST.DESCRIPTION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Byte> field6() {
        return Quest.QUEST.DIFFICULTY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field7() {
        return Quest.QUEST.NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Byte> field8() {
        return Quest.QUEST.PREMIUM;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Byte> field9() {
        return Quest.QUEST.PRIVATE_QUEST;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field10() {
        return Quest.QUEST.CATEGORY_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field11() {
        return Quest.QUEST.USER_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Byte component2() {
        return getActive();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp component3() {
        return getCreatedAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Byte component4() {
        return getDaily();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component5() {
        return getDescription();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Byte component6() {
        return getDifficulty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component7() {
        return getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Byte component8() {
        return getPremium();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Byte component9() {
        return getPrivateQuest();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component10() {
        return getCategoryId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component11() {
        return getUserId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Byte value2() {
        return getActive();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value3() {
        return getCreatedAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Byte value4() {
        return getDaily();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value5() {
        return getDescription();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Byte value6() {
        return getDifficulty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value7() {
        return getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Byte value8() {
        return getPremium();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Byte value9() {
        return getPrivateQuest();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value10() {
        return getCategoryId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value11() {
        return getUserId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public QuestRecord value1(Integer value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public QuestRecord value2(Byte value) {
        setActive(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public QuestRecord value3(Timestamp value) {
        setCreatedAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public QuestRecord value4(Byte value) {
        setDaily(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public QuestRecord value5(String value) {
        setDescription(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public QuestRecord value6(Byte value) {
        setDifficulty(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public QuestRecord value7(String value) {
        setName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public QuestRecord value8(Byte value) {
        setPremium(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public QuestRecord value9(Byte value) {
        setPrivateQuest(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public QuestRecord value10(Integer value) {
        setCategoryId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public QuestRecord value11(Integer value) {
        setUserId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public QuestRecord values(Integer value1, Byte value2, Timestamp value3, Byte value4, String value5, Byte value6, String value7, Byte value8, Byte value9, Integer value10, Integer value11) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        value10(value10);
        value11(value11);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached QuestRecord
     */
    public QuestRecord() {
        super(Quest.QUEST);
    }

    /**
     * Create a detached, initialised QuestRecord
     */
    public QuestRecord(Integer id, Byte active, Timestamp createdAt, Byte daily, String description, Byte difficulty, String name, Byte premium, Byte privateQuest, Integer categoryId, Integer userId) {
        super(Quest.QUEST);

        set(0, id);
        set(1, active);
        set(2, createdAt);
        set(3, daily);
        set(4, description);
        set(5, difficulty);
        set(6, name);
        set(7, premium);
        set(8, privateQuest);
        set(9, categoryId);
        set(10, userId);
    }
}
