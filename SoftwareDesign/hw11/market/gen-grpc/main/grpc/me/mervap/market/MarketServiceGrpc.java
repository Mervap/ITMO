package me.mervap.market;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.36.0)",
    comments = "Source: service.proto")
public final class MarketServiceGrpc {

  private MarketServiceGrpc() {}

  public static final String SERVICE_NAME = "me.mervap.MarketService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<me.mervap.market.GCompanyDesc,
      me.mervap.market.GCommandResponse> getNewCompanyMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "newCompany",
      requestType = me.mervap.market.GCompanyDesc.class,
      responseType = me.mervap.market.GCommandResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<me.mervap.market.GCompanyDesc,
      me.mervap.market.GCommandResponse> getNewCompanyMethod() {
    io.grpc.MethodDescriptor<me.mervap.market.GCompanyDesc, me.mervap.market.GCommandResponse> getNewCompanyMethod;
    if ((getNewCompanyMethod = MarketServiceGrpc.getNewCompanyMethod) == null) {
      synchronized (MarketServiceGrpc.class) {
        if ((getNewCompanyMethod = MarketServiceGrpc.getNewCompanyMethod) == null) {
          MarketServiceGrpc.getNewCompanyMethod = getNewCompanyMethod =
              io.grpc.MethodDescriptor.<me.mervap.market.GCompanyDesc, me.mervap.market.GCommandResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "newCompany"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  me.mervap.market.GCompanyDesc.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  me.mervap.market.GCommandResponse.getDefaultInstance()))
              .setSchemaDescriptor(new MarketServiceMethodDescriptorSupplier("newCompany"))
              .build();
        }
      }
    }
    return getNewCompanyMethod;
  }

  private static volatile io.grpc.MethodDescriptor<me.mervap.market.GStockDesc,
      me.mervap.market.GCommandResponse> getNewStockMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "newStock",
      requestType = me.mervap.market.GStockDesc.class,
      responseType = me.mervap.market.GCommandResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<me.mervap.market.GStockDesc,
      me.mervap.market.GCommandResponse> getNewStockMethod() {
    io.grpc.MethodDescriptor<me.mervap.market.GStockDesc, me.mervap.market.GCommandResponse> getNewStockMethod;
    if ((getNewStockMethod = MarketServiceGrpc.getNewStockMethod) == null) {
      synchronized (MarketServiceGrpc.class) {
        if ((getNewStockMethod = MarketServiceGrpc.getNewStockMethod) == null) {
          MarketServiceGrpc.getNewStockMethod = getNewStockMethod =
              io.grpc.MethodDescriptor.<me.mervap.market.GStockDesc, me.mervap.market.GCommandResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "newStock"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  me.mervap.market.GStockDesc.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  me.mervap.market.GCommandResponse.getDefaultInstance()))
              .setSchemaDescriptor(new MarketServiceMethodDescriptorSupplier("newStock"))
              .build();
        }
      }
    }
    return getNewStockMethod;
  }

  private static volatile io.grpc.MethodDescriptor<me.mervap.market.GOpStokDesc,
      me.mervap.market.GCommandResponse> getBuyStockMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "buyStock",
      requestType = me.mervap.market.GOpStokDesc.class,
      responseType = me.mervap.market.GCommandResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<me.mervap.market.GOpStokDesc,
      me.mervap.market.GCommandResponse> getBuyStockMethod() {
    io.grpc.MethodDescriptor<me.mervap.market.GOpStokDesc, me.mervap.market.GCommandResponse> getBuyStockMethod;
    if ((getBuyStockMethod = MarketServiceGrpc.getBuyStockMethod) == null) {
      synchronized (MarketServiceGrpc.class) {
        if ((getBuyStockMethod = MarketServiceGrpc.getBuyStockMethod) == null) {
          MarketServiceGrpc.getBuyStockMethod = getBuyStockMethod =
              io.grpc.MethodDescriptor.<me.mervap.market.GOpStokDesc, me.mervap.market.GCommandResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "buyStock"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  me.mervap.market.GOpStokDesc.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  me.mervap.market.GCommandResponse.getDefaultInstance()))
              .setSchemaDescriptor(new MarketServiceMethodDescriptorSupplier("buyStock"))
              .build();
        }
      }
    }
    return getBuyStockMethod;
  }

  private static volatile io.grpc.MethodDescriptor<me.mervap.market.GOpStokDesc,
      me.mervap.market.GCommandResponse> getSellStockMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "sellStock",
      requestType = me.mervap.market.GOpStokDesc.class,
      responseType = me.mervap.market.GCommandResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<me.mervap.market.GOpStokDesc,
      me.mervap.market.GCommandResponse> getSellStockMethod() {
    io.grpc.MethodDescriptor<me.mervap.market.GOpStokDesc, me.mervap.market.GCommandResponse> getSellStockMethod;
    if ((getSellStockMethod = MarketServiceGrpc.getSellStockMethod) == null) {
      synchronized (MarketServiceGrpc.class) {
        if ((getSellStockMethod = MarketServiceGrpc.getSellStockMethod) == null) {
          MarketServiceGrpc.getSellStockMethod = getSellStockMethod =
              io.grpc.MethodDescriptor.<me.mervap.market.GOpStokDesc, me.mervap.market.GCommandResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "sellStock"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  me.mervap.market.GOpStokDesc.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  me.mervap.market.GCommandResponse.getDefaultInstance()))
              .setSchemaDescriptor(new MarketServiceMethodDescriptorSupplier("sellStock"))
              .build();
        }
      }
    }
    return getSellStockMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      me.mervap.market.GCompanyDescList> getCompanyListMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "companyList",
      requestType = com.google.protobuf.Empty.class,
      responseType = me.mervap.market.GCompanyDescList.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      me.mervap.market.GCompanyDescList> getCompanyListMethod() {
    io.grpc.MethodDescriptor<com.google.protobuf.Empty, me.mervap.market.GCompanyDescList> getCompanyListMethod;
    if ((getCompanyListMethod = MarketServiceGrpc.getCompanyListMethod) == null) {
      synchronized (MarketServiceGrpc.class) {
        if ((getCompanyListMethod = MarketServiceGrpc.getCompanyListMethod) == null) {
          MarketServiceGrpc.getCompanyListMethod = getCompanyListMethod =
              io.grpc.MethodDescriptor.<com.google.protobuf.Empty, me.mervap.market.GCompanyDescList>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "companyList"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  me.mervap.market.GCompanyDescList.getDefaultInstance()))
              .setSchemaDescriptor(new MarketServiceMethodDescriptorSupplier("companyList"))
              .build();
        }
      }
    }
    return getCompanyListMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      me.mervap.market.GStockDescList> getStocksListMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "stocksList",
      requestType = com.google.protobuf.Empty.class,
      responseType = me.mervap.market.GStockDescList.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      me.mervap.market.GStockDescList> getStocksListMethod() {
    io.grpc.MethodDescriptor<com.google.protobuf.Empty, me.mervap.market.GStockDescList> getStocksListMethod;
    if ((getStocksListMethod = MarketServiceGrpc.getStocksListMethod) == null) {
      synchronized (MarketServiceGrpc.class) {
        if ((getStocksListMethod = MarketServiceGrpc.getStocksListMethod) == null) {
          MarketServiceGrpc.getStocksListMethod = getStocksListMethod =
              io.grpc.MethodDescriptor.<com.google.protobuf.Empty, me.mervap.market.GStockDescList>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "stocksList"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  me.mervap.market.GStockDescList.getDefaultInstance()))
              .setSchemaDescriptor(new MarketServiceMethodDescriptorSupplier("stocksList"))
              .build();
        }
      }
    }
    return getStocksListMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static MarketServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MarketServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MarketServiceStub>() {
        @java.lang.Override
        public MarketServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MarketServiceStub(channel, callOptions);
        }
      };
    return MarketServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static MarketServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MarketServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MarketServiceBlockingStub>() {
        @java.lang.Override
        public MarketServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MarketServiceBlockingStub(channel, callOptions);
        }
      };
    return MarketServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static MarketServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MarketServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MarketServiceFutureStub>() {
        @java.lang.Override
        public MarketServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MarketServiceFutureStub(channel, callOptions);
        }
      };
    return MarketServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class MarketServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void newCompany(me.mervap.market.GCompanyDesc request,
        io.grpc.stub.StreamObserver<me.mervap.market.GCommandResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getNewCompanyMethod(), responseObserver);
    }

    /**
     */
    public void newStock(me.mervap.market.GStockDesc request,
        io.grpc.stub.StreamObserver<me.mervap.market.GCommandResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getNewStockMethod(), responseObserver);
    }

    /**
     */
    public void buyStock(me.mervap.market.GOpStokDesc request,
        io.grpc.stub.StreamObserver<me.mervap.market.GCommandResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getBuyStockMethod(), responseObserver);
    }

    /**
     */
    public void sellStock(me.mervap.market.GOpStokDesc request,
        io.grpc.stub.StreamObserver<me.mervap.market.GCommandResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSellStockMethod(), responseObserver);
    }

    /**
     */
    public void companyList(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<me.mervap.market.GCompanyDescList> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCompanyListMethod(), responseObserver);
    }

    /**
     */
    public void stocksList(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<me.mervap.market.GStockDescList> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getStocksListMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getNewCompanyMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                me.mervap.market.GCompanyDesc,
                me.mervap.market.GCommandResponse>(
                  this, METHODID_NEW_COMPANY)))
          .addMethod(
            getNewStockMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                me.mervap.market.GStockDesc,
                me.mervap.market.GCommandResponse>(
                  this, METHODID_NEW_STOCK)))
          .addMethod(
            getBuyStockMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                me.mervap.market.GOpStokDesc,
                me.mervap.market.GCommandResponse>(
                  this, METHODID_BUY_STOCK)))
          .addMethod(
            getSellStockMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                me.mervap.market.GOpStokDesc,
                me.mervap.market.GCommandResponse>(
                  this, METHODID_SELL_STOCK)))
          .addMethod(
            getCompanyListMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.google.protobuf.Empty,
                me.mervap.market.GCompanyDescList>(
                  this, METHODID_COMPANY_LIST)))
          .addMethod(
            getStocksListMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.google.protobuf.Empty,
                me.mervap.market.GStockDescList>(
                  this, METHODID_STOCKS_LIST)))
          .build();
    }
  }

  /**
   */
  public static final class MarketServiceStub extends io.grpc.stub.AbstractAsyncStub<MarketServiceStub> {
    private MarketServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MarketServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MarketServiceStub(channel, callOptions);
    }

    /**
     */
    public void newCompany(me.mervap.market.GCompanyDesc request,
        io.grpc.stub.StreamObserver<me.mervap.market.GCommandResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getNewCompanyMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void newStock(me.mervap.market.GStockDesc request,
        io.grpc.stub.StreamObserver<me.mervap.market.GCommandResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getNewStockMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void buyStock(me.mervap.market.GOpStokDesc request,
        io.grpc.stub.StreamObserver<me.mervap.market.GCommandResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getBuyStockMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void sellStock(me.mervap.market.GOpStokDesc request,
        io.grpc.stub.StreamObserver<me.mervap.market.GCommandResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSellStockMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void companyList(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<me.mervap.market.GCompanyDescList> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCompanyListMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void stocksList(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<me.mervap.market.GStockDescList> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getStocksListMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class MarketServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<MarketServiceBlockingStub> {
    private MarketServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MarketServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MarketServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public me.mervap.market.GCommandResponse newCompany(me.mervap.market.GCompanyDesc request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getNewCompanyMethod(), getCallOptions(), request);
    }

    /**
     */
    public me.mervap.market.GCommandResponse newStock(me.mervap.market.GStockDesc request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getNewStockMethod(), getCallOptions(), request);
    }

    /**
     */
    public me.mervap.market.GCommandResponse buyStock(me.mervap.market.GOpStokDesc request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getBuyStockMethod(), getCallOptions(), request);
    }

    /**
     */
    public me.mervap.market.GCommandResponse sellStock(me.mervap.market.GOpStokDesc request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSellStockMethod(), getCallOptions(), request);
    }

    /**
     */
    public me.mervap.market.GCompanyDescList companyList(com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCompanyListMethod(), getCallOptions(), request);
    }

    /**
     */
    public me.mervap.market.GStockDescList stocksList(com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getStocksListMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class MarketServiceFutureStub extends io.grpc.stub.AbstractFutureStub<MarketServiceFutureStub> {
    private MarketServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MarketServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MarketServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<me.mervap.market.GCommandResponse> newCompany(
        me.mervap.market.GCompanyDesc request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getNewCompanyMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<me.mervap.market.GCommandResponse> newStock(
        me.mervap.market.GStockDesc request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getNewStockMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<me.mervap.market.GCommandResponse> buyStock(
        me.mervap.market.GOpStokDesc request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getBuyStockMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<me.mervap.market.GCommandResponse> sellStock(
        me.mervap.market.GOpStokDesc request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSellStockMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<me.mervap.market.GCompanyDescList> companyList(
        com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCompanyListMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<me.mervap.market.GStockDescList> stocksList(
        com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getStocksListMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_NEW_COMPANY = 0;
  private static final int METHODID_NEW_STOCK = 1;
  private static final int METHODID_BUY_STOCK = 2;
  private static final int METHODID_SELL_STOCK = 3;
  private static final int METHODID_COMPANY_LIST = 4;
  private static final int METHODID_STOCKS_LIST = 5;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final MarketServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(MarketServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_NEW_COMPANY:
          serviceImpl.newCompany((me.mervap.market.GCompanyDesc) request,
              (io.grpc.stub.StreamObserver<me.mervap.market.GCommandResponse>) responseObserver);
          break;
        case METHODID_NEW_STOCK:
          serviceImpl.newStock((me.mervap.market.GStockDesc) request,
              (io.grpc.stub.StreamObserver<me.mervap.market.GCommandResponse>) responseObserver);
          break;
        case METHODID_BUY_STOCK:
          serviceImpl.buyStock((me.mervap.market.GOpStokDesc) request,
              (io.grpc.stub.StreamObserver<me.mervap.market.GCommandResponse>) responseObserver);
          break;
        case METHODID_SELL_STOCK:
          serviceImpl.sellStock((me.mervap.market.GOpStokDesc) request,
              (io.grpc.stub.StreamObserver<me.mervap.market.GCommandResponse>) responseObserver);
          break;
        case METHODID_COMPANY_LIST:
          serviceImpl.companyList((com.google.protobuf.Empty) request,
              (io.grpc.stub.StreamObserver<me.mervap.market.GCompanyDescList>) responseObserver);
          break;
        case METHODID_STOCKS_LIST:
          serviceImpl.stocksList((com.google.protobuf.Empty) request,
              (io.grpc.stub.StreamObserver<me.mervap.market.GStockDescList>) responseObserver);
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

  private static abstract class MarketServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    MarketServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return me.mervap.market.Service.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("MarketService");
    }
  }

  private static final class MarketServiceFileDescriptorSupplier
      extends MarketServiceBaseDescriptorSupplier {
    MarketServiceFileDescriptorSupplier() {}
  }

  private static final class MarketServiceMethodDescriptorSupplier
      extends MarketServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    MarketServiceMethodDescriptorSupplier(String methodName) {
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
      synchronized (MarketServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new MarketServiceFileDescriptorSupplier())
              .addMethod(getNewCompanyMethod())
              .addMethod(getNewStockMethod())
              .addMethod(getBuyStockMethod())
              .addMethod(getSellStockMethod())
              .addMethod(getCompanyListMethod())
              .addMethod(getStocksListMethod())
              .build();
        }
      }
    }
    return result;
  }
}
