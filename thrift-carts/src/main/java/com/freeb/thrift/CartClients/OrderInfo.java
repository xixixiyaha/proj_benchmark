/**
 * Autogenerated by Thrift Compiler (0.9.3)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.freeb.thrift.CartClients;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import javax.annotation.Generated;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked"})
@Generated(value = "Autogenerated by Thrift Compiler (0.9.3)", date = "2022-01-21")
public class OrderInfo implements org.apache.thrift.TBase<OrderInfo, OrderInfo._Fields>, java.io.Serializable, Cloneable, Comparable<OrderInfo> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("OrderInfo");

  private static final org.apache.thrift.protocol.TField ORDER_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("orderId", org.apache.thrift.protocol.TType.I64, (short)1);
  private static final org.apache.thrift.protocol.TField USER_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("userId", org.apache.thrift.protocol.TType.I64, (short)2);
  private static final org.apache.thrift.protocol.TField PAYMENT_STATUS_FIELD_DESC = new org.apache.thrift.protocol.TField("paymentStatus", org.apache.thrift.protocol.TType.I32, (short)3);
  private static final org.apache.thrift.protocol.TField MERCHANT_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("merchantId", org.apache.thrift.protocol.TType.I64, (short)4);
  private static final org.apache.thrift.protocol.TField MERCHANT_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("merchantName", org.apache.thrift.protocol.TType.STRING, (short)5);
  private static final org.apache.thrift.protocol.TField PROD_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("prodId", org.apache.thrift.protocol.TType.I64, (short)6);
  private static final org.apache.thrift.protocol.TField PROD_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("prodName", org.apache.thrift.protocol.TType.STRING, (short)7);
  private static final org.apache.thrift.protocol.TField PAYMENT_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("paymentId", org.apache.thrift.protocol.TType.I64, (short)8);
  private static final org.apache.thrift.protocol.TField CART_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("cartId", org.apache.thrift.protocol.TType.I64, (short)9);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new OrderInfoStandardSchemeFactory());
    schemes.put(TupleScheme.class, new OrderInfoTupleSchemeFactory());
  }

  public long orderId; // required
  public long userId; // required
  public int paymentStatus; // optional
  public long merchantId; // optional
  public String merchantName; // optional
  public long prodId; // required
  public String prodName; // required
  public long paymentId; // optional
  public long cartId; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    ORDER_ID((short)1, "orderId"),
    USER_ID((short)2, "userId"),
    PAYMENT_STATUS((short)3, "paymentStatus"),
    MERCHANT_ID((short)4, "merchantId"),
    MERCHANT_NAME((short)5, "merchantName"),
    PROD_ID((short)6, "prodId"),
    PROD_NAME((short)7, "prodName"),
    PAYMENT_ID((short)8, "paymentId"),
    CART_ID((short)9, "cartId");

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
        case 1: // ORDER_ID
          return ORDER_ID;
        case 2: // USER_ID
          return USER_ID;
        case 3: // PAYMENT_STATUS
          return PAYMENT_STATUS;
        case 4: // MERCHANT_ID
          return MERCHANT_ID;
        case 5: // MERCHANT_NAME
          return MERCHANT_NAME;
        case 6: // PROD_ID
          return PROD_ID;
        case 7: // PROD_NAME
          return PROD_NAME;
        case 8: // PAYMENT_ID
          return PAYMENT_ID;
        case 9: // CART_ID
          return CART_ID;
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
  private static final int __ORDERID_ISSET_ID = 0;
  private static final int __USERID_ISSET_ID = 1;
  private static final int __PAYMENTSTATUS_ISSET_ID = 2;
  private static final int __MERCHANTID_ISSET_ID = 3;
  private static final int __PRODID_ISSET_ID = 4;
  private static final int __PAYMENTID_ISSET_ID = 5;
  private static final int __CARTID_ISSET_ID = 6;
  private byte __isset_bitfield = 0;
  private static final _Fields optionals[] = {_Fields.PAYMENT_STATUS,_Fields.MERCHANT_ID,_Fields.MERCHANT_NAME,_Fields.PAYMENT_ID,_Fields.CART_ID};
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.ORDER_ID, new org.apache.thrift.meta_data.FieldMetaData("orderId", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.USER_ID, new org.apache.thrift.meta_data.FieldMetaData("userId", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.PAYMENT_STATUS, new org.apache.thrift.meta_data.FieldMetaData("paymentStatus", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.MERCHANT_ID, new org.apache.thrift.meta_data.FieldMetaData("merchantId", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.MERCHANT_NAME, new org.apache.thrift.meta_data.FieldMetaData("merchantName", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.PROD_ID, new org.apache.thrift.meta_data.FieldMetaData("prodId", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.PROD_NAME, new org.apache.thrift.meta_data.FieldMetaData("prodName", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.PAYMENT_ID, new org.apache.thrift.meta_data.FieldMetaData("paymentId", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.CART_ID, new org.apache.thrift.meta_data.FieldMetaData("cartId", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(OrderInfo.class, metaDataMap);
  }

  public OrderInfo() {
  }

  public OrderInfo(
    long orderId,
    long userId,
    long prodId,
    String prodName)
  {
    this();
    this.orderId = orderId;
    setOrderIdIsSet(true);
    this.userId = userId;
    setUserIdIsSet(true);
    this.prodId = prodId;
    setProdIdIsSet(true);
    this.prodName = prodName;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public OrderInfo(OrderInfo other) {
    __isset_bitfield = other.__isset_bitfield;
    this.orderId = other.orderId;
    this.userId = other.userId;
    this.paymentStatus = other.paymentStatus;
    this.merchantId = other.merchantId;
    if (other.isSetMerchantName()) {
      this.merchantName = other.merchantName;
    }
    this.prodId = other.prodId;
    if (other.isSetProdName()) {
      this.prodName = other.prodName;
    }
    this.paymentId = other.paymentId;
    this.cartId = other.cartId;
  }

  public OrderInfo deepCopy() {
    return new OrderInfo(this);
  }

  @Override
  public void clear() {
    setOrderIdIsSet(false);
    this.orderId = 0;
    setUserIdIsSet(false);
    this.userId = 0;
    setPaymentStatusIsSet(false);
    this.paymentStatus = 0;
    setMerchantIdIsSet(false);
    this.merchantId = 0;
    this.merchantName = null;
    setProdIdIsSet(false);
    this.prodId = 0;
    this.prodName = null;
    setPaymentIdIsSet(false);
    this.paymentId = 0;
    setCartIdIsSet(false);
    this.cartId = 0;
  }

  public long getOrderId() {
    return this.orderId;
  }

  public OrderInfo setOrderId(long orderId) {
    this.orderId = orderId;
    setOrderIdIsSet(true);
    return this;
  }

  public void unsetOrderId() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __ORDERID_ISSET_ID);
  }

  /** Returns true if field orderId is set (has been assigned a value) and false otherwise */
  public boolean isSetOrderId() {
    return EncodingUtils.testBit(__isset_bitfield, __ORDERID_ISSET_ID);
  }

  public void setOrderIdIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __ORDERID_ISSET_ID, value);
  }

  public long getUserId() {
    return this.userId;
  }

  public OrderInfo setUserId(long userId) {
    this.userId = userId;
    setUserIdIsSet(true);
    return this;
  }

  public void unsetUserId() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __USERID_ISSET_ID);
  }

  /** Returns true if field userId is set (has been assigned a value) and false otherwise */
  public boolean isSetUserId() {
    return EncodingUtils.testBit(__isset_bitfield, __USERID_ISSET_ID);
  }

  public void setUserIdIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __USERID_ISSET_ID, value);
  }

  public int getPaymentStatus() {
    return this.paymentStatus;
  }

  public OrderInfo setPaymentStatus(int paymentStatus) {
    this.paymentStatus = paymentStatus;
    setPaymentStatusIsSet(true);
    return this;
  }

  public void unsetPaymentStatus() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __PAYMENTSTATUS_ISSET_ID);
  }

  /** Returns true if field paymentStatus is set (has been assigned a value) and false otherwise */
  public boolean isSetPaymentStatus() {
    return EncodingUtils.testBit(__isset_bitfield, __PAYMENTSTATUS_ISSET_ID);
  }

  public void setPaymentStatusIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __PAYMENTSTATUS_ISSET_ID, value);
  }

  public long getMerchantId() {
    return this.merchantId;
  }

  public OrderInfo setMerchantId(long merchantId) {
    this.merchantId = merchantId;
    setMerchantIdIsSet(true);
    return this;
  }

  public void unsetMerchantId() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __MERCHANTID_ISSET_ID);
  }

  /** Returns true if field merchantId is set (has been assigned a value) and false otherwise */
  public boolean isSetMerchantId() {
    return EncodingUtils.testBit(__isset_bitfield, __MERCHANTID_ISSET_ID);
  }

  public void setMerchantIdIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __MERCHANTID_ISSET_ID, value);
  }

  public String getMerchantName() {
    return this.merchantName;
  }

  public OrderInfo setMerchantName(String merchantName) {
    this.merchantName = merchantName;
    return this;
  }

  public void unsetMerchantName() {
    this.merchantName = null;
  }

  /** Returns true if field merchantName is set (has been assigned a value) and false otherwise */
  public boolean isSetMerchantName() {
    return this.merchantName != null;
  }

  public void setMerchantNameIsSet(boolean value) {
    if (!value) {
      this.merchantName = null;
    }
  }

  public long getProdId() {
    return this.prodId;
  }

  public OrderInfo setProdId(long prodId) {
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

  public String getProdName() {
    return this.prodName;
  }

  public OrderInfo setProdName(String prodName) {
    this.prodName = prodName;
    return this;
  }

  public void unsetProdName() {
    this.prodName = null;
  }

  /** Returns true if field prodName is set (has been assigned a value) and false otherwise */
  public boolean isSetProdName() {
    return this.prodName != null;
  }

  public void setProdNameIsSet(boolean value) {
    if (!value) {
      this.prodName = null;
    }
  }

  public long getPaymentId() {
    return this.paymentId;
  }

  public OrderInfo setPaymentId(long paymentId) {
    this.paymentId = paymentId;
    setPaymentIdIsSet(true);
    return this;
  }

  public void unsetPaymentId() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __PAYMENTID_ISSET_ID);
  }

  /** Returns true if field paymentId is set (has been assigned a value) and false otherwise */
  public boolean isSetPaymentId() {
    return EncodingUtils.testBit(__isset_bitfield, __PAYMENTID_ISSET_ID);
  }

  public void setPaymentIdIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __PAYMENTID_ISSET_ID, value);
  }

  public long getCartId() {
    return this.cartId;
  }

  public OrderInfo setCartId(long cartId) {
    this.cartId = cartId;
    setCartIdIsSet(true);
    return this;
  }

  public void unsetCartId() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __CARTID_ISSET_ID);
  }

  /** Returns true if field cartId is set (has been assigned a value) and false otherwise */
  public boolean isSetCartId() {
    return EncodingUtils.testBit(__isset_bitfield, __CARTID_ISSET_ID);
  }

  public void setCartIdIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __CARTID_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case ORDER_ID:
      if (value == null) {
        unsetOrderId();
      } else {
        setOrderId((Long)value);
      }
      break;

    case USER_ID:
      if (value == null) {
        unsetUserId();
      } else {
        setUserId((Long)value);
      }
      break;

    case PAYMENT_STATUS:
      if (value == null) {
        unsetPaymentStatus();
      } else {
        setPaymentStatus((Integer)value);
      }
      break;

    case MERCHANT_ID:
      if (value == null) {
        unsetMerchantId();
      } else {
        setMerchantId((Long)value);
      }
      break;

    case MERCHANT_NAME:
      if (value == null) {
        unsetMerchantName();
      } else {
        setMerchantName((String)value);
      }
      break;

    case PROD_ID:
      if (value == null) {
        unsetProdId();
      } else {
        setProdId((Long)value);
      }
      break;

    case PROD_NAME:
      if (value == null) {
        unsetProdName();
      } else {
        setProdName((String)value);
      }
      break;

    case PAYMENT_ID:
      if (value == null) {
        unsetPaymentId();
      } else {
        setPaymentId((Long)value);
      }
      break;

    case CART_ID:
      if (value == null) {
        unsetCartId();
      } else {
        setCartId((Long)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case ORDER_ID:
      return getOrderId();

    case USER_ID:
      return getUserId();

    case PAYMENT_STATUS:
      return getPaymentStatus();

    case MERCHANT_ID:
      return getMerchantId();

    case MERCHANT_NAME:
      return getMerchantName();

    case PROD_ID:
      return getProdId();

    case PROD_NAME:
      return getProdName();

    case PAYMENT_ID:
      return getPaymentId();

    case CART_ID:
      return getCartId();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case ORDER_ID:
      return isSetOrderId();
    case USER_ID:
      return isSetUserId();
    case PAYMENT_STATUS:
      return isSetPaymentStatus();
    case MERCHANT_ID:
      return isSetMerchantId();
    case MERCHANT_NAME:
      return isSetMerchantName();
    case PROD_ID:
      return isSetProdId();
    case PROD_NAME:
      return isSetProdName();
    case PAYMENT_ID:
      return isSetPaymentId();
    case CART_ID:
      return isSetCartId();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof OrderInfo)
      return this.equals((OrderInfo)that);
    return false;
  }

  public boolean equals(OrderInfo that) {
    if (that == null)
      return false;

    boolean this_present_orderId = true;
    boolean that_present_orderId = true;
    if (this_present_orderId || that_present_orderId) {
      if (!(this_present_orderId && that_present_orderId))
        return false;
      if (this.orderId != that.orderId)
        return false;
    }

    boolean this_present_userId = true;
    boolean that_present_userId = true;
    if (this_present_userId || that_present_userId) {
      if (!(this_present_userId && that_present_userId))
        return false;
      if (this.userId != that.userId)
        return false;
    }

    boolean this_present_paymentStatus = true && this.isSetPaymentStatus();
    boolean that_present_paymentStatus = true && that.isSetPaymentStatus();
    if (this_present_paymentStatus || that_present_paymentStatus) {
      if (!(this_present_paymentStatus && that_present_paymentStatus))
        return false;
      if (this.paymentStatus != that.paymentStatus)
        return false;
    }

    boolean this_present_merchantId = true && this.isSetMerchantId();
    boolean that_present_merchantId = true && that.isSetMerchantId();
    if (this_present_merchantId || that_present_merchantId) {
      if (!(this_present_merchantId && that_present_merchantId))
        return false;
      if (this.merchantId != that.merchantId)
        return false;
    }

    boolean this_present_merchantName = true && this.isSetMerchantName();
    boolean that_present_merchantName = true && that.isSetMerchantName();
    if (this_present_merchantName || that_present_merchantName) {
      if (!(this_present_merchantName && that_present_merchantName))
        return false;
      if (!this.merchantName.equals(that.merchantName))
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

    boolean this_present_prodName = true && this.isSetProdName();
    boolean that_present_prodName = true && that.isSetProdName();
    if (this_present_prodName || that_present_prodName) {
      if (!(this_present_prodName && that_present_prodName))
        return false;
      if (!this.prodName.equals(that.prodName))
        return false;
    }

    boolean this_present_paymentId = true && this.isSetPaymentId();
    boolean that_present_paymentId = true && that.isSetPaymentId();
    if (this_present_paymentId || that_present_paymentId) {
      if (!(this_present_paymentId && that_present_paymentId))
        return false;
      if (this.paymentId != that.paymentId)
        return false;
    }

    boolean this_present_cartId = true && this.isSetCartId();
    boolean that_present_cartId = true && that.isSetCartId();
    if (this_present_cartId || that_present_cartId) {
      if (!(this_present_cartId && that_present_cartId))
        return false;
      if (this.cartId != that.cartId)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_orderId = true;
    list.add(present_orderId);
    if (present_orderId)
      list.add(orderId);

    boolean present_userId = true;
    list.add(present_userId);
    if (present_userId)
      list.add(userId);

    boolean present_paymentStatus = true && (isSetPaymentStatus());
    list.add(present_paymentStatus);
    if (present_paymentStatus)
      list.add(paymentStatus);

    boolean present_merchantId = true && (isSetMerchantId());
    list.add(present_merchantId);
    if (present_merchantId)
      list.add(merchantId);

    boolean present_merchantName = true && (isSetMerchantName());
    list.add(present_merchantName);
    if (present_merchantName)
      list.add(merchantName);

    boolean present_prodId = true;
    list.add(present_prodId);
    if (present_prodId)
      list.add(prodId);

    boolean present_prodName = true && (isSetProdName());
    list.add(present_prodName);
    if (present_prodName)
      list.add(prodName);

    boolean present_paymentId = true && (isSetPaymentId());
    list.add(present_paymentId);
    if (present_paymentId)
      list.add(paymentId);

    boolean present_cartId = true && (isSetCartId());
    list.add(present_cartId);
    if (present_cartId)
      list.add(cartId);

    return list.hashCode();
  }

  @Override
  public int compareTo(OrderInfo other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetOrderId()).compareTo(other.isSetOrderId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetOrderId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.orderId, other.orderId);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetUserId()).compareTo(other.isSetUserId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetUserId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.userId, other.userId);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetPaymentStatus()).compareTo(other.isSetPaymentStatus());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetPaymentStatus()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.paymentStatus, other.paymentStatus);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetMerchantId()).compareTo(other.isSetMerchantId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMerchantId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.merchantId, other.merchantId);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetMerchantName()).compareTo(other.isSetMerchantName());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMerchantName()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.merchantName, other.merchantName);
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
    lastComparison = Boolean.valueOf(isSetProdName()).compareTo(other.isSetProdName());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetProdName()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.prodName, other.prodName);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetPaymentId()).compareTo(other.isSetPaymentId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetPaymentId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.paymentId, other.paymentId);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetCartId()).compareTo(other.isSetCartId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetCartId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.cartId, other.cartId);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("OrderInfo(");
    boolean first = true;

    sb.append("orderId:");
    sb.append(this.orderId);
    first = false;
    if (!first) sb.append(", ");
    sb.append("userId:");
    sb.append(this.userId);
    first = false;
    if (isSetPaymentStatus()) {
      if (!first) sb.append(", ");
      sb.append("paymentStatus:");
      sb.append(this.paymentStatus);
      first = false;
    }
    if (isSetMerchantId()) {
      if (!first) sb.append(", ");
      sb.append("merchantId:");
      sb.append(this.merchantId);
      first = false;
    }
    if (isSetMerchantName()) {
      if (!first) sb.append(", ");
      sb.append("merchantName:");
      if (this.merchantName == null) {
        sb.append("null");
      } else {
        sb.append(this.merchantName);
      }
      first = false;
    }
    if (!first) sb.append(", ");
    sb.append("prodId:");
    sb.append(this.prodId);
    first = false;
    if (!first) sb.append(", ");
    sb.append("prodName:");
    if (this.prodName == null) {
      sb.append("null");
    } else {
      sb.append(this.prodName);
    }
    first = false;
    if (isSetPaymentId()) {
      if (!first) sb.append(", ");
      sb.append("paymentId:");
      sb.append(this.paymentId);
      first = false;
    }
    if (isSetCartId()) {
      if (!first) sb.append(", ");
      sb.append("cartId:");
      sb.append(this.cartId);
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws TException {
    // check for required fields
    // alas, we cannot check 'orderId' because it's a primitive and you chose the non-beans generator.
    // alas, we cannot check 'userId' because it's a primitive and you chose the non-beans generator.
    // alas, we cannot check 'prodId' because it's a primitive and you chose the non-beans generator.
    if (prodName == null) {
      throw new TProtocolException("Required field 'prodName' was not present! Struct: " + toString());
    }
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class OrderInfoStandardSchemeFactory implements SchemeFactory {
    public OrderInfoStandardScheme getScheme() {
      return new OrderInfoStandardScheme();
    }
  }

  private static class OrderInfoStandardScheme extends StandardScheme<OrderInfo> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, OrderInfo struct) throws TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // ORDER_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.orderId = iprot.readI64();
              struct.setOrderIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // USER_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.userId = iprot.readI64();
              struct.setUserIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // PAYMENT_STATUS
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.paymentStatus = iprot.readI32();
              struct.setPaymentStatusIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // MERCHANT_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.merchantId = iprot.readI64();
              struct.setMerchantIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 5: // MERCHANT_NAME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.merchantName = iprot.readString();
              struct.setMerchantNameIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 6: // PROD_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.prodId = iprot.readI64();
              struct.setProdIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 7: // PROD_NAME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.prodName = iprot.readString();
              struct.setProdNameIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 8: // PAYMENT_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.paymentId = iprot.readI64();
              struct.setPaymentIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 9: // CART_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.cartId = iprot.readI64();
              struct.setCartIdIsSet(true);
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
      if (!struct.isSetOrderId()) {
        throw new TProtocolException("Required field 'orderId' was not found in serialized data! Struct: " + toString());
      }
      if (!struct.isSetUserId()) {
        throw new TProtocolException("Required field 'userId' was not found in serialized data! Struct: " + toString());
      }
      if (!struct.isSetProdId()) {
        throw new TProtocolException("Required field 'prodId' was not found in serialized data! Struct: " + toString());
      }
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, OrderInfo struct) throws TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(ORDER_ID_FIELD_DESC);
      oprot.writeI64(struct.orderId);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(USER_ID_FIELD_DESC);
      oprot.writeI64(struct.userId);
      oprot.writeFieldEnd();
      if (struct.isSetPaymentStatus()) {
        oprot.writeFieldBegin(PAYMENT_STATUS_FIELD_DESC);
        oprot.writeI32(struct.paymentStatus);
        oprot.writeFieldEnd();
      }
      if (struct.isSetMerchantId()) {
        oprot.writeFieldBegin(MERCHANT_ID_FIELD_DESC);
        oprot.writeI64(struct.merchantId);
        oprot.writeFieldEnd();
      }
      if (struct.merchantName != null) {
        if (struct.isSetMerchantName()) {
          oprot.writeFieldBegin(MERCHANT_NAME_FIELD_DESC);
          oprot.writeString(struct.merchantName);
          oprot.writeFieldEnd();
        }
      }
      oprot.writeFieldBegin(PROD_ID_FIELD_DESC);
      oprot.writeI64(struct.prodId);
      oprot.writeFieldEnd();
      if (struct.prodName != null) {
        oprot.writeFieldBegin(PROD_NAME_FIELD_DESC);
        oprot.writeString(struct.prodName);
        oprot.writeFieldEnd();
      }
      if (struct.isSetPaymentId()) {
        oprot.writeFieldBegin(PAYMENT_ID_FIELD_DESC);
        oprot.writeI64(struct.paymentId);
        oprot.writeFieldEnd();
      }
      if (struct.isSetCartId()) {
        oprot.writeFieldBegin(CART_ID_FIELD_DESC);
        oprot.writeI64(struct.cartId);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class OrderInfoTupleSchemeFactory implements SchemeFactory {
    public OrderInfoTupleScheme getScheme() {
      return new OrderInfoTupleScheme();
    }
  }

  private static class OrderInfoTupleScheme extends TupleScheme<OrderInfo> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, OrderInfo struct) throws TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      oprot.writeI64(struct.orderId);
      oprot.writeI64(struct.userId);
      oprot.writeI64(struct.prodId);
      oprot.writeString(struct.prodName);
      BitSet optionals = new BitSet();
      if (struct.isSetPaymentStatus()) {
        optionals.set(0);
      }
      if (struct.isSetMerchantId()) {
        optionals.set(1);
      }
      if (struct.isSetMerchantName()) {
        optionals.set(2);
      }
      if (struct.isSetPaymentId()) {
        optionals.set(3);
      }
      if (struct.isSetCartId()) {
        optionals.set(4);
      }
      oprot.writeBitSet(optionals, 5);
      if (struct.isSetPaymentStatus()) {
        oprot.writeI32(struct.paymentStatus);
      }
      if (struct.isSetMerchantId()) {
        oprot.writeI64(struct.merchantId);
      }
      if (struct.isSetMerchantName()) {
        oprot.writeString(struct.merchantName);
      }
      if (struct.isSetPaymentId()) {
        oprot.writeI64(struct.paymentId);
      }
      if (struct.isSetCartId()) {
        oprot.writeI64(struct.cartId);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, OrderInfo struct) throws TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      struct.orderId = iprot.readI64();
      struct.setOrderIdIsSet(true);
      struct.userId = iprot.readI64();
      struct.setUserIdIsSet(true);
      struct.prodId = iprot.readI64();
      struct.setProdIdIsSet(true);
      struct.prodName = iprot.readString();
      struct.setProdNameIsSet(true);
      BitSet incoming = iprot.readBitSet(5);
      if (incoming.get(0)) {
        struct.paymentStatus = iprot.readI32();
        struct.setPaymentStatusIsSet(true);
      }
      if (incoming.get(1)) {
        struct.merchantId = iprot.readI64();
        struct.setMerchantIdIsSet(true);
      }
      if (incoming.get(2)) {
        struct.merchantName = iprot.readString();
        struct.setMerchantNameIsSet(true);
      }
      if (incoming.get(3)) {
        struct.paymentId = iprot.readI64();
        struct.setPaymentIdIsSet(true);
      }
      if (incoming.get(4)) {
        struct.cartId = iprot.readI64();
        struct.setCartIdIsSet(true);
      }
    }
  }

}

