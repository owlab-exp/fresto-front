/**
 * Autogenerated by Thrift Compiler (0.9.1)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package fresto.data;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.server.AbstractNonblockingServer.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OperationID implements org.apache.thrift.TBase<OperationID, OperationID._Fields>, java.io.Serializable, Cloneable, Comparable<OperationID> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("OperationID");

  private static final org.apache.thrift.protocol.TField TYPE_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("typeName", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField OPERATION_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("operationName", org.apache.thrift.protocol.TType.STRING, (short)2);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new OperationIDStandardSchemeFactory());
    schemes.put(TupleScheme.class, new OperationIDTupleSchemeFactory());
  }

  public String typeName; // required
  public String operationName; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    TYPE_NAME((short)1, "typeName"),
    OPERATION_NAME((short)2, "operationName");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // TYPE_NAME
          return TYPE_NAME;
        case 2: // OPERATION_NAME
          return OPERATION_NAME;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.TYPE_NAME, new org.apache.thrift.meta_data.FieldMetaData("typeName", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.OPERATION_NAME, new org.apache.thrift.meta_data.FieldMetaData("operationName", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(OperationID.class, metaDataMap);
  }

  public OperationID() {
  }

  public OperationID(
    String typeName,
    String operationName)
  {
    this();
    this.typeName = typeName;
    this.operationName = operationName;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public OperationID(OperationID other) {
    if (other.isSetTypeName()) {
      this.typeName = other.typeName;
    }
    if (other.isSetOperationName()) {
      this.operationName = other.operationName;
    }
  }

  public OperationID deepCopy() {
    return new OperationID(this);
  }

  @Override
  public void clear() {
    this.typeName = null;
    this.operationName = null;
  }

  public String getTypeName() {
    return this.typeName;
  }

  public OperationID setTypeName(String typeName) {
    this.typeName = typeName;
    return this;
  }

  public void unsetTypeName() {
    this.typeName = null;
  }

  /** Returns true if field typeName is set (has been assigned a value) and false otherwise */
  public boolean isSetTypeName() {
    return this.typeName != null;
  }

  public void setTypeNameIsSet(boolean value) {
    if (!value) {
      this.typeName = null;
    }
  }

  public String getOperationName() {
    return this.operationName;
  }

  public OperationID setOperationName(String operationName) {
    this.operationName = operationName;
    return this;
  }

  public void unsetOperationName() {
    this.operationName = null;
  }

  /** Returns true if field operationName is set (has been assigned a value) and false otherwise */
  public boolean isSetOperationName() {
    return this.operationName != null;
  }

  public void setOperationNameIsSet(boolean value) {
    if (!value) {
      this.operationName = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case TYPE_NAME:
      if (value == null) {
        unsetTypeName();
      } else {
        setTypeName((String)value);
      }
      break;

    case OPERATION_NAME:
      if (value == null) {
        unsetOperationName();
      } else {
        setOperationName((String)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case TYPE_NAME:
      return getTypeName();

    case OPERATION_NAME:
      return getOperationName();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case TYPE_NAME:
      return isSetTypeName();
    case OPERATION_NAME:
      return isSetOperationName();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof OperationID)
      return this.equals((OperationID)that);
    return false;
  }

  public boolean equals(OperationID that) {
    if (that == null)
      return false;

    boolean this_present_typeName = true && this.isSetTypeName();
    boolean that_present_typeName = true && that.isSetTypeName();
    if (this_present_typeName || that_present_typeName) {
      if (!(this_present_typeName && that_present_typeName))
        return false;
      if (!this.typeName.equals(that.typeName))
        return false;
    }

    boolean this_present_operationName = true && this.isSetOperationName();
    boolean that_present_operationName = true && that.isSetOperationName();
    if (this_present_operationName || that_present_operationName) {
      if (!(this_present_operationName && that_present_operationName))
        return false;
      if (!this.operationName.equals(that.operationName))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  @Override
  public int compareTo(OperationID other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetTypeName()).compareTo(other.isSetTypeName());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetTypeName()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.typeName, other.typeName);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetOperationName()).compareTo(other.isSetOperationName());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetOperationName()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.operationName, other.operationName);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("OperationID(");
    boolean first = true;

    sb.append("typeName:");
    if (this.typeName == null) {
      sb.append("null");
    } else {
      sb.append(this.typeName);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("operationName:");
    if (this.operationName == null) {
      sb.append("null");
    } else {
      sb.append(this.operationName);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class OperationIDStandardSchemeFactory implements SchemeFactory {
    public OperationIDStandardScheme getScheme() {
      return new OperationIDStandardScheme();
    }
  }

  private static class OperationIDStandardScheme extends StandardScheme<OperationID> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, OperationID struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // TYPE_NAME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.typeName = iprot.readString();
              struct.setTypeNameIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // OPERATION_NAME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.operationName = iprot.readString();
              struct.setOperationNameIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, OperationID struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.typeName != null) {
        oprot.writeFieldBegin(TYPE_NAME_FIELD_DESC);
        oprot.writeString(struct.typeName);
        oprot.writeFieldEnd();
      }
      if (struct.operationName != null) {
        oprot.writeFieldBegin(OPERATION_NAME_FIELD_DESC);
        oprot.writeString(struct.operationName);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class OperationIDTupleSchemeFactory implements SchemeFactory {
    public OperationIDTupleScheme getScheme() {
      return new OperationIDTupleScheme();
    }
  }

  private static class OperationIDTupleScheme extends TupleScheme<OperationID> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, OperationID struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetTypeName()) {
        optionals.set(0);
      }
      if (struct.isSetOperationName()) {
        optionals.set(1);
      }
      oprot.writeBitSet(optionals, 2);
      if (struct.isSetTypeName()) {
        oprot.writeString(struct.typeName);
      }
      if (struct.isSetOperationName()) {
        oprot.writeString(struct.operationName);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, OperationID struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(2);
      if (incoming.get(0)) {
        struct.typeName = iprot.readString();
        struct.setTypeNameIsSet(true);
      }
      if (incoming.get(1)) {
        struct.operationName = iprot.readString();
        struct.setOperationNameIsSet(true);
      }
    }
  }

}

