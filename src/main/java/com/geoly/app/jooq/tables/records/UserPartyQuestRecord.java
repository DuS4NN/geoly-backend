/*
 * This file is generated by jOOQ.
 */
package com.geoly.app.jooq.tables.records;


import com.geoly.app.jooq.tables.UserPartyQuest;

import java.sql.Timestamp;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record7;
import org.jooq.Row7;
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
public class UserPartyQuestRecord extends UpdatableRecordImpl<UserPartyQuestRecord> implements Record7<Integer, Timestamp, String, Timestamp, Integer, Integer, Integer> {

    private static final long serialVersionUID = -867651335;

    /**
     * Setter for <code>geoly.USER_PARTY_QUEST.ID</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>geoly.USER_PARTY_QUEST.ID</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>geoly.USER_PARTY_QUEST.CREATED_AT</code>.
     */
    public void setCreatedAt(Timestamp value) {
        set(1, value);
    }

    /**
     * Getter for <code>geoly.USER_PARTY_QUEST.CREATED_AT</code>.
     */
    public Timestamp getCreatedAt() {
        return (Timestamp) get(1);
    }

    /**
     * Setter for <code>geoly.USER_PARTY_QUEST.STATUS</code>.
     */
    public void setStatus(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>geoly.USER_PARTY_QUEST.STATUS</code>.
     */
    public String getStatus() {
        return (String) get(2);
    }

    /**
     * Setter for <code>geoly.USER_PARTY_QUEST.UPDATED_AT</code>.
     */
    public void setUpdatedAt(Timestamp value) {
        set(3, value);
    }

    /**
     * Getter for <code>geoly.USER_PARTY_QUEST.UPDATED_AT</code>.
     */
    public Timestamp getUpdatedAt() {
        return (Timestamp) get(3);
    }

    /**
     * Setter for <code>geoly.USER_PARTY_QUEST.PARTY_QUEST_ID</code>.
     */
    public void setPartyQuestId(Integer value) {
        set(4, value);
    }

    /**
     * Getter for <code>geoly.USER_PARTY_QUEST.PARTY_QUEST_ID</code>.
     */
    public Integer getPartyQuestId() {
        return (Integer) get(4);
    }

    /**
     * Setter for <code>geoly.USER_PARTY_QUEST.STAGE_ID</code>.
     */
    public void setStageId(Integer value) {
        set(5, value);
    }

    /**
     * Getter for <code>geoly.USER_PARTY_QUEST.STAGE_ID</code>.
     */
    public Integer getStageId() {
        return (Integer) get(5);
    }

    /**
     * Setter for <code>geoly.USER_PARTY_QUEST.USER_ID</code>.
     */
    public void setUserId(Integer value) {
        set(6, value);
    }

    /**
     * Getter for <code>geoly.USER_PARTY_QUEST.USER_ID</code>.
     */
    public Integer getUserId() {
        return (Integer) get(6);
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
    // Record7 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row7<Integer, Timestamp, String, Timestamp, Integer, Integer, Integer> fieldsRow() {
        return (Row7) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row7<Integer, Timestamp, String, Timestamp, Integer, Integer, Integer> valuesRow() {
        return (Row7) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field1() {
        return UserPartyQuest.USER_PARTY_QUEST.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field2() {
        return UserPartyQuest.USER_PARTY_QUEST.CREATED_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return UserPartyQuest.USER_PARTY_QUEST.STATUS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field4() {
        return UserPartyQuest.USER_PARTY_QUEST.UPDATED_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field5() {
        return UserPartyQuest.USER_PARTY_QUEST.PARTY_QUEST_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field6() {
        return UserPartyQuest.USER_PARTY_QUEST.STAGE_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field7() {
        return UserPartyQuest.USER_PARTY_QUEST.USER_ID;
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
    public Timestamp component2() {
        return getCreatedAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component3() {
        return getStatus();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp component4() {
        return getUpdatedAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component5() {
        return getPartyQuestId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component6() {
        return getStageId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component7() {
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
    public Timestamp value2() {
        return getCreatedAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value3() {
        return getStatus();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value4() {
        return getUpdatedAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value5() {
        return getPartyQuestId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value6() {
        return getStageId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value7() {
        return getUserId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserPartyQuestRecord value1(Integer value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserPartyQuestRecord value2(Timestamp value) {
        setCreatedAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserPartyQuestRecord value3(String value) {
        setStatus(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserPartyQuestRecord value4(Timestamp value) {
        setUpdatedAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserPartyQuestRecord value5(Integer value) {
        setPartyQuestId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserPartyQuestRecord value6(Integer value) {
        setStageId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserPartyQuestRecord value7(Integer value) {
        setUserId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserPartyQuestRecord values(Integer value1, Timestamp value2, String value3, Timestamp value4, Integer value5, Integer value6, Integer value7) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached UserPartyQuestRecord
     */
    public UserPartyQuestRecord() {
        super(UserPartyQuest.USER_PARTY_QUEST);
    }

    /**
     * Create a detached, initialised UserPartyQuestRecord
     */
    public UserPartyQuestRecord(Integer id, Timestamp createdAt, String status, Timestamp updatedAt, Integer partyQuestId, Integer stageId, Integer userId) {
        super(UserPartyQuest.USER_PARTY_QUEST);

        set(0, id);
        set(1, createdAt);
        set(2, status);
        set(3, updatedAt);
        set(4, partyQuestId);
        set(5, stageId);
        set(6, userId);
    }
}
