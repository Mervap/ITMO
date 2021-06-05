package me.mervap.message;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.36.0)",
    comments = "Source: service.proto")
public final class EventStoreServiceGrpc {

  private EventStoreServiceGrpc() {}

  public static final String SERVICE_NAME = "me.mervap.EventStoreService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<me.mervap.message.GUpdateSubscription,
      me.mervap.message.CommandResponse> getBuySubscriptionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "buySubscription",
      requestType = me.mervap.message.GUpdateSubscription.class,
      responseType = me.mervap.message.CommandResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<me.mervap.message.GUpdateSubscription,
      me.mervap.message.CommandResponse> getBuySubscriptionMethod() {
    io.grpc.MethodDescriptor<me.mervap.message.GUpdateSubscription, me.mervap.message.CommandResponse> getBuySubscriptionMethod;
    if ((getBuySubscriptionMethod = EventStoreServiceGrpc.getBuySubscriptionMethod) == null) {
      synchronized (EventStoreServiceGrpc.class) {
        if ((getBuySubscriptionMethod = EventStoreServiceGrpc.getBuySubscriptionMethod) == null) {
          EventStoreServiceGrpc.getBuySubscriptionMethod = getBuySubscriptionMethod =
              io.grpc.MethodDescriptor.<me.mervap.message.GUpdateSubscription, me.mervap.message.CommandResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "buySubscription"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  me.mervap.message.GUpdateSubscription.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  me.mervap.message.CommandResponse.getDefaultInstance()))
              .setSchemaDescriptor(new EventStoreServiceMethodDescriptorSupplier("buySubscription"))
              .build();
        }
      }
    }
    return getBuySubscriptionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<me.mervap.message.GUpdateSubscription,
      me.mervap.message.CommandResponse> getExtendSubscriptionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "extendSubscription",
      requestType = me.mervap.message.GUpdateSubscription.class,
      responseType = me.mervap.message.CommandResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<me.mervap.message.GUpdateSubscription,
      me.mervap.message.CommandResponse> getExtendSubscriptionMethod() {
    io.grpc.MethodDescriptor<me.mervap.message.GUpdateSubscription, me.mervap.message.CommandResponse> getExtendSubscriptionMethod;
    if ((getExtendSubscriptionMethod = EventStoreServiceGrpc.getExtendSubscriptionMethod) == null) {
      synchronized (EventStoreServiceGrpc.class) {
        if ((getExtendSubscriptionMethod = EventStoreServiceGrpc.getExtendSubscriptionMethod) == null) {
          EventStoreServiceGrpc.getExtendSubscriptionMethod = getExtendSubscriptionMethod =
              io.grpc.MethodDescriptor.<me.mervap.message.GUpdateSubscription, me.mervap.message.CommandResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "extendSubscription"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  me.mervap.message.GUpdateSubscription.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  me.mervap.message.CommandResponse.getDefaultInstance()))
              .setSchemaDescriptor(new EventStoreServiceMethodDescriptorSupplier("extendSubscription"))
              .build();
        }
      }
    }
    return getExtendSubscriptionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<me.mervap.message.GTurnstileEvent,
      me.mervap.message.CommandResponse> getTryEnterMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "tryEnter",
      requestType = me.mervap.message.GTurnstileEvent.class,
      responseType = me.mervap.message.CommandResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<me.mervap.message.GTurnstileEvent,
      me.mervap.message.CommandResponse> getTryEnterMethod() {
    io.grpc.MethodDescriptor<me.mervap.message.GTurnstileEvent, me.mervap.message.CommandResponse> getTryEnterMethod;
    if ((getTryEnterMethod = EventStoreServiceGrpc.getTryEnterMethod) == null) {
      synchronized (EventStoreServiceGrpc.class) {
        if ((getTryEnterMethod = EventStoreServiceGrpc.getTryEnterMethod) == null) {
          EventStoreServiceGrpc.getTryEnterMethod = getTryEnterMethod =
              io.grpc.MethodDescriptor.<me.mervap.message.GTurnstileEvent, me.mervap.message.CommandResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "tryEnter"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  me.mervap.message.GTurnstileEvent.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  me.mervap.message.CommandResponse.getDefaultInstance()))
              .setSchemaDescriptor(new EventStoreServiceMethodDescriptorSupplier("tryEnter"))
              .build();
        }
      }
    }
    return getTryEnterMethod;
  }

  private static volatile io.grpc.MethodDescriptor<me.mervap.message.GTurnstileEvent,
      com.google.protobuf.Empty> getExitMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "exit",
      requestType = me.mervap.message.GTurnstileEvent.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<me.mervap.message.GTurnstileEvent,
      com.google.protobuf.Empty> getExitMethod() {
    io.grpc.MethodDescriptor<me.mervap.message.GTurnstileEvent, com.google.protobuf.Empty> getExitMethod;
    if ((getExitMethod = EventStoreServiceGrpc.getExitMethod) == null) {
      synchronized (EventStoreServiceGrpc.class) {
        if ((getExitMethod = EventStoreServiceGrpc.getExitMethod) == null) {
          EventStoreServiceGrpc.getExitMethod = getExitMethod =
              io.grpc.MethodDescriptor.<me.mervap.message.GTurnstileEvent, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "exit"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  me.mervap.message.GTurnstileEvent.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new EventStoreServiceMethodDescriptorSupplier("exit"))
              .build();
        }
      }
    }
    return getExitMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      me.mervap.message.GSubscriptionInfoList> getGetAllSubscriptionsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getAllSubscriptions",
      requestType = com.google.protobuf.Empty.class,
      responseType = me.mervap.message.GSubscriptionInfoList.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      me.mervap.message.GSubscriptionInfoList> getGetAllSubscriptionsMethod() {
    io.grpc.MethodDescriptor<com.google.protobuf.Empty, me.mervap.message.GSubscriptionInfoList> getGetAllSubscriptionsMethod;
    if ((getGetAllSubscriptionsMethod = EventStoreServiceGrpc.getGetAllSubscriptionsMethod) == null) {
      synchronized (EventStoreServiceGrpc.class) {
        if ((getGetAllSubscriptionsMethod = EventStoreServiceGrpc.getGetAllSubscriptionsMethod) == null) {
          EventStoreServiceGrpc.getGetAllSubscriptionsMethod = getGetAllSubscriptionsMethod =
              io.grpc.MethodDescriptor.<com.google.protobuf.Empty, me.mervap.message.GSubscriptionInfoList>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getAllSubscriptions"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  me.mervap.message.GSubscriptionInfoList.getDefaultInstance()))
              .setSchemaDescriptor(new EventStoreServiceMethodDescriptorSupplier("getAllSubscriptions"))
              .build();
        }
      }
    }
    return getGetAllSubscriptionsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      me.mervap.message.GStatList> getGetStatMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getStat",
      requestType = com.google.protobuf.Empty.class,
      responseType = me.mervap.message.GStatList.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      me.mervap.message.GStatList> getGetStatMethod() {
    io.grpc.MethodDescriptor<com.google.protobuf.Empty, me.mervap.message.GStatList> getGetStatMethod;
    if ((getGetStatMethod = EventStoreServiceGrpc.getGetStatMethod) == null) {
      synchronized (EventStoreServiceGrpc.class) {
        if ((getGetStatMethod = EventStoreServiceGrpc.getGetStatMethod) == null) {
          EventStoreServiceGrpc.getGetStatMethod = getGetStatMethod =
              io.grpc.MethodDescriptor.<com.google.protobuf.Empty, me.mervap.message.GStatList>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getStat"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  me.mervap.message.GStatList.getDefaultInstance()))
              .setSchemaDescriptor(new EventStoreServiceMethodDescriptorSupplier("getStat"))
              .build();
        }
      }
    }
    return getGetStatMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.google.protobuf.StringValue,
      me.mervap.message.GUserName> getFindNameByIdMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "findNameById",
      requestType = com.google.protobuf.StringValue.class,
      responseType = me.mervap.message.GUserName.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.protobuf.StringValue,
      me.mervap.message.GUserName> getFindNameByIdMethod() {
    io.grpc.MethodDescriptor<com.google.protobuf.StringValue, me.mervap.message.GUserName> getFindNameByIdMethod;
    if ((getFindNameByIdMethod = EventStoreServiceGrpc.getFindNameByIdMethod) == null) {
      synchronized (EventStoreServiceGrpc.class) {
        if ((getFindNameByIdMethod = EventStoreServiceGrpc.getFindNameByIdMethod) == null) {
          EventStoreServiceGrpc.getFindNameByIdMethod = getFindNameByIdMethod =
              io.grpc.MethodDescriptor.<com.google.protobuf.StringValue, me.mervap.message.GUserName>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "findNameById"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.StringValue.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  me.mervap.message.GUserName.getDefaultInstance()))
              .setSchemaDescriptor(new EventStoreServiceMethodDescriptorSupplier("findNameById"))
              .build();
        }
      }
    }
    return getFindNameByIdMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.google.protobuf.StringValue,
      com.google.protobuf.Timestamp> getFindPurchaseDateByIdMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "findPurchaseDateById",
      requestType = com.google.protobuf.StringValue.class,
      responseType = com.google.protobuf.Timestamp.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.protobuf.StringValue,
      com.google.protobuf.Timestamp> getFindPurchaseDateByIdMethod() {
    io.grpc.MethodDescriptor<com.google.protobuf.StringValue, com.google.protobuf.Timestamp> getFindPurchaseDateByIdMethod;
    if ((getFindPurchaseDateByIdMethod = EventStoreServiceGrpc.getFindPurchaseDateByIdMethod) == null) {
      synchronized (EventStoreServiceGrpc.class) {
        if ((getFindPurchaseDateByIdMethod = EventStoreServiceGrpc.getFindPurchaseDateByIdMethod) == null) {
          EventStoreServiceGrpc.getFindPurchaseDateByIdMethod = getFindPurchaseDateByIdMethod =
              io.grpc.MethodDescriptor.<com.google.protobuf.StringValue, com.google.protobuf.Timestamp>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "findPurchaseDateById"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.StringValue.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Timestamp.getDefaultInstance()))
              .setSchemaDescriptor(new EventStoreServiceMethodDescriptorSupplier("findPurchaseDateById"))
              .build();
        }
      }
    }
    return getFindPurchaseDateByIdMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.google.protobuf.Int32Value,
      me.mervap.message.CommandResponse> getSubscribeOnCommandsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "subscribeOnCommands",
      requestType = com.google.protobuf.Int32Value.class,
      responseType = me.mervap.message.CommandResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.protobuf.Int32Value,
      me.mervap.message.CommandResponse> getSubscribeOnCommandsMethod() {
    io.grpc.MethodDescriptor<com.google.protobuf.Int32Value, me.mervap.message.CommandResponse> getSubscribeOnCommandsMethod;
    if ((getSubscribeOnCommandsMethod = EventStoreServiceGrpc.getSubscribeOnCommandsMethod) == null) {
      synchronized (EventStoreServiceGrpc.class) {
        if ((getSubscribeOnCommandsMethod = EventStoreServiceGrpc.getSubscribeOnCommandsMethod) == null) {
          EventStoreServiceGrpc.getSubscribeOnCommandsMethod = getSubscribeOnCommandsMethod =
              io.grpc.MethodDescriptor.<com.google.protobuf.Int32Value, me.mervap.message.CommandResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "subscribeOnCommands"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Int32Value.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  me.mervap.message.CommandResponse.getDefaultInstance()))
              .setSchemaDescriptor(new EventStoreServiceMethodDescriptorSupplier("subscribeOnCommands"))
              .build();
        }
      }
    }
    return getSubscribeOnCommandsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.google.protobuf.Int32Value,
      com.google.protobuf.Empty> getUnsubscribeFromCommandsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "unsubscribeFromCommands",
      requestType = com.google.protobuf.Int32Value.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.protobuf.Int32Value,
      com.google.protobuf.Empty> getUnsubscribeFromCommandsMethod() {
    io.grpc.MethodDescriptor<com.google.protobuf.Int32Value, com.google.protobuf.Empty> getUnsubscribeFromCommandsMethod;
    if ((getUnsubscribeFromCommandsMethod = EventStoreServiceGrpc.getUnsubscribeFromCommandsMethod) == null) {
      synchronized (EventStoreServiceGrpc.class) {
        if ((getUnsubscribeFromCommandsMethod = EventStoreServiceGrpc.getUnsubscribeFromCommandsMethod) == null) {
          EventStoreServiceGrpc.getUnsubscribeFromCommandsMethod = getUnsubscribeFromCommandsMethod =
              io.grpc.MethodDescriptor.<com.google.protobuf.Int32Value, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "unsubscribeFromCommands"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Int32Value.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new EventStoreServiceMethodDescriptorSupplier("unsubscribeFromCommands"))
              .build();
        }
      }
    }
    return getUnsubscribeFromCommandsMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static EventStoreServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<EventStoreServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<EventStoreServiceStub>() {
        @java.lang.Override
        public EventStoreServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new EventStoreServiceStub(channel, callOptions);
        }
      };
    return EventStoreServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static EventStoreServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<EventStoreServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<EventStoreServiceBlockingStub>() {
        @java.lang.Override
        public EventStoreServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new EventStoreServiceBlockingStub(channel, callOptions);
        }
      };
    return EventStoreServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static EventStoreServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<EventStoreServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<EventStoreServiceFutureStub>() {
        @java.lang.Override
        public EventStoreServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new EventStoreServiceFutureStub(channel, callOptions);
        }
      };
    return EventStoreServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class EventStoreServiceImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * Commands
     * </pre>
     */
    public void buySubscription(me.mervap.message.GUpdateSubscription request,
        io.grpc.stub.StreamObserver<me.mervap.message.CommandResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getBuySubscriptionMethod(), responseObserver);
    }

    /**
     */
    public void extendSubscription(me.mervap.message.GUpdateSubscription request,
        io.grpc.stub.StreamObserver<me.mervap.message.CommandResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getExtendSubscriptionMethod(), responseObserver);
    }

    /**
     */
    public void tryEnter(me.mervap.message.GTurnstileEvent request,
        io.grpc.stub.StreamObserver<me.mervap.message.CommandResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getTryEnterMethod(), responseObserver);
    }

    /**
     */
    public void exit(me.mervap.message.GTurnstileEvent request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getExitMethod(), responseObserver);
    }

    /**
     * <pre>
     * Queries
     * </pre>
     */
    public void getAllSubscriptions(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<me.mervap.message.GSubscriptionInfoList> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetAllSubscriptionsMethod(), responseObserver);
    }

    /**
     */
    public void getStat(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<me.mervap.message.GStatList> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetStatMethod(), responseObserver);
    }

    /**
     */
    public void findNameById(com.google.protobuf.StringValue request,
        io.grpc.stub.StreamObserver<me.mervap.message.GUserName> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getFindNameByIdMethod(), responseObserver);
    }

    /**
     */
    public void findPurchaseDateById(com.google.protobuf.StringValue request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Timestamp> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getFindPurchaseDateByIdMethod(), responseObserver);
    }

    /**
     * <pre>
     * Commands Subscribe
     * </pre>
     */
    public void subscribeOnCommands(com.google.protobuf.Int32Value request,
        io.grpc.stub.StreamObserver<me.mervap.message.CommandResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSubscribeOnCommandsMethod(), responseObserver);
    }

    /**
     */
    public void unsubscribeFromCommands(com.google.protobuf.Int32Value request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUnsubscribeFromCommandsMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getBuySubscriptionMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                me.mervap.message.GUpdateSubscription,
                me.mervap.message.CommandResponse>(
                  this, METHODID_BUY_SUBSCRIPTION)))
          .addMethod(
            getExtendSubscriptionMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                me.mervap.message.GUpdateSubscription,
                me.mervap.message.CommandResponse>(
                  this, METHODID_EXTEND_SUBSCRIPTION)))
          .addMethod(
            getTryEnterMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                me.mervap.message.GTurnstileEvent,
                me.mervap.message.CommandResponse>(
                  this, METHODID_TRY_ENTER)))
          .addMethod(
            getExitMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                me.mervap.message.GTurnstileEvent,
                com.google.protobuf.Empty>(
                  this, METHODID_EXIT)))
          .addMethod(
            getGetAllSubscriptionsMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.google.protobuf.Empty,
                me.mervap.message.GSubscriptionInfoList>(
                  this, METHODID_GET_ALL_SUBSCRIPTIONS)))
          .addMethod(
            getGetStatMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.google.protobuf.Empty,
                me.mervap.message.GStatList>(
                  this, METHODID_GET_STAT)))
          .addMethod(
            getFindNameByIdMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.google.protobuf.StringValue,
                me.mervap.message.GUserName>(
                  this, METHODID_FIND_NAME_BY_ID)))
          .addMethod(
            getFindPurchaseDateByIdMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.google.protobuf.StringValue,
                com.google.protobuf.Timestamp>(
                  this, METHODID_FIND_PURCHASE_DATE_BY_ID)))
          .addMethod(
            getSubscribeOnCommandsMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.google.protobuf.Int32Value,
                me.mervap.message.CommandResponse>(
                  this, METHODID_SUBSCRIBE_ON_COMMANDS)))
          .addMethod(
            getUnsubscribeFromCommandsMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.google.protobuf.Int32Value,
                com.google.protobuf.Empty>(
                  this, METHODID_UNSUBSCRIBE_FROM_COMMANDS)))
          .build();
    }
  }

  /**
   */
  public static final class EventStoreServiceStub extends io.grpc.stub.AbstractAsyncStub<EventStoreServiceStub> {
    private EventStoreServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected EventStoreServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new EventStoreServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * Commands
     * </pre>
     */
    public void buySubscription(me.mervap.message.GUpdateSubscription request,
        io.grpc.stub.StreamObserver<me.mervap.message.CommandResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getBuySubscriptionMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void extendSubscription(me.mervap.message.GUpdateSubscription request,
        io.grpc.stub.StreamObserver<me.mervap.message.CommandResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getExtendSubscriptionMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void tryEnter(me.mervap.message.GTurnstileEvent request,
        io.grpc.stub.StreamObserver<me.mervap.message.CommandResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getTryEnterMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void exit(me.mervap.message.GTurnstileEvent request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getExitMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Queries
     * </pre>
     */
    public void getAllSubscriptions(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<me.mervap.message.GSubscriptionInfoList> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetAllSubscriptionsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getStat(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<me.mervap.message.GStatList> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetStatMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void findNameById(com.google.protobuf.StringValue request,
        io.grpc.stub.StreamObserver<me.mervap.message.GUserName> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getFindNameByIdMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void findPurchaseDateById(com.google.protobuf.StringValue request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Timestamp> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getFindPurchaseDateByIdMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Commands Subscribe
     * </pre>
     */
    public void subscribeOnCommands(com.google.protobuf.Int32Value request,
        io.grpc.stub.StreamObserver<me.mervap.message.CommandResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSubscribeOnCommandsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void unsubscribeFromCommands(com.google.protobuf.Int32Value request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUnsubscribeFromCommandsMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class EventStoreServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<EventStoreServiceBlockingStub> {
    private EventStoreServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected EventStoreServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new EventStoreServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Commands
     * </pre>
     */
    public me.mervap.message.CommandResponse buySubscription(me.mervap.message.GUpdateSubscription request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getBuySubscriptionMethod(), getCallOptions(), request);
    }

    /**
     */
    public me.mervap.message.CommandResponse extendSubscription(me.mervap.message.GUpdateSubscription request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getExtendSubscriptionMethod(), getCallOptions(), request);
    }

    /**
     */
    public me.mervap.message.CommandResponse tryEnter(me.mervap.message.GTurnstileEvent request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getTryEnterMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.google.protobuf.Empty exit(me.mervap.message.GTurnstileEvent request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getExitMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Queries
     * </pre>
     */
    public me.mervap.message.GSubscriptionInfoList getAllSubscriptions(com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetAllSubscriptionsMethod(), getCallOptions(), request);
    }

    /**
     */
    public me.mervap.message.GStatList getStat(com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetStatMethod(), getCallOptions(), request);
    }

    /**
     */
    public me.mervap.message.GUserName findNameById(com.google.protobuf.StringValue request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getFindNameByIdMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.google.protobuf.Timestamp findPurchaseDateById(com.google.protobuf.StringValue request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getFindPurchaseDateByIdMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Commands Subscribe
     * </pre>
     */
    public me.mervap.message.CommandResponse subscribeOnCommands(com.google.protobuf.Int32Value request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSubscribeOnCommandsMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.google.protobuf.Empty unsubscribeFromCommands(com.google.protobuf.Int32Value request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUnsubscribeFromCommandsMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class EventStoreServiceFutureStub extends io.grpc.stub.AbstractFutureStub<EventStoreServiceFutureStub> {
    private EventStoreServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected EventStoreServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new EventStoreServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Commands
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<me.mervap.message.CommandResponse> buySubscription(
        me.mervap.message.GUpdateSubscription request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getBuySubscriptionMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<me.mervap.message.CommandResponse> extendSubscription(
        me.mervap.message.GUpdateSubscription request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getExtendSubscriptionMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<me.mervap.message.CommandResponse> tryEnter(
        me.mervap.message.GTurnstileEvent request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getTryEnterMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> exit(
        me.mervap.message.GTurnstileEvent request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getExitMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Queries
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<me.mervap.message.GSubscriptionInfoList> getAllSubscriptions(
        com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetAllSubscriptionsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<me.mervap.message.GStatList> getStat(
        com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetStatMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<me.mervap.message.GUserName> findNameById(
        com.google.protobuf.StringValue request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getFindNameByIdMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Timestamp> findPurchaseDateById(
        com.google.protobuf.StringValue request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getFindPurchaseDateByIdMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Commands Subscribe
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<me.mervap.message.CommandResponse> subscribeOnCommands(
        com.google.protobuf.Int32Value request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSubscribeOnCommandsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> unsubscribeFromCommands(
        com.google.protobuf.Int32Value request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUnsubscribeFromCommandsMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_BUY_SUBSCRIPTION = 0;
  private static final int METHODID_EXTEND_SUBSCRIPTION = 1;
  private static final int METHODID_TRY_ENTER = 2;
  private static final int METHODID_EXIT = 3;
  private static final int METHODID_GET_ALL_SUBSCRIPTIONS = 4;
  private static final int METHODID_GET_STAT = 5;
  private static final int METHODID_FIND_NAME_BY_ID = 6;
  private static final int METHODID_FIND_PURCHASE_DATE_BY_ID = 7;
  private static final int METHODID_SUBSCRIBE_ON_COMMANDS = 8;
  private static final int METHODID_UNSUBSCRIBE_FROM_COMMANDS = 9;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final EventStoreServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(EventStoreServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_BUY_SUBSCRIPTION:
          serviceImpl.buySubscription((me.mervap.message.GUpdateSubscription) request,
              (io.grpc.stub.StreamObserver<me.mervap.message.CommandResponse>) responseObserver);
          break;
        case METHODID_EXTEND_SUBSCRIPTION:
          serviceImpl.extendSubscription((me.mervap.message.GUpdateSubscription) request,
              (io.grpc.stub.StreamObserver<me.mervap.message.CommandResponse>) responseObserver);
          break;
        case METHODID_TRY_ENTER:
          serviceImpl.tryEnter((me.mervap.message.GTurnstileEvent) request,
              (io.grpc.stub.StreamObserver<me.mervap.message.CommandResponse>) responseObserver);
          break;
        case METHODID_EXIT:
          serviceImpl.exit((me.mervap.message.GTurnstileEvent) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_GET_ALL_SUBSCRIPTIONS:
          serviceImpl.getAllSubscriptions((com.google.protobuf.Empty) request,
              (io.grpc.stub.StreamObserver<me.mervap.message.GSubscriptionInfoList>) responseObserver);
          break;
        case METHODID_GET_STAT:
          serviceImpl.getStat((com.google.protobuf.Empty) request,
              (io.grpc.stub.StreamObserver<me.mervap.message.GStatList>) responseObserver);
          break;
        case METHODID_FIND_NAME_BY_ID:
          serviceImpl.findNameById((com.google.protobuf.StringValue) request,
              (io.grpc.stub.StreamObserver<me.mervap.message.GUserName>) responseObserver);
          break;
        case METHODID_FIND_PURCHASE_DATE_BY_ID:
          serviceImpl.findPurchaseDateById((com.google.protobuf.StringValue) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Timestamp>) responseObserver);
          break;
        case METHODID_SUBSCRIBE_ON_COMMANDS:
          serviceImpl.subscribeOnCommands((com.google.protobuf.Int32Value) request,
              (io.grpc.stub.StreamObserver<me.mervap.message.CommandResponse>) responseObserver);
          break;
        case METHODID_UNSUBSCRIBE_FROM_COMMANDS:
          serviceImpl.unsubscribeFromCommands((com.google.protobuf.Int32Value) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class EventStoreServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    EventStoreServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return me.mervap.message.Service.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("EventStoreService");
    }
  }

  private static final class EventStoreServiceFileDescriptorSupplier
      extends EventStoreServiceBaseDescriptorSupplier {
    EventStoreServiceFileDescriptorSupplier() {}
  }

  private static final class EventStoreServiceMethodDescriptorSupplier
      extends EventStoreServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    EventStoreServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (EventStoreServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new EventStoreServiceFileDescriptorSupplier())
              .addMethod(getBuySubscriptionMethod())
              .addMethod(getExtendSubscriptionMethod())
              .addMethod(getTryEnterMethod())
              .addMethod(getExitMethod())
              .addMethod(getGetAllSubscriptionsMethod())
              .addMethod(getGetStatMethod())
              .addMethod(getFindNameByIdMethod())
              .addMethod(getFindPurchaseDateByIdMethod())
              .addMethod(getSubscribeOnCommandsMethod())
              .addMethod(getUnsubscribeFromCommandsMethod())
              .build();
        }
      }
    }
    return result;
  }
}
