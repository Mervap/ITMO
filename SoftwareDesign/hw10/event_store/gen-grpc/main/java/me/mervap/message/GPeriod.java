// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: service.proto

package me.mervap.message;

/**
 * Protobuf enum {@code me.mervap.GPeriod}
 */
public enum GPeriod
    implements com.google.protobuf.ProtocolMessageEnum {
  /**
   * <code>DAY = 0;</code>
   */
  DAY(0),
  /**
   * <code>WEEK = 1;</code>
   */
  WEEK(1),
  /**
   * <code>MONTH = 2;</code>
   */
  MONTH(2),
  /**
   * <code>MONTH_3 = 3;</code>
   */
  MONTH_3(3),
  /**
   * <code>YEAR = 4;</code>
   */
  YEAR(4),
  UNRECOGNIZED(-1),
  ;

  /**
   * <code>DAY = 0;</code>
   */
  public static final int DAY_VALUE = 0;
  /**
   * <code>WEEK = 1;</code>
   */
  public static final int WEEK_VALUE = 1;
  /**
   * <code>MONTH = 2;</code>
   */
  public static final int MONTH_VALUE = 2;
  /**
   * <code>MONTH_3 = 3;</code>
   */
  public static final int MONTH_3_VALUE = 3;
  /**
   * <code>YEAR = 4;</code>
   */
  public static final int YEAR_VALUE = 4;


  public final int getNumber() {
    if (this == UNRECOGNIZED) {
      throw new java.lang.IllegalArgumentException(
          "Can't get the number of an unknown enum value.");
    }
    return value;
  }

  /**
   * @param value The numeric wire value of the corresponding enum entry.
   * @return The enum associated with the given numeric wire value.
   * @deprecated Use {@link #forNumber(int)} instead.
   */
  @java.lang.Deprecated
  public static GPeriod valueOf(int value) {
    return forNumber(value);
  }

  /**
   * @param value The numeric wire value of the corresponding enum entry.
   * @return The enum associated with the given numeric wire value.
   */
  public static GPeriod forNumber(int value) {
    switch (value) {
      case 0: return DAY;
      case 1: return WEEK;
      case 2: return MONTH;
      case 3: return MONTH_3;
      case 4: return YEAR;
      default: return null;
    }
  }

  public static com.google.protobuf.Internal.EnumLiteMap<GPeriod>
      internalGetValueMap() {
    return internalValueMap;
  }
  private static final com.google.protobuf.Internal.EnumLiteMap<
      GPeriod> internalValueMap =
        new com.google.protobuf.Internal.EnumLiteMap<GPeriod>() {
          public GPeriod findValueByNumber(int number) {
            return GPeriod.forNumber(number);
          }
        };

  public final com.google.protobuf.Descriptors.EnumValueDescriptor
      getValueDescriptor() {
    if (this == UNRECOGNIZED) {
      throw new java.lang.IllegalStateException(
          "Can't get the descriptor of an unrecognized enum value.");
    }
    return getDescriptor().getValues().get(ordinal());
  }
  public final com.google.protobuf.Descriptors.EnumDescriptor
      getDescriptorForType() {
    return getDescriptor();
  }
  public static final com.google.protobuf.Descriptors.EnumDescriptor
      getDescriptor() {
    return me.mervap.message.Service.getDescriptor().getEnumTypes().get(0);
  }

  private static final GPeriod[] VALUES = values();

  public static GPeriod valueOf(
      com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
    if (desc.getType() != getDescriptor()) {
      throw new java.lang.IllegalArgumentException(
        "EnumValueDescriptor is not for this type.");
    }
    if (desc.getIndex() == -1) {
      return UNRECOGNIZED;
    }
    return VALUES[desc.getIndex()];
  }

  private final int value;

  private GPeriod(int value) {
    this.value = value;
  }

  // @@protoc_insertion_point(enum_scope:me.mervap.GPeriod)
}

