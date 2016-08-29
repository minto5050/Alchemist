package in.co.geekninja.dbgen;

import android.support.annotation.Nullable;

/**
 * Created by PS on 1/25/2016.
 */
public class DbField {

    String FieldName;
    String FieldType;
    @Nullable
    int FieldLength;
    private boolean autoIncrement;
    private boolean primaryKey;
    private boolean notNull;
    private String default_;

    public String getDefault() {
        return default_;
    }

    public void setDefault(String default_) {
        this.default_ = default_;
    }

    public boolean isNotNull() {
        return notNull;
    }

    public void setNotNull(boolean notNull) {
        this.notNull = notNull;
    }

    public DbField(String fieldName, String fieldType) {
        FieldName = fieldName;
        FieldType = fieldType;
    }

    public DbField() {
    }

    public DbField(String fieldName, String fieldType, boolean primaryKey, boolean autoIncrement,boolean notNull) {
        FieldName = fieldName;
        FieldType = fieldType;
        this.primaryKey = primaryKey;
        this.autoIncrement = autoIncrement;
        this.notNull=notNull;
    }
    public DbField(String fieldName, String fieldType,String Default, boolean primaryKey, boolean autoIncrement,boolean notNull) {
        this.FieldName = fieldName;
        this.FieldType = fieldType;
        this.primaryKey = primaryKey;
        this.autoIncrement = autoIncrement;
        this.notNull=notNull;
        this.default_=Default;
    }

    public DbField(String fieldName, String fieldType, int fieldLength) {
        FieldName = fieldName;
        FieldType = fieldType;
        FieldLength = fieldLength;
    }
    public DbField(String fieldName, String fieldType, String Default) {
        this.FieldName = fieldName;
        this.FieldType = fieldType;
        this.default_ = Default;
    }
    public String getFieldName() {
        return FieldName;
    }

    public void setFieldName(String fieldName) {
        FieldName = fieldName;
    }

    public String getFieldType() {
        return FieldType;
    }

    public void setFieldType(String fieldType) {
        FieldType = fieldType;
    }

    @Nullable
    public int getFieldLength() {
        return FieldLength;
    }

    public void setFieldLength(@Nullable int fieldLength) {
        FieldLength = fieldLength;
    }

    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        if (this.getFieldType()==DbGen.INTEGER)
            this.autoIncrement = autoIncrement;
        else
            throw new IllegalArgumentException("Only numberic data can be auto incremntal");
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }
}
