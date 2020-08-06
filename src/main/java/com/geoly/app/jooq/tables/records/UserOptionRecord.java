/*
 * This file is generated by jOOQ.
 */
package com.geoly.app.jooq.tables.records;


import com.geoly.app.jooq.tables.UserOption;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record6;
import org.jooq.Row6;
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
public class UserOptionRecord extends UpdatableRecordImpl<UserOptionRecord> implements Record6<Integer, Byte, Integer, Byte, Integer, Integer> {

    private static final long serialVersionUID = -1480249336;

    /**
     * Setter for <code>geoly.USER_OPTION.ID</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>geoly.USER_OPTION.ID</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>geoly.USER_OPTION.DARK_MODE</code>.
     */
    public void setDarkMode(Byte value) {
        set(1, value);
    }

    /**
     * Getter for <code>geoly.USER_OPTION.DARK_MODE</code>.
     */
    public Byte getDarkMode() {
        return (Byte) get(1);
    }

    /**
     * Setter for <code>geoly.USER_OPTION.MAP_THEME</code>.
     */
    public void setMapTheme(Integer value) {
        set(2, value);
    }

    /**
     * Getter for <code>geoly.USER_OPTION.MAP_THEME</code>.
     */
    public Integer getMapTheme() {
        return (Integer) get(2);
    }

    /**
     * Setter for <code>geoly.USER_OPTION.PRIVATE_PROFILE</code>.
     */
    public void setPrivateProfile(Byte value) {
        set(3, value);
    }

    /**
     * Getter for <code>geoly.USER_OPTION.PRIVATE_PROFILE</code>.
     */
    public Byte getPrivateProfile() {
        return (Byte) get(3);
    }

    /**
     * Setter for <code>geoly.USER_OPTION.LANGUAGE_ID</code>.
     */
    public void setLanguageId(Integer value) {
        set(4, value);
    }

    /**
     * Getter for <code>geoly.USER_OPTION.LANGUAGE_ID</code>.
     */
    public Integer getLanguageId() {
        return (Integer) get(4);
    }

    /**
     * Setter for <code>geoly.USER_OPTION.USER_ID</code>.
     */
    public void setUserId(Integer value) {
        set(5, value);
    }

    /**
     * Getter for <code>geoly.USER_OPTION.USER_ID</code>.
     */
    public Integer getUserId() {
        return (Integer) get(5);
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
    // Record6 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row6<Integer, Byte, Integer, Byte, Integer, Integer> fieldsRow() {
        return (Row6) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row6<Integer, Byte, Integer, Byte, Integer, Integer> valuesRow() {
        return (Row6) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field1() {
        return UserOption.USER_OPTION.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Byte> field2() {
        return UserOption.USER_OPTION.DARK_MODE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field3() {
        return UserOption.USER_OPTION.MAP_THEME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Byte> field4() {
        return UserOption.USER_OPTION.PRIVATE_PROFILE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field5() {
        return UserOption.USER_OPTION.LANGUAGE_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field6() {
        return UserOption.USER_OPTION.USER_ID;
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
        return getDarkMode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component3() {
        return getMapTheme();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Byte component4() {
        return getPrivateProfile();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component5() {
        return getLanguageId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component6() {
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
        return getDarkMode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value3() {
        return getMapTheme();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Byte value4() {
        return getPrivateProfile();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value5() {
        return getLanguageId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value6() {
        return getUserId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserOptionRecord value1(Integer value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserOptionRecord value2(Byte value) {
        setDarkMode(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserOptionRecord value3(Integer value) {
        setMapTheme(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserOptionRecord value4(Byte value) {
        setPrivateProfile(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserOptionRecord value5(Integer value) {
        setLanguageId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserOptionRecord value6(Integer value) {
        setUserId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserOptionRecord values(Integer value1, Byte value2, Integer value3, Byte value4, Integer value5, Integer value6) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached UserOptionRecord
     */
    public UserOptionRecord() {
        super(UserOption.USER_OPTION);
    }

    /**
     * Create a detached, initialised UserOptionRecord
     */
    public UserOptionRecord(Integer id, Byte darkMode, Integer mapTheme, Byte privateProfile, Integer languageId, Integer userId) {
        super(UserOption.USER_OPTION);

        set(0, id);
        set(1, darkMode);
        set(2, mapTheme);
        set(3, privateProfile);
        set(4, languageId);
        set(5, userId);
    }
}
