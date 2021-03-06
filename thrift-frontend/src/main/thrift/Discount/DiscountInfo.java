/**
 * Autogenerated by Thrift Compiler (0.9.3)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.freeb.thrift;

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
import javax.annotation.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked"})
@Generated(value = "Autogenerated by Thrift Compiler (0.9.3)", date = "2022-01-24")
public class DiscountInfo implements org.apache.thrift.TBase<DiscountInfo, DiscountInfo._Fields>, java.io.Serializable, Cloneable, Comparable<DiscountInfo> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("DiscountInfo");

  private static final org.apache.thrift.protocol.TField DISCOUNT_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("discountId", org.apache.thrift.protocol.TType.I64, (short)1);
  private static final org.apache.thrift.protocol.TField DISCOUNT_TYPE_FIELD_DESC = new org.apache.thrift.protocol.TField("discountType", org.apache.thrift.protocol.TType.I32, (short)2);
  private static final org.apache.thrift.protocol.TField PROD_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("prodId", org.apache.thrift.protocol.TType.I64, (short)3);
  private static final org.apache.thrift.protocol.TField DISCOUNT_VAL_FIELD_DESC = new org.apache.thrift.protocol.TField("discountVal", org.apache.thrift.protocol.TType.DOUBLE, (short)4);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new DiscountInfoStandardSchemeFactory());
    schemes.put(TupleScheme.class, new DiscountInfoTupleSchemeFactory());
  }

  public long discountId; // required
  public int discountType; // optional
  public long prodId; // required
  public double discountVal; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    DISCOUNT_ID((short)1, "discountId"),
    DISCOUNT_TYPE((short)2, "discountType"),
    PROD_ID((short)3, "prodId"),
    DISCOUNT_VAL((short)4, "discountVal");

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
        case 1: // DISCOUNT_ID
          return DISCOUNT_ID;
        case 2: // DISCOUNT_TYPE
          return DISCOUNT_TYPE;
        case 3: // PROD_ID
          return PROD_ID;
        case 4: // DISCOUNT_VAL
          return DISCOUNT_VAL;
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
  private static final int __DISCOUNTID_ISSET_ID = 0;
  private static final int __DISCOUNTTYPE_ISSET_ID = 1;
  private static final int __PRODID_ISSET_ID = 2;
  private static final int __DISCOUNTVAL_ISSET_ID = 3;
  private byte __isset_bitfield = 0;
  private static final _Fields optionals[] = {_Fields.DISCOUNT_TYPE};
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.DISCOUNT_ID, new org.apache.thrift.meta_data.FieldMetaData("discountId", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.DISCOUNT_TYPE, new org.apache.thrift.meta_data.FieldMetaData("discountType", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.PROD_ID, new org.apache.thrift.meta_data.FieldMetaData("prodId", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.DISCOUNT_VAL, new org.apache.thrift.meta_data.FieldMetaData("discountVal", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.DOUBLE)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(DiscountInfo.class, metaDataMap);
  }

  public DiscountInfo() {
  }

  public DiscountInfo(
    long discountId,
    long prodId,
    double discountVal)
  {
    this();
    this.discountId = discountId;
    setDiscountIdIsSet(true);
    this.prodId = prodId;
    setProdIdIsSet(true);
    this.discountVal = discountVal;
    setDiscountValIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public DiscountInfo(DiscountInfo other) {
    __isset_bitfield = other.__isset_bitfield;
    this.discountId = other.discountId;
    this.discountType = other.discountType;
    this.prodId = other.prodId;
    this.discountVal = other.discountVal;
  }

  public DiscountInfo deepCopy() {
    return new DiscountInfo(this);
  }

  @Override
  public void clear() {
    setDiscountIdIsSet(false);
    this.discountId = 0;
    setDiscountTypeIsSet(false);
    this.discountType = 0;
    setProdIdIsSet(false);
    this.prodId = 0;
    setDiscountValIsSet(false);
    this.discountVal = 0.0;
  }

  public long getDiscountId() {
    return this.discountId;
  }

  public DiscountInfo setDiscountId(long discountId) {
    this.discountId = discountId;
    setDiscountIdIsSet(true);
    return this;
  }

  public void unsetDiscountId() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __DISCOUNTID_ISSET_ID);
  }

  /** Returns true if field discountId is set (has been assigned a value) and false otherwise */
  public boolean isSetDiscountId() {
    return EncodingUtils.testBit(__isset_bitfield, __DISCOUNTID_ISSET_ID);
  }

  public void setDiscountIdIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __DISCOUNTID_ISSET_ID, value);
  }

  public int getDiscountType() {
    return this.discountType;
  }

  public DiscountInfo setDiscountType(int discountType) {
    this.discountType = discountType;
    setDiscountTypeIsSet(true);
    return this;
  }

  public void unsetDiscountType() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __DISCOUNTTYPE_ISSET_ID);
  }

  /** Returns true if field discountType is set (has been assigned a value) and false otherwise */
  public boolean isSetDiscountType() {
    return EncodingUtils.testBit(__isset_bitfield, __DISCOUNTTYPE_ISSET_ID);
  }

  public void setDiscountTypeIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __DISCOUNTTYPE_ISSET_ID, value);
  }

  public long getProdId() {
    return this.prodId;
  }

  public DiscountInfo setProdId(long prodId) {
    this.prodId = prodId;
    setProdIdIsSet(true);
    return this;
  }

  public void unsetProdId() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __PRODID_ISSET_ID);
  }

  /** Returns true if field prodId is set (has been assigned a value) and false otherwise */
  public boolean isSetProdId() {
    return EncodingUtils.testBit(__isset_bitfield, __PRODID_ISSET_ID);
  }

  public void setProdIdIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __PRODID_ISSET_ID, value);
  }

  public double getDiscountVal() {
    return this.discountVal;
  }

  public DiscountInfo setDiscountVal(double discountVal) {
    this.discountVal = discountVal;
    setDiscountValIsSet(true);
    return this;
  }

  public void unsetDiscountVal() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __DISCOUNTVAL_ISSET_ID);
  }

  /** Returns true if field discountVal is set (has been assigned a value) and false otherwise */
  public boolean isSetDiscountVal() {
    return EncodingUtils.testBit(__isset_bitfield, __DISCOUNTVAL_ISSET_ID);
  }

  public void setDiscountValIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __DISCOUNTVAL_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case DISCOUNT_ID:
      if (value == null) {
        unsetDiscountId();
      } else {
        setDiscountId((Long)value);
      }
      break;

    case DISCOUNT_TYPE:
      if (value == null) {
        unsetDiscountType();
      } else {
        setDiscountType((Integer)value);
      }
      break;

    case PROD_ID:
      if (value == null) {
        unsetProdId();
      } else {
        setProdId((Long)value);
      }
      break;

    case DISCOUNT_VAL:
      if (value == null) {
        unsetDiscountVal();
      } else {
        setDiscountVal((Double)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case DISCOUNT_ID:
      return getDiscountId();

    case DISCOUNT_TYPE:
      return getDiscountType();

    case PROD_ID:
      return getProdId();

    case DISCOUNT_VAL:
      return getDiscountVal();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case DISCOUNT_ID:
      return isSetDiscountId();
    case DISCOUNT_TYPE:
      return isSetDiscountType();
    case PROD_ID:
      return isSetProdId();
    case DISCOUNT_VAL:
      return isSetDiscountVal();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof DiscountInfo)
      return this.equals((DiscountInfo)that);
    return false;
  }

  public boolean equals(DiscountInfo that) {
    if (that == null)
      return false;

    boolean this_present_discountId = true;
    boolean that_present_discountId = true;
    if (this_present_discountId || that_present_discountId) {
      if (!(this_present_discountId && that_present_discountId))
        return false;
      if (this.discountId != that.discountId)
        return false;
    }

    boolean this_present_discountType = true && this.isSetDiscountType();
    boolean that_present_discountType = true && that.isSetDiscountType();
    if (this_present_discountType || that_present_discountType) {
      if (!(this_present_discountType && that_present_discountType))
        return false;
      if (this.discountType != that.discountType)
        return false;
    }

    boolean this_present_prodId = true;
    boolean that_present_prodId = true;
    if (this_present_prodId || that_present_prodId) {
      if (!(this_present_prodId && that_present_prodId))
        return false;
      if (this.prodId != that.prodId)
        return false;
    }

    boolean this_present_discountVal = true;
    boolean that_present_discountVal = true;
    if (this_present_discountVal || that_present_discountVal) {
      if (!(this_present_discountVal && that_present_discountVal))
        return false;
      if (this.discountVal != that.discountVal)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_discountId = true;
    list.add(present_discountId);
    if (present_discountId)
      list.add(discountId);

    boolean present_discountType = true && (isSetDiscountType());
    list.add(present_discountType);
    if (present_discountType)
      list.add(discountType);

    boolean present_prodId = true;
    list.add(present_prodId);
    if (present_prodId)
      list.add(prodId);

    boolean present_discountVal = true;
    list.add(present_discountVal);
    if (present_discountVal)
      list.add(discountVal);

    return list.hashCode();
  }

  @Override
  public int compareTo(DiscountInfo other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetDiscountId()).compareTo(other.isSetDiscountId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetDiscountId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.discountId, other.discountId);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetDiscountType()).compareTo(other.isSetDiscountType());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetDiscountType()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.discountType, other.discountType);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetProdId()).compareTo(other.isSetProdId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetProdId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.prodId, other.prodId);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetDiscountVal()).compareTo(other.isSetDiscountVal());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetDiscountVal()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.discountVal, other.discountVal);
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
    StringBuilder sb = new StringBuilder("DiscountInfo(");
    boolean first = true;

    sb.append("discountId:");
    sb.append(this.discountId);
    first = false;
    if (isSetDiscountType()) {
      if (!first) sb.append(", ");
      sb.append("discountType:");
      sb.append(this.discountType);
      first = false;
    }
    if (!first) sb.append(", ");
    sb.append("prodId:");
    sb.append(this.prodId);
    first = false;
    if (!first) sb.append(", ");
    sb.append("discountVal:");
    sb.append(this.discountVal);
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // alas, we cannot check 'discountId' because it's a primitive and you chose the non-beans generator.
    // alas, we cannot check 'prodId' because it's a primitive and you chose the non-beans generator.
    // alas, we cannot check 'discountVal' because it's a primitive and you chose the non-beans generator.
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
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class DiscountInfoStandardSchemeFactory implements SchemeFactory {
    public DiscountInfoStandardScheme getScheme() {
      return new DiscountInfoStandardScheme();
    }
  }

  private static class DiscountInfoStandardScheme extends StandardScheme<DiscountInfo> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, DiscountInfo struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // DISCOUNT_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.discountId = iprot.readI64();
              struct.setDiscountIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // DISCOUNT_TYPE
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.discountType = iprot.readI32();
              struct.setDiscountTypeIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // PROD_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.prodId = iprot.readI64();
              struct.setProdIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // DISCOUNT_VAL
            if (schemeField.type == org.apache.thrift.protocol.TType.DOUBLE) {
              struct.discountVal = iprot.readDouble();
              struct.setDiscountValIsSet(true);
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
      if (!struct.isSetDiscountId()) {
        throw new org.apache.thrift.protocol.TProtocolException("Required field 'discountId' was not found in serialized data! Struct: " + toString());
      }
      if (!struct.isSetProdId()) {
        throw new org.apache.thrift.protocol.TProtocolException("Required field 'prodId' was not found in serialized data! Struct: " + toString());
      }
      if (!struct.isSetDiscountVal()) {
        throw new org.apache.thrift.protocol.TProtocolException("Required field 'discountVal' was not found in serialized data! Struct: " + toString());
      }
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, DiscountInfo struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(DISCOUNT_ID_FIELD_DESC);
      oprot.writeI64(struct.discountId);
      oprot.writeFieldEnd();
      if (struct.isSetDiscountType()) {
        oprot.writeFieldBegin(DISCOUNT_TYPE_FIELD_DESC);
        oprot.writeI32(struct.discountType);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(PROD_ID_FIELD_DESC);
      oprot.writeI64(struct.prodId);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(DISCOUNT_VAL_FIELD_DESC);
      oprot.writeDouble(struct.discountVal);
      oprot.writeFieldEnd();
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class DiscountInfoTupleSchemeFactory implements SchemeFactory {
    public DiscountInfoTupleScheme getScheme() {
      return new DiscountInfoTupleScheme();
    }
  }

  private static class DiscountInfoTupleScheme extends TupleScheme<DiscountInfo> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, DiscountInfo struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      oprot.writeI64(struct.discountId);
      oprot.writeI64(struct.prodId);
      oprot.writeDouble(struct.discountVal);
      BitSet optionals = new BitSet();
      if (struct.isSetDiscountType()) {
        optionals.set(0);
      }
      oprot.writeBitSet(optionals, 1);
      if (struct.isSetDiscountType()) {
        oprot.writeI32(struct.discountType);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, DiscountInfo struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      struct.discountId = iprot.readI64();
      struct.setDiscountIdIsSet(true);
      struct.prodId = iprot.readI64();
      struct.setProdIdIsSet(true);
      struct.discountVal = iprot.readDouble();
      struct.setDiscountValIsSet(true);
      BitSet incoming = iprot.readBitSet(1);
      if (incoming.get(0)) {
        struct.discountType = iprot.readI32();
        struct.setDiscountTypeIsSet(true);
      }
    }
  }

}

