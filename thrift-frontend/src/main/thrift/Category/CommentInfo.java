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
@Generated(value = "Autogenerated by Thrift Compiler (0.9.3)", date = "2022-01-21")
public class CommentInfo implements org.apache.thrift.TBase<CommentInfo, CommentInfo._Fields>, java.io.Serializable, Cloneable, Comparable<CommentInfo> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("CommentInfo");

  private static final org.apache.thrift.protocol.TField COMMENT_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("commentId", org.apache.thrift.protocol.TType.I64, (short)1);
  private static final org.apache.thrift.protocol.TField USER_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("userId", org.apache.thrift.protocol.TType.I64, (short)2);
  private static final org.apache.thrift.protocol.TField USER_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("userName", org.apache.thrift.protocol.TType.STRING, (short)3);
  private static final org.apache.thrift.protocol.TField PROD_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("prodId", org.apache.thrift.protocol.TType.I64, (short)4);
  private static final org.apache.thrift.protocol.TField COMMENT_DETAILS_FIELD_DESC = new org.apache.thrift.protocol.TField("commentDetails", org.apache.thrift.protocol.TType.STRING, (short)5);
  private static final org.apache.thrift.protocol.TField COMMENT_IMAGES_FIELD_DESC = new org.apache.thrift.protocol.TField("commentImages", org.apache.thrift.protocol.TType.LIST, (short)6);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new CommentInfoStandardSchemeFactory());
    schemes.put(TupleScheme.class, new CommentInfoTupleSchemeFactory());
  }

  public long commentId; // required
  public long userId; // required
  public String userName; // optional
  public long prodId; // required
  public String commentDetails; // required
  public List<String> commentImages; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    COMMENT_ID((short)1, "commentId"),
    USER_ID((short)2, "userId"),
    USER_NAME((short)3, "userName"),
    PROD_ID((short)4, "prodId"),
    COMMENT_DETAILS((short)5, "commentDetails"),
    COMMENT_IMAGES((short)6, "commentImages");

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
        case 1: // COMMENT_ID
          return COMMENT_ID;
        case 2: // USER_ID
          return USER_ID;
        case 3: // USER_NAME
          return USER_NAME;
        case 4: // PROD_ID
          return PROD_ID;
        case 5: // COMMENT_DETAILS
          return COMMENT_DETAILS;
        case 6: // COMMENT_IMAGES
          return COMMENT_IMAGES;
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
  private static final int __COMMENTID_ISSET_ID = 0;
  private static final int __USERID_ISSET_ID = 1;
  private static final int __PRODID_ISSET_ID = 2;
  private byte __isset_bitfield = 0;
  private static final _Fields optionals[] = {_Fields.USER_NAME,_Fields.COMMENT_IMAGES};
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.COMMENT_ID, new org.apache.thrift.meta_data.FieldMetaData("commentId", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.USER_ID, new org.apache.thrift.meta_data.FieldMetaData("userId", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.USER_NAME, new org.apache.thrift.meta_data.FieldMetaData("userName", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.PROD_ID, new org.apache.thrift.meta_data.FieldMetaData("prodId", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.COMMENT_DETAILS, new org.apache.thrift.meta_data.FieldMetaData("commentDetails", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.COMMENT_IMAGES, new org.apache.thrift.meta_data.FieldMetaData("commentImages", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING))));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(CommentInfo.class, metaDataMap);
  }

  public CommentInfo() {
  }

  public CommentInfo(
    long commentId,
    long userId,
    long prodId,
    String commentDetails)
  {
    this();
    this.commentId = commentId;
    setCommentIdIsSet(true);
    this.userId = userId;
    setUserIdIsSet(true);
    this.prodId = prodId;
    setProdIdIsSet(true);
    this.commentDetails = commentDetails;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public CommentInfo(CommentInfo other) {
    __isset_bitfield = other.__isset_bitfield;
    this.commentId = other.commentId;
    this.userId = other.userId;
    if (other.isSetUserName()) {
      this.userName = other.userName;
    }
    this.prodId = other.prodId;
    if (other.isSetCommentDetails()) {
      this.commentDetails = other.commentDetails;
    }
    if (other.isSetCommentImages()) {
      List<String> __this__commentImages = new ArrayList<String>(other.commentImages);
      this.commentImages = __this__commentImages;
    }
  }

  public CommentInfo deepCopy() {
    return new CommentInfo(this);
  }

  @Override
  public void clear() {
    setCommentIdIsSet(false);
    this.commentId = 0;
    setUserIdIsSet(false);
    this.userId = 0;
    this.userName = null;
    setProdIdIsSet(false);
    this.prodId = 0;
    this.commentDetails = null;
    this.commentImages = null;
  }

  public long getCommentId() {
    return this.commentId;
  }

  public CommentInfo setCommentId(long commentId) {
    this.commentId = commentId;
    setCommentIdIsSet(true);
    return this;
  }

  public void unsetCommentId() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __COMMENTID_ISSET_ID);
  }

  /** Returns true if field commentId is set (has been assigned a value) and false otherwise */
  public boolean isSetCommentId() {
    return EncodingUtils.testBit(__isset_bitfield, __COMMENTID_ISSET_ID);
  }

  public void setCommentIdIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __COMMENTID_ISSET_ID, value);
  }

  public long getUserId() {
    return this.userId;
  }

  public CommentInfo setUserId(long userId) {
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

  public String getUserName() {
    return this.userName;
  }

  public CommentInfo setUserName(String userName) {
    this.userName = userName;
    return this;
  }

  public void unsetUserName() {
    this.userName = null;
  }

  /** Returns true if field userName is set (has been assigned a value) and false otherwise */
  public boolean isSetUserName() {
    return this.userName != null;
  }

  public void setUserNameIsSet(boolean value) {
    if (!value) {
      this.userName = null;
    }
  }

  public long getProdId() {
    return this.prodId;
  }

  public CommentInfo setProdId(long prodId) {
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

  public String getCommentDetails() {
    return this.commentDetails;
  }

  public CommentInfo setCommentDetails(String commentDetails) {
    this.commentDetails = commentDetails;
    return this;
  }

  public void unsetCommentDetails() {
    this.commentDetails = null;
  }

  /** Returns true if field commentDetails is set (has been assigned a value) and false otherwise */
  public boolean isSetCommentDetails() {
    return this.commentDetails != null;
  }

  public void setCommentDetailsIsSet(boolean value) {
    if (!value) {
      this.commentDetails = null;
    }
  }

  public int getCommentImagesSize() {
    return (this.commentImages == null) ? 0 : this.commentImages.size();
  }

  public java.util.Iterator<String> getCommentImagesIterator() {
    return (this.commentImages == null) ? null : this.commentImages.iterator();
  }

  public void addToCommentImages(String elem) {
    if (this.commentImages == null) {
      this.commentImages = new ArrayList<String>();
    }
    this.commentImages.add(elem);
  }

  public List<String> getCommentImages() {
    return this.commentImages;
  }

  public CommentInfo setCommentImages(List<String> commentImages) {
    this.commentImages = commentImages;
    return this;
  }

  public void unsetCommentImages() {
    this.commentImages = null;
  }

  /** Returns true if field commentImages is set (has been assigned a value) and false otherwise */
  public boolean isSetCommentImages() {
    return this.commentImages != null;
  }

  public void setCommentImagesIsSet(boolean value) {
    if (!value) {
      this.commentImages = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case COMMENT_ID:
      if (value == null) {
        unsetCommentId();
      } else {
        setCommentId((Long)value);
      }
      break;

    case USER_ID:
      if (value == null) {
        unsetUserId();
      } else {
        setUserId((Long)value);
      }
      break;

    case USER_NAME:
      if (value == null) {
        unsetUserName();
      } else {
        setUserName((String)value);
      }
      break;

    case PROD_ID:
      if (value == null) {
        unsetProdId();
      } else {
        setProdId((Long)value);
      }
      break;

    case COMMENT_DETAILS:
      if (value == null) {
        unsetCommentDetails();
      } else {
        setCommentDetails((String)value);
      }
      break;

    case COMMENT_IMAGES:
      if (value == null) {
        unsetCommentImages();
      } else {
        setCommentImages((List<String>)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case COMMENT_ID:
      return getCommentId();

    case USER_ID:
      return getUserId();

    case USER_NAME:
      return getUserName();

    case PROD_ID:
      return getProdId();

    case COMMENT_DETAILS:
      return getCommentDetails();

    case COMMENT_IMAGES:
      return getCommentImages();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case COMMENT_ID:
      return isSetCommentId();
    case USER_ID:
      return isSetUserId();
    case USER_NAME:
      return isSetUserName();
    case PROD_ID:
      return isSetProdId();
    case COMMENT_DETAILS:
      return isSetCommentDetails();
    case COMMENT_IMAGES:
      return isSetCommentImages();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof CommentInfo)
      return this.equals((CommentInfo)that);
    return false;
  }

  public boolean equals(CommentInfo that) {
    if (that == null)
      return false;

    boolean this_present_commentId = true;
    boolean that_present_commentId = true;
    if (this_present_commentId || that_present_commentId) {
      if (!(this_present_commentId && that_present_commentId))
        return false;
      if (this.commentId != that.commentId)
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

    boolean this_present_userName = true && this.isSetUserName();
    boolean that_present_userName = true && that.isSetUserName();
    if (this_present_userName || that_present_userName) {
      if (!(this_present_userName && that_present_userName))
        return false;
      if (!this.userName.equals(that.userName))
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

    boolean this_present_commentDetails = true && this.isSetCommentDetails();
    boolean that_present_commentDetails = true && that.isSetCommentDetails();
    if (this_present_commentDetails || that_present_commentDetails) {
      if (!(this_present_commentDetails && that_present_commentDetails))
        return false;
      if (!this.commentDetails.equals(that.commentDetails))
        return false;
    }

    boolean this_present_commentImages = true && this.isSetCommentImages();
    boolean that_present_commentImages = true && that.isSetCommentImages();
    if (this_present_commentImages || that_present_commentImages) {
      if (!(this_present_commentImages && that_present_commentImages))
        return false;
      if (!this.commentImages.equals(that.commentImages))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_commentId = true;
    list.add(present_commentId);
    if (present_commentId)
      list.add(commentId);

    boolean present_userId = true;
    list.add(present_userId);
    if (present_userId)
      list.add(userId);

    boolean present_userName = true && (isSetUserName());
    list.add(present_userName);
    if (present_userName)
      list.add(userName);

    boolean present_prodId = true;
    list.add(present_prodId);
    if (present_prodId)
      list.add(prodId);

    boolean present_commentDetails = true && (isSetCommentDetails());
    list.add(present_commentDetails);
    if (present_commentDetails)
      list.add(commentDetails);

    boolean present_commentImages = true && (isSetCommentImages());
    list.add(present_commentImages);
    if (present_commentImages)
      list.add(commentImages);

    return list.hashCode();
  }

  @Override
  public int compareTo(CommentInfo other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetCommentId()).compareTo(other.isSetCommentId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetCommentId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.commentId, other.commentId);
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
    lastComparison = Boolean.valueOf(isSetUserName()).compareTo(other.isSetUserName());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetUserName()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.userName, other.userName);
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
    lastComparison = Boolean.valueOf(isSetCommentDetails()).compareTo(other.isSetCommentDetails());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetCommentDetails()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.commentDetails, other.commentDetails);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetCommentImages()).compareTo(other.isSetCommentImages());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetCommentImages()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.commentImages, other.commentImages);
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
    StringBuilder sb = new StringBuilder("CommentInfo(");
    boolean first = true;

    sb.append("commentId:");
    sb.append(this.commentId);
    first = false;
    if (!first) sb.append(", ");
    sb.append("userId:");
    sb.append(this.userId);
    first = false;
    if (isSetUserName()) {
      if (!first) sb.append(", ");
      sb.append("userName:");
      if (this.userName == null) {
        sb.append("null");
      } else {
        sb.append(this.userName);
      }
      first = false;
    }
    if (!first) sb.append(", ");
    sb.append("prodId:");
    sb.append(this.prodId);
    first = false;
    if (!first) sb.append(", ");
    sb.append("commentDetails:");
    if (this.commentDetails == null) {
      sb.append("null");
    } else {
      sb.append(this.commentDetails);
    }
    first = false;
    if (isSetCommentImages()) {
      if (!first) sb.append(", ");
      sb.append("commentImages:");
      if (this.commentImages == null) {
        sb.append("null");
      } else {
        sb.append(this.commentImages);
      }
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // alas, we cannot check 'commentId' because it's a primitive and you chose the non-beans generator.
    // alas, we cannot check 'userId' because it's a primitive and you chose the non-beans generator.
    // alas, we cannot check 'prodId' because it's a primitive and you chose the non-beans generator.
    if (commentDetails == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'commentDetails' was not present! Struct: " + toString());
    }
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

  private static class CommentInfoStandardSchemeFactory implements SchemeFactory {
    public CommentInfoStandardScheme getScheme() {
      return new CommentInfoStandardScheme();
    }
  }

  private static class CommentInfoStandardScheme extends StandardScheme<CommentInfo> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, CommentInfo struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // COMMENT_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.commentId = iprot.readI64();
              struct.setCommentIdIsSet(true);
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
          case 3: // USER_NAME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.userName = iprot.readString();
              struct.setUserNameIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // PROD_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.prodId = iprot.readI64();
              struct.setProdIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 5: // COMMENT_DETAILS
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.commentDetails = iprot.readString();
              struct.setCommentDetailsIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 6: // COMMENT_IMAGES
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list0 = iprot.readListBegin();
                struct.commentImages = new ArrayList<String>(_list0.size);
                String _elem1;
                for (int _i2 = 0; _i2 < _list0.size; ++_i2)
                {
                  _elem1 = iprot.readString();
                  struct.commentImages.add(_elem1);
                }
                iprot.readListEnd();
              }
              struct.setCommentImagesIsSet(true);
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
      if (!struct.isSetCommentId()) {
        throw new org.apache.thrift.protocol.TProtocolException("Required field 'commentId' was not found in serialized data! Struct: " + toString());
      }
      if (!struct.isSetUserId()) {
        throw new org.apache.thrift.protocol.TProtocolException("Required field 'userId' was not found in serialized data! Struct: " + toString());
      }
      if (!struct.isSetProdId()) {
        throw new org.apache.thrift.protocol.TProtocolException("Required field 'prodId' was not found in serialized data! Struct: " + toString());
      }
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, CommentInfo struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(COMMENT_ID_FIELD_DESC);
      oprot.writeI64(struct.commentId);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(USER_ID_FIELD_DESC);
      oprot.writeI64(struct.userId);
      oprot.writeFieldEnd();
      if (struct.userName != null) {
        if (struct.isSetUserName()) {
          oprot.writeFieldBegin(USER_NAME_FIELD_DESC);
          oprot.writeString(struct.userName);
          oprot.writeFieldEnd();
        }
      }
      oprot.writeFieldBegin(PROD_ID_FIELD_DESC);
      oprot.writeI64(struct.prodId);
      oprot.writeFieldEnd();
      if (struct.commentDetails != null) {
        oprot.writeFieldBegin(COMMENT_DETAILS_FIELD_DESC);
        oprot.writeString(struct.commentDetails);
        oprot.writeFieldEnd();
      }
      if (struct.commentImages != null) {
        if (struct.isSetCommentImages()) {
          oprot.writeFieldBegin(COMMENT_IMAGES_FIELD_DESC);
          {
            oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRING, struct.commentImages.size()));
            for (String _iter3 : struct.commentImages)
            {
              oprot.writeString(_iter3);
            }
            oprot.writeListEnd();
          }
          oprot.writeFieldEnd();
        }
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class CommentInfoTupleSchemeFactory implements SchemeFactory {
    public CommentInfoTupleScheme getScheme() {
      return new CommentInfoTupleScheme();
    }
  }

  private static class CommentInfoTupleScheme extends TupleScheme<CommentInfo> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, CommentInfo struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      oprot.writeI64(struct.commentId);
      oprot.writeI64(struct.userId);
      oprot.writeI64(struct.prodId);
      oprot.writeString(struct.commentDetails);
      BitSet optionals = new BitSet();
      if (struct.isSetUserName()) {
        optionals.set(0);
      }
      if (struct.isSetCommentImages()) {
        optionals.set(1);
      }
      oprot.writeBitSet(optionals, 2);
      if (struct.isSetUserName()) {
        oprot.writeString(struct.userName);
      }
      if (struct.isSetCommentImages()) {
        {
          oprot.writeI32(struct.commentImages.size());
          for (String _iter4 : struct.commentImages)
          {
            oprot.writeString(_iter4);
          }
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, CommentInfo struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      struct.commentId = iprot.readI64();
      struct.setCommentIdIsSet(true);
      struct.userId = iprot.readI64();
      struct.setUserIdIsSet(true);
      struct.prodId = iprot.readI64();
      struct.setProdIdIsSet(true);
      struct.commentDetails = iprot.readString();
      struct.setCommentDetailsIsSet(true);
      BitSet incoming = iprot.readBitSet(2);
      if (incoming.get(0)) {
        struct.userName = iprot.readString();
        struct.setUserNameIsSet(true);
      }
      if (incoming.get(1)) {
        {
          org.apache.thrift.protocol.TList _list5 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRING, iprot.readI32());
          struct.commentImages = new ArrayList<String>(_list5.size);
          String _elem6;
          for (int _i7 = 0; _i7 < _list5.size; ++_i7)
          {
            _elem6 = iprot.readString();
            struct.commentImages.add(_elem6);
          }
        }
        struct.setCommentImagesIsSet(true);
      }
    }
  }

}

