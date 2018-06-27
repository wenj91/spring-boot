package com.github.wenj91.dubbo.filter;

import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.alibaba.dubbo.common.Constants.CONSUMER;

@Activate(group = CONSUMER)
public class ConsumerFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(ConsumerFilter.class);

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        log.info("consumer filter");

        return invoker.invoke(invocation);
    }
}
