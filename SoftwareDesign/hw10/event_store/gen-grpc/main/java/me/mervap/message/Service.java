// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: service.proto

package me.mervap.message;

public final class Service {
  private Service() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_me_mervap_CommandResponse_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_me_mervap_CommandResponse_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_me_mervap_GUserName_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_me_mervap_GUserName_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_me_mervap_GUpdateSubscription_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_me_mervap_GUpdateSubscription_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_me_mervap_GSubscriptionInfo_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_me_mervap_GSubscriptionInfo_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_me_mervap_GSubscriptionInfoList_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_me_mervap_GSubscriptionInfoList_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_me_mervap_GTurnstileEvent_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_me_mervap_GTurnstileEvent_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_me_mervap_GStat_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_me_mervap_GStat_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_me_mervap_GStat_WeekStatEntry_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_me_mervap_GStat_WeekStatEntry_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_me_mervap_GStatList_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_me_mervap_GStatList_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\rservice.proto\022\tme.mervap\032\033google/proto" +
      "buf/empty.proto\032\036google/protobuf/wrapper" +
      "s.proto\032\037google/protobuf/timestamp.proto" +
      "\".\n\017CommandResponse\022\n\n\002ok\030\001 \001(\010\022\017\n\007messa" +
      "ge\030\002 \001(\t\"0\n\tGUserName\022\021\n\tfirstName\030\001 \001(\t" +
      "\022\020\n\010lastName\030\002 \001(\t\"\237\001\n\023GUpdateSubscripti" +
      "on\022\n\n\002id\030\001 \001(\t\022&\n\010userName\030\002 \001(\0132\024.me.me" +
      "rvap.GUserName\0220\n\014purchaseDate\030\003 \001(\0132\032.g" +
      "oogle.protobuf.Timestamp\022\"\n\006period\030\004 \001(\016" +
      "2\022.me.mervap.GPeriod\"w\n\021GSubscriptionInf" +
      "o\022\n\n\002id\030\001 \001(\t\022&\n\010userName\030\002 \001(\0132\024.me.mer" +
      "vap.GUserName\022.\n\nexpireDate\030\003 \001(\0132\032.goog" +
      "le.protobuf.Timestamp\"D\n\025GSubscriptionIn" +
      "foList\022+\n\005infos\030\001 \003(\0132\034.me.mervap.GSubsc" +
      "riptionInfo\"G\n\017GTurnstileEvent\022\n\n\002id\030\001 \001" +
      "(\t\022(\n\004date\030\002 \001(\0132\032.google.protobuf.Times" +
      "tamp\"\255\002\n\005GStat\022\n\n\002id\030\001 \001(\t\022&\n\010userName\030\002" +
      " \001(\0132\024.me.mervap.GUserName\0220\n\010weekStat\030\003" +
      " \003(\0132\036.me.mervap.GStat.WeekStatEntry\022\024\n\014" +
      "visitsAtWeek\030\004 \003(\005\022\026\n\016visitsDuration\030\005 \003" +
      "(\003\0220\n\014purchaseDate\030\006 \001(\0132\032.google.protob" +
      "uf.Timestamp\022-\n\tlastEnter\030\007 \001(\0132\032.google" +
      ".protobuf.Timestamp\032/\n\rWeekStatEntry\022\013\n\003" +
      "key\030\001 \001(\005\022\r\n\005value\030\002 \001(\005:\0028\001\",\n\tGStatLis" +
      "t\022\037\n\005stats\030\001 \003(\0132\020.me.mervap.GStat*>\n\007GP" +
      "eriod\022\007\n\003DAY\020\000\022\010\n\004WEEK\020\001\022\t\n\005MONTH\020\002\022\013\n\007M" +
      "ONTH_3\020\003\022\010\n\004YEAR\020\0042\210\006\n\021EventStoreService" +
      "\022O\n\017buySubscription\022\036.me.mervap.GUpdateS" +
      "ubscription\032\032.me.mervap.CommandResponse\"" +
      "\000\022R\n\022extendSubscription\022\036.me.mervap.GUpd" +
      "ateSubscription\032\032.me.mervap.CommandRespo" +
      "nse\"\000\022D\n\010tryEnter\022\032.me.mervap.GTurnstile" +
      "Event\032\032.me.mervap.CommandResponse\"\000\022<\n\004e" +
      "xit\022\032.me.mervap.GTurnstileEvent\032\026.google" +
      ".protobuf.Empty\"\000\022Q\n\023getAllSubscriptions" +
      "\022\026.google.protobuf.Empty\032 .me.mervap.GSu" +
      "bscriptionInfoList\"\000\0229\n\007getStat\022\026.google" +
      ".protobuf.Empty\032\024.me.mervap.GStatList\"\000\022" +
      "D\n\014findNameById\022\034.google.protobuf.String" +
      "Value\032\024.me.mervap.GUserName\"\000\022R\n\024findPur" +
      "chaseDateById\022\034.google.protobuf.StringVa" +
      "lue\032\032.google.protobuf.Timestamp\"\000\022P\n\023sub" +
      "scribeOnCommands\022\033.google.protobuf.Int32" +
      "Value\032\032.me.mervap.CommandResponse\"\000\022P\n\027u" +
      "nsubscribeFromCommands\022\033.google.protobuf" +
      ".Int32Value\032\026.google.protobuf.Empty\"\0002\325\002" +
      "\n\032CommandSubscriptionService\022R\n\026buySubsc" +
      "riptionCommand\022\036.me.mervap.GUpdateSubscr" +
      "iption\032\026.google.protobuf.Empty\"\000\022U\n\031exte" +
      "ndSubscriptionCommand\022\036.me.mervap.GUpdat" +
      "eSubscription\032\026.google.protobuf.Empty\"\000\022" +
      "G\n\017tryEnterCommand\022\032.me.mervap.GTurnstil" +
      "eEvent\032\026.google.protobuf.Empty\"\000\022C\n\013exit" +
      "Command\022\032.me.mervap.GTurnstileEvent\032\026.go" +
      "ogle.protobuf.Empty\"\000B\025\n\021me.mervap.messa" +
      "geP\001b\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          com.google.protobuf.EmptyProto.getDescriptor(),
          com.google.protobuf.WrappersProto.getDescriptor(),
          com.google.protobuf.TimestampProto.getDescriptor(),
        });
    internal_static_me_mervap_CommandResponse_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_me_mervap_CommandResponse_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_me_mervap_CommandResponse_descriptor,
        new java.lang.String[] { "Ok", "Message", });
    internal_static_me_mervap_GUserName_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_me_mervap_GUserName_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_me_mervap_GUserName_descriptor,
        new java.lang.String[] { "FirstName", "LastName", });
    internal_static_me_mervap_GUpdateSubscription_descriptor =
      getDescriptor().getMessageTypes().get(2);
    internal_static_me_mervap_GUpdateSubscription_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_me_mervap_GUpdateSubscription_descriptor,
        new java.lang.String[] { "Id", "UserName", "PurchaseDate", "Period", });
    internal_static_me_mervap_GSubscriptionInfo_descriptor =
      getDescriptor().getMessageTypes().get(3);
    internal_static_me_mervap_GSubscriptionInfo_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_me_mervap_GSubscriptionInfo_descriptor,
        new java.lang.String[] { "Id", "UserName", "ExpireDate", });
    internal_static_me_mervap_GSubscriptionInfoList_descriptor =
      getDescriptor().getMessageTypes().get(4);
    internal_static_me_mervap_GSubscriptionInfoList_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_me_mervap_GSubscriptionInfoList_descriptor,
        new java.lang.String[] { "Infos", });
    internal_static_me_mervap_GTurnstileEvent_descriptor =
      getDescriptor().getMessageTypes().get(5);
    internal_static_me_mervap_GTurnstileEvent_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_me_mervap_GTurnstileEvent_descriptor,
        new java.lang.String[] { "Id", "Date", });
    internal_static_me_mervap_GStat_descriptor =
      getDescriptor().getMessageTypes().get(6);
    internal_static_me_mervap_GStat_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_me_mervap_GStat_descriptor,
        new java.lang.String[] { "Id", "UserName", "WeekStat", "VisitsAtWeek", "VisitsDuration", "PurchaseDate", "LastEnter", });
    internal_static_me_mervap_GStat_WeekStatEntry_descriptor =
      internal_static_me_mervap_GStat_descriptor.getNestedTypes().get(0);
    internal_static_me_mervap_GStat_WeekStatEntry_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_me_mervap_GStat_WeekStatEntry_descriptor,
        new java.lang.String[] { "Key", "Value", });
    internal_static_me_mervap_GStatList_descriptor =
      getDescriptor().getMessageTypes().get(7);
    internal_static_me_mervap_GStatList_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_me_mervap_GStatList_descriptor,
        new java.lang.String[] { "Stats", });
    com.google.protobuf.EmptyProto.getDescriptor();
    com.google.protobuf.WrappersProto.getDescriptor();
    com.google.protobuf.TimestampProto.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
