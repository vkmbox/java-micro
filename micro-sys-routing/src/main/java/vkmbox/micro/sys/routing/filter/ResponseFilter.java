package vkmbox.micro.sys.routing.filter;

import brave.Span;
import brave.Tracer;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ResponseFilter extends ZuulFilter {

    private static final int FILTER_ORDER = 1;
    private static final boolean DO_FILTER = true;
    private static final String POST_TYPE = "post";
    
    private final Tracer tracer;
    
    @Autowired
    public ResponseFilter( Tracer tracer ) {
      this.tracer = tracer;
    }
    
    @Override
    public String filterType() {
        return POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return DO_FILTER;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        Span span = tracer.currentSpan();
        
        if ( span != null ) {
            String traceIdString = span.context().traceIdString();
            log.info("Setting correlation id {}", traceIdString);
            ctx.getResponse().addHeader("tmx-correlation-id", traceIdString);
        }
        
        return null;
    }
    
}
