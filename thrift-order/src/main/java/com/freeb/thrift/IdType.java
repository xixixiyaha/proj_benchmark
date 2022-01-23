/**
 * Autogenerated by Thrift Compiler (0.9.3)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.freeb.thrift;


import java.util.Map;
import java.util.HashMap;
import org.apache.thrift.TEnum;

public enum IdType implements TEnum {
  ACCOUNT_ID(0),
  ORDER_ID(1),
  PAYMENT_ID(2),
  SHIPPING_ID(3),
  MERCHANT_ID(4),
  OBJ_ID(5);

  private final int value;

  private IdType(int value) {
    this.value = value;
  }

  /**
   * Get the integer value of this enum value, as defined in the Thrift IDL.
   */
  public int getValue() {
    return value;
  }

  /**
   * Find a the enum type by its integer value, as defined in the Thrift IDL.
   * @return null if the value is not found.
   */
  public static IdType findByValue(int value) { 
    switch (value) {
      case 0:
        return ACCOUNT_ID;
      case 1:
        return ORDER_ID;
      case 2:
        return PAYMENT_ID;
      case 3:
        return SHIPPING_ID;
      case 4:
        return MERCHANT_ID;
      case 5:
        return OBJ_ID;
      default:
        return null;
    }
  }
}
