package edu.tamu.scholars.discovery.controller.resolver;

import static edu.tamu.scholars.discovery.utility.ArgumentUtility.getFacetArguments;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import edu.tamu.scholars.discovery.controller.argument.FacetArg;

public class FacetArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        ResolvableType resolvableType = ResolvableType.forMethodParameter(parameter);
        return resolvableType.hasGenerics() && FacetArg.class.isAssignableFrom(resolvableType.getGeneric(0).resolve());
    }

    @Override
    public Object resolveArgument(
        MethodParameter parameter,
        @Nullable ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest,
        @Nullable WebDataBinderFactory binderFactory
    ) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        return getFacetArguments(request);
    }

}
