package com.github.wenj91.dubbo.filter;

import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.alibaba.dubbo.common.Constants.PROVIDER;

@Activate(group = PROVIDER)
public class ProviderFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(ProviderFilter.class);

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        log.info("provider filter!");

        return invoker.invoke(invocation);
    }
}
