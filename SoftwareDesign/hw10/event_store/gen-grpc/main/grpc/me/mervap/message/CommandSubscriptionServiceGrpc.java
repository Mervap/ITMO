package me.mervap.message;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.36.0)",
    comments = "Source: service.proto")
public final class CommandSubscriptionServiceGrpc {

  private CommandSubscriptionServiceGrpc() {}

  public static final String SERVICE_NAME = "me.mervap.CommandSubscriptionService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<me.mervap.message.GUpdateSubscription,
      com.google.protobuf.Empty> getBuySubscriptionCommandMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "buySubscriptionCommand",
      requestType = me.mervap.message.GUpdateSubscription.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<me.mervap.message.GUpdateSubscription,
      com.google.protobuf.Empty> getBuySubscriptionCommandMethod() {
    io.grpc.MethodDescriptor<me.mervap.message.GUpdateSubscription, com.google.protobuf.Empty> getBuySubscriptionCommandMethod;
    if ((getBuySubscriptionCommandMethod = CommandSubscriptionServiceGrpc.getBuySubscriptionCommandMethod) == null) {
      synchronized (CommandSubscriptionServiceGrpc.class) {
        if ((getBuySubscriptionCommandMethod = CommandSubscriptionServiceGrpc.getBuySubscriptionCommandMethod) == null) {
          CommandSubscriptionServiceGrpc.getBuySubscriptionCommandMethod = getBuySubscriptionCommandMethod =
              io.grpc.MethodDescriptor.<me.mervap.message.GUpdateSubscription, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "buySubscriptionCommand"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  me.mervap.message.GUpdateSubscription.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new CommandSubscriptionServiceMethodDescriptorSupplier("buySubscriptionCommand"))
              .build();
        }
      }
    }
    return getBuySubscriptionCommandMethod;
  }

  private static volatile io.grpc.MethodDescriptor<me.mervap.message.GUpdateSubscription,
      com.google.protobuf.Empty> getExtendSubscriptionCommandMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "extendSubscriptionCommand",
      requestType = me.mervap.message.GUpdateSubscription.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<me.mervap.message.GUpdateSubscription,
      com.google.protobuf.Empty> getExtendSubscriptionCommandMethod() {
    io.grpc.MethodDescriptor<me.mervap.message.GUpdateSubscription, com.google.protobuf.Empty> getExtendSubscriptionCommandMethod;
    if ((getExtendSubscriptionCommandMethod = CommandSubscriptionServiceGrpc.getExtendSubscriptionCommandMethod) == null) {
      synchronized (CommandSubscriptionServiceGrpc.class) {
        if ((getExtendSubscriptionCommandMethod = CommandSubscriptionServiceGrpc.getExtendSubscriptionCommandMethod) == null) {
          CommandSubscriptionServiceGrpc.getExtendSubscriptionCommandMethod = getExtendSubscriptionCommandMethod =
              io.grpc.MethodDescriptor.<me.mervap.message.GUpdateSubscription, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "extendSubscriptionCommand"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  me.mervap.message.GUpdateSubscription.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new CommandSubscriptionServiceMethodDescriptorSupplier("extendSubscriptionCommand"))
              .build();
        }
      }
    }
    return getExtendSubscriptionCommandMethod;
  }

  private static volatile io.grpc.MethodDescriptor<me.mervap.message.GTurnstileEvent,
      com.google.protobuf.Empty> getTryEnterCommandMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "tryEnterCommand",
      requestType = me.mervap.message.GTurnstileEvent.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<me.mervap.message.GTurnstileEvent,
      com.google.protobuf.Empty> getTryEnterCommandMethod() {
    io.grpc.MethodDescriptor<me.mervap.message.GTurnstileEvent, com.google.protobuf.Empty> getTryEnterCommandMethod;
    if ((getTryEnterCommandMethod = CommandSubscriptionServiceGrpc.getTryEnterCommandMethod) == null) {
      synchronized (CommandSubscriptionServiceGrpc.class) {
        if ((getTryEnterCommandMethod = CommandSubscriptionServiceGrpc.getTryEnterCommandMethod) == null) {
          CommandSubscriptionServiceGrpc.getTryEnterCommandMethod = getTryEnterCommandMethod =
              io.grpc.MethodDescriptor.<me.mervap.message.GTurnstileEvent, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "tryEnterCommand"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  me.mervap.message.GTurnstileEvent.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new CommandSubscriptionServiceMethodDescriptorSupplier("tryEnterCommand"))
              .build();
        }
      }
    }
    return getTryEnterCommandMethod;
  }

  private static volatile io.grpc.MethodDescriptor<me.mervap.message.GTurnstileEvent,
      com.google.protobuf.Empty> getExitCommandMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "exitCommand",
      requestType = me.mervap.message.GTurnstileEvent.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<me.mervap.message.GTurnstileEvent,
      com.google.protobuf.Empty> getExitCommandMethod() {
    io.grpc.MethodDescriptor<me.mervap.message.GTurnstileEvent, com.google.protobuf.Empty> getExitCommandMethod;
    if ((getExitCommandMethod = CommandSubscriptionServiceGrpc.getExitCommandMethod) == null) {
      synchronized (CommandSubscriptionServiceGrpc.class) {
        if ((getExitCommandMethod = CommandSubscriptionServiceGrpc.getExitCommandMethod) == null) {
          CommandSubscriptionServiceGrpc.getExitCommandMethod = getExitCommandMethod =
              io.grpc.MethodDescriptor.<me.mervap.message.GTurnstileEvent, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "exitCommand"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  me.mervap.message.GTurnstileEvent.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new CommandSubscriptionServiceMethodDescriptorSupplier("exitCommand"))
              .build();
        }
      }
    }
    return getExitCommandMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static CommandSubscriptionServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CommandSubscriptionServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CommandSubscriptionServiceStub>() {
        @java.lang.Override
        public CommandSubscriptionServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CommandSubscriptionServiceStub(channel, callOptions);
        }
      };
    return CommandSubscriptionServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static CommandSubscriptionServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CommandSubscriptionServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CommandSubscriptionServiceBlockingStub>() {
        @java.lang.Override
        public CommandSubscriptionServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CommandSubscriptionServiceBlockingStub(channel, callOptions);
        }
      };
    return CommandSubscriptionServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static CommandSubscriptionServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CommandSubscriptionServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CommandSubscriptionServiceFutureStub>() {
        @java.lang.Override
        public CommandSubscriptionServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CommandSubscriptionServiceFutureStub(channel, callOptions);
        }
      };
    return CommandSubscriptionServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class CommandSubscriptionServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void buySubscriptionCommand(me.mervap.message.GUpdateSubscription request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getBuySubscriptionCommandMethod(), responseObserver);
    }

    /**
     */
    public void extendSubscriptionCommand(me.mervap.message.GUpdateSubscription request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getExtendSubscriptionCommandMethod(), responseObserver);
    }

    /**
     */
    public void tryEnterCommand(me.mervap.message.GTurnstileEvent request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getTryEnterCommandMethod(), responseObserver);
    }

    /**
     */
    public void exitCommand(me.mervap.message.GTurnstileEvent request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getExitCommandMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getBuySubscriptionCommandMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                me.mervap.message.GUpdateSubscription,
                com.google.protobuf.Empty>(
                  this, METHODID_BUY_SUBSCRIPTION_COMMAND)))
          .addMethod(
            getExtendSubscriptionCommandMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                me.mervap.message.GUpdateSubscription,
                com.google.protobuf.Empty>(
                  this, METHODID_EXTEND_SUBSCRIPTION_COMMAND)))
          .addMethod(
            getTryEnterCommandMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                me.mervap.message.GTurnstileEvent,
                com.google.protobuf.Empty>(
                  this, METHODID_TRY_ENTER_COMMAND)))
          .addMethod(
            getExitCommandMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                me.mervap.message.GTurnstileEvent,
                com.google.protobuf.Empty>(
                  this, METHODID_EXIT_COMMAND)))
          .build();
    }
  }

  /**
   */
  public static final class CommandSubscriptionServiceStub extends io.grpc.stub.AbstractAsyncStub<CommandSubscriptionServiceStub> {
    private CommandSubscriptionServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CommandSubscriptionServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CommandSubscriptionServiceStub(channel, callOptions);
    }

    /**
     */
    public void buySubscriptionCommand(me.mervap.message.GUpdateSubscription request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getBuySubscriptionCommandMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void extendSubscriptionCommand(me.mervap.message.GUpdateSubscription request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getExtendSubscriptionCommandMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void tryEnterCommand(me.mervap.message.GTurnstileEvent request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getTryEnterCommandMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void exitCommand(me.mervap.message.GTurnstileEvent request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getExitCommandMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class CommandSubscriptionServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<CommandSubscriptionServiceBlockingStub> {
    private CommandSubscriptionServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CommandSubscriptionServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CommandSubscriptionServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.google.protobuf.Empty buySubscriptionCommand(me.mervap.message.GUpdateSubscription request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getBuySubscriptionCommandMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.google.protobuf.Empty extendSubscriptionCommand(me.mervap.message.GUpdateSubscription request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getExtendSubscriptionCommandMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.google.protobuf.Empty tryEnterCommand(me.mervap.message.GTurnstileEvent request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getTryEnterCommandMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.google.protobuf.Empty exitCommand(me.mervap.message.GTurnstileEvent request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getExitCommandMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class CommandSubscriptionServiceFutureStub extends io.grpc.stub.AbstractFutureStub<CommandSubscriptionServiceFutureStub> {
    private CommandSubscriptionServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CommandSubscriptionServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CommandSubscriptionServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> buySubscriptionCommand(
        me.mervap.message.GUpdateSubscription request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getBuySubscriptionCommandMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> extendSubscriptionCommand(
        me.mervap.message.GUpdateSubscription request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getExtendSubscriptionCommandMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> tryEnterCommand(
        me.mervap.message.GTurnstileEvent request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getTryEnterCommandMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> exitCommand(
        me.mervap.message.GTurnstileEvent request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getExitCommandMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_BUY_SUBSCRIPTION_COMMAND = 0;
  private static final int METHODID_EXTEND_SUBSCRIPTION_COMMAND = 1;
  private static final int METHODID_TRY_ENTER_COMMAND = 2;
  private static final int METHODID_EXIT_COMMAND = 3;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final CommandSubscriptionServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(CommandSubscriptionServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_BUY_SUBSCRIPTION_COMMAND:
          serviceImpl.buySubscriptionCommand((me.mervap.message.GUpdateSubscription) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_EXTEND_SUBSCRIPTION_COMMAND:
          serviceImpl.extendSubscriptionCommand((me.mervap.message.GUpdateSubscription) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_TRY_ENTER_COMMAND:
          serviceImpl.tryEnterCommand((me.mervap.message.GTurnstileEvent) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_EXIT_COMMAND:
          serviceImpl.exitCommand((me.mervap.message.GTurnstileEvent) request,
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

  private static abstract class CommandSubscriptionServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    CommandSubscriptionServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return me.mervap.message.Service.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("CommandSubscriptionService");
    }
  }

  private static final class CommandSubscriptionServiceFileDescriptorSupplier
      extends CommandSubscriptionServiceBaseDescriptorSupplier {
    CommandSubscriptionServiceFileDescriptorSupplier() {}
  }

  private static final class CommandSubscriptionServiceMethodDescriptorSupplier
      extends CommandSubscriptionServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    CommandSubscriptionServiceMethodDescriptorSupplier(String methodName) {
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
      synchronized (CommandSubscriptionServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new CommandSubscriptionServiceFileDescriptorSupplier())
              .addMethod(getBuySubscriptionCommandMethod())
              .addMethod(getExtendSubscriptionCommandMethod())
              .addMethod(getTryEnterCommandMethod())
              .addMethod(getExitCommandMethod())
              .build();
        }
      }
    }
    return result;
  }
}
