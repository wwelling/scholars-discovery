package edu.tamu.scholars.discovery.discovery.resolver;

import static edu.tamu.scholars.discovery.discovery.utility.ArgumentUtility.getHightlightArgument;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import edu.tamu.scholars.discovery.discovery.argument.HighlightArg;

public class HighlightArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        ResolvableType resolvableType = ResolvableType.forMethodParameter(parameter);
        return HighlightArg.class.isAssignableFrom(resolvableType.toClass());
    }

    @Override
    public Object resolveArgument(
        MethodParameter parameter,
        @Nullable ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest,
        @Nullable WebDataBinderFactory binderFactory
    ) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        return getHightlightArgument(request);
    }

}
